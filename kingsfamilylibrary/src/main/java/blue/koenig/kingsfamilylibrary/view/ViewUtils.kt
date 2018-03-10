package blue.koenig.kingsfamilylibrary.view

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import blue.koenig.kingsfamilylibrary.R
import com.koenig.FamilyConstants
import org.joda.time.LocalDate


/**
 * Created by Thomas on 13.10.2017.
 */

object ViewUtils {
    fun getDateFromDatePicker(datePicker: DatePicker): LocalDate {
        val day = datePicker.dayOfMonth
        val month = datePicker.month + 1
        val year = datePicker.year

        return LocalDate(year, month, day)
    }

    fun setDateToDatePicker(datePicker: DatePicker, time: LocalDate) {
        val year = time.year
        val month = time.monthOfYear - 1      // Need to subtract 1 here.
        val day = time.dayOfMonth

        datePicker.updateDate(year, month, day)
    }

    fun getDateFromDialog(context: Context, dateListener: (LocalDate) -> Unit) {
        getDateFromDialogHelper(context, false, dateListener)
    }

    fun getUnlimitedDateFromDialog(context: Context, dateListener: (LocalDate) -> Unit) {
        getDateFromDialogHelper(context, true, dateListener)
    }

    private fun getDateFromDialogHelper(context: Context, unlimited: Boolean, dateListener: (LocalDate) -> Unit) {
        val builder = android.app.AlertDialog.Builder(context)
        val layout = LayoutInflater.from(context).inflate(R.layout.date_picker, null)
        builder.setView(layout)
        builder.setTitle(R.string.date)
        if (unlimited) {
            builder.setNeutralButton(R.string.unlimited) { _, _ -> dateListener.invoke(FamilyConstants.UNLIMITED) }
        }
        builder.setNegativeButton(R.string.cancel, null)
        val datePicker = layout.findViewById<DatePicker>(R.id.datePicker)
        ViewUtils.setDateToDatePicker(datePicker, LocalDate())
        builder.setPositiveButton("OK") { _, _ ->
            val date = ViewUtils.getDateFromDatePicker(datePicker)
            dateListener.invoke(date)
        }

        builder.setNegativeButton(R.string.cancel, null)
        builder.create().show()

    }


    fun getBirthdayFromDialog(context: Context, dateListener: (LocalDate) -> Unit) {

        val builder = android.app.AlertDialog.Builder(context)
        val layout = LayoutInflater.from(context).inflate(R.layout.date_picker, null)
        builder.setView(layout)
        builder.setTitle(R.string.date)
        builder.setNegativeButton(R.string.cancel, null)
        val datePicker = layout.findViewById<DatePicker>(R.id.datePicker)
        ViewUtils.setDateToDatePicker(datePicker, LocalDate())
        builder.setPositiveButton("OK") { _, _ ->
            val date = ViewUtils.getDateFromDatePicker(datePicker)
            dateListener.invoke(date)
        }

        builder.setNegativeButton(R.string.cancel, null)
        builder.create().show()
    }

    fun getScreenSize(activity: Activity): Point {
        return getScreenSize(activity.windowManager)
    }

    fun getScreenSize(manager: WindowManager): Point {
        val size = Point()
        val display = manager.defaultDisplay
        display.getSize(size)
        return size
    }

    fun getScreenWidth(activity: Activity): Int {
        return getScreenSize(activity).x
    }

    fun getScreenHeight(activity: Activity): Int {
        return getScreenSize(activity).y
    }

    fun clickOn(view: View) {
        view.postDelayed({
            view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
        }, 100)

    }


}
