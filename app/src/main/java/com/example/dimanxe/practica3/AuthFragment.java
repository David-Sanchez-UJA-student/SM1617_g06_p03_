package com.example.dimanxe.practica3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


/**
 * Created by dimanxe on 06/10/2016.
 */

public class AuthFragment extends Fragment{
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private String mUser="";
    private String mPass="";


    private EditText user = null;
    private EditText pass = null;


    private Autentication autent=new Autentication();

    public AuthFragment(){

    }

    /**
     *
     * @param user
     * @param pass
     * @return
     */
    public static AuthFragment newInstance(String user, String pass) {

        AuthFragment fragment = new AuthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user);
        args.putString(ARG_PARAM2, pass);

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

        }else if (getArguments() != null) {
            mUser = getArguments().getString(ARG_PARAM1);
            mPass = getArguments().getString(ARG_PARAM2);


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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View fragmento = inflater.inflate(R.layout.autfrag, container, false);
        final SharedPreferences pr=getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        redibuja(fragmento);
        user=(EditText)fragmento.findViewById(R.id.Nombre);
        pass=(EditText)fragmento.findViewById(R.id.Pass);

        user.setText(autent.getmUser());
        Button boton = (Button)fragmento.findViewById(R.id.boton1);
        boton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                autent.setmUser(user.getEditableText().toString());
                autent.setmPass(pass.getEditableText().toString());
                if(autent.getmPass().equals("")||autent.getmUser().equals("")){

                    Toast.makeText(getActivity(),R.string.Faltan_datos,Toast.LENGTH_LONG).show();


                }
                else if ((!pr.contains("port")&&!pr.contains("IP"))){
                    Toast.makeText(getActivity(),R.string.Faltan_ajustes,Toast.LENGTH_LONG).show();
                }else if (pr.getString("IP","").equals("")){
                    Toast.makeText(getActivity(),R.string.Falta_ajuste_IP,Toast.LENGTH_LONG).show();
                }else if (pr.getInt("port",0)==0){
                    Toast.makeText(getActivity(),R.string.Falta_ajuste_puerto,Toast.LENGTH_LONG).show();
                }
                else{
                    getActivity().findViewById(R.id.main).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.linlaHeaderProgress1).setVisibility(View.VISIBLE);
                    Logeo log= null;
                    String port=String.valueOf(autent.getServicePort());

                    Conection con= new Conection(getContext(),getActivity());
                    try {
                        log=con.execute(autent.getmUser(),autent.getmPass(),String.valueOf(pr.getInt("port",6000)),pr.getString("IP","")).get();//Ejecuto la tarea asincrona y guardo los resultados en log
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(log.getSid()==null){

                        Toast.makeText(getActivity(),R.string.Server_noConect, Toast.LENGTH_LONG).show();


                    }
                    else{
                        if(!log.getSid().equals("ERROR")){//Si no hay ningun error en la conexion pasamos a la activida que se encarga del servicio.
                            Intent in;
                            in=new Intent(getActivity(),Servicio_Billetes.class);
                            in.putExtra("User",log.getUser());
                            startActivity(in);
                        }else{

                            pass.setText("");
                            Toast.makeText(getActivity(), R.string.Invalid_user, Toast.LENGTH_LONG).show();
                        }

                    }
                }

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




        user.setText(autent.getmUser());
        pass.setText(autent.getmPass());


    }


    /**
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        try{
            user=(EditText)AuthFragment.this.getView().findViewById(R.id.Nombre);
            pass=(EditText)AuthFragment.this.getView().findViewById(R.id.Pass);


            autent.setmUser(user.getEditableText().toString());
            autent.setmPass(pass.getEditableText().toString());

            // mUser=autent.getmUser();
            outState.putString(ARG_PARAM1,autent.getmUser());
            outState.putString(ARG_PARAM2, autent.getmPass());
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        //Se guardan los datos que hubiera en el objeto autent.





    }

}
