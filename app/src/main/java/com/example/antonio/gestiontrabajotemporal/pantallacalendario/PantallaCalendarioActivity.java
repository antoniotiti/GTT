package com.example.antonio.gestiontrabajotemporal.pantallacalendario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.antonio.gestiontrabajotemporal.calendariodetalle.CalendarioDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.calendarios.CalendariosActivity;
import com.example.antonio.gestiontrabajotemporal.fichajedetalle.FichajeDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.modelo.Fichaje;
import com.example.antonio.gestiontrabajotemporal.nominadetalle.NominaDetalle;
import com.example.antonio.gestiontrabajotemporal.operariodetalle.OperarioDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.puestodetalle.PuestoDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.puestos.PuestosActivity;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.turnodetalle.TurnoDetalleActivity;
import com.example.antonio.gestiontrabajotemporal.turnos.TurnosActivity;
import com.example.antonio.gestiontrabajotemporal.ui.Info;
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

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.FORMATO_DECIMAL;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.FORMATO_FECHA_DATETIME;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.formatter_fecha;


@SuppressLint("SimpleDateFormat")
public class PantallaCalendarioActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener, DialogoSeleccionTurno.OnItemClickListener, DialogoSeleccionPuesto.OnItemClickListener, DialogoSeleccionCalendario.OnItemClickListener {

    public static final int REQUEST_UPDATE_DELETE_FICHAJE = 2;
    public static final String EXTRA_CALENDARIO_ID = "extra_calendario_id";
    public static final String EXTRA_FECHA = "extra_fecha";
    public static final String EXTRA_OPERARIO_ID = "extra_operario_id";

    double totalHorasTrabajadas, totalHorasTrabajadasExtras, totalHorasTrabajadasNocturnas, totalHoras, totalEurosHorasTrabajadas,
            totalEurosHorasTrabajadasNocturnas, totalEurosHorasTrabajadasExtras, totalNeto, pref_RetencionIrpf, pref_RetencionHorasExtras,
            pref_RetencionContingenciasComunes, pref_RetencionFormacionDesempleoAccd;
    int numeroDiasTrabajados;
    String primerDiaMes, ultimoDiaMes;
    OperacionesBaseDatos datos;
    TextView calendarioSeleccionado, puestoSeleccionado, horasTrabajadas, diasTrabajados, nominaValor;
    RelativeLayout layoutNomina;
    String codigoOperario, password;
    String idCalendario = "";
    String idPuesto = "";
    String fechaSeleccionada = "";
    String calendarioPredeterminado, puestoPredeterminado;
    boolean habilitarModoAsistido;
    SharedPreferences sharedPref;

    private CaldroidFragment caldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_calendario);
        setToolbar();// Añadir la Toolbar

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(this);

        //Obtenemos las referencias de las vistas.
        layoutNomina = (RelativeLayout) findViewById(R.id.layout_nomina);
        horasTrabajadas = (TextView) findViewById(R.id.textView_horas_trabajadas_valor);
        diasTrabajados = (TextView) findViewById(R.id.textView_dias_trabajados_valor);
        calendarioSeleccionado = (TextView) findViewById(R.id.textView_calendario_seleccionado);
        puestoSeleccionado = (TextView) findViewById(R.id.textView_seleccion_puesto);
        nominaValor = (TextView) findViewById(R.id.textView_nomina_valor);

        //Recibimos el código de usuario y password de la pantalla de logeo.
        Bundle bundle = getIntent().getExtras();
        codigoOperario = bundle.getString("codigoOperario");
        password = bundle.getString("password");

        //Establecemos los valores por defecto de las preferencias
        PreferenceManager.setDefaultValues(this, R.xml.pref_calendario_puesto, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Obtenemos las preferencias.
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pref_operario_activo", codigoOperario);
        editor.apply();

        boolean modoTemaOscuro = sharedPref.getBoolean("pref_switch_modo_tema_oscuro", false);
        boolean habilitarFlechas = sharedPref.getBoolean("pref_switch_hablitar_flechas", true);
        boolean habilitarCambioMesSinFlechas = sharedPref.getBoolean("pref_switch_hablitar_cambio_mes_sin_flechas", true);
        boolean habilitar6Semanas = sharedPref.getBoolean("pref_switch_hablitar_6_semanas", true);
        habilitarModoAsistido = sharedPref.getBoolean("pref_switch_habilitar_modo_asistido", true);

        calendarioPredeterminado = sharedPref.getString("pref_calendario_predeterminado", "");
        puestoPredeterminado = sharedPref.getString("pref_puesto_predeterminado", "");
        String nombreDiaComienzoSemana = sharedPref.getString("pref_dia_comienzo_semana", "");
        pref_RetencionIrpf = Double.parseDouble(sharedPref.getString("pref_retencion_irpf", "0"));
        pref_RetencionHorasExtras = Double.parseDouble(sharedPref.getString("pref_retencion_extras", "0"));
        pref_RetencionContingenciasComunes = Double.parseDouble(sharedPref.getString("pref_retencion_contingencias_comunes", "0"));
        pref_RetencionFormacionDesempleoAccd = Double.parseDouble(sharedPref.getString("pref_retencion_formacion_desempleo_accd", "0"));

        // Setup caldroid fragment
        caldroidFragment = new PantallaCalendarioFragment();

        // Setup arguments
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));

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
        // Activar/descativar las flechas para cambiar de mes.
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, habilitarFlechas);

        //Habilitar pasar de mes desde el calendario sin utilizar las flechas
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, habilitarCambioMesSinFlechas);

        //Mostrar siempre 6 semanas en el calendario
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, habilitar6Semanas);

        //Habilitar hacer click en las fechas desabilitadas.
        //args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
        // Activar desactivar modo compacto.
        //args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);

        // Calendario predeterminado.
        args.putString("pref_calendario_predeterminado", calendarioPredeterminado);

        // Puesto predeterminado.
        args.putString("pref_puesto_predeterminado", puestoPredeterminado);

        //Comprobamos si esta activo el modo asistido que ayuda al usuario a crea el primer calendario, puesto y turno.
        if (habilitarModoAsistido) {
            SimpleDialog dialogo = new SimpleDialog();
            dialogo.show(getSupportFragmentManager(), "ModoAsistido");
        } else {
            modoNormal();
        }
        //Activar o desactivar modo asistido.
        args.putBoolean("pref_switch_habilitar_modo_asistido", habilitarModoAsistido);

        //Establecemos las configuraciones de las preferencias
        if (caldroidFragment.getArguments() == null) {
            caldroidFragment.setArguments(args);
        } else {
            caldroidFragment.getArguments().putAll(args);
        }

        refrescarView();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            /**
             * Listener al seleccionar un dia del calendario.
             */
            @Override
            public void onSelectDate(Date date, View view) {

                Cursor cursorFichajeFecha = datos.obtenerFichajeFecha(codigoOperario, formatter_fecha.format(date), idCalendario);

                if (cursorFichajeFecha.moveToFirst()) { //Si el día seleccionado contiene un fichaje, mostramos los detalles
                    Intent intent = new Intent(getApplicationContext(), FichajeDetalleActivity.class);
                    intent.putExtra(EXTRA_CALENDARIO_ID, idCalendario);
                    intent.putExtra(EXTRA_FECHA, formatter_fecha.format(date));
                    intent.putExtra(EXTRA_OPERARIO_ID, codigoOperario);
                    startActivityForResult(intent, REQUEST_UPDATE_DELETE_FICHAJE);
                } else {//Si no contiene un fichaje, mostramos un dialogo de selección de turno
                    if (idCalendario != "") {
                        if (idPuesto != "") {
                            // Mostramos un diálogo de selección de turnos
                            DialogoSeleccionTurno dialogo = new DialogoSeleccionTurno();
                            dialogo.show(getSupportFragmentManager(), "SeleccionarTurno");
                            fechaSeleccionada = formatter_fecha.format(date);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.seleccionar_puesto), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.seleccionar_calendario), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            /**
             * Listener al cambiar de mes, calculamos la nómina del mes.
             */
            @Override
            public void onChangeMonth(int month, int year) {

                DateTime firstDateOfMonth = new DateTime(year, month, 1, 0, 0, 0, 0);
                DateTime lastDateOfMonth = firstDateOfMonth.plusDays(firstDateOfMonth
                        .getNumDaysInMonth() - 1);

                primerDiaMes = firstDateOfMonth.format(FORMATO_FECHA_DATETIME);
                ultimoDiaMes = lastDateOfMonth.format(FORMATO_FECHA_DATETIME);
                calcularNomina();
            }

            /**
             * Listener al dejar pulsado un día del calendario. Si el día pulsado contiene un fichaje,
             * mostramos diálogo de advertencia de borrado.
             */
            @Override
            public void onLongClickDate(Date date, View view) {

                Cursor cursorFichajeFecha = datos.obtenerFichajeFecha(codigoOperario, formatter_fecha.format(date), idCalendario);
                if (cursorFichajeFecha.moveToFirst()) {
                    Bundle args = new Bundle();
                    args.putString("fecha", formatter_fecha.format(date));
                    SimpleDialog dialogo = new SimpleDialog();
                    dialogo.setArguments(args);
                    dialogo.show(getSupportFragmentManager(), "EliminarFichaje");
                }
            }

            @Override
            public void onCaldroidViewCreated() {
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
    }

    /**
     * Método que se encarga de comprobar que haya un Calendario y un Puesto creado antes de asignar
     * un Turno.
     */
    private void modoNormal() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("pref_switch_habilitar_modo_asistido", habilitarModoAsistido);
        editor.apply();

        if (!calendarioPredeterminado.equals("")) {
            calendarioSeleccionado.setText(calendarioPredeterminado);
            Cursor cursorIdCalendario = datos.obtenerIdCalendarioByNombre(calendarioPredeterminado);
            //Nos aseguramos de que existe al menos un registro
            if (cursorIdCalendario.moveToFirst()) {
                idCalendario = cursorIdCalendario.getString(0);
            }
        }

        if (!puestoPredeterminado.equals("")) {
            puestoSeleccionado.setText(puestoPredeterminado);
            Cursor cursorIdPuesto = datos.obtenerIdPuestoByNombre(puestoPredeterminado);
            //Nos aseguramos de que existe al menos un registro
            if (cursorIdPuesto.moveToFirst()) {
                idPuesto = cursorIdPuesto.getString(0);
            }
        }
        refrescarView();
    }

    /**
     * Método que se encarga de asistir al usuario en la creación de los datos necesarios en el
     * orden correcto. Calendario - Puesto - Turno.
     */
    private void modoAsistido() {

        if (datos.obtenerCalendarios().moveToFirst()) {//Comprobamos si hay algún Calendario creado en la BBDD.
            if (!calendarioPredeterminado.equals("")) {//Comprobamos si se ha seleccionado el calendario predeterminado de la BBDD.
                calendarioSeleccionado.setText(calendarioPredeterminado);//Establecemos el nombre del calendario
                Cursor cursorIdCalendario = datos.obtenerIdCalendarioByNombre(calendarioPredeterminado);//Obtenemos el id del calendario seleccionado
                //Nos aseguramos de que existe al menos un registro
                if (cursorIdCalendario.moveToFirst()) {
                    idCalendario = cursorIdCalendario.getString(0);
                    if (datos.obtenerPuestos().moveToFirst()) {//Comprobamos si hay algún Puesto creado en la BBDD.
                        if (!puestoPredeterminado.equals("")) {//Comprobamos si se ha seleccionado el puesto predeterminado de la BBDD.
                            puestoSeleccionado.setText(puestoPredeterminado);//Establecemos el nombre del puesto
                            Cursor cursorIdPuesto = datos.obtenerIdPuestoByNombre(puestoPredeterminado);//Obtenemos el id del puesto seleccionado
                            //Nos aseguramos de que existe al menos un registro
                            if (cursorIdPuesto.moveToFirst()) {
                                idPuesto = cursorIdPuesto.getString(0);
                                if (datos.obtenerTurnos().moveToFirst()) {//Comprobamos si hay algún Turno creado en la BBDD.
                                    Toast.makeText(getApplicationContext(), getString(R.string.seleccione_dia_turno), Toast.LENGTH_LONG).show();
                                    habilitarModoAsistido = false;
                                    modoNormal();
                                } else {
                                    SimpleDialog dialogo = new SimpleDialog();
                                    dialogo.show(getSupportFragmentManager(), "CrearTurno");
                                }
                            }
                        } else {
                            SimpleDialog dialogo = new SimpleDialog();
                            dialogo.show(getSupportFragmentManager(), "SeleccionarPuesto");
                        }
                    } else {
                        puestoSeleccionado.setText("");
                        SimpleDialog dialogo = new SimpleDialog();
                        dialogo.show(getSupportFragmentManager(), "CrearPuesto");
                    }
                }
            } else {
                SimpleDialog dialogo = new SimpleDialog();
                dialogo.show(getSupportFragmentManager(), "SeleccionarCalendario");
            }
        } else {
            SimpleDialog dialogo = new SimpleDialog();
            dialogo.show(getSupportFragmentManager(), "CrearCalendario");
        }
    }

    /**
     * Método que se encarga de calcular la nómina.
     */
    private void calcularNomina() {

        totalNeto = 0;
        totalHorasTrabajadas = 0;
        totalHorasTrabajadasNocturnas = 0;
        totalHorasTrabajadasExtras = 0;
        totalHoras = 0;
        totalEurosHorasTrabajadas = 0;
        totalEurosHorasTrabajadasNocturnas = 0;
        totalEurosHorasTrabajadasExtras = 0;
        numeroDiasTrabajados = 0;

        Cursor cursorDatosNomina = datos.obtenerDatosNomina(idCalendario, codigoOperario, primerDiaMes, ultimoDiaMes);

        if (cursorDatosNomina.moveToFirst()) {
            // numeroDiasTrabajados = cursorDatosNomina.getCount();
            //Recorremos el cursor hasta que no haya más registros
            do {
                double horasTrabajadas = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORAS_TRABAJADAS));
                double horasTrabajadasNocturnas = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORAS_TRABAJADAS_NOCTURNAS));
                double horasTrabajadasExtras = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Fichajes.HORA_EXTRA));
                double precioHoraTrabajada = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA));
                double precioHoraTrabajadaExtra = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA_EXTRA));
                double precioHoraTrabajadaNocturna = cursorDatosNomina.getDouble(cursorDatosNomina.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA_NOCTURNAS));

                if (horasTrabajadas > 0 || horasTrabajadasNocturnas > 0 || horasTrabajadasExtras > 0) {//Comprobamos que tenga alguna hora de trabajo, solo mostramos los turnos que tengan horas de trabajo.
                    numeroDiasTrabajados++;
                    totalHorasTrabajadas += horasTrabajadas;
                    totalHorasTrabajadasNocturnas += horasTrabajadasNocturnas;
                    totalHorasTrabajadasExtras += horasTrabajadasExtras;

                    totalEurosHorasTrabajadas += (horasTrabajadas - horasTrabajadasNocturnas) * precioHoraTrabajada;
                    totalEurosHorasTrabajadasNocturnas += horasTrabajadasNocturnas * precioHoraTrabajadaNocturna;
                    totalEurosHorasTrabajadasExtras += horasTrabajadasExtras * precioHoraTrabajadaExtra;
                }
            } while (cursorDatosNomina.moveToNext());

            totalHoras = totalHorasTrabajadas+totalHorasTrabajadasExtras+totalHorasTrabajadasNocturnas;

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
            horasTrabajadas.setText(FORMATO_DECIMAL.format(totalHoras));
            nominaValor.setText(String.format("%s€", FORMATO_DECIMAL.format(totalNeto)));
        } else {
            diasTrabajados.setText("0");
            horasTrabajadas.setText("0");
            nominaValor.setText("0€");
        }
    }

    /**
     * Método que se encarga de refrescar el Fragment
     */
    private void refrescarView() {

        Map<String, Object> extraData = caldroidFragment.getExtraData();

        extraData.put("OPERARIO", codigoOperario);
        extraData.put("CALENDARIO", idCalendario);
        extraData.put("PUESTO", idPuesto);

        // Refresh view
        caldroidFragment.refreshView();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.layout_calendario, caldroidFragment);
        t.commit();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pantalla_calendario);
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
        }
        setSupportActionBar(toolbar);
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }

    /**
     * Creamos el ménu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Ejecutamos una acción al seleccionar una opcion del menú.
     *
     * @param item Opcion del menu seleccionada
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ver_operario:
                startActivity(new Intent(this, OperarioDetalleActivity.class).putExtra(EXTRA_OPERARIO_ID, codigoOperario));
                return true;
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
            case R.id.action_info:
                startActivity(new Intent(this, Info.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Ejecutamos una acción, al aceptar en el diálogo de confirmación, según la opcción elegida.
     *
     * @param tag   Opcion elegida
     * @param fecha Fecha selecionada
     */
    @Override
    public void onPossitiveButtonClick(String tag, String fecha) {
        switch (tag) {
            case "ModificarFichaje":
                Toast.makeText(getApplicationContext(), getString(R.string.fichaje_modificado) + fecha, Toast.LENGTH_SHORT).show();
                break;
            case "EliminarFichaje":
                Date date = null;
                try {
                    date = formatter_fecha.parse(fecha);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (datos.eliminarFichaje(codigoOperario, fecha, idCalendario)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.fichaje_eliminado) + fecha, Toast.LENGTH_SHORT).show();
                    caldroidFragment.clearBackgroundDrawableForDate(date);
                    caldroidFragment.clearTextColorForDate(date);
                    caldroidFragment.refreshView();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fichaje_no_eliminado) + fecha, Toast.LENGTH_SHORT).show();
                }
                break;
            case "CrearCalendario":
                startActivity(new Intent(this, CalendarioDetalleActivity.class));
                break;
            case "CrearPuesto":
                startActivity(new Intent(this, PuestoDetalleActivity.class));
                break;
            case "CrearTurno":
                startActivity(new Intent(this, TurnoDetalleActivity.class));
                break;
            case "SeleccionarCalendario":
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case "SeleccionarPuesto":
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case "ModoAsistido":
                modoAsistido();
                break;
        }
        calcularNomina();
    }

    /**
     * Mostramos un mensaje al cancelar en el diálogo de confirmación, según la opcción elegida.
     *
     * @param tag   Opcion elegida
     * @param fecha Fecha selecionada
     */
    @Override
    public void onNegativeButtonClick(String tag, String fecha) {

        switch (tag) {
            case "ModificarFichaje":
                Toast.makeText(getApplicationContext(), getString(R.string.fichaje_no_modificado) + fecha,
                        Toast.LENGTH_SHORT).show();
                break;
            case "EliminarFichaje":
                Toast.makeText(getApplicationContext(), getString(R.string.fichaje_no_eliminado) + fecha,
                        Toast.LENGTH_SHORT).show();
                break;
            case "ModoAsistido":
                habilitarModoAsistido = false;
                modoNormal();
                break;
        }
    }

    /**
     * Ejecutamos una acción, según el item seleccionado del diálogo.
     *
     * @param tag       Opcion elegida
     * @param currentId Id del item seleccionado en el diálogo.
     */
    @Override
    public void onItemClick(String currentId, String tag) {
        switch (tag) {
            case "turno":
                long insertado = datos.insertarFichaje(new Fichaje(codigoOperario, fechaSeleccionada, currentId, idPuesto, idCalendario, null, null));
                caldroidFragment.refreshView();
                calcularNomina();
                break;
            case "puesto":
                idPuesto = currentId;
                String nombrePuesto = "";
                Cursor cursorIdPuesto = datos.obtenerPuestoById(idPuesto);
                //Nos aseguramos de que existe al menos un registro
                if (cursorIdPuesto.moveToFirst()) {
                    nombrePuesto = cursorIdPuesto.getString(cursorIdPuesto.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));
                }
                puestoSeleccionado.setText(nombrePuesto);
                break;
            case "calendario":
                idCalendario = currentId;
                String nombreCalendario;
                Cursor cursorIdCalendario = datos.obtenerCalendarioById(idCalendario);
                //Nos aseguramos de que existe al menos un registro
                if (cursorIdCalendario.moveToFirst()) {
                    nombreCalendario = cursorIdCalendario.getString(cursorIdCalendario.getColumnIndex(NombresColumnasBaseDatos.Calendarios.NOMBRE));
                    calendarioSeleccionado.setText(nombreCalendario);
                    caldroidFragment.refreshView();
                }
                break;
        }
    }

    /**
     * Clase asíncrona encargada de cargar los Fichajes de la base de datos.
     */
    private class ObtenerFichajes extends AsyncTask<String, Void, Cursor> {

        AsyncResponse delegate = null;

        @Override
        protected Cursor doInBackground(String... voids) {
            String idCalendario = voids[0];
            Cursor cursorFichajes = null;
            try {
                cursorFichajes = datos.obtenerFichajesParaCalendario(codigoOperario, idCalendario);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            }
            return cursorFichajes;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            if (cursor.moveToFirst()) {
                delegate.processFinish(cursor);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.no_hay_fichajes), Toast.LENGTH_SHORT).show();
            }
        }
    }
}