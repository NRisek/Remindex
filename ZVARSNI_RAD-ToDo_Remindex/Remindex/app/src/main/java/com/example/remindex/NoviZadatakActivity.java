package com.example.remindex;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoviZadatakActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    //Date notifiDatum;
    EventiDBHelper dbHelper;
    int RBAlarma=1;  //RBAlarma, sluzi za identifikaciju ALARMA i RBDogadaja u isto vrijeme
    public static final String SPREMANJE_RB = "SpremljeniRB";   //spremamo RB u lokalnu datoteku kako se ne bi resetirala pri gasenju aplikacije
    int notifiGodina;
    int notifiMjesec;
    int notifiDan;
    int notifiSat=0;
    int notifiMinute=0;
    long razlikaDatuma=0;  //sluzi za AlarmManager
    String bojaFaba=""; //Kreiramo globalnu varijablu koja odreduje boju FAB-a kako bi ju mogli slati

    //test
    Calendar vrijemeOkidanjaObavijesti = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novi_zadatak);
        dbHelper=new EventiDBHelper(NoviZadatakActivity.this);
        SharedPreferences spremljeniRB = getSharedPreferences(SPREMANJE_RB,0);      //ucitavamo RB iz datoteke
        RBAlarma = spremljeniRB.getInt("spremljeniRB",RBAlarma);                     //ucitavamo RB iz datoteke

        //Ukoliko zelimo resetirati RB, zovemo fju resetRB() u onCreate

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
    public void onDateSet(DatePicker view, int godina, int mjesec, int dan) {
        Calendar kalendar = Calendar.getInstance();  //Trenutni datum
        kalendar.set(Calendar.YEAR, godina);
        kalendar.set(Calendar.MONTH, mjesec);
        kalendar.set(Calendar.DAY_OF_MONTH, dan);
        String datumString = DateFormat.getDateInstance(DateFormat.FULL).format(kalendar.getTime());
        EditText editTextDatum= (EditText) findViewById(R.id.editTextDatum);
        editTextDatum.setText(datumString);
        notifiGodina=kalendar.get(Calendar.YEAR);
        notifiMjesec=kalendar.get(Calendar.MONTH);
        notifiDan=kalendar.get(Calendar.DAY_OF_MONTH);
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
        notifiSat=sati;
        notifiMinute=minute;
    }

    public void radio1Dan(View view){
        razlikaDatuma=0;  //refresh ako se vise puta stisne
        Calendar trenutno = Calendar.getInstance();
        Calendar notifiiii=Calendar.getInstance();
        trenutno.set(trenutno.get(Calendar.YEAR), trenutno.get(Calendar.MONTH), trenutno.get(Calendar.DAY_OF_MONTH), trenutno.get(Calendar.HOUR_OF_DAY), trenutno.get(Calendar.MINUTE));
        notifiiii.set(notifiGodina, notifiMjesec, (notifiDan-1), notifiSat, notifiMinute);

        Date trenutnoVrijeme = trenutno.getTime();
        Date vrijemeNotifiiii = notifiiii.getTime();
        long pocetak=trenutnoVrijeme.getTime();
        long zavrsetak = vrijemeNotifiiii.getTime();
        long razlika = zavrsetak-pocetak; //vraca milisekunde od 1970
        long razlikaUSekundama=razlika/1000;  //to dela, vrne tocno vreme v sekundama od trenutnoga do odabranoga datuma + vreme
        razlikaDatuma=razlikaUSekundama*1000;

    }

    public void radio1Sat(View view){
        razlikaDatuma=0;  //refresh ako se vise puta stisne
        Calendar trenutno = Calendar.getInstance();
        Calendar notifiiii=Calendar.getInstance();
        trenutno.set(trenutno.get(Calendar.YEAR), trenutno.get(Calendar.MONTH), trenutno.get(Calendar.DAY_OF_MONTH), trenutno.get(Calendar.HOUR_OF_DAY), trenutno.get(Calendar.MINUTE));
        notifiiii.set(notifiGodina, notifiMjesec, notifiDan, (notifiSat-1), notifiMinute);

        Date trenutnoVrijeme = trenutno.getTime();
        Date vrijemeNotifiiii = notifiiii.getTime();
        long pocetak=trenutnoVrijeme.getTime();
        long zavrsetak = vrijemeNotifiiii.getTime();
        long razlika = zavrsetak-pocetak; //vraca milisekunde od 1970
        long razlikaUSekundama=razlika/1000;  //to dela, vrne tocno vreme v sekundama od trenutnoga do odabranoga datuma + vreme
        razlikaDatuma=razlikaUSekundama*1000;
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
    public void spremiZadatak(View view){
        EditText editTextNazivDogadaja = (EditText) findViewById(R.id.editTextNazivDogadaja);
        EditText editTextDatum = (EditText) findViewById(R.id.editTextDatum);
        EditText editTextVrijeme = (EditText) findViewById(R.id.editTextVrijeme);
        RadioButton radioBtn1Dan = (RadioButton) findViewById(R.id.radioBtn1dan);
        String nazivDogadaja = editTextNazivDogadaja.getText().toString();
        String datum = editTextDatum.getText().toString();
        String vrijeme = editTextVrijeme.getText().toString();
        if(radioBtn1Dan.isChecked()){
            long unos = dbHelper.insert(RBAlarma, nazivDogadaja, datum, vrijeme, bojaFaba, notifiGodina, notifiMjesec, (notifiDan-1), notifiSat, notifiMinute);
            /*
            if(unos==-1){
                Toast.makeText(NoviZadatakActivity.this, "Greška kod spremanja! ID vec postoji, kreirajte događaj još jednom.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(NoviZadatakActivity.this, "Uspjesno uneseno!, ID: "+RBAlarma, Toast.LENGTH_LONG).show();
            }
            */

            //Razlika se tu mora raditi (moguce da prođe vrijeme od postavljanja do spremanja)
            razlikaDatuma=0;  //refresh ako se vise puta stisne
            Calendar trenutno = Calendar.getInstance();
            Calendar notifiiii=Calendar.getInstance();
            trenutno.set(trenutno.get(Calendar.YEAR), trenutno.get(Calendar.MONTH), trenutno.get(Calendar.DAY_OF_MONTH), trenutno.get(Calendar.HOUR_OF_DAY), trenutno.get(Calendar.MINUTE));
            notifiiii.set(notifiGodina, notifiMjesec, (notifiDan-1), notifiSat, notifiMinute);

            Date trenutnoVrijeme = trenutno.getTime();
            Date vrijemeNotifiiii = notifiiii.getTime();
            long pocetak=trenutnoVrijeme.getTime();
            long zavrsetak = vrijemeNotifiiii.getTime();
            long razlika = zavrsetak-pocetak; //vraca milisekunde od 1970
            long razlikaUSekundama=razlika/1000;  //to dela, vrne tocno vreme v sekundama od trenutnoga do odabranoga datuma + vreme
            //razlikaDatuma=razlikaUSekundama;
            razlikaDatuma=razlikaUSekundama*1000;

            //test
            vrijemeOkidanjaObavijesti.set(Calendar.YEAR, notifiGodina);
            vrijemeOkidanjaObavijesti.set(Calendar.MONTH, notifiMjesec);
            vrijemeOkidanjaObavijesti.set(Calendar.DAY_OF_MONTH, (notifiDan-1));
            vrijemeOkidanjaObavijesti.set(Calendar.HOUR_OF_DAY, notifiSat);
            vrijemeOkidanjaObavijesti.set(Calendar.MINUTE, notifiMinute);
            vrijemeOkidanjaObavijesti.set(Calendar.SECOND,0);
        }
        else{
            long unos = dbHelper.insert(RBAlarma, nazivDogadaja, datum, vrijeme, bojaFaba, notifiGodina, notifiMjesec, notifiDan, (notifiSat-1), notifiMinute);
            // -1 znaci da se nije insertalo
            /*
            if(unos==-1){
                Toast.makeText(NoviZadatakActivity.this, "Greška kod spremanja! ID vec postoji, kreirajte događaj još jednom.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(NoviZadatakActivity.this, "Uspjesno uneseno!, ID: "+RBAlarma, Toast.LENGTH_LONG).show();
            }
            */

            //Drugi slucaj alarma
            razlikaDatuma=0;  //refresh ako se vise puta stisne
            Calendar trenutno = Calendar.getInstance();
            Calendar notifiiii=Calendar.getInstance();
            trenutno.set(trenutno.get(Calendar.YEAR), trenutno.get(Calendar.MONTH), trenutno.get(Calendar.DAY_OF_MONTH), trenutno.get(Calendar.HOUR_OF_DAY), trenutno.get(Calendar.MINUTE));
            notifiiii.set(notifiGodina, notifiMjesec, notifiDan, (notifiSat-1), notifiMinute);

            Date trenutnoVrijeme = trenutno.getTime();
            Date vrijemeNotifiiii = notifiiii.getTime();
            long pocetak=trenutnoVrijeme.getTime();
            long zavrsetak = vrijemeNotifiiii.getTime();
            long razlika = zavrsetak-pocetak; //vraca milisekunde od 1970
            long razlikaUSekundama=razlika/1000;  //to dela, vrne tocno vreme v sekundama od trenutnoga do odabranoga datuma + vreme
            //razlikaDatuma=razlikaUSekundama;
            razlikaDatuma=razlikaUSekundama*1000;

            //test
            vrijemeOkidanjaObavijesti.set(Calendar.YEAR, notifiGodina);
            vrijemeOkidanjaObavijesti.set(Calendar.MONTH, notifiMjesec);
            vrijemeOkidanjaObavijesti.set(Calendar.DAY_OF_MONTH, notifiDan);
            vrijemeOkidanjaObavijesti.set(Calendar.HOUR_OF_DAY, (notifiSat-1));
            vrijemeOkidanjaObavijesti.set(Calendar.MINUTE, notifiMinute);
            vrijemeOkidanjaObavijesti.set(Calendar.SECOND,0);
        }

        //startAlarm(razlikaDatuma);
        startAlarm(vrijemeOkidanjaObavijesti);

        Intent intentVratiSe = new Intent(this, MainActivity.class);
        startActivity(intentVratiSe);
    }

    private void startAlarm(Calendar vrijemeZaObavijest){
        EditText etTekstDogadjaja= (EditText) findViewById(R.id.editTextNazivDogadaja);
        //Calendar calendarDanas = Calendar.getInstance();  //vraca milisekunde od 1.1.1970.  //Prije sam radio danas+razlika
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);                  //NA MOBITELU SE NE CRAHA, A TU SE CRASHA
        intent.putExtra("tekstDogađaja", (etTekstDogadjaja.getText().toString()));      //NA MOBITELU SE NE CRAHA, A TU SE CRASHA

        //requestCode mora stalno biti drugaciji, za svaki alarm - RBAlarma
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, RBAlarma, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, vrijemeZaObavijest.getTimeInMillis() , pendingIntent);   //U build.gradle je api s 15 stavljen na 19 za setExact

        RBAlarma++;
        SharedPreferences spremljeniRB = getSharedPreferences(SPREMANJE_RB,0);  //spremamo RB u datoteku
        SharedPreferences.Editor editor=spremljeniRB.edit();                          //spremamo RB u datoteku
        editor.putInt("spremljeniRB",RBAlarma);                                       //spremamo RB u datoteku
        editor.commit();
    }

    /*private void startAlarm(long razlikaUMilisekundama){
        Calendar calendarDanas = Calendar.getInstance();  //vraca milisekunde od 1.1.1970.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        //requestCode mora stalno biti drugaciji, za svaki alarm - RBAlarma
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, RBAlarma, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, (calendarDanas.getTimeInMillis()+razlikaUMilisekundama), pendingIntent);   //U build.gradle je api s 15 stavljen na 19

        RBAlarma++;
        SharedPreferences spremljeniRB = getSharedPreferences(SPREMANJE_RB,0);  //spremamo RB u datoteku
        SharedPreferences.Editor editor=spremljeniRB.edit();                          //spremamo RB u datoteku
        editor.putInt("spremljeniRB",RBAlarma);                                       //spremamo RB u datoteku
        editor.commit();
    }*/
}


