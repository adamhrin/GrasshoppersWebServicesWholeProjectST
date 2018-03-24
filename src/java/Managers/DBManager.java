/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

/**
 *
 * @author Adam
 */
import CustomException.CustomException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Adam
 */
public class DBManager {
    public Connection getConnection()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //Connection conn = DriverManager.getConnection("jdbc:mysql://172.30.203.72:3306/grassDb", "root", "root");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/grasshoppers", "root", "chester9397");
            return conn;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public ResultSet selectQuery(Connection conn, String query) throws CustomException {
        ResultSet resultSet = null;
        Statement statement = null;
        try 
        {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_SELECT, ex.getMessage());
//        } finally {
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//                throw new CustomException(CustomException.ERR_DB_ENTITY_CLOSE, ex.getMessage());
//            }
        }
        return resultSet;
    }
    
    public ResultSet selectPreparedStatementQuery(PreparedStatement s) throws CustomException {
        try {
            return s.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_SELECT, ex.getMessage());
        } 
    }

    public void insertPreparedStatementQuery(PreparedStatement s) throws CustomException {
        try {
            s.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_UPDATE_INSERT, ex.getMessage());
        } 
    }

    public void deletePreparedStatementQuery(PreparedStatement s) throws CustomException {
        try {
            s.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_DELETE, ex.getMessage());
        } 
    }

    public void deleteQuery(Connection conn, String query) throws CustomException {
        Statement statement = null;
        try 
        {
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_DELETE, ex.getMessage());
//        } finally {
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//                throw new CustomException(CustomException.ERR_DB_ENTITY_CLOSE, ex.getMessage());
//            }
        }
    }

    public void insertQuery(Connection conn, String query) throws CustomException {
        Statement statement = null;
        try 
        {
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_UPDATE_INSERT, ex.getMessage());
//        } finally {
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//                throw new CustomException(CustomException.ERR_DB_ENTITY_CLOSE, ex.getMessage());
//            }
        }
    }
}
