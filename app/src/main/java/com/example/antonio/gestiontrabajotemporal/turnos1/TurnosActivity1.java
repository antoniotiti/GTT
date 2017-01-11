package com.example.antonio.gestiontrabajotemporal.turnos1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.antonio.gestiontrabajotemporal.R;

public class TurnosActivity1 extends AppCompatActivity {

    public static final String EXTRA_TURNO_ID = "extra_turno_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TurnosFragment1 fragment = (TurnosFragment1)
                getSupportFragmentManager().findFragmentById(R.id.turnos_container);

        if (fragment == null) {
            fragment = TurnosFragment1.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.turnos_container, fragment)
                    .commit();
        }

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
