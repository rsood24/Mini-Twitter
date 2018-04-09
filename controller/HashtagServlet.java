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
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author riteshsood
 */
public class HashtagServlet extends HttpServlet {

   protected void doGet(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException {

        HttpSession session = request.getSession();
    
        String action = request.getParameter("action");
       
        String url ="";               
            // get parameters from the request
        if(action.equals("hash"))
        {
            String hash = "#" + request.getParameter("hashTag");
            Hashtag tag = new Hashtag();
            tag.setHashtagText(hash);
            tag = HashtagDB.getTagCount(tag);
            ArrayList<Tweet> a_list = HashtagDB.getHash(tag);
            
            session.setAttribute("hash", a_list);

            url = "/Hashtag.jsp";
        }
            
        
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
        
            
   }
}
