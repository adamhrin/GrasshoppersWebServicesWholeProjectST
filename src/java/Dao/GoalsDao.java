/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import CustomException.CustomException;
import Managers.DBManager;
import Models.Goal;
import Models.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam
 */
public class GoalsDao {

    public List<Goal> getGrassGoalsForMatch(int idMatch, int idPlayer, DBManager db, Connection conn) throws CustomException {
        List<Goal> grassGoalsForMatchList = null;
        String grassGoalsForMatchQuery = "SELECT id_goal_grass, period, time, id_scorer, s.firstname as scorer_firstname, s.nick as scorer_nick, s.surname as scorer_surname, s.number as scorer_number, id_assistent " +
                                        "FROM goal_grass gg join player s on (gg.id_scorer = s.id_player) " +
                                        "WHERE id_match = " + idMatch + " " +
                                        "order by period, time";
        ResultSet rGrassGoals = db.selectQuery(conn, grassGoalsForMatchQuery);
        try {
                
            grassGoalsForMatchList = new ArrayList<Goal>();
            while (rGrassGoals.next()) {
                int idGoalGrass = rGrassGoals.getInt("id_goal_grass");
                int period = rGrassGoals.getInt("period");
                String timeString = rGrassGoals.getString("time");
                int scorerId = rGrassGoals.getInt("id_scorer");
                String scorerFirstname = rGrassGoals.getString("scorer_firstname");
                String scorerNick = rGrassGoals.getString("scorer_nick");
                String scorerSurname = rGrassGoals.getString("scorer_surname");
                int scorerNumber = rGrassGoals.getInt("scorer_number");
                
                Player scorer = new Player();
                scorer.setIdPlayer(scorerId);
                scorer.setFirstname(scorerFirstname);
                scorer.setNick(scorerNick);
                scorer.setSurname(scorerSurname);
                scorer.setNumber(scorerNumber);
                
                Player assistent = null;
                int assistentId = rGrassGoals.getInt("id_assistent");
                if (!rGrassGoals.wasNull()) {
                    assistent = new Player();
                    String assistentQuery = "SELECT firstname, nick, surname, number FROM player WHERE id_player = " + assistentId;
                    ResultSet rAssistent = db.selectQuery(conn, assistentQuery);
                    
                    //bude to vzdy len jeden ale dame to whilu ktory sa zopakuje len raz
                    while(rAssistent.next()) {
                        assistent.setIdPlayer(assistentId);
                        assistent.setFirstname(rAssistent.getString("firstname"));
                        assistent.setNick(rAssistent.getString("nick"));
                        assistent.setSurname(rAssistent.getString("surname"));
                        assistent.setNumber(rAssistent.getInt("number"));
                    }
                }
                
                Goal goal = new Goal();
                goal.setIdGoal(idGoalGrass);
                goal.setPeriod(period);
                goal.setTimeString(timeString);
                goal.setScorer(scorer);
                goal.setAssistent(assistent);
                grassGoalsForMatchList.add(goal);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return grassGoalsForMatchList;
    }

    public List<Goal> getOpponentGoalsForMatch(int idMatch, int idPlayer, DBManager db, Connection conn) throws CustomException {
        List<Goal> opponentGoalsForMatchList = null;
        String opponentGoalsQuery = "SELECT id_goal_opponent, scorer, assistent, period, time FROM goal_opponent WHERE id_match = " + idMatch + " " +
                                    "order by period, time";
        ResultSet rOpponentGoals = db.selectQuery(conn, opponentGoalsQuery);
        try {
            //boolean isPlayerAdmin = Evaluator.isPlayerAdmin(idPlayer, db, conn);
            boolean isPlayerAdmin = true;
                
            opponentGoalsForMatchList = new ArrayList<Goal>();
            while (rOpponentGoals.next()) {
                int idGoalOpponent = rOpponentGoals.getInt("id_goal_opponent");
                int period = rOpponentGoals.getInt("period");
                String timeString = rOpponentGoals.getString("time");
                String scorer = rOpponentGoals.getString("scorer");
                if (rOpponentGoals.wasNull()) {
                    scorer = "nezadaný";
                }
                String assistent = rOpponentGoals.getString("assistent");
                if (rOpponentGoals.wasNull()) {
                    assistent = "nezadaný";
                }
                
                Goal goal = new Goal();
                goal.setIdGoal(idGoalOpponent);
                goal.setPeriod(period);
                goal.setTimeString(timeString);
                goal.setOpponentScorer(scorer);
                goal.setOpponentAssistent(assistent);
                opponentGoalsForMatchList.add(goal);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return opponentGoalsForMatchList;
    }

    public void insertGrassGoal(Goal goal, int idMatch) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        String timeString = "00:" + goal.getTimeString();
        String insertGrassGoalQuery = "INSERT INTO goal_grass (id_match, id_scorer, id_assistent, period, time) " +
                                      "VALUES (?, ?, ?, ?, time_format(?, '%H:%i:%s'))";
        PreparedStatement psInsertGrassGoal = null;
        try {
            psInsertGrassGoal = conn.prepareStatement(insertGrassGoalQuery);
            psInsertGrassGoal.setInt(1, idMatch);
            psInsertGrassGoal.setInt(2, goal.getScorer().getIdPlayer());
            if (goal.getAssistent() == null) {
                psInsertGrassGoal.setNull(3, Types.INTEGER);
            } else {
                psInsertGrassGoal.setInt(3, goal.getAssistent().getIdPlayer());
            }
            psInsertGrassGoal.setInt(4, goal.getPeriod());
            psInsertGrassGoal.setString(5, timeString);
            db.insertPreparedStatementQuery(psInsertGrassGoal);
        } catch (SQLException ex) {
            Logger.getLogger(GoalsDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psInsertGrassGoal != null) {
                    psInsertGrassGoal.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void insertOpponentGoal(Goal goal, int idMatch) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        String timeString = "00:" + goal.getTimeString();
        String insertGrassGoalQuery = "INSERT INTO goal_opponent (id_match, scorer, assistent, period, time) " +
                                      "VALUES (?, ?, ?, ?, time_format(?, '%H:%i:%s'))";
        PreparedStatement psInsertGrassGoal = null;
        try {
            psInsertGrassGoal = conn.prepareStatement(insertGrassGoalQuery);
            psInsertGrassGoal.setInt(1, idMatch);
            psInsertGrassGoal.setString(2, goal.getOpponentScorer());
            psInsertGrassGoal.setString(3, goal.getOpponentAssistent());
            psInsertGrassGoal.setInt(4, goal.getPeriod());
            psInsertGrassGoal.setString(5, timeString);
            db.insertPreparedStatementQuery(psInsertGrassGoal);
        } catch (SQLException ex) {
            Logger.getLogger(GoalsDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psInsertGrassGoal != null) {
                    psInsertGrassGoal.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(TrainingsDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deleteGrassGoal(int idGoal) throws CustomException {
        this.deleteGoal(idGoal, "goal_grass");
    }

    public void deleteOpponentGoal(int idGoal) throws CustomException {
        this.deleteGoal(idGoal, "goal_opponent");
    }

    private void deleteGoal(int idGoal, String tableString) throws CustomException {
        DBManager db = new DBManager();
        Connection conn = db.getConnection();
        PreparedStatement psDeleteGoal = null;
        String deleteGoalQuery = "DELETE FROM " + tableString + " WHERE id_" + tableString + " = ?";
        try {
            psDeleteGoal = conn.prepareStatement(deleteGoalQuery);
            psDeleteGoal.setInt(1, idGoal);
            db.deletePreparedStatementQuery(psDeleteGoal);
        } catch (SQLException ex) {
            Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (psDeleteGoal != null) {
                    psDeleteGoal.close();
                }
            } catch (SQLException ex) {
                    Logger.getLogger(CategoriesDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
