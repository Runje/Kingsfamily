package blue.koenig.kingsfamilylibrary.view

import android.app.*
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import org.joda.time.LocalDate

@Suppress("DEPRECATION")
/**
 * Created by Thomas on 13.10.2017.
 */

class BirthdayPickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    internal var listener: (LocalDate) -> Unit? = {}

    fun setListener(listener: (LocalDate) -> Unit) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {

        // Create a new instance of TimePickerDialog and return it
        return DatePickerDialog(activity, AlertDialog.THEME_HOLO_DARK, this, 1987, 5, 14)
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        listener.invoke(LocalDate(year, month + 1, day))
    }

    fun show(context: Context) {
        show((context as Activity).fragmentManager, "birthdayPicker")
    }
}