package com.example.remindex;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;

public class NoviZadatakActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    String bojaFaba=""; //Kreiramo globalnu varijablu koja odreduje boju FAB-a kako bi ju mogli slati
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novi_zadatak);

        EditText editTextDatum = (EditText) findViewById(R.id.editTextDatum);
        EditText editTextVrijeme = (EditText) findViewById(R.id.editTextVrijeme);
        editTextDatum.setKeyListener(null);
        editTextVrijeme.setKeyListener(null);

        editTextDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment odabirDatuma = new OdabirDatuma();
                odabirDatuma.show(getSupportFragmentManager(), "Odabir datuma");
            }
        });

        editTextVrijeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment odabirVremena = new OdabirVremena();
                odabirVremena.show(getSupportFragmentManager(), "Odabir vremena");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int godina, int mjesec, int dan) {
        Calendar kalendar = Calendar.getInstance();
        kalendar.set(Calendar.YEAR, godina);
        kalendar.set(Calendar.MONTH, mjesec);
        kalendar.set(Calendar.DAY_OF_MONTH, dan);
        String odabraniDatumString = DateFormat.getDateInstance(DateFormat.FULL).format(kalendar.getTime());
        EditText editTextDatum= (EditText) findViewById(R.id.editTextDatum);
        editTextDatum.setText(odabraniDatumString);
    }

    @Override
    public void onTimeSet(TimePicker view, int sati, int minute) {
        EditText EditTextVrijeme = (EditText)findViewById(R.id.editTextVrijeme);
        if(sati<10 && minute<10){
            EditTextVrijeme.setText("0"+sati+" : 0"+minute);   // Rijesavamo problem npr. 4:5 -> 04:05
        }
        else if(sati<10 && minute>9){
            EditTextVrijeme.setText("0"+sati+" : "+minute);
        }
        else if(sati>9 && minute<10){
            EditTextVrijeme.setText(""+sati+" : 0"+minute);
        }
        else{
            EditTextVrijeme.setText(""+sati+" : "+minute);
        }
    }
    public void odaberiBoju(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.odabir_boja, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FloatingActionButton fabBoja = (FloatingActionButton) findViewById(R.id.fabBoja);
                switch (which) {
                    case 0:
                        fabBoja.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Crvena)));
                        bojaFaba="Crvena";
                        break;
                    case 1:
                        fabBoja.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Zelena)));
                        bojaFaba="Zelena";
                        break;
                    case 2:
                        fabBoja.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Plava)));
                        bojaFaba="Plava";
                        break;
                    case 3:
                        fabBoja.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Žuta)));
                        bojaFaba="Žuta";
                        break;
                }
            }
        });
        builder.show();
    }
    // Problem visestrukih dodavanja bi se mogo rjesiti s boolom mozda***************************************
    public void spremiZadatak(View view){
        EditText editTextNazivDogadaja = (EditText) findViewById(R.id.editTextNazivDogadaja); //Prijenos podataka
        EditText editTextDatum = (EditText) findViewById(R.id.editTextDatum);                 //Prijenos podataka
        EditText editTextVrijeme = (EditText) findViewById(R.id.editTextVrijeme);             //Prijenos podataka
        FloatingActionButton fabBoja = (FloatingActionButton) findViewById(R.id.fabBoja);     //Prijenos podataka

        String nazivDogadaja = editTextNazivDogadaja.getText().toString();  //Prijenos podataka
        String datum = editTextDatum.getText().toString();                  //Prijenos podataka
        String vrijeme = editTextVrijeme.getText().toString();              //Prijenos podataka

        Intent intentDogadaj = new Intent(this, MainActivity.class); //Prijenos podataka
        intentDogadaj.putExtra("Naziv_dogadaja", nazivDogadaja);             //Prijenos podataka
        intentDogadaj.putExtra("Datum", datum);                              //Prijenos podataka
        intentDogadaj.putExtra("Vrijeme", vrijeme);                          //Prijenos podataka
        intentDogadaj.putExtra("Boja", bojaFaba);                             //Boja Dogadaja
        startActivity(intentDogadaj);                                              //Prijenos podataka
    }
}
