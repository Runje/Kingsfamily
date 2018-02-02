package blue.koenig.kingsfamilylibrary.view.family

import android.app.AlertDialog
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import blue.koenig.kingsfamilylibrary.R
import blue.koenig.kingsfamilylibrary.model.family.FamilyModel
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

/**
 * Created by Thomas on 18.10.2017.
 */

abstract class FamilyActivity : AppCompatActivity(), FamilyView {

    protected var connectionStatus: TextView? = null
    protected var logger = LoggerFactory.getLogger(javaClass.simpleName)

    protected lateinit var model: FamilyModel

    override fun showText(text: String) {
        runOnUiThread { Toast.makeText(this@FamilyActivity, text, Toast.LENGTH_LONG).show() }

    }

    override fun showText(@StringRes stringResource: Int) {
        runOnUiThread { Toast.makeText(this@FamilyActivity, stringResource, Toast.LENGTH_LONG).show() }

    }

    override fun showConnectionStatus(connected: Boolean) {
        if (connectionStatus == null) {
            return
        }
        runOnUiThread {
            if (connected) {
                connectionStatus!!.setText(R.string.connected)
                connectionStatus!!.setTextColor(resources.getColor(R.color.green))
            } else {
                connectionStatus!!.setText(R.string.not_connected)
                connectionStatus!!.setTextColor(resources.getColor(R.color.red))
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = createModel()
    }

    protected abstract fun createModel(): FamilyModel

    override fun askForNameOrImport() {
        runOnUiThread {
            val dialog = CreateUserDialog(this@FamilyActivity, object : CreateUserDialog.CreateUserListener {
                override fun onCreateUser(userName: String, birthday: DateTime) {
                    model.onCreatingUser(userName, birthday)
                }

                override fun onImportUser(userId: String) {
                    model.importUser(userId)

                }
            })
            dialog.show()
        }

    }

    override fun askJoinOrCreateFamily() {
        runOnUiThread {
            val builder = AlertDialog.Builder(this@FamilyActivity)
            builder.setTitle(R.string.Name)
            //builder.setMessage(R.string.enter_name_or_import);
            val layout = LayoutInflater.from(this@FamilyActivity).inflate(R.layout.enter_name, null)
            builder.setView(layout)
            val editName = layout.findViewById<EditText>(R.id.editTextName)
            builder.setPositiveButton(R.string.createFamily) { dialog, which ->
                val name = editName.text.toString()
                model.createFamily(name)
            }

            builder.setNegativeButton(R.string.joinFamily) { dialog, which ->
                val name = editName.text.toString()
                model.joinFamily(name)
            }
            builder.show()
        }

    }

    override fun onStart() {
        logger.info("OnStart")
        super.onStart()
        model.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        logger.info("OnStop")
        model.detachView()
    }
}
