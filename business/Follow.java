/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.io.Serializable;

/**
 *
 * @author riteshsood
 */
public class Follow implements Serializable{
    
    private String user_ID;
    private String followedUserID;
    private Object followDate;
    
    public void Follow()
    {
        this.user_ID = "";
        this.followedUserID = "";
        this.followDate = null;
    }
    
    public String getUser_ID()
    {
        return this.user_ID;
    }
    public void setUser_ID(String ID)
    {
        this.user_ID = ID;
    }
    public String getFollowUser_ID()
    {
        return this.followedUserID;
    }
    public void setFollowUser_ID(String ID)
    {
        this.followedUserID = ID;
    }
    public Object getfollowDate()
    {
        return this.followDate;
    }
    public void setfollowDate(Object date)
    {
        this.followDate = date;
    }
}
