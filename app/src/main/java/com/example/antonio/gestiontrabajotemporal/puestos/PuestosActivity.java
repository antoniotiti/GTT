package com.example.antonio.gestiontrabajotemporal.puestos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.antonio.gestiontrabajotemporal.R;

public class PuestosActivity extends AppCompatActivity {
    public static final String EXTRA_PUESTO_ID = "extra_puesto_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puestos);

        setToolbar(); //Añadir la Toolbar.

        PuestosFragment fragment = (PuestosFragment)
                getSupportFragmentManager().findFragmentById(R.id.puestos_container);

        if (fragment == null) {
            fragment = PuestosFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.puestos_container, fragment)
                    .commit();
        }
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_puesto);
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