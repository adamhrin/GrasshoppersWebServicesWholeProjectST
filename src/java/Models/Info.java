/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.List;

/**
 *
 * @author Adam
 */
public class Info {
    private int idInfo;
    private Player creator;
    private String creationDateTimeString;
    private String lastUpdateDateTimeString;
    private List<Category> categories;
    private String content;
    private boolean isGridDeleteEditInfoVisible;

    public int getIdInfo() {
        return idInfo;
    }

    public void setIdInfo(int idInfo) {
        this.idInfo = idInfo;
    }

    public Player getCreator() {
        return creator;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }

    public String getCreationDateTimeString() {
        return creationDateTimeString;
    }

    public void setCreationDateTimeString(String creationDateTimeString) {
        this.creationDateTimeString = creationDateTimeString;
    }

    public String getLastUpdateDateTimeString() {
        return lastUpdateDateTimeString;
    }

    public void setLastUpdateDateTimeString(String lastUpdateDateTimeString) {
        this.lastUpdateDateTimeString = lastUpdateDateTimeString;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIsGridDeleteEditInfoVisible() {
        return isGridDeleteEditInfoVisible;
    }

    public void setIsGridDeleteEditInfoVisible(boolean isGridDeleteEditInfoVisible) {
        this.isGridDeleteEditInfoVisible = isGridDeleteEditInfoVisible;
    }
    
}
