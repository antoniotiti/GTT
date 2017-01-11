package com.example.antonio.gestiontrabajotemporal.turnos1;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Turnos;


public class TurnosCursorAdapter1 extends CursorAdapter {

    public TurnosCursorAdapter1(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup  viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_turno1, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
// Referencias UI.
        TextView nameText = (TextView) view.findViewById(R.id.textView_nombre_turno1);
       // Button turnoLista=(Button)view.findViewById(R.id.btn_turno_lista);


        // Get valores.
        String nombreTurno = cursor.getString(cursor.getColumnIndex(Turnos.NOMBRE));
        String abreviaturaNombreTurno = cursor.getString(cursor.getColumnIndex(Turnos.ABREVIATURA_NOMBRE_TURNO));
        int colorFondo = cursor.getInt(cursor.getColumnIndex(Turnos.COLOR_FONDO));
        int colorTexto = cursor.getInt(cursor.getColumnIndex(Turnos.COLOR_TEXTO));

        // Setup.
        nameText.setText(nombreTurno);
       // turnoLista.setBackgroundColor(colorFondo);
       // turnoLista.setTextColor(colorTexto);
       // turnoLista.setText(abreviaturaNombreTurno);
    }
}
