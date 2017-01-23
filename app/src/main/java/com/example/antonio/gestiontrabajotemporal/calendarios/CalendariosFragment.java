package com.example.antonio.gestiontrabajotemporal.calendarios;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.calendariodetalle.CalendarioDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.calendarios.CalendariosActivity;
import com.example.antonio.gestiontrabajotemporal.calendarios.CalendariosCursorAdapter;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

public class CalendariosFragment extends Fragment {

    public static final int REQUEST_ADD_CALENDARIO = 1;
    public static final int REQUEST_UPDATE_DELETE_CALENDARIO = 2;

    OperacionesBaseDatos datos;

    private CalendariosCursorAdapter mCalendariosAdapter;

    /**
     * Constructor por defecto.
     */
    public CalendariosFragment() {
        // Required empty public constructor
    }

    public static CalendariosFragment newInstance() {
        return new CalendariosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendarios, container, false);

        // Referencias UI
        ListView mCalendariosList = (ListView) root.findViewById(R.id.calendarios_list);
        mCalendariosAdapter = new CalendariosCursorAdapter(getActivity(), null);
        FloatingActionButton mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_calendario);

        // Setup
        mCalendariosList.setAdapter(mCalendariosAdapter);

        // Eventos
        mCalendariosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mCalendariosAdapter.getItem(i);
                String currentCalendarioId = currentItem.getString(currentItem.getColumnIndex(NombresColumnasBaseDatos.Calendarios.ID));
                showDetailScreen(currentCalendarioId);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddScreen();
            }
        });

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity());

        // Carga de datos
        cargarCalendarios();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            cargarCalendarios();
            /*switch (requestCode) {
                case AddEditLawyerActivity.REQUEST_ADD_CALENDARIO:
                    Toast.makeText(getActivity(),"Calendario guardado correctamente", Toast.LENGTH_SHORT).show();
                    cargarCalendarios();
                    break;
                case REQUEST_UPDATE_DELETE_CALENDARIO:
                    Toast.makeText(getActivity(),"Calendario eliminado correctamente", Toast.LENGTH_SHORT).show();
                    cargarCalendarios();
                    break;
            }*/
        }
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para cargar los calendarios.
     */
    private void cargarCalendarios() {
        new CalendariosLoadTask().execute();
    }

    /**
     * Método encargado de mostrar la ventana de descripcionPuesto del Calendario vacía para crear un Calendario nuevo.
     */
    private void showAddScreen() {
        Intent intent = new Intent(getActivity(), CalendarioDetalleActivity.class);
        startActivityForResult(intent, REQUEST_ADD_CALENDARIO);
    }

    /**
     * Método encargado de mostrar la ventana de descripcionPuesto del Calendario, mostrando los datos del mismo
     * obtenidos mediante el id del calendario seleccionado de la lista, para editarlo.
     *
     * @param calendarioId Calendario seleccionado de la lista.
     */
    private void showDetailScreen(String calendarioId) {
        Intent intent = new Intent(getActivity(), CalendarioDetalleActivity.class);
        intent.putExtra(CalendariosActivity.EXTRA_CALENDARIO_ID, calendarioId);
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_CALENDARIO);
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
                mCalendariosAdapter.swapCursor(cursor);
            } else {
                Toast.makeText(getActivity(),
                        "No hay calendarios", Toast.LENGTH_SHORT).show();
            }
        }
    }
}