package com.example.remindex;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class OdabirDatuma extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar kalendar = Calendar.getInstance();
        int godina = kalendar.get(Calendar.YEAR);                                       //Spremamo trenutno vrijeme koje ćemo koristiti kao konstruktor
        int mjesec = kalendar.get(Calendar.MONTH);
        int dan = kalendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), godina, mjesec, dan);
    }
}
