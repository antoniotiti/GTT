package com.example.antonio.gestiontrabajotemporal.puestos;

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
import com.example.antonio.gestiontrabajotemporal.puestodetalle.PuestoDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

public class PuestosFragment extends Fragment {

    public static final int REQUEST_ADD_PUESTO = 1;
    public static final int REQUEST_UPDATE_DELETE_PUESTO = 2;

    OperacionesBaseDatos datos;

    private PuestosCursorAdapter mPuestosAdapter;

    /**
     * Constructor por defecto.
     */
    public PuestosFragment() {
        // Required empty public constructor
    }

    public static PuestosFragment newInstance() {
        return new PuestosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_puestos, container, false);

        // Referencias UI
        ListView mPuestosList = (ListView) root.findViewById(R.id.puestos_list);
        mPuestosAdapter = new PuestosCursorAdapter(getActivity(), null);
        FloatingActionButton mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_puesto);

        // Setup
        mPuestosList.setAdapter(mPuestosAdapter);

        // Eventos
        mPuestosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor) mPuestosAdapter.getItem(i);
                String currentPuestoId = currentItem.getString(currentItem.getColumnIndex(NombresColumnasBaseDatos.Puestos.ID));
                showDetailScreen(currentPuestoId);
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
        cargarPuestos();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            cargarPuestos();
            /*switch (requestCode) {
                case AddEditLawyerActivity.REQUEST_ADD_PUESTO:
                    Toast.makeText(getActivity(),"Puesto guardado correctamente", Toast.LENGTH_SHORT).show();
                    cargarPuestos();
                    break;
                case REQUEST_UPDATE_DELETE_PUESTO:
                    Toast.makeText(getActivity(),"Puesto eliminado correctamente", Toast.LENGTH_SHORT).show();
                    cargarPuestos();
                    break;
            }*/
        }
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para cargar los puestos.
     */
    private void cargarPuestos() {
        new PuestosLoadTask().execute();
    }

    /**
     * Método encargado de mostrar la ventana de descripcionPuesto del Puesto vacía para crear un Puesto nuevo.
     */
    private void showAddScreen() {
        Intent intent = new Intent(getActivity(), PuestoDetalleActivity.class);
        startActivityForResult(intent, REQUEST_ADD_PUESTO);
    }

    /**
     * Método encargado de mostrar la ventana de descripcionPuesto del Puesto, mostrando los datos del mismo
     * obtenidos mediante el id del puesto seleccionado de la lista, para editarlo.
     *
     * @param puestoId Puesto seleccionado de la lista.
     */
    private void showDetailScreen(String puestoId) {
        Intent intent = new Intent(getActivity(), PuestoDetalleActivity.class);
        intent.putExtra(PuestosActivity.EXTRA_PUESTO_ID, puestoId);
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_PUESTO);
    }

    /**
     * Clase asíncrona encargada de cargar los puestos de la base de datos a la lista.
     */
    private class PuestosLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return datos.obtenerPuestos();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mPuestosAdapter.swapCursor(cursor);
            } else {
                Toast.makeText(getActivity(),
                        "No hay puestos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}