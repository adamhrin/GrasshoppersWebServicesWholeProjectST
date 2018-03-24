/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Adam
 */
public class Goal {
    private int idGoal;
    private Player scorer;
    private Player assistent;
    private int period;
    private String timeString;
    private String opponentScorer;
    private String opponentAssistent;

    public int getIdGoal() {
        return idGoal;
    }

    public void setIdGoal(int idGoal) {
        this.idGoal = idGoal;
    }

    public Player getScorer() {
        return scorer;
    }

    public void setScorer(Player scorer) {
        this.scorer = scorer;
    }

    public Player getAssistent() {
        return assistent;
    }

    public void setAssistent(Player assistent) {
        this.assistent = assistent;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getOpponentScorer() {
        return opponentScorer;
    }

    public void setOpponentScorer(String opponentScorer) {
        this.opponentScorer = opponentScorer;
    }

    public String getOpponentAssistent() {
        return opponentAssistent;
    }

    public void setOpponentAssistent(String opponentAssistent) {
        this.opponentAssistent = opponentAssistent;
    }
}
