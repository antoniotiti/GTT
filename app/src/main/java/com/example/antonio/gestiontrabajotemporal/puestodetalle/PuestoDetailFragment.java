package com.example.antonio.gestiontrabajotemporal.puestodetalle;
//TODO los layout para los puestos,añadir descripccion ouesto base datos y modificar todo lo  que conlleve

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.modelo.Puesto;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEditTextVacio;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PuestoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PuestoDetailFragment extends Fragment {

    private static final String ARG_PUESTO_ID = "puestoId";

    OperacionesBaseDatos datos;

    EditText editTextNombrePuesto, editTextDescripcionPuesto;
    private String mPuestoId;
    private CollapsingToolbarLayout mCollapsingView;
    private boolean nombrePuestoValidado=false, descripcionPuestoValidado = false;

    /**
     * Constructor por defecto.
     */
    public PuestoDetailFragment() {
        // Required empty public constructor
    }

    public static PuestoDetailFragment newInstance(String puestoId) {
        PuestoDetailFragment fragment = new PuestoDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PUESTO_ID, puestoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPuestoId = getArguments().getString(ARG_PUESTO_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_puesto_detail, container, false);

        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout_puesto);

        editTextNombrePuesto = (EditText) root.findViewById(R.id.editText_nombre_puesto);//
        editTextNombrePuesto.addTextChangedListener(new MyTextWatcher(editTextNombrePuesto));

        editTextDescripcionPuesto = (EditText) root.findViewById(R.id.editText_descripcion_puesto);
        editTextDescripcionPuesto.addTextChangedListener(new MyTextWatcher(editTextDescripcionPuesto));

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity());

        // Carga de datos
        if (mPuestoId != null) {
            new GetPuestoByIdTask().execute();//Cargamos los datos del puesto seleccionado
        }
        return root;
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para borrar un puesto.
     */
    public void borrarPuesto() {
        new DeletePuestoTask().execute();
        //Toast.makeText(getActivity(), "Puesto Borrado", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addEditPuesto();

                //new AddEditPuestoTask().execute();
                break;
            case R.id.action_delete:
                new SimpleDialog().show(getFragmentManager(), "EliminarPuesto");

                //new DeletePuestoTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEditPuesto() {

        nombrePuestoValidado = validarEditTextVacio(editTextNombrePuesto);
        descripcionPuestoValidado = validarEditTextVacio(editTextDescripcionPuesto);


        if (nombrePuestoValidado && descripcionPuestoValidado) {

                String nombrePuesto = editTextNombrePuesto.getText().toString();
                String descripcionPuesto = editTextDescripcionPuesto.getText().toString();

                Puesto puestoInsertar = new Puesto(nombrePuesto, descripcionPuesto);

                new AddEditPuestoTask().execute(puestoInsertar);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Acciones
    }

    /**
     * Método encargado de mostrar en pantalla los datos del Puesto seleccionado.
Puesto     */
    private void showPuesto(Puesto puesto) {

        mCollapsingView.setTitle(puesto.getNombrePuesto());

        editTextNombrePuesto.setText(puesto.getNombrePuesto());
        editTextDescripcionPuesto.setText(puesto.getDescripcionPuesto());
    }

    private void showPuestoScreenFromDelete(boolean requery) {
        if (!requery) {
            showDeleteError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(),
                    "Puesto eliminado correctamente", Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
//  datos.close();
            getActivity().finish();
        }
    }

    private void showPuestoScreenFromAdd(Boolean requery) {
        if (!requery) {
            showAddError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(),
                    "Puesto creado correctamente", Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK); // datos.close();
            getActivity().finish();
        }
    }

    private void showPuestoScreenFromEdit(Boolean requery) {
        if (!requery) {
            showEditError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(),
                    "Puesto editado correctamente", Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            // datos.close();
            getActivity().finish();
        }
    }

    //Mostrar Errores
    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteError() {
        Toast.makeText(getActivity(),
                "Error al eliminar el Puesto seleccionado", Toast.LENGTH_SHORT).show();
    }

    private void showAddError() {
        Toast.makeText(getActivity(),
                "Error al crear el Puesto", Toast.LENGTH_SHORT).show();
    }

    private void showEditError() {
        Toast.makeText(getActivity(),
                "Error al editar el Puesto seleccionado", Toast.LENGTH_SHORT).show();
    }

    /**
     * Clase asíncrona encargada de obtener los datos del puesto selecciondo y mostrarlos en pantalla,
     * en caso de que no se obtengan los datos muetra un mensaje de error.
     */
    private class GetPuestoByIdTask extends AsyncTask<Void, Void, Cursor> {
        /**
         * Obtenemos los datos del puesto seleccionado
         *
         * @param voids
         * @return Cursor con los datos del puesto seleccionado.
         */
        @Override
        protected Cursor doInBackground(Void... voids) {

            return datos.obtenerPuestoById(mPuestoId);
        }

        /**
         * Mostramos los datos obtenidos del puesto en pantalla, si no hemos obtenido los datos del
         * puesto mostramos un menaje de error.
         *
         * @param cursor Cursor que contiene los datos del Puesto para mostrar.
         */
        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showPuesto(new Puesto(cursor));
            } else {
                showLoadError();
            }
        }

    }

    /**
     * Clae asíncrona encargada de borrar un puesto seleccionado
     */
    private class DeletePuestoTask extends AsyncTask<Void, Void, Integer> {
        /**
         * Eliminamos el puesto seleccionado
         *
         * @param voids
         * @return Si se ha eliminado el puesto
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            return datos.eliminarPuesto(mPuestoId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showPuestoScreenFromDelete(integer > 0);
        }

    }

    /**
     * Clase asíncrona encargada de crear o editar un puesto.
     */
    private class AddEditPuestoTask extends AsyncTask<Puesto, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Puesto... puestos) {
            if (mPuestoId != null) {
                return datos.editarPuesto(puestos[0], mPuestoId) > 0;
            } else {
                return datos.insertarPuesto(puestos[0]) != null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (mPuestoId != null) {
                showPuestoScreenFromEdit(result);
            } else {
                showPuestoScreenFromAdd(result);
            }
        }
    }

    /**
     *
     */
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText_nombre_puesto:
                    nombrePuestoValidado = validarEditTextVacio(editTextNombrePuesto);
                    break;
                case R.id.editText_descripcion_puesto:
                    descripcionPuestoValidado = validarEditTextVacio(editTextDescripcionPuesto);
                    break;
            }
        }
    }
}