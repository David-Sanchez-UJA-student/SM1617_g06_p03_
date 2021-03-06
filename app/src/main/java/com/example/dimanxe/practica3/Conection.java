package com.example.dimanxe.practica3;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by dimanxe on 29/11/2016.
 */

public class Conection extends AsyncTask<String,Float,Logeo> {
    public static final String PREFS_NAME = "MyPrefsFile";
    private Context con;
    private FragmentActivity v;
    RelativeLayout linlaHeaderProgress = null;
    RelativeLayout content =null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        linlaHeaderProgress=(RelativeLayout)v.findViewById(R.id.linlaHeaderProgress1);
        content=(RelativeLayout) v.findViewById(R.id.main);
        content.post(new Runnable() {
            @Override
            public void run() {
                content.setVisibility(View.GONE);
            }
        });
        linlaHeaderProgress.post(new Runnable() {
            @Override
            public void run() {
                linlaHeaderProgress.setVisibility(View.VISIBLE);
            }
        });

    }

    Conection (Context con,FragmentActivity v){
        this.con=con;
        this.v=v;
    }


    @Override
    protected Logeo doInBackground(String... params) {
        Logeo log=new Logeo();
        Autentication aut = new Autentication();

        aut.setmUser(params[0]);
        aut.setmPass(params[1]);

        log.setUser(aut.getmUser());
        log.setPass(aut.getmPass());
        aut.setmIP(params[3]);
        aut.mPort = Integer.parseInt(params[2]);
        log.setIp(aut.getmIP());
        Socket sClient = null;
        DataOutputStream output = null;

        BufferedReader is = null;
        try {
            SocketAddress sockaddr= new InetSocketAddress(aut.getmIP(),aut.mPort);
            sClient = new Socket();
            sClient.connect(sockaddr,5000);
            is = new BufferedReader(new InputStreamReader(sClient.getInputStream()));
            output = new DataOutputStream(sClient.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        String resp = "";
        int count = 0;
        if (sClient != null && is != null && output != null) {
            try {
                output.writeBytes("USER "+aut.getmUser()+"\r\n");
                output.writeBytes("PASS "+aut.getmPass()+"\r\n");
                output.writeBytes("QUIT\r\n");
                String responseLine;
                while ((responseLine = is.readLine()) != null) {
                    if (count == 2) {
                        resp = responseLine;
                        sClient.close();
                        break;
                    }
                    if (responseLine.indexOf("OK") != -1) {
                        count += 1;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (resp.indexOf("ERROR") != -1) {
                log.setSid("ERROR");
                log.setExpires("EXPIRED");
                return log;//
            } else {
                String sID = resp.substring(10);
                String[] exp = sID.split("&");
                log.setSid(exp[0]);
                exp=exp[1].split("=");
                log.setExpires(exp[1]);



                return log;//
            }

        }
        return log;
    }

    @Override
    protected void onPostExecute(Logeo s) {
        super.onPostExecute(s);
        linlaHeaderProgress=(RelativeLayout)v.findViewById(R.id.linlaHeaderProgress1);
        content=(RelativeLayout) v.findViewById(R.id.main);
        SharedPreferences.Editor editor=con.getSharedPreferences(PREFS_NAME, con.MODE_PRIVATE).edit();
        editor.putString("user",s.getUser());
        editor.putString("sID", s.getSid());
        editor.putString("Exp",s.getExpires());
        editor.putString("PWD",s.getPass());
        editor.putString("IP",s.getIp());
        editor.commit();
        content.post(new Runnable() {
            @Override
            public void run() {
                content.setVisibility(View.VISIBLE);
            }
        });
        linlaHeaderProgress.post(new Runnable() {
            @Override
            public void run() {
                linlaHeaderProgress.setVisibility(View.GONE);
            }
        });

    }
}
