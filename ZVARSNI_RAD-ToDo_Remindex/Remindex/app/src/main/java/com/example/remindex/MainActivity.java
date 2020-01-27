package com.example.remindex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EventiDBHelper dbHelper;
    int RBzaBrisanje;
    int RBBrojac;
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
        SharedPreferences spremljeniRB = getSharedPreferences(SPREMANJE_RB,0);      //ucitavamo RB iz datoteke
        RBBrojac = spremljeniRB.getInt("spremljeniRB",RBBrojac);                     //ucitavamo RB iz datoteke

        ispis();   // nakon 1 sekunde ispis svih dogadaja

        FloatingActionButton fabDodaj = findViewById(R.id.fabDodaj);                                                    //Inicijalizacija FAB-a
        fabDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                                            //Prilikom pritiska na FAB NoviZadatak...
                IdiNaNoviZadatakActivity();                                                                             //Pozivamo metodu IdiNaNoviActivity()
            }
        });
        FloatingActionButton fabBrisi = findViewById(R.id.fabBrisi);                                              //Inicijalizacija FAB-a
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
            //Inicijalizacija LinearLayouta za dinamicko stvaranje TW-a
        /*
            LinearLayout mainLinear = (LinearLayout) findViewById(R.id.main_linear);
            Intent intentDogadaj = getIntent();                                                                          // Provjeravamo je li poslan intent (kako bi izbjegli NULL u poruci), te kreiramo
            if(intentDogadaj.getExtras()!=null){
                // novi tw samo ako smo na drugom activityju aktivirali intent
                String nazivDogadaja = intentDogadaj.getStringExtra("Naziv_dogadaja");
                String datum = intentDogadaj.getStringExtra("Datum");
                String vrijeme = intentDogadaj.getStringExtra("Vrijeme");
                String bojaFaba = intentDogadaj.getStringExtra("Boja");
                String poruka=nazivDogadaja+"\n"+datum+"\n"+vrijeme;

                //Dinamicko stvaranje TW-a - RADI, samo na jednom i bez mijenjanja boje
                TextView tw = new TextView(getApplicationContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,                                                         // Sirina of TextView
                        LinearLayout.LayoutParams.WRAP_CONTENT);                                                        // Visina of TextView
                tw.setLayoutParams(params);
                params.setMargins(48, 0, 48, 5);
                tw.setTextSize(20);
                tw.setPadding(10,10,10,10);
                tw.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.Crna)));
                if("Crvena".equals(bojaFaba))                                                                            //Koristimo .equals posto je String u pitanju (usporedivanje charactera na pozicijama)
                    tw.setBackgroundDrawable(getResources().getDrawable(R.color.Crvena));
                else if("Žuta".equals(bojaFaba)){
                    tw.setBackgroundDrawable(getResources().getDrawable(R.color.Žuta));
                }
                else if("Zelena".equals(bojaFaba)){
                    tw.setBackgroundDrawable(getResources().getDrawable(R.color.Zelena));
                }
                else{
                    tw.setBackgroundDrawable(getResources().getDrawable(R.color.Plava));
                }
                tw.setText(poruka);
                mainLinear.addView(tw);
            }
            */
/*
        LinearLayout mainLinear = (LinearLayout) findViewById(R.id.main_linear);
        StringBuffer finalData = new StringBuffer();
        Cursor cursor = dbHelper.getAllRecords();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            finalData.append(cursor.getInt(cursor.getColumnIndex((EventiDBHelper.REDNI_BROJ))));
            finalData.append("\n");
            finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.NAZIV))));
            finalData.append(", ");
            finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.DATUM))));
            finalData.append(", ");
            finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.VRIJEME))));
            finalData.append(", ");
            finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.BOJA))));
            finalData.append("\n");
            TextView tw = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,                                                         // Sirina of TextView
                    LinearLayout.LayoutParams.WRAP_CONTENT);                                                        // Visina of TextView
            tw.setLayoutParams(params);
            params.setMargins(48, 0, 48, 5);
            tw.setTextSize(20);
            tw.setPadding(10,10,10,10);
            tw.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.Crna)));
            if("Crvena".equals(cursor.getString(cursor.getColumnIndex(EventiDBHelper.BOJA)))){
                tw.setBackgroundDrawable(getResources().getDrawable(R.color.Crvena));                                                                    //Koristimo .equals posto je String u pitanju (usporedivanje charactera na pozicijama)
            }
            else if("Žuta".equals(cursor.getColumnIndex((EventiDBHelper.BOJA)))){
                tw.setBackgroundDrawable(getResources().getDrawable(R.color.Žuta));
            }
            else if("Zelena".equals(cursor.getColumnIndex((EventiDBHelper.BOJA)))){
                tw.setBackgroundDrawable(getResources().getDrawable(R.color.Zelena));
            }
            else{
                tw.setBackgroundDrawable(getResources().getDrawable(R.color.Plava));
            }
            tw.setText(finalData);
            mainLinear.addView(tw);
        }*/
    }
    //samo test, dole je working stuff
    public void spremiRetkeUArrayList(){
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
    //klasa test
    public class Dogadaj{
        public int RB;
        public String nazivDogadaja;
        public String datumDogadaja;
        public String vrijemeDogadaja;
        public String boja;
        Dogadaj(int _RB, String _nazivDogadaja, String _datumDogadaja, String _vrijemeDogadaja, String _boja){
            this.RB=_RB;
            this.nazivDogadaja=_nazivDogadaja;
            this.datumDogadaja=_datumDogadaja;
            this.vrijemeDogadaja=_vrijemeDogadaja;
            this.boja=_boja;
        }
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

                    while (cursor.isAfterLast() == false) {
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

        /*
        StringBuffer finalData = new StringBuffer();
        TextView tw =(TextView)findViewById(R.id.textView);
        Cursor cursor = dbHelper.getAllRecords();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            finalData.append(cursor.getInt(cursor.getColumnIndex((EventiDBHelper.REDNI_BROJ))));
            finalData.append("\n");
            finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.NAZIV))));
            finalData.append(", ");
            finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.DATUM))));
            finalData.append(", ");
            finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.VRIJEME))));
            finalData.append(", ");
            finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.BOJA))));
            finalData.append("\n");
        }
        tw.setText(finalData);
        */
        /*
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                LinearLayout mainLinear = (LinearLayout) findViewById(R.id.main_linear);
                mainLinear.removeAllViews(); // Refreshamo LinearLayout, inace bi se samo nadopunjavalo s tw-evima
                StringBuffer finalData = new StringBuffer();
                Cursor cursor = dbHelper.getAllRecords();
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    finalData.append(cursor.getInt(cursor.getColumnIndex((EventiDBHelper.REDNI_BROJ))));
                    finalData.append("\n");
                    finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.NAZIV))));
                    finalData.append("\n");
                    finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.DATUM))));
                    finalData.append("\n");
                    finalData.append(cursor.getString(cursor.getColumnIndex((EventiDBHelper.VRIJEME))));
                    finalData.append("\n");
                    TextView tw = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,                                                         // Sirina of TextView
                            LinearLayout.LayoutParams.WRAP_CONTENT);                                                        // Visina of TextView
                    tw.setLayoutParams(params);
                    params.setMargins(48, 0, 48, 5);
                    tw.setTextSize(20);
                    tw.setPadding(10,10,10,10);
                    tw.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.Crna)));
                    if("Crvena".equals(cursor.getString(cursor.getColumnIndex(EventiDBHelper.BOJA)))){
                        tw.setBackgroundDrawable(getResources().getDrawable(R.color.Crvena));                               //Koristimo .equals posto je String u pitanju (usporedivanje charactera na pozicijama)
                    }
                    else if("Žuta".equals(cursor.getColumnIndex((EventiDBHelper.BOJA)))){
                        tw.setBackgroundDrawable(getResources().getDrawable(R.color.Žuta));
                    }
                    else if("Zelena".equals(cursor.getColumnIndex((EventiDBHelper.BOJA)))){
                        tw.setBackgroundDrawable(getResources().getDrawable(R.color.Zelena));
                    }
                    else{
                        tw.setBackgroundDrawable(getResources().getDrawable(R.color.Plava));
                    }
                    tw.setText(finalData);
                    mainLinear.addView(tw);
                }
            }
        }.start();
        */
    }
    private void IdiNaNoviZadatakActivity(){                                                                            //Stvaramo metodu IdiNaNoviZadatakActivity() koja pokreće drugi Activity
        Intent NoviZadatakIntent=new Intent(MainActivity.this, NoviZadatakActivity.class);                //Koristimo Intent
        startActivity(NoviZadatakIntent);
    }
}
