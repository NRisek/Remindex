package com.example.remindex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EventiDBHelper dbHelper;
    int RBzaBrisanje;
    int RBBrojac;
    ArrayList<Dogadaj> dogadaji = new ArrayList<Dogadaj>();
    public static final String SPREMANJE_RB = "SpremljeniRB";
    @Override
    protected void onStart() {
        super.onStart();
        dbHelper.openDB();
    }
    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.clodeDB();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new EventiDBHelper(MainActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences spremljeniRB = getSharedPreferences(SPREMANJE_RB,0);     //ucitavamo RB iz datoteke
        RBBrojac = spremljeniRB.getInt("spremljeniRB",RBBrojac);                  //ucitavamo RB iz datoteke

        pomocuKlase(); // nakon 1 sekunde ispis svih dogadaja

        FloatingActionButton fabDodaj = findViewById(R.id.fabDodaj);   //Inicijalizacija FAB-a
        fabDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdiNaNoviZadatakActivity();
            }
        });
        FloatingActionButton fabBrisi = findViewById(R.id.fabBrisi);
        fabBrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Unesi redni broj događaja:");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setPositiveButton("Obriši događaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RBzaBrisanje = Integer.parseInt(input.getText().toString());
                        Toast.makeText(MainActivity.this, "Odabrano: "+RBzaBrisanje, Toast.LENGTH_LONG).show();
                        long result = dbHelper.delete(RBzaBrisanje);
                        if(result==0){
                            Toast.makeText(MainActivity.this, "Greška kod brisanja!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Uspjesno obrisano!, ID: "+RBzaBrisanje, Toast.LENGTH_LONG).show();
                        }
                        //RBBrojac--;
                        //SharedPreferences spremljeniRB = getSharedPreferences(SPREMANJE_RB,0);  //spremamo RB u datoteku
                        //SharedPreferences.Editor editor=spremljeniRB.edit();
                        //editor.putInt("spremljeniRB",RBBrojac);
                        //editor.commit();
                        ispis();
                    }
                });
                builder.setNegativeButton("Poništi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        spremiRetkeUArrayList();
                    }
                });
                builder.setNeutralButton("Resetiraj ID", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RBBrojac=1;
                        SharedPreferences spremljeniRB = getSharedPreferences(SPREMANJE_RB,0);  //spremamo RB u datoteku
                        SharedPreferences.Editor editor=spremljeniRB.edit();
                        editor.putInt("spremljeniRB",RBBrojac);
                        editor.commit();
                    }
                });
                builder.show();
            }
        });
    }
    public void spremiRetkeUArrayList(){  //samo test
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = dbHelper.getAllRecords();
        String redak;
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                redak = cursor.getString(cursor
                        .getColumnIndex((EventiDBHelper.REDNI_BROJ)))+"\n"+
                        cursor.getString(cursor
                         .getColumnIndex((EventiDBHelper.NAZIV)))+"\n"+
                        cursor.getString(cursor
                          .getColumnIndex((EventiDBHelper.DATUM)))+"\n"+
                        cursor.getString(cursor
                          .getColumnIndex((EventiDBHelper.VRIJEME)))+"\n"+
                        cursor.getString(cursor
                          .getColumnIndex((EventiDBHelper.BOJA)));
                list.add(redak);
                cursor.moveToNext();
            }
        }
        String poruka="";
        for (int i = 0; i < list.size(); i++) {
            poruka+="ID: "+list.get(i)+"\n";
        }
        Toast.makeText(this, ""+poruka, Toast.LENGTH_LONG).show();
    }
    public class Dogadaj{
        public int RB;
        public String nazivDogadaja;
        public String datumDogadaja;
        public String vrijemeDogadaja;
        public String boja;
        public int notifiGodina;
        public int notifiMjesec;
        public int notifiDan;
        public int notifiSat;
        public int notifiMinuta;
    }
    public void pomocuKlase(){
        new CountDownTimer(1000,1000){
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                LinearLayout mainLinear = (LinearLayout) findViewById(R.id.main_linear);
                mainLinear.removeAllViews(); // Refreshamo LinearLayout, inace bi se samo nadopunjavalo s tw-evima
                Cursor cursor = dbHelper.getAllRecords(); //setanje po bazi
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        Dogadaj noviDogadaj = new Dogadaj();
                        noviDogadaj.RB=cursor.getInt(cursor.getColumnIndex((EventiDBHelper.REDNI_BROJ)));
                        noviDogadaj.nazivDogadaja=cursor.getString(cursor.getColumnIndex((EventiDBHelper.NAZIV)));
                        noviDogadaj.datumDogadaja=cursor.getString(cursor.getColumnIndex((EventiDBHelper.DATUM)));
                        noviDogadaj.vrijemeDogadaja=cursor.getString(cursor.getColumnIndex((EventiDBHelper.VRIJEME)));
                        noviDogadaj.boja=cursor.getString(cursor.getColumnIndex((EventiDBHelper.BOJA)));
                        noviDogadaj.notifiGodina=cursor.getInt(cursor.getColumnIndex((EventiDBHelper.NOTIFI_GODINA)));
                        noviDogadaj.notifiMjesec=cursor.getInt(cursor.getColumnIndex((EventiDBHelper.NOTIFI_MJESEC)));
                        noviDogadaj.notifiDan=cursor.getInt(cursor.getColumnIndex((EventiDBHelper.NOTIFI_DAN)));
                        noviDogadaj.notifiSat=cursor.getInt(cursor.getColumnIndex((EventiDBHelper.NOTIFI_SAT)));
                        noviDogadaj.notifiMinuta=cursor.getInt(cursor.getColumnIndex((EventiDBHelper.NOTIFI_MINUTA)));
                        dogadaji.add(noviDogadaj);
                        cursor.moveToNext();
                    }
                }
                for(int i=0;i<dogadaji.size();i++){
                    String tekst = ""+dogadaji.get(i).nazivDogadaja+"\n"+
                                      dogadaji.get(i).datumDogadaja+"\n"+
                                      dogadaji.get(i).vrijemeDogadaja;
                    TextView tw = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,                                                         // Sirina of TextView
                            LinearLayout.LayoutParams.WRAP_CONTENT);                                                        // Visina of TextView
                    tw.setLayoutParams(params);
                    params.setMargins(48, 0, 48, 20);
                    tw.setTextSize(20);
                    tw.setPadding(10,10,10,10);
                    tw.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.Crna)));
                    tw.setBackgroundResource(R.drawable.zaobljeni_tw);   //zaobljeni rubovi
                    GradientDrawable drawable = (GradientDrawable) tw.getBackground(); //sluzi tome da se zaobljeni rubovi i boja tw-a ne "prebrišu"
                    if(("Zelena").equals(dogadaji.get(i).boja)){  //Koristim .equals jer je bolje za String, gleda se svaki character, s == ne radi...
                        drawable.setColor(getResources().getColor(R.color.Zelena)); //ucitavanje "custom" boje
                        //tw.setBackgroundDrawable(getResources().getDrawable(R.color.Zelena));
                    }
                    else if(("Crvena").equals(dogadaji.get(i).boja)){
                        drawable.setColor(getResources().getColor(R.color.Crvena));
                    }
                    else if(("Žuta").equals(dogadaji.get(i).boja)){
                        drawable.setColor(getResources().getColor(R.color.Žuta));
                    }
                    else{
                        drawable.setColor(getResources().getColor(R.color.Plava));
                    }
                    final int redniBrojZaBrisanje=dogadaji.get(i).RB;
                    tw.setText(tekst);
                    mainLinear.addView(tw);                                               //Problem kod brisanja, obrise se, ali se kreira isti...
                    tw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(MainActivity.this, ""+redniBrojZaBrisanje, Toast.LENGTH_SHORT).show();
                            long result = dbHelper.delete(redniBrojZaBrisanje);
                            if(result==0){
                                Toast.makeText(MainActivity.this, "Greška kod brisanja!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Uspjesno obrisano!, ID: "+redniBrojZaBrisanje, Toast.LENGTH_LONG).show();
                            }
                            pomocuKlase(); //to ne dela jer se kreira novi tw, rijesit cemo
                            //bypass();//funkcija koja poziva funkciju koja briše TW na koji smo stisnuli
                        }
                    });
                }
            }
        }.start();
    }
    private void IdiNaNoviZadatakActivity(){                                                                            //Stvaramo funkciju IdiNaNoviZadatakActivity() koja pokreće drugi Activity
        Intent NoviZadatakIntent=new Intent(MainActivity.this, NoviZadatakActivity.class);                //Koristimo Intent
        startActivity(NoviZadatakIntent);
    }
    /*
    public void bypass(){
        ispis();
    }
    public void ispis(){
        new CountDownTimer(1000,1000){
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                LinearLayout mainLinear = (LinearLayout) findViewById(R.id.main_linear);
                mainLinear.removeAllViews(); // Refreshamo LinearLayout, inace bi se samo nadopunjavalo s tw-evima
                ArrayList<String> list = new ArrayList<String>();
                Cursor cursor = dbHelper.getAllRecords(); //setanje po bazi
                String redak; //sluzi za spremanje retka u string i u array list
                if (cursor.moveToFirst()) {

                    while (!cursor.isAfterLast()) {
                        redak = cursor.getString(cursor
                                .getColumnIndex((EventiDBHelper.REDNI_BROJ)))+"\n"+
                                cursor.getString(cursor
                                        .getColumnIndex((EventiDBHelper.NAZIV)))+"\n"+
                                cursor.getString(cursor
                                        .getColumnIndex((EventiDBHelper.DATUM)))+"\n"+
                                cursor.getString(cursor
                                        .getColumnIndex((EventiDBHelper.VRIJEME)))+
                                cursor.getString(cursor
                                        .getColumnIndex((EventiDBHelper.BOJA)));
                        list.add(redak);
                        cursor.moveToNext();
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    String dogadajZaTW=list.get(i);
                    TextView tw = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,                                                         // Sirina of TextView
                            LinearLayout.LayoutParams.WRAP_CONTENT);                                                        // Visina of TextView
                    tw.setLayoutParams(params);
                    params.setMargins(48, 0, 48, 20);
                    tw.setTextSize(20);
                    tw.setPadding(10,10,10,10);
                    tw.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.Crna)));
                    if (dogadajZaTW.contains("Crvena")){
                        tw.setBackgroundDrawable(getResources().getDrawable(R.color.Crvena));
                        String bezBoje = dogadajZaTW.replace("Crvena","");
                        tw.setText(bezBoje);
                    }
                    else if (dogadajZaTW.contains("Zelena")){
                        tw.setBackgroundDrawable(getResources().getDrawable(R.color.Zelena));
                        String bezBoje = dogadajZaTW.replace("Zelena","");
                        tw.setText(bezBoje);
                    }
                    else if (dogadajZaTW.contains("Žuta")){
                        tw.setBackgroundDrawable(getResources().getDrawable(R.color.Žuta));
                        String bezBoje = dogadajZaTW.replace("Žuta","");
                        tw.setText(bezBoje);
                    }
                    else{
                        tw.setBackgroundDrawable(getResources().getDrawable(R.color.Plava));
                        String bezBoje = dogadajZaTW.replace("Plava","");
                        tw.setText(bezBoje);
                    }
                    mainLinear.addView(tw);
                }

            }
        }.start();
    }*/
}
