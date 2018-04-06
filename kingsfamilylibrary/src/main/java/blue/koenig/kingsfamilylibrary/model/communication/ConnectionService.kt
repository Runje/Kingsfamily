package blue.koenig.kingsfamilylibrary.model.communication

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler
import com.koenig.communication.messages.FamilyMessage
import org.slf4j.LoggerFactory
import java.util.concurrent.ScheduledExecutorService

/**
 * Created by Thomas on 01.11.2017.
 */

class ConnectionService : Service(), ConnectionEventListener {
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    internal val mMessenger = Messenger(IncomingHandler())
    protected var logger = LoggerFactory.getLogger(javaClass.simpleName)
    var serverConnection: ServerConnection? = null
        private set
    private var service: ScheduledExecutorService? = null
    private var loginHandler: LoginHandler? = null
    /**@Inject
    lateinit var config: FamilyConfig**/

    override fun onConnectionStatusChange(connected: Boolean) {

    }

    override fun onReceiveMessage(message: FamilyMessage) {

    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder? {
        Log.d("ConnectionService", "On Bind")
        logger.info("OnBind")
        /**
        (application as FamilyApplication).familyAppComponent?.inject(this)

        serverConnection = ServerConnection(config.userIdObservable)
        serverConnection!!.addOnConnectionEventListener(this)
        service = Executors.newScheduledThreadPool(1)
        val tryConnect = Runnable {
        if (!serverConnection!!.isConnected) {
        serverConnection!!.connect()
        }
        }
        loginHandler = LoginHandler(serverConnection!!, config)

        service!!.scheduleAtFixedRate(tryConnect, 0, 1, TimeUnit.SECONDS)**/
        return mMessenger.binder
    }

    /**
     * Handler of incoming messages from clients.
     */
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            logger.info("Received message")
            when (msg.what) {
                MSG_SAY_HELLO -> Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    companion object {
        /**
         * Command to the service to display a message
         */
        internal val MSG_SAY_HELLO = 1
    }
}
