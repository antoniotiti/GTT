package com.example.antonio.gestiontrabajotemporal.util;

import com.example.antonio.gestiontrabajotemporal.AbstractUtilityBaseTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilidadesTest extends AbstractUtilityBaseTester<Utilidades> {

    @Test
    public void redondear2Decimales() throws Exception {
        double resultadoReal = Utilidades.redondear2Decimales(2.5333);
        double resultadoEsperado = 2.53;
        assertEquals(resultadoEsperado, resultadoReal, 0);
    }

    @Test
    public void redondear2Decimales2() throws Exception {//Debe Fallar
        double resultadoReal = Utilidades.redondear2Decimales(2.5533);
        double resultadoEsperado = 2.53;
        assertEquals(resultadoEsperado, resultadoReal, 0);
    }

    @Test
    public void redondear2Decimales3() throws Exception {
        double resultadoReal = Utilidades.redondear2Decimales(2.5363);
        double resultadoEsperado = 2.54;
        assertEquals(resultadoEsperado, resultadoReal, 0);
    }

    @Test
    public void redondear2Decimales4() throws Exception {
        double resultadoReal = Utilidades.redondear2Decimales(2.5323);
        double resultadoEsperado = 2.53;
        assertEquals(resultadoEsperado, resultadoReal, 0);
    }

    @Test
    public void redondear2Decimales5() throws Exception {
        double resultadoReal = Utilidades.redondear2Decimales(0d);
        double resultadoEsperado = 0;
        assertEquals(resultadoEsperado, resultadoReal, 0);
    }

    @Test
    public void calcularHoraDecimal() throws Exception {
        double resultadoReal = Utilidades.calcularHoraDecimal("09:20");
        double resultadoEsperado = 9.333333015441895;
        assertEquals(resultadoEsperado, resultadoReal, 0);
    }

    @Test
    public void calcularHoraDecimal2() throws Exception {
        double resultadoReal = Utilidades.calcularHoraDecimal("09:20");
        double resultadoEsperado = 9.33;
        assertEquals(resultadoEsperado, resultadoReal, 0.01);
    }

    @Test
    public void calcularHoraDecimal3() throws Exception {//Debe Fallar
        double resultadoReal = Utilidades.calcularHoraDecimal("09:45");
        double resultadoEsperado = 9.33;
        assertEquals(resultadoEsperado, resultadoReal, 0.01);
    }

    @Test
    public void calcularHoraFormateada() throws Exception {
        String resultadoReal = Utilidades.calcularHoraFormateada(9.75f);
        String resultadoEsperado = "09:45";
        assertEquals(resultadoEsperado, resultadoReal);
    }

    @Test
    public void calcularHoraFormateada2() throws Exception {//Debe Fallar
        String resultadoReal = Utilidades.calcularHoraFormateada(9.75f);
        String resultadoEsperado = "09:44";
        assertEquals(resultadoEsperado, resultadoReal);
    }
}