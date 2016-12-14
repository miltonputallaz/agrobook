package com.putallazmilton.agrobook2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.putallazmilton.agrobook2.R;
import com.putallazmilton.agrobook2.models.Problema;
import com.putallazmilton.agrobook2.views.descripcionActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Milton on 10/11/2016.
 */

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.PictureViewHolder> {

    private ArrayList<Problema> problemas;
    private int resource;
    private Activity activity;
    private RequestQueue requestQueue;
    public homeAdapter(int resource, final Activity activity) {

        this.resource = resource;
        this.activity = activity;
        problemas=new ArrayList<Problema>();
       requestQueue= Volley.newRequestQueue(activity.getApplicationContext());

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://192.168.1.2:8080/problemas",
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        problemas = parseJson(response);
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity.getApplicationContext(),"Error en el servidor",Toast.LENGTH_LONG).show();

                    }
                }
        );

        jsArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 500000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 500000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });



        requestQueue.add(jsArrayRequest);
    }



    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new PictureViewHolder(view);
    }

    public ArrayList<Problema> parseJson(JSONObject jsonObject){
        // Variables locales
        ArrayList<Problema> problemas1 = new ArrayList();
        JSONArray jsonArray= null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("problemas");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);
                    Image imagen;

                    Problema problema = new Problema(
                            objeto.getString("descripcion"),
                            objeto.getString("usuario"),
                            objeto.getString("_id"));


                    problemas1.add(problema);

                } catch (JSONException e) {
                    problemas=new ArrayList<Problema>();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return problemas1;
    }

    public void nuevoProblema(Problema problema){
        this.problemas.add(problema);

    }
    @Override
    public void onBindViewHolder(PictureViewHolder holder, final int position) {
        final Problema problema = problemas.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity,descripcionActivity.class);
                intent.putExtra("problema",problema);

                activity.startActivity(intent);
            }
        });
        holder.descripcion.setText(problema.getDescripcion());
        holder.usuario.setText(problema.getUsuario());
        holder.id.setText(String.valueOf(problema.getId()));
        Picasso.with(activity.getApplicationContext()).load("http://192.168.1.2:8080/images/"+problema.getId()+".jpg").into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return problemas.size();
    }


    public class PictureViewHolder extends RecyclerView.ViewHolder{


        private TextView usuario;
        private TextView descripcion;
        private TextView id;
        private ImageView imagen;


        public PictureViewHolder(View itemView) {
            super(itemView);


            usuario    = (TextView) itemView.findViewById(R.id.tvusuario);
            descripcion= (TextView) itemView.findViewById(R.id.tvdescripcion);
            id  = (TextView) itemView.findViewById(R.id.idproblema);
            imagen =(ImageView) itemView.findViewById(R.id.imagen);


        }
    }
}