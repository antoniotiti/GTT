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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.calendarios.CalendariosActivity;
import com.example.antonio.gestiontrabajotemporal.puestos.PuestosActivity;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.turnos.TurnosActivity;
import com.example.antonio.gestiontrabajotemporal.ui.SettingsActivity;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class PantallaCalendarioActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    OperacionesBaseDatos datos;
    // Spinner calendarioSpinner;
    // SimpleCursorAdapter calendarioSpinnerAdapter;
    TextView calendarioSeleccionado;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    String codigoOperario;
    String password;

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

        // If Activity is created after rotation
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
        boolean modoCompacto = sharedPref.getBoolean("pref_switch_modo_compacto", false);
        String calendarioPredeterminado = sharedPref.getString("pref_calendario_predeterminado", "");
        String diaComienzoSemana = sharedPref.getString("pref_dia_comienzo_semana", "");

        // Setup caldroid fragment
        caldroidFragment = new PantallaCalendarioFragment();

        // Setup arguments
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        int numeroDiaComienzoSemana; //Weekday conventions Caldroid
        switch (diaComienzoSemana) {
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
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, numeroDiaComienzoSemana); //

        // Activar desactivar modo compacto.
        //args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, !modoCompacto);

        // Calendario predeterminado.
        args.putString("pref_calendario_predeterminado", calendarioPredeterminado);
        if (!calendarioPredeterminado.equals("")) {
            calendarioSeleccionado.setText(calendarioPredeterminado);
            Cursor cursorIdCalendario= datos.obtenerIdCalendarioByNombre(calendarioPredeterminado);
            //Nos aseguramos de que existe al menos un registro
            if (cursorIdCalendario.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String idCalendario= cursorIdCalendario.getString(0);
                    obtenerFichajes(idCalendario);

                } while(cursorIdCalendario.moveToNext());
            }

        }



        if (modoCompacto) {
            // Activar dark theme
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        }

        if (caldroidFragment.getArguments() == null) {
            caldroidFragment.setArguments(args);
        } else {
            //Consider explicitly clearing arguments here
            caldroidFragment.getArguments().putAll(args);
        }
        //caldroidFragment.setArguments(args);


        setCustomResourceForDates();

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

    private void obtenerFichajes(String idCalendario) {
        new ObtenerFichajes().execute(idCalendario);
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
                //showSnackBar("Se abren los ajustes");
                //startActivity(new Intent(this, CrearTurno.class));
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String currentCalendarioNombre = (String) parent.getSelectedItem();

        Toast.makeText(getApplicationContext(), currentCalendarioNombre, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }


    /**
     * Clase asíncrona encargada de cargar los calendarios de la base de datos a la lista.
     */
    private class ObtenerFichajes extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... voids) {
            String idCalendario=voids[0];

            ArrayList<String> my_array = new ArrayList<>();

            try {
                Cursor cursorFichajes= datos.obtenerFichajes(codigoOperario,idCalendario);
                //Nos aseguramos de que existe al menos un registro
                if (cursorFichajes.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        String fechaFichaje= cursorFichajes.getString(cursorFichajes.getColumnIndex(NombresColumnasBaseDatos.Fichajes.FECHA));
                        double horasExtras = cursorFichajes.getDouble(cursorFichajes.getColumnIndex(NombresColumnasBaseDatos.Fichajes.HORA_EXTRA));
                        String idturno= cursorFichajes.getString(cursorFichajes.getColumnIndex(NombresColumnasBaseDatos.Fichajes.ID_TURNO));
                        String idPuesto= cursorFichajes.getString(cursorFichajes.getColumnIndex(NombresColumnasBaseDatos.Fichajes.ID_PUESTO));


                        //TODO pintar el calendario(no coge el puesto)

                    } while(cursorFichajes.moveToNext());
                }
                cursorFichajes.close();
                datos.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error encountered.",
                        Toast.LENGTH_LONG);
            }
            return my_array;
        }


        @Override
        protected void onPostExecute(ArrayList<String> cursor) {
            if (cursor != null && cursor.size() > 0) {

                ArrayAdapter calendarioSpinnerAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_row, cursor);


            } else {
                Toast.makeText(getApplicationContext(),
                        "No hay calendarios, tiene que crear un calendario pra poder continuar.", Toast.LENGTH_SHORT).show();
            }
        }


    }

}