package com.example.dimanxe.practica3;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * Created by dimanxe on 01/01/2017.
 */


    public class CustomAdapter extends BaseAdapter {
        public static final String PREFS_NAME = "MyPrefsFile";
        LinkedList<Billete> bils;
        Context context;
        int [] imageId;
        private static LayoutInflater inflater=null;
        public CustomAdapter(Context mainActivity, LinkedList<Billete> bills) {
            // TODO Auto-generated constructor stub
            bils=new LinkedList<Billete>(bills);

            context=mainActivity;

            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return bils.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView tv1,tv2,tv3;

        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            final SharedPreferences pr=context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
            rowView = inflater.inflate(R.layout.billetes_list, null);
            holder.tv1=(TextView) rowView.findViewById(R.id.tv1);
            holder.tv2=(TextView) rowView.findViewById(R.id.tv2);
            holder.tv3=(TextView) rowView.findViewById(R.id.tv3);

            holder.tv1.setText(bils.get(position).getOrigen()+"-"+bils.get(position).getDestino());
            holder.tv2.setText(context.getString(R.string.fecha) +":\t"+bils.get(position).getFecha()+"\t"+bils.get(position).getHora());
            holder.tv3.setText(context.getString(R.string.IDBillete) +"\t"+bils.get(position).getIdbillete()+"\t"+context.getString(R.string.precio)+bils.get(position).getPrecio()+context.getString(R.string.euro));
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, context.getString(R.string.Enhorabuena)+" "+pr.getString("user","")+context.getString(R.string.Compra)+"\r\n"+bils.get(position).getOrigen()+"-"+bils.get(position).getDestino()+" "+context.getString(R.string.fecha) +":\t"+bils.get(position).getFecha()+"\t"+bils.get(position).getHora(), Toast.LENGTH_LONG).show();
                }
            });
            return rowView;
        }

    }

