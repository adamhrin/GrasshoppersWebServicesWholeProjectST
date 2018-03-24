/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import CustomException.CustomException;
import Managers.DBManager;
import Models.AcceptedTrainingOptions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Adam
 */
public class Evaluator {

    public static boolean isPlayerAdmin(int idPlayer, DBManager db, Connection conn) throws SQLException, CustomException {
        boolean isPlayerAdmin = false;
        String isPlayerAdminQuery = "select count(*) as is_admin from player where id_player = " + idPlayer + " and admin = 1";
        ResultSet rIsPlayerAdmin = db.selectQuery(conn, isPlayerAdminQuery);
        //jeden riadok kde je ako vysledok 0 ak player nie je admin a 1< ak je admin
        while(rIsPlayerAdmin.next()) {
            int isPlayerAdminInt = rIsPlayerAdmin.getInt("is_admin");
            if (isPlayerAdminInt > 0) {
                isPlayerAdmin = true;
            }
        }
        return isPlayerAdmin;
    }

    public static AcceptedTrainingOptions evaluateAcceptedTraining(ResultSet result, String acceptsString) throws SQLException {
        boolean playerAcceptedTrainingBoolean = result.getBoolean(acceptsString);
        if (result.wasNull()) {
            return AcceptedTrainingOptions.NotStated;
        } else if (playerAcceptedTrainingBoolean) {
            return AcceptedTrainingOptions.Accepted;
        } else {
            return AcceptedTrainingOptions.Declined;
        }
    }
    
}
