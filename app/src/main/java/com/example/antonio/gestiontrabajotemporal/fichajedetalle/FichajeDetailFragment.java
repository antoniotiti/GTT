package com.example.antonio.gestiontrabajotemporal.fichajedetalle;
//TODO los layout para los calendarios,añadir descripccion ouesto base datos y modificar todo lo  que conlleve

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.modelo.Fichaje;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.util.DialogoSeleccionPuesto;
import com.example.antonio.gestiontrabajotemporal.util.DialogoSeleccionTurno;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FichajeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FichajeDetailFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_CALENDARIO_ID = "calendarioId";
    private static final String ARG_FECHA = "fecha";
    private static final String ARG_OPERARIO_ID = "operarioId";

    OperacionesBaseDatos datos;

    EditText editTextCodigoOperarioFichajeDetalle, editTextNombreOperarioFichajeDetalle, editTextTurnoFichajeDetalle, editTextPuestoFichajeDetalle, editTextComentarioFichajeDetalle, editTextHorasExtrasFichajeDetalle;
    private String mCalendarioId;
    private String mFecha;
    private String mOperarioId;
    private String mTurnoId;
    private String mPuestoId;
    private CollapsingToolbarLayout mCollapsingView;
    TextView textViewPrevisualizacionTurnoFichajeDetalle;

    /**
     * Constructor por defecto.
     */
    public FichajeDetailFragment() {
        // Required empty public constructor
    }

    public static FichajeDetailFragment newInstance(String calendarioId, String fecha, String operarioId) {
        FichajeDetailFragment fragment = new FichajeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CALENDARIO_ID, calendarioId);
        args.putString(ARG_FECHA, fecha);
        args.putString(ARG_OPERARIO_ID, operarioId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCalendarioId = getArguments().getString(ARG_CALENDARIO_ID);
            mFecha = getArguments().getString(ARG_FECHA);
            mOperarioId = getArguments().getString(ARG_OPERARIO_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fichaje_detail, container, false);

        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout_fichaje);

        editTextNombreOperarioFichajeDetalle = (EditText) root.findViewById(R.id.editText_NombreOperarioFichajeDetalle);
        editTextCodigoOperarioFichajeDetalle = (EditText) root.findViewById(R.id.editText_CodigoOperarioFichajeDetalle);
        editTextTurnoFichajeDetalle = (EditText) root.findViewById(R.id.editText_TurnoFichajeDetalle);
        editTextTurnoFichajeDetalle.setOnClickListener(this);
        editTextPuestoFichajeDetalle = (EditText) root.findViewById(R.id.editText_PuestoFichajeDetalle);
        editTextPuestoFichajeDetalle.setOnClickListener(this);

        editTextHorasExtrasFichajeDetalle = (EditText) root.findViewById(R.id.editText_HorasExtraFichajeDetalle);
        editTextComentarioFichajeDetalle = (EditText) root.findViewById(R.id.editText_ComentarioFichajeDetalle);

        textViewPrevisualizacionTurnoFichajeDetalle = (TextView) root.findViewById(R.id.textView_prev_turnoFichajeDetalle);
        textViewPrevisualizacionTurnoFichajeDetalle.setOnClickListener(this);

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity());

        new GetFichajeTask().execute();

        // Carga de datos
       /* if (mCalendarioId != null) {
            new GetCalendarioByIdTask().execute();//Cargamos los datos del calendario seleccionado
        }*/
        return root;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                new SimpleDialog().show(getFragmentManager(), "ModificarFichaje");
                break;
            case R.id.action_delete:
                new SimpleDialog().show(getFragmentManager(), "EliminarFichaje");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para borrar un fichaje.
     */
    public void borrarFichaje() {
        new DeleteFichajeTask().execute();
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para editar un fichaje.
     */
    public void editarFichaje() {

        //TODO Crear un Fichaje con los datos editados. Debe coger los ID de turno y puesto
        Double mHoraExtra = Double.parseDouble(editTextHorasExtrasFichajeDetalle.getText().toString());
        String mComentario = editTextComentarioFichajeDetalle.getText().toString();

        Fichaje fichajeinsertar = new Fichaje(mOperarioId, mFecha, mTurnoId, mPuestoId, mCalendarioId, mHoraExtra, mComentario);
        new EditFichajeTask().execute(fichajeinsertar);
    }

    //TODO borrar
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Acciones
    }

    /**
     * Método encargado de mostrar en pantalla los datos del Fichaje seleccionado.
     */
    private void showFichajeDetalle(Cursor fichaje) {

        if (fichaje.moveToFirst()) {

            String mNombreOperario = fichaje.getString(fichaje.getColumnIndex(NombresColumnasBaseDatos.Operarios.NOMBRE));
            mNombreOperario += " " + fichaje.getString(fichaje.getColumnIndex(NombresColumnasBaseDatos.Operarios.APELLIDOS));
            String mNombreTurno = fichaje.getString(fichaje.getColumnIndex(NombresColumnasBaseDatos.Turnos.NOMBRE));
            mTurnoId = fichaje.getString(fichaje.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));
            int mColorFondoTurno = fichaje.getInt(fichaje.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_FONDO));
            int mColorTextoTurno = fichaje.getInt(fichaje.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_TEXTO));
            String mAbreviaturaTurno = fichaje.getString(fichaje.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));
            String mNombrePuesto = fichaje.getString(fichaje.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));
            mPuestoId = fichaje.getString(fichaje.getColumnIndex(NombresColumnasBaseDatos.Puestos.ID));
            String mComentario = fichaje.getString(fichaje.getColumnIndex(NombresColumnasBaseDatos.Fichajes.COMENTARIO));
            Double mHoraExtra = fichaje.getDouble(fichaje.getColumnIndex(NombresColumnasBaseDatos.Fichajes.HORA_EXTRA));

            textViewPrevisualizacionTurnoFichajeDetalle.setBackgroundColor(mColorFondoTurno);
            textViewPrevisualizacionTurnoFichajeDetalle.setTextColor(mColorTextoTurno);
            textViewPrevisualizacionTurnoFichajeDetalle.setText(mAbreviaturaTurno);

            editTextCodigoOperarioFichajeDetalle.setText(mOperarioId);
            editTextNombreOperarioFichajeDetalle.setText(mNombreOperario);
            editTextTurnoFichajeDetalle.setText(mNombreTurno);
            editTextPuestoFichajeDetalle.setText(mNombrePuesto);
            editTextHorasExtrasFichajeDetalle.setText(String.valueOf(mHoraExtra));
            editTextComentarioFichajeDetalle.setText(mComentario);

        }
    }

    private void showFichajeScreenFromDelete(boolean requery) {
        if (!requery) {
            showDeleteError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(),
                    "Fichaje eliminado correctamente", Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK);
//  datos.close();
            getActivity().finish();
        }
    }

    private void showFichajeScreenFromAdd(Boolean requery) {
        if (!requery) {
            showAddError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(),
                    "Fichaje creado correctamente", Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_OK); // datos.close();
            getActivity().finish();
        }
    }

    private void showFichajeScreenFromEdit(Boolean requery) {
        if (!requery) {
            showEditError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(),
                    "Fichaje editado correctamente", Toast.LENGTH_SHORT).show();
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
                "Error al eliminar el Fichaje seleccionado", Toast.LENGTH_SHORT).show();
    }

    private void showAddError() {
        Toast.makeText(getActivity(),
                "Error al crear el Fichaje", Toast.LENGTH_SHORT).show();
    }

    private void showEditError() {
        Toast.makeText(getActivity(),
                "Error al editar el Fichaje seleccionado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {

            case R.id.textView_prev_turnoFichajeDetalle:
                new DialogoSeleccionTurno().show(getFragmentManager(), "SeleccionarTurno");
                break;
            case R.id.editText_TurnoFichajeDetalle:
                 new DialogoSeleccionTurno().show(getFragmentManager(), "SeleccionarTurno");
                break;
            case R.id.editText_PuestoFichajeDetalle:
                //TODO seleccion de puestos
                new DialogoSeleccionPuesto().show(getFragmentManager(), "SeleccionarPuesto");
                break;

        }
    }

    public void obtenerSetearPuesto(String currentId) {
        String idPuesto = currentId;
        String nombrePuesto = "";

        Cursor cursorIdPuesto = datos.obtenerPuestoById(idPuesto);

        //Nos aseguramos de que existe al menos un registro
        if (cursorIdPuesto.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                nombrePuesto = cursorIdPuesto.getString(cursorIdPuesto.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));
                mPuestoId = cursorIdPuesto.getString(cursorIdPuesto.getColumnIndex(NombresColumnasBaseDatos.Puestos.ID));

            } while (cursorIdPuesto.moveToNext());
        }
        editTextPuestoFichajeDetalle.setText(nombrePuesto);
    }

    public void obtenerSetearTurno(String currentId) {

        String idTurno = currentId;
        String nombreTurno = "";

        Cursor cursorIdTurno = datos.obtenerTurnoById(idTurno);

        //Nos aseguramos de que existe al menos un registro
        if (cursorIdTurno.moveToFirst()) {
                nombreTurno = cursorIdTurno.getString(cursorIdTurno.getColumnIndex(NombresColumnasBaseDatos.Turnos.NOMBRE));
                mTurnoId = cursorIdTurno.getString(cursorIdTurno.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));
                int mColorFondoTurno = cursorIdTurno.getInt(cursorIdTurno.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_FONDO));
                int mColorTextoTurno = cursorIdTurno.getInt(cursorIdTurno.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_TEXTO));
                String mAbreviaturaTurno = cursorIdTurno.getString(cursorIdTurno.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));

                textViewPrevisualizacionTurnoFichajeDetalle.setBackgroundColor(mColorFondoTurno);
                textViewPrevisualizacionTurnoFichajeDetalle.setTextColor(mColorTextoTurno);
                textViewPrevisualizacionTurnoFichajeDetalle.setText(mAbreviaturaTurno);
        }
        editTextTurnoFichajeDetalle.setText(nombreTurno);
    }


    /**
     * Clase asíncrona encargada de obtener los datos del fichaje selecciondo y mostrarlos en pantalla,
     * en caso de que no se obtengan los datos muetra un mensaje de error.
     */
    private class GetFichajeTask extends AsyncTask<Void, Void, Cursor> {
        /**
         * Obtenemos los datos del fichaje seleccionado
         *
         * @param voids
         * @return Cursor con los datos del fichaje seleccionado.
         */
        @Override
        protected Cursor doInBackground(Void... voids) {

            return datos.obtenerFichajesParaDetalle(mOperarioId, mCalendarioId, mFecha);
        }

        /**
         * Mostramos los datos obtenidos del fichaje en pantalla, si no hemos obtenido los datos del
         * fichaje mostramos un mensaje de error.
         *
         * @param cursor Cursor que contiene los datos del Fichaje para mostrar.
         */
        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showFichajeDetalle(cursor);
            } else {
                showLoadError();
            }
        }

    }

    /**
     * Clae asíncrona encargada de borrar un fichaje seleccionado
     */
    private class DeleteFichajeTask extends AsyncTask<Void, Void, Boolean> {
        /**
         * Eliminamos el fichaje seleccionado
         *
         * @param voids
         * @return Si se ha eliminado el fichaje
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            return datos.eliminarFichaje(mOperarioId, mFecha, mCalendarioId);
        }

        @Override
        protected void onPostExecute(Boolean eliminado) {
            showFichajeScreenFromDelete(eliminado);
        }

    }

    /**
     * Clase asíncrona encargada de editar un fichaje.
     */
    private class EditFichajeTask extends AsyncTask<Fichaje, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Fichaje... fichaje) {
            return datos.editarFichaje(fichaje[0]);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            showFichajeScreenFromEdit(result);
        }
    }
}