package com.example.antonio.gestiontrabajotemporal.turnodetalle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.R;
import com.example.antonio.gestiontrabajotemporal.turnos.TurnosActivity;
import com.example.antonio.gestiontrabajotemporal.util.SimpleDialog;

public class TurnoDetalleActivity extends AppCompatActivity implements  SimpleDialog.OnSimpleDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turno_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra(TurnosActivity.EXTRA_TURNO_ID);

        TurnoDetailFragment fragment = (TurnoDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.turno_detail_container);
        if (fragment == null) {
            fragment = TurnoDetailFragment.newInstance(id);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.turno_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_turno_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onPossitiveButtonClick() {
        TurnoDetailFragment fragment =(TurnoDetailFragment)getSupportFragmentManager().findFragmentById(R.id.turno_detail_container);
        if (fragment instanceof TurnoDetailFragment){
            fragment.borrarTurno();
        }

    }

    @Override
    public void onNegativeButtonClick() {
        Toast.makeText(this, "Cancelar", Toast.LENGTH_LONG).show();

    }


    /*private void borrarTurno() {
        Toast.makeText(this, "Borrar Formulario", Toast.LENGTH_LONG).show();

    }*/
}
