package sk.kosickaakademia.company.database;

import sk.kosickaakademia.company.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    Log log = new Log();
    /*

    private String url = "jdbc:mysql://itsovy.sk:3306/company";
    private String nameOfDatabase = "mysqluser";
    private String passwordOfDatabase = "Kosice2021!";

     */

    public Connection getConnection(){
        Connection con;
        try {
            Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties");
            properties.load(inputStream);
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String passwordOfDatabase = properties.getProperty("password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, passwordOfDatabase);
            if(con != null){
                log.print("Connection success");
                return con;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                log.print("Connection closed!");
            }catch (SQLException e){
                //e.printStackTrace();
                log.error(e.toString());
            }
        }
    }

}
