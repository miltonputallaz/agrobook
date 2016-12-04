package com.putallazmilton.agrobook2.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.putallazmilton.agrobook2.R;
import com.putallazmilton.agrobook2.models.Problema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milton on 13/11/2016.
 */

public class respuestasAdapter extends ArrayAdapter<Problema> {
    private Context context;
    private ArrayList<Problema> problemas;

    public respuestasAdapter(Context context, ArrayList<Problema> problemas) {
        super(context, R.layout.item, problemas);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.problemas = problemas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.itemdescripcion, null);

        TextView tvusuario=(TextView) item.findViewById(R.id.usuariorespuesta);
        tvusuario.setText(problemas.get(position).getUsuario());
        TextView tvdescripcion=(TextView) item.findViewById(R.id.descripcionrespuesta);
        tvdescripcion.setText(problemas.get(position).getDescripcion());

        // Devolvemos la vista para que se muestre en el ListView.
        return item;
    }


}