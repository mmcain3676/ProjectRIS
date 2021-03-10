package com.example.application.persistence;

public class PostMsg {
    private String msg;

    public PostMsg(){}

    public PostMsg(String msg)
    {
        this.msg = msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getMsg()
    {
        return this.msg;
    }
}
