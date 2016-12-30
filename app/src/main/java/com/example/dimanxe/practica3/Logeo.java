package com.example.dimanxe.practica3;

/**
 * Created by dimanxe on 01/12/2016.
 */

public class Logeo {


    private String Sid;
    private String Expires;
    private String user;
    private String pass;

    Logeo(){

    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }
    public String getExpires() {
        return Expires;
    }

    public void setExpires(String expires) {
        Expires = expires;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
