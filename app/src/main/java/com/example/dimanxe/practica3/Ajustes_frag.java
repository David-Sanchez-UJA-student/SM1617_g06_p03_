package com.example.dimanxe.practica3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dimanxe on 08/01/2017.
 */

public class Ajustes_frag extends Fragment {
    public static final String PREFS_NAME = "MyPrefsFile";
    public Ajustes_frag() {
    }

    public static Ajustes_frag newInstance() {

        Bundle args = new Bundle();

        Ajustes_frag fragment = new Ajustes_frag();
        fragment.setArguments(args);
        return fragment;
    }
    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View frag= inflater.inflate(R.layout.ajustes_frag,container,false);
        SharedPreferences pr=getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final EditText mport=(EditText) frag.findViewById(R.id.et_port);
        final EditText mIP=(EditText) frag.findViewById(R.id.et_IP);
        if(pr.contains("port")&&pr.contains("IP")){
            mport.setText(String.valueOf(pr.getInt("port",6000)));
            mIP.setText(pr.getString("IP",""));
        }

        Button but=(Button) frag.findViewById(R.id.bt_actualizar);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mport.getEditableText().toString().equals("")||mIP.getEditableText().toString().equals("")){
                    Toast.makeText(getContext(),R.string.Falta_IP_puerto,Toast.LENGTH_LONG).show();
                }
                else{
                    SharedPreferences.Editor editor=getContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit();
                    editor.putString("IP",mIP.getText().toString());
                    editor.putInt("port",Integer.parseInt(mport.getEditableText().toString()));
                    editor.commit();
                    Toast.makeText(getActivity(),R.string.Ajustes_cambiados,Toast.LENGTH_LONG).show();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStackImmediate();
                }


            }
        });
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
