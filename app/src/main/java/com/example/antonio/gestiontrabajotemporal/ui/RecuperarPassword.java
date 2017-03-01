package com.example.antonio.gestiontrabajotemporal.ui;

import android.database.CursorIndexOutOfBoundsException;
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
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarCodigoOperario;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.validarEmail;

public class RecuperarPassword extends AppCompatActivity {

    // TODO:  Rellenar datos email
    private static final String EMAIL_REMITENTE = "antoniotiti@gmail.com";
    private static final String PASSWORD = "bart16tyti";

    Button btnRecuperarPassword;
    EditText editTextCodigoOperario, editTextEmail;
    boolean codigoOperarioValidado, emailValidado;
    OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recuperar_password);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        // Obtenemos las referencias de las vistas
        editTextCodigoOperario = (EditText) findViewById(R.id.editText_CodigoOperarioToRecuperar);
        editTextCodigoOperario.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                codigoOperarioValidado = validarCodigoOperario(editTextCodigoOperario);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
        });

        editTextEmail = (EditText) findViewById(R.id.editText_EmailTorecuperar);
        editTextEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                emailValidado = validarEmail(editTextEmail);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        btnRecuperarPassword = (Button) findViewById(R.id.btn_RecuperarPassword);
        // Set On ClickListener
        btnRecuperarPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //TODO eliminar pruebas
                editTextCodigoOperario.setText("8246");
                editTextEmail.setText("antoniotiti@hotmail.com");

                //Obtenemos el operario y el email introducido por el usuario
                String codigoOperario = editTextCodigoOperario.getText().toString();
                String email = editTextEmail.getText().toString();

                if ((!codigoOperario.isEmpty()) && (!email.isEmpty())) {//Se comprueba que los campos no estén vacios.
                    if (codigoOperarioValidado) {//Se comprueba que el código de operario introducido sea correcto
                        if (emailValidado) {//Se comprueba que el email introducido sea correcto
                            try {
                                // Se recupera la contraseña del operario desde la base de datos.
                                String passwordAlmacenada = datos.obtenerPasswordOperarioId(codigoOperario, email);
                                Toast.makeText(RecuperarPassword.this, passwordAlmacenada, Toast.LENGTH_LONG).show();
                                enviarEmail(codigoOperario, email, passwordAlmacenada);


                            } catch (CursorIndexOutOfBoundsException CIOOBE) {//Si el código de operario o el email no estan registrado mustra un mensaje indicándolo.
                                editTextEmail.setError("Operario no registrado");
                                Log.d("Error", CIOOBE.toString());
                                Toast.makeText(RecuperarPassword.this, "Operario no registrado", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RecuperarPassword.this, "El email introducido no es válido", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RecuperarPassword.this, "El Código de operario debe contener 4 dígitos comenzando por 8", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RecuperarPassword.this, "Campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void enviarEmail(final String codigoOperario, final String emailUsuario, final String passwordAlmacenada) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {

                boolean enviado;
                try {
                    //La configuración es para una cuenta de gmail.
                    Properties props = new Properties();
                    // Nombre del host de correo, es smtp.gmail.com
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    // TLS si está disponible
                    props.setProperty("mail.smtp.starttls.enable", "true");
                    // Puerto de gmail para envio de correos
                    props.setProperty("mail.smtp.port", "587");
                    // Nombre del usuario
                    props.setProperty("mail.smtp.user", EMAIL_REMITENTE);
                    // Si requiere o no usuario y password para conectarse.
                    props.setProperty("mail.smtp.auth", "true");

                    // Se obtiene el objeto Session.
                    Session session = Session.getDefaultInstance(props, null);
                    // session.setDebug(true);// para obtener más información por pantalla de lo que está sucediendo

                    // Se compone el correo, dando to, from, subject
                    MimeMessage message = new MimeMessage(session);
                    // Quien envia el correo
                    message.setFrom(new InternetAddress(EMAIL_REMITENTE));
                    // A quien va dirigido
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailUsuario));
                    // Asunto
                    message.setSubject("Recuperacion Contraseña");
                    //Mensaje
                    //message.setText("Esta es la contraseña: " + passwordAlmacenada);
                    message.setText("La contraseña para el operario: <b>" + codigoOperario +"</b> es: <b>" + passwordAlmacenada + "</b>", "ISO-8859-1", "html");

                    //Para enviar el mensaje usamos la clase Transport, que se obtiene de Session.
                    Transport t = session.getTransport("smtp");
                    // Establecemos la conexión, dando el nombre de usuario y password.
                    t.connect(EMAIL_REMITENTE, PASSWORD);
                    // Enviamos el mensaje.
                    t.sendMessage(message, message.getAllRecipients());
                    //Cerramos la conexión
                    t.close();
                    enviado = true;
                } catch (Exception e) {
                    enviado = false;
                    e.printStackTrace();
                }
                return enviado;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    Toast.makeText(RecuperarPassword.this, "Email enviado correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RecuperarPassword.this, "Ha ocurrido un erro Email No enviado", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

}
