package com.example.antonio.gestiontrabajotemporal.puestodetalle;

import android.app.Activity;
import android.content.Context;
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
    private Context context;
    private String mPuestoId;
    private CollapsingToolbarLayout mCollapsingView;
    private boolean nombrePuestoValidado = false, descripcionPuestoValidado = false;

    /**
     * Constructor por defecto.
     */
    public PuestoDetailFragment() {
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
        context = getActivity().getApplicationContext();

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

        //Obtenemos las referencias de las vistas.
        editTextNombrePuesto = (EditText) root.findViewById(R.id.editText_nombre_puesto);//
        editTextDescripcionPuesto = (EditText) root.findViewById(R.id.editText_descripcion_puesto);

        //Establecemos los Listener
        editTextNombrePuesto.addTextChangedListener(new MyTextWatcher(editTextNombrePuesto));
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addEditPuesto();
                break;
            case R.id.action_delete:
                new SimpleDialog().show(getFragmentManager(), "EliminarPuesto");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Método que se encarga de añadir un Puesto a la BBDD validando los datos previamente.
     */
    private void addEditPuesto() {

        nombrePuestoValidado = validarEditTextVacio(context, editTextNombrePuesto);
        descripcionPuestoValidado = validarEditTextVacio(context, editTextDescripcionPuesto);
        //Comprobamos que todos los datos esten validados
        if (nombrePuestoValidado && descripcionPuestoValidado) {
            String nombrePuesto = editTextNombrePuesto.getText().toString();
            String descripcionPuesto = editTextDescripcionPuesto.getText().toString();
            //Creamos un puesto con todos los datos validados.
            Puesto puestoInsertar = new Puesto(nombrePuesto, descripcionPuesto);
            //Añadimos el puesto a la BBDD
            new AddEditPuestoTask().execute(puestoInsertar);
        }
    }

    /**
     * Método encargado de mostrar en pantalla los datos del Puesto seleccionado.
     *
     * @param puesto Puesto del cual queremos mostrar los datos en pantalla.
     */
    private void showPuesto(Puesto puesto) {
        mCollapsingView.setTitle(puesto.getNombrePuesto());
        editTextNombrePuesto.setText(puesto.getNombrePuesto());
        editTextDescripcionPuesto.setText(puesto.getDescripcionPuesto());
    }
    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha borrado o no el puesto de la BBDD.
     *
     * @param requery Si se ha borrado o no el puesto.
     */
    private void showPuestoScreenFromDelete(boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_eliminar_puesto), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.puesto_eliminado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha creado o no el puesto en la BBDD.
     *
     * @param requery Si se ha creado o no el puesto.
     */
    private void showPuestoScreenFromAdd(Boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_crear_puesto), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.puesto_creado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha editado o no el puesto en la BBDD.
     *
     * @param requery Si se ha editado o no el puesto.
     */
    private void showPuestoScreenFromEdit(Boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_editar_puesto), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.puesto_editado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            // datos.close();
            getActivity().finish();
        }
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
                Toast.makeText(getActivity(), getString(R.string.error_cargar_informacion), Toast.LENGTH_SHORT).show();
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
        /**
         * Insertamos o editamos el turno
         * @param puestos
         * @return
         */
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
     * Clase privada encargada de validar los datos mientras se introducen.
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
                    nombrePuestoValidado = validarEditTextVacio(context, editTextNombrePuesto);
                    break;
                case R.id.editText_descripcion_puesto:
                    descripcionPuestoValidado = validarEditTextVacio(context, editTextDescripcionPuesto);
                    break;
            }
        }
    }
}