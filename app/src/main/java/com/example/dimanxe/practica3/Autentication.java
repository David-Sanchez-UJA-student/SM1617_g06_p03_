package com.example.dimanxe.practica3;

/**
 * Created by dimanxe on 29/09/2016.
 */

public class Autentication {

    public static final int SERVICE_PORT=6000;

    protected String mUser="";
    protected String mPass="";
    protected String mIP="";
    protected int mPort=SERVICE_PORT;




    public Autentication(String user, String pass, String IP, int port){
        this.mUser=user;
        this.mPass=pass;
        this.mIP=IP;
        this.mPort=port;
    }
    public Autentication(){

    }

    public String getmUser(){
        return mUser;
    }
    public void setmUser(String user){
        this.mUser=user;
    }
    public String getmPass(){
        return mPass;
    }
    public void setmPass(String pass){
        mPass=pass;
    }
    public String getmIP(){
        return mIP;
    }
    public void setmIP(String ip){
        mIP=ip;
    }
    public int getServicePort() {
        return SERVICE_PORT;
    }

}
