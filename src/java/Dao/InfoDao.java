/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Helpers.Evaluator;
import Managers.DBManager;
import Models.Category;
import Models.Info;
import Models.Location;
import Models.Player;
import Models.Training;
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
public class InfoDao {
    public List<Info> getInfoForPlayer(int idPlayer) throws CustomException {
        List<Info> infoList = new ArrayList<Info>();
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        if (conn == null) {
            Info unsuccessful = new Info();
            unsuccessful.setIdInfo(-1);
            infoList.add(unsuccessful);
        } else {
            String infoForPlayerQuery = "SELECT i.id_info, i.id_creator, p.firstname, p.nick, p.surname, i.creation_date_time, i.last_update_date_time, i.content " +
                                        "FROM info i join player p on (i.id_creator = p.id_player) join info_for_category ifc on (i.id_info = ifc.id_info) " +
                                        "join player_in_category pic on (ifc.id_category = pic.id_category) " +
                                        "where pic.id_player = " + idPlayer + " " +
                                        "group by i.id_info " +
                                        "order by i.creation_date_time, i.last_update_date_time";
            ResultSet rInfoForPlayer = db.selectQuery(conn, infoForPlayerQuery);
            try {
                while (rInfoForPlayer.next()) {
                    //boolean isPlayerAdmin = Evaluator.isPlayerAdmin(idPlayer, db, conn);
                    boolean isPlayerAdmin = true;
                    
                    Info info = new Info();
                    int idInfo = rInfoForPlayer.getInt("id_info");
                    info.setIdInfo(idInfo);
                    int idCreator = rInfoForPlayer.getInt("id_creator");
                    String firstname = rInfoForPlayer.getString("firstname");
                    String nick = rInfoForPlayer.getString("nick");
                    String surname = rInfoForPlayer.getString("surname");
                    
                    Player creator = new Player();
                    creator.setIdPlayer(idCreator);
                    creator.setFirstname(firstname);
                    creator.setNick(nick);
                    creator.setSurname(surname);
                    info.setCreator(creator);
                    
                    String content = rInfoForPlayer.getString("content");
                    info.setContent(content);
                    String creationDateTimeString = rInfoForPlayer.getString("creation_date_time");
                    String lastUpdateDateTimeString = rInfoForPlayer.getString("last_update_date_time");
                    info.setCreationDateTimeString(creationDateTimeString);
                    info.setLastUpdateDateTimeString(lastUpdateDateTimeString);
                    info.setIsGridDeleteEditInfoVisible(isPlayerAdmin);
                    
                    CategoriesDao categoriesDao = new CategoriesDao();
                    info.setCategories(categoriesDao.getCategoriesForInfo(idInfo, db, conn));
                    
                    infoList.add(info);
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
        return infoList;
    }

    public void insertInfo(Info i) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        
        String getInfoIdQuery = "SELECT max(id_info) as max_id FROM info";
        
        String insertInfoQuery = "INSERT INTO info (id_creator, content) " +
                                     "VALUES (?, ?)";
        PreparedStatement psInsertInfo = null;
        
        String insertCategoryForInfo = "INSERT INTO info_for_category (id_info, id_category) VALUES (?, ?)";
        PreparedStatement psInsertCategoryForInfo = null;
        try {
            psInsertInfo = conn.prepareStatement(insertInfoQuery);
            psInsertInfo.setInt(1, i.getCreator().getIdPlayer());
            psInsertInfo.setString(2, i.getContent());
            db.insertPreparedStatementQuery(psInsertInfo);
            
            ResultSet rInfoId = db.selectQuery(conn, getInfoIdQuery);
            int idInfo = -1;
            try {
                while (rInfoId.next()) {
                    idInfo = rInfoId.getInt("max_id");
                }
            } catch (Exception ex) {
                throw new CustomException(CustomException.ERR_DATA_NOT_FOUND, ex.getMessage());
            } 
            
            psInsertCategoryForInfo = conn.prepareStatement(insertCategoryForInfo);
            psInsertCategoryForInfo.setInt(1, idInfo);
            for (Category category : i.getCategories()) {
                psInsertCategoryForInfo.setInt(2, category.getId());
                db.insertPreparedStatementQuery(psInsertCategoryForInfo);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(InfoDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psInsertInfo != null) {
                    psInsertInfo.close();
                }
                if (psInsertCategoryForInfo != null) {
                    psInsertCategoryForInfo.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(InfoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void updateInfo(Info newInfo) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        int idInfo = newInfo.getIdInfo();
        
        List<Category> oldCategories = this.getCategoriesOfInfo(idInfo, conn, db); 
        List<Category> newCategories = newInfo.getCategories(); 
        
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
        
        String updatePlayerQuery = "UPDATE info SET content = ? WHERE id_info = ?";
        
        PreparedStatement psUpdatePlayer = null;
        try {
            psUpdatePlayer = conn.prepareStatement(updatePlayerQuery);
            psUpdatePlayer.setString(1, newInfo.getContent());
            psUpdatePlayer.setInt(2, newInfo.getIdInfo());
            db.insertPreparedStatementQuery(psUpdatePlayer);
        } catch (SQLException ex) {
            Logger.getLogger(PlayersDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CategoriesDao categoriesDao = new CategoriesDao();   
        
        categoriesDao.deleteCategoriesForInfo(toDeleteCategories, idInfo, db, conn);
        categoriesDao.insertCategoriesForInfo(toInsertCategories, idInfo, db, conn);
        
        
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

    private List<Category> getCategoriesOfInfo(int idInfo, Connection conn, DBManager db) throws CustomException {
        List<Category> categories = new ArrayList<>();
        String getCategoriesQuery = "SELECT id_category FROM info_for_category WHERE id_info = " + idInfo;
        ResultSet rsCategories = db.selectQuery(conn, getCategoriesQuery);
        try {
            while(rsCategories.next()) {
                Category cat = new Category();
                cat.setId(rsCategories.getInt("id_category"));
                categories.add(cat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BrigadesDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categories;
    }
}
