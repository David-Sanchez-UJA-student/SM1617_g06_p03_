package com.example.dimanxe.practica3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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





public class Activid3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spOrigen, spDestino;
    private EditText etFecha;
    Autentication aut=new Autentication();
    Billete billete=new Billete();
    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences pr=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pr=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_activid3);
        spOrigen = (Spinner) findViewById(R.id.sp_Org);
        spDestino = (Spinner) findViewById(R.id.sp_Dest);
        LinkedList<String> resp=new LinkedList<String>();
        Conection con=new Conection();
        con.execute("","");





        /*int c=0;
        for(int i=0;i<resp.size();i++){
            if(resp.get(i).equals("fin")){
                c=1;
            }else{
                if(c==1){
                    destinos.add(resp.get(i));
                }else{
                    origenes.add(resp.get(i));
                }
            }
        }*/



    }

    private void loadspinners(String org,String dest) {
        if(org.equals("")||dest.equals("")){
            Toast.makeText(getApplicationContext(),"Servidor no activo",Toast.LENGTH_LONG).show();
        }
        String[] orig=org.split(";");
        String[] desti=dest.split(";");
        ArrayAdapter adap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, orig);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrigen.setAdapter(adap);
        ArrayAdapter adap2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, desti);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestino.setAdapter(adap2);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void enviarSolicitud(View v) {
        pr=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        etFecha=(EditText)findViewById(R.id.eT_Fecha);
        aut.setmUser(pr.getString("user",""));
        aut.setmPass(pr.getString("PWD",""));
        aut.setmIP(pr.getString("IP",""));
        billete.setDestino(spDestino.getSelectedItem().toString());
        billete.setOrigen(spOrigen.getSelectedItem().toString());
        billete.setFecha(etFecha.getText().toString());
        //Toast.makeText(getApplicationContext(), spOrigen.getSelectedItem().toString() + spDestino.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

        Thread nt=new Thread(new Conect(aut,billete));
        nt.start();
    }

    private class Conection extends AsyncTask<String, Void, LinkedList<String>> {
        public Conection(){

        }
        protected LinkedList<String> doInBackground(String... params) {
            LinkedList<String> res = new LinkedList<String>();
            Socket sClient = null;
            DataOutputStream output = null;
            BufferedReader is = null;
            //String ip=params[0];
            //int port=Integer.parseInt(params[1]);
            try {
                SocketAddress socket = new InetSocketAddress(pr.getString("IP",""), 6000);
                sClient = new Socket();
                sClient.connect(socket, 5000);
                is = new BufferedReader(new InputStreamReader(sClient.getInputStream()));
                output = new DataOutputStream(sClient.getOutputStream());
            } catch (IOException e) {
                System.out.println(e);
            }

            int count = 0;
            if (sClient != null && is != null && output != null) {
                try {
                    output.writeBytes("USER USER\r\n");
                    output.writeBytes("PASS 12345\r\n");
                    output.writeBytes("ORIGEN\r\n");//TODO idem
                    output.writeBytes("DESTINO\r\n");
                    output.writeBytes("QUIT\r\n");
                    String responseLine;
                    while ((responseLine = is.readLine()) != null) {
                        String resp = "";
                        if(responseLine.indexOf("OK Adios")!=-1){
                            break;
                        }
                        if (responseLine.indexOf("OK") != -1) {
                            count += 1;

                        } else if (responseLine.indexOf("SESION-ID")!=-1) {
                            count+=1;
                        }else if(count==3)
                        {


                            resp = responseLine;
                            res.add(resp);


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
            String origenes="";
            String destinos="";
            try{
                origenes= res.get(0);
                destinos= res.get(1);

            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }

            loadspinners(origenes,destinos);

        }
    }
    private class Conect implements Runnable {
        private Autentication aut = new Autentication();
        private Billete bil = new Billete();

        public Conect(Autentication mAut, Billete mBil) {
            this.aut = mAut;
            this.bil = mBil;
        }

        @Override
        public void run() {
            Logeo log = new Logeo();
            log.setUser(aut.getmUser());
            log.setPass(aut.getmPass());
            Socket sClient = null;
            DataOutputStream output = null;

            BufferedReader is = null;
            try {
                SocketAddress sockaddr = new InetSocketAddress(aut.getmIP(), aut.mPort);
                sClient = new Socket();
                sClient.connect(sockaddr, 5000);
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
                    output.writeBytes("1 ORG "+bil.getOrigen()+"\r\n");
                    output.writeBytes("1 DST "+bil.getDestino()+"\r\n");
                    output.writeBytes("1 FCH "+bil.getFecha()+"\r\n");
                    output.writeBytes("QUIT\r\n");
                    String responseLine;
                    while ((responseLine = is.readLine()) != null) {
                        if (count == 4) {
                            resp = responseLine;
                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", resp);
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);
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
            }
        }
        private final Handler handler = new Handler() {

            public void handleMessage(Message msg) {

                String aResponse = msg.getData().getString("message");

                if ((null != aResponse)) {
                    if(aResponse.indexOf("{")==-1){
                        Toast.makeText(getBaseContext(),"No se han encontrado billetes que coincidan con la búsqueda...", Toast.LENGTH_LONG).show();
                    }else{
                        // ALERT MESSAGE
                        Toast.makeText(
                                getBaseContext(),
                                "Server Response: "+aResponse,
                                Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),Activid4.class);
                        intent.putExtra("billetes",aResponse);
                        startActivity(intent);

                    }

                }
                else
                {

                    // ALERT MESSAGE
                    Toast.makeText(
                            getBaseContext(),
                            "Not Got Response From Server.",
                            Toast.LENGTH_LONG).show();
                }

            }
        };
    }
}

