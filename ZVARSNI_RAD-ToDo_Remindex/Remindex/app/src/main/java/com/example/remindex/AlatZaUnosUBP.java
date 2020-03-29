package com.example.remindex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlatZaUnosUBP extends SQLiteOpenHelper {   //SQLiteOpenHelper je klasa koja služi unosu podataka u SQLite bazu podataka

    //Varijable koje pomažu pri interakciji s bazom podataka
    private static final int VerzijaBP=1; //Verzija baze
    private static final String NAZIV_BAZE="EVENTI.db";       //Naziv baze
    public static final String NAZIV_TABLICE="dogadaji";      //Naziv tablice
    //Stupci
    public static final String REDNI_BROJ="rb";
    public static final String NAZIV="naziv";
    public static final String DATUM="datum";
    public static final String VRIJEME="vrijeme";
    public static final String BOJA="boja";
    //Ovi stupci se ne koriste, no ostavio sam ih u bazi
    public static final String NOTIFI_GODINA="notifiGodina";
    public static final String NOTIFI_MJESEC="notifiMjesec";
    public static final String NOTIFI_DAN="notifiDan";
    public static final String NOTIFI_SAT="notifiSat";
    public static final String NOTIFI_MINUTA="notifiMinuta";

    private SQLiteDatabase mojaBaza;

    //Konstruktor koji stvara bazu pri pozivanju klase
    public AlatZaUnosUBP(Context context) {
        super(context, NAZIV_BAZE, null, VerzijaBP);
    }

    //Kod stvaranja baze, stvara se i tablica
    @Override
    public void onCreate(SQLiteDatabase db) {

        String stvaranjeTablice = "CREATE TABLE " + NAZIV_TABLICE +
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
        db.execSQL(stvaranjeTablice);
    }
    //onUpgrade mora biti naveden, no u ovom slučaju nije potreban, stoga je prazan
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDB(){
        mojaBaza=getWritableDatabase();
    }
    public void closeDB(){
        if(mojaBaza!=null && mojaBaza.isOpen()){
            mojaBaza.close();
        }
    }

    public long unos(int rb, String nazivDog, String datumDog, String vrijemeDog, String bojaDog, int notifiGodina, int notifiMjesec, int notifiDan, int notifiSat, int notifiMinuta){
        ContentValues vrijednosti = new ContentValues();

        vrijednosti.put(REDNI_BROJ, rb);
        vrijednosti.put(NAZIV,nazivDog);
        vrijednosti.put(DATUM,datumDog);
        vrijednosti.put(VRIJEME,vrijemeDog);
        vrijednosti.put(BOJA,bojaDog);
        vrijednosti.put(NOTIFI_GODINA,notifiGodina);
        vrijednosti.put(NOTIFI_MJESEC,notifiMjesec);
        vrijednosti.put(NOTIFI_DAN,notifiDan);
        vrijednosti.put(NOTIFI_SAT,notifiSat);
        vrijednosti.put(NOTIFI_MINUTA,notifiMinuta);
        //Metoda "insert" unosi podatke u bazu te vraća "-1" u slučaju greške kod unosa
        return mojaBaza.insert(NAZIV_TABLICE, null, vrijednosti);
    }

    public long brisiRedak(int rb){
        String gdje = REDNI_BROJ+" = "+rb;
        //metoda "delete" vraća "0" ako se dogodi greška kod brisanja
        return mojaBaza.delete(NAZIV_TABLICE, gdje, null);
    }

    public Cursor ucitajPodatkeIzBaze(){  //Vraca sve podatke iz baze
        String upit="SELECT * FROM "+NAZIV_TABLICE+";";
        return mojaBaza.rawQuery(upit, null);
    }

}