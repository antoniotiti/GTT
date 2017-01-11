package com.example.antonio.gestiontrabajotemporal.turnos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.puestos.PuestosFragment;

public class TurnosActivity extends AppCompatActivity {
    public static final String EXTRA_TURNO_ID = "extra_turno_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TurnosFragment fragment = (TurnosFragment)
                getSupportFragmentManager().findFragmentById(R.id.turnos_container);

        if (fragment == null) {
            fragment = TurnosFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.turnos_container, fragment)
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
