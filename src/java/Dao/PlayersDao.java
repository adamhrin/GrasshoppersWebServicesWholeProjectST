/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Helpers.Evaluator;
import Helpers.PasswordEvaluator;
import Managers.DBManager;
import Models.AcceptedTrainingOptions;
import Models.Category;
import Models.Player;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam
 */
public class PlayersDao {
    public List<Player> getInvitedPlayersForTraining(int idTraining, DBManager db, Connection conn) throws CustomException {
        List<Player> invitedPlayersForTraining = null;
        String invitedPlayersForTrainingQuery = "SELECT id_player, firstname, nick, surname, number, accepts " +
                                                "FROM player join player_on_training using (id_player) " +
                                                "WHERE id_training = " + idTraining + " " +
                                                "ORDER BY accepts desc";
        ResultSet rInvitedPlayers = db.selectQuery(conn, invitedPlayersForTrainingQuery);
        try {
            invitedPlayersForTraining = new ArrayList<Player>();
            while (rInvitedPlayers.next()) {
                int idPlayer = rInvitedPlayers.getInt("id_player");
                String firstname = rInvitedPlayers.getString("firstname");
                String nick = rInvitedPlayers.getString("nick");
                String surname = rInvitedPlayers.getString("surname");
                int number = rInvitedPlayers.getInt("number");
                //int playerAcceptedTraining = Evaluator.evaluateAcceptedTraining(rInvitedPlayers, "accepts");
                AcceptedTrainingOptions ato = Evaluator.evaluateAcceptedTraining(rInvitedPlayers, "accepts");
                
                Player invitedPlayer = new Player();
                invitedPlayer.setIdPlayer(idPlayer);
                invitedPlayer.setFirstname(firstname);
                invitedPlayer.setNick(nick);
                invitedPlayer.setSurname(surname);
                invitedPlayer.setNumber(number);
                //invitedPlayer.setPlayerAcceptedTraining(playerAcceptedTraining);
                invitedPlayer.setPlayerAcceptedTraining(ato);
                invitedPlayersForTraining.add(invitedPlayer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_DATA_NOT_FOUND, ex.getMessage());
        }  
        return invitedPlayersForTraining;
    }

    public List<Player> getPlayersForCategory(int idCategory) throws CustomException {
        List<Player> playersForCategoryList = new ArrayList<Player>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null) {
            Player unsuccessful = new Player();
            unsuccessful.setIdPlayer(-1);
            playersForCategoryList.add(unsuccessful);
        } else {
            String trainingsForPlayerQuery = "SELECT id_player, firstname, nick, surname, number " +
                                             "FROM category join player_in_category using (id_category) join player using (id_player) " +
                                             "where id_category = " + idCategory + " " +
                                             "order by number";
            ResultSet rPlayersForCategory = db.selectQuery(conn, trainingsForPlayerQuery);
            try {
                playersForCategoryList = new ArrayList<Player>();
                while (rPlayersForCategory.next()) {
                    int idPlayer = rPlayersForCategory.getInt("id_player");
                    String firstname = rPlayersForCategory.getString("firstname");
                    String nick = rPlayersForCategory.getString("nick");
                    String surname = rPlayersForCategory.getString("surname");
                    int number = rPlayersForCategory.getInt("number");

                    Player player = new Player();
                    player.setIdPlayer(idPlayer);
                    player.setFirstname(firstname);
                    player.setNick(nick);
                    player.setSurname(surname);
                    player.setNumber(number);
                    playersForCategoryList.add(player);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
                throw new CustomException(CustomException.ERR_DATA_NOT_FOUND, ex.getMessage());
            }  
        }
        try {  
            if(conn != null)
                conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_DB_ENTITY_CLOSE, ex.getMessage());
        }
        return playersForCategoryList;
    }

    public void registerPlayer(Player p) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        String getPlayerQuery = "SELECT id_player, password FROM player WHERE email = ?";
        PreparedStatement psGetPlayer = null;
        String hashedPwdFromDb = "";
        try {
            psGetPlayer = conn.prepareStatement(getPlayerQuery);
            psGetPlayer.setString(1, p.getEmail());
            
            ResultSet rPlayer = db.selectPreparedStatementQuery(psGetPlayer);
            
            if (rPlayer.isBeforeFirst()) { // su tam data => player s takymto mejlom existuje (minimalne 1)
                while(rPlayer.next()) {
                    hashedPwdFromDb = rPlayer.getString("password");
                    try {
                        if (PasswordEvaluator.verifyPassword(p.getPassword(), hashedPwdFromDb)) {
                            throw new CustomException(CustomException.ERR_CONFLICT, "Player with email " + p.getEmail() + " and password " + hashedPwdFromDb + " already exists");
                        }
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                        Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.insertPlayer(conn, db, p, hashedPwdFromDb);
            } else { // player s takym mejlom neexistuje
                String hashedPwd = "";
                try {
                    hashedPwd = PasswordEvaluator.hashPassword(p.getPassword());
                } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                    Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.insertPlayer(conn, db, p, hashedPwd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Player loginPlayer(Player playerToLogin) throws CustomException {
        Player playerToReturn = null;
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        String getPlayerQuery = "SELECT id_player, password, id_admin_level FROM player WHERE email = ?";
        PreparedStatement psGetPlayer = null;
        String hashedPwd = "";
        try {
            psGetPlayer = conn.prepareStatement(getPlayerQuery);
            psGetPlayer.setString(1, playerToLogin.getEmail());
            ResultSet rPlayer = db.selectPreparedStatementQuery(psGetPlayer);
            
            if (!rPlayer.isBeforeFirst()) {  //no data => player neexistuje alebo su zle prihl. udaje
                try {
                    hashedPwd = PasswordEvaluator.hashPassword(playerToLogin.getPassword());
                } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                    Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
                }
                throw new CustomException(CustomException.ERR_BAD_LOGIN_DATA, "Player with email " + playerToLogin.getEmail() + " and password " + hashedPwd + " not in db (bad email)");
            } else { //player najdeny
                while (rPlayer.next()) {
                    String hashedPwdFromDb = rPlayer.getString("password");
                    try {
                        if (PasswordEvaluator.verifyPassword(playerToLogin.getPassword(), hashedPwdFromDb)) {
                            playerToReturn = new Player();
                            int idPlayer = rPlayer.getInt("id_player");
                            playerToReturn.setIdPlayer(idPlayer);
                            playerToReturn.setAdminLevel(rPlayer.getInt("id_admin_level"));
                            return playerToReturn;
                        }
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                        Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new CustomException(CustomException.ERR_BAD_LOGIN_DATA, "Player with email " + playerToLogin.getEmail() + " and password " + hashedPwd + " not in db (bad pwd)");
    }

    private void insertPlayer(Connection conn, DBManager db, Player p, String hashedPwd) throws SQLException, CustomException {
        String insertPlayer = "INSERT INTO player (firstname, nick, surname, password, email, number) " +
                              "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement psInsertPlayer = null;
        psInsertPlayer = conn.prepareStatement(insertPlayer);
        psInsertPlayer.setString(1, p.getFirstname());
        psInsertPlayer.setString(2, p.getNick());
        psInsertPlayer.setString(3, p.getSurname());
        psInsertPlayer.setString(4, hashedPwd);
        psInsertPlayer.setString(5, p.getEmail());
        psInsertPlayer.setInt(6, p.getNumber());
        db.insertPreparedStatementQuery(psInsertPlayer);
    }
    
    public Player getPlayer(int id) throws CustomException {
        Player player = null;
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        PreparedStatement psGetPlayer = null;
        try {
            String playerQuery = "SELECT `id_player`, `firstname`, `nick`, `surname`, `email`, `number`, `id_admin_level` FROM `player` WHERE id_player = ?";
            
            psGetPlayer = conn.prepareStatement(playerQuery);
            psGetPlayer.setInt(1, id);
            ResultSet rPlayer = db.selectPreparedStatementQuery(psGetPlayer);
            while(rPlayer.next()) {
                player = new Player();
                player.setIdPlayer(id);
                player.setFirstname(rPlayer.getString("firstname"));
                player.setNick(rPlayer.getString("nick"));
                player.setSurname(rPlayer.getString("surname"));
                player.setEmail(rPlayer.getString("email"));
                player.setNumber(rPlayer.getInt("number"));
                player.setAdminLevel(rPlayer.getInt("id_admin_level"));
                
                CategoriesDao categoriesDao = new CategoriesDao();
                player.setCategories(categoriesDao.getCategoriesForPlayer(id, db, conn));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
        } try {
            if (psGetPlayer != null) {
                psGetPlayer.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_DB_ENTITY_CLOSE, ex.getMessage());
        }
        return player;
    }
    
    public boolean existsPlayer(String email, String password) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        String getPlayerQuery = "SELECT password FROM player WHERE email = ?";
        PreparedStatement psGetPlayer = null;
        String hashedPwdFromDb = "";
        try {
            psGetPlayer = conn.prepareStatement(getPlayerQuery);
            psGetPlayer.setString(1, email);
            ResultSet rPlayer = db.selectPreparedStatementQuery(psGetPlayer);
            if (rPlayer.isBeforeFirst()) { // su tam data => player s takymto mejlom existuje (minimalne 1)
                while(rPlayer.next()) {
                    hashedPwdFromDb = rPlayer.getString("password");
                    try {
                        if (PasswordEvaluator.verifyPassword(password, hashedPwdFromDb)) {
                            return true; //existuje taky player s takym emailom a pwd
                        }
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                        Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psGetPlayer != null) {
                    psGetPlayer.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public void updatePlayer(Player newPlayer) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        int idPlayer = newPlayer.getIdPlayer();
        Player oldPlayer = this.getPlayer(idPlayer);
        List<Category> oldCategories = oldPlayer.getCategories(); 
        List<Category> newCategories = newPlayer.getCategories(); 
        
        List<Category> toInsertCategories = new ArrayList<>();
        List<Category> toDeleteCategories = new ArrayList<>();
        
        if (oldCategories != null && newCategories != null) {
            for (Category oldCategory : oldCategories) {
                if (!newCategories.contains(oldCategory)) {
                    toDeleteCategories.add(oldCategory);
                }
            }
            
            for (Category newCategory : newCategories) {
                if (!oldCategories.contains(newCategory)) {
                    toInsertCategories.add(newCategory);
                }
            }
        }
        
        String updatePlayerQuery = "UPDATE player SET firstname = ?, nick = ?, surname = ?, email = ?, number = ? WHERE id_player = ?";
        
        PreparedStatement psUpdatePlayer = null;
        try {
            psUpdatePlayer = conn.prepareStatement(updatePlayerQuery);
            psUpdatePlayer.setString(1, newPlayer.getFirstname());
            psUpdatePlayer.setString(2, newPlayer.getNick());
            psUpdatePlayer.setString(3, newPlayer.getSurname());
            psUpdatePlayer.setString(4, newPlayer.getEmail());
            psUpdatePlayer.setInt(5, newPlayer.getNumber());
            psUpdatePlayer.setInt(6, idPlayer);
            db.insertPreparedStatementQuery(psUpdatePlayer);
        } catch (SQLException ex) {
            Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CategoriesDao categoriesDao = new CategoriesDao();
        TrainingsDao trainingsDao = new TrainingsDao();
        
        //vymazanie a pridanie treningov podla kategorii ktore sa maju vymazat/pridat
        trainingsDao.updateTrainingsForPlayer(toDeleteCategories, toInsertCategories, oldCategories, newCategories, idPlayer, db, conn);
                
        //vymazanie a pridanie kategorii podla volby playera
        categoriesDao.deleteCategoriesForPlayer(toDeleteCategories, idPlayer, db, conn);
        categoriesDao.insertCategoriesForPlayer(toInsertCategories, idPlayer, db, conn);
        
        
        try {
            if (conn != null) {
                conn.close();
            }
            if (psUpdatePlayer != null) {
                psUpdatePlayer.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
