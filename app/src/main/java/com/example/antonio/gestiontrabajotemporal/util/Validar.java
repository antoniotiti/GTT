package com.example.antonio.gestiontrabajotemporal.util;

import android.content.Context;
import android.widget.EditText;

import com.example.antonio.gestiontrabajotemporal.R;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.formatter_fecha;

/**
 * Clase que contiene métodos para validar datos.
 */
public final class Validar {

    private static final int MAX_LENGHT_TELEFONO = 9; //Tamaño máximo para un número de teléfono.
    private static final int MAX_LENGHT_CODIGO_OPERARIO = 4; //Tamaño máximo para el código de operario.
    private static final String INICIO_CODIGO_OPERARIO = "8"; //El código de operario debe iniciar por 8.

    private Validar() {
        throw new UnsupportedOperationException("Not instanciable class!");
    }

    /**
     * Método que se encarga de comprobar que el código de operario introducido sea correcto.
     * Debe ser un número de 4 cifras que comience por 8.
     *
     * @param context          Contexto de la app
     * @param eTCodigoOperario Código del Operario introducido por el usuario a validar.
     * @return Si el código de operario introducido es correcto o no lo es.
     */
    public static boolean validarCodigoOperario(Context context, EditText eTCodigoOperario) {

        String codigoOperario = eTCodigoOperario.getText().toString();

        boolean validado = false;

        if (codigoOperario.length() == MAX_LENGHT_CODIGO_OPERARIO && codigoOperario.startsWith(INICIO_CODIGO_OPERARIO)) {
            try {
                Integer.parseInt(codigoOperario);
            } catch (NumberFormatException e) {
                eTCodigoOperario.setError(context.getString(R.string.codigo_operario_no_valido));
            }
            validado = true;
            eTCodigoOperario.setError(null);
        } else {
            eTCodigoOperario.setError(context.getString(R.string.codigo_operario_no_valido));
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el password introducido por el usuario sea correcto.
     * El password debe tener como mínimo 8 caracteres, debe incluir mayúsculas y minúsculas,
     * al menos un número y no debe contener espacios en blanco.
     *
     * @param context    Contexto de la app
     * @param eTPassword Password introducido por el usuario a validar.
     * @return Si el passwordintroducido es correcto o no lo es.
     */
    public static boolean validarPassword(Context context, EditText eTPassword) {

        String password = eTPassword.getText().toString();
        boolean validado = false;

        Pattern p = Pattern.compile(context.getString(R.string.password_reg_exp));
        Matcher m = p.matcher(password);
        if ((m.matches())) {
            validado = true;
            eTPassword.setError(null);
        } else {
            eTPassword.setError(context.getString(R.string.password_no_valido));
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el EditText pasado por parámetro no esté vacío.
     *
     * @param context Contexto de la app
     * @param eTTexto EditText a comprobar
     * @return Si el EditText esta vacío o no
     */
    public static boolean validarEditTextVacio(Context context, EditText eTTexto) {

        String texto = eTTexto.getText().toString();
        boolean validado = false;

        if (!texto.isEmpty()) {
            validado = true;
            eTTexto.setError(null);
        } else {
            eTTexto.setError(context.getString(R.string.campo_vacio));
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el EditText con la hora pasado por parámetro no esté vacío,
     * si está vacio inserta 00:00
     *
     * @param context Contexto de la app
     * @param eTTexto EditText con la hora a comprobar
     */
    public static void validarHoraVacio(Context context, EditText eTTexto) {

        String texto = eTTexto.getText().toString();
        if (texto.isEmpty()) {
            eTTexto.setText(R.string.hora_00);
        }

    }

    /**
     * Método que se encarga de comprobar que la fecha introducida por el
     * usuario sea correcta y en el siguiente formato: yyyy-MM-dd.
     *
     * @param context  Contexto de la app
     * @param eTTfecha Fecha introducida por el usuario.
     * @return Si la fecha es correcta o no lo es.
     */
    public static boolean validarFecha(Context context, EditText eTTfecha) {

        String fecha = eTTfecha.getText().toString();

        boolean validado = false;
        formatter_fecha.setLenient(false);
        try {
            //TODO que coja el año bien
            Date fechacorrecta = formatter_fecha.parse(fecha);
            validado = true;
            eTTfecha.setError(null);
        } catch (ParseException e) {
            eTTfecha.setError(context.getString(R.string.formato_fecha_correcto));
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el número de la seguridad social introducido sea válido.
     *
     * @param context Contexto de la app
     * @param eTNSS   Número de la Seguridad Social introducido.
     * @return Si el Número de la Seguridad Social introducido es válido o no.
     */
    public static boolean validarNSS(Context context, EditText eTNSS) {

        String nSS = eTNSS.getText().toString();

        long resultado, controlObtenido;
        boolean validado = false;
        if (nSS.length() == 12) {

            int provincia = Integer.valueOf(nSS.substring(0, 2));
            int numero = Integer.valueOf(nSS.substring(2, 10));
            int control = Integer.valueOf(nSS.substring(10));

            if (numero < 10000000) {
                resultado = (numero + provincia) * 10000000;
            } else {
                resultado = Long.valueOf(nSS.substring(0, 10));
            }
            controlObtenido = (resultado % 97);

            if (control == controlObtenido) {
                validado = true;
                eTNSS.setError(null);
            } else {
                eTNSS.setError(context.getString(R.string.nss_no_valido));
            }
        } else {
            eTNSS.setError(context.getString(R.string.nss_12_digitos));
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el número de teléfono introducido
     * por el usuario sea correcto.
     *
     * @param context    Contexto de la app
     * @param eTTelefono Número de teléfono introducido por el usuario.
     * @return Si el número de teléfono es correcto o no lo es.
     */
    public static boolean validarTelefono(Context context, EditText eTTelefono) {

        String telefono = eTTelefono.getText().toString();

        boolean validado = false;
        if ((telefono.length() == MAX_LENGHT_TELEFONO) && ((telefono.charAt(0) == '6')
                || (telefono.charAt(0) == '7') || (telefono.charAt(0) == '8')
                || (telefono.charAt(0) == '9'))) {
            validado = true;
            eTTelefono.setError(null);
        } else {
            eTTelefono.setError(context.getString(R.string.telefono_no_valido));
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el email introducido por el usuario sea correcto.
     *
     * @param context Contexto de la app
     * @param eTEmail Email introducido por el usuario.
     * @return Si el email es correcto o no
     */
    public static boolean validarEmail(Context context, EditText eTEmail) {

        String email = eTEmail.getText().toString();

        boolean validado = false;
        Pattern p = android.util.Patterns.EMAIL_ADDRESS;
        Matcher m = p.matcher(email);
        if ((m.matches())) {
            validado = true;
            eTEmail.setError(null);
        } else {
            eTEmail.setError(context.getString(R.string.email_no_valido));
        }
        return validado;
    }

    /**
     * Método que se encarga de validar el NIE o NIF introducido por el
     * usuario.
     *
     * @param context   Contexto de la app
     * @param eTNieONif NIE o NIF introducido por el usuario.
     * @return Si el NIE o NIF es correcto o no lo es.
     */
    public static boolean validarNieONif(Context context, EditText eTNieONif) {

        String nieONif = eTNieONif.getText().toString();

        int resultado;
        boolean validado = false;
        if (esCorrecto(nieONif)) {
            String letra = nieONif.substring(8);
            if (esNie(nieONif)) {
                String nie = sustituirLetra(nieONif);
                resultado = Integer.valueOf(nie.substring(0, 8)) % 23;
            } else {
                resultado = Integer.valueOf(nieONif.substring(0, 8)) % 23;
            }
            if ((obtenerLetra(resultado).equalsIgnoreCase(letra))) {
                validado = true;
            } else {
                eTNieONif.setError(context.getString(R.string.letra_dni_no_valido));
            }
        } else {
            eTNieONif.setError(context.getString(R.string.dni_no_valido));

        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar si el NIE o NIF introducido por el
     * usuario es correcto.
     *
     * @param nieONif NIE o NIF introducido por el usuario.
     * @return Si el NIE o NIF es correcto o no lo es.
     */
    private static boolean esCorrecto(String nieONif) {
        boolean validado = false;
        Pattern p = Pattern.compile("([0-9]{8}[a-zA-Z])|([XxYyZz][0-9]{7}[a-zA-Z])");
        Matcher m = p.matcher(nieONif);
        if ((nieONif.length() == 9) && (m.matches())) {
            validado = true;
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar si es un NIE.
     *
     * @param nieONif NIE o NIF introducido por el usuario.
     * @return Si es un NIE o no lo es.
     */
    private static boolean esNie(String nieONif) {

        return ((nieONif.toUpperCase().charAt(0) == 'X')
                || (nieONif.toUpperCase().charAt(0) == 'Y')
                || (nieONif.toUpperCase().charAt(0) == 'Z'));
    }

    /**
     * Método que se encarga de sustituir la primera letra del NIE introducido.
     *
     * @param nieONif NIE introducido por el usuario.
     * @return NIE introducido por el usuario modificado.
     */
    private static String sustituirLetra(String nieONif) {

        String resultado;
        switch (nieONif.toUpperCase().charAt(0)) {
            case 'X':
                resultado = ("0" + nieONif.substring(1));
                break;
            case 'Y':
                resultado = ("1" + nieONif.substring(1));
                break;
            default:
                resultado = ("2" + nieONif.substring(1));
                break;
        }
        return resultado;
    }

    /**
     * Método que se encarga de obtener la letra a través del número obtenido
     * por el algoritmo módulo 23 del número del NIE o NIF.
     *
     * @param resultado número obtenido por el algoritmo módulo 23 del número
     *                  del NIE o NIF.
     * @return La letra final del NIE o NIF.
     */
    private static String obtenerLetra(int resultado) {

        String letra;
        switch (resultado) {
            case 0:
                letra = "T";
                break;
            case 1:
                letra = "R";
                break;
            case 2:
                letra = "W";
                break;
            case 3:
                letra = "A";
                break;
            case 4:
                letra = "G";
                break;
            case 5:
                letra = "M";
                break;
            case 6:
                letra = "Y";
                break;
            case 7:
                letra = "F";
                break;
            case 8:
                letra = "P";
                break;
            case 9:
                letra = "D";
                break;
            case 10:
                letra = "X";
                break;
            case 11:
                letra = "B";
                break;
            case 12:
                letra = "N";
                break;
            case 13:
                letra = "J";
                break;
            case 14:
                letra = "Z";
                break;
            case 15:
                letra = "S";
                break;
            case 16:
                letra = "Q";
                break;
            case 17:
                letra = "V";
                break;
            case 18:
                letra = "H";
                break;
            case 19:
                letra = "L";
                break;
            case 20:
                letra = "C";
                break;
            case 21:
                letra = "K";
                break;
            default:
                letra = "E";
                break;
        }
        return letra;
    }
}