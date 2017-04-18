package com.example.antonio.gestiontrabajotemporal.fichajedetalle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.pantallacalendario.PantallaCalendarioActivity;
import com.example.antonio.gestiontrabajotemporal.util.DialogoSeleccionPuesto;
import com.example.antonio.gestiontrabajotemporal.util.DialogoSeleccionTurno;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

public class FichajeDetalleActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener , DialogoSeleccionTurno.OnItemClickListener, DialogoSeleccionPuesto.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichaje_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_fichaje_detalle);

        String calendarioId = getIntent().getStringExtra(PantallaCalendarioActivity.EXTRA_CALENDARIO_ID);
        String fecha = getIntent().getStringExtra(PantallaCalendarioActivity.EXTRA_FECHA);
        String operarioId = getIntent().getStringExtra(PantallaCalendarioActivity.EXTRA_OPERARIO_ID);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.

        FichajeDetailFragment fragment = (FichajeDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fichaje_detail_container);

        if (fragment == null) {
            fragment = FichajeDetailFragment.newInstance(calendarioId, fecha, operarioId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fichaje_detail_container, fragment)
                    .commit();
        }
    }

    /**
     * Método encargado de crear el menú en la SupportActionBar.
     *
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
     * Método encargado de recoger el evento al pulsar en "Ok" del SimpleDialog para borrar un fichaje de la lista.
     * Borra el fichaje seleccionado
     *
     * @param tag
     * @param fecha
     */
    @Override
    public void onPossitiveButtonClick(String tag, String fecha) {
        FichajeDetailFragment fragment = (FichajeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fichaje_detail_container);
        if (fragment instanceof FichajeDetailFragment) {
            switch (tag) {
                case "ModificarFichaje":
                    fragment.editarFichaje();
                    break;
                case "EliminarFichaje":
                    fragment.borrarFichaje();
                    break;
            }
        }
    }

    /**
     * Método encargado de recoger el evento al pulsar en "Cancelar" del SimpleDialog para borrar un fichaje de la lista.
     * Muestra un mensaje.
     *
     * @param tag
     * @param fecha
     */
    @Override
    public void onNegativeButtonClick(String tag, String fecha) {
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(String currentId, String tag) {
        FichajeDetailFragment fragment = (FichajeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fichaje_detail_container);
        if (fragment instanceof FichajeDetailFragment) {
            switch (tag) {
                case "turno":
                    fragment.obtenerSetearTurno(currentId);

                    break;
                case "puesto":
                   fragment.obtenerSetearPuesto(currentId);

                    break;
            }
        }
    }
}
