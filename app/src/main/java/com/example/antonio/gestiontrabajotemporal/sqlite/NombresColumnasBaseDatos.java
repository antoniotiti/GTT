package com.example.antonio.gestiontrabajotemporal.sqlite;

import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class NombresColumnasBaseDatos {

    interface ColumnasOperario {
        String ID = "idOperario";
        String DNI = "dni";
        String NOMBRE = "nombre";
        String APELLIDOS = "apellidos";
        String FOTO = "foto";
        String DIRECCION = "direccion";
        String FECHA_NACIMIENTO = "fechaNacimiento";
        String TELEFONO = "telefono";
        String EMAIL = "email";
        String FECHA_INICIO = "fechaInicio";
        String NUMERO_S_S = "numeroSS";
        String PASSWORD = "password";
    }

    interface ColumnasPuesto {
        String ID = "idPuesto";
        String NOMBRE = "nombrePuesto";
    }

    interface ColumnasTurno {

        String ID = "idTurno";
        String NOMBRE = "nombreTurno";
        String ABREVIATURA_NOMBRE_TURNO  = "abreviaturaNombreTurno";
        String HORA_INICIO_1 = "horaInicio1";
        String HORA_FIN_1 = "horaFin1";
        String TURNO_PARTIDO = "turnoPartido";
        String HORA_INICIO_2 = "horaInicio2";
        String HORA_FIN_2 = "horaFin2";
        String HORAS_TRABAJADAS = "horasTrabajadas";
        String HORAS_TRABAJADAS_NOCTURNAS = "horasTrabajadasNocturnas";
        String PRECIO_HORA = "precioHora";
        String PRECIO_HORA_NOCTURNAS = "precioHoraNocturnas";
        String PRECIO_HORA_EXTRA = "precioHoraExtra";
        String AVISO = "aviso";
        String AVISO_DIA_ANTES = "avisoDiaAntes";
        String HORA_AVISO = "horaAviso";
        String MODO_TELEFONO = "modoTelefono";
        String COLOR_FONDO = "colorFondo";
        String COLOR_TEXTO = "colorTexto";
    }

    interface ColumnasFichaje {

        String ID_OPERARIO= "idOperario";
        String FECHA= "fecha";
        String ID_TURNO= "idTurno";
        String ID_PUESTO ="idPuesto";
        String ID_CALENDARIO= "idCalendario";
        String HORA_EXTRA= "horaExtra";
    }


    interface ColumnasCalendario {
        String ID = "idCalendario";
        String NOMBRE = "nombreCalendario";
    }


    public static class Operarios implements ColumnasOperario {
        /*public static String generarIdOperario() {
            return "OP-" + UUID.randomUUID().toString();
        }*/
    }

    public static class Puestos implements ColumnasPuesto {
        public static String generarIdPuesto() {
            return "PU-" + UUID.randomUUID().toString();
        }
    }

    public static class Turnos implements ColumnasTurno {
        public static String generarIdTurno() {
            return "TU-" + UUID.randomUUID().toString();
        }
    }

    public static class Fichajes implements ColumnasFichaje {
        public static String generarIdFichaje() {
            return "FI-" + UUID.randomUUID().toString();
        }
    }

    public static class Calendarios implements ColumnasCalendario{
        public static String generarIdCalendario() {
            return "CA-" + UUID.randomUUID().toString();
        }
    }

    private NombresColumnasBaseDatos() {
    }

}
