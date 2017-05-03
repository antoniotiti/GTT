package com.example.antonio.gestiontrabajotemporal.nominadetalle;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.FORMATO_DECIMAL;

public class NominaDetalle extends AppCompatActivity {

    OperacionesBaseDatos datos;
    String calendario, operario, primerDiaMes, ultimoDiaMes;
    double totalNeto, pref_RetencionIrpf, pref_RetencionHorasExtras,
            pref_RetencionContingenciasComunes, pref_RetencionFormacionDesempleoAccd;
    int numeroDiasTrabajados;

    ArrayList<NominaTurno> nominaTurnos = new ArrayList<>();
    TextView textViewNombreApellidoOperario, textViewFechaNacimientoOperario, textViewDireccionOperario, textViewDniOperario,
            textViewNSSOperario, textViewTelefonoOperario, textViewEmailOperario, textViewFechaAntiguedadOperario, textViewTotalNeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomina_detalle);
        setToolbar(); //Añadir la Toolbar.

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(this);

        //Obtenemos las preferencias.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        pref_RetencionIrpf = Double.parseDouble(sharedPref.getString("pref_retencion_irpf", "0"));
        pref_RetencionHorasExtras = Double.parseDouble(sharedPref.getString("pref_retencion_extras", "0"));
        pref_RetencionContingenciasComunes = Double.parseDouble(sharedPref.getString("pref_retencion_contingencias_comunes", "4.7"));
        pref_RetencionFormacionDesempleoAccd = Double.parseDouble(sharedPref.getString("pref_retencion_formacion_desempleo_accd", "1.7"));

        // Referencias UI
        ListView mTurnosList = (ListView) findViewById(R.id.listview_nomina);
        textViewNombreApellidoOperario = (TextView) findViewById(R.id.textView_nombre_apellido_operario);
        textViewFechaNacimientoOperario = (TextView) findViewById(R.id.textView_fecha_nacimiento_operario);
        textViewDniOperario = (TextView) findViewById(R.id.textView_dni_operario);
        textViewNSSOperario = (TextView) findViewById(R.id.textView_nss_operario);
        textViewDireccionOperario = (TextView) findViewById(R.id.textView_direccion_operario);
        textViewEmailOperario = (TextView) findViewById(R.id.textView_email_operario);
        textViewTelefonoOperario = (TextView) findViewById(R.id.textView_telefono_operario);
        textViewFechaAntiguedadOperario = (TextView) findViewById(R.id.textView_fecha_antiguedad_operario);
        textViewTotalNeto = (TextView) findViewById(R.id.textView_total_neto_valor);

        //Recibimos datos desde la pantalla de calendario.
        Bundle bundle = getIntent().getExtras();
        calendario = bundle.getString("calendario");
        operario = bundle.getString("operario");
        primerDiaMes = bundle.getString("primerDiaMes");
        ultimoDiaMes = bundle.getString("ultimoDiaMes");

        calcularDetalleNomina();
        //Creamos el adaptador para la lista.
        NominaCursorAdapter mNominaAdapter = new NominaCursorAdapter(this, nominaTurnos);
        // Setup
        if (mTurnosList != null) {
            mTurnosList.setAdapter(mNominaAdapter);
        }
    }

    /**
     * Método que se encarga de calcular la nómina con detalles.
     */
    private void calcularDetalleNomina() {
        totalNeto = 0;
        Cursor cursorDatosNomina = datos.obtenerDatosNominaDetalle(calendario, operario, primerDiaMes, ultimoDiaMes);
        if (cursorDatosNomina.moveToFirst()) {
            numeroDiasTrabajados = cursorDatosNomina.getCount();//Obtenemos el número de días trabajados
            ArrayList<String> arrayTurnos = new ArrayList<>();
            ArrayList<Double> arrayHorasTrabajadas = new ArrayList<>();
            ArrayList<Double> arrayHorasTrabajadasExtras = new ArrayList<>();
            ArrayList<Double> arrayHorasTrabajadasNocturnas = new ArrayList<>();

            ArrayList<Double> arrayPrecioHorasTrabajadas = new ArrayList<>();
            ArrayList<Double> arrayPrecioHorasTrabajadasExtras = new ArrayList<>();
            ArrayList<Double> arrayPrecioHorasTrabajadasNocturnas = new ArrayList<>();

            String nombreOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.NOMBRE));
            String apellidoOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.APELLIDOS));
            String nombreApellidoOperario = nombreOperario + " " + apellidoOperario;

            String dniOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.DNI));
            String direccionOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.DIRECCION));
            String emailOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.EMAIL));
            String fotoOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.FOTO));
            String numeroSSOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.NUMERO_S_S));
            String telefonoOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.TELEFONO));
            String fechaInicioOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.FECHA_INICIO));
            String fechaNacimientoOperario = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Operarios.FECHA_NACIMIENTO));

            //Recorremos el cursor hasta que no haya más registros
            do {
                String nombreTurno = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.NOMBRE));
                String nombrePuesto = cursorDatosNomina.getString(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));

                double horasTrabajadas = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORAS_TRABAJADAS));
                double horasTrabajadasNocturnas = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORAS_TRABAJADAS_NOCTURNAS));
                double horasTrabajadasExtras = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Fichajes.HORA_EXTRA));
                double precioHoraTrabajada = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA));
                double precioHoraTrabajadaExtra = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA_EXTRA));
                double precioHoraTrabajadaNocturna = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA_NOCTURNAS));

                if (!arrayTurnos.contains(nombreTurno)) {
                    arrayTurnos.add(nombreTurno);

                    arrayHorasTrabajadas.add(horasTrabajadas);
                    arrayHorasTrabajadasExtras.add(horasTrabajadasExtras);
                    arrayHorasTrabajadasNocturnas.add(horasTrabajadasNocturnas);

                    arrayPrecioHorasTrabajadas.add(precioHoraTrabajada);
                    arrayPrecioHorasTrabajadasExtras.add(precioHoraTrabajadaExtra);
                    arrayPrecioHorasTrabajadasNocturnas.add(precioHoraTrabajadaNocturna);

                } else {
                    int a = arrayTurnos.indexOf(nombreTurno);
                    double horasTrabajadasTurno = arrayHorasTrabajadas.get(a) + horasTrabajadas;
                    double horasTrabajadasExtrasTurno = arrayHorasTrabajadasExtras.get(a) + horasTrabajadasExtras;
                    double horasTrabajadasNocturnasTurno = arrayHorasTrabajadasNocturnas.get(a) + horasTrabajadasNocturnas;

                    arrayHorasTrabajadas.set(a, horasTrabajadasTurno);
                    arrayHorasTrabajadasExtras.set(a, horasTrabajadasExtrasTurno);
                    arrayHorasTrabajadasNocturnas.set(a, horasTrabajadasNocturnasTurno);
                }

            } while (cursorDatosNomina.moveToNext());

            for (int i = 0; i < arrayTurnos.size(); i++) {

                double totalEurosHorasTrabajadas = (arrayHorasTrabajadas.get(i) - arrayHorasTrabajadasNocturnas.get(i)) * arrayPrecioHorasTrabajadas.get(i);
                double totalEurosHorasTrabajadasNocturnas = arrayHorasTrabajadasNocturnas.get(i) * arrayPrecioHorasTrabajadasNocturnas.get(i);
                double totalEurosHorasTrabajadasExtras = arrayHorasTrabajadasExtras.get(i) * arrayPrecioHorasTrabajadasExtras.get(i);

                //Calculamos las bases necesarias para la nómina
                double baseIrpf = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas + totalEurosHorasTrabajadasExtras;//+indemnizacion
                double baseHorasExtras = totalEurosHorasTrabajadasExtras;
                double baseContingenciasComunes = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas;
                double baseFormacionDesempleoAccd = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas + totalEurosHorasTrabajadasExtras;

                //Calculamos las retenciones
                double retencionIrpf = baseIrpf * pref_RetencionIrpf / 100;
                double retencionHorasExtras = baseHorasExtras * pref_RetencionHorasExtras / 100;
                double retencionContingenciasComunes = baseContingenciasComunes * pref_RetencionContingenciasComunes / 100;
                double retencionFormacionDesempleoAccd = baseFormacionDesempleoAccd * pref_RetencionFormacionDesempleoAccd / 100;

                totalNeto += totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas + totalEurosHorasTrabajadasExtras - retencionIrpf - retencionHorasExtras - retencionContingenciasComunes - retencionFormacionDesempleoAccd;

                NominaTurno nomina = new NominaTurno(arrayTurnos.get(i), arrayHorasTrabajadas.get(i), arrayHorasTrabajadasNocturnas.get(i), arrayHorasTrabajadasExtras.get(i),
                        arrayPrecioHorasTrabajadas.get(i), arrayPrecioHorasTrabajadasExtras.get(i), arrayPrecioHorasTrabajadasNocturnas.get(i), totalEurosHorasTrabajadas,
                        totalEurosHorasTrabajadasNocturnas, totalEurosHorasTrabajadasExtras);
                nominaTurnos.add(nomina);
            }

            textViewNombreApellidoOperario.setText(nombreApellidoOperario);
            textViewFechaNacimientoOperario.setText(fechaNacimientoOperario);
            textViewDniOperario.setText(dniOperario);
            textViewNSSOperario.setText(numeroSSOperario);
            textViewDireccionOperario.setText(direccionOperario);
            textViewTelefonoOperario.setText(telefonoOperario);
            textViewEmailOperario.setText(emailOperario);
            textViewFechaAntiguedadOperario.setText(fechaInicioOperario);
            textViewTotalNeto.setText(String.format("%s€", FORMATO_DECIMAL.format(totalNeto)));
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_hay_dias_trabajados), Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
}