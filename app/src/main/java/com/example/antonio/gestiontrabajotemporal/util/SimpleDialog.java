package com.example.antonio.gestiontrabajotemporal.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.antonio.gestiontrabajotemporal.R;

/**
 * Fragmento con diálogo básico
 */
public class SimpleDialog extends DialogFragment {

    // Interfaz de comunicación
    OnSimpleDialogListener listener;

    public SimpleDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String fecha = getArguments() != null ? getArguments().getString("fecha") : "";
        return createSimpleDialog(fecha);
    }

    /**
     * Crea un diálogo de alerta sencillo dependiendo del tag que se reciba
     *
     * @param fecha Fecha seleccionada
     * @return Nuevo diálogo
     */
    public AlertDialog createSimpleDialog(final String fecha) {

        final String tag = getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String titulo = getString(R.string.atencion);
        String mensaje = "";

        switch (tag) {
            case "EliminarTurno":
                mensaje = getString(R.string.mensaje_eliminar_turno);
                break;
            case "EliminarPuesto":
                mensaje = getString(R.string.mensaje_eliminar_puesto);
                break;
            case "EliminarCalendario":
                mensaje = getString(R.string.mensaje_eliminar_calendario);
                break;
            case "EliminarFichaje":
                mensaje = getString(R.string.mensaje_eliminar_fichaje);
                break;
            case "EliminarOperario":
                mensaje = getString(R.string.mensaje_eliminar_operario);
                break;
            case "ModificarFichaje":
                mensaje = getString(R.string.mensaje_modificar_fichaje);
                break;
            case "CrearCalendario":
                mensaje = getString(R.string.mensaje_crear_calendario);
                break;
            case "CrearPuesto":
                mensaje = getString(R.string.mensaje_crear_puesto);
                break;
            case "CrearTurno":
                mensaje = getString(R.string.mensaje_crear_turno);
                break;
            case "SeleccionarCalendario":
                mensaje = getString(R.string.mensaje_seleccionar_calendario);
                break;
            case "SeleccionarPuesto":
                mensaje = getString(R.string.mensaje_seleccionar_puesto);
                break;
            case "ModoAsistido":
                mensaje = getString(R.string.mensaje_modo_asistido);
                break;
        }

        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onPossitiveButtonClick(tag, fecha);
                            }
                        })
                .setNegativeButton(getString(R.string.cancelar),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onNegativeButtonClick(tag, fecha);
                            }
                        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnSimpleDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + getString(R.string.error_OnSimpleDialogListener));
        }
    }

    public interface OnSimpleDialogListener {
        void onPossitiveButtonClick(String tag, String fecha); //Eventos Botón Positivo
        void onNegativeButtonClick(String tag, String fecha); //Eventos Botón Negativo
    }
}