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
public class TweetDB {
    
    public static int insert(Tweet a_tweet) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        Hashtag hashID = new Hashtag();
        String userMention = "";
        String Text = a_tweet.getText();
        String newMessage = "";
        int startInd = 0;
        if(Text.indexOf("#", startInd) != -1)
        {
            while(Text.indexOf("#", startInd) != -1)
            {
                int indexOf = Text.indexOf("#", startInd);
                int indexOfSpace = Text.indexOf(" ", indexOf+1);
                if(indexOfSpace == -1) {
                
                   indexOfSpace = Text.length();
                }
                String mention = Text.substring(indexOf, indexOfSpace);
                Hashtag tag = new Hashtag();
                tag.setHashtagText(mention);
                Hashtag Htag = HashtagDB.getTagCount(tag);
                String aText = Htag.getHashtagText();
                if(aText == null)
                {
                    tag.setHashtagCount(1);
                    HashtagDB.insert(tag);
                    Htag = HashtagDB.getTagCount(tag);
                    hashID.setHashtagID(Htag.getHashtagID());
                }
                else
                {
                    int hold = Htag.getHashtagCount();
                    String check = Htag.getHashtagText();
                    Htag.setHashtagCount(hold + 1);
                    HashtagDB.updateCount(Htag);
                    hashID.setHashtagID(Htag.getHashtagID());
                }
                newMessage = Text.replace(mention, "<span class='blueX'><a href='HashtagServlet?action=hash&hashTag=" + mention.substring(1) + "'>" + mention +"</a></span>");
                startInd = indexOf+1;
            //blueX is a class in CSS files, defining a blue color for text.
            } 
        }
        else
            newMessage = a_tweet.getText();
        
        String a_mess = "";
        if(newMessage.indexOf("@", startInd) != -1)
        {
            while(newMessage.indexOf("@", startInd) != -1)
            {
                int indexOf = newMessage.indexOf("@", startInd);
                int indexOfSpace = newMessage.indexOf(" ", indexOf+1);
                if(indexOfSpace == -1) {
                    
                    indexOfSpace = newMessage.length();
                }
                String mention = newMessage.substring(indexOf, indexOfSpace);
                userMention = newMessage.substring((indexOf+1), indexOfSpace);                
                a_mess = newMessage.replace(mention, "<span class='blueX'>" + mention + "</span>");
                startInd = indexOf+1;
                
            }
        }
        else
            a_mess = newMessage;
      /*  String oneMoreMess = "";
        startInd = 0;
        while(newMessage.indexOf("@", startInd) != -1)
        {
            int indexOf = newMessage.indexOf("@", startInd);
            int indexOfSpace = newMessage.indexOf(" ", indexOf+1);
            if(indexOfSpace == -1) {
                
                indexOfSpace = newMessage.length();
            }
             String mention = newMessage.substring(indexOf, indexOfSpace);
            oneMoreMess = newMessage.replace(mention, "<span class='blueX'>" + mention + "</span>");
            startInd = indexOf+1;
         //blueX is a class in CSS files, defining a blue color for text.
        }   */
        String query
                = "INSERT INTO Tweets (Email, FullName, Text, mentionedUserID, Date, H_ID, userIdOfA) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, a_tweet.getEmail());
            ps.setString(2, a_tweet.getFullName());
            ps.setString(3, a_mess);
            java.util.Date date = new java.util.Date();
            Object param = new java.sql.Timestamp(date.getTime());
                
            //java.sql.Date sDate = new java.sql.Date(a_tweet.getDate().getTime());
            ps.setString(4, userMention);
            ps.setObject(5, param);
            ps.setInt(6, hashID.getHashtagID());
            ps.setString(7, a_tweet.getUserID());
            //ps.setDate();
            
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
     
    public static int updateNumTweets(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        
        String query = "UPDATE Users SET NumOfTweets = ? WHERE Email = ?";
        try{
            ps = connection.prepareStatement(query);
            ps.setInt(1, user.getNumOfTweets());
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
     
    public static int updateTweetName(User user)
    {
       ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        
        String query = "UPDATE Tweets SET FullName = ? WHERE Email = ?";
        try{
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getFullName());
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
    
    public static int delete(Tweet a_tweet) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "DELETE FROM Tweets WHERE Email = ? AND Text = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, a_tweet.getEmail());
            ps.setString(2, a_tweet.getText());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    
    }
    
    public static ArrayList<Tweet> selectLoginTweets(User user)
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String query = "Select * from Tweets where mentionedUserID = ? And Date >= ?";
         try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getUserID());
            ps.setObject(2, user.getLast_login_time());
            rs = ps.executeQuery();
            ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
            
            Tweet a_tweet = null;
            
            while (rs.next()) {
                a_tweet = new Tweet();
                a_tweet.setFullName(rs.getString("FullName"));
                a_tweet.setEmail(rs.getString("Email"));
                a_tweet.setText(rs.getString("Text"));
                a_tweet.setDate(rs.getDate("Date"));
               
                tweetList.add(a_tweet);
            }
            return tweetList;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    public static ArrayList<Tweet> selectAllFollowTweets(User user)
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Tweets WHERE Tweets.userIdOfA IN (SELECT followedUserID FROM Follow WHERE user_ID = ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getUserID());
            rs = ps.executeQuery();
            ArrayList<Tweet> userList = new ArrayList<Tweet>();
            
            while (rs.next()) {
                Tweet a_tweet = new Tweet();
                a_tweet.setFullName(rs.getString("FullName"));
                a_tweet.setEmail(rs.getString("Email"));
                a_tweet.setText(rs.getString("Text"));
                a_tweet.setDate(rs.getDate("Date"));
                userList.add(a_tweet);
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
    
    public static ArrayList<Tweet> selectAllTweets()
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Tweets ";
        try {
            ps = connection.prepareStatement(query);
//            ps.setString(1, email);
            rs = ps.executeQuery();
            ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
            
            Tweet a_tweet = null;
            
            while (rs.next()) {
                a_tweet = new Tweet();
                a_tweet.setFullName(rs.getString("FullName"));
                a_tweet.setEmail(rs.getString("Email"));
                a_tweet.setText(rs.getString("Text"));
                a_tweet.setDate(rs.getDate("Date"));
               /* String newMessage = "";
                String message = rs.getString("Text");
                int startInd = 0;
                while(message.indexOf("@", startInd) != -1)
                {
                    int indexOf = message.indexOf("@", startInd);
                    int indexOfSpace = message.indexOf(" ", indexOf+1);
                    if(indexOfSpace == -1) {
                    indexOfSpace = message.length();
                    }
                    String mention = message.substring(indexOf, indexOfSpace);
                    newMessage = message.replace(mention, "<a class='blueX'>" + mention + "</a>");
                    startInd = indexOf+1;
                    a_tweet.setText(newMessage);
                //blueX is a class in CSS files, defining a blue color for text.
                }
                startInd = 0;
                String mess = "";
                while(newMessage.indexOf("#", startInd) != -1)
                {
                    int indexOf = message.indexOf("#", startInd);
                    int indexOfSpace = message.indexOf(" ", indexOf+1);
                    if(indexOfSpace == -1) {
                    indexOfSpace = message.length();
                    }
                    String mention = message.substring(indexOf, indexOfSpace);
                    newMessage = message.replace(mention, "<a class='blueX'>" + mention + "</a>");
                    startInd = indexOf+1;
                    a_tweet.setText(newMessage);
                }*/
                tweetList.add(a_tweet);
            }
            return tweetList;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        
    }
    
    public static ArrayList<Tweet> selectUserTweets(String email, String userID)
    {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Tweets WHERE Email = ? OR mentionedUserID = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, userID);
            rs = ps.executeQuery();
            ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
            
            Tweet a_tweet = null;
            
            while (rs.next()) {
                a_tweet = new Tweet();
                a_tweet.setFullName(rs.getString("FullName"));
                a_tweet.setEmail(rs.getString("Email"));
                a_tweet.setText(rs.getString("Text"));
                a_tweet.setDate(rs.getDate("Date"));
               
                tweetList.add(a_tweet);
            }
            return tweetList;
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
