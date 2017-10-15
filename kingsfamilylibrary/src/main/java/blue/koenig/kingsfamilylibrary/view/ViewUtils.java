package blue.koenig.kingsfamilylibrary.view;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.DateTime;

import blue.koenig.kingsfamilylibrary.R;


/**
 * Created by Thomas on 13.10.2017.
 */

public class ViewUtils {
    public static DateTime getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        return new DateTime(year, month, day, 0, 0, 0);
    }

    public static void setDateToDatePicker(DatePicker datePicker, DateTime time) {
        int year = time.getYear();
        int month = time.getMonthOfYear() - 1;      // Need to subtract 1 here.
        int day = time.getDayOfMonth();

        datePicker.updateDate(year, month, day);
    }

    public static void getDateFromDialog(Context context, final DateListener dateListener) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        final View layout = LayoutInflater.from(context).inflate(R.layout.date_picker, null);
        builder.setView(layout);
        builder.setTitle(R.string.date);
        builder.setNegativeButton(R.string.cancel, null);
        final DatePicker datePicker = layout.findViewById(R.id.datePicker);
        ViewUtils.setDateToDatePicker(datePicker, DateTime.now());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DateTime date = ViewUtils.getDateFromDatePicker(datePicker);
                if (dateListener != null) {
                    dateListener.onDate(date);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    public static void getBirthdayFromDialog(Context context, final DateListener dateListener) {


        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        final View layout = LayoutInflater.from(context).inflate(R.layout.date_picker, null);
        builder.setView(layout);
        builder.setTitle(R.string.date);
        builder.setNegativeButton(R.string.cancel, null);
        final DatePicker datePicker = layout.findViewById(R.id.datePicker);
        ViewUtils.setDateToDatePicker(datePicker, DateTime.now());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DateTime date = ViewUtils.getDateFromDatePicker(datePicker);
                if (dateListener != null) {
                    dateListener.onDate(date);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    public interface DateListener {
        void onDate(DateTime dateTime);
    }
}
