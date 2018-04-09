/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.util.Date;
import java.io.Serializable;
//import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 *
 * @author riteshsood
 */
public class Tweet implements Serializable {
    
   
   int TweetID;
   private String email;
   private String fullName;
   private String text;
   private String mentionedUserId;
   private String userID;
   private int H_ID;
   private Object date;
   
   public void Tweet()
    {
        this.fullName = "";
        this.email = "";
        this.text = "";
        //SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        this.mentionedUserId = "";
        this.H_ID = 0;
        this.date = new Date();
    }
    public void Tweet(String name, String email, String someText)
    {
        this.fullName = name;
        this.email = email;
        this.text = someText;
        //SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        //Date now = new Date();
        this.date = new Date();
    }
    public String getUserID()
    {
        return this.userID;
    }
    public void setUserID(String ID)
    {
        this.userID = ID;
    }
    public int getHashID()
    {
        return this.H_ID;
    }
    public void setHashID(int ID)
    {
        this.H_ID = ID;
    }
    public int getTweetID()
    {
        return this.TweetID;
    }
    public void setTweetID(int ID)
    {
        this.TweetID = ID;
    }
    public Object getDate()
    {
        return this.date;
    }
    public void setDate(Object date)
    {
        this.date = date;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String a_email){
        this.email = a_email;
    }
    public String getText(){
        return this.text;
    }
    public void setText(String a_text){
        this.text = a_text;
    }
    public String getFullName(){
        return this.fullName;
    }
    public void setFullName(String name){
        this.fullName = name;
    }
    public String getMentionID(){
        return this.mentionedUserId;
    }
    public void setMentionID(String User){
        this.mentionedUserId = User;
    }
   
}