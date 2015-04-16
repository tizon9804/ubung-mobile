package com.ubung.tc.ubungmobile.controlador;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ubung.tc.ubungmobile.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Stevenson on 08/03/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private int año;
    private int mes;
    private int dia;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        año = year;
        mes = month;
        dia = day;
        Calendar cal = new GregorianCalendar(year, month, day);
        EditText date = (EditText) getActivity().findViewById(R.id.date_picker);
        date.setText(day + "/" + month + "/" + year);
    }
}