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
public class Brigade {
    private int idBrigade;
    private Location location;
    private League league;
    private String startDateTimeString;
    private String startTimeString;
    private String endDateTimeString;
    private String endTimeString;
    private boolean isRegisteredPlayer;
    private List<Position> positions;

    public int getIdBrigade() {
        return idBrigade;
    }

    public void setIdBrigade(int idBrigade) {
        this.idBrigade = idBrigade;
    }

    public Location getLocation() {
        return location;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStartDateTimeString() {
        return startDateTimeString;
    }

    public void setStartDateTimeString(String startDateTimeString) {
        this.startDateTimeString = startDateTimeString;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public String getEndDateTimeString() {
        return endDateTimeString;
    }

    public void setEndDateTimeString(String endDateTimeString) {
        this.endDateTimeString = endDateTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public boolean isIsRegisteredPlayer() {
        return isRegisteredPlayer;
    }

    public void setIsRegisteredPlayer(boolean isRegisteredPlayer) {
        this.isRegisteredPlayer = isRegisteredPlayer;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
    
    
}
