package com.example.dimanxe.practica3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;



/**
 * Created by dimanxe on 06/10/2016.
 */

public class AuthFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3= "param3";
    private static final String ARG_PARAM4= "param4";


    private String mUser="";
    private String mPass="";
    private int mPort=0;
    private String mIP="";

    private EditText user = null;
    private EditText pass = null;
    private EditText ip=null;
    private EditText port=null;

    private Autentication autent=new Autentication();

    public AuthFragment(){

    }

    /**
     *
     * @param user
     * @param pass
     * @return
     */
    public static AuthFragment newInstance(String user, String pass, int port, String IP) {

        AuthFragment fragment = new AuthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user);
        args.putString(ARG_PARAM2, pass);
        args.putInt(ARG_PARAM3,port);
        args.putString(ARG_PARAM4,IP);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Si hay algo guardado previamente con savedInstanceState lo obtenemos. Si no buscamos a ver si hay algun argumento guardado.
        if(savedInstanceState!=null) {
            autent.setmUser(savedInstanceState.getString(ARG_PARAM1));
            autent.setmPass(savedInstanceState.getString(ARG_PARAM2));
            autent.mPort=savedInstanceState.getInt(ARG_PARAM3);
            autent.setmIP(savedInstanceState.getString(ARG_PARAM4));
        }else if (getArguments() != null) {
            mUser = getArguments().getString(ARG_PARAM1);
            mPass = getArguments().getString(ARG_PARAM2);
            mPort = getArguments().getInt(ARG_PARAM3);
            mIP = getArguments().getString(ARG_PARAM4);

        }

    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View fragmento = inflater.inflate(R.layout.autfrag, container, false);

        redibuja(fragmento);
        user=(EditText)fragmento.findViewById(R.id.Nombre);
        pass=(EditText)fragmento.findViewById(R.id.Pass);
        port=(EditText)fragmento.findViewById(R.id.port);
        ip=(EditText)fragmento.findViewById(R.id.IP);
        user.setText(autent.getmUser());
        Button boton = (Button)fragmento.findViewById(R.id.boton1);
        boton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                autent.setmUser(user.getEditableText().toString());
                autent.setmPass(pass.getEditableText().toString());
                autent.setmIP(ip.getEditableText().toString());
                autent.mPort=Integer.parseInt(port.getEditableText().toString());
                Intent i;
                i = new Intent(fragmento.getContext(),Activid2.class);
                i.putExtra("User",autent.getmUser());
                i.putExtra("Pass",autent.getmPass());
                i.putExtra("IP",autent.getmIP());
                i.putExtra("port","6000");
                startActivity(i);
            }

                                 });


        return fragmento;

    }

    /**
     *
     * @param fragmento
     */
    private void redibuja(View fragmento){
        user=(EditText)fragmento.findViewById(R.id.Nombre);
        pass=(EditText)fragmento.findViewById(R.id.Pass);
        port=(EditText)fragmento.findViewById(R.id.port);
        ip=(EditText)fragmento.findViewById(R.id.IP);



        user.setText(autent.getmUser());
        pass.setText(autent.getmPass());
        port.setText("6000");
        ip.setText(autent.getmIP());

    }

    /**
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //Se guardan los datos que hubiera en el objeto autent.
        user=(EditText)AuthFragment.this.getView().findViewById(R.id.Nombre);
        pass=(EditText)AuthFragment.this.getView().findViewById(R.id.Pass);
        port=(EditText)AuthFragment.this.getView().findViewById(R.id.port);
        ip=(EditText)AuthFragment.this.getView().findViewById(R.id.IP);

        autent.setmUser(user.getEditableText().toString());
        autent.setmPass(pass.getEditableText().toString());
        autent.mPort=Integer.parseInt(port.getEditableText().toString());
        autent.setmIP(ip.getEditableText().toString());
        // mUser=autent.getmUser();
        outState.putString(ARG_PARAM1,autent.getmUser());
        outState.putString(ARG_PARAM2, autent.getmPass());
        outState.putInt(ARG_PARAM3,autent.mPort);
        outState.putString(ARG_PARAM4,autent.getmIP());



    }

}
