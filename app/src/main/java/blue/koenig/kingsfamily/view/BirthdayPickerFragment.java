package blue.koenig.kingsfamily.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import org.joda.time.DateTime;

/**
 * Created by Thomas on 13.10.2017.
 */

public class BirthdayPickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    ViewUtils.DateListener listener;

    public void setListener(ViewUtils.DateListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, this, 1987, 5, 14);
    }

    public void onTimeSet(DatePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        if (listener != null) {
            listener.onDate(new DateTime(year, month + 1, day, 12, 0, 0));
        }
    }

    public void show(Context context) {
        show(((Activity) context).getFragmentManager(), "birthdayPicker");
    }
}