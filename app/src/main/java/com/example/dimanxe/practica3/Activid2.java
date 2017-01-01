package com.example.dimanxe.practica3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;



import java.util.concurrent.ExecutionException;

public class Activid2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activid2);
        Intent i=getIntent();

        Autentication aut=new Autentication();

        aut.setmUser(i.getStringExtra("User"));
        aut.setmPass(i.getStringExtra("Pass"));
        aut.setmIP(i.getStringExtra("IP"));
        aut.mPort=Integer.parseInt(i.getStringExtra("port"));
        TextView txt =(TextView) this.findViewById(R.id.textView2);
        txt.setText("Nombre:"+aut.getmUser()+"   IP:"+aut.getmIP());
        Logeo log= null;
        String port=String.valueOf(aut.getServicePort());
        Conection con= new Conection(this);
        try {
            log=con.execute(aut.getmUser(),aut.getmPass(),String.valueOf(aut.getServicePort()),aut.getmIP()).get();//Ejecuto la tarea asincrona y guardo los resultados en log
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(log.getSid()==null){
            Intent in;
            in=new Intent(this,Primer.class);
            startActivity(in);
            Toast.makeText(this, "El servidor no esta activo", Toast.LENGTH_LONG).show();
        }
        else{
            if(!log.getSid().equals("ERROR")){//Si no hay ningun error en la conexion pasamos a la activida que se encarga del servicio.
                Intent in;
                in=new Intent(this,Activid3.class);
                in.putExtra("User",log.getUser());
                startActivity(in);
            }else{
                Intent in;
                in=new Intent(this,Primer.class);
                startActivity(in);
                Toast.makeText(this, "Ha habido algún error en los datos de conexión", Toast.LENGTH_LONG).show();
            }

        }

    }


}
