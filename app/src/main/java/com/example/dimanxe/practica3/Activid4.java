package com.example.dimanxe.practica3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class Activid4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activid4);
        Intent intent=getIntent();
        String json=intent.getStringExtra("billetes");
        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        try {
             jsonObject= new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonArray=jsonObject.getJSONArray("billetes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LinkedList<Billete> billetes=new LinkedList<Billete>();
        for (int i=0;i<jsonArray.length();i++){
            Billete bil=new Billete();
            try {
                bil.setOrigen(jsonArray.getJSONObject(i).getString("origen"));
                bil.setDestino(jsonArray.getJSONObject(i).getString("destino"));
                bil.setFecha(jsonArray.getJSONObject(i).getString("fecha"));
                bil.setHora(jsonArray.getJSONObject(i).getString("hora"));
                bil.setIdbillete(Integer.parseInt(jsonArray.getJSONObject(i).getString("IDbillete")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            billetes.add(bil);
        }
        ListView lv=(ListView) findViewById(R.id.lvBilletes);
        lv.setAdapter(new CustomAdapter(this,billetes));
    }

}
