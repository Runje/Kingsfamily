package blue.koenig.kingsfamilylibrary.model.communication


import com.example.OnConnectionChangedListener
import com.example.OnReceiveBytesListener
import com.example.SocketChannelTCPClient
import com.koenig.communication.ConnectUtils
import com.koenig.communication.Parser
import com.koenig.communication.messages.FamilyMessage
import io.reactivex.Observable
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

/**
 * Created by Thomas on 08.01.2017.
 */
class ServerConnection(userIdObservable: Observable<String>) : SocketChannelTCPClient(ConnectUtils.PORT, ConnectUtils.SERVER_IP), OnConnectionChangedListener, OnReceiveBytesListener {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val connectionEventListeners: MutableList<ConnectionEventListener>

    private lateinit var fromId: String

    init {
        userIdObservable.subscribe { fromId = it }
        connectionEventListeners = ArrayList()
        addOnConnectionChangedListener(this)
        addOnReceiveBytesListener(this)
    }

    override fun connect() {
        logger.trace("Trying to connect...")
        tryConnect()
    }

    fun addOnConnectionEventListener(connectionEventListener: ConnectionEventListener) {
        logger.info("AddonConnectionEventListener: " + connectionEventListener.toString())
        this.connectionEventListeners.add(connectionEventListener)
    }

    fun sendFamilyMessage(msg: FamilyMessage) {
        msg.fromId = fromId
        msg.toId = FamilyMessage.ServerId
        super.sendMessage(msg)
        logger.info("Sent message " + msg.name)
    }

    override fun onConnectionChanged(b: Boolean) {
        logger.info("ConnectionChanged: " + b)
        for (connectionEventListener in connectionEventListeners) {
            connectionEventListener.onConnectionStatusChange(b)
        }

    }

    override fun onReceiveBytes(bytes: ByteArray) {

        logger.info("Receive bytes: " + bytes.size)
        val msg: FamilyMessage?
        try {
            msg = Parser.parse(ByteBuffer.wrap(bytes))

        } catch (e: Exception) {
            logger.error("Error while parsing message: " + e.message)
            return
        }

        logger.info(msg.toString())
        for (connectionEventListener in connectionEventListeners) {
            connectionEventListener.onReceiveMessage(msg)
        }

    }


}
