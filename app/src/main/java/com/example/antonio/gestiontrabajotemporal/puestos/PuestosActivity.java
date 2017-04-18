package com.example.antonio.gestiontrabajotemporal.puestos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.antonio.gestiontrabajotemporal.R;

public class PuestosActivity extends AppCompatActivity {
    public static final String EXTRA_PUESTO_ID = "extra_puesto_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puestos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_puesto);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.


        PuestosFragment fragment = (PuestosFragment)
                getSupportFragmentManager().findFragmentById(R.id.puestos_container);

        if (fragment == null) {
            fragment = PuestosFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.puestos_container, fragment)
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_puesto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
