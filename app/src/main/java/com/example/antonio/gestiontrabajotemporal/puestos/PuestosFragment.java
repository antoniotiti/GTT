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
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;


public class PuestosFragment extends Fragment {

    public static final int REQUEST_UPDATE_DELETE_PUESTO = 2;

    OperacionesBaseDatos datos;

    private ListView mPuestosList;
    private PuestosCursorAdapter mPuestosAdapter;
    private FloatingActionButton mAddButton;

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
        mPuestosList = (ListView) root.findViewById(R.id.puestos_list);
        mPuestosAdapter = new PuestosCursorAdapter(getActivity(), null);
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mPuestosList.setAdapter(mPuestosAdapter);


        // Eventos
        mPuestosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Cursor currentItem = (Cursor) mPuestosAdapter.getItem(i);
                String currentPuestoNombre = currentItem.getString(
                        currentItem.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));

                Toast.makeText(getActivity(), "Detalle "+ currentPuestoNombre, Toast.LENGTH_LONG).show();


                String currentPuestoId = currentItem.getString(
                        currentItem.getColumnIndex(NombresColumnasBaseDatos.Puestos.ID));

              //  showDetailScreen(currentLawyerId);
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
        cargarPuestos();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                /*case AddEditLawyerActivity.REQUEST_ADD_TURNO:
                    showSuccessfullSavedMessage();
                    cargarPuestos();
                    break;*/
                case REQUEST_UPDATE_DELETE_PUESTO:
                    cargarPuestos();
                    break;
            }
        }
    }

    private void cargarPuestos() {
        new PuestosLoadTask().execute();
    }

    private void showSuccessfullSavedMessage() {
        Toast.makeText(getActivity(),
                "Puesto guardado correctamente", Toast.LENGTH_SHORT).show();
    }

   /* private void showAddScreen() {
        Intent intent = new Intent(getActivity(), AddEditLawyerActivity.class);
        startActivityForResult(intent, AddEditLawyerActivity.REQUEST_ADD_TURNO);
    }

    private void showDetailScreen(String lawyerId) {
        Intent intent = new Intent(getActivity(), LawyerDetailActivity.class);
        intent.putExtra(LawyersActivity.EXTRA_LAWYER_ID, lawyerId);
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_LAWYER);
    }*/

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
                // Mostrar empty state
            }
        }
    }

}
