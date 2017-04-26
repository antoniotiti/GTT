package com.example.antonio.gestiontrabajotemporal.pantallacalendario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.calendarios.CalendariosActivity;
import com.example.antonio.gestiontrabajotemporal.fichajedetalle.FichajeDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.modelo.Fichaje;
import com.example.antonio.gestiontrabajotemporal.nominadetalle.NominaDetalle;
import com.example.antonio.gestiontrabajotemporal.puestos.PuestosActivity;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.turnos.TurnosActivity;
import com.example.antonio.gestiontrabajotemporal.ui.SettingsActivity;
import com.example.antonio.gestiontrabajotemporal.util.DialogoSeleccionCalendario;
import com.example.antonio.gestiontrabajotemporal.util.DialogoSeleccionPuesto;
import com.example.antonio.gestiontrabajotemporal.util.DialogoSeleccionTurno;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import hirondelle.date4j.DateTime;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.form;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.formatter_fecha;
import static com.example.antonio.gestiontrabajotemporal.util.Validar.FORMATO_FECHA_DATETIME;


@SuppressLint("SimpleDateFormat")
public class PantallaCalendarioActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener, DialogoSeleccionTurno.OnItemClickListener, DialogoSeleccionPuesto.OnItemClickListener, DialogoSeleccionCalendario.OnItemClickListener {

    public static final int REQUEST_UPDATE_DELETE_FICHAJE = 2;
    public static final String EXTRA_CALENDARIO_ID = "extra_calendario_id";
    public static final String EXTRA_FECHA = "extra_fecha";
    public static final String EXTRA_OPERARIO_ID = "extra_operario_id";



    double totalHorasTrabajadas, totalHorasTrabajadasExtras, totalHorasTrabajadasNocturnas, totalEurosHorasTrabajadas,
            totalEurosHorasTrabajadasNocturnas, totalEurosHorasTrabajadasExtras, totalNeto, pref_RetencionIrpf, pref_RetencionHorasExtras,
            pref_RetencionContingenciasComunes, pref_RetencionFormacionDesempleoAccd;
    int numeroDiasTrabajados;
    String primerDiaMes, ultimoDiaMes;
    OperacionesBaseDatos datos;
    // Spinner calendarioSpinner;
    // SimpleCursorAdapter calendarioSpinnerAdapter;
    TextView calendarioSeleccionado, puestoSeleccionado, horasTrabajadas, diasTrabajados, nominaValor;
    RelativeLayout layoutNomina;
    String codigoOperario, password;
    String idCalendario = "";
    String idPuesto = "";
    String fechaSeleccionada = "";

    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_calendario);
        setToolbar();// Añadir la Toolbar
        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(this);

        layoutNomina = (RelativeLayout) findViewById(R.id.layout_nomina);
        horasTrabajadas = (TextView) findViewById(R.id.textView_horas_trabajadas_valor);
        diasTrabajados = (TextView) findViewById(R.id.textView_dias_trabajados_valor);
        calendarioSeleccionado = (TextView) findViewById(R.id.textView_calendario_seleccionado);
        puestoSeleccionado = (TextView) findViewById(R.id.textView_seleccion_puesto);
        nominaValor = (TextView) findViewById(R.id.textView_nomina_valor);

        //TODO al hacer clik mandar a seleecion calendario de preferencias

        //calendarioSpinner = (Spinner) findViewById(R.id.spinner_calendarios);

        //Recibimos el código de usuario y password de la página de logeo.
        Bundle bundle = getIntent().getExtras();
        codigoOperario = getIntent().getExtras().getString("codigoOperario");
        password = getIntent().getExtras().getString("password");

        //TODO Configuración de valores predeterminados
        PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Obtenemos las preferencias.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pref_operario_activo", codigoOperario);
        editor.apply();

        boolean modoTemaOscuro = sharedPref.getBoolean("pref_switch_modo_tema_oscuro", false);
        String calendarioPredeterminado = sharedPref.getString("pref_calendario_predeterminado", "");
        String puestoPredeterminado = sharedPref.getString("pref_puesto_predeterminado", "");
        String nombreDiaComienzoSemana = sharedPref.getString("pref_dia_comienzo_semana", "");
        pref_RetencionIrpf = Double.parseDouble(sharedPref.getString("pref_retencion_irpf", "0"));
        pref_RetencionHorasExtras = Double.parseDouble(sharedPref.getString("pref_retencion_extras", "0"));
        pref_RetencionContingenciasComunes = 4.7;//Double.parseDouble(sharedPref.getString("pref_retencion_contingencias_comunes", ""));pref_RetencionFormacionDesempleoAccd
        pref_RetencionFormacionDesempleoAccd = 1.7;//Double.parseDouble(sharedPref.getString("pref_retencion_formacion_desempleo_accd", ""));


        // Setup caldroid fragment
        caldroidFragment = new PantallaCalendarioFragment();
        // Attach to the activity
       /* FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.calendar1, caldroidFragment);
        t.commit();*/

        // Setup arguments
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);//Habilitar pasar de mes desde el calendario sin utilizar las flechas
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);//Mostrar siempre 6 semanas en el calendario
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, true);//Habilitar desabilitar las fechas para pasar de mes

        int numeroDiaComienzoSemana; //Weekday conventions Caldroid

        switch (nombreDiaComienzoSemana) {
            case "MONDAY":
                numeroDiaComienzoSemana = CaldroidFragment.MONDAY;
                break;
            case "TUESDAY":
                numeroDiaComienzoSemana = CaldroidFragment.TUESDAY;
                break;
            case "WEDNESDAY":
                numeroDiaComienzoSemana = CaldroidFragment.WEDNESDAY;
                break;
            case "THURSDAY":
                numeroDiaComienzoSemana = CaldroidFragment.THURSDAY;
                break;
            case "FRIDAY":
                numeroDiaComienzoSemana = CaldroidFragment.FRIDAY;
                break;
            case "SATURDAY":
                numeroDiaComienzoSemana = CaldroidFragment.SATURDAY;
                break;
            case "SUNDAY":
                numeroDiaComienzoSemana = CaldroidFragment.SUNDAY;
                break;
            default:
                numeroDiaComienzoSemana = CaldroidFragment.MONDAY;
        }
        // Día que comienza la semana.
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, numeroDiaComienzoSemana);

        // Activar tema oscuro
        if (modoTemaOscuro) {
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        }

        // Calendario predeterminado.
        args.putString("pref_calendario_predeterminado", calendarioPredeterminado);

        // Puesto predeterminado.
        args.putString("pref_puesto_predeterminado", puestoPredeterminado);


        //args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);//Habilita o desabilita hacer click en las fechas desabilitadas.
        //args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false); // Activar desactivar modo compacto.

        //Establecemos las configuraciones de las preferencias
        if (caldroidFragment.getArguments() == null) {
            caldroidFragment.setArguments(args);
        } else {
            //Consider explicitly clearing arguments here
            caldroidFragment.getArguments().putAll(args);
        }

        if (!calendarioPredeterminado.equals("")) {
            calendarioSeleccionado.setText(calendarioPredeterminado);
            Cursor cursorIdCalendario = datos.obtenerIdCalendarioByNombre(calendarioPredeterminado);
            //Nos aseguramos de que existe al menos un registro
            if (cursorIdCalendario.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    idCalendario = cursorIdCalendario.getString(0);
                    //obtenerFichajesParaCalendario();
                } while (cursorIdCalendario.moveToNext());
            }
        }

        if (!puestoPredeterminado.equals("")) {
            puestoSeleccionado.setText(puestoPredeterminado);
            Cursor cursorIdPuesto = datos.obtenerIdPuestoByNombre(puestoPredeterminado);
            //Nos aseguramos de que existe al menos un registro
            if (cursorIdPuesto.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    idPuesto = cursorIdPuesto.getString(0);
                    //obtenerFichajesParaCalendario();
                } while (cursorIdPuesto.moveToNext());
            }
        }

        Map<String, Object> extraData = caldroidFragment.getExtraData();

        extraData.put("OPERARIO", codigoOperario);
        extraData.put("CALENDARIO", idCalendario);
        extraData.put("PUESTO", idPuesto);
        // Refresh view
        caldroidFragment.refreshView();

        //  setCustomResourceForDates();//*TODO????

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                Cursor cursorFichajeFecha = datos.obtenerFichajeFecha(codigoOperario, formatter_fecha.format(date), idCalendario);

                if (cursorFichajeFecha.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        Intent intent = new Intent(getApplicationContext(), FichajeDetalleActivity.class);

                        intent.putExtra(EXTRA_CALENDARIO_ID, idCalendario);
                        intent.putExtra(EXTRA_FECHA, formatter_fecha.format(date));
                        intent.putExtra(EXTRA_OPERARIO_ID, codigoOperario);

                        startActivityForResult(intent, REQUEST_UPDATE_DELETE_FICHAJE);
                    } while (cursorFichajeFecha.moveToNext());
                } else {

                    if (idCalendario != "") {
                        if (idPuesto != "") {
                            // Mostramos un diálogo de selección de turnos
                            DialogoSeleccionTurno dialogo = new DialogoSeleccionTurno();
                            dialogo.show(getSupportFragmentManager(), "SeleccionarTurno");
                            fechaSeleccionada = formatter_fecha.format(date);
                        } else {
                            Toast.makeText(getApplicationContext(), "Debe seleccionar un Puesto antes de realizar el Fichaje", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Debe seleccionar un Calendario antes de realizar el Fichaje", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {

                DateTime firstDateOfMonth = new DateTime(year, month, 1, 0, 0, 0, 0);
                DateTime lastDateOfMonth = firstDateOfMonth.plusDays(firstDateOfMonth
                        .getNumDaysInMonth() - 1);

                primerDiaMes = firstDateOfMonth.format(FORMATO_FECHA_DATETIME);
                ultimoDiaMes = lastDateOfMonth.format(FORMATO_FECHA_DATETIME);

                calcularNomina();
            }


            @Override
            public void onLongClickDate(Date date, View view) {

                Cursor cursorFichajeFecha = datos.obtenerFichajeFecha(codigoOperario, formatter_fecha.format(date), idCalendario);

                if (cursorFichajeFecha.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        Bundle args = new Bundle();
                        args.putString("fecha", formatter_fecha.format(date));
                        SimpleDialog dialogo = new SimpleDialog();

                        dialogo.setArguments(args);
                        dialogo.show(getSupportFragmentManager(), "EliminarFichaje");
                    } while (cursorFichajeFecha.moveToNext());
                } else {
                    Toast.makeText(getApplicationContext(), "No hay fichajes para: " + formatter_fecha.format(date),
                            Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    //Toast.makeText(getApplicationContext(), "Caldroid view is created", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        puestoSeleccionado.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoSeleccionPuesto dialogo = new DialogoSeleccionPuesto();
                dialogo.show(getSupportFragmentManager(), "SeleccionarPuesto");
            }
        });

        layoutNomina.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NominaDetalle.class);
                i.putExtra("calendario", idCalendario);
                i.putExtra("operario", codigoOperario);
                i.putExtra("primerDiaMes", primerDiaMes);
                i.putExtra("ultimoDiaMes", ultimoDiaMes);
                startActivity(i);
            }
        });


//TODO seleccion de calendarios
        /*calendarioSeleccionado.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogoSeleccionCalendario dialogo = new DialogoSeleccionCalendario();
                dialogo.show(getSupportFragmentManager(), "SeleccionarCalendario");

            }
        });*/




       /* Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);

        final Bundle state = savedInstanceState;
        showDialogButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Setup caldroid to use as dialog
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(listener);

                // If activity is recovered from rotation
                final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                if (state != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getSupportFragmentManager(), state,
                            "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    // Setup arguments
                    Bundle bundle = new Bundle();
                    // Setup dialogTitle
                    dialogCaldroidFragment.setArguments(bundle);
                }

                dialogCaldroidFragment.show(getSupportFragmentManager(),
                        dialogTag);
            }
        });*/

    }

    private void calcularNomina() {

        totalNeto = 0;
        totalHorasTrabajadas = 0;
        totalHorasTrabajadasNocturnas = 0;
        totalHorasTrabajadasExtras = 0;
        totalEurosHorasTrabajadas = 0;
        totalEurosHorasTrabajadasNocturnas = 0;
        totalEurosHorasTrabajadasExtras = 0;
        numeroDiasTrabajados = 0;

        Cursor cursorDatosNomina = datos.obtenerDatosNomina(idCalendario, codigoOperario, primerDiaMes, ultimoDiaMes);

        if (cursorDatosNomina.moveToFirst()) {
            numeroDiasTrabajados = cursorDatosNomina.getCount();
            //Recorremos el cursor hasta que no haya más registros
            do {
                double horasTrabajadas = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORAS_TRABAJADAS));
                double horasTrabajadasNocturnas = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORAS_TRABAJADAS_NOCTURNAS));
                double horasTrabajadasExtras = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Fichajes.HORA_EXTRA));
                double precioHoraTrabajada = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA));
                double precioHoraTrabajadaExtra = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA_EXTRA));
                double precioHoraTrabajadaNocturna = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA_NOCTURNAS));

                totalHorasTrabajadas += horasTrabajadas;
                totalHorasTrabajadasNocturnas += horasTrabajadasNocturnas;
                totalHorasTrabajadasExtras += horasTrabajadasExtras;

                totalEurosHorasTrabajadas += (horasTrabajadas - horasTrabajadasNocturnas) * precioHoraTrabajada;
                totalEurosHorasTrabajadasNocturnas += horasTrabajadasNocturnas * precioHoraTrabajadaNocturna;
                totalEurosHorasTrabajadasExtras += horasTrabajadasExtras * precioHoraTrabajadaExtra;

            } while (cursorDatosNomina.moveToNext());

            double baseIrpf = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas + totalEurosHorasTrabajadasExtras;//+indemnizacion
            double baseHorasExtras = totalEurosHorasTrabajadasExtras;
            double baseContingenciasComunes = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas;
            double baseFormacionDesempleoAccd = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas + totalEurosHorasTrabajadasExtras;

            double retencionIrpf = baseIrpf * pref_RetencionIrpf / 100;
            double retencionHorasExtras = baseHorasExtras * pref_RetencionHorasExtras / 100;
            double retencionContingenciasComunes = baseContingenciasComunes * pref_RetencionContingenciasComunes / 100;
            double retencionFormacionDesempleoAccd = baseFormacionDesempleoAccd * pref_RetencionFormacionDesempleoAccd / 100;

            totalNeto = totalEurosHorasTrabajadas + totalEurosHorasTrabajadasNocturnas + totalEurosHorasTrabajadasExtras - retencionIrpf - retencionHorasExtras - retencionContingenciasComunes - retencionFormacionDesempleoAccd;

            diasTrabajados.setText(String.valueOf(numeroDiasTrabajados));
            horasTrabajadas.setText(form.format(totalHorasTrabajadas));
            nominaValor.setText(form.format(totalNeto) + "€");
        } else {
            Toast.makeText(getApplicationContext(), "No hay días trabajados en el mes", Toast.LENGTH_SHORT).show();
        }
    }



    /*private void obtenerFichajes() {
        ObtenerFichajes obtenerFichajesAsyncTask = new ObtenerFichajes();

        obtenerFichajesAsyncTask.delegate = this;
        obtenerFichajesAsyncTask.execute(idCalendario);
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        //cargarCalendarios();

    }

    /**
     *
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pantalla_calendario);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }


    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.crear_turno:
                startActivity(new Intent(this, TurnosActivity.class));
                return true;
            case R.id.crear_puesto:
                startActivity(new Intent(this, PuestosActivity.class));
                return true;
            case R.id.crear_calendario:
                startActivity(new Intent(this, CalendariosActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.contenedorRegistro), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * Método encargado de lanzar la tarea en segundo plano para cargar los calendarios.
     */
    /*private void cargarCalendarios() {
        new CalendariosLoadTask().execute();
        calendarioSpinner.setOnItemSelectedListener(this);
    }*/
    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String currentCalendarioNombre = (String) parent.getSelectedItem();

        Toast.makeText(getApplicationContext(), currentCalendarioNombre, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/

    /**
     * TODO Fechas minimas y maximas en color(eliminar)
     */
    /*private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();
        Date today=cal.getTime();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(Color.BLUE);
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            ColorDrawable yellow = new ColorDrawable(Color.YELLOW);

            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setBackgroundDrawableForDate(yellow, today);

            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, today);
        }
    }*/
 /*   @Override
    public void processFinish(Cursor cursorFichajesRecibidos) {

        //Nos aseguramos de que existe al menos un registro
        if (cursorFichajesRecibidos.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String idOperario = cursorFichajesRecibidos.getString(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Operarios.ID));
                String fechaFichaje = cursorFichajesRecibidos.getString(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Fichajes.FECHA));
                double horasExtras = cursorFichajesRecibidos.getDouble(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Fichajes.HORA_EXTRA));
                String idTurno = cursorFichajesRecibidos.getString(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Fichajes.ID_TURNO));
                String idPuesto = cursorFichajesRecibidos.getString(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Fichajes.ID_PUESTO));
                String nombreTurno = cursorFichajesRecibidos.getString(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Turnos.NOMBRE));
                String abreviaturnaNombreTurno = cursorFichajesRecibidos.getString(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));
                int colorFondoTurno = cursorFichajesRecibidos.getInt(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_FONDO));
                int colorTextoTurno = cursorFichajesRecibidos.getInt(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_TEXTO));
                String calendario = cursorFichajesRecibidos.getString(cursorFichajesRecibidos.getColumnIndex(NombresColumnasBaseDatos.Calendarios.ID));

                //TODO pintar el calendario(no coge el ultimo)

                if (caldroidFragment != null) {

                    Date dateObj;
                    try {
                        dateObj = formatter_hora_minutos.parse(fechaFichaje);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dateObj);

                   *//* int[] mColors = getResources().getIntArray(R.array.colores_dialogo);

                    int color = getResources().getInteger(colorTextoTurno);*//*


                        ColorDrawable colorFondo = new ColorDrawable(colorFondoTurno);
                        ColorDrawable colorTexto = new ColorDrawable(colorTextoTurno);
                        colorTexto.getColor();
                        int color = R.color.white;

                        // To set the extraData:TODO
                    *//*Map<String, Object> extraData = caldroidFragment.getExtraData();
                    extraData.put("ABREVIATURA", abreviaturnaNombreTurno);*//*


                        caldroidFragment.setBackgroundDrawableForDate(colorFondo, calendar.getTime());
                        caldroidFragment.setTextColorForDate(R.color.red, calendar.getTime());//TODO no encuentra el color pasado int
                        // Refresh view
                        // caldroidFragment.refreshView();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            } while (cursorFichajesRecibidos.moveToNext());

          *//*  // To set the extraData:TODO
            Map<String, Object> extraData = caldroidFragment.getExtraData();
                    extraData.put("CURSOR", cursorFichajesRecibidos);*//*

            Map<String, Object> extraData = caldroidFragment.getExtraData();

            extraData.put("OPERAIO", codigoOperario);
            extraData.put("CALENDARIO", idCalendario);
            extraData.put("PUESTO", idPuesto);
            // Refresh view
            caldroidFragment.refreshView();

            // Attach to the activity
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar1, caldroidFragment);
            //t.addToBackStack(null);//Volver al fragment anterior
            t.commit();
        }

    }*/
    @Override
    public void onPossitiveButtonClick(String tag, String fecha) {
        switch (tag) {
            case "ModificarFichaje":
                Toast.makeText(getApplicationContext(), "ModificarFichaje" + fecha,
                        Toast.LENGTH_SHORT).show();
                break;
            case "EliminarFichaje":
                ColorDrawable colorFondo = new ColorDrawable(Color.WHITE);
                Date date = null;
                try {
                    date = formatter_fecha.parse(fecha);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (datos.eliminarFichaje(codigoOperario, fecha, idCalendario)) {
                    Toast.makeText(getApplicationContext(), "Fichaje eliminado: " + fecha, Toast.LENGTH_SHORT).show();


                    caldroidFragment.clearBackgroundDrawableForDate(date);
                    caldroidFragment.clearTextColorForDate(date);

                    //TODO no lo borra directamente
                    caldroidFragment.setBackgroundDrawableForDate(colorFondo, date);

                    caldroidFragment.refreshView();

                } else {
                    Toast.makeText(getApplicationContext(), "No Fichaje eliminado: " + fecha, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        calcularNomina();
    }

    @Override
    public void onNegativeButtonClick(String tag, String fecha) {

        switch (tag) {
            case "ModificarFichaje":
                Toast.makeText(getApplicationContext(), "No ModificarFichaje" + fecha,
                        Toast.LENGTH_SHORT).show();
                break;
            case "EliminarFichaje":
                Toast.makeText(getApplicationContext(), "No EliminarFichaje" + fecha,
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onItemClick(String currentId, String tag) {

        switch (tag) {

            case "turno":
                //TODO Comentario
                long insertado = datos.insertarFichaje(new Fichaje(codigoOperario, fechaSeleccionada, currentId, idPuesto, idCalendario, null, null));//PU-7144a470-a5cc-4cd9-afe2-c48797f59229
                caldroidFragment.refreshView();//emu airbag PU-b8eade0b-50aa-4450-b450-4ead1668f250, emu cpd PU-d459da85-7f31-4f61-8c7e-36485d465be9
                calcularNomina();
                //calcularNomina();
                break;

            case "puesto":
                idPuesto = currentId;
                String nombrePuesto = "";

                Cursor cursorIdPuesto = datos.obtenerPuestoById(idPuesto);

                //Nos aseguramos de que existe al menos un registro
                if (cursorIdPuesto.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        nombrePuesto = cursorIdPuesto.getString(cursorIdPuesto.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));

                    } while (cursorIdPuesto.moveToNext());
                }
                puestoSeleccionado.setText(nombrePuesto);
                break;

            case "calendario":
                idCalendario = currentId;
                String nombreCalendario = "";

                Cursor cursorIdCalendario = datos.obtenerCalendarioById(idCalendario);

                //Nos aseguramos de que existe al menos un registro
                if (cursorIdCalendario.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        nombreCalendario = cursorIdCalendario.getString(cursorIdCalendario.getColumnIndex(NombresColumnasBaseDatos.Calendarios.NOMBRE));
                        calendarioSeleccionado.setText(nombreCalendario);
                        caldroidFragment.refreshView();
                    } while (cursorIdCalendario.moveToNext());
                }

                break;
        }
    }


    /**
     * Clase asíncrona encargada de cargar los Fichajes de la base de datos.
     */
    private class ObtenerFichajes extends AsyncTask<String, Void, Cursor> {

        public AsyncResponse delegate = null;

        @Override
        protected Cursor doInBackground(String... voids) {
            String idCalendario = voids[0];

            Cursor cursorFichajes = null;

            try {
                cursorFichajes = datos.obtenerFichajesParaCalendario(codigoOperario, idCalendario);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error encountered.",
                        Toast.LENGTH_LONG).show();
            }
            return cursorFichajes;
        }


        @Override
        protected void onPostExecute(Cursor cursor) {

            if (cursor.moveToFirst()) {
                delegate.processFinish(cursor);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No hay Fichajes.", Toast.LENGTH_SHORT).show();
            }
        }


    }

}