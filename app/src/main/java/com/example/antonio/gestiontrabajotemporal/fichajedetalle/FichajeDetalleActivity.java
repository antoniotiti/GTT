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

public class FichajeDetalleActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener, DialogoSeleccionTurno.OnItemClickListener, DialogoSeleccionPuesto.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichaje_detalle);
        setToolbar(); //Añadir la Toolbar.
        //Obtenemos los datos de la pantalla de calencario
        String calendarioId = getIntent().getStringExtra(PantallaCalendarioActivity.EXTRA_CALENDARIO_ID);
        String fecha = getIntent().getStringExtra(PantallaCalendarioActivity.EXTRA_FECHA);
        String operarioId = getIntent().getStringExtra(PantallaCalendarioActivity.EXTRA_OPERARIO_ID);

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
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_fichaje_detalle);
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

    /**
     * Método encargado de recoger el evento al pulsar en "Ok" del SimpleDialog para borrar un fichaje de la lista.
     * Borra el fichaje seleccionado
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
     */
    @Override
    public void onNegativeButtonClick(String tag, String fecha) {
        Toast.makeText(this, getString(R.string.cancelar) , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(String currentId, String tag) {
        FichajeDetailFragment fragment = (FichajeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fichaje_detail_container);
        if (fragment != null) {
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