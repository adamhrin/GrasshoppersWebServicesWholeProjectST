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
public class Match {
    private int idMatch;
    private Location location;
    private League league;
    private Category category;
    private String startDateTimeString;
    private String startTimeString;
    private String endDateTimeString;
    private String endTimeString;
    private List<Goal> grassGoals;
    private List<Goal> opponentGoals;
    private String opponentName;
    private String opponentAbbreviation;

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public List<Goal> getGrassGoals() {
        return grassGoals;
    }

    public void setGrassGoals(List<Goal> grassGoals) {
        this.grassGoals = grassGoals;
    }
    
    public void addGrassGoal(Goal grassGoal) {
        this.grassGoals.add(grassGoal);
    }

    public List<Goal> getOpponentGoals() {
        return opponentGoals;
    }

    public void setOpponentGoals(List<Goal> opponentGoals) {
        this.opponentGoals = opponentGoals;
    }
    
    public void addOpponentGoal(Goal opponentGoal) {
        this.opponentGoals.add(opponentGoal);
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getOpponentAbbreviation() {
        return opponentAbbreviation;
    }

    public void setOpponentAbbreviation(String opponentAbbreviation) {
        this.opponentAbbreviation = opponentAbbreviation;
    }
    
    
}
