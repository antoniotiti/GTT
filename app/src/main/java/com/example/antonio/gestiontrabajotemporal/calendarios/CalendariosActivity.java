package com.example.antonio.gestiontrabajotemporal.calendarios;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.antonio.gestiontrabajotemporal.R;

public class CalendariosActivity extends AppCompatActivity {
    public static final String EXTRA_CALENDARIO_ID = "extra_calendario_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calendario);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.



        CalendariosFragment fragment = (CalendariosFragment)
                getSupportFragmentManager().findFragmentById(R.id.calendarios_container);

        if (fragment == null) {
            fragment = CalendariosFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.calendarios_container, fragment)
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_calendario);
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
