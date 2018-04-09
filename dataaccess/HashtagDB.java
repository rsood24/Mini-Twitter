/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

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
public class HashtagDB {
    
    public static int insert(Hashtag tag) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query
                = "INSERT INTO Hashtag (HashtagID, HashtagText, HashtagCount) "
                + "VALUES (?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, tag.getHashtagID());
            ps.setString(2, tag.getHashtagText());
            ps.setInt(3, tag.getHashtagCount());
               
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static Hashtag getTagCount(Hashtag tag) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String query
                = "SELECT * FROM Hashtag WHERE HashtagText = ? ";
       try {
            ps = connection.prepareStatement(query);
            ps.setString(1, tag.getHashtagText());
            rs = ps.executeQuery();

            
            Hashtag a_tag = new Hashtag();
            
            while (rs.next()) {
                a_tag.setHashtagID(rs.getInt("HashtagID"));
                a_tag.setHashtagText(rs.getString("HashtagText"));
                a_tag.setHashtagCount(rs.getInt("HashtagCount"));
            }
            return a_tag;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    public static int updateCount(Hashtag tag) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        String query
                = "UPDATE Hashtag SET HashtagCount = ? WHERE HashtagText = ? ";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, tag.getHashtagCount());
            ps.setString(2, tag.getHashtagText());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static ArrayList<Tweet> getHash(Hashtag tag) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String query
                = "SELECT * FROM Tweets WHERE H_ID = ? ";
       try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, tag.getHashtagID());
            rs = ps.executeQuery();

            ArrayList<Tweet> a_list = new ArrayList<Tweet>();
            
            while (rs.next()) {
                
                Tweet a_tweet = new Tweet();
                a_tweet.setFullName(rs.getString("FullName"));
                a_tweet.setEmail(rs.getString("Email"));
                a_tweet.setText(rs.getString("Text"));
                a_tweet.setDate(rs.getDate("Date"));
                a_list.add(a_tweet);
                
            }
            return a_list;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    public static ArrayList<Hashtag> getTrendingHash() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String query
                = "SELECT * FROM Hashtag ORDER BY HashtagCount DESC LIMIT 10 ";
       try {
            ps = connection.prepareStatement(query);
            //ps.setInt(1, tag.getHashtagID());
            rs = ps.executeQuery();

            ArrayList<Hashtag> a_list = new ArrayList<Hashtag>();
            
            while (rs.next()) {
                
                Hashtag a_tweet = new Hashtag();
                String temp = rs.getString("HashtagText");
                a_tweet.setHashtagCount(rs.getInt("HashtagCount"));
                a_tweet.setHashtagText(temp.substring(1));
                a_tweet.setHashtagID(rs.getInt("HashtagID"));
                a_list.add(a_tweet);
                
            }
            return a_list;
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
