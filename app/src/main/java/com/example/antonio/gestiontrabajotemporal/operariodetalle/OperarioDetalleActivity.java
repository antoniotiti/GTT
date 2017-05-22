package com.example.antonio.gestiontrabajotemporal.operariodetalle;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.pantallacalendario.PantallaCalendarioActivity;
import com.example.antonio.gestiontrabajotemporal.turnodetalle.TurnoDetailFragment;
import com.example.antonio.gestiontrabajotemporal.turnos.TurnosActivity;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

public class OperarioDetalleActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener {

     static final int REQUEST_PERMISSION = 2;
     static final int RESULT_LOAD_IMAGE = 1;
    OperarioDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operario_detalle);
        setToolbar(); //Añadir la Toolbar.

        String operarioId = getIntent().getStringExtra(PantallaCalendarioActivity.EXTRA_OPERARIO_ID);//Obtenemos el id del operario de la pantalla anterior

         fragment = (OperarioDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.operario_detail_container);
        if (fragment == null) {
            fragment = OperarioDetailFragment.newInstance(operarioId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.operario_detail_container, fragment)
                    .commit();
        }
    }

    /**
     * Método que se encarga de establecer la toolbar.
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_operario_detalle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.
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
     * Método al que se llama cuando se utiliza el botón de retroceso de la SupportActionBar.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Método encargado de recoger el evento al pulsar en "Ok" del SimpleDialog para borrar un operario de la lista.
     * Borra el turno seleccionado
     */
    @Override
    public void onPossitiveButtonClick(String tag, String fecha) {
        OperarioDetailFragment fragment = (OperarioDetailFragment) getSupportFragmentManager().findFragmentById(R.id.operario_detail_container);
        if (fragment != null) {
            fragment.borrarOperario();
        }
    }

    /**
     * Método encargado de recoger el evento al pulsar en "Cancelar" del SimpleDialog para borrar un operario de la lista.
     * Muestra un mensaje.
     */
    @Override
    public void onNegativeButtonClick(String tag, String fecha) {
        Toast.makeText(this, getString(R.string.cancelar), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               fragment.abrirGaleria();
            } else {
                Toast.makeText(this, getString(R.string.error_permisos), Toast.LENGTH_LONG).show();
            }
        }
    }
}