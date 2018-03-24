/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;


/**
 *
 * @author Adam
 */
public class Training implements Serializable {
    private int idTraining;
    private Location location;
    private String startDateTimeString;
    private String startTimeString;
    private String endDateTimeString;
    private String endTimeString;
    private AcceptedTrainingOptions acceptedByPlayer;
    private List<Category> categories;
    private List<Player> invitedPlayers;

    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }
    
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    public List<Category> getCategories() {
        return this.categories;
    }
    
    public void addInvitedPlayer(Player player) {
        this.invitedPlayers.add(player);
    }

    public void setInvitedPlayers(List<Player> invitedPlayers) {
        this.invitedPlayers = invitedPlayers;
    }

    public List<Player> getInvitedPlayers() {
        return this.invitedPlayers;
    }

    public AcceptedTrainingOptions getAcceptedByPlayer() {
        return acceptedByPlayer;
    }

    public void setAcceptedByPlayer(AcceptedTrainingOptions acceptedByPlayer) {
        this.acceptedByPlayer = acceptedByPlayer;
    }

    public int getIdTraining() {
        return this.idTraining;
    }

    public void setIdTraining(int idTraining) {
        this.idTraining = idTraining;
    }

    public String getStartTimeString() {
        return this.startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public String getEndTimeString() {
        return this.endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public String getStartDateTimeString() {
        return this.startDateTimeString;
    }

    public void setStartDateTimeString(String startDateTimeString) {
        this.startDateTimeString = startDateTimeString;
    }

    public String getEndDateTimeString() {
        return this.endDateTimeString;
    }

    public void setEndDateTimeString(String endDateTimeString) {
        this.endDateTimeString = endDateTimeString;
    }
}
