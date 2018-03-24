/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Managers.DBManager;
import Models.Category;
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
public class CategoriesDao extends ComponentsDao<Category> {
    
    public List<Category> getCategoriesForTraining(int idTraining, DBManager db, Connection conn) throws CustomException {
        
        String categoriesForTrainingQuery = "SELECT id_category, name " +
                                               "FROM category join category_on_training using (id_category) " +
                                               "WHERE id_training = " + idTraining + " " +
                                               "ORDER BY id_category";
        return this.getCategoriesHelper(db, conn, categoriesForTrainingQuery);
    }
    
    public List<Category> getCategories() throws CustomException {
//        return (List<Category>)super.getComponents("id_category", "category");
        List<Category> categoriesList = new ArrayList<Category>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null) {
            Category unsuccessful = new Category();
            unsuccessful.setId(-1);
            categoriesList.add(unsuccessful);
        } else {
            String allCategoriesQuery = "SELECT id_category, name from category";
            categoriesList = this.getCategoriesHelper(db, conn, allCategoriesQuery); 
        }
        try {  
            if(conn != null)
                conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_DB_ENTITY_CLOSE, ex.getMessage());
        }
        return categoriesList;
    }

    public List<Category> getCategoriesForInfo(int idInfo, DBManager db, Connection conn) throws CustomException {
        String categoriesForInfoQuery = "SELECT id_category, name from info_for_category join category using (id_category) where id_info = " + idInfo;
        return this.getCategoriesHelper(db, conn, categoriesForInfoQuery);
    }
    
    private List<Category> getCategoriesHelper(DBManager db, Connection conn, String query) throws CustomException {
        List<Category> categories = null;
        ResultSet rAllCategories = db.selectQuery(conn, query);
        try {
            categories = new ArrayList<Category>();
            while (rAllCategories.next()) {
                int idCategory = rAllCategories.getInt("id_category");
                String name = rAllCategories.getString("name");
                Category category = new Category();
                category.setId(idCategory);
                category.setName(name);
                categories.add(category);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new CustomException(CustomException.ERR_DATA_NOT_FOUND, ex.getMessage());
        }  
        return categories;
    }

    public List<Category> getCategoriesForPlayer(int idPlayer, DBManager db, Connection conn) throws CustomException {
        String categoriesForPlayerQuery = "SELECT id_category, name FROM category JOIN player_in_category USING (id_category) " + 
                                          "WHERE id_player = " + idPlayer;
        return this.getCategoriesHelper(db, conn, categoriesForPlayerQuery);
    }

    public void deleteCategoriesForPlayer(List<Category> toDeleteCategories, int idPlayer, DBManager db, Connection conn) throws CustomException {
        PreparedStatement psDeleteCategory = null;
        try {
            String deleteCategoryQuery = "DELETE FROM player_in_category WHERE id_player = ? AND id_category = ?" ;
            psDeleteCategory = conn.prepareStatement(deleteCategoryQuery);
            psDeleteCategory.setInt(1, idPlayer);
            //deletne mi vsetky stare kategorie daneho playera, tie, ktore uz nesleduje
            for (Category toDeleteCategory : toDeleteCategories) {
                psDeleteCategory.setInt(2, toDeleteCategory.getId());
                db.deletePreparedStatementQuery(psDeleteCategory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (psDeleteCategory != null) {
                try {
                    psDeleteCategory.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void insertCategoriesForPlayer(List<Category> toInsertCategories, int idPlayer, DBManager db, Connection conn) throws CustomException {
        PreparedStatement psInsertCategory = null;
        try {
            String insertCategoryQuery = "INSERT INTO player_in_category (id_player, id_category) VALUES (?, ?)";
            psInsertCategory = conn.prepareStatement(insertCategoryQuery);
            psInsertCategory.setInt(1, idPlayer);
            //insertne vsetky nove kategorie playerovi, tie, ktore doteraz nesledoval
            for (Category toInsertCategory : toInsertCategories) {
                psInsertCategory.setInt(2, toInsertCategory.getId());
                db.insertPreparedStatementQuery(psInsertCategory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (psInsertCategory != null) {
                try {
                    psInsertCategory.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void deleteCategoriesForInfo(List<Category> toDeleteCategories, int idInfo, DBManager db, Connection conn) throws CustomException {
        PreparedStatement psDeleteCategory = null;
        try {
            String deleteCategoryQuery = "DELETE FROM info_for_category WHERE id_info = ? AND id_category = ?" ;
            psDeleteCategory = conn.prepareStatement(deleteCategoryQuery);
            psDeleteCategory.setInt(1, idInfo);
            for (Category toDeleteCategory : toDeleteCategories) {
                psDeleteCategory.setInt(2, toDeleteCategory.getId());
                db.deletePreparedStatementQuery(psDeleteCategory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (psDeleteCategory != null) {
                try {
                    psDeleteCategory.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void insertCategoriesForInfo(List<Category> toInsertCategories, int idInfo, DBManager db, Connection conn) throws CustomException {
        PreparedStatement psInsertCategory = null;
        try {
            String insertCategoryQuery = "INSERT INTO info_for_category (id_info, id_category) VALUES (?, ?)";
            psInsertCategory = conn.prepareStatement(insertCategoryQuery);
            psInsertCategory.setInt(1, idInfo);
            for (Category toInsertCategory : toInsertCategories) {
                psInsertCategory.setInt(2, toInsertCategory.getId());
                db.insertPreparedStatementQuery(psInsertCategory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (psInsertCategory != null) {
                try {
                    psInsertCategory.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
