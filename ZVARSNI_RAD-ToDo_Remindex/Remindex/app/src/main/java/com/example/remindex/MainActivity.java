package com.example.remindex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EventiDBHelper dbHelper;
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
        dbHelper.closeDB();
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

        pomocuKlase(); // Ispis svih dogadaja nakon 200 milisekundi

        FloatingActionButton fabDodaj = findViewById(R.id.fabDodaj);   //Inicijalizacija FAB-a
        fabDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdiNaNoviZadatakActivity();
            }
        });
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
        new CountDownTimer(200,200){
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                LinearLayout mainLinear = (LinearLayout) findViewById(R.id.main_linear);
                mainLinear.removeAllViews(); // Refreshamo LinearLayout, inace bi se samo nadopunjavalo tw-evima
                Cursor cursor = dbHelper.getAllRecords(); // "Šetanje po bazi"
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
                            LinearLayout.LayoutParams.MATCH_PARENT,        // Sirina tw-a
                            LinearLayout.LayoutParams.WRAP_CONTENT);       // Visina tw-a
                    tw.setLayoutParams(params);
                    params.setMargins(48, 0, 48, 20);
                    tw.setTextSize(20);
                    tw.setPadding(10,10,10,10);
                    tw.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.Crna)));
                    tw.setBackgroundResource(R.drawable.zaobljeni_tw);   // Zaobljeni rubovi
                    GradientDrawable drawable = (GradientDrawable) tw.getBackground(); // Sluzi tome da se zaobljeni rubovi i boja tw-a ne "prebrišu"
                    if(("Zelena").equals(dogadaji.get(i).boja)){  // Koristim .equals jer je bolje za String, gleda se svaki character, s == ne radi...
                        drawable.setColor(getResources().getColor(R.color.Zelena)); // Ucitavanje "custom" boje
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
                    final int redniBrojZaBrisanje=dogadaji.get(i).RB; // Spremamo RB dogadaja koji ce nam koristiti za brisanje
                    tw.setText(tekst);
                    mainLinear.addView(tw);
                    /*
                    tw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {    //listener za brisanje kod pritiska na dogadaj
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Jeste li sigurni da želite obrisati događaj?");
                            builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    long result = dbHelper.delete(redniBrojZaBrisanje);
                                    if(result==0){
                                        Toast.makeText(MainActivity.this, "Greška kod brisanja!", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Uspjesno obrisano!, ID: "+redniBrojZaBrisanje, Toast.LENGTH_LONG).show();
                                    }
                                    dogadaji.clear(); //brisemo trenutni array list
                                    pomocuKlase(); //ponovno zovemo funkciju koja ce "osvjeziti" listu
                                }
                            });
                            builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                    });
                    */
                    //ON LONG
                    tw.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {    //listener za brisanje kod pritiska na dogadaj
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Jeste li sigurni da želite obrisati događaj?");
                            builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    long result = dbHelper.delete(redniBrojZaBrisanje);
                                    if(result==0){
                                        Toast.makeText(MainActivity.this, "Greška kod brisanja!", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Uspjesno obrisano!, ID: "+redniBrojZaBrisanje, Toast.LENGTH_LONG).show();
                                    }
                                    dogadaji.clear(); //brisemo trenutni array list
                                    pomocuKlase(); //ponovno zovemo funkciju koja ce "osvjeziti" listu
                                }
                            });
                            builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                            return true;
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
}
