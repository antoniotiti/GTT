package com.example.antonio.gestiontrabajotemporal.ui;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);
        context = getApplicationContext();

        setToolbar(); //Añadir la Toolbar.

        datos = OperacionesBaseDatos.obtenerInstancia(context);

        // Obtenemos las referencias de las vistas
        editTextCodigoOperario = (EditText) findViewById(R.id.editText_CodigoOperarioToRecuperar);
        editTextEmail = (EditText) findViewById(R.id.editText_EmailTorecuperar);
        btnRecuperarPassword = (Button) findViewById(R.id.btn_RecuperarPassword);

        //Establecemos los Listener
        editTextCodigoOperario.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                codigoOperarioValidado = validarCodigoOperario(context, editTextCodigoOperario);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                emailValidado = validarEmail(context, editTextEmail);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        btnRecuperarPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //Obtenemos el operario y el email introducido por el usuario
                String codigoOperario = editTextCodigoOperario.getText().toString();
                String email = editTextEmail.getText().toString();

                if ((!codigoOperario.isEmpty()) && (!email.isEmpty())) {//Se comprueba que los campos no estén vacios.
                    if (codigoOperarioValidado) {//Se comprueba que el código de operario introducido sea correcto
                        if (emailValidado) {//Se comprueba que el email introducido sea correcto
                            try {
                                // Se recupera la contraseña del operario desde la base de datos.
                                String passwordAlmacenada = datos.obtenerPasswordOperarioId(codigoOperario, email);
                                enviarEmail(codigoOperario, email, passwordAlmacenada);//Enviamos la contraseña por email.
                            } catch (CursorIndexOutOfBoundsException CIOOBE) {//Si el código de operario o el email no estan registrado mustra un mensaje indicándolo.
                                Toast.makeText(RecuperarPassword.this, context.getString(R.string.error_datos_introducido), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RecuperarPassword.this, context.getString(R.string.email_no_valido), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RecuperarPassword.this, context.getString(R.string.codigo_operario_erroneo), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RecuperarPassword.this, context.getString(R.string.campo_vacio), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recuperar_password);
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
     * Método que se encarga de enviar por email la contraseña del usuario
     *
     * @param codigoOperario     Código del operario
     * @param emailUsuario       Email del usuario donde se va a enviar la contraseña
     * @param passwordAlmacenada Contraseña del usuario
     */
    private void enviarEmail(final String codigoOperario, final String emailUsuario, final String passwordAlmacenada) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {

                boolean enviado;
                try {
                    //La configuración es para una cuenta de gmail.
                    Properties props = new Properties();
                    //Nombre del host de correo, es smtp.gmail.com
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    //TLS si está disponible
                    props.setProperty("mail.smtp.starttls.enable", "true");
                    //Puerto de gmail para envio de correos
                    props.setProperty("mail.smtp.port", "587");
                    //Nombre del usuario
                    props.setProperty("mail.smtp.user", EMAIL_REMITENTE);
                    //Si requiere o no usuario y password para conectarse.
                    props.setProperty("mail.smtp.auth", "true");

                    //Se obtiene el objeto Session.
                    Session session = Session.getDefaultInstance(props, null);
                    //session.setDebug(true);// para obtener más información por pantalla de lo que está sucediendo

                    //Se compone el correo, dando to, from, subject
                    MimeMessage message = new MimeMessage(session);
                    //Quien envia el correo
                    message.setFrom(new InternetAddress(EMAIL_REMITENTE));
                    //A quien va dirigido
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailUsuario));
                    //Asunto
                    message.setSubject("Recuperacion Contraseña");
                    //Mensaje
                    message.setText("La contraseña para el operario: <b>" + codigoOperario + "</b> es: <b>" + passwordAlmacenada + "</b>", "ISO-8859-1", "html");

                    //Para enviar el mensaje usamos la clase Transport, que se obtiene de Session.
                    Transport t = session.getTransport("smtp");
                    //Establecemos la conexión, dando el nombre de usuario y password.
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
                    Toast.makeText(RecuperarPassword.this, context.getString(R.string.email_enviado_corectamente), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RecuperarPassword.this, context.getString(R.string.error_enviar_email), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}