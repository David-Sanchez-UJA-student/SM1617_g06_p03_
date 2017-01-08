package com.example.dimanxe.practica3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;





public class Servicio_Billetes extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spOrigen, spDestino;

    Autentication aut=new Autentication();
    Billete billete=new Billete();
    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences pr=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        pr=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_servicio_billetes);
        spOrigen = (Spinner) findViewById(R.id.sp_Org);
        spDestino = (Spinner) findViewById(R.id.sp_Dest);
        Conection con=new Conection();
        con.execute("","");
    }

    //Metodo encargado de cargar los spinners dinamicamente
    private void loadspinners(String org,String dest) {

        String[] orig=org.split(";");
        String[] desti=dest.split(";");
        ArrayAdapter adap = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1, orig);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrigen.setAdapter(adap);
        ArrayAdapter adap2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1, desti);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestino.setAdapter(adap2);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //Metodo de onclick encargado de buscar y generar los billetes
    public void enviarSolicitud(View v) {
        pr=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        EditText etFecha=(EditText)findViewById(R.id.eT_Fecha);
        aut.setmUser(pr.getString("user",""));
        aut.setmPass(pr.getString("PWD",""));
        aut.setmIP(pr.getString("IP",""));
        billete.setDestino(spDestino.getSelectedItem().toString());
        billete.setOrigen(spOrigen.getSelectedItem().toString());
        billete.setFecha(etFecha.getText().toString());
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
        if(billete.getFecha().equals("")){
            Toast.makeText( this,R.string.Falta_fecha,Toast.LENGTH_LONG).show();

        }else{
            Thread nt=new Thread(new Conect(aut,billete));
            nt.start();
        }


    }

    //Esta clase es la encargada de realizar la peticion al servidor de origenes y destinos para rellenar los spinners
    private class Conection extends AsyncTask<String, Void, LinkedList<String>> {
        RelativeLayout linlaHeaderProgress = (RelativeLayout) findViewById(R.id.linlaHeaderProgress);
        LinearLayout billetesLayout=(LinearLayout)findViewById(R.id.Billetes_layout);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linlaHeaderProgress.post(new Runnable() {
                @Override
                public void run() {
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                }
            });
            billetesLayout.post(new Runnable() {
                @Override
                public void run() {
                    billetesLayout.setVisibility(View.GONE);
                }
            });


        }

        Conection(){

        }
        protected LinkedList<String> doInBackground(String... params) {

            LinkedList<String> res = new LinkedList<>();
            Socket sClient = null;
            DataOutputStream output = null;
            BufferedReader is = null;

            try {
                SocketAddress socket = new InetSocketAddress(pr.getString("IP",""), pr.getInt("port",0));
                sClient = new Socket();
                sClient.connect(socket, 5000);
                is = new BufferedReader(new InputStreamReader(sClient.getInputStream()));
                output = new DataOutputStream(sClient.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int count = 0;
            if (is != null && output != null) {
                try {
                    output.writeBytes("USER USER\r\n");//Como se esta haciendo peticion de los origenes y destinos usaremos un usuario estandar para la aplicacion que solo tendra permisos de lectura en la BBDD en las tablas en las que esten almacenados los origenes y destinos.
                    output.writeBytes("PASS 12345\r\n");
                    output.writeBytes("ORIGEN\r\n");
                    output.writeBytes("DESTINO\r\n");
                    output.writeBytes("QUIT\r\n");
                    String responseLine;
                    while ((responseLine = is.readLine()) != null) {
                        String resp ;
                        if(responseLine.contains("OK Adios")){
                            break;
                        }
                        if (responseLine.contains("OK")) {
                            count += 1;

                        } else if (responseLine.contains("SESION-ID")) {
                            count+=1;
                        }else if(count==3)
                        {

                            if(responseLine.equals("ERROR")){

                                resp="";
                                res.add(resp);
                            }else{
                                resp = responseLine;
                                res.add(resp);
                            }



                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                sClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }
        protected void onPostExecute(LinkedList<String> res){
            super.onPostExecute(res);
            if(res.getFirst().equals("")){
                Toast.makeText(getApplicationContext(),R.string.Server_noConect,Toast.LENGTH_LONG).show();
                finish();
            }
            String origenes="";
            String destinos="";
            try{
                origenes= res.get(0);
                destinos= res.get(1);

            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }

            loadspinners(origenes,destinos);
            linlaHeaderProgress.post(new Runnable() {
                @Override
                public void run() {
                    linlaHeaderProgress.setVisibility(View.GONE);
                }
            });
            billetesLayout.post(new Runnable() {
                @Override
                public void run() {
                    billetesLayout.setVisibility(View.VISIBLE);
                }
            });

        }
    }

    //Esta clase es la encargada de realizar la peticion de los billetes
    private class Conect implements Runnable {
        private Autentication aut = new Autentication();
        private Billete bil = new Billete();

        Conect(Autentication mAut, Billete mBil) {
            this.aut = mAut;
            this.bil = mBil;
        }

        @Override
        public void run() {
            com.example.dimanxe.practica3.Message mes= new com.example.dimanxe.practica3.Message();
            final RelativeLayout linlaHeaderProgress = (RelativeLayout) findViewById(R.id.linlaHeaderProgress);
            final LinearLayout billetesLayout=(LinearLayout)findViewById(R.id.Billetes_layout);
            linlaHeaderProgress.post(new Runnable() {
                @Override
                public void run() {
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                }
            });
            billetesLayout.post(new Runnable() {
                @Override
                public void run() {
                    billetesLayout.setVisibility(View.GONE);
                }
            });

            Logeo log = new Logeo();
            log.setUser(aut.getmUser());
            log.setPass(aut.getmPass());
            Socket sClient = null;
            DataOutputStream output = null;
            SharedPreferences pr=getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            BufferedReader is = null;
            try {
                SocketAddress sockaddr = new InetSocketAddress(pr.getString("IP",""), pr.getInt("port",0));
                sClient = new Socket();
                sClient.connect(sockaddr, 5000);
                is = new BufferedReader(new InputStreamReader(sClient.getInputStream()));
                output = new DataOutputStream(sClient.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String resp;
            int count = 0;
            if (is != null && output != null) {
                try {
                    output.writeBytes("USER "+aut.getmUser()+"\r\n");
                    output.writeBytes("PASS "+aut.getmPass()+"\r\n");
                    mes.setEstado(1);
                    mes.setHeader("ORG");
                    mes.setData(bil.getOrigen());
                    output.writeBytes(mes.toMessage());
                    mes.setHeader("DST");
                    mes.setData(bil.getDestino());
                    output.writeBytes(mes.toMessage());
                    mes.setHeader("FCH");
                    mes.setData(bil.getFecha());
                    output.writeBytes(mes.toMessage());
                    output.writeBytes("QUIT\r\n");
                    String responseLine;
                    //ya que los origenes y destinos estan definidos desde la base de datos y usuario y contraseña son los que introdujo el cliente en la actividad anterior
                    // no habrá problemas al ejecutar los comandos anteriores, por lo que no hará falta buscar errores en las respuestas del servidor.
                    while ((responseLine = is.readLine()) != null) {
                        if (count == 4) {
                            resp = responseLine;
                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", resp);
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);
                            linlaHeaderProgress.post(new Runnable() {
                                @Override
                                public void run() {
                                    linlaHeaderProgress.setVisibility(View.GONE);
                                }
                            });
                            billetesLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    billetesLayout.setVisibility(View.VISIBLE);
                                }
                            });
                            sClient.close();
                            break;
                        }
                        if (responseLine.contains("OK")) {
                            count += 1;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //si no hay conexion con el servidor se envia error como mensaje al handler.
                Message msg=handler.obtainMessage();
                Bundle bundle=new Bundle();
                bundle.putString("message","ERROR");
                msg.setData(bundle);
                handler.sendMessage(msg);
                linlaHeaderProgress.post(new Runnable() {
                    @Override
                    public void run() {
                        linlaHeaderProgress.setVisibility(View.GONE);
                    }
                });
                billetesLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        billetesLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
        private  final Handler handler = new Handler() {

            public void handleMessage(Message msg) {

                String aResponse = msg.getData().getString("message");

                if ((null != aResponse)) {//Nunca deberia de ser null
                    if(aResponse.equals("ERROR")){
                        Toast.makeText(getBaseContext(),R.string.Server_noConect, Toast.LENGTH_LONG).show();
                    }else if(aResponse.indexOf("{",2)==-1){
                        Toast.makeText(getApplicationContext(),R.string.Fecha_inv,Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Si no hay mensaje de error y hay billetes se llama al fragmento que los genera
                        FragmentManager fm = getSupportFragmentManager();
                        if(fm.findFragmentById(R.id.billetes_frame)==null) {
                            FragmentTransaction ft = fm.beginTransaction();
                            Billetes_fragment bi=Billetes_fragment.newInstance(aResponse);
                            if(fm.getBackStackEntryCount()>1){
                                fm.popBackStackImmediate();
                            }
                            ft.replace(R.id.billetes_frame, bi);
                            ft.addToBackStack(null);
                            ft.commit();
                        }


                    }

                }


            }
        };
    }
}

