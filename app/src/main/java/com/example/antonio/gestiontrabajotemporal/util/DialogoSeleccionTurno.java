package com.example.antonio.gestiontrabajotemporal.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.pantallacalendario.PantallaCalendarioActivity;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Diálogo para mostrar los Turnos que existen en la BBDD
 */
public class DialogoSeleccionTurno extends DialogFragment {

    Dialog dialog;
    String TAG = "turno";
    OperacionesBaseDatos datos;
    // Interfaz de comunicación
    OnItemClickListener listener;
    private DialogoSeleccionTurnoAdapter mDialogoAdapter;

    public DialogoSeleccionTurno() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder;
        Context mContext = getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_dialogo_seleccion_turnos, (ViewGroup) getActivity().findViewById(R.id.layout_rootTurnos));

        mDialogoAdapter = new DialogoSeleccionTurnoAdapter(getActivity(), null);

        GridView gridview = (GridView) layout.findViewById(R.id.gridviewTurnos);
        gridview.setAdapter(mDialogoAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Cursor currentItem = (Cursor) mDialogoAdapter.getItem(position);
                String currentTurnoId = currentItem.getString(currentItem.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));

                listener.onItemClick(currentTurnoId, TAG);
                dialog.dismiss();
            }
        });

        ImageView close = (ImageView) layout.findViewById(R.id.closeTurnos);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity());

        // Carga de datos
        cargarTurnos();

        return dialog;
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para cargar los turnos.
     */
    private void cargarTurnos() {
        new TurnosLoadTask().execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnItemClickListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + getString(R.string.error_OnItemClickListener));
        }
    }


    /**
     * {@link PantallaCalendarioActivity#onItemClick(String, String)}
     */
    public interface OnItemClickListener {
        void onItemClick(String currentTurnoId, String tag);// Eventos Botón Positivo
    }

    /**
     * Clase asíncrona encargada de cargar los turnos de la base de datos a la lista.
     */
    private class TurnosLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return datos.obtenerTurnos();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mDialogoAdapter.swapCursor(cursor);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_hay_turnos), Toast.LENGTH_SHORT).show();
            }
        }
    }
}