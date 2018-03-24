/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Managers.DBManager;
import Models.Component;
import Models.Player;
import Models.Position;
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
public class PositionsDao extends ComponentsDao<Position> {

    public List<Position> getPositionsForBrigade(int idBrigade, int idPlayer, boolean isRegisteredPlayerForBrigade, DBManager db, Connection conn) throws CustomException {
        List<Position> positionsForBrigade = null;
        String allPositionsForBrigadeQuery = "SELECT id_position, position.name, hours, id_player, firstname, nick, surname " +
                                            "FROM brigade_position_player " +
                                            "join brigade using (id_brigade) " +
                                            "join position using (id_position) " +
                                            "left join player using (id_player) WHERE id_brigade = " + idBrigade + " " +
                                            "order by id_position";
        ResultSet rAllPositions = db.selectQuery(conn, allPositionsForBrigadeQuery);
        try {
            positionsForBrigade = new ArrayList<Position>();
            while (rAllPositions.next()) {
                int idPosition = rAllPositions.getInt("id_position");
                String positionName = rAllPositions.getString("name");
                int numOfHours = rAllPositions.getInt("hours");
                
                //Player na pozicii
                //mysli sa ten player ktory odoslal request, cize ten, koho appka to ma zobrazit
                boolean isRegisteredPlayerForPosition = false;
                Player registeredPlayer = null;
                int idPlayerForPosition = rAllPositions.getInt("id_player");
                if (!rAllPositions.wasNull())
                {
                    registeredPlayer = new Player();
                    registeredPlayer.setFirstname(rAllPositions.getString("firstname"));
                    registeredPlayer.setNick(rAllPositions.getString("nick"));
                    registeredPlayer.setSurname(rAllPositions.getString("surname"));
                    if (idPlayer == idPlayerForPosition)
                    {
                        isRegisteredPlayerForPosition = true;
                    }
                }
                Position position = new Position();
                position.setId(idPosition);
                position.setName(positionName);
                position.setNumberOfHours(numOfHours);
                position.setIsRegisteredPlayerForBrigade(isRegisteredPlayerForBrigade);
                position.setIsRegisteredPlayerForPosition(isRegisteredPlayerForPosition);
                position.setRegisteredPlayer(registeredPlayer);
                positionsForBrigade.add(position);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return positionsForBrigade;
    }

    public List<Position> getPositions() throws CustomException {
        List<Position> positionsList = new ArrayList<Position>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null) {
            Position unsuccessful = new Position();
            unsuccessful.setId(-1);
            positionsList.add(unsuccessful);
        } else {
            String allPositionsQuery = "SELECT id_position, name, hours from position";
            ResultSet rAllPositions = db.selectQuery(conn, allPositionsQuery);
            try {
                while (rAllPositions.next()) {
                    Position position = new Position();
                    
                    int idPosition = rAllPositions.getInt("id_position");
                    position.setId(idPosition);
                    
                    String name = rAllPositions.getString("name");
                    position.setName(name);
                    
                    int numOfHours = rAllPositions.getInt("hours");
                    position.setNumberOfHours(numOfHours);
                    
                    positionsList.add(position);
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
        return positionsList;
    }

    @Override
    public void insertComponent(Component component, String positionTableString) throws CustomException {
        Position position = (Position)component;
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        PreparedStatement psInsertPosition = null;
        String insertPositionQuery = "INSERT INTO " + positionTableString + " (name, hours) VALUES (?, ?)";
        try {
            psInsertPosition = conn.prepareStatement(insertPositionQuery);
            psInsertPosition.setString(1, position.getName());
            psInsertPosition.setInt(2, position.getNumberOfHours());
            db.insertPreparedStatementQuery(psInsertPosition);
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psInsertPosition != null) {
                    psInsertPosition.close();
                }
            } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void updateComponent(Component component, String positionTableString) throws CustomException {
        Position position = (Position)component;
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        PreparedStatement psInsertPosition = null;
        String insertPositionQuery = "UPDATE " + positionTableString + " SET name = ?, hours = ? WHERE id_" + positionTableString + " = ?";
        try {
            psInsertPosition = conn.prepareStatement(insertPositionQuery);
            psInsertPosition.setString(1, position.getName());
            psInsertPosition.setInt(2, position.getNumberOfHours());
            psInsertPosition.setInt(3, position.getId());
            db.insertPreparedStatementQuery(psInsertPosition);
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psInsertPosition != null) {
                    psInsertPosition.close();
                }
            } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void registerPlayerOnPosition(int idPlayer, int idBrigade, int idPosition) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        PreparedStatement psRegisterPlayerOnPosition = null;
        String updatePositionQuery = "UPDATE brigade_position_player SET id_player = ? WHERE id_brigade = ? AND id_position = ?";
        try {
            psRegisterPlayerOnPosition = conn.prepareStatement(updatePositionQuery);
            psRegisterPlayerOnPosition.setInt(1, idPlayer);
            psRegisterPlayerOnPosition.setInt(2, idBrigade);
            psRegisterPlayerOnPosition.setInt(3, idPosition);
            db.insertPreparedStatementQuery(psRegisterPlayerOnPosition);
        } catch (SQLException ex) {
            Logger.getLogger(PositionsDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psRegisterPlayerOnPosition != null) {
                    psRegisterPlayerOnPosition.close();
                }
            } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void unregisterPlayerOnPosition(int idPlayer, int idBrigade, int idPosition) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        PreparedStatement psRegisterPlayerOnPosition = null;
        String updatePositionQuery = "UPDATE brigade_position_player SET id_player = null WHERE id_brigade = ? AND id_position = ?";
        try {
            psRegisterPlayerOnPosition = conn.prepareStatement(updatePositionQuery);
            psRegisterPlayerOnPosition.setInt(1, idBrigade);
            psRegisterPlayerOnPosition.setInt(2, idPosition);
            db.insertPreparedStatementQuery(psRegisterPlayerOnPosition);
        } catch (SQLException ex) {
            Logger.getLogger(PositionsDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psRegisterPlayerOnPosition != null) {
                    psRegisterPlayerOnPosition.close();
                }
            } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void deletePositionsForBrigade(List<Position> toDeletePositions, int idBrigade, DBManager db, Connection conn) throws CustomException {
        PreparedStatement psDeletePosition = null;
        try {
            String deletePositionQuery = "DELETE FROM brigade_position_player WHERE id_brigade = ? AND id_position = ?" ;
            psDeletePosition = conn.prepareStatement(deletePositionQuery);
            psDeletePosition.setInt(1, idBrigade);
            for (Position toDeletePosition : toDeletePositions) {
                psDeletePosition.setInt(2, toDeletePosition.getId());
                db.deletePreparedStatementQuery(psDeletePosition);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (psDeletePosition != null) {
                try {
                    psDeletePosition.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void insertPositionsForBrigade(List<Position> toInsertPositions, int idBrigade, DBManager db, Connection conn) throws CustomException {
        PreparedStatement psInsertPosition = null;
        try {
            String insertPositionQuery = "INSERT INTO brigade_position_player (id_brigade, id_position) VALUES (?, ?)";
            psInsertPosition = conn.prepareStatement(insertPositionQuery);
            psInsertPosition.setInt(1, idBrigade);
            for (Position toInsertPosition : toInsertPositions) {
                psInsertPosition.setInt(2, toInsertPosition.getId());
                db.insertPreparedStatementQuery(psInsertPosition);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (psInsertPosition != null) {
                try {
                    psInsertPosition.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
