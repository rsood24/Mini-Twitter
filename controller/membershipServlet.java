/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import business.Hashtag;
import business.Tweet;
import business.User;
import dataaccess.HashtagDB;
import dataaccess.TweetDB;
import dataaccess.UserDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import twitter.util.PasswordUtil;

/**
 *
 * @author xl
 */
@WebServlet(name = "membershipServlet", urlPatterns = {"/membership"})
public class membershipServlet extends HttpServlet {

  
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException
   {
       HttpSession session = request.getSession();
        
        // get current action
        String action = request.getParameter("action");
       
        String url ="";
        if (action.equals("add"))
        {                
            // get parameters from the request
            String userID = request.getParameter("tbUserID");
            String fullName = request.getParameter("tbFullname");
            String email = request.getParameter("tbEmail");
            String password = request.getParameter("tbPassword");
            String dateObirth = request.getParameter("tbDoB");
            String SecQuest = request.getParameter("secQuest");
            String SecAns = request.getParameter("Question");
            
            User a_guy = UserDB.emailExists(email);
            User b_guy = UserDB.UserIDExists(userID);
            if(a_guy.getEmail().equals("") && b_guy.getEmail().equals(""))
            {

                if(fullName.equals("")||email.equals("")||password.equals("")||dateObirth.equals("")||SecAns.equals(""))
                {
                    url = "/signup.jsp";
                }
                else
                {                                       // store data in User object and save User object in database
                    
                    java.util.Date date = new Date();
                    Object param = new java.sql.Timestamp(date.getTime());
                    String salt = PasswordUtil.getSalt();
                    try {
                        password = PasswordUtil.hash_SaltPassword(password, salt);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(membershipServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    User user = new User(userID, fullName, email, password, dateObirth, SecQuest, SecAns, param, salt);
                    UserDB.insert(user);
                    Cookie c = new Cookie("emailAddress", URLEncoder.encode(email, "UTF-8"));
                    Cookie d = new Cookie("fullName", URLEncoder.encode(fullName, "UTF-8"));
                    c.setMaxAge(60*60*24*365);
                    d.setMaxAge(60*60*24*365);
                    c.setPath("/");
                    d.setPath("/");
                    response.addCookie(c);
                    response.addCookie(d);
                    ArrayList<User> users = new ArrayList<User>();
                    users = UserDB.selectAllUsers();
                    ArrayList<Tweet> Tweets = new ArrayList<Tweet>();
                    Tweets = TweetDB.selectAllTweets();
                    
                ArrayList<Tweet> tweets = new ArrayList<Tweet>();
                ArrayList<User> followedUsers = new ArrayList<User>();
                ArrayList<User> unfollowedUsers = new ArrayList<User>();
                ArrayList<Tweet> mytweets = new ArrayList<Tweet>();
                ArrayList<Hashtag> trend = new ArrayList<Hashtag>();
            
                trend = HashtagDB.getTrendingHash();
                followedUsers = UserDB.selectAllFollowedUsers(user);
                unfollowedUsers = UserDB.selectAllUnfollowedUsers(user);
                tweets = TweetDB.selectAllFollowTweets(user);
                mytweets = TweetDB.selectUserTweets(user.getEmail(), user.getUserID());
                      
                session.setAttribute("unUsers", unfollowedUsers);
                session.setAttribute("users", followedUsers);
                session.setAttribute("tweets", tweets);
                session.setAttribute("mytweets", mytweets);
                session.setAttribute("trend", trend);
                    //session.setAttribute("tweets", Tweets);
                    
                    // set User object in request object and set URL
                    //session.setAttribute("users", users);
                session.setAttribute("user", user);
                    url = "/home.jsp";
                }
            }
            else
            {
                request.setAttribute("message", "this email is taken");
                url = "/signup_error.jsp";
            }
        }
        else if(action.equals("update"))
        {
            String fullName = request.getParameter("tbFullname");
            String email = request.getParameter("tbEmail");
            String dateObirth = request.getParameter("tbDoB");
            String SecQuest = request.getParameter("secQuest");
            String SecAns = request.getParameter("Question");
            
            if(fullName.equals("")||email.equals("")|| dateObirth.equals("")||SecAns.equals(""))
            {
                url = "/signup.jsp";
            }
            else
            {                                       // store data in User object and save User object in database
                User user = new User();
                user.setEmail(email);
                user.setFullName(fullName);
                user.setDofBirth(dateObirth);
                user.setSecQuestion(SecQuest);
                user.setSecAnswer(SecAns);
                UserDB.update(user);
                TweetDB.updateTweetName(user);
                Cookie c = new Cookie("emailAddress", URLEncoder.encode(email, "UTF-8"));
                Cookie d = new Cookie("fullName", URLEncoder.encode(fullName, "UTF-8"));
                c.setMaxAge(60*60*24*365);
                d.setMaxAge(60*60*24*365);
                c.setPath("/");
                d.setPath("/");
                response.addCookie(c);
                response.addCookie(d);
                ArrayList<User> users = new ArrayList<User>();
                users = UserDB.selectAllUsers();
                ArrayList<Tweet> Tweets = new ArrayList<Tweet>();
                Tweets = TweetDB.selectAllTweets();
                    
                    
                session.setAttribute("tweets", Tweets);
                    
                // set User object in request object and set URL
                session.setAttribute("users", users);
                session.setAttribute("user", user);
                url = "/home.jsp";
            }
        }
        
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
   }
}
