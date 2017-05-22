package com.example.antonio.gestiontrabajotemporal.operariodetalle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.modelo.Operario;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.ui.MainActivity;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.antonio.gestiontrabajotemporal.operariodetalle.OperarioDetalleActivity.REQUEST_PERMISSION;
import static com.example.antonio.gestiontrabajotemporal.operariodetalle.OperarioDetalleActivity.RESULT_LOAD_IMAGE;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.formatter_fecha;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarCodigoOperario;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEditTextVacio;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEmail;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarFecha;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarNSS;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarNieONif;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarPassword;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarTelefono;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperarioDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperarioDetailFragment extends Fragment {

    private static final String ARG_OPERARIO_ID = "operarioId";


    EditText editTextCodigoOperario, editTextDni, editTextNombre, editTextApellidos, editTextFechaNacimiento,
            editTextNumeroSS, editTextDireccion, editTextTelefono, editTextEmail, editTextFechaInicio,
            editTextPassword, editTextConfirmarPassword;
    Boolean codigoOperarioValidado = false, dniValidado = false, nombreValidado = false,
            appelidosValidado = false, fechaNacimientoValidado = false, numeroSSValidado = false,
            direccionValidado = false, telefonoValidado = false, emailValidado = false,
            fechaInicioValidado = false, passwordValidado = false, confirmarPasswordValidado = false;
    ImageView imageViewFotoOperario;
    OperacionesBaseDatos datos;
    private Context context;

    private String mOperarioId;

    /**
     * Constructor por defecto.
     */
    public OperarioDetailFragment() {
    }

    public static OperarioDetailFragment newInstance(String operarioId) {
        OperarioDetailFragment fragment = new OperarioDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OPERARIO_ID, operarioId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

        if (getArguments() != null) {
            mOperarioId = getArguments().getString(ARG_OPERARIO_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_operario_detail, container, false);

        //CollapsingToolbarLayout mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout_operario);

        // Obtenemos las referencias de las vistas.
        editTextCodigoOperario = (EditText) root.findViewById(R.id.editText_CodigoOperarioDetalle);
        imageViewFotoOperario = (ImageView) root.findViewById(R.id.imageView_foto_operario);
        editTextDni = (EditText) root.findViewById(R.id.editText_DniDetalle);
        editTextNombre = (EditText) root.findViewById(R.id.editText_NombreDetalle);
        editTextApellidos = (EditText) root.findViewById(R.id.editText_ApellidoDetalle);
        editTextFechaNacimiento = (EditText) root.findViewById(R.id.editText_FechaNacimientoDetalle);
        editTextNumeroSS = (EditText) root.findViewById(R.id.editText_NSSDetalle);
        editTextDireccion = (EditText) root.findViewById(R.id.editText_DireccionDetalle);
        editTextTelefono = (EditText) root.findViewById(R.id.editText_TelefonoDetalle);
        editTextEmail = (EditText) root.findViewById(R.id.editText_EmailDetalle);
        editTextFechaInicio = (EditText) root.findViewById(R.id.editText_FechaInicioDetalle);
        editTextPassword = (EditText) root.findViewById(R.id.editText_PasswordDetalle);
        editTextConfirmarPassword = (EditText) root.findViewById(R.id.editText_ConfirmarPasswordDetalle);

        //Establecemos los Listener
        editTextCodigoOperario.addTextChangedListener(new MyTextWatcher(editTextCodigoOperario));
        editTextDni.addTextChangedListener(new MyTextWatcher(editTextDni));
        editTextNombre.addTextChangedListener(new MyTextWatcher(editTextNombre));
        editTextApellidos.addTextChangedListener(new MyTextWatcher(editTextApellidos));
        editTextFechaNacimiento.addTextChangedListener(new MyTextWatcher(editTextFechaNacimiento));
        editTextNumeroSS.addTextChangedListener(new MyTextWatcher(editTextNumeroSS));
        editTextDireccion.addTextChangedListener(new MyTextWatcher(editTextDireccion));
        editTextTelefono.addTextChangedListener(new MyTextWatcher(editTextTelefono));
        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));
        editTextFechaInicio.addTextChangedListener(new MyTextWatcher(editTextFechaInicio));
        editTextPassword.addTextChangedListener(new MyTextWatcher(editTextPassword));
        editTextConfirmarPassword.addTextChangedListener(new MyTextWatcher(editTextConfirmarPassword));
        imageViewFotoOperario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)  {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSION);
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSION);
                    return;
                }else{
                    abrirGaleria();

                }
            }
        });


        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getActivity());

        // Carga de datos
        if (mOperarioId != null) {
            new GetOperarioByIdTask().execute();//Cargamos los datos del operario
        }
        return root;
    }

    public void abrirGaleria() {
        // Abrir la galería
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    /**
     * Método que se encarga de seleccionar una imagen de la galeria al pulsar en la foto del operario.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri uriSelectedImage = data.getData();
            String[] pathColumnName = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(uriSelectedImage,
                    pathColumnName, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {

                int columnIndex = cursor.getColumnIndex(pathColumnName[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();

                imageViewFotoOperario.setImageBitmap(BitmapFactory.decodeFile(path));
                imageViewFotoOperario.buildDrawingCache();
                imageViewFotoOperario.setTag(path);
                Bitmap bm = imageViewFotoOperario.getDrawingCache();

                ContextWrapper cw = new ContextWrapper(context);
                File dirImages = cw.getDir("OperariosGTT", Context.MODE_PRIVATE);
                File myPath = new File(dirImages, mOperarioId + ".jpg");
                imageViewFotoOperario.setTag(myPath.getPath());

                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(myPath);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.selecciona_imagen), Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageViewFotoOperario.setImageDrawable(context.getDrawable(R.mipmap.sin_operario));
            } else {
                imageViewFotoOperario.setImageDrawable(getResources().getDrawable(R.mipmap.sin_operario));
            }
        }
    }



    /**
     * Método encargado de lanzar la tarea en segundo plano para borrar un operario.
     */
    public void borrarOperario() {
        new DeleteOperarioTask().execute();
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
                editTurno();
                break;
            case R.id.action_delete:
                new SimpleDialog().show(getFragmentManager(), "EliminarOperario");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que se encarga de editar un Operario validando los datos previamente.
     */
    private void editTurno() {

        if (codigoOperarioValidado && dniValidado && nombreValidado && appelidosValidado &&
                fechaNacimientoValidado && numeroSSValidado && direccionValidado && telefonoValidado
                && emailValidado && passwordValidado && confirmarPasswordValidado) {

            String codigoOperario = editTextCodigoOperario.getText().toString();
            String fotoOperario = (String) imageViewFotoOperario.getTag();
            String dni = editTextDni.getText().toString();
            String nombre = editTextNombre.getText().toString();
            String apellidos = editTextApellidos.getText().toString();
            String fechaNacimiento = editTextFechaNacimiento.getText().toString();
            String numeroSS = editTextNumeroSS.getText().toString();
            String direccion = editTextDireccion.getText().toString();
            String telefono = editTextTelefono.getText().toString();
            String email = editTextEmail.getText().toString();
            String fechaInicio = editTextFechaInicio.getText().toString();
            String password = editTextPassword.getText().toString();
            String confirmarPassword = editTextConfirmarPassword.getText().toString();

            //Comprobar si coinciden las passwords
            if (password.equals(confirmarPassword)) {
                Operario operarioEditar = new Operario(codigoOperario, dni, nombre, apellidos, fotoOperario, direccion, fechaNacimiento, telefono, email, fechaInicio, numeroSS, password);
                //Editamos el Operario
                new EditOperarioTask().execute(operarioEditar);
                datos.editarOperario(operarioEditar, mOperarioId);
            } else {
                Toast.makeText(context, context.getString(R.string.password_no_coinciden), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, context.getString(R.string.rellenar_campor_correctamente), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método que se encarga de mostrar en pantalla los datos del Operario.
     *
     * @param operario Operario del cual queremos mostrar los datos en pantalla.
     */
    private void showOperario(Operario operario) {
        File foto;
        try {
            foto = new File(operario.getFoto());
            if (foto.exists()) {
                imageViewFotoOperario.setImageURI(Uri.fromFile(foto));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageViewFotoOperario.setImageDrawable(context.getDrawable(R.mipmap.sin_operario));
                } else {
                    imageViewFotoOperario.setImageDrawable(getResources().getDrawable(R.mipmap.sin_operario));
                }
            }
        } catch (NullPointerException e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageViewFotoOperario.setImageDrawable(context.getDrawable(R.mipmap.sin_operario));
            } else {
                imageViewFotoOperario.setImageDrawable(getResources().getDrawable(R.mipmap.sin_operario));
            }
        }
        try {
            Date fechaNacimiento = formatter_fecha.parse(operario.getFechaNacimiento());
            Date fechaInicio = formatter_fecha.parse(operario.getFechaInicio());

            editTextCodigoOperario.setText(operario.getIdOperario());
            editTextDni.setText(operario.getDni());
            editTextNombre.setText(operario.getNombre());
            editTextApellidos.setText(operario.getApellidos());
            editTextFechaNacimiento.setText(formatter_fecha.format(fechaNacimiento));
            editTextFechaInicio.setText(formatter_fecha.format(fechaInicio));
            editTextNumeroSS.setText(operario.getNumeroSS());
            editTextDireccion.setText(operario.getDireccion());
            editTextTelefono.setText(operario.getTelefono());
            editTextEmail.setText(operario.getEmail());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha borrado o no el operario de la BBDD.
     *
     * @param requery Si se ha borrado o no el operario.
     */
    private void showOperarioScreenFromDelete(boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_eliminar_operario), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.operario_eliminado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Método que se encarga de mostrar un mensaje, dependiendo si se ha editado o no el operario en la BBDD.
     *
     * @param requery Si se ha editado o no el operario.
     */
    private void showTurnoScreenFromEdit(Boolean requery) {
        if (!requery) {
            Toast.makeText(getActivity(), getString(R.string.error_editar_operario), Toast.LENGTH_SHORT).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            Toast.makeText(getActivity(), getString(R.string.operario_editado_correctamente), Toast.LENGTH_SHORT).show();
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
        }
    }

    /**
     * Clase asíncrona encargada de obtener los datos del operario y mostrarlos en pantalla,
     * en caso de que no se obtengan los datos muetra un mensaje de error.
     */
    private class GetOperarioByIdTask extends AsyncTask<Void, Void, Cursor> {
        /**
         * Obtenemos los datos del operario seleccionado
         *
         * @return Cursor con los datos del operario seleccionado.
         */
        @Override
        protected Cursor doInBackground(Void... voids) {
            return datos.obtenerOperarioById(mOperarioId);
        }

        /**
         * Mostramos los datos obtenidos del operario en pantalla.
         *
         * @param cursor Cursor que contiene los datos del Operario para mostrar.
         */
        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showOperario(new Operario(cursor));
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_cargar_informacion), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Clase asíncrona encargada de borrar un operario
     */
    private class DeleteOperarioTask extends AsyncTask<Void, Void, Integer> {
        /**
         * Eliminamos el operario
         *
         * @return Si se ha eliminado el operario
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            return datos.eliminarOperario(mOperarioId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showOperarioScreenFromDelete(integer > 0);
            startActivity(new Intent(context, MainActivity.class));//Volvemos a la pantalla de inicio
        }
    }

    /**
     * Clase asíncrona encargada de editar un operario.
     */
    private class EditOperarioTask extends AsyncTask<Operario, Void, Boolean> {
        /**
         * Editamos el operario
         */
        @Override
        protected Boolean doInBackground(Operario... operario) {
            return datos.editarOperario(operario[0], mOperarioId);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showTurnoScreenFromEdit(result);
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
                case R.id.editText_CodigoOperarioDetalle:
                    codigoOperarioValidado = validarCodigoOperario(context, editTextCodigoOperario);
                    break;
                case R.id.editText_DniDetalle:
                    dniValidado = validarNieONif(context, editTextDni);
                    break;
                case R.id.editText_NombreDetalle:
                    nombreValidado = validarEditTextVacio(context, editTextNombre);
                    break;
                case R.id.editText_ApellidoDetalle:
                    appelidosValidado = validarEditTextVacio(context, editTextApellidos);
                    break;
                case R.id.editText_FechaNacimientoDetalle:
                    fechaNacimientoValidado = validarFecha(context, editTextFechaNacimiento);
                    break;
                case R.id.editText_NSSDetalle:
                    numeroSSValidado = validarNSS(context, editTextNumeroSS);
                    break;
                case R.id.editText_DireccionDetalle:
                    direccionValidado = validarEditTextVacio(context, editTextDireccion);
                    break;
                case R.id.editText_TelefonoDetalle:
                    telefonoValidado = validarTelefono(context, editTextTelefono);
                    break;
                case R.id.editText_EmailDetalle:
                    emailValidado = validarEmail(context, editTextEmail);
                    break;
                case R.id.editText_FechaInicioDetalle:
                    fechaInicioValidado = validarFecha(context, editTextFechaInicio);
                    break;
                case R.id.editText_PasswordDetalle:
                    passwordValidado = validarPassword(context, editTextPassword);
                    break;
                case R.id.editText_ConfirmarPasswordDetalle:
                    confirmarPasswordValidado = validarPassword(context, editTextConfirmarPassword);
                    break;
            }
        }
    }
}