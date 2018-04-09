/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Managers.DBManager;
import Models.Category;
import Models.League;
import Models.Location;
import Models.Match;
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
public class MatchesDao {

    public List<Match> getMatchesForPlayer(int idPlayer) throws CustomException {
        List<Match> matchesList = new ArrayList<Match>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null) {
            Match unsuccessful = new Match();
            unsuccessful.setIdMatch(-1);
            matchesList.add(unsuccessful);
        } else {
            String matchesForPlayerQuery = "SELECT m.id_match as id_match, m.id_league as id_league, le.name as league_name, " +
                                            "m.id_location as id_location, lo.name as location_name, " +
                                            "m.id_category as id_category, pic.id_player, c.name as category_name, " +
                                            "m.start_date_time as start_date_time, DATE_FORMAT(m.start_date_time,'%H:%i:%s') as start_time, " +
                                            "m.end_date_time as end_date_time, DATE_FORMAT(m.end_date_time,'%H:%i:%s') as end_time, " +
                                            "m.opponent_name as opponent_name, m.opponent_abbreviation as opponent_abbreviation " +
                                            "FROM `match` m join league le USING (id_league) join location lo USING (id_location) " +
                                            "join category c USING (id_category) join player_in_category pic using (id_category) " +
                                            "where pic.id_player = " + idPlayer + " " + 
                                            "order by m.start_date_time";
            ResultSet rMatchesForPlayer = db.selectQuery(conn, matchesForPlayerQuery);
            try {
                while (rMatchesForPlayer.next()) {
                    Match match = new Match();
                    
                    int idMatch = rMatchesForPlayer.getInt("id_match");
                    match.setIdMatch(idMatch);
                    
                    //START DATE AND TIME
                    String startDateTimeString = rMatchesForPlayer.getString("start_date_time");
                    String startTimeString = rMatchesForPlayer.getString("start_time");
                    match.setStartDateTimeString(startDateTimeString);
                    match.setStartTimeString(startTimeString);
                    
                    //END DATE AND TIME
                    String endDateTimeString = rMatchesForPlayer.getString("end_date_time");
                    String endTimeString = rMatchesForPlayer.getString("end_time");
                    match.setEndDateTimeString(endDateTimeString);
                    match.setEndTimeString(endTimeString);
                    
                    String opponentName = rMatchesForPlayer.getString("opponent_name");
                    String opponentAbbreviation = rMatchesForPlayer.getString("opponent_abbreviation");
                    match.setOpponentName(opponentName);
                    match.setOpponentAbbreviation(opponentAbbreviation);
                    
                    League league = new League();
                    league.setId(rMatchesForPlayer.getInt("id_league"));
                    league.setName(rMatchesForPlayer.getString("league_name"));
                    match.setLeague(league);
                    
                    Location location = new Location();
                    location.setId(rMatchesForPlayer.getInt("id_location"));
                    location.setName(rMatchesForPlayer.getString("location_name"));
                    match.setLocation(location);
                    
                    Category category = new Category();
                    category.setId(rMatchesForPlayer.getInt("id_category"));
                    category.setName(rMatchesForPlayer.getString("category_name"));
                    match.setCategory(category);
                    
                    GoalsDao goalsDao = new GoalsDao();
                    match.setGrassGoals(goalsDao.getGrassGoalsForMatch(idMatch, idPlayer, db, conn));
                    match.setOpponentGoals(goalsDao.getOpponentGoalsForMatch(idMatch, idPlayer, db, conn));
                    
                    matchesList.add(match);
                }
            } catch (SQLException ex) {
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
        return matchesList;
    }

    public Match getMatchById(int idPlayer, int idMatch) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        Match match = new Match();
        if (conn == null) {
            match = new Match();
            match.setIdMatch(-1);
        } else {
            String allMatchesForPlayerQuery = "SELECT m.id_match as id_match, m.id_league as id_league, le.name as league_name, " +
                                            "m.id_location as id_location, lo.name as location_name, " +
                                            "m.id_category as id_category, pic.id_player, c.name as category_name, " +
                                            "m.start_date_time as start_date_time, DATE_FORMAT(m.start_date_time,'%H:%i:%s') as start_time, " +
                                            "m.end_date_time as end_date_time, DATE_FORMAT(m.end_date_time,'%H:%i:%s') as end_time, " +
                                            "m.opponent_name as opponent_name, m.opponent_abbreviation as opponent_abbreviation " +
                                            "FROM `match` m join league le USING (id_league) join location lo USING (id_location) " +
                                            "join category c USING (id_category) join player_in_category pic using (id_category) " +
                                            "where pic.id_player = " + idPlayer + " and m.id_match = " + idMatch;
            ResultSet rAllMatches = db.selectQuery(conn, allMatchesForPlayerQuery);
            try {
                while (rAllMatches.next()) {
                    
                    match.setIdMatch(idMatch);
                    
                    //START DATE AND TIME
                    String startDateTimeString = rAllMatches.getString("start_date_time");
                    String startTimeString = rAllMatches.getString("start_time");
                    match.setStartDateTimeString(startDateTimeString);
                    match.setStartTimeString(startTimeString);
                    
                    //END DATE AND TIME
                    String endDateTimeString = rAllMatches.getString("end_date_time");
                    String endTimeString = rAllMatches.getString("end_time");
                    match.setEndDateTimeString(endDateTimeString);
                    match.setEndTimeString(endTimeString);
                    
                    String opponentName = rAllMatches.getString("opponent_name");
                    String opponentAbbreviation = rAllMatches.getString("opponent_abbreviation");
                    match.setOpponentName(opponentName);
                    match.setOpponentAbbreviation(opponentAbbreviation);
                    
                    League league = new League();
                    league.setId(rAllMatches.getInt("id_league"));
                    league.setName(rAllMatches.getString("league_name"));
                    match.setLeague(league);
                    
                    Location location = new Location();
                    location.setId(rAllMatches.getInt("id_location"));
                    location.setName(rAllMatches.getString("location_name"));
                    match.setLocation(location);
                    
                    Category category = new Category();
                    category.setId(rAllMatches.getInt("id_category"));
                    category.setName(rAllMatches.getString("category_name"));
                    match.setCategory(category);
                    
                    GoalsDao goalsDao = new GoalsDao();
                    match.setGrassGoals(goalsDao.getGrassGoalsForMatch(idMatch, idPlayer, db, conn));
                    match.setOpponentGoals(goalsDao.getOpponentGoalsForMatch(idMatch, idPlayer, db, conn));
                    
                }
            } catch (SQLException ex) {
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
        return match;
    }

    public void insertMatch(Match m) throws CustomException {
        
        DBManager db = new DBManager();
        Connection conn = db.getConnection();

        String insertMatchQuery = "INSERT INTO `match` (id_league, id_location, id_category, start_date_time, end_date_time, opponent_name, opponent_abbreviation) " +
                                  "VALUES (?, ?, ?, str_to_date(?, '%d.%m.%Y %H:%i'), str_to_date(?, '%d.%m.%Y %H:%i'), ?, ?)";
        
        PreparedStatement psInsertMatch = null;
        try {    
            psInsertMatch = conn.prepareStatement(insertMatchQuery);
            psInsertMatch.setInt(1, m.getLeague().getId());
            psInsertMatch.setInt(2, m.getLocation().getId());
            psInsertMatch.setInt(3, m.getCategory().getId());
            psInsertMatch.setString(4, m.getStartDateTimeString());
            psInsertMatch.setString(5, m.getEndDateTimeString());
            psInsertMatch.setString(6, m.getOpponentName());
            psInsertMatch.setString(7, m.getOpponentAbbreviation());
            db.insertPreparedStatementQuery(psInsertMatch);
        } catch (SQLException ex) {
            Logger.getLogger(MatchesDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateMatch(Match match) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        PreparedStatement psUpdateMatch = null;
        String updateMatchQuery = "UPDATE `match` SET id_league = ?, id_location = ?, id_category = ?, " +
                                  "start_date_time = str_to_date(?, '%d.%m.%Y %H:%i'), end_date_time = str_to_date(?, '%d.%m.%Y %H:%i'), " +
                                  "opponent_name = ?, opponent_abbreviation = ? " +
                                  "WHERE id_match = ?";
        try {
            psUpdateMatch = conn.prepareStatement(updateMatchQuery);
            psUpdateMatch.setInt(1, match.getLeague().getId());
            psUpdateMatch.setInt(2, match.getLocation().getId());
            psUpdateMatch.setInt(3, match.getCategory().getId());
            psUpdateMatch.setString(4, match.getStartDateTimeString());
            psUpdateMatch.setString(5, match.getEndDateTimeString());
            psUpdateMatch.setString(6, match.getOpponentName());
            psUpdateMatch.setString(7, match.getOpponentAbbreviation());
            psUpdateMatch.setInt(8, match.getIdMatch());
            
            db.insertPreparedStatementQuery(psUpdateMatch);
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psUpdateMatch != null) {
                    psUpdateMatch.close();
                }
            } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
