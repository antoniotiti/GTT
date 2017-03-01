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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.calendarios.CalendariosActivity;
import com.example.antonio.gestiontrabajotemporal.modelo.Fichaje;
import com.example.antonio.gestiontrabajotemporal.puestos.PuestosActivity;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.turnos.TurnosActivity;
import com.example.antonio.gestiontrabajotemporal.ui.SettingsActivity;
import com.example.antonio.gestiontrabajotemporal.util.DialogoSeleccionTurno;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


@SuppressLint("SimpleDateFormat")
public class PantallaCalendarioActivity extends AppCompatActivity implements AsyncResponse, SimpleDialog.OnSimpleDialogListener, DialogoSeleccionTurno.OnItemClickListener {

    final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    OperacionesBaseDatos datos;
    // Spinner calendarioSpinner;
    // SimpleCursorAdapter calendarioSpinnerAdapter;
    TextView calendarioSeleccionado;
    String codigoOperario;
    String password;
    String idCalendario = "";
    String fechaSeleccionada = "";
    TextView txtSeleccionPuesto;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    //ObtenerFichajes obtenerFichajesAsyncTask =new ObtenerFichajes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_calendario);

        setToolbar();// Añadir la Toolbar

        // Obtenemos la instancia del adaptador de Base de Datos.
        datos = OperacionesBaseDatos.obtenerInstancia(this);
        calendarioSeleccionado = (TextView) findViewById(R.id.textView_calendario_seleccionado);
        //TODO al hacer clik mandar a seleecion calendario de preferencias

        //calendarioSpinner = (Spinner) findViewById(R.id.spinner_calendarios);

        txtSeleccionPuesto = (TextView) findViewById(R.id.textView_seleccion_puesto);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_turno);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//Recibimos el código de usuario y password de la página de logeo.
        Bundle bundle = getIntent().getExtras();
        codigoOperario = getIntent().getExtras().getString("codigoOperario");
        password = getIntent().getExtras().getString("password");


        //TODO Configuración de valores predeterminados
        PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);

       /* SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean modoCompacto = sharedPref.getBoolean("pref_switch_modo_compacto", false);
        String calendarioPredeterminado=sharedPref.getString("calendario_predeterminado","");*/

        // Carga de datos
        //cargarCalendarios();


        //Cursor calendarios = datos.obtenerCalendarios();

       /* //creating adapter for spinner
        String[] nameList=new String[calendarios.getCount()];

        for(int i=0;i<calendarios.getCount();i++){
            nameList[i]=calendarios.getString(calendarios.getInt(i)); //create array of name
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameList);
        //drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attaching data adapter to spinner
        calendarioSpinner.setAdapter(dataAdapter);*/


// Relacionado la escucha de selección de GenreSpinner
        // calendarioSpinner.setOnItemSelectedListener(this);


        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        // caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized version, uncomment below line ****
        //caldroidFragment = new PantallaCalendarioFragment();
/*
        // Setup arguments

        // If Activity is created after rotation TODO
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            if (modoCompacto==true){
                Toast.makeText(this, "Modo compacto", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "OFF", Toast.LENGTH_SHORT).show();
            }

            // Día que comienza la semana.
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY); //

            // Activar desactivar modo compacto.
            //args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, !modoCompacto);

            // Calendario predeterminado.
            args.putString("calendario_predeterminado", calendarioPredeterminado);

            // Activar dark theme
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }*/


        // Actualizar visibilidad de miniaturas
     /*   boolean miniaturasPref = sharedPref.getBoolean("miniaturas", true);
        adapter.setConMiniaturas(miniaturasPref);

        //Actualizar cantidad de items
        cantidadItems = Integer.parseInt(sharedPref.getString("numArticulos", "8"));
        updateAdapter(ConjuntoListas.randomList(cantidadItems));*/

      /*  setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        final TextView textView = (TextView) findViewById(R.id.textview);

        final Button customizeButton = (Button) findViewById(R.id.customize_button);

        // Customize the calendar
        customizeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (undo) {
                    customizeButton.setText(getString(R.string.customize));
                    textView.setText("");

                    // Reset calendar
                    caldroidFragment.clearDisableDates();
                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setMinDate(null);
                    caldroidFragment.setMaxDate(null);
                    caldroidFragment.setShowNavigationArrows(true);
                    caldroidFragment.setEnableSwipe(true);
                    caldroidFragment.refreshView();
                    undo = false;
                    return;
                }

                // Else
                undo = true;
                customizeButton.setText(getString(R.string.undo));
                Calendar cal = Calendar.getInstance();

                // Min date is last 7 days
                cal.add(Calendar.DATE, -7);
                Date minDate = cal.getTime();

                // Max date is next 7 days
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 14);
                Date maxDate = cal.getTime();

                // Set selected dates
                // From Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 2);
                Date fromDate = cal.getTime();

                // To Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 3);
                Date toDate = cal.getTime();

                // Set disabled dates
                ArrayList<Date> disabledDates = new ArrayList<Date>();
                for (int i = 5; i < 8; i++) {
                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, i);
                    disabledDates.add(cal.getTime());
                }

                // Customize
                caldroidFragment.setMinDate(minDate);
                caldroidFragment.setMaxDate(maxDate);
                caldroidFragment.setDisableDates(disabledDates);
                caldroidFragment.setSelectedDates(fromDate, toDate);
                caldroidFragment.setShowNavigationArrows(false);
                caldroidFragment.setEnableSwipe(false);

                caldroidFragment.refreshView();

                // Move to date
                // cal = Calendar.getInstance();
                // cal.add(Calendar.MONTH, 12);
                // caldroidFragment.moveToDate(cal.getTime());

                String text = "Today: " + formatter.format(new Date()) + "\n";
                text += "Min Date: " + formatter.format(minDate) + "\n";
                text += "Max Date: " + formatter.format(maxDate) + "\n";
                text += "Select From Date: " + formatter.format(fromDate)
                        + "\n";
                text += "Select To Date: " + formatter.format(toDate) + "\n";
                for (Date date : disabledDates) {
                    text += "Disabled Date: " + formatter.format(date) + "\n";
                }

                textView.setText(text);
            }
        });

        Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);

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

    @Override
    protected void onResume() {
        super.onResume();

        //Obtenemos las preferencias.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean modoTemaOscuro = sharedPref.getBoolean("pref_switch_modo_tema_oscuro", false);
        String calendarioPredeterminado = sharedPref.getString("pref_calendario_predeterminado", "");
        String nombreDiaComienzoSemana = sharedPref.getString("pref_dia_comienzo_semana", "");

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
                    //obtenerFichajes();
                } while (cursorIdCalendario.moveToNext());
            }
        }

        Map<String, Object> extraData = caldroidFragment.getExtraData();

        extraData.put("OPERARIO", codigoOperario);
        extraData.put("CALENDARIO", idCalendario);
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

                Cursor cursorFichajeFecha = datos.obtenerFichajeFecha(codigoOperario, formatter.format(date));

                if (cursorFichajeFecha.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        Bundle args = new Bundle();
                        args.putString("fecha", formatter.format(date));
                        SimpleDialog dialogo = new SimpleDialog();

                        dialogo.setArguments(args);
                        dialogo.show(getSupportFragmentManager(), "ModificarFichaje");

                    } while (cursorFichajeFecha.moveToNext());
                } else {
                    // Mostramos un diálogo de selección de turnos
                    DialogoSeleccionTurno dialogo = new DialogoSeleccionTurno();
                    dialogo.show(getSupportFragmentManager(), "SeleccionarTurno");
                    fechaSeleccionada = formatter.format(date);
                    /*Toast.makeText(getApplicationContext(), formatter.format(date),
                            Toast.LENGTH_SHORT).show();*/


                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {

                Cursor cursorFichajeFecha = datos.obtenerFichajeFecha(codigoOperario, formatter.format(date));

                if (cursorFichajeFecha.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        Bundle args = new Bundle();
                        args.putString("fecha", formatter.format(date));
                        SimpleDialog dialogo = new SimpleDialog();

                        dialogo.setArguments(args);
                        dialogo.show(getSupportFragmentManager(), "EliminarFichaje");
                    } while (cursorFichajeFecha.moveToNext());
                } else {
                    Toast.makeText(getApplicationContext(), "No hay fichajes para: " + formatter.format(date),
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

        // Customize the calendar
        txtSeleccionPuesto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               //TODO seleccion de puestos

            }
        });

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

    private void obtenerFichajes() {
        ObtenerFichajes obtenerFichajesAsyncTask = new ObtenerFichajes();

        obtenerFichajesAsyncTask.delegate = this;
        obtenerFichajesAsyncTask.execute(idCalendario);
    }

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
    @Override
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
                        dateObj = formatter.parse(fechaFichaje);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dateObj);

                   /* int[] mColors = getResources().getIntArray(R.array.colores_dialogo);

                    int color = getResources().getInteger(colorTextoTurno);*/


                        ColorDrawable colorFondo = new ColorDrawable(colorFondoTurno);
                        ColorDrawable colorTexto = new ColorDrawable(colorTextoTurno);
                        colorTexto.getColor();
                        int color = R.color.white;

                        // To set the extraData:TODO
                    /*Map<String, Object> extraData = caldroidFragment.getExtraData();
                    extraData.put("ABREVIATURA", abreviaturnaNombreTurno);*/


                        caldroidFragment.setBackgroundDrawableForDate(colorFondo, calendar.getTime());
                        caldroidFragment.setTextColorForDate(R.color.white, calendar.getTime());//TODO no encuentra el color pasado int
                        // Refresh view
                        // caldroidFragment.refreshView();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            } while (cursorFichajesRecibidos.moveToNext());

          /*  // To set the extraData:TODO
            Map<String, Object> extraData = caldroidFragment.getExtraData();
                    extraData.put("CURSOR", cursorFichajesRecibidos);*/

            Map<String, Object> extraData = caldroidFragment.getExtraData();

            extraData.put("OPERAIO", codigoOperario);
            extraData.put("CALENDARIO", idCalendario);
            // Refresh view
            caldroidFragment.refreshView();

            // Attach to the activity
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar1, caldroidFragment);
            //t.addToBackStack(null);//Volver al fragment anterior
            t.commit();
        }

    }

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
                    date = formatter.parse(fecha);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (datos.eliminarFichaje(codigoOperario, fecha)) {
                    Toast.makeText(getApplicationContext(), "Fichaje eliminado: " + fecha, Toast.LENGTH_SHORT).show();

                    caldroidFragment.clearBackgroundDrawableForDate(date);
                    caldroidFragment.clearTextColorForDate(date);

                    //TODO no lo borra directamente
                    caldroidFragment.setBackgroundDrawableForDate(colorFondo,date);

                    caldroidFragment.refreshView();


                } else {
                    Toast.makeText(getApplicationContext(), "No Fichaje eliminado: " + fecha, Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
    public void onItemClick(String currentTurnoId) {

        /*String currentTurnoId = cursorTurnoSeleccionado.getString(cursorTurnoSeleccionado.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));
        String currentTurnoAbreviatura = cursorTurnoSeleccionado.getString(cursorTurnoSeleccionado.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));
        String currentTurnoColorFondo = cursorTurnoSeleccionado.getString(cursorTurnoSeleccionado.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_FONDO));
        String currentTurnoColorTexto = cursorTurnoSeleccionado.getString(cursorTurnoSeleccionado.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_TEXTO));*/

        /*txtSeleccionPuesto.setText(currentTurnoAbreviatura);
        txtSeleccionPuesto.setBackgroundColor(Integer.parseInt(currentTurnoColorFondo));
        txtSeleccionPuesto.setTextColor(Integer.parseInt(currentTurnoColorTexto));*/
//TODO cambiar puesto cada vez q borremos datos
        long insertado = datos.insertarFichaje(new Fichaje(codigoOperario, fechaSeleccionada, currentTurnoId, "PU-1eec3e59-01a2-4b99-a44e-13a89a3ff565", idCalendario, 2.6));
        caldroidFragment.refreshView();

        //Toast.makeText(getApplicationContext(), currentTurnoId, Toast.LENGTH_SHORT).show();
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
                cursorFichajes = datos.obtenerFichajes(codigoOperario, idCalendario);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error encountered.",
                        Toast.LENGTH_LONG);
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