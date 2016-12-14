package com.putallazmilton.agrobook2.socket;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.putallazmilton.agrobook2.R;
import com.putallazmilton.agrobook2.adapters.homeAdapter;
import com.putallazmilton.agrobook2.models.Problema;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Milton on 09/12/2016.
 */

public class    sockets {

    private static sockets instanciaUnica;
    private Context contexthome;
    private Context contextdesc;
    private Activity activityhome;
    private Activity activitydesc;
    private LinearLayout ll;
    private Socket mSocket;
    private RecyclerView rv;

    private sockets() {}



    public Socket getmSocket() {
        return mSocket;
    }

    public void conectarSocket(){

        try {
            mSocket = IO.socket("http://192.168.1.3:8080");
        } catch (URISyntaxException e) {}
        mSocket.on("agregar_respuesta",onNewAnswer);
        mSocket.on("agregar_problema", onNewProblem);
        mSocket.connect();

    }

    public void setContextdesc(Context contextdesc) {
        this.contextdesc = contextdesc;
    }

    public void setContexthome(Context contexthome) {
        this.contexthome = contexthome;
    }

    public void setActivityhome(Activity activityhome) {
        this.activityhome = activityhome;
    }

    public void setActivitydesc(Activity activitydesc) {
        this.activitydesc = activitydesc;
    }

    public Context getContextdesc() {
        return contextdesc;
    }

    public Context getContexthome() {
        return contexthome;
    }

    public Activity getActivityhome() {
        return activityhome;
    }

    public Activity getActivitydesc() {
        return activitydesc;
    }

    private Emitter.Listener onNewProblem = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activityhome.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //rv.setAdapter(new homeAdapter(R.layout.item,activityhome));
                    JSONObject data = (JSONObject) args[0];
                    String descripcion="";
                    String usuario="";
                    String id="";
                    try {
                        Problema problema=new Problema();
                        problema.setId(data.getString("_id"));
                        problema.setDescripcion(data.getString("descripcion"));
                        problema.setUsuario(data.getString("usuario"));

                        homeAdapter adapter=(homeAdapter) rv.getAdapter();
                        adapter.nuevoProblema(problema);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(contextdesc,"Error en parseo JSON",Toast.LENGTH_LONG).show();
                    }
                }




            });
        }
    };

    private Emitter.Listener onNewAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activitydesc.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String usuario="";
                    String descripcion="";
                    try {
                        usuario = data.getString("usuario");
                        descripcion = data.getString("descripcion");
                        View vi = activitydesc.getLayoutInflater().inflate(R.layout.itemdescripcion, null);
                        TextView tvusuario = (TextView) vi.findViewById(R.id.usuariorespuesta);
                        tvusuario.setText(usuario);
                        TextView tvdesc = (TextView) vi.findViewById(R.id.descripcionrespuesta);
                        tvdesc.setText(descripcion);

                        ll.addView(vi);
                    } catch (JSONException e) {
                        Toast.makeText(contextdesc,"Error en parseo JSON",Toast.LENGTH_LONG).show();
                    }




                }




            });
        }
    };

    public void setLl(LinearLayout ll) {
        this.ll = ll;
    }

    public void setRv(RecyclerView rv) {
        this.rv = rv;
    }



    public LinearLayout getLl() {
        return ll;
    }

    public RecyclerView getRv() {
        return rv;
    }

    private synchronized static void createInstance() {
        if (instanciaUnica == null) {
            instanciaUnica = new sockets();
        }
    }

    public static sockets getInstance() {
        createInstance();

        return instanciaUnica;
    }



}
