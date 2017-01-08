package com.example.dimanxe.practica3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;



import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Principal extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        switch (item.getItemId()){

            case R.id.action_favorite:


                Information_frag frag=Information_frag.newInstance();

                if(fm.findFragmentById(R.id.main_frame)==null) {


                    ft.add(R.id.main_frame,frag);
                    ft.addToBackStack(null);
                    ft.commit();
                }else{

                    if(fm.getBackStackEntryCount()>1){
                        fm.popBackStackImmediate();
                    }

                    ft.replace(R.id.main_frame,frag);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            case R.id.action_settings:

                Ajustes_frag frag1= Ajustes_frag.newInstance();
                if(fm.findFragmentById(R.id.main_frame)==null) {


                    ft.add(R.id.main_frame,frag1);
                    ft.addToBackStack(null);
                    ft.commit();
                }else{
                    if(fm.getBackStackEntryCount()>1){
                        fm.popBackStackImmediate();
                    }

                    ft.replace(R.id.main_frame,frag1);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            case R.id.action_salir:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                SharedPreferences.Editor editor=getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit();
                                editor.remove("sID");
                                editor.remove("Exp");
                                editor.commit();
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.DesconectarYSalir).setPositiveButton(R.string.Si, dialogClickListener)
                        .setNegativeButton(R.string.No, dialogClickListener).show();
                break;
        }
    return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        SharedPreferences pr = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Logeo log = new Logeo();
        //se comprueba el estado guardado para que no vuelva a conectar al girar la pantalla
        if (savedInstanceState != null) {
            if (savedInstanceState.getInt("first") == 1) {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.findFragmentById(R.id.main_frame) == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    AuthFragment au = AuthFragment.newInstance(log.getUser(), "");
                    ft.add(R.id.main_frame, au);
                    ft.addToBackStack(null);
                    ft.commit();
                    savedInstanceState.remove("first");
                }

            } else {
                //se comprueba las preferencias en busca de una sesion previa




            }
        } else {
            //se comprueba las preferencias en busca de una sesion previa
            if (pr.getAll().size() > 4) {

                log.setUser(pr.getString("user", "no lo lee"));
                log.setExpires(pr.getString("Exp", ""));
                log.setSid(pr.getString("sID", ""));
                if (!log.getExpires().equals("EXPIRED")) {
                    SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd-H-m-s");
                    long exp = 0;
                    long exp2 = System.currentTimeMillis();
                    try {
                        exp = dt1.parse(log.getExpires()).getTime();
                        //exp2=dt1.parse(log.getExpires()).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (exp2 < exp) {
                        Toast.makeText(this, R.string.Session_rec, Toast.LENGTH_LONG).show();
                        Intent i;
                        i = new Intent(this, Servicio_Billetes.class);
                        i.putExtra("User", log.getUser());
                        startActivity(i);
                    } else {
                        Toast.makeText(this, R.string.Session_end, Toast.LENGTH_LONG).show();
                        //SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        //editor.remove("sID");
                        //editor.remove("Exp");
                        //editor.commit();
                    }
                }FragmentManager fm = getSupportFragmentManager();
                if (fm.findFragmentById(R.id.main_frame) == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    AuthFragment au = AuthFragment.newInstance(log.getUser(), "");
                    ft.add(R.id.main_frame, au);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }else {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.findFragmentById(R.id.main_frame) == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    AuthFragment au = AuthFragment.newInstance(log.getUser(), "");
                    ft.add(R.id.main_frame, au);
                    ft.addToBackStack(null);
                    ft.commit();
                }

            }


        }


    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.Salir, Toast.LENGTH_SHORT).show();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        if(fm.getBackStackEntryCount()>1){
            fm.popBackStackImmediate();
        }//se añade de nuevo el fragmento de autenticación ya que si estamos en ajustes o informacion no vuelve atrás.
        AuthFragment au = AuthFragment.newInstance("", "");
        ft.replace(R.id.main_frame,au);
        ft.addToBackStack(null);
        ft.commit();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("first",1);
        super.onSaveInstanceState(outState);
    }
}
