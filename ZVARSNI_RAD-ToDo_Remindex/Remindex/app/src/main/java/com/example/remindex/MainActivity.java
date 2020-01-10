package com.example.remindex;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabDodaj = findViewById(R.id.fabDodaj);                                                    //Inicijalizacija FAB-a
        fabDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                                            //Prilikom pritiska na FAB NoviZadatak...
                IdiNaNoviZadatakActivity();                                                                             //Pozivamo metodu IdiNaNoviActivity()

            }
        });
        FloatingActionButton fabKalendar = findViewById(R.id.fabKalendar);                                              //Inicijalizacija FAB-a
        fabKalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                                            //Prilikom pritiska na FAB Kalendar...
                Toast.makeText(getBaseContext(), "Otvoren kalendar." , Toast.LENGTH_SHORT ).show();

            }
        });
            //Inicijalizacija LinearLayouta za dinamicko stvaranje TW-a
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
                if("Žuta".equals(bojaFaba)){
                    tw.setBackgroundDrawable(getResources().getDrawable(R.color.Žuta));
                }
                if("Zelena".equals(bojaFaba)){
                    tw.setBackgroundDrawable(getResources().getDrawable(R.color.Zelena));
                }
                else{
                    tw.setBackgroundDrawable(getResources().getDrawable(R.color.Plava));
                }
                tw.setText(poruka);
                mainLinear.addView(tw);
            }
    }
    private void IdiNaNoviZadatakActivity(){                                                                            //Stvaramo metodu IdiNaNoviZadatakActivity() koja pokreće drugi Activity
        Intent NoviZadatakIntent=new Intent(MainActivity.this, NoviZadatakActivity.class);                //Koristimo Intent
        startActivity(NoviZadatakIntent);
    }
}
