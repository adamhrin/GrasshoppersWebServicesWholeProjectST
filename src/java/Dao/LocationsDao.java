/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Managers.DBManager;
import Models.Location;
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
public class LocationsDao extends ComponentsDao<Location> {
    
    public List<Location> getLocations() throws CustomException {
        //return (List<Category>)super.getComponents("id_category", "category");
        List<Location> locationsList = new ArrayList<Location>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null) {
            Location unsuccessful = new Location();
            unsuccessful.setId(-1);
            locationsList.add(unsuccessful);
        } else {
            String allLocationsQuery = "SELECT id_location, name from location";
            ResultSet rAllLocations = db.selectQuery(conn, allLocationsQuery);
            try {
                while (rAllLocations.next()) {
                    Location location = new Location();
                    
                    int idLocation = rAllLocations.getInt("id_location");
                    location.setId(idLocation);
                    
                    String name = rAllLocations.getString("name");
                    location.setName(name);
                    
                    locationsList.add(location);
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
        return locationsList;
    }
}
