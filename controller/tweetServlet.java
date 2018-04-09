/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import business.Tweet;
import business.User;
import dataaccess.TweetDB;
import dataaccess.UserDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author riteshsood
 */
public class tweetServlet extends HttpServlet {

   protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException {

        HttpSession session = request.getSession();
    
        String action = request.getParameter("action");
       
        String url ="";
        if (action.equals("add"))
        {                
            // get parameters from the request
            String fullName = request.getParameter("fullNameID");
            String email = request.getParameter("emailID");
            String text = request.getParameter("textID");
            User user = UserDB.emailExists(email);
            Tweet a_tweet = new Tweet();
            a_tweet.setEmail(email);
            a_tweet.setFullName(fullName);
            a_tweet.setText(text);
            a_tweet.setUserID(user.getUserID());
            
            TweetDB.insert(a_tweet);
            
            
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            ArrayList<User> users = new ArrayList<User>();
            ArrayList<Tweet> mytweets = new ArrayList<Tweet>();
            ArrayList<User> followedUsers = new ArrayList<User>();
            ArrayList<User> unfollowedUsers = new ArrayList<User>();
            
            User thisGuy = new User();
            thisGuy = UserDB.emailExists(email);
            thisGuy.addATweet();
            TweetDB.updateNumTweets(thisGuy);
            followedUsers = UserDB.selectAllFollowedUsers(thisGuy);
            unfollowedUsers = UserDB.selectAllUnfollowedUsers(thisGuy);
            tweets = TweetDB.selectAllFollowTweets(thisGuy);
            mytweets = TweetDB.selectUserTweets(email, thisGuy.getUserID());
                // set User object in request object and set URL
            session.setAttribute("user", thisGuy);
            session.setAttribute("unUsers", unfollowedUsers);
            session.setAttribute("users", followedUsers);
            session.setAttribute("tweets", tweets);
            session.setAttribute("mytweets", mytweets);
            String check = request.getParameter("profile");
            if(check == "profile")
            {
                url = "/profile.jsp";
            }
            else
                url = "/home.jsp";
        }
        else if(action.equals("delete"))
        {
            String email = request.getParameter("email");
            String text = request.getParameter("text");
            
            Tweet a_tweet = new Tweet();
            a_tweet.setEmail(email);
            a_tweet.setText(text);
            
            TweetDB.delete(a_tweet);
            
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            ArrayList<User> users = new ArrayList<User>();
            ArrayList<Tweet> mytweets = new ArrayList<Tweet>();
            ArrayList<User> followedUsers = new ArrayList<User>();
            ArrayList<User> unfollowedUsers = new ArrayList<User>();
            
            User thisGuy = new User();
            thisGuy = UserDB.emailExists(email);
            thisGuy.removeATweet();
            TweetDB.updateNumTweets(thisGuy);
            followedUsers = UserDB.selectAllFollowedUsers(thisGuy);
            unfollowedUsers = UserDB.selectAllUnfollowedUsers(thisGuy);
            tweets = TweetDB.selectAllFollowTweets(thisGuy);
            mytweets = TweetDB.selectUserTweets(email, thisGuy.getUserID());
                // set User object in request object and set URL
            session.setAttribute("user", thisGuy);
            session.setAttribute("unUsers", unfollowedUsers);
            session.setAttribute("users", followedUsers);
            session.setAttribute("tweets", tweets);
            session.setAttribute("mytweets", mytweets);
            url = "/profile.jsp";
        }
        else
            url = "/signup.jsp";
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
        
            
   }
   
   protected void doGet(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException {

        HttpSession session = request.getSession();
    
        String action = request.getParameter("action");
       
        String url ="";
        if (action.equals("delete"))
        {                
            // get parameters from the request
            String email = request.getParameter("email");
            String text = request.getParameter("text");
            
            Tweet a_tweet = new Tweet();
            a_tweet.setEmail(email);
            a_tweet.setText(text);
            
            TweetDB.delete(a_tweet);
            
            
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            ArrayList<User> users = new ArrayList<User>();
            User thisGuy = new User();
            thisGuy = UserDB.emailExists(email);
            thisGuy.addATweet();
            TweetDB.updateNumTweets(thisGuy);
            users = UserDB.selectAllUsers();
            tweets = TweetDB.selectAllTweets();
                // set User object in request object and set URL
            session.setAttribute("users", users);
            session.setAttribute("tweets", tweets);
            session.setAttribute("user", thisGuy);
            url = "/profile.jsp";
        }
        else
            url = "/signup.jsp";
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
        
            
   }
}
