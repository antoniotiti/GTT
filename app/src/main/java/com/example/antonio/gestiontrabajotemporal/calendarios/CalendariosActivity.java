package com.example.antonio.gestiontrabajotemporal.calendarios;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.antonio.gestiontrabajotemporal.R;

public class CalendariosActivity extends AppCompatActivity {
    public static final String EXTRA_CALENDARIO_ID = "extra_calendario_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarios);

        setToolbar(); //Añadir la Toolbar.

        CalendariosFragment fragment = (CalendariosFragment)
                getSupportFragmentManager().findFragmentById(R.id.calendarios_container);

        if (fragment == null) {
            fragment = CalendariosFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.calendarios_container, fragment)
                    .commit();
        }
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calendario);
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