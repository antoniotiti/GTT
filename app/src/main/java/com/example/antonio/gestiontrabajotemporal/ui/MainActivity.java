package com.example.antonio.gestiontrabajotemporal.ui;

import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.modelo.Calendario;
import com.example.antonio.gestiontrabajotemporal.modelo.Fichaje;
import com.example.antonio.gestiontrabajotemporal.modelo.Operario;
import com.example.antonio.gestiontrabajotemporal.modelo.Puesto;
import com.example.antonio.gestiontrabajotemporal.modelo.Turno;
import com.example.antonio.gestiontrabajotemporal.pantallacalendario.PantallaCalendarioActivity;
import com.example.antonio.gestiontrabajotemporal.servicio.ServicioGTT;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

import java.io.File;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.NOMBRE_BASE_DATOS;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarCodigoOperario;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarPassword;

/**
 * Clase principal que inicia la aplicación.
 */
public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    TextView txtRegister, txtRecuperarPassword, txtCrearDatosPueba, txtBorrarDatosPueba;
    EditText editTextCodigoOperario, editTextPassword;
    boolean codigoOperarioValidado, passwordValidado;
    OperacionesBaseDatos datos;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        setToolbar(); //Añadir la Toolbar.

        //datos = OperacionesBaseDatos.obtenerInstancia(context);

        /*if (!checkDataBase()) { //Comprobamos si existe ls BBDD.
            //Si no existe la creamos.
            datos = OperacionesBaseDatos.obtenerInstancia(context);
        } else {
            //Si existe la abrimos.
            datos = OperacionesBaseDatos.obtenerInstancia(context);
        }*/

        //Obtenemos las referencias de las vistas.
        txtRegister = (TextView) findViewById(R.id.txt_LinkToRegister);
        txtRecuperarPassword = (TextView) findViewById(R.id.txt_LinkToRecuperarPassword);
        txtCrearDatosPueba = (TextView) findViewById(R.id.txt_LinkToDatosPrueba);
        txtBorrarDatosPueba = (TextView) findViewById(R.id.txt_LinkToBorrarDatosPrueba);
        editTextCodigoOperario = (EditText) findViewById(R.id.editText_CodigoOperarioToLogin);
        editTextPassword = (EditText) findViewById(R.id.editText_PasswordToLogin);
        btnLogin = (Button) findViewById(R.id.btn_Login);

        //Establecemos los Listener
        txtRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Pasamos a la panttalla de registro
                Intent i = new Intent(getApplicationContext(), Registro.class);
                startActivity(i);
            }
        });

        txtRecuperarPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Pasamos a la pantalla para recuerar password
                Intent i = new Intent(getApplicationContext(), RecuperarPassword.class);
                startActivity(i);
            }
        });

        txtCrearDatosPueba.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (!checkDataBase()) {
                    //Creamos o abrimos la BBDD
                    datos = OperacionesBaseDatos.obtenerInstancia(context);
                    //Introducimos los datos de prueba
                    new TareaInsertarDatosPredeterminados().execute();//TODO Borrar cuando no hagan falta los datos de prueba
                } else{
                    Toast.makeText(MainActivity.this, context.getString(R.string.debe_borrar_bbdd), Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtBorrarDatosPueba.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Borramos la BBDD
               if(checkDataBase()){
                   datos = OperacionesBaseDatos.obtenerInstancia(context);
                   getApplicationContext().deleteDatabase("Fichajes.db");
                   datos.close();
                   Toast.makeText(MainActivity.this, context.getString(R.string.datos_borrados), Toast.LENGTH_SHORT).show();

                   //Establecemos los valores por defecto de las preferencias
                   PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
               }else{
                   Toast.makeText(MainActivity.this, context.getString(R.string.no_hay_datos), Toast.LENGTH_SHORT).show();
               }
            }
        });

        editTextCodigoOperario.addTextChangedListener(new TextWatcher() { //Establecemos un listener para validar el texto introducido.

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                codigoOperarioValidado = validarCodigoOperario(context, editTextCodigoOperario); //Comprobamos si el código de operario introducido es válido o no.
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {//Establecemos un listener para validar el texto introducido.

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                passwordValidado = validarPassword(context, editTextPassword); //Comprobamos si el password introducido es válido o no.
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Obtenemos el codigo de operario y el password introducido.
                String codigoOperario = editTextCodigoOperario.getText().toString();
                String password = editTextPassword.getText().toString();

                if ((!codigoOperario.isEmpty()) && (!password.isEmpty())) {//Se comprueba que los campos no estén vacios.
                    if (codigoOperarioValidado) {//Se comprueba que el código de operario introducido sea correcto
                        if (passwordValidado) {//Se comprueba que la contraseña introducida sea correcta
                            try {
                                datos = OperacionesBaseDatos.obtenerInstancia(context);
                                // Se recupera la contraseña del operario desde la base de datos.
                                String passwordAlmacenada = datos.obtenerPasswordOperarioId(codigoOperario);
                                // Se comprueba que la contraseña almacenada en la base de datos coincida con la contraseña introducida por el usuario.
                                if (password.equals(passwordAlmacenada)) {
                                    //Pasamos a la pantalla calendario
                                    Intent i = new Intent(getApplicationContext(), PantallaCalendarioActivity.class);
                                    i.putExtra("codigoOperario", codigoOperario);
                                    i.putExtra("password", password);
                                    startActivity(i);
                                } else {
                                    editTextPassword.setError(context.getString(R.string.password_erroneo));
                                    Toast.makeText(MainActivity.this, context.getString(R.string.password_erroneo), Toast.LENGTH_LONG).show();
                                }
                            } catch (CursorIndexOutOfBoundsException | NullPointerException CIOOBE) {//Si el código de operario no esta registrado mustra un mensaje indicándolo.
                                editTextPassword.setError(context.getString(R.string.operario_no_registrado));
                                Toast.makeText(MainActivity.this, context.getString(R.string.operario_no_registrado), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, context.getString(R.string.password_caracteres_erroneos), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, context.getString(R.string.codigo_operario_erroneo), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, context.getString(R.string.campo_vacio), Toast.LENGTH_LONG).show();
                }
            }
        });

        /*
        //Iniciamos Servicio
        ServicioGTT.setUpdateListener(this);
        iniciarServicio();*/
    }

    /**
     * Método que se encarga de inicar el servicio.
     */
    private void iniciarServicio() {
        Intent service = new Intent(this, ServicioGTT.class);
        startService(service);
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Inicio_turnos);
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
        }
        setSupportActionBar(toolbar);
    }

    /**
     * Método que se encarga de comprobar si existe o no la BBDD Fichajes.db
     *
     * @return Si existe o no la BBDD Fichajes.db
     */
    private boolean checkDataBase() {
        File fileDB = getApplicationContext().getDatabasePath(NOMBRE_BASE_DATOS);
        return (fileDB.exists());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_inicio, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Cerramos la BBDD
        if (checkDataBase()) {
            datos.close();
        }
    }

    /**
     * Tarea encargada de introducir los datos de prueba.
     */
    private class TareaInsertarDatosPredeterminados extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean correcto=false;
            try {
                datos.getDb().beginTransaction();

                // Inserción Operario
                String idOperario1 = datos.insertarOperario(new Operario("8246", "74880029x", "Antonio", "Carrillo Cuenca", "Rutafoto", "Direccion", "1985-07-16", "629916157", "antoniotiti@hotmail.com", "2014-10-01", "291075928768", "Password1"));

                // Inserción Puesto
                String idPuesto1 = datos.insertarPuesto(new Puesto(null, "Airbag", "Operario de Airbag y Ecorrun"));
                String idPuesto2 = datos.insertarPuesto(new Puesto(null, "CPD", "Operario del CPD"));

                // Inserción Calendario
                String idCalendario1 = datos.insertarCalendario(new Calendario(null, "Fujitsu", "Calendario de Fujitsu"));
                String idCalendario2 = datos.insertarCalendario(new Calendario(null, "Fujitsu2", "Calendario de Fujitsu2"));
                String idCalendario3 = datos.insertarCalendario(new Calendario(null, "Fujitsu3", "Calendario de Fujitsu3"));


                // Inserción Truno
                String idTurno1 = datos.insertarTurno(new Turno(null, "Mañana CPD", "M_CPD", "07:10", "15:00", 0, null, null, 7.83333349227905f, 0, 8.90, 12.52, 13, 1, 1, "06:30", "ADVA00", -16023485, -1));
                String idTurno2 = datos.insertarTurno(new Turno(null, "Tarde CPD", "T_CPD", "14:10", "22:00", 0, null, null, 7.83333349227905f, 0, 8.90, 12.52, 13, 1, 1, "13:30", "ADVAAD", -7461718, -16777216));
                String idTurno3 = datos.insertarTurno(new Turno(null, "Descanso", "DESC", "", "", 0, null, null, 0, 0, 0, 0, 0, 0, 0, "", "", -16540699, -1));
                String idTurno4 = datos.insertarTurno(new Turno(null, "Cortado", "CORT", "", "", 0, null, null, 0, 0, 0, 0, 0, 0, 0, "", "", -2818048, -1));
                String idTurno5 = datos.insertarTurno(new Turno(null, "Partido CPD", "P_CPD", "09:00", "14:00", 1, "16:00", "19:00", 8, 0, 8.90, 12.52, 13, 1, 1, "06:30", "ADVA00", -8812853, -1));
                String idTurno6 = datos.insertarTurno(new Turno(null, "Extra", "Extra", "", "", 0, null, null, 0, 0, 0, 0, 13, 0, 0, "", "", -606426, -1));

                // Inserción Fichaje
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-01", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-02", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-03", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-04", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-05", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-06", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-07", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-08", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-09", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-10", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-11", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-12", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-13", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-14", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-15", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-16", idTurno4, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-17", idTurno4, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-18", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-19", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-20", idTurno4, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-21", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-22", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-23", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-24", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-25", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-26", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-27", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-02-28", idTurno4, idPuesto2, idCalendario1, null, "comentario"));

                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-01", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-02", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-03", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-04", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-05", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-06", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-07", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-08", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-09", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-10", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-11", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-12", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-13", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-14", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-15", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-16", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-17", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-18", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-19", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-20", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-21", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-22", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-23", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-24", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-25", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-26", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-27", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-28", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-29", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-30", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-03-31", idTurno1, idPuesto2, idCalendario1, 1.0, "comentario"));

                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-01", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-02", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-03", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-04", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-05", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-06", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-07", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-08", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-09", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-10", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-11", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-12", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-13", idTurno4, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-14", idTurno4, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-15", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-16", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-17", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-18", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-19", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-20", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-21", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-22", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-23", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-24", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-25", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-26", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-27", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-28", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-29", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-04-30", idTurno3, idPuesto2, idCalendario1, null, "comentario"));

                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-01", idTurno4, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-02", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-03", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-04", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-05", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-06", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-07", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-08", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-09", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-10", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-11", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-12", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-13", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-14", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-15", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-16", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-17", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-18", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-19", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-20", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-21", idTurno6, idPuesto2, idCalendario1, 9.75, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-22", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-23", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-24", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-25", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-26", idTurno1, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-27", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-28", idTurno3, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-29", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-30", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                datos.insertarFichaje(new Fichaje(idOperario1, "2017-05-31", idTurno2, idPuesto2, idCalendario1, null, "comentario"));
                correcto=true;
                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }
            return correcto;
        }
        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(MainActivity.this, context.getString(R.string.datos_prueba_correctos), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, context.getString(R.string.error_datos_prueba), Toast.LENGTH_LONG).show();
            }
        }
    }
}