package com.example.antonio.gestiontrabajotemporal.puestodetalle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.puestos.PuestosActivity;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

public class PuestoDetalleActivity extends AppCompatActivity implements SimpleDialog.OnSimpleDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puesto_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_puesto);

        String puestoId = getIntent().getStringExtra(PuestosActivity.EXTRA_PUESTO_ID);

        toolbar.setTitle(puestoId == null ? "Añadir Puesto" : "Editar Puesto");//TODO Cambia el titulo?

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar el botón de retroceso en la SupportActionBar.

        PuestoDetailFragment fragment = (PuestoDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.puesto_detail_container);
        if (fragment == null) {
            fragment = PuestoDetailFragment.newInstance(puestoId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.puesto_detail_container, fragment)
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
     * Método encargado de recoger el evento al pulsar en "Ok" del SimpleDialog para borrar un puesto de la lista.
     * Borra el puesto seleccionado
     */
    @Override
    public void onPossitiveButtonClick() {
        PuestoDetailFragment fragment = (PuestoDetailFragment) getSupportFragmentManager().findFragmentById(R.id.puesto_detail_container);
        if (fragment instanceof PuestoDetailFragment) {
            fragment.borrarPuesto();
        }
    }

    /**
     * Método encargado de recoger el evento al pulsar en "Cancelar" del SimpleDialog para borrar un puesto de la lista.
     * Muestra un mensaje.
     */
    @Override
    public void onNegativeButtonClick() {
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();
    }

}
