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
import java.util.Random;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import twitter.util.CookieUtil;
import twitter.util.MailUtilGmail;
import twitter.util.PasswordUtil;

/**
 *
 * @author riteshsood
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException {

        HttpSession session = request.getSession();
       
        String url = "";
        String action = request.getParameter("action");                 //retrieve action parameter from request
        if(action.equals("validate"))
        {                                                               //if action is validate check to see if credentials exist
            String email = request.getParameter("A_Email");                        
            String password = request.getParameter("A_Password");       //retrieve inputs
            String remPass = request.getParameter("checkBox");
            
            User user = UserDB.emailExists(email);
            try {
                password = PasswordUtil.hash_SaltPassword(password, user.getUserSalt());
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            Object last_login_time = user.getLast_login_time();
            if(user == null)
            {
               url = "/login_error.jsp";             //how to write message from servlet
               request.setAttribute("message", "Invalid Input");//if file returns null redirect login_error page
            }
            else if(email.equals(user.getEmail())&&password.equals(user.getPassword()))
            {
                ArrayList<Tweet> notTweets = new ArrayList<Tweet>();
                ArrayList<User> notUsers = new ArrayList<User>();
                
                notTweets = TweetDB.selectLoginTweets(user);
                notUsers = UserDB.selectLoginUsers(user);
                
                java.util.Date date = new Date();
                Object param = new java.sql.Timestamp(date.getTime());
                user.setLast_login_time(param);
                session.setAttribute("user", user);  //if they match create a new user and add to file
                UserDB.update(user);
                url = "/home.jsp";
                      //create a cookie for the user
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
                // set User object in request object and set URL
                
                
                if(remPass != null)
                {
                    Cookie pass = new Cookie("pass", user.getPassword());
                    Cookie c = new Cookie("emailAddress", email);
                    c.setMaxAge(60*60*24*365);              
                    c.setPath("/");
                    pass.setMaxAge(60*60*24*365);
                    pass.setPath("/");
                    response.addCookie(c);
                    response.addCookie(pass);
                }

                // set User object in request object and set URL
                session.setAttribute("unUsers", unfollowedUsers);
                session.setAttribute("users", followedUsers);
                session.setAttribute("tweets", tweets);
                session.setAttribute("mytweets", mytweets);
                session.setAttribute("notTweets", notTweets);
                session.setAttribute("notUsers", notUsers);
                session.setAttribute("trend", trend);
                
            }
            else
            {
                url = "/login_error.jsp";             //how to write message from servlet
                request.setAttribute("message", "Invalid Input");
            }
                          
        }
        else if(action.equals("forgotpassword"))
        {
            String email = request.getParameter("passID");
            String subject = "password change";
            String to = email;
            String from = "forgotpassword@twitter.com";
            
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 9) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
            }
            String newPass = salt.toString();
            String body = "Here is your new password " + newPass;
            User user = UserDB.emailExists(email);
            user.setPassword(newPass);
            UserDB.updatePass(user);
            boolean isBodyHTML = false;

            try
            {
                MailUtilGmail.sendMail(to, from, subject, body, isBodyHTML);
            }
            catch (MessagingException e)
            {
                String errorMessage = 
                    "ERROR: Unable to send email. " + 
                        "Check Tomcat logs for details.<br>" +
                    "NOTE: You may need to configure your system " + 
                        "as described in chapter 14.<br>" +
                    "ERROR MESSAGE: " + e.getMessage();
                request.setAttribute("errorMessage", errorMessage);
                this.log(
                    "Unable to send email. \n" +
                    "Here is the email you tried to send: \n" +
                    "=====================================\n" +
                    "TO: " + email + "\n" +
                    "FROM: " + from + "\n" +
                    "SUBJECT: " + subject + "\n" +
                    "\n" +
                    body + "\n\n");
            }            
            url = "/emailSent.jsp";
        }
        
        
        else
        {
            url = "/login_error.jsp";             //how to write message from servlet
            request.setAttribute("message", "Invalid Input");
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);

    }
    @Override
    protected void doGet(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException {
       
        HttpSession session = request.getSession();
        String url = "";
        String action = request.getParameter("action");
        if(action == null)
        {
            Cookie[] cookies = request.getCookies();
            String email = CookieUtil.getCookieValue(cookies, "emailAddress");
            String pass = CookieUtil.getCookieValue(cookies, "pass");
            User user = UserDB.selectUser(email, pass);
            
            if(user == null)
            {
               url = "/login.jsp";             
            }
            else if(email.equals(user.getEmail())&&pass.equals(user.getPassword()))
            {
               
                session.setAttribute("user", user);                         //if they match create a new user and add to file
                url = "/home.jsp";
                      //create a cookie for the user
                
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
                
            }
        }
        else if(action.equals("signout"))
        {     
            Cookie[] coookies;
            coookies = request.getCookies();
            for(Cookie cookie: coookies){
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            session.removeAttribute("users");
            session.removeAttribute("user");
            session.removeAttribute("tweets");
            session.removeAttribute("mytweets");
            url = "/login.jsp";
        }
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}