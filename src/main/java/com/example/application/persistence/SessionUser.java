package com.example.application.persistence;

public class SessionUser {
    private boolean logged_in;
    private int user_id;
    private Integer user_level;

    public SessionUser(){}

    public SessionUser(boolean logged_in, Integer user_id)
    {
        this.logged_in = logged_in;
        this.user_id = user_id;
    }

    public void setLoggedIn(boolean logged_in)
    {
        this.logged_in = logged_in;
    }

    public void setUserId(int user_id)
    {
        this.user_id = user_id;
    }

    public void setUserLevel(int user_level)
    {
        this.user_level = user_level;
    }

    public boolean getLoggedIn()
    {
        return this.logged_in;
    }

    public int getUserId()
    {
        return this.user_id;
    }

    public int getUserLevel()
    {
        return this.user_level;
    }

    public String toString()
    {
        return "Logged in: " + this.logged_in + ", Id: " + this.user_id;
    }
}
