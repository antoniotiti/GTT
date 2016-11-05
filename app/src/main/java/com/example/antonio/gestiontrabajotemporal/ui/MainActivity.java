package com.example.antonio.gestiontrabajotemporal.ui;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

import java.io.File;
import java.util.Calendar;

import static com.example.antonio.gestiontrabajotemporal.util.Validar.*;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    TextView txtRegister;
    EditText editTextCodigoOperario, editTextPassword;
    boolean codigoOperarioValidado, passwordValidado;

    OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Eliminar Tabla
        //getApplicationContext().deleteDatabase("Fichajes.db");

        if (!checkDataBase()) {
            //Crear BD
            datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
            new TareaInsertarDatosPredeterminados().execute();
        } else {
            //Abrir BD
            datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        }

        // get the Refferences of views
        txtRegister = (TextView) findViewById(R.id.txt_LinkToRegister);
        editTextCodigoOperario = (EditText) findViewById(R.id.editText_CodigoOperarioToLogin);

        editTextCodigoOperario.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                codigoOperarioValidado=validarCodigoOperario(getApplicationContext(), editTextCodigoOperario);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        /*.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarCodigoOperario(getApplicationContext(), editTextCodigoOperario);
                }
            }
        });*/

        editTextPassword = (EditText) findViewById(R.id.editText_PasswordToLogin);
        editTextPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                passwordValidado= validarPassword(getApplicationContext(), editTextPassword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        btnLogin = (Button) findViewById(R.id.btn_Login);
        // Set On ClickListener
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Pruebas
                editTextCodigoOperario.setText("8246");
                editTextPassword.setText("Bart16tyti");

               // boolean codigoOperarioValidado, passwordValidado;
                // get The User name and Password
                String codigoOperario = editTextCodigoOperario.getText().toString();
                String password = editTextPassword.getText().toString();

                //codigoOperarioValidado = validarCodigoOperario(getApplicationContext(), editTextCodigoOperario);

                //editTextCodigoOperario.setBackgroundColor(Color.WHITE);
               // passwordValidado = validarPassword(getApplicationContext(), editTextPassword);
                //editTextPassword.setBackgroundColor(Color.WHITE);

                if ((validarCodigoOperario(getApplicationContext(), editTextCodigoOperario)) && (validarPassword(getApplicationContext(), editTextPassword))) {
                    try {
                        // fetch the Password form database for respective user name
                        String passwordAlmacenada = datos.obtenerPasswordOperarioId(codigoOperario);
                        // check if the Stored password matches with  Password entered by user
                        if (password.equals(passwordAlmacenada)) {

                            //TODO: Login correcto ir a otra ventana
                            Toast.makeText(MainActivity.this, "Login Correcto", Toast.LENGTH_LONG).show();
                            // Switching to Register screen
                            Intent i = new Intent(getApplicationContext(), PantallaCalendario.class);
                            i.putExtra("codigoOperario", codigoOperario);
                            i.putExtra("password", password);
                            startActivity(i);

                        } else {
                            Toast.makeText(MainActivity.this, "Contraseña erronea", Toast.LENGTH_LONG).show();
                        }
                    } catch (CursorIndexOutOfBoundsException CIOOBE) {
                        Log.d("Error", CIOOBE.toString());
                        Toast.makeText(MainActivity.this, "Usuario no registrado", Toast.LENGTH_LONG).show();
                    }
                }/*else{
                        editTextPassword.requestFocus();
                        editTextPassword.setBackgroundColor(Color.RED);
                        Toast.makeText(MainActivity.this, "Password no válido", Toast.LENGTH_LONG).show();
                    }*/
               /* } else {
                    editTextCodigoOperario.requestFocus();
                    editTextCodigoOperario.setBackgroundColor(Color.RED);
                    Toast.makeText(MainActivity.this, "Código de operario no válido", Toast.LENGTH_LONG).show();

                }*/


            }
        });

        // Listening to register new account link
        txtRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), Registro.class);
                startActivity(i);
            }
        });
    }

    public boolean checkDataBase() {
        File fileDB;
        fileDB = getApplicationContext().getDatabasePath("Fichajes.db");
        boolean checkDB = fileDB.exists();
        return (checkDB);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        datos.close();
    }

    public class TareaInsertarDatosPredeterminados extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {

                datos.getDb().beginTransaction();

                // Inserción Puesto
                String puesto1 = datos.insertarPuesto(new Puesto(null, "Airbag"));
                String puesto2 = datos.insertarPuesto(new Puesto(null, "CPD"));

                // Inserción Calendario
                String calendario1 = datos.insertarCalendario(new Calendario(null, "Fujitsu"));

                // Inserción Operario
                String operario1 = datos.insertarOperario(new Operario("8246", "74880029x", "Antonio", "Carrillo Cuenca", "foto", "Direccion", "16/07/1985", "629916157", "antoniotiti@hotmail.com", "1/10/204", "numeross", "Bart16tyti"));

                // Inserción Truno
                String turno1 = datos.insertarTurno(new Turno(null, "Mañana CPD", "M_CPD", "06:50", "15:00", 0, "inicio2", "fin2", 8, 0, 8.82, 12.64, 11.60, 1, "ruta alarma", 1, "06:30", "modotelefono"));
                String turno2 = datos.insertarTurno(new Turno(null, "Tarde CPD", "T_CPD", "13:50", "22:00", 0, "inicio2", "fin2", 8, 0, 8.82, 12.64, 11.60, 1, "ruta alarma", 1, "13:30", "modotelefono"));

                // Inserción Fichaje
                datos.insertarFichaje(new Fichaje("8246", "01/01/2016", "1", "1", "1", 1.5));

                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }

            // [QUERIES]
            Log.d("Calendario", "Calendario");
            DatabaseUtils.dumpCursor(datos.obtenerCalendarios());
            Log.d("Puesto", "Puesto");
            DatabaseUtils.dumpCursor(datos.obtenerPuestos());
            Log.d("Operario", "Operario");
            DatabaseUtils.dumpCursor(datos.obtenerOperarios());
            Log.d("Turno", "Turno");
            DatabaseUtils.dumpCursor(datos.obtenerTurnos());
            //  Log.d("Fichaje", "Fichaje");
            // DatabaseUtils.dumpCursor(datos.obtenerFichajes());
            return null;
        }
    }
}




