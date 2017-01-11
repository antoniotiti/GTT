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
import com.example.antonio.gestiontrabajotemporal.puestos.PuestosCursorAdapter;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.turnodetalle.TurnoDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.turnos1.TurnosActivity1;


public class TurnosFragment extends Fragment {

    public static final int REQUEST_UPDATE_DELETE_TURNO = 2;

    OperacionesBaseDatos datos;

    private ListView mTurnosList;
    private TurnosCursorAdapter mTurnosAdapter;
    private FloatingActionButton mAddButton;

    public TurnosFragment() {
        // Required empty public constructor
    }


    public static TurnosFragment newInstance() {
        return new TurnosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_turnos, container, false);

        // Referencias UI
        mTurnosList = (ListView) root.findViewById(R.id.turnos_list);
        mTurnosAdapter = new TurnosCursorAdapter(getActivity(), null);
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mTurnosList.setAdapter(mTurnosAdapter);


        // Eventos
        mTurnosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Cursor currentItem = (Cursor) mTurnosAdapter.getItem(i);
               /* String currentTurnoNombre = currentItem.getString(
                        currentItem.getColumnIndex(NombresColumnasBaseDatos.Turnos.NOMBRE));

                Toast.makeText(getActivity(), "Detalle "+ currentTurnoNombre, Toast.LENGTH_LONG).show();*/


                String currentTurnoId = currentItem.getString(
                        currentItem.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));

                showDetailScreen(currentTurnoId);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showAddScreen();
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
            switch (requestCode) {
                /*case AddEditLawyerActivity.REQUEST_ADD_LAWYER:
                    showSuccessfullSavedMessage();
                    cargarTurnos();
                    break;*/
                case REQUEST_UPDATE_DELETE_TURNO:
                    cargarTurnos();
                    break;
            }
        }
    }

    private void cargarTurnos() {
        new TurnosLoadTask().execute();
    }

    private void showSuccessfullSavedMessage() {
        Toast.makeText(getActivity(),
                "Turno guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    /*private void showAddScreen() {
        Intent intent = new Intent(getActivity(), AddEditLawyerActivity.class);
        startActivityForResult(intent, AddEditLawyerActivity.REQUEST_ADD_LAWYER);
    }*/

    private void showDetailScreen(String turnoId) {
        Intent intent = new Intent(getActivity(), TurnoDetalleActivity.class);
        intent.putExtra(TurnosActivity.EXTRA_TURNO_ID, turnoId);
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_TURNO);
    }

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

                Toast.makeText(getActivity(),
                        "No hay turnos", Toast.LENGTH_SHORT).show();
                // TODO Mostrar empty state
                getActivity().finish();
            }
        }
    }

}
