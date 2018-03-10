package blue.koenig.kingsfamilylibrary.view.family

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import blue.koenig.kingsfamilylibrary.R
import blue.koenig.kingsfamilylibrary.model.Utils
import blue.koenig.kingsfamilylibrary.view.BirthdayPickerFragment
import org.joda.time.DateTime

/**
 * Created by Thomas on 13.10.2017.
 */

internal class CreateUserDialog(private val context: Context, private val listener: CreateUserListener) {
    private var createUser = true

    fun show() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.Name)
        builder.setMessage(R.string.enter_name_or_id)
        val layout = LayoutInflater.from(context).inflate(R.layout.create_user_dialog, null)
        builder.setView(layout)
        val editName = layout.findViewById<EditText>(R.id.editTextName)
        val editBirthday = layout.findViewById<EditText>(R.id.editBirthday)
        editBirthday.setOnClickListener {
            val birthdayPickerFragment = BirthdayPickerFragment()
            birthdayPickerFragment.setListener {
                editBirthday.setText(Utils.localDateToString(it))
            }

            birthdayPickerFragment.show(context)
        }
        // TODO: only enable button on entering username, birhtday / ID
        builder.setPositiveButton(R.string.ok) { _, _ ->
            val name = editName.text.toString()
            if (createUser) {
                listener.onCreateUser(name, Utils.stringToDateTime(editBirthday.text.toString()))
            } else {
                // name = Id
                listener.onImportUser(name)
            }
        }

        val dialog = builder.create()

        val group = layout.findViewById<RadioGroup>(R.id.radiogroup)
        group.setOnCheckedChangeListener { _, i ->
            if (i == R.id.radio_create) {
                createUser = true
                editName.setHint(R.string.name)
                editBirthday.visibility = View.VISIBLE

            } else {
                createUser = false
                editName.setHint(R.string.user_id)
                editBirthday.visibility = View.INVISIBLE
            }
        }

        dialog.show()

    }

    interface CreateUserListener {
        fun onCreateUser(userName: String, birthday: DateTime)

        fun onImportUser(userId: String)
    }
}
