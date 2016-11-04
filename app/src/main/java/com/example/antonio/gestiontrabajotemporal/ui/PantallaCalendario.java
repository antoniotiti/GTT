package com.example.antonio.gestiontrabajotemporal.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.example.antonio.gestiontrabajotemporal.R;


public class PantallaCalendario extends Activity {
    CalendarView calendario;
    String idOperario, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_calendario);

        Bundle bundle=getIntent().getExtras();
        idOperario= bundle.getString("codigoOperario");
        password = bundle.getString("password");

        calendario =(CalendarView) findViewById(R.id.calendarView);

        //calendario.setFirstDayOfWeek(2);
    }
}