package com.example.antonio.gestiontrabajotemporal.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter {
    static final String DATABASE_NAME = "LOGIN.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;

    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "CREATE TABLE " + "LOGIN" +
            "( " + "CODIGO_OPERARIO INTEGER PRIMARY KEY, PASSWORD TEXT); ";

    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelper dbHelper;

    public LoginDataBaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public LoginDataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void insertEntry(int codigoOperario, String password) {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("CODIGO_OPERARIO", codigoOperario);
        newValues.put("PASSWORD", password);

        // Insert the row into your table
        db.insert("LOGIN", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }

    public int deleteEntry(int codigoOperario) {
        //String idPuesto=String.valueOf(ID);
        String where = "CODIGO_OPERARIO=?";
        int numberOFEntriesDeleted = db.delete("LOGIN", where, new String[]{Integer.toString(codigoOperario)});
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    public String getSinlgeEntry(int codigoOperario) {
        Cursor cursor = db.query("LOGIN", null, " CODIGO_OPERARIO=?", new String[]{Integer.toString(codigoOperario)}, null, null, null);
        if (cursor.getCount() < 1) // codigo Operarios Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    public void updateEntry(int codigoOperario, String password) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("CODIGO_OPERARIO", codigoOperario);
        updatedValues.put("PASSWORD", password);

        String where = "CODIGO_OPERARIO = ?";
        db.update("123", updatedValues, where, new String[]{Integer.toString(codigoOperario)});
    }
}