/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import business.Follow;
import business.Hashtag;
import business.Tweet;
import business.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author riteshsood
 */
public class FollowDB {
    
    public static int insert(String followed, String follower) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query
                = "INSERT INTO Follow (user_ID, followedUserID, followDate) "
                + "VALUES (?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, follower);
            ps.setString(2, followed);
            java.util.Date date = new java.util.Date();
            Object param = new java.sql.Timestamp(date.getTime());
            ps.setObject(3, param);
               
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static int delete(Follow user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "DELETE FROM Follow WHERE user_ID = ? AND followedUserID = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getUser_ID());
            ps.setString(2, user.getFollowUser_ID());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    
    }
}
