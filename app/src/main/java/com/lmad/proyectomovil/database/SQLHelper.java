package com.lmad.proyectomovil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mario on 06/11/2016.
 */

public class SQLHelper extends SQLiteOpenHelper {
    private static final String sDBName = "FoodPoints.db";
    private static final int sDBVersion = 1;

    public SQLHelper(Context context) {
        super(context, SQLHelper.sDBName, null, SQLHelper.sDBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String UsuarioLoegado = "CREATE TABLE Usuario_Logeado(id INTEGER PRIMARY KEY AUTOINCREMENT, idUsuario INTEGER);";
        db.execSQL(UsuarioLoegado);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Usuario_Logeado");
        onCreate(db);
    }
}
