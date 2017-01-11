package com.example.antonio.gestiontrabajotemporal.util;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Fragmento con un diálogo de elección de tiempos
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TimeDialog extends DialogFragment {
    // Interfaz de comunicación
    TimePickerDialog.OnTimeSetListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Iniciar con el tiempo actual
        final Calendar c = Calendar.getInstance();
        c.set(0, 0, 0, 0, 0, 0);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Retornar en nueva instancia del dialogo selector de tiempo
        return new TimePickerDialog(
                getActivity(),
                (TimePickerDialog.OnTimeSetListener) getActivity().getSupportFragmentManager(),
                hour,
                minute,
                true);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (TimePickerDialog.OnTimeSetListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " no implementó TimePickerDialog.OnTimeSetListener");

        }
    }

}