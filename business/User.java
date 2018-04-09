/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @javabean for User Entity
 */
public class User implements Serializable {
    //define attributes fullname, ...
    
    //define set/get methods for all attributes.
    private String userID;
    private String fullName;
    private String email;
    private String passWord;
    private String d_of_birth;
    private String SecQuestion;
    private String SecAnswer;
    private Integer numOfTweets;
    private Object last_login_time;
    private String salt;
    
    public User()
    {
        userID = "";
        fullName = "";
        email = "";
        passWord = "";
        d_of_birth = "";
        SecQuestion = "";
        SecAnswer = "";
        salt = "";
        numOfTweets = 0;
    }
    public User(String userID, String name, String email_0, String password_0, String d_of_birth_0, String SecQuest, String SecAn_s, Object login_time, String a_salt)
    {
        this.setUserID(userID);
        this.setFullName(name);
        this.setEmail(email_0);
        this.setPassword(password_0);
        this.setDofBirth(d_of_birth_0);
        this.setSecQuestion(SecQuest);
        this.setSecAnswer(SecAn_s);
        this.setLast_login_time(login_time);
        this.setUserSalt(a_salt);
        this.numOfTweets = 0;
        
    }
    
    public User(String fromString)
    {
        String[] data = fromString.replace("[", "").split(",");
        this.setFullName(data[0]);
        this.setEmail(data[1]);
        this.setPassword(data[2]);
        this.setDofBirth(data[3]);
        this.setSecQuestion(data[4]);
        this.setSecAnswer(data[5]);
    }
    public String getUserSalt()
    {
        return this.salt;
    }
    public void setUserSalt(String asalt)
    {
        this.salt = asalt;
    }
    public String getUserID()
    {
        return this.userID;
    }
    public void setUserID(String ID)
    {
        this.userID = ID;
    }
    public String getFullName()
    {
        return this.fullName;
    }
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
    public String getEmail()
    {
        return this.email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getPassword()
    {
        return this.passWord;
    }
    public void setPassword(String password)
    {
        this.passWord = password;
    }
    public String getDofBirth()
    {
        return this.d_of_birth;
    }
    public void setDofBirth(String date)
    {
        this.d_of_birth = date;
    }
    public String getSecQuestion()
    {
        return this.SecQuestion;
    }
    public void setSecQuestion(String sec_quest)
    {
        this.SecQuestion = sec_quest;
    }
    public String getSecAnswer()
    {
        return this.SecAnswer;
    }
    public void setSecAnswer(String secAnswer)
    {
        this.SecAnswer = secAnswer;
    }
    public void addATweet()
    {
        this.numOfTweets += 1;
    }
    public void removeATweet()
    {
        this.numOfTweets -= 1;
    }
    public void setNumOfTweets(int num)
    {
        this.numOfTweets = num;
    }
    public int getNumOfTweets()
    {
        return this.numOfTweets;
    }
    public void setLast_login_time(Object date)
    {
        this.last_login_time = date;
    }
    public Object getLast_login_time()
    {
        return this.last_login_time;
    }
    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("[%s,%s,%s,%s,%s,%s,%s]", this.getFullName(), this.getEmail(), this.getPassword(), this.getDofBirth(), this.getSecQuestion(), this.getSecAnswer(), this.getNumOfTweets()));
      return sb.toString();
    }
    
    
}
