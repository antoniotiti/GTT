package com.example.antonio.gestiontrabajotemporal.util;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;


public class DialogoSeleccionTurnoAdapter extends CursorAdapter {

    public DialogoSeleccionTurnoAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.grid_item_turnos, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Referencias UI.
        TextView turnoPrev = (TextView) view.findViewById(R.id.textView_prev_turno);

        // Get valores.
        String abreviaturaNombreTurno = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));
        int colorFondo = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_FONDO));
        int colorTexto = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_TEXTO));

        // Setup.
        turnoPrev.setBackgroundColor(colorFondo);
        turnoPrev.setTextColor(colorTexto);
        turnoPrev.setText(abreviaturaNombreTurno);
    }
}
