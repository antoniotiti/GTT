package com.example.antonio.gestiontrabajotemporal.calendariodetalle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.calendarios.CalendariosActivity;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

public class CalendarioDetalleActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calendario_detalle);

        String calendarioId = getIntent().getStringExtra(CalendariosActivity.EXTRA_CALENDARIO_ID);

        toolbar.setTitle(calendarioId == null ? "Añadir Calendario" : "Editar Calendario");//TODO Cambia el titulo?

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.

        CalendarioDetailFragment fragment = (CalendarioDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.calendario_detail_container);
        if (fragment == null) {
            fragment = CalendarioDetailFragment.newInstance(calendarioId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.calendario_detail_container, fragment)
                    .commit();
        }
    }

    /**
     * Método encargado de crear el menú en la SupportActionBar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
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

    /**
     * Método encargado de recoger el evento al pulsar en "Ok" del SimpleDialog para borrar un calendario de la lista.
     * Borra el calendario seleccionado
     * @param tag
     * @param fecha
     */
    @Override
    public void onPossitiveButtonClick(String tag, String fecha) {
        CalendarioDetailFragment fragment = (CalendarioDetailFragment) getSupportFragmentManager().findFragmentById(R.id.calendario_detail_container);
        if (fragment instanceof CalendarioDetailFragment) {
            fragment.borrarCalendario();
        }
    }

    /**
     * Método encargado de recoger el evento al pulsar en "Cancelar" del SimpleDialog para borrar un calendario de la lista.
     * Muestra un mensaje.
     * @param tag
     * @param fecha
     */
    @Override
    public void onNegativeButtonClick(String tag, String fecha) {
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
    }

}
