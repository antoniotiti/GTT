package com.example.antonio.gestiontrabajotemporal.util;

import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public final class Utilidades {

    public static final String NOMBRE_BASE_DATOS = "Fichajes.db"; //Nombre de la BBDD

    public static final String FORMATO_FECHA_DATETIME = "YYYY-MM-DD"; //Formato fecha para Datetime.
    public static final DecimalFormat FORMATO_DECIMAL = new DecimalFormat("0.00"); //Formato decimal
    private static final String FORMATO_FECHA = "yyyy-MM-dd"; //Formato fecha.
    private static final String FORMATO_HORA = "kk:mm"; //Formato hora.
    private static final String FORMATO_MINUTOS = "mm"; //Formato hora.
    //Formateadores
    public static SimpleDateFormat formatter_hora_minutos = new SimpleDateFormat(FORMATO_HORA, new Locale("es", "ES"));
    public static SimpleDateFormat formatter_minutos = new SimpleDateFormat(FORMATO_MINUTOS, new Locale("es", "ES"));
    public static SimpleDateFormat formatter_fecha = new SimpleDateFormat(FORMATO_FECHA, new Locale("es", "ES"));

    private Utilidades() {
        throw new UnsupportedOperationException("Not instanciable class!");
    }

    /**
     * Método que se encarga de redondear al alza un número decimal a 2 decimales.
     *
     * @param numero Número decimal a redondear.
     * @return Número redondeado a 2 decimales.
     */
    public static double redondear2Decimales(Double numero) {
        return Math.rint(numero * 100) / 100;
    }

    /**
     * Metodo que se encargar de pasar una hora pasada por parámetros en String en formato kk:mm, a decimal.
     *
     * @param hora Hora que se quiere pasar a formato decimal.
     * @return La hora en formato decimal.
     */
    public static float calcularHoraDecimal(String hora) {
        float horaDecimal = 0;
        try {
            String[] parts = hora.split(":");
            int horas = Integer.parseInt(parts[0]);
            int minutos = Integer.parseInt(parts[1]);
            horaDecimal = (float) (horas * 60 + minutos) / 60;//Multiplicas las horas por 60 y le sumas los minutos, para obtener los minutos totales. Lo dividimos por 60 para calcular las horas con decimales.
        } catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return horaDecimal;
    }

    /**
     * Metodo que se encargar de pasar una hora pasada por parámetros en decimal a formato kk:mm.
     *
     * @param hora Hora que se quiere formatear.
     * @return La hora formateada.
     */
    public static String calcularHoraFormateada(float hora) {
        String horaDecimal;
        int hours = (int) hora;
        int minutes = (int) (hora * 60) % 60;
        horaDecimal = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
        return horaDecimal;
    }

    /**
     * Método que se encarga de calcular el número de horas trabajadas en total.
     *
     * @param horaInicio1  Hora de inicio
     * @param horaFin1     Hora fin
     * @param horaInicio2  Hora inicio turno partido
     * @param horaFin2     Hora fin turno partido
     * @param turnoPartido Si el turno es partido o no
     * @return El número de horas trabajadas
     */
    public static String calcularHorasTrabajadas(EditText horaInicio1, EditText horaFin1, EditText horaInicio2, EditText horaFin2, boolean turnoPartido) {
        String horasTrabajadas;
        String stringHoraInicio1 = horaInicio1.getText().toString();
        String stringHoraFin1 = horaFin1.getText().toString();
        long tiempoTrabajadoMilisegundos = calcularDiferenciaMilisegundos(stringHoraInicio1, stringHoraFin1);
        if (turnoPartido) {
            String stringHoraInicio2 = horaInicio2.getText().toString();
            String stringHoraFin2 = horaFin2.getText().toString();

            long tiempoTrabajadoMilisegundosTurnoPartido = calcularDiferenciaMilisegundos(stringHoraInicio2, stringHoraFin2);
            long tiempoTrabajadoMilisegundosTotal = tiempoTrabajadoMilisegundos + tiempoTrabajadoMilisegundosTurnoPartido;

            horasTrabajadas = calcularDiferenciaHoras(tiempoTrabajadoMilisegundosTotal);
        } else {
            horasTrabajadas = calcularDiferenciaHoras(tiempoTrabajadoMilisegundos);
        }
        return horasTrabajadas;
    }

    /**
     * Método que se encarga de calcular el número de horas que hay en milisegundos entre la
     * hora fin y la de inicio.
     *
     * @param horaInicio Hora de inicio
     * @param horaFin    Hora fin
     * @return Horas trabajadas en milisegundos.
     */
    private static long calcularDiferenciaMilisegundos(String horaInicio, String horaFin) {
        long tiempoTrabajado = 0;

        Calendar calHoraInicio = Calendar.getInstance();
        Calendar calHoraFin = Calendar.getInstance();

        if (!horaInicio.equals("") && !horaFin.equals("")) {
            try {
                calHoraInicio.setTime(formatter_hora_minutos.parse(horaInicio));
                calHoraFin.setTime(formatter_hora_minutos.parse(horaFin));
                if (calHoraFin.after(calHoraInicio)) {
                    tiempoTrabajado = (calHoraFin.getTimeInMillis() - calHoraInicio.getTimeInMillis());
                } else {
                    calHoraFin.add(Calendar.HOUR, 24); //Sumamos 24 horas ya que la hora de fin es del dia siguiente.
                    tiempoTrabajado = (calHoraFin.getTimeInMillis() - calHoraInicio.getTimeInMillis());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return tiempoTrabajado;
    }

    /**
     * Método que se encarga de calcular las horas trabajadas pasadas en milisegundos al formato utilizado.
     *
     * @param tiempoTrabajado Horas trabajadas en milisegundos
     * @return Horas trabajadas formatedas
     */
    private static String calcularDiferenciaHoras(long tiempoTrabajado) {

        long segsMilli = 1000; //Un segundo en milisegundos, 1000 milisegundos
        long minsMilli = segsMilli * 60;//Un minuto en milisegundos, 60000 milisegundos
        long horasMilli = minsMilli * 60;//Una horas en milisegundos, 3600000 milisegundos
        long diasMilli = horasMilli * 24;// Un dia en milisegundos, 86400000 milisegundos

        long diasTranscurridos = tiempoTrabajado / diasMilli;
        tiempoTrabajado = tiempoTrabajado % diasMilli;

        long horasTranscurridos = tiempoTrabajado / horasMilli;
        tiempoTrabajado = tiempoTrabajado % horasMilli;

        long minutosTranscurridos = tiempoTrabajado / minsMilli;
        tiempoTrabajado = tiempoTrabajado % minsMilli;

        long segsTranscurridos = tiempoTrabajado / segsMilli;

        String sHoras = String.valueOf(horasTranscurridos).length() == 1 ? ("0" + horasTranscurridos) : ("" + horasTranscurridos);
        String sMinutos = String.valueOf(minutosTranscurridos).length() == 1 ? ("0" + minutosTranscurridos) : ("" + minutosTranscurridos);

        return (sHoras + ":" + sMinutos);
    }
}



/*TODO calcular horas nocturnas automáticamente
  public static String calcularHorasNocturnas(EditText horaInicio1, EditText horaFin1, EditText horaInicio2, EditText horaFin2, boolean turnoPartido) {

        String horasTrabajadas;
        String stringHoraInicio1 = horaInicio1.getText().toString();
        String stringHoraFin1 = horaFin1.getText().toString();

        String horasNocturnas = "00:00";
        String horaInicioNocturna = "22:00";
        String horaFinNocturna = "06:00";
        long tiempoTrabajadoNONocturno = 0;
        long tiempoTrabajadoNONocturno2 = 0;
        long horasNocturnasMilisegundos;

        long tiempoTrabajadoMilisegundos1;
        Calendar calHoraInicioNocturna = Calendar.getInstance();
        Calendar calHoraFinNocturna = Calendar.getInstance();
        Calendar calHoraInicio = Calendar.getInstance();
        Calendar calHoraFin = Calendar.getInstance();
        try {
            calHoraInicioNocturna.setTime(formatter_hora_minutos.parse(horaInicioNocturna));
            calHoraFinNocturna.setTime(formatter_hora_minutos.parse(horaFinNocturna));
            // calHoraFinNocturna.add(Calendar.HOUR, 24);

            if (!stringHoraInicio1.equals("") && !stringHoraFin1.equals("")) {
                try {
                    calHoraInicio.setTime(formatter_hora_minutos.parse(stringHoraInicio1));
                    calHoraFin.setTime(formatter_hora_minutos.parse(stringHoraFin1));
                    long milisegundosTrabajados;
                    if (calHoraFin.after(calHoraInicio)) {

                        milisegundosTrabajados = calcularDiferenciaMilisegundos(stringHoraInicio1, stringHoraFin1);
                    } else {
                        calHoraFin.add(Calendar.HOUR, 24);
                        milisegundosTrabajados = calcularDiferenciaMilisegundos(stringHoraInicio1, stringHoraFin1);
                    }


                    if ((calHoraInicio.after(calHoraInicioNocturna) || (calHoraInicio.compareTo(calHoraInicioNocturna) == 0)) && (calHoraFin.before(calHoraFinNocturna) || calHoraFin.compareTo(calHoraFinNocturna) == 0)) {

                        tiempoTrabajadoNONocturno = calcularDiferenciaMilisegundos(stringHoraInicio1, stringHoraFin1);
                    } else if ((calHoraInicio.after(calHoraInicioNocturna) || (calHoraInicio.compareTo(calHoraInicioNocturna) == 0)) && calHoraFin.after(calHoraFinNocturna)) {

                        long milisegundosNoNocturnos = calcularDiferenciaMilisegundos(horaFinNocturna, stringHoraFin1);

                        tiempoTrabajadoNONocturno = milisegundosTrabajados - milisegundosNoNocturnos;

                    } else if (calHoraInicio.before(calHoraInicioNocturna) && (calHoraFin.before(calHoraFinNocturna) || calHoraFin.compareTo(calHoraFinNocturna) == 0)) {

                        long milisegundosNoNocturnos = calcularDiferenciaMilisegundos(stringHoraInicio1, horaInicioNocturna);

                        tiempoTrabajadoNONocturno = milisegundosTrabajados - milisegundosNoNocturnos;

                    } else if (calHoraInicio.before(calHoraInicioNocturna) && (calHoraFin.after(calHoraFinNocturna))) {

                        *//*long milisegundosNoNocturnos1 = calcularDiferenciaMilisegundos(stringHoraInicio1, horaInicioNocturna);
                        long milisegundosNoNocturnos2 = calcularDiferenciaMilisegundos(horaFinNocturna, stringHoraFin1);*//*

                        long milisegundosNoNocturnos1 = calcularDiferenciaMilisegundos(horaInicioNocturna, stringHoraFin1);


                        tiempoTrabajadoNONocturno = milisegundosTrabajados - milisegundosNoNocturnos1;
                        if (tiempoTrabajadoNONocturno <= 0) {
                            tiempoTrabajadoNONocturno = 0;
                        }
                    }

                    *//*if(calHoraFin.after(calHoraFinNocturna)){
                        tiempoTrabajadoNONocturno2 = (calHoraFin.getTimeInMillis() - calHoraFinNocturna.getTimeInMillis());

                    }*//*

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //  tiempoTrabajadoMilisegundos1 = calcularDiferenciaMilisegundos(stringHoraInicio1, stringHoraFin1);
            // horasNocturnasMilisegundos= tiempoTrabajadoMilisegundos1-tiempoTrabajadoNONocturno-tiempoTrabajadoNONocturno2;
            horasNocturnas = calcularDiferenciaHoras(tiempoTrabajadoNONocturno);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return horasNocturnas;
    }*/

/*TODO calcular horas nocturnas automáticamente
    public static String calcularHorasNocturnas(EditText horaInicio1, EditText horaFin1, EditText horaInicio2, EditText horaFin2, boolean turnoPartido) {

        String horasTrabajadas = "Error";
        long tiempoTrabajado1, tiempoTrabajado2, tiempoTrabajadoTotal;

        String stringHoraInicio1, stringHoraFin1;
        int horasNocturnas = 0;

        Calendar calHoraInicio1 = Calendar.getInstance();
        Calendar calHoraFin1 = Calendar.getInstance();
        Calendar calHoraInicioNocturna = Calendar.getInstance();
        Calendar calHoraFinNocturna = Calendar.getInstance();

        Calendar calHoraInicioPrueba = Calendar.getInstance();
        stringHoraInicio1 = horaInicio1.getText().toString();
        stringHoraFin1 = horaFin1.getText().toString();

        if (!stringHoraInicio1.equals("") && !stringHoraFin1.equals("")) {
            try {
                calHoraInicio1.setTime(formatter_hora_minutos.parse(stringHoraInicio1));
                calHoraFin1.setTime(formatter_hora_minutos.parse(stringHoraFin1));
                calHoraFin1.add(Calendar.HOUR, 24);

                calHoraInicioNocturna.setTime(formatter_hora_minutos.parse("21:59"));
                calHoraFinNocturna.setTime(formatter_hora_minutos.parse("06:01"));
                calHoraFinNocturna.add(Calendar.HOUR, 24);

                calHoraInicioPrueba = calHoraInicio1;
                while (calHoraInicioPrueba.after(calHoraInicioNocturna) && calHoraInicioPrueba.before(calHoraFinNocturna) && calHoraInicioPrueba.before(calHoraFin1)) {

                    calHoraInicioPrueba.add(Calendar.HOUR, 1);
                    horasNocturnas++;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(horasNocturnas);
    }*/