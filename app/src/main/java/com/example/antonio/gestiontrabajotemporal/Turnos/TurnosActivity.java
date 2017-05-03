package com.example.antonio.gestiontrabajotemporal.turnos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.antonio.gestiontrabajotemporal.R;

public class TurnosActivity extends AppCompatActivity {
    public static final String EXTRA_TURNO_ID = "extra_turno_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);

        setToolbar(); //Añadir la Toolbar.

        TurnosFragment fragment = (TurnosFragment)
                getSupportFragmentManager().findFragmentById(R.id.turnos_container);

        if (fragment == null) {
            fragment = TurnosFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.turnos_container, fragment)
                    .commit();
        }
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_turno);
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