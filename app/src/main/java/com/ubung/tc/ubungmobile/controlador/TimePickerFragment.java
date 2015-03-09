package com.ubung.tc.ubungmobile.controlador;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ubung.tc.ubungmobile.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Stevenson on 08/03/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int minuto;
    private int hora;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hora=hourOfDay;
        minuto=minute;

        EditText time=(EditText)getActivity().findViewById(R.id.hour_picker);
        time.setText(hourOfDay+":"+minute);

    }
}