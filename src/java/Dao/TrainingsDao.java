/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Helpers.Evaluator;
import Managers.DBManager;
import Models.AcceptedTrainingOptions;
import Models.Category;
import Models.Location;
import Models.Player;
import Models.Training;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;

/**
 *
 * @author Adam
 */
public class TrainingsDao {
    Logger logger = Logger.getLogger("erfe");
    
    public List<Training> getTrainingsForPlayer(int idPlayer) throws CustomException {
        List<Training> trainingsList = new ArrayList<Training>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        ResultSet rTrainingsForPlayer = null;
        if (conn == null) {
            Training unsuccessful = new Training();
            unsuccessful.setIdTraining(-1);
            trainingsList.add(unsuccessful);
        } else {
            String trainingsForPlayerQuery = "SELECT id_training, id_location, name, " +
                                             "start_date_time, DATE_FORMAT(start_date_time,'%H:%i:%s') as start_time, " +
                                             "end_date_time, DATE_FORMAT(end_date_time,'%H:%i:%s') as end_time, " +
                                             "accepts " +
                                             "FROM training join location using (id_location) join player_on_training using (id_training) " +
                                             "WHERE id_player = " + idPlayer + " " +
                                             "ORDER BY start_date_time";
            rTrainingsForPlayer = db.selectQuery(conn, trainingsForPlayerQuery);
            try {
                while (rTrainingsForPlayer.next()) {
                    Training training = new Training();
                    
                    int idTraining = rTrainingsForPlayer.getInt("id_training");
                    training.setIdTraining(idTraining);
                    
                    //START DATE AND TIME
                    String startDateTimeString = rTrainingsForPlayer.getString("start_date_time");
                    String startTimeString = rTrainingsForPlayer.getString("start_time");
                    training.setStartDateTimeString(startDateTimeString);
                    training.setStartTimeString(startTimeString);
                    
                    //END DATE AND TIME
                    String endDateTimeString = rTrainingsForPlayer.getString("end_date_time");
                    String endTimeString = rTrainingsForPlayer.getString("end_time");
                    training.setEndDateTimeString(endDateTimeString);
                    training.setEndTimeString(endTimeString);
                    
                    
                    AcceptedTrainingOptions ato = Evaluator.evaluateAcceptedTraining(rTrainingsForPlayer, "accepts");
                    training.setAcceptedByPlayer(ato);
                    
                    Location location = new Location();
                    location.setId(rTrainingsForPlayer.getInt("id_location"));
                    location.setName(rTrainingsForPlayer.getString("name"));
                    training.setLocation(location);
                    
                    CategoriesDao categoriesDao = new CategoriesDao();
                    training.setCategories(categoriesDao.getCategoriesForTraining(idTraining, db, conn));
                    
                    PlayersDao playersDao = new PlayersDao();
                    training.setInvitedPlayers(playersDao.getInvitedPlayersForTraining(idTraining, db, conn));
                    
                    trainingsList.add(training);
                }
            } catch (SQLException ex) {
                throw new CustomException(CustomException.ERR_DATA_NOT_FOUND, ex.getMessage());
            }        
        }
        try {  
            if (conn != null) {
                conn.close();
            }
            if (rTrainingsForPlayer != null) {
                rTrainingsForPlayer.close();
            }   
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_DB_ENTITY_CLOSE, ex.getMessage());
        }
        return trainingsList;
    }

    public void insertTraining(Training t) throws CustomException, IOException {
        
        FileHandler fh = new FileHandler("d:\\Pracovny\\Skola\\FRI\\TRETIAK\\Bakalarka\\GrasshoppersWebServices\\log.log");  
        logger.addHandler(fh);
        
        logger.info("Zaciatok metody insertTraining");
        
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        
        String getTrainingsIdQuery = "SELECT max(id_training) as max_id FROM training";
        
        String insertTrainingQuery = "INSERT INTO `training`(`id_location`, `start_date_time`, `end_date_time`) " +
                                     "VALUES (?, str_to_date(?, '%d.%m.%Y %H:%i'), str_to_date(?, '%d.%m.%Y %H:%i'))";
        PreparedStatement psInsertTraining = null;
        
        String insertCategoryOnTraining = "INSERT INTO category_on_training (id_category, id_training) VALUES (?, ?)";
        PreparedStatement psInsertCategoryOnTraining = null;
        
        String insertPlayerOnTraining = "INSERT INTO player_on_training (id_player, id_training) VALUES (?, ?)";
        PreparedStatement psInsertPlayerOnTraining = null;
        
        try {
            psInsertTraining = conn.prepareStatement(insertTrainingQuery);
            psInsertTraining.setInt(1, t.getLocation().getId());
            psInsertTraining.setString(2, t.getStartDateTimeString());
            psInsertTraining.setString(3, t.getEndDateTimeString());
            db.insertPreparedStatementQuery(psInsertTraining);
            
            //ZISKANIE ID TRENINGU
            ResultSet rTrainingsId = db.selectQuery(conn, getTrainingsIdQuery);
            int idTraining = -1;
            logger.info("idTraining = " + idTraining);
            try {
                while (rTrainingsId.next()) {
                    logger.info("v next pred getint = " + idTraining);
                    idTraining = rTrainingsId.getInt("max_id");
                    logger.info("v next po getint = " + idTraining);
                }
            } catch (Exception ex) {
                throw new CustomException(CustomException.ERR_DATA_NOT_FOUND, ex.getMessage());
            } 
            
            String playersInCategoryQuery = "SELECT DISTINCT id_player FROM player_in_category WHERE";
            //VKLADANIE DO CATEGORY_ON_TRAINING
            psInsertCategoryOnTraining = conn.prepareStatement(insertCategoryOnTraining);
            psInsertCategoryOnTraining.setInt(2, idTraining);
            for (Category category : t.getCategories()) {
                psInsertCategoryOnTraining.setInt(1, category.getId());
                db.insertPreparedStatementQuery(psInsertCategoryOnTraining);
                
                //PRE CAST VKLADANIE DO PLAYER_ON_TRAINING
                playersInCategoryQuery += " id_category = " + category.getId() + " OR";
            }
            
            //VKLADANIE DO PLAYER_ON_TRAINING
            playersInCategoryQuery = playersInCategoryQuery.substring(0, playersInCategoryQuery.length() - 2);
            ResultSet rPlayersInCategories = db.selectQuery(conn, playersInCategoryQuery);
            List<Integer> playersIdsInCategories = new ArrayList<Integer>();
            try {
                while(rPlayersInCategories.next()) {
                    playersIdsInCategories.add(rPlayersInCategories.getInt("id_player"));
                }
            } catch (Exception ex) {
                throw new CustomException(CustomException.ERR_DATA_NOT_FOUND, ex.getMessage());
            } 
            
            psInsertPlayerOnTraining = conn.prepareStatement(insertPlayerOnTraining);
            psInsertPlayerOnTraining.setInt(2, idTraining);
            for (Integer id : playersIdsInCategories) {
                psInsertPlayerOnTraining.setInt(1, id);
                db.insertPreparedStatementQuery(psInsertPlayerOnTraining);
            }
            
        } catch (Exception ex) {
            
            //Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
            
            logger.info(ex.getMessage());
            throw new CustomException(CustomException.ERR_PREPARE_STATEMENT, ex.getMessage());
        } finally {
            try {
                if (psInsertTraining != null) {
                    psInsertTraining.close();
                }
                if (psInsertCategoryOnTraining != null) {
                    psInsertCategoryOnTraining.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
                throw new CustomException(CustomException.ERR_DB_ENTITY_CLOSE, ex.getMessage());
            }
        }
    }
    
    //vymazanie a pridanie treningov podla kategorii ktore sa maju vymazat/pridat
    void updateTrainingsForPlayer(List<Category> toDeleteCategories, List<Category> toInsertCategories, List<Category> oldCategories, List<Category> newCategories, int idPlayer, DBManager db, Connection conn) throws CustomException {
        
        //DELETE
        //osetrit ked je newCategories prazdne - vymazu sa mu vsetky riadky v player_on_training
        //ked toDeleteCategories je prazdne - nic nevymazavam
        
        //INSERT
        //ak je oldCategories prazdne, druhu cast selectu nepridavame do uvahy
        //ked toInsert je prazdne - nic nepridavame
        
        //DELETE
        String deleteTrainingsQuery = "";
        if (!toDeleteCategories.isEmpty()) {
            if (!newCategories.isEmpty()) {
                deleteTrainingsQuery = "DELETE FROM player_on_training " +
                                       "WHERE id_player = " + idPlayer + " " +
                                       "AND id_training IN (SELECT DISTINCT id_training " +
                                       "		    FROM category_on_training JOIN player_in_category USING (id_category) " +
                                       "                    WHERE id_player = " + idPlayer + " " +
                                       "		    AND id_category IN (";
                for (Category toDeleteCategory : toDeleteCategories) {
                    deleteTrainingsQuery += toDeleteCategory.getId() + ",";
                }
                deleteTrainingsQuery = deleteTrainingsQuery.substring(0, deleteTrainingsQuery.length() - 1);//odrezem poslednu ciarku
                deleteTrainingsQuery += ") ) " +
                                        "AND id_training NOT IN (SELECT DISTINCT id_training " +
                                        "		    	 FROM category_on_training JOIN player_in_category USING (id_category) " +
//                                        "                        WHERE id_player = " + idPlayer + " " +
                                        "		   	 WHERE id_category IN (";
                for (Category newCategory : newCategories) {
                    deleteTrainingsQuery += newCategory.getId() + ",";
                }
                deleteTrainingsQuery = deleteTrainingsQuery.substring(0, deleteTrainingsQuery.length() - 1);
                deleteTrainingsQuery += ") ) ";
            } else {
                deleteTrainingsQuery = "DELETE FROM player_on_training WHERE id_player = " + idPlayer;
            }
        }
        
        if (!deleteTrainingsQuery.isEmpty()) {
            db.deleteQuery(conn, deleteTrainingsQuery);
        }
        
        //INSERT
        String selectTrainingsQuery = "";
        if (!toInsertCategories.isEmpty()) {
            //prva cast SELECTU
            selectTrainingsQuery = "SELECT id_training FROM training " +
                                   "WHERE id_training IN (SELECT DISTINCT id_training " +
                                   "                      FROM category_on_training " +
                                   "                      WHERE id_category IN (";
            for (Category toInsertCategory : toInsertCategories) {
                selectTrainingsQuery += toInsertCategory.getId() + ",";
            }
            selectTrainingsQuery = selectTrainingsQuery.substring(0, selectTrainingsQuery.length() - 1);
            selectTrainingsQuery += ") ) ";
            
            //druha cast selectu
            if (!oldCategories.isEmpty()) {
                selectTrainingsQuery += "AND id_training NOT IN (SELECT DISTINCT id_training " +
                                        "		    	 FROM category_on_training JOIN player_in_category USING (id_category) " +
                                        "    			 WHERE id_player = " + idPlayer + " " +
                                        "		   	 AND id_category IN (";
                for (Category oldCategory : oldCategories) {
                    selectTrainingsQuery += oldCategory.getId() + ",";
                }
                selectTrainingsQuery = selectTrainingsQuery.substring(0, selectTrainingsQuery.length() - 1);
                selectTrainingsQuery += ") ) ";
            }
        }
        
        if (!selectTrainingsQuery.isEmpty()) {
            ResultSet rTrainings = db.selectQuery(conn, selectTrainingsQuery);
            String insertTrainingsQuery = "INSERT INTO player_on_training (id_player, id_training) VALUES (?, ?)";
            PreparedStatement psInsertTrainings = null;
            try {
                psInsertTrainings = conn.prepareStatement(insertTrainingsQuery);
                psInsertTrainings.setInt(1, idPlayer);
                while(rTrainings.next()) {
                    psInsertTrainings.setInt(2, rTrainings.getInt("id_training"));
                    db.insertPreparedStatementQuery(psInsertTrainings);
                }
            } catch (SQLException ex) {
                Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (psInsertTrainings != null) {
                        psInsertTrainings.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void updatePlayerOnTraining(int idPlayer, int idTraining, String acceptString) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        PreparedStatement psUpdatePlayerOnTraining = null;
        String updatePlayerOnTrainingQuery = "UPDATE player_on_training SET accepts = " + acceptString + " WHERE id_player = ? AND id_training = ?";
        try {
            psUpdatePlayerOnTraining = conn.prepareStatement(updatePlayerOnTrainingQuery);
            psUpdatePlayerOnTraining.setInt(1, idPlayer);
            psUpdatePlayerOnTraining.setInt(2, idTraining);
            db.insertPreparedStatementQuery(psUpdatePlayerOnTraining);
        } catch (SQLException ex) {
            Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psUpdatePlayerOnTraining != null) {
                    psUpdatePlayerOnTraining.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}