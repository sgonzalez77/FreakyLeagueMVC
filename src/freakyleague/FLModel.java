/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;

/**
 *
 * @author sergi
 */
public class FLModel {
    private static String user = "";
    
    public static String getUser() {
        return user;
    }
    
    public static void setUser(String pUser) {
        user = pUser;
    }
    
    private static ClassDatabaseManager db = new ClassDatabaseManager(
            "littlecatmonster.com:3306", //"./", //
            "sergiopmiPES",
            "sergiopmiPES",
            "Pastis1Boli", 0);

    public static List<ClassTournament> giveMeTournaments() {
        List<ClassTournament> tournaments = null;

        if (db.open()) {
            ResultSet rs = db.executeQuery("select * from sergiopmiPES.\"tbltournament\" order by \"date\" desc, \"id\" desc");
            try {
                tournaments = new ArrayList<>();
                while (rs.next()) {
                    tournaments.add(
                        new ClassTournament(rs.getString("id"),
                        rs.getString("date"))
                    );
                }
            } catch (SQLException ex) {
                Logger.getLogger(FLApp.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            db.close();
        }
        return tournaments;
    }
    
   

    public static List<ClassMatch> giveMeTournamentData(int idT) {
        ResultSet rs = null;
        List<ClassMatch> matches = new ArrayList<>();
        List<ClassParam> params = new ArrayList<>();

        if (db.open()) {
            params.add(new ClassParam(String.valueOf(idT), "Integer"));
            rs = db.executeQuery("SELECT * FROM sergiopmiPES.\"tblmatch\" where \"id\" = ?", params);
        }
        try {
            while (rs.next()) {
                matches.add(new ClassMatch(
                        rs.getInt("id"),
                        rs.getString("home"),
                        rs.getString("away"),
                        rs.getInt("scoreHome"),
                        rs.getInt("scoreAway")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FLApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.close();
        return matches;
    }

    public static ClassTournament newTournament() {
        String txtId = "";
        String myDate = "";
        ResultSet rs = null;
        List<ClassParam> params = new ArrayList<>();
        if (db.open()) {
            txtId = db.executeUpdate("insert into sergiopmiPES.\"tbltournament\" (\"date\") values (CURRENT_DATE)",
                    true);
            params.add(new ClassParam(txtId, "Integer"));
            rs = db.executeQuery("select \"date\" from sergiopmiPES.\"tbltournament\" where \"id\" = ?", params);
            try {
                if (rs.next()) {
                    myDate = rs.getString("Date");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FLApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        db.close();
        return new ClassTournament(Integer.valueOf(txtId), myDate);
    }

    public static int giveMeData(String type, String idTeam, String idTourn) {
        int sum = -1;
        String sql1 = "", sql2 = "";
        List<ClassParam> params = new ArrayList<>();
        params.add(new ClassParam(idTeam, "String"));
        params.add(new ClassParam(idTourn, "Integer"));
        switch (type) {
            case "GF": //goals that you scored
                sql1 = "SELECT sum(\"scoreHome\") FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"id\" = ?";
                sql2 = "SELECT sum(\"scoreAway\") FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"id\" = ?";
                break;
            case "GA": //goals against you
                sql1 = "SELECT sum(\"scoreAway\") FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"id\" = ?";
                sql2 = "SELECT sum(\"scoreHome\") FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"id\" = ?";
                break;
            case "LW": //matches won at home
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" > \"scoreAway\" and \"id\" = ?";
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" < \"scoreAway\"  and \"id\" = ?";
                break;
            case "LL": //matches lost at home
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" < \"scoreAway\" and \"id\" = ?";
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" > \"scoreAway\" and \"id\" = ?";
                break;
            case "LD": //matches drawn at home
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" = \"scoreAway\" and \"id\" = ?";
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" = \"scoreAway\" and \"id\" = ?";
                break;
        }
        try {
            db.open();
            ResultSet rs = db.executeQuery(sql1, params);
            if (rs.next()) {
                sum = rs.getInt(1);
            }

            rs = db.executeQuery(sql2, params);

            if (rs.next()) {
                sum += rs.getInt(1);
            }
            db.close();
        } catch (SQLException e) {
            //return false;
            System.out.println(e.getMessage());
            System.out.println(sql1);
            System.out.println(sql2);

        }
        return sum;
    }
    
    public static int giveMeData(String type, String idTeam) {
        int sum = -1;
        String sql1 = "", sql2 = "";
        List<ClassParam> params = new ArrayList<>();
        params.add(new ClassParam(idTeam, "String"));
        
        switch (type) {
            case "GW": //matches won globaly
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" > \"scoreAway\"";
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" < \"scoreAway\"";
                break;
            case "GL": //matches lost globaly
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" < \"scoreAway\"";
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" > \"scoreAway\"";
                break;
            case "GD": //matches drawn globaly
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" = \"scoreAway\"";
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" = \"scoreAway\"";
                break;
        }
        try {
            db.open();
            ResultSet rs = db.executeQuery(sql1, params);
            if (rs.next()) {
                sum = rs.getInt(1);
            }

            rs = db.executeQuery(sql2, params);

            if (rs.next()) {
                sum += rs.getInt(1);
            }
            db.close();
        } catch (SQLException e) {
            //return false;
            System.out.println(e.getMessage());
            System.out.println(sql1);
            System.out.println(sql2);

        }
        return sum;
    }
    
    public static boolean insertMatch(TextField txtId, String player1, String player2, TextField sHome, TextField sAway) {
        Boolean bool;
        List<ClassParam> params = new ArrayList<>();
        params.add(new ClassParam(txtId.getText(), "Integer"));
        params.add(new ClassParam(player1, "String"));
        params.add(new ClassParam(player2, "String"));
        params.add(new ClassParam(sHome.getText(), "Integer"));
        params.add(new ClassParam(sAway.getText(), "Integer"));
        
        String sql
                = "INSERT INTO \"tblmatch\" (\"id\", \"home\", \"away\", \"scoreHome\", "
                + "\"scoreAway\") VALUES (?, ?, ?, ?, ?)";
        if (db.open()) {
            bool = db.executeUpdate(sql, params);
            db.close();
            return bool;
        } else {
            return false;
        }

    }

    public static boolean validateUser(String user, String password) {
        List<ClassParam> params = new ArrayList<>();
        
        if (db.open()) {
            params.add(new ClassParam(user, "String"));
            ResultSet rs = db.executeQuery("select \"password\" from \"users\" where \"id\" = ?", params);
            try {
                if (rs.next()) {
                    //System.out.println(rs.getString("password"));
                    if (rs.getString("password").equals(password)) {
                        db.close();
                        //do it in a smarter way with a table for profiles
                        return true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(FLApp.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            db.close();   
        }
        return false;
    }
}
