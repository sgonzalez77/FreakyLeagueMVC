/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freakyleague;

/**
 *
 * @author sergi
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ClassDatabaseManager {

    private Connection connection = null;
    private Statement statement = null;
    //private ResultSet resultset = null;
    private String url;
    private String dbName;
    private String username;
    private String password;
    private int type; //0 - mysql, 1 - derby

    // the constructor for the database manager.
    public ClassDatabaseManager(String pUrl, String pDbName, String pUsername, String pPassword, Integer pType) {

        url = pUrl;
        dbName = pDbName;
        username = pUsername;
        password = pPassword;
        type = pType;
    }

    private String dbCompatibility(String str) {
        switch (type) {
            case 0:
                if (str.indexOf('\"') != -1) {
                    return str.replaceAll("\"", "`");
                }
            case 1:
                if (str.indexOf('`') != -1) {
                    return str.replaceAll("`", "\"");
                }
        }
        return str;
    }

    public void close() {
        try {
            /*
            if (resultset != null) {
                resultset.close();
            }
             */
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
            //System.out.println("DB disconnected!");
        } catch (Exception e) {
            System.out.println("Message:    " + e.getMessage());
        }
    }

    public boolean open() {
        String connString = "";
        try {
            //Class.forName ("com.mysql.jdbc.Driver");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (Exception e) {
            System.out.println("Failed to load driver.");                        // cannot even find the driver--return to caller since cannot do anything.
        }

        try {                                                                   // Establish the database connection, create a statement for execution of SQL commands.
            //connString = "jdbc:mysql://" + url + "/" + dbName;
            //connString = "jdbc:derby:" + url + "/" + dbName;
            switch (type) {
                case 0:
                    connString = "jdbc:mysql://" + url + "/" + dbName;
                    break;
                case 1:
                    connString = "jdbc:derby:" + url + "/" + dbName;
                    break;
            }
            connection = DriverManager.getConnection(connString, username, password);
            statement = connection.createStatement();                          // statement used to do things in the database (e.g., create the PhoneBook table).
            return true;

        } catch (SQLException exception) {
            System.out.println("\n*** SQLException caught ***\n");
            while (exception != null) {                                                                   // grab the exception caught to tell us the problem.
                System.out.println("SQLState:   " + exception.getSQLState());
                System.out.println("Message:    " + exception.getMessage());
                System.out.println("Error code: " + exception.getErrorCode());
                exception = exception.getNextException();
                System.out.println("");
            }
        } catch (java.lang.Exception exception) {                                 // perhaps there is an exception that was not SQL related.
            exception.printStackTrace();                                        // shows a trace of the exception error--like we see in the console.
        }
        return false;
    }

    public ResultSet executeQuery(String sql) {
        try {
            //statement = connection.prepareStatement(sql);
            return statement.executeQuery(dbCompatibility(sql));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(dbCompatibility(sql));
            return null;
        }
    }

    public ResultSet executeQuery(String sql, List<ClassParam> params) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(dbCompatibility(sql));
            for (int i = 0; i < params.size(); i++) {
                switch (params.get(i).getType()) {
                    case "String":
                        pStatement.setString(i + 1, params.get(i).getValue());
                        break;
                    case "Integer":
                        pStatement.setInt(i + 1, Integer.parseInt(params.get(i).getValue()));
                        break;
                }
            }

            return pStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(dbCompatibility(sql));
            return null;
        }
    }

    //returns autoincrement, "" or null
    public String executeUpdate(String sql, boolean autoincrement) {
        //upgrade this method passing params in an array
        ResultSet resultset = null;
        try {
            //statement = connection.prepareStatement(sql);
            if (autoincrement) {
                //statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.executeUpdate(dbCompatibility(sql), Statement.RETURN_GENERATED_KEYS);
                if (autoincrement) {
                    resultset = statement.getGeneratedKeys();
                    resultset.next();
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(resultset.getInt(1));
                    return sb.toString();
                } else {
                    return "";
                }
            } else {
                statement.executeUpdate(dbCompatibility(sql));
                return "";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(dbCompatibility(sql));
            return null;
        }
    }

    public Boolean executeUpdate(String sql, List<ClassParam> params) {
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        ResultSet resultset = null;
        PreparedStatement pStatement = null;
        try {
            pStatement = connection.prepareStatement(dbCompatibility(sql));
            for (int i = 0; i < params.size(); i++) {
                switch (params.get(i).getType()) {
                    case "String":
                        pStatement.setString(i + 1, params.get(i).getValue());
                        break;
                    case "Integer":
                        pStatement.setInt(i + 1, Integer.parseInt(params.get(i).getValue()));
                        break;
                }
            }
            return pStatement.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(dbCompatibility(sql));
            return false;
        }
    }

}
