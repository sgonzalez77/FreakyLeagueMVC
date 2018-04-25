/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/*
 * @author sergi
 */
public class FLModel {
    //prova git
    private static String user = "";

    public static String getUser() {
        return user;
    }

    public static void setUser(String pUser) {
        user = pUser;
    }

    public static void generalReport() {
        try {
            Map parameters = new HashMap();
            db.open();
            //String dir = "//Users//sergi//NetBeansProjects//FreakyLeagueMVC//src//freakyleague//rptLeagueResultsGlobal.jrxml";
            String dir = "./src/freakyleague/rptGeneral.jrxml";

            JasperReport rptLeagueResults = JasperCompileManager.compileReport(dir);

            parameters.put("ReportTitle", "Global Results");
            parameters.put("ReportSubtitle", "Freaky League v. 1.0");
            JasperPrint showRptLR = JasperFillManager.fillReport(rptLeagueResults, parameters, db.getConn());

            JasperViewer.viewReport(showRptLR, false);
        } catch (JRException ex) {
            System.out.println("An error occurred generating the report");
            System.out.println(ex.getMessage());
            Logger.getLogger(FLApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.close();

    }

    public static void tournamentReport(String txtId, String date) {
        try {
            Map parameters = new HashMap();
            db.open();
            //String dir = "//Users//sergi//NetBeansProjects//FreakyLeagueMVC//src//freakyleague//rptLeagueResultsGlobal.jrxml";
            String dir = "./src/freakyleague/rptTournament.jrxml";

            JasperReport rptLeagueResults = JasperCompileManager.compileReport(dir);

            parameters.put("ReportTitle", "Tournament Results");
            parameters.put("ReportSubtitle", "Freaky League v. 1.0");
            parameters.put("ID", Integer.parseInt(txtId));
            parameters.put("data", date);
            JasperPrint showRptLR = JasperFillManager.fillReport(rptLeagueResults, parameters, db.getConn());

            JasperViewer.viewReport(showRptLR, false);
        } catch (JRException ex) {
            System.out.println("An error occurred generating the report");
            System.out.println(ex.getMessage());
            Logger.getLogger(FLApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.close();

    }

    private static DatabaseManager db = new DatabaseManager(
            "littlecatmonster.com:3306", //"littlecatmonster.com:3306" //mysql on a web server //set type to 0!
            "sergiopmiPES", //"localhost:1527" //derby running on NetBeans //set type to 1!
            "sergiopmiPES", //"." //local derby database //set type to 2!
            "Pastis1Boli", 0);

    public static Boolean closeTournament(String txtId) {
        Boolean bool;
        List<Param> params = new ArrayList<>();
        params.add(new Param(txtId, "Integer"));

        String sql
                = "update \"tbltournament\" set \"closed\" = true where \"id\"=?";
        if (db.open()) {
            bool = db.executeUpdate(sql, params);
            db.close();
            return bool;
        } else {
            return false;
        }
    }

    public static void deleteDBGarbage(){
        String sql
                = "delete from \"tbltournament\" where \"closed\" = false";
        //Integer id = 0;
        //try {
            if (db.open()) {
                db.executeUpdate(sql, false);
                /*
                ResultSet rs = db.executeQuery("SELECT MAX(id)+1 AS lastid FROM sergiopmiPES.\"tblmatch\"");
                if (rs.next()) {
                    id = rs.getInt("lastid");
                    System.out.println(id);
                    if (id > 1) {
                        sql = "ALTER TABLE sergiopmiPES.\"tblmatch\" auto_increment = " + id;
                        System.out.println(sql);
                        //List<Param> params = new ArrayList<>();
                        //params.add(new Param(String.valueOf(id), "Integer"));
                        db.executeUpdate(sql, false);
                    }
                }
                */
            }

            db.close();
        //} catch (SQLException ex) {
        //    Logger.getLogger(FLApp.class.getName()).log(Level.SEVERE,
        //            null, ex);

        //}
    }

    public static List<Tournament> giveMeTournaments() {
        List<Tournament> tournaments = null;

        if (db.open()) {
            ResultSet rs = db.executeQuery("select * from sergiopmiPES.\"tbltournament\" order by \"date\" desc, \"id\" desc");
            try {
                tournaments = new ArrayList<>();
                while (rs.next()) {
                    tournaments.add(
                            new Tournament(rs.getString("id"),
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

    public static List<Match> giveMeTournamentData(int idT) {
        ResultSet rs = null;
        List<Match> matches = new ArrayList<>();
        List<Param> params = new ArrayList<>();

        if (db.open()) {
            params.add(new Param(String.valueOf(idT), "Integer"));
            rs = db.executeQuery("SELECT * FROM sergiopmiPES.\"tblmatch\" where \"id\" = ?", params);
        }
        try {
            while (rs.next()) {
                matches.add(new Match(
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

    public static Tournament newTournament() {
        String txtId = "";
        String myDate = "";
        ResultSet rs = null;
        List<Param> params = new ArrayList<>();
        if (db.open()) {
            txtId = db.executeUpdate("insert into sergiopmiPES.\"tbltournament\" (\"date\") values (CURRENT_DATE)",
                    true);
            params.add(new Param(txtId, "Integer"));
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
        return new Tournament(Integer.valueOf(txtId), myDate);
    }

    public static int giveMeData(String type, String idTeam, String idTourn) {
        int sum = -1;
        String sql1 = "", sql2 = "", addId = "";
        List<Param> params = new ArrayList<>();
        params.add(new Param(idTeam, "String"));
        if (!idTourn.equals("0")) {
            params.add(new Param(idTourn, "Integer"));
            addId = " and \"id\" = ?";
        }
        switch (type) {
            case "GF": //goals that you scored
                sql1 = "SELECT sum(\"scoreHome\") FROM sergiopmiPES.\"tblmatch\" where \"home\" = ?" + addId;
                sql2 = "SELECT sum(\"scoreAway\") FROM sergiopmiPES.\"tblmatch\" where \"away\" = ?" + addId;
                break;
            case "GA": //goals against you
                sql1 = "SELECT sum(\"scoreAway\") FROM sergiopmiPES.\"tblmatch\" where \"home\" = ?" + addId;
                sql2 = "SELECT sum(\"scoreHome\") FROM sergiopmiPES.\"tblmatch\" where \"away\" = ?" + addId;
                break;
            case "LW": //matches won at home
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" > \"scoreAway\"" + addId;
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" < \"scoreAway\"" + addId;
                break;
            case "LL": //matches lost at home
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" < \"scoreAway\"" + addId;
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" > \"scoreAway\"" + addId;
                break;
            case "LD": //matches drawn at home
                sql1 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"home\" = ? and \"scoreHome\" = \"scoreAway\"" + addId;
                sql2 = "SELECT COUNT(*) FROM sergiopmiPES.\"tblmatch\" where \"away\" = ? and \"scoreHome\" = \"scoreAway\"" + addId;
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
        List<Param> params = new ArrayList<>();
        params.add(new Param(idTeam, "String"));

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
        List<Param> params = new ArrayList<>();
        params.add(new Param(txtId.getText(), "Integer"));
        params.add(new Param(player1, "String"));
        params.add(new Param(player2, "String"));
        params.add(new Param(sHome.getText(), "Integer"));
        params.add(new Param(sAway.getText(), "Integer"));

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
        List<Param> params = new ArrayList<>();

        if (db.open()) {
            params.add(new Param(user, "String"));
            ResultSet rs = db.executeQuery("select \"password\" from \"tblusers\" where \"id\" = ?", params);
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
