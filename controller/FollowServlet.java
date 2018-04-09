/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import business.Follow;
import business.Hashtag;
import business.Tweet;
import business.User;
import dataaccess.FollowDB;
import dataaccess.HashtagDB;
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
 *href="FollowServlet?action=follow&userID=${something}&followeduserID=${something}"
 * @author riteshsood
 */
public class FollowServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException {

        HttpSession session = request.getSession();
    
        String action = request.getParameter("action");
       
        String url ="";               
            // get parameters from the request
        if(action.equals("follow"))
        {
            String userID = request.getParameter("userID");
            String followeduserID = request.getParameter("followeduserID");
            FollowDB.insert(followeduserID, userID);
            
            User user = UserDB.UserIDExists(userID);
            
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            ArrayList<User> followedUsers = new ArrayList<User>();
            ArrayList<User> unfollowedUsers = new ArrayList<User>();
            
            followedUsers = UserDB.selectAllFollowedUsers(user);
            unfollowedUsers = UserDB.selectAllUnfollowedUsers(user);
            tweets = TweetDB.selectAllFollowTweets(user);
                // set User object in request object and set URL
            session.setAttribute("users", followedUsers);
            session.setAttribute("unUsers", unfollowedUsers);
            session.setAttribute("tweets", tweets);
            url = "/home.jsp";
        }
        else if(action.equals("unfollow"))
        {
            String userID = request.getParameter("userID");
            String followeduserID = request.getParameter("followeduserID");
            Follow temp = new Follow();
            temp.setUser_ID(userID);
            temp.setFollowUser_ID(followeduserID);
            FollowDB.delete(temp);
            
            User user = UserDB.UserIDExists(userID);
            
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            ArrayList<User> followedUsers = new ArrayList<User>();
            ArrayList<User> unfollowedUsers = new ArrayList<User>();
            
            followedUsers = UserDB.selectAllFollowedUsers(user);
            unfollowedUsers = UserDB.selectAllUnfollowedUsers(user);
            tweets = TweetDB.selectAllFollowTweets(user);
                // set User object in request object and set URL
            session.setAttribute("users", followedUsers);
            session.setAttribute("unUsers", unfollowedUsers);
            session.setAttribute("tweets", tweets);
            url = "/home.jsp";
        }
            
        
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
        
            
   }
}
