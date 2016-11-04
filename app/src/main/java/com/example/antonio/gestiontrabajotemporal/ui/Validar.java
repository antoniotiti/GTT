package com.example.antonio.gestiontrabajotemporal.ui;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validar {

    private static final int MAX_LENGHT_TELEFONO = 9;


    /**
     * Método que se encarga de comprobar que el código de operario introducido sea correcto.
     * Debe ser un número de 4 cifras que comience por 8.
     *
     * @param eTCodigoOperario Código del Operario introducido por el usuario a validar.
     * @return Si el código de operario introducido es correcto o no lo es.
     */
    public static boolean validarCodigoOperario(Context context, EditText eTCodigoOperario) {

        String codigoOperario= eTCodigoOperario.getText().toString();

        boolean validado = false;
        try {
            if (codigoOperario.length() == 4 && codigoOperario.startsWith("8")) {
                Integer.parseInt(codigoOperario);
                validado = true;
                eTCodigoOperario.setError(null);
                //eTCodigoOperario.setBackgroundColor(Color.WHITE);
            } else {
                //eTCodigoOperario.requestFocus();
                eTCodigoOperario.setError("Código de operario no válido");
                //Toast.makeText(context, "Código de operario no válido", Toast.LENGTH_LONG).show();
                Log.d("Error", "Error. El Código de Operario debe tener " + 4 + " dígitos, comenzando por 8.");
            }
        } catch (NumberFormatException e) {
            Log.d("Error", "Error. Debe introducir el Código de Operario compuesto por cuatro dígitos.");
        }
        return validado;
    }


    /**
     * Método que se encarga de comprobar que el password introducido por el usuario sea correcto.
     * El password debe tener como mínimo 8 caracteres, debe incluir mayúsculas y minúsculas,
     * al menos un número y no debe contener espacios en blanco.
     *
     * @param eTPassword Password introducido por el usuario a validar.
     * @return Si el passwordintroducido es correcto o no lo es.
     */
    public static boolean validarPassword(Context context, EditText eTPassword) {

        String password= eTPassword.getText().toString();
        boolean correcto = false;

        Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        Matcher m = p.matcher(password);
        if ((m.matches())) {
            correcto = true;
            eTPassword.setError(null);
            //eTPassword.setBackgroundColor(Color.WHITE);
        }else{
            //eTPassword.requestFocus();
            eTPassword.setError("Password no válido");
            //eTPassword.setBackgroundColor(Color.RED);
            //Toast.makeText(context, "Password no válido", Toast.LENGTH_LONG).show();
            Log.d("Error","Error, password no válido");
        }
        return correcto;
    }

    /**
     * Método que se encarga de comprobar que el nombre del titular de la cuenta
     * sea correcto, (que no sea null o que no este vacio).
     *
     * @param nombreTitularCuenta El nombre del titular de la cuenta
     * @return Si el nombre del titular de la cuenta es corrcto o no.
     */
    public static boolean validarDatosCliente(String nombreTitularCuenta) {
        return ((nombreTitularCuenta != null) && (!nombreTitularCuenta.equals("")));
    }

    /**
     * Método que se encarga de comprobar que la fecha introducida por el
     * usuario sea correcta y en el siguiente formato: dd-MM-yyyy.
     *
     * @param fecha Fecha introducida por el usuario.
     * @return Si la fecha es correcta o no lo es.
     */
    public static boolean validarFecha(String fecha) {

        boolean validado = false;
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        formateador.setLenient(false);
        try {
            Date fechacorrecta = formateador.parse(fecha);
            validado = true;
        } catch (ParseException e) {
            // ES.msgErrln("La Fecha introducida no es válida. Debe introducirla en este formato: dd-MM-yyyy");
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el número de teléfono introducido
     * por el usuario sea correcto.
     *
     * @param telefono Número de teléfono introducido por el usuario.
     * @return Si el número de teléfono es correcto o no lo es.
     */
    public static boolean validarTelefono(String telefono) {

        boolean validado = false;
        if ((telefono.length() == MAX_LENGHT_TELEFONO) && ((telefono.charAt(0) == '6')
                || (telefono.charAt(0) == '7') || (telefono.charAt(0) == '8')
                || (telefono.charAt(0) == '9'))) {
            validado = true;
        } else {
            //ES.msgErrln("El teléfono introducido no es válido.");
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el NIE o NIF introducido por el
     * usuario sea correcto.
     *
     * @param nieONif NIE o NIF introducido por el usuario.
     * @return Si el NIE o NIFes correcto o no lo es.
     */
    public static boolean validarNieONif(String nieONif) {

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
                // ES.msgErrln("La letra del NIE o NIF no es válida.");
            }
        } else {
            //ES.msgErrln("El NIE o NIF no es válido.");
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
        boolean correcto = false;
        Pattern p = Pattern.compile("([0-9]{8}[a-zA-Z])|([XxYyZz][0-9]{7}[a-zA-Z])");
        Matcher m = p.matcher(nieONif);
        if ((nieONif.length() == 9) && (m.matches())) {
            correcto = true;
        }
        return correcto;
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
