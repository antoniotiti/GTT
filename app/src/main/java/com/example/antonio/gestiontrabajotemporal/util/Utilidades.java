package com.example.antonio.gestiontrabajotemporal.util;


import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utilidades {

    //Formato de hora.
    public static SimpleDateFormat formatter = new SimpleDateFormat("kk:mm", new Locale("es", "ES"));
    public static SimpleDateFormat formatter_minutos = new SimpleDateFormat("mm", new Locale("es", "ES"));
    //Formato decimal
    public static DecimalFormat form = new DecimalFormat("0.00");

    public static double redondear2Decimales(Double numero){
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
            e.printStackTrace();
        }

        return horaDecimal;
    }

    /**
     * @param horaInicio1
     * @param horaFin1
     * @param horaInicio2
     * @param horaFin2
     * @param turnoPartido
     * @return
     */
    public static String calcularHorasTrabajadas(EditText horaInicio1, EditText horaFin1, EditText horaInicio2, EditText horaFin2, boolean turnoPartido) {

        String horasTrabajadas;
        String stringHoraInicio1 = horaInicio1.getText().toString();
        String stringHoraFin1 = horaFin1.getText().toString();

        long tiempoTrabajadoMilisegundos1 = calcularDiferenciaMilisegundos(stringHoraInicio1, stringHoraFin1);

        if (turnoPartido) {

            String stringHoraInicio2 = horaInicio2.getText().toString();
            String stringHoraFin2 = horaFin2.getText().toString();

            long tiempoTrabajadoMilisegundos2 = calcularDiferenciaMilisegundos(stringHoraInicio2, stringHoraFin2);
            long tiempoTrabajadoMilisegundosTotal = tiempoTrabajadoMilisegundos1 + tiempoTrabajadoMilisegundos2;

            horasTrabajadas = calcularDiferenciaHoras(tiempoTrabajadoMilisegundosTotal);
        } else {
            horasTrabajadas = calcularDiferenciaHoras(tiempoTrabajadoMilisegundos1);
        }

        return horasTrabajadas;
    }


       /* String stringHoraInicio1, stringHoraFin1;

        Calendar calHoraInicio1 = Calendar.getInstance();
        Calendar calHoraFin1 = Calendar.getInstance();


        stringHoraInicio1 = horaInicio1.getText().toString();
        stringHoraFin1 = horaFin1.getText().toString();

        if (!stringHoraInicio1.equals("") && !stringHoraFin1.equals("")) {
            try {
                calHoraInicio1.setTime(formatter.parse(stringHoraInicio1));
                calHoraFin1.setTime(formatter.parse(stringHoraFin1));

                if (calHoraFin1.after(calHoraInicio1)) {

                    tiempoTrabajado1 = calcularDiferenciaMilisegundos(calHoraInicio1, calHoraFin1);
                } else {
                    calHoraFin1.add(Calendar.HOUR, 24);
                    tiempoTrabajado1 = calcularDiferenciaMilisegundos(calHoraInicio1, calHoraFin1);

                }*/

               /* if (turnoPartido) {

                    String stringHoraInicio2, stringHoraFin2;
                    Calendar calHoraInicio2 = Calendar.getInstance();
                    Calendar calHoraFin2 = Calendar.getInstance();

                    stringHoraInicio2 = horaInicio2.getText().toString();
                    stringHoraFin2 = horaFin2.getText().toString();

                    if (stringHoraInicio2 != "" && stringHoraFin2 != "") {

                        calHoraInicio2.setTime(formatter.parse(stringHoraInicio2));
                        calHoraFin2.setTime(formatter.parse(stringHoraFin2));
                        if (calHoraFin2.before(calHoraInicio2)) {

                            tiempoTrabajado2 = calcularDiferenciaMilisegundos(calHoraInicio2, calHoraFin2);
                        } else {
                            calHoraFin2.add(Calendar.HOUR, 24);
                            tiempoTrabajado2 = calcularDiferenciaMilisegundos(calHoraInicio2, calHoraFin2);

                        }

                        tiempoTrabajadoTotal = tiempoTrabajado1 + tiempoTrabajado2;

                        horasTrabajadas = calcularDiferenciaHoras(tiempoTrabajadoTotal);
                    }


                } else {

                    horasTrabajadas = calcularDiferenciaHoras(tiempoTrabajado1);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
       // }

        return horasTrabajadas;
    }*/

    /**
     * @param horaInicio
     * @param horaFin
     * @return
     */
    public static long calcularDiferenciaMilisegundos(String horaInicio, String horaFin) {


        long tiempoTrabajado = 0;

        Calendar calHoraInicio = Calendar.getInstance();
        Calendar calHoraFin = Calendar.getInstance();

        if (!horaInicio.equals("") && !horaFin.equals("")) {
            try {
                calHoraInicio.setTime(formatter.parse(horaInicio));
                calHoraFin.setTime(formatter.parse(horaFin));

                if (calHoraFin.after(calHoraInicio)) {

                    tiempoTrabajado = (calHoraFin.getTimeInMillis() - calHoraInicio.getTimeInMillis());
                } else {
                    calHoraFin.add(Calendar.HOUR, 24);
                    tiempoTrabajado = (calHoraFin.getTimeInMillis() - calHoraInicio.getTimeInMillis());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return tiempoTrabajado;
    }

    /*public static long calcularDiferenciaMilisegundos(Calendar horaInicio, Calendar horaFin) {
        long diferencia = ((horaFin.getTimeInMillis() - horaInicio.getTimeInMillis()));
        return (diferencia);
    }*/

    public static String calcularDiferenciaHoras(long tiempoTrabajado) {


        long segsMilli = 1000;
        long minsMilli = segsMilli * 60;//60000
        long horasMilli = minsMilli * 60;//3600000
        long diasMilli = horasMilli * 24;//86400000

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

    /*public static String calcularHorasNocturnas(EditText horaInicio1, EditText horaFin1, EditText horaInicio2, EditText horaFin2, boolean turnoPartido) {

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
                calHoraInicio1.setTime(formatter.parse(stringHoraInicio1));
                calHoraFin1.setTime(formatter.parse(stringHoraFin1));
                calHoraFin1.add(Calendar.HOUR, 24);

                calHoraInicioNocturna.setTime(formatter.parse("21:59"));
                calHoraFinNocturna.setTime(formatter.parse("06:01"));
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

    /**
     * @param horaInicio1
     * @param horaFin1
     * @param horaInicio2
     * @param horaFin2
     * @param turnoPartido
     * @return
     */
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
            calHoraInicioNocturna.setTime(formatter.parse(horaInicioNocturna));
            calHoraFinNocturna.setTime(formatter.parse(horaFinNocturna));
            // calHoraFinNocturna.add(Calendar.HOUR, 24);

            if (!stringHoraInicio1.equals("") && !stringHoraFin1.equals("")) {
                try {
                    calHoraInicio.setTime(formatter.parse(stringHoraInicio1));
                    calHoraFin.setTime(formatter.parse(stringHoraFin1));
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

                        /*long milisegundosNoNocturnos1 = calcularDiferenciaMilisegundos(stringHoraInicio1, horaInicioNocturna);
                        long milisegundosNoNocturnos2 = calcularDiferenciaMilisegundos(horaFinNocturna, stringHoraFin1);*/

                        long milisegundosNoNocturnos1 = calcularDiferenciaMilisegundos(horaInicioNocturna, stringHoraFin1);


                        tiempoTrabajadoNONocturno = milisegundosTrabajados - milisegundosNoNocturnos1;
                        if (tiempoTrabajadoNONocturno <= 0) {
                            tiempoTrabajadoNONocturno = 0;

                        }

                    }

                    /*if(calHoraFin.after(calHoraFinNocturna)){
                        tiempoTrabajadoNONocturno2 = (calHoraFin.getTimeInMillis() - calHoraFinNocturna.getTimeInMillis());

                    }*/

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
    }
}
