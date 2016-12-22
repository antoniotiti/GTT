package com.example.antonio.gestiontrabajotemporal.util;


import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validar {

    private static final int MAX_LENGHT_TELEFONO = 9;
    private static final int MAX_LENGHT_CODIGO_OPERARIO = 4;
    private static final String INICIO_CODIGO_OPERARIO = "8";


    /**
     * Método que se encarga de comprobar que el código de operario introducido sea correcto.
     * Debe ser un número de 4 cifras que comience por 8.
     *
     * @param eTCodigoOperario Código del Operario introducido por el usuario a validar.
     * @return Si el código de operario introducido es correcto o no lo es.
     */
    public static boolean validarCodigoOperario(EditText eTCodigoOperario) {

        String codigoOperario = eTCodigoOperario.getText().toString();

        boolean validado = false;
        try {
            if (codigoOperario.length() == MAX_LENGHT_CODIGO_OPERARIO && codigoOperario.startsWith(INICIO_CODIGO_OPERARIO)) {
                Integer.parseInt(codigoOperario);
                validado = true;
                eTCodigoOperario.setError(null);
            } else {
                eTCodigoOperario.setError("Código de operario no válido");
            }
        } catch (NumberFormatException e) {
            eTCodigoOperario.setError("Código de operario no válido");
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
    public static boolean validarPassword(EditText eTPassword) {

        String password = eTPassword.getText().toString();
        boolean validado = false;

        Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        Matcher m = p.matcher(password);
        if ((m.matches())) {
            validado = true;
            eTPassword.setError(null);
        } else {
            eTPassword.setError("Password no válido");
        }
        return validado;
    }

    /**
     * @param eTTexto
     * @return
     */
    public static boolean validarEditTextVacio(EditText eTTexto) {

        String texto = eTTexto.getText().toString();
        boolean validado = false;

        if (!texto.isEmpty()){
            validado=true;
            eTTexto.setError(null);
        } else{
            eTTexto.setError("Campo vacío");
        }
        return validado;
    }


    /**
     * Método que se encarga de comprobar que la fecha introducida por el
     * usuario sea correcta y en el siguiente formato: dd-MM-yyyy.
     *
     * @param eTTfecha Fecha introducida por el usuario.
     * @return Si la fecha es correcta o no lo es.
     */
    public static boolean validarFecha(EditText eTTfecha) {

        String fecha = eTTfecha.getText().toString();

        boolean validado = false;
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        formateador.setLenient(false);
        try {
            //TODO que coja el año bien
            Date fechacorrecta = formateador.parse(fecha);
            validado = true;
            eTTfecha.setError(null);
        } catch (ParseException e) {
            eTTfecha.setError("Formato fecha correcto dd/MM/yyyy");
        }
        return validado;
    }

    /**
     *
     * @param eTNSS
     * @return
     */
    public static boolean validarNSS(EditText eTNSS) {

        String nSS = eTNSS.getText().toString();

        long resultado, controlObtenido;
        boolean validado = false;
        if (nSS.length()==12) {

            int provincia = Integer.valueOf(nSS.substring(0,2));
            int numero = Integer.valueOf(nSS.substring(2,10));
            int control = Integer.valueOf(nSS.substring(10));

            if (numero < 10000000) {
                resultado= (numero+provincia)*10000000;
            } else {
                resultado= Long.valueOf(nSS.substring(0,10));
            }
            controlObtenido = (resultado % 97);

            if (control ==controlObtenido) {
                validado = true;
                eTNSS.setError(null);
            } else {
                eTNSS.setError("EL NSS no es válido.");
            }
        } else {
            eTNSS.setError("El NSS debe tener 12 dígitos");
        }
        return validado;
    }


    /**
     * Método que se encarga de comprobar que el número de teléfono introducido
     * por el usuario sea correcto.
     *
     * @param eTTelefono Número de teléfono introducido por el usuario.
     * @return Si el número de teléfono es correcto o no lo es.
     */
    public static boolean validarTelefono(EditText eTTelefono) {

        String telefono = eTTelefono.getText().toString();

        boolean validado = false;
        if ((telefono.length() == MAX_LENGHT_TELEFONO) && ((telefono.charAt(0) == '6')
                || (telefono.charAt(0) == '7') || (telefono.charAt(0) == '8')
                || (telefono.charAt(0) == '9'))) {
            validado = true;
            eTTelefono.setError(null);
        } else {
            eTTelefono.setError("El teléfono introducido no es válido.");
        }
        return validado;
    }

    /**
     * Método que se encarga de comprobar que el email introducido por el usuario sea correcto.
     * @param eTEmail Email introducido por el usuario.
     * @returnSi el email es correcto o no
     */
    public static boolean validarEmail(EditText eTEmail) {

        String email = eTEmail.getText().toString();

        boolean validado = false;
        Pattern p = android.util.Patterns.EMAIL_ADDRESS;
        Matcher m = p.matcher(email);
        if ((m.matches())) {
            validado = true;
            eTEmail.setError(null);
        } else {
            eTEmail.setError("Email no válido");
        }
        return validado;
    }

    /**
     * Método que se encarga de validar el NIE o NIF introducido por el
     * usuario.
     *
     * @param eTNieONif NIE o NIF introducido por el usuario.
     * @return Si el NIE o NIF es correcto o no lo es.
     */
    public static boolean validarNieONif(EditText eTNieONif) {

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
                eTNieONif.setError("La letra del NIE o NIF no es válida.");
                Log.d("Error", "Error. La letra del NIE o NIF no es válida.");
            }
        } else {
            eTNieONif.setError("El NIE o NIF no es válido.");
            Log.d("Error", "Error. El NIE o NIF no es válido..");
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
