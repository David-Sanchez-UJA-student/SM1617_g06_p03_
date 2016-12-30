package com.example.dimanxe.practica3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dimanxe.prueba1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Primer extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer);
        SharedPreferences pr=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Logeo log=new Logeo();
        if(pr.getAll().size()>0){

            log.setUser(pr.getString("user","no lo lee"));
            log.setExpires(pr.getString("Exp",""));
            log.setSid(pr.getString("sID",""));
            if (!log.getExpires().equals("EXPIRED")){
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd-H-m-s");
                long exp=0;
                long exp2 = System.currentTimeMillis()+3600000;
                try {
                    exp=dt1.parse(log.getExpires()).getTime()+360000;
                    //exp2=dt1.parse(log.getExpires()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (exp2<exp){
                    Toast.makeText(this,"Se ha recuperado su sesion anterior",Toast.LENGTH_LONG).show();
                    Intent i;
                    i=new Intent(this,Activid3.class);
                    i.putExtra("User",log.getUser());
                    startActivity(i);
                }else{
                    Toast.makeText(this,"Su sesión anterior ha caducado",Toast.LENGTH_LONG).show();
                }
            }



        }

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.main_frame)==null) {
            FragmentTransaction ft = fm.beginTransaction();
            AuthFragment au = AuthFragment.newInstance(log.getUser(), "",6000,"");
            ft.add(R.id.main_frame, au);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    //TODO: hubiera estado bien tener un menú para mostrar algo más de información de la práctica

}
