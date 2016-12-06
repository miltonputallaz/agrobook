package com.putallazmilton.agrobook2.views;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.putallazmilton.agrobook2.R;
import com.putallazmilton.agrobook2.adapters.homeAdapter;
import com.putallazmilton.agrobook2.models.Problema;
import com.putallazmilton.agrobook2.models.Respuesta;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class descripcionActivity extends AppCompatActivity {
    ArrayList<Respuesta> respuestas = new ArrayList<>();


    private Socket mSocket;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);
        TextView tv= (TextView) findViewById(R.id.tvdescripcion2);
        ImageView iv = (ImageView) findViewById(R.id.ivdescripcion);
        Bundle extras= getIntent().getExtras();
        final Problema problema= (Problema) extras.get("problema");
        Button botonenv=(Button) findViewById(R.id.btnenviar);
        final RequestQueue requestq= Volley.newRequestQueue(getApplicationContext());
        try {
            mSocket = IO.socket("http://192.168.1.2:8080");
        } catch (URISyntaxException e) {}

        mSocket.on("agregar_respuesta",onNewAnswer);
        mSocket.connect();
        final EditText edrespuet =(EditText) findViewById(R.id.etrespuesta);

        ll=(LinearLayout) findViewById(R.id.linearlayoutdesc);
        String url="http://192.168.1.2:8080/images/"+problema.getId()+".jpg";
        Picasso.with(descripcionActivity.this).load(url).into(iv);
        JsonObjectRequest joreq = new JsonObjectRequest(GET,"http://192.168.1.2:8080/respuestas/"+problema.getId(),(String)null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                respuestas = parseJson(response);
                Iterator i = respuestas.iterator();
                while(i.hasNext()){
                    Respuesta respuesta =(Respuesta) i.next();


                    View vi = getLayoutInflater().inflate(R.layout.itemdescripcion, null);
                    TextView tvusuario = (TextView) vi.findViewById(R.id.usuariorespuesta);
                    tvusuario.setText(respuesta.getUsuario());
                    TextView tvdesc = (TextView) vi.findViewById(R.id.descripcionrespuesta);
                    tvdesc.setText(respuesta.getDescripcion());

                    ll.addView(vi);


                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestq.add(joreq);






        tv.setText(problema.getDescripcion());


        botonenv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject json = new JSONObject();
                try {
                    json.put("descripcion",edrespuet.getText().toString());
                    json.put("usuario","Jose");
                    json.put("idproblema",problema.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest object= new JsonObjectRequest(POST,"http://192.168.1.2:8080/respuestas",json,new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(final JSONObject response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(descripcionActivity.this);
                        builder.setMessage("Su respuesta se ha publicado con exito!")
                                .setTitle("Exito!!")
                                .setCancelable(false)
                                .setNeutralButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                if (mSocket.connected()){
                                                    mSocket.emit("nueva_respuesta",response);
                                                }
                                                JSONObject data= (JSONObject) response;
                                                String usuario="";
                                                String descripcion="";
                                                try {
                                                    usuario = data.getString("usuario");
                                                    descripcion = data.getString("descripcion");
                                                    View vi = getLayoutInflater().inflate(R.layout.itemdescripcion, null);
                                                    TextView tvusuario = (TextView) vi.findViewById(R.id.usuariorespuesta);
                                                    tvusuario.setText(usuario);
                                                    TextView tvdesc = (TextView) vi.findViewById(R.id.descripcionrespuesta);
                                                    tvdesc.setText(descripcion);

                                                    ll.addView(vi);
                                                } catch (JSONException e) {
                                                    Toast.makeText(getApplicationContext(),"Error en parseo JSON",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestq.add(object);
            }
        });





    }


    private Emitter.Listener onNewAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 JSONObject data = (JSONObject) args[0];
                    String usuario="";
                    String descripcion="";
                    try {
                        usuario = data.getString("usuario");
                        descripcion = data.getString("descripcion");
                        View vi = getLayoutInflater().inflate(R.layout.itemdescripcion, null);
                        TextView tvusuario = (TextView) vi.findViewById(R.id.usuariorespuesta);
                        tvusuario.setText(usuario);
                        TextView tvdesc = (TextView) vi.findViewById(R.id.descripcionrespuesta);
                        tvdesc.setText(descripcion);

                        ll.addView(vi);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Error en parseo JSON",Toast.LENGTH_LONG).show();
                    }




                }




            });
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("agregar_respuesta", onNewAnswer);
    }

    public ArrayList<Respuesta> parseJson(JSONObject jsonObject){
        // Variables locales
        ArrayList<Respuesta> respuestas1 = new ArrayList();
        JSONArray jsonArray= null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("respuestas");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);


                    Respuesta respuesta = new Respuesta(
                            objeto.getString("usuario"),
                            objeto.getString("descripcion"));


                    respuestas1.add(respuesta);

                } catch (JSONException e) {
                    respuestas1=new ArrayList<Respuesta>();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return respuestas1;
    }
}
