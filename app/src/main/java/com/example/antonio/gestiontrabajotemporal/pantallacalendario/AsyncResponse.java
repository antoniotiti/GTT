package com.example.antonio.gestiontrabajotemporal.pantallacalendario;

import android.database.Cursor;

public interface AsyncResponse {
    void processFinish(Cursor output);
}