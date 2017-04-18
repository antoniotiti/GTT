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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.calendarios.CalendariosCursorAdapter;
import com.example.antonio.gestiontrabajotemporal.pantallacalendario.PantallaCalendarioActivity;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Fragmento con diálogo básico
 */
public class DialogoSeleccionCalendario extends DialogFragment {

    Dialog dialog;
    String TAG = "calendario";
    OperacionesBaseDatos datos;
    // Interfaz de comunicación
    OnItemClickListener listener;
    private CalendariosCursorAdapter mDialogoAdapter;

    public DialogoSeleccionCalendario() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder;
        Context mContext = getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_dialogo_seleccion_calendarios, (ViewGroup) getActivity().findViewById(R.id.layout_rootCalendario));

        mDialogoAdapter = new CalendariosCursorAdapter(getActivity(), null);

        ListView listView = (ListView) layout.findViewById(R.id.listviewCalendario);
        listView.setAdapter(mDialogoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Cursor currentItem = (Cursor) mDialogoAdapter.getItem(position);
                String currentCalendarioId = currentItem.getString(currentItem.getColumnIndex(NombresColumnasBaseDatos.Calendarios.ID));

                listener.onItemClick(currentCalendarioId, TAG);
                dialog.dismiss();
            }
        });

        ImageView close = (ImageView) layout.findViewById(R.id.closeCalendario);
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
        cargarCalendarios();

        return dialog;
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para cargar los calendarios.
     */
    private void cargarCalendarios() {
        new CalendariosLoadTask().execute();
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
        void onItemClick(String currentCalendarioId, String tag);// Eventos Botón Positivo
    }

    /**
     * Clase asíncrona encargada de cargar los calendarios de la base de datos a la lista.
     */
    private class CalendariosLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return datos.obtenerCalendarios();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mDialogoAdapter.swapCursor(cursor);
            } else {
                Toast.makeText(getActivity(),
                        "No hay Calendarios", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
