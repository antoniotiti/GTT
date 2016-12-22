package com.example.antonio.gestiontrabajotemporal.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEmail;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarFecha;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarNSS;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarPassword;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarTelefono;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEditTextVacio;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarNieONif;

public class Registro extends Activity {

    EditText editTextCodigoOperario, editTextDni, editTextNombre, editTextApellidos, editTextFechaNacimiento, editTextNumeroSS, editTextDireccion, editTextTelefono, editTextEmail, editTextPassword, editTextConfirmarPassword;
    Button btnCreateAccount;
    Boolean codigoOperarioValidado = false, dniValidado = false, nombreValidado = false, appelidosValidado = false, fechaNacimientoValidado = false, numeroSSValidado = false, direccionValidado = false, telefonoValidado = false, emailValidado = false, passwordValidado = false, confirmarPasswordValidado = false;

    OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        // Obtenemos las referencias de las vistas.
        editTextCodigoOperario = (EditText) findViewById(R.id.editText_CodigoOperarioToRegister);
        editTextCodigoOperario.addTextChangedListener(new MyTextWatcher(editTextCodigoOperario));

        editTextDni = (EditText) findViewById(R.id.editText_DniToRegister);
        editTextDni.addTextChangedListener(new MyTextWatcher(editTextDni));

        editTextNombre = (EditText) findViewById(R.id.editText_NombreToRegister);
        editTextNombre.addTextChangedListener(new MyTextWatcher(editTextNombre));

        editTextApellidos = (EditText) findViewById(R.id.editText_ApellidoToRegister);
        editTextApellidos.addTextChangedListener(new MyTextWatcher(editTextApellidos));

        editTextFechaNacimiento = (EditText) findViewById(R.id.editText_FechaNacimientoToRegister);
        editTextFechaNacimiento.addTextChangedListener(new MyTextWatcher(editTextFechaNacimiento));

        editTextNumeroSS = (EditText) findViewById(R.id.editText_NSSToRegister);
        editTextNumeroSS.addTextChangedListener(new MyTextWatcher(editTextNumeroSS));

        editTextDireccion = (EditText) findViewById(R.id.editText_DireccionToRegister);
        editTextDireccion.addTextChangedListener(new MyTextWatcher(editTextDireccion));

        editTextTelefono = (EditText) findViewById(R.id.editText_TelefonoToRegister);
        editTextTelefono.addTextChangedListener(new MyTextWatcher(editTextTelefono));

        editTextEmail = (EditText) findViewById(R.id.editText_EmailToRegister);
        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));

        editTextPassword = (EditText) findViewById(R.id.editText_PasswordToRegister);
        editTextPassword.addTextChangedListener(new MyTextWatcher(editTextPassword));

        editTextConfirmarPassword = (EditText) findViewById(R.id.editText_ConfirmarPasswordToRegister);
        editTextConfirmarPassword.addTextChangedListener(new MyTextWatcher(editTextConfirmarPassword));

        btnCreateAccount = (Button) findViewById(R.id.button_CreateAccount);
        // Set On ClickListener
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (codigoOperarioValidado && dniValidado && nombreValidado && appelidosValidado && fechaNacimientoValidado && numeroSSValidado && dniValidado && telefonoValidado && emailValidado && passwordValidado && confirmarPasswordValidado) {

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

                    // Comprobar password
                    if (password.equals(confirmarPassword)) {
                        Operario operarioInsertar = new Operario(codigoOperario, dni, nombre, apellidos, direccion, fechaNacimiento, telefono, email, numeroSS, password);
                        // Save the Data in Database
                        datos.insertarOperario(operarioInsertar);
                        Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Snackbar.make(findViewById(R.id.contenedorRegistro), "Rellene todos los campos correctamente", Snackbar.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "Rellene todos los campos correctamente", Toast.LENGTH_LONG).show();
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
                    codigoOperarioValidado = validarCodigoOperario(editTextCodigoOperario);
                    break;
                case R.id.editText_DniToRegister:
                    direccionValidado = validarNieONif(editTextDni);
                    break;
                case R.id.editText_NombreToRegister:
                    nombreValidado = validarEditTextVacio(editTextNombre);
                    break;
                case R.id.editText_ApellidoToRegister:
                    appelidosValidado = validarEditTextVacio(editTextApellidos);
                    break;
                case R.id.editText_FechaNacimientoToRegister:
                    fechaNacimientoValidado = validarFecha(editTextFechaNacimiento);
                    break;
                case R.id.editText_NSSToRegister:
                    nombreValidado = validarNSS(editTextNumeroSS);
                    break;
                case R.id.editText_DireccionToRegister:
                    direccionValidado = validarEditTextVacio(editTextDireccion);
                    break;
                case R.id.editText_TelefonoToRegister:
                    telefonoValidado = validarTelefono(editTextTelefono);
                    break;
                case R.id.editText_EmailToRegister:
                    emailValidado = validarEmail(editTextEmail);
                    break;
                case R.id.editText_PasswordToRegister:
                    passwordValidado = validarPassword(editTextPassword);
                    break;
                case R.id.editText_ConfirmarPasswordToRegister:
                    confirmarPasswordValidado = validarPassword(editTextConfirmarPassword);
                    break;
            }
        }
    }
}
