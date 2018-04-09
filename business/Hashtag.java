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
 * @author riteshsood
 */
public class Hashtag implements Serializable{
    
    private int hashtagID;
    private String hashtagText;
    private int hashtagCount;
    
    public void Hashtag()
    {
        this.hashtagID = 0;
        this.hashtagText = "";
        this.hashtagCount = 0;
    }
    
    public int getHashtagID()
    {
        return this.hashtagID;
    }
    public void setHashtagID(int ID)
    {
        this.hashtagID = ID;
    }
    public String getHashtagText()
    {
        return this.hashtagText;
    }
    public void setHashtagText(String text)
    {
        this.hashtagText = text;
    }
    public int getHashtagCount()
    {
        return this.hashtagCount;
    }
    public void setHashtagCount(int count)
    {
        this.hashtagCount = count;
    }
}
