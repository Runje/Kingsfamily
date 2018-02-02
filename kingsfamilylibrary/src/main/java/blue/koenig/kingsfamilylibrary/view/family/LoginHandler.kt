package blue.koenig.kingsfamilylibrary.view.family

import android.content.Context
import blue.koenig.kingsfamilylibrary.model.FamilyConfig
import blue.koenig.kingsfamilylibrary.model.Utils
import blue.koenig.kingsfamilylibrary.model.communication.ConnectionEventListener
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection
import com.koenig.commonModel.User
import com.koenig.communication.messages.FamilyMessage
import com.koenig.communication.messages.TextMessage
import com.koenig.communication.messages.family.FamilyMemberMessage
import com.koenig.communication.messages.family.FamilyTextMessages
import com.koenig.communication.messages.family.UserMessage
import org.slf4j.LoggerFactory

/**
 * Created by Thomas on 01.11.2017.
 */

class LoginHandler(internal var connection: ServerConnection, private val context: Context) : ConnectionEventListener {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    var isLogin = false
        private set
    private var listener: LoginListener? = null
    var isLoggingIn: Boolean = false
    var members: List<User>? = null
        private set

    init {
        connection.addOnConnectionEventListener(this)
        members = FamilyConfig.getFamilyMembers(context)
    }

    protected fun processFamilyCommand(words: Array<String>) {
        when (words[0]) {
            FamilyTextMessages.LOGIN_FAIL -> {
                logger.error("Login failed")
                isLogin = false
                isLoggingIn = false
                if (listener != null) listener!!.onLoginFailed()
            }


            FamilyTextMessages.GET_FAMILY_MEMBER_FAIL -> {
                logger.error("Get members failed")
                if (listener != null) listener!!.onFamilyMembersFailed()
            }
        }
    }

    override fun onConnectionStatusChange(connected: Boolean) {
        if (!connected) {
            logger.info("Disconnected")
            isLogin = false
            isLoggingIn = false
        } else {
            logger.info("Connected")
            isLogin = false
            isLoggingIn = false
            login()
        }
    }

    override fun onReceiveMessage(message: FamilyMessage) {
        val messageName = message.name
        //logger.info("Received message: " + messageName);
        //logger.info("Component: " + message.getComponent());
        if (messageName == TextMessage.NAME) {
            val textMessage = message as TextMessage
            //logger.info("Received text: " + textMessage.getText());
            val words = textMessage.text.split(FamilyMessage.SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            processFamilyCommand(words)
        }

        when (message.name) {

            UserMessage.NAME -> {
                val userMessage = message as UserMessage
                userInfo(userMessage.user)
            }

            FamilyMemberMessage.NAME -> {
                val familyMemberMessage = message as FamilyMemberMessage
                members = familyMemberMessage.members
                if (listener != null) {
                    listener!!.onFamilyMembers(familyMemberMessage.members)
                }
                FamilyConfig.setFamilyMembers(context, members!!)
                isLogin = true
                isLoggingIn = false
                if (listener != null) listener!!.onLogin()
            }
        }
    }

    private fun userInfo(user: User) {
        Utils.setUserId(connection, context, user.id)

        if (user.family.isEmpty()) {
            // no family
            logger.info("User has no family")
            isLogin = false
            isLoggingIn = false
            if (listener != null) listener!!.onNoFamily()
        } else {

            // login successfully, get all family members
            connection.sendFamilyMessage(FamilyTextMessages.getFamilyMemberMessage())
        }
    }

    fun login() {
        if (isLoggingIn) {
            logger.info("Already logging in")
            return
        }

        if (!connection.isConnected) {
            logger.info("Can not login because there is no connection to the server")
            return
        }

        logger.info("Login from LoginHandler")
        isLoggingIn = true
        val userId = FamilyConfig.getUserId(context)
        if (userId != FamilyConfig.NO_ID) {
            Utils.setUserId(connection, context, userId)
            connection.sendFamilyMessage(FamilyTextMessages.loginMessage())
        } else {
            logger.info("No id for user found")
            isLoggingIn = false
            isLogin = false
            listener!!.onLoginFailed()
        }
    }

    fun setLoginListener(onLoginListener: LoginListener) {
        this.listener = onLoginListener
    }

    interface LoginListener {
        fun onLogin()

        fun onLoginFailed()

        fun onNoFamily()

        fun onFamilyMembers(members: List<User>)

        fun onFamilyMembersFailed()
    }
}
