package com.example.antonio.gestiontrabajotemporal.turnodetalle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.turnos.TurnosActivity;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

public class TurnoDetalleActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turno_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        String turnoId = getIntent().getStringExtra(TurnosActivity.EXTRA_TURNO_ID);
        toolbar.setTitle(turnoId == null ? "Añadir Turno" : "Editar Turno");//TODO Cambia el titulo?

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.

        TurnoDetailFragment fragment = (TurnoDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.turno_detail_container);
        if (fragment == null) {
            fragment = TurnoDetailFragment.newInstance(turnoId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.turno_detail_container, fragment)
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
        getMenuInflater().inflate(R.menu.menu_turno_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Método que se llama cuando se utiliza el botón de retroceso de la SupportActionBar.
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Método encargado de recoger el evento al pulsar en "Ok" del SimpleDialog para borrar un turno de la lista.
     * Borra el turno seleccionado
     */
    @Override
    public void onPossitiveButtonClick() {
        TurnoDetailFragment fragment = (TurnoDetailFragment) getSupportFragmentManager().findFragmentById(R.id.turno_detail_container);
        if (fragment instanceof TurnoDetailFragment) {
            fragment.borrarTurno();
        }
    }

    /**
     * Método encargado de recoger el evento al pulsar en "Cancelar" del SimpleDialog para borrar un turno de la lista.
     * Muestra un mensaje.
     */
    @Override
    public void onNegativeButtonClick() {
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
    }

}
