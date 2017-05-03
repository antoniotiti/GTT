package com.example.antonio.gestiontrabajotemporal.calendarios;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;

public class CalendariosCursorAdapter extends CursorAdapter {

    public CalendariosCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_calendario, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Referencias UI.
        TextView nombreCalendario = (TextView) view.findViewById(R.id.textView_nombre_calendario);
        TextView descripcionCalendario = (TextView) view.findViewById(R.id.textView_descripcion_calendario);

        // Get valores.
        String name = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Calendarios.NOMBRE));
        String descripcion = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Calendarios.DESCRIPCION));

        // Setup.
        nombreCalendario.setText(name);
        descripcionCalendario.setText(descripcion);
    }
}