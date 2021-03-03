package sk.kosickaakademia.company.database;

import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {
    Log log = new Log();

    private final String INSERTQUERY = "INSERT INTO user (fname, lname, age, gender) " +
            "VALUES (?, ?, ?, ?)";


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

    public boolean insertNewUser(User user){
        Connection con = getConnection();
        if(con != null){
            try {
                PreparedStatement ps = con.prepareStatement(INSERTQUERY);
                ps.setString(1, user.getFname());
                ps.setString(2, user.getLname());
                ps.setInt(3, user.getAge());
                ps.setInt(4, user.getGender().getValue());
                int result = ps.executeUpdate();
                closeConnection(con);
                log.print("NEW USER HAS BEEN ADDED");
                return result==1;
            }catch (SQLException e){
                log.print(e.toString());
            }
        }
        return false;
    }

    public List<User> getFemale(){
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE gender = 1";
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int age = rs.getInt("age");
                int id = rs.getInt("id");
                int gender = rs.getInt("gender");
                User user = new User(id, fname, lname, age, gender);
                list.add(user);
            }
            closeConnection(con);
            return list;
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }

    public List<User> getMale(){
        String sql = "SELECT * FROM user WHERE gender = 0";
        List<User> list = new ArrayList<>();
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int age = rs.getInt("age");
                int id = rs.getInt("id");
                int gender = rs.getInt("gender");
                User user = new User(id, fname, lname, age, gender);
                list.add(user);
            }
            closeConnection(con);
            return list;
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }

    public List<User> getUsersByAge(int from, int to){
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE age >= ? AND age <= ? ORDER BY age";
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, from);
            ps.setInt(2, to);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int age = rs.getInt("age");
                int id = rs.getInt("id");
                int gender = rs.getInt("gender");
                User user = new User(id, fname, lname, age, gender);
                list.add(user);
            }
            closeConnection(con);
            return list;
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }

}
