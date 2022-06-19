package com.aplicacion.pm2e12840.Transacciones;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteConexion extends SQLiteOpenHelper {

    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version){
        super(context, dbname, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Transaction.createTableContacts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Transaction.dropTable);
        onCreate(db);
    }
}