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

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.form;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.redondear2Decimales;

public class NominaDetalle extends AppCompatActivity {

    OperacionesBaseDatos datos;
    String calendario, operario, primerDiaMes, ultimoDiaMes;
    double totalNeto, pref_RetencionIrpf, pref_RetencionHorasExtras,
            pref_RetencionContingenciasComunes, pref_RetencionFormacionDesempleoAccd;;
    int numeroDiasTrabajados;

    ArrayList<NominaTurno> nominaTurnos = new ArrayList<>();


    private NominaCursorAdapter mNominaAdapter;
    TextView textViewNombreApellidoOperario, textViewFechaNacimientoOperario, textViewDireccionOperario, textViewDniOperario,
            textViewNSSOperario, textViewTelefonoOperario, textViewEmailOperario, textViewFechaAntiguedadOperario, textViewTotalNeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomina_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.
        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(this);

        //Obtenemos las preferencias.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        pref_RetencionIrpf = Double.parseDouble(sharedPref.getString("pref_retencion_irpf", "0"));
        pref_RetencionHorasExtras = Double.parseDouble(sharedPref.getString("pref_retencion_extras", "0"));
        pref_RetencionContingenciasComunes = 4.7;//Double.parseDouble(sharedPref.getString("pref_retencion_contingencias_comunes", ""));pref_RetencionFormacionDesempleoAccd
        pref_RetencionFormacionDesempleoAccd = 1.7;//Double.parseDouble(sharedPref.getString("pref_retencion_formacion_desempleo_accd", ""));

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

        //Recibimos el código de usuario y password de la página de logeo.
        Bundle bundle = getIntent().getExtras();

        calendario = getIntent().getExtras().getString("calendario");
        operario = getIntent().getExtras().getString("operario");
        primerDiaMes = getIntent().getExtras().getString("primerDiaMes");
        ultimoDiaMes = getIntent().getExtras().getString("ultimoDiaMes");

        calcularDetalleNomina();

        mNominaAdapter = new NominaCursorAdapter(this, nominaTurnos);

        /*LayoutInflater inflater = getLayoutInflater();

        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.list_view_footer, mTurnosList, false);

        //TextView totalNeto = (TextView) footer.findViewById(R.id.textView_total_neto_valor);

        mTurnosList.addFooterView(footer, null, false);*/
        // Setup
        mTurnosList.setAdapter(mNominaAdapter);


    }

    private void calcularDetalleNomina() {
        totalNeto=0;

        Cursor cursorDatosNomina = datos.obtenerDatosNominaDetalle(calendario, operario, primerDiaMes, ultimoDiaMes);

        if (cursorDatosNomina.moveToFirst()) {

            numeroDiasTrabajados = cursorDatosNomina.getCount();
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

                double baseIrpf = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas + totalEurosHorasTrabajadasExtras;//+indemnizacion
                double baseHorasExtras = totalEurosHorasTrabajadasExtras;
                double baseContingenciasComunes = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas;
                double baseFormacionDesempleoAccd = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas + totalEurosHorasTrabajadasExtras;

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
            textViewTotalNeto.setText(form.format(totalNeto) + "€");

        } else {
            Toast.makeText(getApplicationContext(), "No hay dias trabajados en el mes", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método al que se llama cuando se utiliza el botón de retroceso de la SupportActionBar.
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
