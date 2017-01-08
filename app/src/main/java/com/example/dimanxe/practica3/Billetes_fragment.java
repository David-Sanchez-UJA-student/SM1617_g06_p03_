package com.example.dimanxe.practica3;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by dimanxe on 07/01/2017.
 */

public class Billetes_fragment extends Fragment {
    private static final String ARG_PARAM1 = "billetes";;
    ListView lv=null;
    String billetes="";
    public Billetes_fragment(){

    }
    public static Billetes_fragment newInstance(String billetes) {

        Bundle args = new Bundle();

        Billetes_fragment fragment = new Billetes_fragment();
        args.putString(ARG_PARAM1,billetes);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            billetes=savedInstanceState.getString(ARG_PARAM1);

        }else if (getArguments() != null) {
            billetes = getArguments().getString(ARG_PARAM1);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View fragmento=inflater.inflate(R.layout.billetes_fragment,container,false);
        lv= (ListView) fragmento.findViewById(R.id.lvBilletes);
        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        try {
            jsonObject= new JSONObject(billetes);
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
                bil.setPrecio(Float.parseFloat(jsonArray.getJSONObject(i).getString("Precio")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            billetes.add(bil);

        }
        lv.setAdapter(new CustomAdapter(getContext(),billetes));
        return fragmento;
    }

    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(ARG_PARAM1,billetes);
    }
}
