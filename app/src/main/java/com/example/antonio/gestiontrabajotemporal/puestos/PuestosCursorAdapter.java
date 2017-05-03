package com.example.antonio.gestiontrabajotemporal.puestos;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;


public class PuestosCursorAdapter extends CursorAdapter {

    public PuestosCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_puesto, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Referencias UI.
        TextView nombrePuesto = (TextView) view.findViewById(R.id.textView_nombre_puesto);
        TextView descripcionPuesto = (TextView) view.findViewById(R.id.textView_descripcion_puesto);

        // Get valores.
        String name = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));
        String descripcion = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Puestos.DESCRIPCION));

        // Setup.
        nombrePuesto.setText(name);
        descripcionPuesto.setText(descripcion);
    }
}