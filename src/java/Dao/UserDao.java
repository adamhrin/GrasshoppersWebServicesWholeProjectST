/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

/**
 *
 * @author Adam
 */
import CustomException.CustomException;
import Managers.DBManager;
import Models.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 
import java.util.List;  
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao { 
    
    public List<User> getAllUsers() throws CustomException{       
        List<User> userList = null; 
        /*try { */
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null)
        {
            User user = new User(1, "Neuspesna", "Connection"); 
            userList = new ArrayList<User>(); 
            userList.add(user);
        } 
        else
        {
            //String query = "SELECT * FROM semka_user JOIN semka_user_on_training USING (id_user) WHERE id_user = 13";
            String query = "SELECT * FROM player";
            ResultSet result = db.selectQuery(conn, query);
            try {
                userList = new ArrayList<User>();
                while (result.next())
                {
                    int id_user = result.getInt("id_player");
                    String firstname = result.getString("firstname");
                    User user = new User(id_user, firstname, "cauko");
                    userList.add(user);
                }
//                User user = null;
//                while (result.next())
//                {
//                    if (user == null)
//                    {
//                        int id_user = result.getInt("id_user");
//                        String firstname = result.getString("firstname");
//                        user = new User(id_user, firstname, "cauko");
//                        
//                    }
//                    int id_training = result.getInt("id_training");
//                    Training training = new Training();
//                    training.setIdTraining(id_training);
//                    user.addTraining(training);
//                    
//                }
                
                //userList.add(user);
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }        
        }
        try {  
            if(conn != null)
                conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userList;
   } 
}
