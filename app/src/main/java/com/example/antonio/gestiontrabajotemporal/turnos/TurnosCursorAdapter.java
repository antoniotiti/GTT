package com.example.antonio.gestiontrabajotemporal.turnos;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;


public class TurnosCursorAdapter extends CursorAdapter {

    public TurnosCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_turnos, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Referencias UI.
        TextView nameText = (TextView) view.findViewById(R.id.textView_nombre_turno);
        TextView turnoPrev = (TextView) view.findViewById(R.id.textView_prev_turno);

        // Get valores.
        String nombreTurno = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.NOMBRE));
        String abreviaturaNombreTurno = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));
        int colorFondo = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_FONDO));
        int colorTexto = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_TEXTO));

        // Setup.
        nameText.setText(nombreTurno);
        turnoPrev.setBackgroundColor(colorFondo);
        turnoPrev.setTextColor(colorTexto);
        turnoPrev.setText(abreviaturaNombreTurno);
    }
}
