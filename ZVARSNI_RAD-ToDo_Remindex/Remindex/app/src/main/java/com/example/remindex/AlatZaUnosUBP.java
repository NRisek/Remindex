package com.example.remindex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlatZaUnosUBP extends SQLiteOpenHelper {
    private static final int DBVersion=1;
    private static final String DATABASE_NAME="EVENTI.db";
    public static final String TABLE_NAME="dogadaji";
    public static final String REDNI_BROJ="rb";
    public static final String NAZIV="naziv";  //ovo v navodnikima su nazivi stupaca
    public static final String DATUM="datum";
    public static final String VRIJEME="vrijeme";
    public static final String BOJA="boja";
    public static final String NOTIFI_GODINA="notifiGodina";
    public static final String NOTIFI_MJESEC="notifiMjesec";
    public static final String NOTIFI_DAN="notifiDan";
    public static final String NOTIFI_SAT="notifiSat";
    public static final String NOTIFI_MINUTA="notifiMinuta";

    private SQLiteDatabase myDB;

    public AlatZaUnosUBP(Context context) {
        super(context, DATABASE_NAME, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryTable = "CREATE TABLE " + TABLE_NAME +
                "(" +
                REDNI_BROJ+" INTEGER PRIMARY KEY, " +                                               //REDNI_BROJ+" INTEGER PRIMARY KEY, " +
                NAZIV+" TEXT NOT NULL, " +
                DATUM+" TEXT NOT NULL, " +
                VRIJEME+" TEXT NOT NULL, " +
                BOJA+" TEXT NOT NULL, " +
                NOTIFI_GODINA+" INTEGER NOT NULL, " +
                NOTIFI_MJESEC+" INTEGER NOT NULL, " +
                NOTIFI_DAN+" INTEGER NOT NULL, " +
                NOTIFI_SAT+" INTEGER NOT NULL, " +
                NOTIFI_MINUTA+" INTEGER NOT NULL " +
                ")";
        db.execSQL(queryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void openDB(){
        myDB=getWritableDatabase();
    }
    public void closeDB(){
        if(myDB!=null && myDB.isOpen()){
            myDB.close();
        }
    }
    public long insert(int rb, String nazivDog, String datumDog, String vrijemeDog, String bojaDog, int notifiGodina, int notifiMjesec, int notifiDan, int notifiSat, int notifiMinuta){ //rb -1 je auto increment
        ContentValues values=new ContentValues();
        /*if(rb!=-1){
            values.put(REDNI_BROJ, rb);
        }*/

        values.put(REDNI_BROJ, rb);
        values.put(NAZIV,nazivDog);
        values.put(DATUM,datumDog);
        values.put(VRIJEME,vrijemeDog);
        values.put(BOJA,bojaDog);
        values.put(NOTIFI_GODINA,notifiGodina);
        values.put(NOTIFI_MJESEC,notifiMjesec);
        values.put(NOTIFI_DAN,notifiDan);
        values.put(NOTIFI_SAT,notifiSat);
        values.put(NOTIFI_MINUTA,notifiMinuta);
        return myDB.insert(TABLE_NAME, null, values);
    }
    /*public long update(int rb, String nazivDog, String datumDog, String vrijemeDog, String bojaDog, String notifiDatDog, String notifiVrijDog){
        ContentValues values=new ContentValues();
         /*if(rb!=-1){
            values.put(REDNI_BROJ, rb);
        }*//*
        values.put(REDNI_BROJ, rb);
        values.put(NAZIV,nazivDog);
        values.put(DATUM,datumDog);
        values.put(VRIJEME,vrijemeDog);
        values.put(BOJA,bojaDog);
        values.put(NOTIFI_DATUM,notifiDatDog);
        values.put(NOTIFI_VRIJEME,notifiVrijDog);
        String where = REDNI_BROJ+" = "+rb; // odredimo koji redak
        return myDB.update(TABLE_NAME, values, where, null);
    }*/
    public long delete(int rb){
        String where = REDNI_BROJ+" = "+rb;
        return myDB.delete(TABLE_NAME, where, null);
    }
    public Cursor getAllRecords(){  //privremena za podatke iz baze
        //myDB.query(TABLE_NAME, null, null, null, null, null, null, null); - jedan nacin, dole drugi nacin...
        String query="SELECT * FROM "+TABLE_NAME+";";
        return myDB.rawQuery(query, null);
    }

}