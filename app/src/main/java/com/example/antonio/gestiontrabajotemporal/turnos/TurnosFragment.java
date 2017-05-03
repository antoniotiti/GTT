package com.example.antonio.gestiontrabajotemporal.turnos;

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
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.turnodetalle.TurnoDetalleActivity;

public class TurnosFragment extends Fragment {

    public static final int REQUEST_ADD_TURNO = 1;
    public static final int REQUEST_UPDATE_DELETE_TURNO = 2;

    OperacionesBaseDatos datos;

    private TurnosCursorAdapter mTurnosAdapter;

    /**
     * Constructor por defecto.
     */
    public TurnosFragment() {
    }

    public static TurnosFragment newInstance() {
        return new TurnosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_turnos, container, false);

        //Obtenemos las referencias de las vistas.
        ListView mTurnosList = (ListView) root.findViewById(R.id.turnos_list);
        FloatingActionButton mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_turnos);

        mTurnosAdapter = new TurnosCursorAdapter(getActivity(), null);

        // Setup
        mTurnosList.setAdapter(mTurnosAdapter);

        // Eventos
        mTurnosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mTurnosAdapter.getItem(i);
                String currentTurnoId = currentItem.getString(currentItem.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));
                showDetailScreen(currentTurnoId);
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
        cargarTurnos();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            cargarTurnos();
        }
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para cargar los turnos.
     */
    private void cargarTurnos() {
        new TurnosLoadTask().execute();
    }

    /**
     * Método encargado de mostrar la pantalla de descripcion del Turno vacía para crear un Turno nuevo.
     */
    private void showAddScreen() {
        Intent intent = new Intent(getActivity(), TurnoDetalleActivity.class);
        startActivityForResult(intent, REQUEST_ADD_TURNO);
    }

    /**
     * Método encargado de mostrar la pantalla de descripción del Turno, mostrando los datos del mismo
     * obtenidos mediante el id del turno seleccionado de la lista.
     *
     * @param turnoId Turno seleccionado de la lista.
     */
    private void showDetailScreen(String turnoId) {
        Intent intent = new Intent(getActivity(), TurnoDetalleActivity.class);
        intent.putExtra(TurnosActivity.EXTRA_TURNO_ID, turnoId); //Enviamos el id del turno para mostrar los detalles
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_TURNO);
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
                mTurnosAdapter.swapCursor(cursor);
            } else {
                Toast.makeText(getActivity(), getString(R.string.no_hay_turnos), Toast.LENGTH_SHORT).show();
            }
        }
    }
}