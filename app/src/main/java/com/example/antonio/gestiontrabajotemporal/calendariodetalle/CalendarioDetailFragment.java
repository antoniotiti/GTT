package com.example.antonio.gestiontrabajotemporal.calendariodetalle;

import android.app.Activity;
import android.content.Context;
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
import com.example.antonio.gestiontrabajotemporal.modelo.Calendario;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEditTextVacio;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarioDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarioDetailFragment extends Fragment {

    private static final String ARG_CALENDARIO_ID = "calendarioId";
    OperacionesBaseDatos datos;
    EditText editTextNombreCalendario, editTextDescripcionCalendario;
    private Context context;
    private String mCalendarioId;
    private CollapsingToolbarLayout mCollapsingView;
    private boolean nombreCalendarioValidado = false, descripcionCalendarioValidado = false;

    /**
     * Constructor por defecto.
     */
    public CalendarioDetailFragment() {
    }

    public static CalendarioDetailFragment newInstance(String calendarioId) {
        CalendarioDetailFragment fragment = new CalendarioDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CALENDARIO_ID, calendarioId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

        if (getArguments() != null) {
            mCalendarioId = getArguments().getString(ARG_CALENDARIO_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendario_detail, container, false);

        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout_calendario);

        //Obtenemos las referencias de las vistas.
        editTextNombreCalendario = (EditText) root.findViewById(R.id.editText_nombre_calendario);
        editTextDescripcionCalendario = (EditText) root.findViewById(R.id.editText_descripcion_calendario);

        //Establecemos los Listener
        editTextNombreCalendario.addTextChangedListener(new MyTextWatcher(editTextNombreCalendario));
        editTextDescripcionCalendario.addTextChangedListener(new MyTextWatcher(editTextDescripcionCalendario));

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity());

        // Carga de datos
        if (mCalendarioId != null) {
            new GetCalendarioByIdTask().execute();//Cargamos los datos del calendario seleccionado
        }
        return root;
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para borrar un calendario.
     */
    public void borrarCalendario() {
        new DeleteCalendarioTask().execute();
    }


    /**
     * Ejecutamos una acción al seleccionar una opcion del menú.
     *
     * @param item Opcion del menu seleccionada
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addEditCalendario();
                break;
            case R.id.action_delete:
                new SimpleDialog().show(getFragmentManager(), "EliminarCalendario");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que se encarga de insertar o editar un calendario
     */
    private void addEditCalendario() {
        nombreCalendarioValidado = validarEditTextVacio(context, editTextNombreCalendario);
        descripcionCalendarioValidado = validarEditTextVacio(context, editTextDescripcionCalendario);

        if (nombreCalendarioValidado && descripcionCalendarioValidado) {
            String nombreCalendario = editTextNombreCalendario.getText().toString();
            String descripcionCalendario = editTextDescripcionCalendario.getText().toString();

            Calendario calendarioInsertar = new Calendario(nombreCalendario, descripcionCalendario);
            new AddEditCalendarioTask().execute(calendarioInsertar);
        }
    }

    /**
     * Método encargado de mostrar en pantalla los datos del Calendario seleccionado.
     * Calendario
     */
    private void showCalendario(Calendario calendario) {

        mCollapsingView.setTitle(calendario.getNombreCalendario());
        editTextNombreCalendario.setText(calendario.getNombreCalendario());
        editTextDescripcionCalendario.setText(calendario.getDescripcionCalendario());
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha borrado o no el calendario de la BBDD.
     *
     * @param requery Si se ha borrado o no el calendario.
     */
    private void showCalendarioScreenFromDelete(boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_eliminar_calendario), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.calendario_eliminado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha creado o no el calendario en la BBDD.
     *
     * @param requery Si se ha creado o no el calendario.
     */
    private void showCalendarioScreenFromAdd(Boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_crear_calendario), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.calendario_creado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK); // datos.close();
            getActivity().finish();
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha editado o no el calendario en la BBDD.
     *
     * @param requery Si se ha editado o no el calendario.
     */
    private void showCalendarioScreenFromEdit(Boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_editar_calendario), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.calendario_editado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Clase asíncrona encargada de obtener los datos del calendario selecciondo y mostrarlos en pantalla,
     * en caso de que no se obtengan los datos muetra un mensaje de error.
     */
    private class GetCalendarioByIdTask extends AsyncTask<Void, Void, Cursor> {
        /**
         * Obtenemos los datos del calendario seleccionado
         *
         * @param voids
         * @return Cursor con los datos del calendario seleccionado.
         */
        @Override
        protected Cursor doInBackground(Void... voids) {

            return datos.obtenerCalendarioById(mCalendarioId);
        }

        /**
         * Mostramos los datos obtenidos del calendario en pantalla, si no hemos obtenido los datos del
         * calendario mostramos un menaje de error.
         *
         * @param cursor Cursor que contiene los datos del Calendario para mostrar.
         */
        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showCalendario(new Calendario(cursor));
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_cargar_informacion), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Clae asíncrona encargada de borrar un calendario seleccionado
     */
    private class DeleteCalendarioTask extends AsyncTask<Void, Void, Integer> {
        /**
         * Eliminamos el calendario seleccionado
         *
         * @param voids
         * @return Si se ha eliminado el calendario
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            return datos.eliminarCalendario(mCalendarioId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showCalendarioScreenFromDelete(integer > 0);
        }
    }

    /**
     * Clase asíncrona encargada de crear o editar un calendario.
     */
    private class AddEditCalendarioTask extends AsyncTask<Calendario, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Calendario... calendarios) {
            if (mCalendarioId != null) {
                return datos.editarCalendario(calendarios[0], mCalendarioId) > 0;
            } else {
                return datos.insertarCalendario(calendarios[0]) != null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (mCalendarioId != null) {
                showCalendarioScreenFromEdit(result);
            } else {
                showCalendarioScreenFromAdd(result);
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
                case R.id.editText_nombre_calendario:
                    nombreCalendarioValidado = validarEditTextVacio(context, editTextNombreCalendario);
                    break;
                case R.id.editText_descripcion_calendario:
                    descripcionCalendarioValidado = validarEditTextVacio(context, editTextDescripcionCalendario);
                    break;
            }
        }
    }
}