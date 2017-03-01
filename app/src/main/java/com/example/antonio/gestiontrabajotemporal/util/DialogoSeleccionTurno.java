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
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.turnos.TurnosFragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Fragmento con diálogo básico
 */
public class DialogoSeleccionTurno extends DialogFragment {

    Dialog dialog;
    private DialogoSeleccionTurnoAdapter mDialogoAdapter;

    OperacionesBaseDatos datos;

    public DialogoSeleccionTurno() {
    }

    public interface OnItemClickListener {
        void onItemClick(String currentTurnoId);// Eventos Botón Positivo
    }

    // Interfaz de comunicación
   OnItemClickListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder;
        Context mContext = getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_dialogo_seleccion_turnos, (ViewGroup) getActivity().findViewById(R.id.layout_root));

        mDialogoAdapter = new DialogoSeleccionTurnoAdapter(getActivity(), null);

        GridView gridview = (GridView) layout.findViewById(R.id.gridview);
        gridview.setAdapter(mDialogoAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Cursor currentItem = (Cursor) mDialogoAdapter.getItem(position);
                String currentTurnoId = currentItem.getString(currentItem.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));

                listener.onItemClick(currentTurnoId);
                dialog.dismiss();


               /* String currentTurnoId = currentItem.getString(currentItem.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));
                String currentTurnoAbreviatura = currentItem.getString(currentItem.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));
                Toast.makeText(v.getContext(), currentTurnoAbreviatura, Toast.LENGTH_SHORT).show();*/
            }
        });

        ImageView close = (ImageView) layout.findViewById(R.id.close);
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
                Toast.makeText(getActivity(),
                        "No hay turnos", Toast.LENGTH_SHORT).show();
                // cargarTurnos();//TODO no se si va, cuando no haya turnos recargar para no mostrar nada?
                //getActivity().finish();
            }
        }
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
}
