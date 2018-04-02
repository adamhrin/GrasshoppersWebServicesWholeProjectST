/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Adam
 */
public class Player implements Serializable {
    private int idPlayer; 
    private String firstname;
    private String nick;
    private String surname;
    private String password;
    private String email;
    private int number;
    private int adminLevel;
    private AcceptedTrainingOptions playerAcceptedTraining;
    private List<Category> categories;
    private int numOfBrigadeHours;

    public int getIdPlayer() {
        return this.idPlayer;
    }

    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }
    
    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public AcceptedTrainingOptions getPlayerAcceptedTraining() {
        return playerAcceptedTraining;
    }

    public void setPlayerAcceptedTraining(AcceptedTrainingOptions playerAcceptedTraining) {
        this.playerAcceptedTraining = playerAcceptedTraining;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }   

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }

    public int getNumOfBrigadeHours() {
        return numOfBrigadeHours;
    }

    public void setNumOfBrigadeHours(int numOfBrigadeHours) {
        this.numOfBrigadeHours = numOfBrigadeHours;
    }
    
    
}
