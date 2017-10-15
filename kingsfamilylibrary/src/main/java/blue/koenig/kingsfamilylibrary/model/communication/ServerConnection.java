package blue.koenig.kingsfamilylibrary.model.communication;


import com.example.OnConnectionChangedListener;
import com.example.OnReceiveBytesListener;
import com.example.SocketChannelTCPClient;
import com.koenig.communication.ConnectUtils;
import com.koenig.communication.Parser;
import com.koenig.communication.messages.FamilyMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 08.01.2017.
 */
public class ServerConnection extends SocketChannelTCPClient implements OnConnectionChangedListener, OnReceiveBytesListener {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private ConnectionEventListener connectionEventListener;
    private String fromId;

    public ServerConnection(String fromId) {
        super(ConnectUtils.PORT, ConnectUtils.SERVER_IP);
        this.fromId = fromId;
        addOnConnectionChangedListener(this);
        addOnReceiveBytesListener(this);
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public void connect() {
        logger.info("Trying to connect...");
        tryConnect();
    }

    @Override
    public void disconnect() {
        super.disconnect();
    }

    public void setOnConnectionEventListener(ConnectionEventListener connectionEventListener) {
        this.connectionEventListener = connectionEventListener;
    }

    public void sendFamilyMessage(FamilyMessage msg) {
        msg.setFromId(fromId);
        msg.setToId(FamilyMessage.ServerId);
        super.sendMessage(msg);
    }

    @Override
    public void onConnectionChanged(boolean b) {
        if (connectionEventListener != null)
        {
            connectionEventListener.onConnectionStatusChange(b);
        }
    }

    @Override
    public void onReceiveBytes(byte[] bytes) {

        logger.info("Receive bytes: " + bytes.length);
        FamilyMessage msg = Parser.parse(ByteBuffer.wrap(bytes));

        logger.info(msg.toString());
        if (connectionEventListener != null)
        {
            connectionEventListener.onReceiveMessage(msg);
        }
    }


}
