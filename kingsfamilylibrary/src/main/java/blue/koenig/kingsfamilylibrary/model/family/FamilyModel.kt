package blue.koenig.kingsfamilylibrary.model.family

import android.content.Context
import android.support.annotation.StringRes
import blue.koenig.kingsfamilylibrary.FamilyApplication
import blue.koenig.kingsfamilylibrary.R
import blue.koenig.kingsfamilylibrary.model.Utils
import blue.koenig.kingsfamilylibrary.model.communication.ConnectionEventListener
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection
import blue.koenig.kingsfamilylibrary.view.family.FamilyView
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler.LoginListener
import blue.koenig.kingsfamilylibrary.view.family.NullFamilyView
import com.koenig.commonModel.Component
import com.koenig.commonModel.User
import com.koenig.communication.messages.FamilyMessage
import com.koenig.communication.messages.TextMessage
import com.koenig.communication.messages.family.CreateUserMessage
import com.koenig.communication.messages.family.FamilyTextMessages
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by Thomas on 18.10.2017.
 */

abstract class FamilyModel(protected var connection: ServerConnection, protected var context: Context, protected var loginHandler: LoginHandler) : ConnectionEventListener, LoginListener {
    protected var logger = LoggerFactory.getLogger(javaClass.simpleName)
    protected var plugins: MutableList<Plugin> = ArrayList()
    protected var view: FamilyView = NullFamilyView()

    val familyMembers: List<User>?
        get() = loginHandler.members


    init {
        this.view = createNullView()
        connection.addOnConnectionEventListener(this)
        loginHandler.setLoginListener(this)
    }

    fun sendMessageToServer(message: FamilyMessage) {
        connection.sendFamilyMessage(message)
    }

    fun onCreatingUser(name: String, birthday: DateTime) {
        connection.sendFamilyMessage(CreateUserMessage(name, birthday))
    }

    fun createFamily(name: String) {
        connection.sendFamilyMessage(FamilyTextMessages.CreateFamilyMessage(name))
    }

    fun joinFamily(name: String) {
        connection.sendFamilyMessage(FamilyTextMessages.JoinFamilyMessage(name))
    }

    fun importUser(userId: String) {
        var userId = userId
        userId = userId.trim { it <= ' ' }
        setUserId(userId)
        connection.sendFamilyMessage(FamilyTextMessages.loginMessage())
    }

    private fun setUserId(userId: String) {
        Utils.setUserId(connection, context, userId)
    }

    override fun onLogin() {
        // do nothing --> On FamilyMembers is called
    }

    override fun onLoginFailed() {
        view.showText(R.string.login_failed)

        // try again
        view.askForNameOrImport()
    }

    override fun onNoFamily() {

    }

    override fun onFamilyMembers(members: List<User>) {
        updateFamilymembers(members)
        start()
    }

    override fun onFamilyMembersFailed() {
        logger.error("Get members failed")
        view.showText(R.string.get_familymembers_fail)
    }

    override fun onReceiveMessage(message: FamilyMessage) {
        logger.debug("Received message: " + message.toString())
        logger.debug("Component: " + message.component)
        var words = arrayOf<String>()
        var isTextMessage = false
        if (message.name == TextMessage.NAME) {
            val textMessage = message as TextMessage
            logger.info("Received text: " + textMessage.text)
            words = textMessage.text.split(FamilyMessage.SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            isTextMessage = true
        }

        when (message.component) {
            Component.FINANCE -> if (isTextMessage) {
                processFinanceCommand(words)
            } else {
                onReceiveFinanceMessage(message)
            }
            Component.CONTRACTS -> if (isTextMessage) {
                processContractsCommand(words)
            } else {
                onReceiveContractsMessage(message)
            }
            Component.OWNINGS -> if (isTextMessage) {
                processOwningsCommand(words)
            } else {
                onReceiveOwningsMessage(message)
            }
            Component.HEALTH -> if (isTextMessage) {
                processHealthCommand(words)
            } else {
                onReceiveHealthMessage(message)
            }
            Component.WIKI -> if (isTextMessage) {
                processWikiCommand(words)
            } else {
                onReceiveWikiMessage(message)
            }
            Component.FAMILY -> if (isTextMessage) {
                processFamilyCommand(words)
            } else {
                onReceiveFamilyMessage(message)
            }
            Component.WORK -> if (isTextMessage) {
                processWorkCommand(words)
            } else {
                onReceiveWorkMessage(message)
            }
            else -> logger.error("Unknown component")
        }

    }

    protected open fun onReceiveFinanceMessage(message: FamilyMessage) {
        // To be overridden in subclasses
    }

    protected fun onReceiveContractsMessage(message: FamilyMessage) {
        // To be overridden in subclasses
    }

    protected fun onReceiveHealthMessage(message: FamilyMessage) {
        // To be overridden in subclasses
    }

    protected fun onReceiveWikiMessage(message: FamilyMessage) {
        // To be overridden in subclasses
    }

    protected fun onReceiveWorkMessage(message: FamilyMessage) {
        // To be overridden in subclasses
    }

    protected fun onReceiveOwningsMessage(message: FamilyMessage) {
        // To be overridden in subclasses
    }

    protected fun onReceiveFamilyMessage(message: FamilyMessage) {
        when (message.name) {

        }
    }

    protected abstract fun updateFamilymembers(members: List<User>)


    protected abstract fun start()

    protected open fun processFinanceCommand(words: Array<String>) {
        // to be overridden in subclass
    }

    protected fun processHealthCommand(words: Array<String>) {
        // to be overridden in subclass
    }

    protected fun processWorkCommand(words: Array<String>) {
        // to be overridden in subclass
    }

    protected fun processWikiCommand(words: Array<String>) {
        // to be overridden in subclass
    }

    protected fun processOwningsCommand(words: Array<String>) {
        // to be overridden in subclass
    }

    protected fun processContractsCommand(words: Array<String>) {
        // to be overridden in subclass
    }

    protected fun processFamilyCommand(words: Array<String>) {
        when (words[0]) {
            FamilyTextMessages.CREATE_USER_SUCCESS -> {
                logger.info("Create user success")
                connection.sendFamilyMessage(FamilyTextMessages.loginMessage())
                view.showText(R.string.create_user_success)
            }

            FamilyTextMessages.CREATE_USER_FAIL -> {
                logger.error("create user failed")
                view.showText(R.string.create_user_failed)
                view.askForNameOrImport()
            }

            FamilyTextMessages.CREATE_FAMILY_SUCCESS -> {
                var familyName = words[1]
                val text = context.getString(R.string.createdNewFamily, familyName)
                view.showText(text)
                // login successfully
                connection.sendFamilyMessage(FamilyTextMessages.getFamilyMemberMessage())
            }

            FamilyTextMessages.CREATE_FAMILY_FAIL -> {
                logger.error("Create FAMILY failed")
                view.showText(R.string.family_already_exists)
                view.askJoinOrCreateFamily()
            }

            FamilyTextMessages.JOIN_FAMILY_SUCCESS -> {
                var familyName = words[1]
                val string = context.getString(R.string.joinedFamily, familyName)
                view.showText(string)
                // login successfully
                connection.sendFamilyMessage(FamilyTextMessages.getFamilyMemberMessage())
            }

            FamilyTextMessages.JOIN_FAMILY_FAIL -> {
                logger.error("Join FAMILY failed")
                view.showText(R.string.family_doesnt_exists)
                view.askJoinOrCreateFamily()
            }
        }
    }

    fun resume() {
        logger.info("Resume")
        view.showConnectionStatus(connection.isConnected)
        (context.applicationContext as FamilyApplication).start()
        if (!loginHandler.isLoggingIn) {

            if (!loginHandler.isLogin) {
                loginHandler.login()
            } else {
                start()
            }
        } else {
            logger.info("Login is already in process...")
        }
    }

    fun stop() {
        logger.info("Pause")
        (context.applicationContext as FamilyApplication).stop()
    }

    override fun onConnectionStatusChange(connected: Boolean) {
        view.showConnectionStatus(connected)
    }

    fun attachView(view: FamilyView) {
        this.view = view
        resume()
    }

    fun detachView() {
        this.view = createNullView()
        stop()
    }

    protected abstract fun createNullView(): FamilyView

    protected fun startApp(@StringRes app: Int) {
        val appUri = context.getString(app)
        logger.info("Starting app: " + appUri)
        val LaunchIntent = context.packageManager
                .getLaunchIntentForPackage(appUri)
        context.startActivity(LaunchIntent)
    }
}
