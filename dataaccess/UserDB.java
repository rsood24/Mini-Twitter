/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;
import business.Tweet;
import business.User;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author xl
 */
public class UserDB {
    
    public static int insert(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query
                = "INSERT INTO Users (Email, FullName, Password, DateOfBirth, SecQuestion, SecAnswer, NumOfTweets, UserID, salt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getDofBirth());
            ps.setString(5, user.getSecQuestion());
            ps.setString(6, user.getSecAnswer());
            ps.setInt(7, user.getNumOfTweets());
            ps.setString(8, user.getUserID()); 
            ps.setString(9, user.getUserSalt());
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static int updatePass(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        
        String query = "UPDATE Users SET Password = ? WHERE Email = ?";
        
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getEmail());
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    public static int update(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
   
        String query = "UPDATE Users SET "
                + "UserID = ?,"
                + "FullName = ?, "
                + "DateOfBirth = ?, "
                + "SecQuestion = ?, "
                + "SecAnswer = ?, "
                + "last_login_time = ? "
                + "WHERE Email = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getUserID());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getDofBirth());
            ps.setString(4, user.getSecQuestion());
            ps.setString(5, user.getSecAnswer());
            ps.setObject(6, user.getLast_login_time());
            ps.setString(7, user.getEmail());
            

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static int delete(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "DELETE FROM User WHERE Email = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getEmail());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    
    }
    public static ArrayList<User> selectAllUsers()
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users ";
        try {
            ps = connection.prepareStatement(query);
//            ps.setString(1, email);
            rs = ps.executeQuery();
            ArrayList<User> userList = new ArrayList<User>();
            
            while (rs.next()) {
                User user = new User();
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setDofBirth(rs.getString("DateOfBirth"));
                user.setSecQuestion(rs.getString("SecQuestion"));
                user.setSecAnswer(rs.getString("SecAnswer"));
                user.setNumOfTweets(rs.getInt("NumOfTweets"));
                user.setUserID(rs.getString("UserID"));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        
    }
    public static ArrayList<User> selectAllFollowedUsers(User user)
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users WHERE Users.UserID IN (SELECT followedUserID FROM Follow WHERE user_ID = ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getUserID());
            rs = ps.executeQuery();
            ArrayList<User> userList = new ArrayList<User>();
            
            while (rs.next()) {
                user = new User();
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setDofBirth(rs.getString("DateOfBirth"));
                user.setSecQuestion(rs.getString("SecQuestion"));
                user.setSecAnswer(rs.getString("SecAnswer"));
                user.setNumOfTweets(rs.getInt("NumOfTweets"));
                user.setUserID(rs.getString("UserID"));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        
    }
    public static ArrayList<User> selectLoginUsers(User auser)
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String query = "SELECT * FROM Users WHERE Users.UserID IN (SELECT user_ID FROM Follow WHERE followedUserID = ? AND followDate >= ?)";
         try {
            ps = connection.prepareStatement(query);
            ps.setString(1, auser.getUserID());
            ps.setObject(2, auser.getLast_login_time());
            rs = ps.executeQuery();
            ArrayList<User> userList = new ArrayList<User>();
            
            User user = null;
            
            while (rs.next()) {
                user = new User();
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setDofBirth(rs.getString("DateOfBirth"));
                user.setSecQuestion(rs.getString("SecQuestion"));
                user.setSecAnswer(rs.getString("SecAnswer"));
                user.setNumOfTweets(rs.getInt("NumOfTweets"));
                user.setUserID(rs.getString("UserID"));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    public static ArrayList<User> selectAllUnfollowedUsers(User user)
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users WHERE Users.UserID NOT IN (SELECT followedUserID FROM Follow WHERE user_ID = ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getUserID());
            rs = ps.executeQuery();
            ArrayList<User> userList = new ArrayList<User>();
            
            while (rs.next()) {
                user = new User();
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setDofBirth(rs.getString("DateOfBirth"));
                user.setSecQuestion(rs.getString("SecQuestion"));
                user.setSecAnswer(rs.getString("SecAnswer"));
                user.setNumOfTweets(rs.getInt("NumOfTweets"));
                user.setUserID(rs.getString("UserID"));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        
    }
    public static User emailExists(String email) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users "
                + "WHERE Email = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            User user = new User();
            while (rs.next()) {
                user = new User();
                user.setUserID(rs.getString("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setDofBirth(rs.getString("DateOfBirth"));
                user.setSecQuestion(rs.getString("SecQuestion"));
                user.setSecAnswer(rs.getString("SecAnswer"));
                user.setNumOfTweets(rs.getInt("NumOfTweets"));
                user.setLast_login_time(rs.getObject("last_login_time"));
                user.setUserSalt(rs.getString("salt"));
            }
            return user;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static User UserIDExists(String UserID) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users "
                + "WHERE UserID = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, UserID);
            rs = ps.executeQuery();
            User user = new User();
            while (rs.next()) {
                user = new User();
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setDofBirth(rs.getString("DateOfBirth"));
                user.setSecQuestion(rs.getString("SecQuestion"));
                user.setSecAnswer(rs.getString("SecAnswer"));
                user.setNumOfTweets(rs.getInt("NumOfTweets"));
                user.setLast_login_time(rs.getObject("last_login_time"));
                user.setUserID(rs.getString("UserID"));
            }
            return user;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static User selectUser(String email, String password) {
       ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users WHERE Email =? AND Password =? ";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            
            User user = null;
            
            while (rs.next()) {
                user = new User();
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setDofBirth(rs.getString("DateOfBirth"));
                user.setSecQuestion(rs.getString("SecQuestion"));
                user.setSecAnswer(rs.getString("SecAnswer"));
                user.setNumOfTweets(rs.getInt("NumOfTweets"));
                user.setUserID(rs.getString("UserID"));
                user.setLast_login_time(rs.getObject("last_login_time"));
            }
            return user;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
   
}
