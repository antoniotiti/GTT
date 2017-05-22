package com.example.antonio.gestiontrabajotemporal.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.modelo.Operario;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarCodigoOperario;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEditTextVacio;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEmail;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarFecha;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarNSS;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarNieONif;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarPassword;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarTelefono;

public class Registro extends AppCompatActivity {

    EditText editTextCodigoOperario, editTextDni, editTextNombre, editTextApellidos, editTextFechaNacimiento, editTextNumeroSS, editTextDireccion, editTextTelefono, editTextEmail, editTextPassword, editTextConfirmarPassword;
    Button btnCreateAccount;
    Boolean codigoOperarioValidado = false, dniValidado = false, nombreValidado = false, appelidosValidado = false, fechaNacimientoValidado = false, numeroSSValidado = false, direccionValidado = false, telefonoValidado = false, emailValidado = false, passwordValidado = false, confirmarPasswordValidado = false;

    OperacionesBaseDatos datos;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        context = getApplicationContext();

        setToolbar(); //Añadir la Toolbar.

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(context);

        // Obtenemos las referencias de las vistas.
        editTextCodigoOperario = (EditText) findViewById(R.id.editText_CodigoOperarioToRegister);
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
        editTextPassword.addTextChangedListener(new MyTextWatcher(editTextPassword));
        editTextConfirmarPassword.addTextChangedListener(new MyTextWatcher(editTextConfirmarPassword));

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (codigoOperarioValidado && dniValidado && nombreValidado && appelidosValidado &&
                        fechaNacimientoValidado && numeroSSValidado && direccionValidado && telefonoValidado
                        && emailValidado && passwordValidado && confirmarPasswordValidado) {

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

                    //Comprobar si coinciden las passwords
                    if (password.equals(confirmarPassword)) {
                        Operario operarioInsertar = new Operario(codigoOperario, dni, nombre, apellidos, direccion, fechaNacimiento, telefono, email, numeroSS, password);
                        //Insertamos el Operario
                        datos.insertarOperario(operarioInsertar);
                        Toast.makeText(getApplicationContext(), context.getString(R.string.operario_creado_correctamente), Toast.LENGTH_LONG).show();
                        finish();//Cerramos pantalla de registro
                    } else {
                        Toast.makeText(getApplicationContext(), context.getString(R.string.password_no_coinciden), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.contenedorRegistro), context.getString(R.string.rellenar_campor_correctamente), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        datos.close();
    }
    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_registro);
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.
    }

    /**
     * Método al que se llama cuando se utiliza el botón de retroceso de la SupportActionBar.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                case R.id.editText_CodigoOperarioToRegister:
                    codigoOperarioValidado = validarCodigoOperario(context, editTextCodigoOperario);
                    break;
                case R.id.editText_DniToRegister:
                    dniValidado = validarNieONif(context, editTextDni);
                    break;
                case R.id.editText_NombreToRegister:
                    nombreValidado = validarEditTextVacio(context, editTextNombre);
                    break;
                case R.id.editText_ApellidoToRegister:
                    appelidosValidado = validarEditTextVacio(context, editTextApellidos);
                    break;
                case R.id.editText_FechaNacimientoToRegister:
                    fechaNacimientoValidado = validarFecha(context, editTextFechaNacimiento);
                    break;
                case R.id.editText_NSSToRegister:
                    numeroSSValidado = validarNSS(context, editTextNumeroSS);
                    break;
                case R.id.editText_DireccionToRegister:
                    direccionValidado = validarEditTextVacio(context, editTextDireccion);
                    break;
                case R.id.editText_TelefonoToRegister:
                    telefonoValidado = validarTelefono(context, editTextTelefono);
                    break;
                case R.id.editText_EmailToRegister:
                    emailValidado = validarEmail(context, editTextEmail);
                    break;
                case R.id.editText_PasswordToRegister:
                    passwordValidado = validarPassword(context, editTextPassword);
                    break;
                case R.id.editText_ConfirmarPasswordToRegister:
                    confirmarPasswordValidado = validarPassword(context, editTextConfirmarPassword);
                    break;
            }
        }
    }
}