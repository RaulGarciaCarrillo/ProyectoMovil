package com.lmad.proyectomovil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mario on 06/11/2016.
 */

public class UsuarioDataSource extends SQLHelper {
    private static final String sTableName ="Usuario_Logeado"; //no es case sensitive
    //nombres exactamente iguales
    private static final String sColumnId = "id";
    private static final String sColumnIdUsuarioLogeado = "idUsuario";


    public UsuarioDataSource(Context context) {
        super(context);
    }

    public void insertUsuario (Integer idUsuario) {
        SQLiteDatabase db = getWritableDatabase(); //abre la conexión, y devuelve un objeto tipo SQLiteDB
        //metodos de android, objeto ContentValues
        //Toda la información para agregar, actualizar
        ContentValues values = new ContentValues();
        values.put(sColumnIdUsuarioLogeado, idUsuario); //la llave se tiene que llamar igual que la columna
        db.insert(sTableName, null, values);

        db.close();
    }

    public void deleteUsuario (){
        SQLiteDatabase db = getWritableDatabase();

        //db.delete(sTableName,sColumnId + "=?", new String[]{"" + idUsuario}); //clausula where
        db.execSQL("delete from "+ sTableName);

        db.close();
    }

    public Integer getUsuario () {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(sTableName, null, null, null, null, null, sColumnId + " DESC", "1");

        if (cursor.moveToFirst()) { //mueve al primer elemento, si existe o no
            int id = cursor.getInt(cursor.getColumnIndex(sColumnId)); //indice de la columna
            int idUsuarioLogeado = cursor.getInt(cursor.getColumnIndex(sColumnIdUsuarioLogeado));

            cursor.close();

            db.close();
            return idUsuarioLogeado;
        }
        return null;
    }

}
