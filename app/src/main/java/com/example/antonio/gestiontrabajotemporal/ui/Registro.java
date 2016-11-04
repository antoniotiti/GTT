package com.example.antonio.gestiontrabajotemporal.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.modelo.Operario;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

public class Registro extends Activity {

    EditText editTextCodigoOperario, editTextDni, editTextNombre, editTextApellidos, editTextFechaNacimiento, editTextNumeroSS, editTextDireccion, editTextTelefono, editTextEmail, editTextPassword, editTextConfirmarPassword;
    Button btnCreateAccount;

    OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // get Instance  of Database Adapter
        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        // Get Refferences of Views
        editTextCodigoOperario = (EditText) findViewById(R.id.editText_CodigoOperarioToLogin);
        editTextDni = (EditText) findViewById(R.id.editText_DniToRegister);
        editTextNombre = (EditText) findViewById(R.id.editText_NombreToRegister);
        editTextApellidos = (EditText) findViewById(R.id.editText_ApellidoToRegister);
        editTextFechaNacimiento = (EditText) findViewById(R.id.editText_FechaNacimientoToRegister);
        editTextNumeroSS = (EditText) findViewById(R.id.editText_NSSToRegister);
        editTextDireccion = (EditText) findViewById(R.id.editText_DireccionToRegister);
        editTextTelefono = (EditText) findViewById(R.id.editText_TelefonoToRegister);
        editTextEmail = (EditText) findViewById(R.id.editText_EmailToRegister);
        editTextPassword = (EditText) findViewById(R.id.editText_PasswordToRegister);
        editTextConfirmarPassword = (EditText) findViewById(R.id.editText_ConfirmarPasswordToRegister);

        btnCreateAccount = (Button) findViewById(R.id.button_CreateAccount);
        // Set On ClickListener
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String codigoOperario = editTextCodigoOperario.getText().toString();
                String dni = editTextDni.getText().toString();
                String nombre = editTextNombre.getText().toString();
                String apellidos = editTextApellidos.getText().toString();
                String fechaNacimiento = editTextFechaNacimiento.getText().toString();
                String numeroSS = editTextNumeroSS.getText().toString();
                String direccion = editTextDireccion.getText().toString();
                String telefono = editTextTelefono.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmarPassword = editTextConfirmarPassword.getText().toString();

                Operario operarioInsertar = new Operario(codigoOperario, dni, nombre, apellidos, direccion, fechaNacimiento, telefono, email, numeroSS, password);

                // Comprobar campos
                if (codigoOperario.equals("") || password.equals("") || confirmarPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                // Comprobar password
                if (!password.equals(confirmarPassword)) {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // Save the Data in Database
                    datos.insertarOperario(operarioInsertar);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        datos.close();
    }
}

/* private TextView dni, cod_operario, nombre, apellidos, email, fecha_nacimiento, telefono, password, repetir_password;
     Button btn_Registrar;


     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         // Set View to register.xml
         setContentView(R.layout.registroDatos);

         dni = (TextView) findViewById(R.idPuesto.ed_txt_dni);
         cod_operario = (TextView) findViewById(R.idPuesto.ed_txt_codigo_operario);
         nombre = (TextView) findViewById(R.idPuesto.ed_txt_nombre);
         apellidos = (TextView) findViewById(R.idPuesto.ed_txt_apellidos);
         email = (TextView) findViewById(R.idPuesto.ed_txt_email);
         fecha_nacimiento = (TextView) findViewById(R.idPuesto.ed_txt_fecha_nacimiento);
         telefono = (TextView) findViewById(R.idPuesto.ed_txt_telefono);
         password = (TextView) findViewById(R.idPuesto.ed_txt_password);
         repetir_password = (TextView) findViewById(R.idPuesto.ed_txt_repetir_password);


         btn_Registrar = (Button) findViewById(R.idPuesto.btn_Registrar);
         // Listening to register new account link
         btn_Registrar.setOnClickListener(new View.OnClickListener() {

             public void onClick(View v) {
                 // Switching to Register screen
                 Intent i = new Intent(getApplicationContext(), Registro.class);
                 startActivity(i);

             }
         });
     }*/