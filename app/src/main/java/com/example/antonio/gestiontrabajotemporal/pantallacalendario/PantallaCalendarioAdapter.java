package com.example.antonio.gestiontrabajotemporal.pantallacalendario;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Map;

import hirondelle.date4j.DateTime;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.FORMATO_FECHA_DATETIME;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.calcularHoraFormateada;

class PantallaCalendarioAdapter extends CaldroidGridAdapter {

    PantallaCalendarioAdapter(Context context, int month, int year,
                              Map<String, Object> caldroidData,
                              Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // Obtenemos la instancia del adaptador de Base de Datos.
        OperacionesBaseDatos datos = OperacionesBaseDatos.obtenerInstancia(context);

        // Obtenemos el codigo de operario y el calendario de la Actividad
        String codigoOperario = (String) extraData.get("OPERARIO");
        String idCalendario = (String) extraData.get("CALENDARIO");

        // Obtenemos la fecha de la celda
        DateTime dateTime = this.datetimeList.get(position);

        // Formatemamos la fecha
        String fechaFormateada = dateTime.format(FORMATO_FECHA_DATETIME);

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }

        // Obtenemos los paddings de la celda
        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView txtDia = (TextView) cellView.findViewById(R.id.txt_dia);
        TextView txtTurno = (TextView) cellView.findViewById(R.id.txt_turno);
        TextView txtHoraExtra = (TextView) cellView.findViewById(R.id.txt_hora_extra);

        // Establecemos el día correspondiente a cada celda
        txtDia.setText(String.format("%d", dateTime.getDay()));
        txtDia.setTextColor(Color.BLACK);
        txtTurno.setText("");
        txtHoraExtra.setText("");

        // Establecemos el color de la celda remarcada para hoy.
        if (dateTime.equals(getToday())) {
            cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
        }

        // Recuperamos el padding después de establecer el color de fondo con setBackgroundResource
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Obtenemos el fichaje a partir del operario y calendario elegido y el día del fichaje.
        Cursor cursorFichajes = datos.obtenerFichajesParaCalendario(codigoOperario, idCalendario, fechaFormateada);

        if (cursorFichajes.moveToFirst()) {

            String horasExtras = calcularHoraFormateada(cursorFichajes.getFloat(cursorFichajes.getColumnIndex(NombresColumnasBaseDatos.Fichajes.HORA_EXTRA)));
            String abreviaturnaNombreTurno = cursorFichajes.getString(cursorFichajes.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));
            int colorFondoTurno = cursorFichajes.getInt(cursorFichajes.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_FONDO));
            int colorTextoTurno = cursorFichajes.getInt(cursorFichajes.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_TEXTO));

            txtTurno.setText(abreviaturnaNombreTurno);
            txtHoraExtra.setText(horasExtras);

            txtDia.setTextColor(colorTextoTurno);
            txtTurno.setTextColor(colorTextoTurno);
            txtHoraExtra.setTextColor(colorTextoTurno);

            //Si la fecha de la celda coincide con hoy, establecemos un borde para diferenciarla de las demás
            if (dateTime.equals(getToday())) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(3, Color.RED);
                drawable.setCornerRadius(2);
                drawable.setColor(colorFondoTurno);
                cellView.setBackground(drawable);
            } else {
                cellView.setBackgroundColor(colorFondoTurno);
            }

        } else {
            cellView.setBackgroundColor(Color.WHITE);
        }
        cursorFichajes.close();

        // Establecemos el color de los días para el mes anterior y siguiente.
        if (dateTime.getMonth() != month) {
            txtDia.setTextColor(Color.LTGRAY);
            txtTurno.setTextColor(Color.LTGRAY);
            txtHoraExtra.setTextColor(Color.LTGRAY);
            cellView.getBackground().setAlpha(100);
        }

        // Set custom color if required
        setCustomResources(dateTime, cellView, txtDia);
        setCustomResources(dateTime, cellView, txtTurno);
        setCustomResources(dateTime, cellView, txtHoraExtra);

        return cellView;
    }
}