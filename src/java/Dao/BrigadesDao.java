/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Helpers.MailHelper;
import Managers.DBManager;
import Models.Brigade;
import Models.League;
import Models.Location;
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
public class BrigadesDao {

    public List<Brigade> getBrigadesForPlayer(int idPlayer) throws CustomException {
        List<Brigade> brigadesList = new ArrayList<Brigade>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null) {
            Brigade unsuccessful = new Brigade();
            unsuccessful.setIdBrigade(-1);
            brigadesList.add(unsuccessful);
        } else {
            String allBrigadesQuery = "SELECT brigade.id_brigade, id_league, league.name as league_name, id_location, location.name as location_name, " +
                                      "start_date_time, DATE_FORMAT(start_date_time,'%H:%i:%s') as start_time, " +
                                      "end_date_time, DATE_FORMAT(end_date_time,'%H:%i:%s') as end_time, " +
                                      "(select (count(*) > 0) from brigade_position_player where id_brigade = brigade.id_brigade and id_player = " + idPlayer + ") as is_registered_player " +
                                      "FROM brigade join location using (id_location) join league using (id_league) " + 
                                      "ORDER BY start_date_time";
            ResultSet rAllBrigades = db.selectQuery(conn, allBrigadesQuery);
            try {
                while (rAllBrigades.next()) {
                    Brigade brigade = new Brigade();
                    
                    int idBrigade = rAllBrigades.getInt("id_brigade");
                    brigade.setIdBrigade(idBrigade);
                    
                    Location location = new Location();
                    location.setId(rAllBrigades.getInt("id_location"));
                    location.setName(rAllBrigades.getString("location_name"));
                    brigade.setLocation(location);
                    
                    League league = new League();
                    league.setId(rAllBrigades.getInt("id_league"));
                    league.setName(rAllBrigades.getString("league_name"));
                    brigade.setLeague(league);
                    
                    //START DATE AND TIME
                    String startDateTimeString = rAllBrigades.getString("start_date_time");
                    String startTimeString = rAllBrigades.getString("start_time");
                    brigade.setStartDateTimeString(startDateTimeString);
                    brigade.setStartTimeString(startTimeString);
                    
                    //END DATE AND TIME
                    String endDateTimeString = rAllBrigades.getString("end_date_time");
                    String endTimeString = rAllBrigades.getString("end_time");
                    brigade.setEndDateTimeString(endDateTimeString);
                    brigade.setEndTimeString(endTimeString);
                    
                    boolean isRegisteredPlayer = rAllBrigades.getBoolean("is_registered_player");
                    brigade.setIsRegisteredPlayer(isRegisteredPlayer);
                    
                    PositionsDao positionsDao = new PositionsDao();
                    brigade.setPositions(positionsDao.getPositionsForBrigade(idBrigade, idPlayer, isRegisteredPlayer, db, conn));
                    
                    brigadesList.add(brigade);
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
        return brigadesList;
    }

    public void insertBrigade(Brigade b) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();

        String getBrigadeIdQuery = "SELECT max(id_brigade) as max_id FROM brigade";

        String insertBrigadeQuery = "INSERT INTO brigade (`id_league`, `id_location`, `start_date_time`, `end_date_time`) " +
                "VALUES (?, ?, str_to_date(?, '%d.%m.%Y %H:%i'), str_to_date(?, '%d.%m.%Y %H:%i'))";
        PreparedStatement psInsertBrigade = null;

        String insertPositionOnBrigade = "INSERT INTO brigade_position_player (id_brigade, id_position) VALUES (?, ?)";
        PreparedStatement psInsertPositionOnBrigade = null;
        try {
            psInsertBrigade = conn.prepareStatement(insertBrigadeQuery);
            psInsertBrigade.setInt(1, b.getLeague().getId());
            psInsertBrigade.setInt(2, b.getLocation().getId());
            psInsertBrigade.setString(3, b.getStartDateTimeString());
            psInsertBrigade.setString(4, b.getEndDateTimeString());
            db.insertPreparedStatementQuery(psInsertBrigade);
            
            ResultSet rBrigadeId = db.selectQuery(conn, getBrigadeIdQuery);
            int idBrigade = -1;
            try {
                while (rBrigadeId.next()) {
                    idBrigade = rBrigadeId.getInt("max_id");
                }
            } catch (Exception ex) {
                throw new CustomException(CustomException.ERR_DATA_NOT_FOUND, ex.getMessage());
            } 
            
            psInsertPositionOnBrigade = conn.prepareStatement(insertPositionOnBrigade);
            psInsertPositionOnBrigade.setInt(1, idBrigade);
            for (Position position : b.getPositions()) {
                psInsertPositionOnBrigade.setInt(2, position.getId());
                db.insertPreparedStatementQuery(psInsertPositionOnBrigade);
            }
            
            PlayersDao playersDao = new PlayersDao();
            List<Player> allPlayers = playersDao.getAllPlayers();
            List<String> toEmailList = new ArrayList<>();
            for (Player player : allPlayers) {
                toEmailList.add(player.getEmail());
            }
            
            String subject = MailHelper.NEW_BRIGADE;
            String message = MailHelper.buildMessage(b.getStartDateTimeString(), b.getEndDateTimeString(), b.getLocation().getName());
            MailHelper.sendEmail(toEmailList, subject, message);
            
        } catch (SQLException ex) {
            Logger.getLogger(BrigadesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psInsertBrigade != null) {
                    psInsertBrigade.close();
                }
                if (psInsertPositionOnBrigade != null) {
                    psInsertPositionOnBrigade.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BrigadesDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void updateBrigade(Brigade newBrigade) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        
        int idBrigade = newBrigade.getIdBrigade();
        List<Position> oldBrigadePositions = this.getPositionsOnBrigade(idBrigade, conn, db);
        List<Position> newBrigadePositions = newBrigade.getPositions(); 
        
        List<Position> toInsertPositions = new ArrayList<>();
        List<Position> toDeletePositions = new ArrayList<>();
        
        if (oldBrigadePositions != null && newBrigadePositions != null) {
            for (Position oldPosition : oldBrigadePositions) {
                if (!newBrigadePositions.contains(oldPosition)) {
                    toDeletePositions.add(oldPosition);
                }
            }
            
            for (Position newPosition : newBrigadePositions) {
                if (!oldBrigadePositions.contains(newPosition)) {
                    toInsertPositions.add(newPosition);
                }
            }
        }
        
        PreparedStatement psUpdateBrigade = null;
        String updateBrigadeQuery = "UPDATE brigade SET id_league = ?, id_location = ?, " +
                                    "start_date_time = str_to_date(?, '%d.%m.%Y %H:%i'), end_date_time = str_to_date(?, '%d.%m.%Y %H:%i') " +
                                    "WHERE id_brigade = ?";
        try {
            psUpdateBrigade = conn.prepareStatement(updateBrigadeQuery);
            psUpdateBrigade.setInt(1, newBrigade.getLeague().getId());
            psUpdateBrigade.setInt(2, newBrigade.getLocation().getId());
            psUpdateBrigade.setString(3, newBrigade.getStartDateTimeString());
            psUpdateBrigade.setString(4, newBrigade.getEndDateTimeString());
            psUpdateBrigade.setInt(5, newBrigade.getIdBrigade());
            
            db.insertPreparedStatementQuery(psUpdateBrigade);
            
            PositionsDao positionsDao = new PositionsDao();
            
            positionsDao.deletePositionsForBrigade(toDeletePositions, idBrigade, db, conn);
            positionsDao.insertPositionsForBrigade(toInsertPositions, idBrigade, db, conn);
            
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psUpdateBrigade != null) {
                    psUpdateBrigade.close();
                }
            } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private List<Position> getPositionsOnBrigade(int idBrigade, Connection conn, DBManager db) throws CustomException {
        List<Position> positions = new ArrayList<>();
        String getPositionsQuery = "SELECT id_position FROM brigade_position_player WHERE id_brigade = " + idBrigade;
        ResultSet rsPositions = db.selectQuery(conn, getPositionsQuery);
        try {
            while(rsPositions.next()) {
                Position pos = new Position();
                pos.setId(rsPositions.getInt("id_position"));
                positions.add(pos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BrigadesDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return positions;
    }
}
