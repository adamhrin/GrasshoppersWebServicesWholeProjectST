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
public class LeaguesDao extends ComponentsDao<League> {

    public List<League> getLeagues() throws CustomException {
        List<League> leaguesList = new ArrayList<League>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null) {
            League unsuccessful = new League();
            unsuccessful.setId(-1);
            leaguesList.add(unsuccessful);
        } else {
            String allLeaguesQuery = "SELECT id_league, name from league";
            ResultSet rAllLeagues = db.selectQuery(conn, allLeaguesQuery);
            try {
                while (rAllLeagues.next()) {
                    League league = new League();
                    
                    int idLeague = rAllLeagues.getInt("id_league");
                    league.setId(idLeague);
                    
                    String name = rAllLeagues.getString("name");
                    league.setName(name);
                    
                    leaguesList.add(league);
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
        return leaguesList;
    }
}
