package com.example.antonio.gestiontrabajotemporal.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Fragmento con un diálogo de elección de tiempos
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TimeDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance(); //Iniciar con el tiempo actual
        c.set(0, 0, 0, 0, 0, 0);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Retornar en nueva instancia del diálogo selector de tiempo
        return new TimePickerDialog(
                getActivity(),
                (TimePickerDialog.OnTimeSetListener) getTargetFragment(),
                hour,
                minute,
                true);
    }
}