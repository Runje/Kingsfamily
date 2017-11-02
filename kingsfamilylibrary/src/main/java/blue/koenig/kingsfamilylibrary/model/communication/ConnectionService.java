package blue.koenig.kingsfamilylibrary.model.communication;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.koenig.communication.messages.FamilyMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import blue.koenig.kingsfamilylibrary.model.FamilyConfig;
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler;

/**
 * Created by Thomas on 01.11.2017.
 */

public class ConnectionService extends Service implements ConnectionEventListener {
    /**
     * Command to the service to display a message
     */
    static final int MSG_SAY_HELLO = 1;
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private ServerConnection connection;
    private ScheduledExecutorService service;
    private LoginHandler loginHandler;

    @Override
    public void onConnectionStatusChange(boolean connected) {

    }

    @Override
    public void onReceiveMessage(FamilyMessage message) {

    }

    public ServerConnection getServerConnection() {
        return connection;
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("ConnectionService", "On Bind");
        logger.info("OnBind");
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        String id = FamilyConfig.getUserId(this);
        connection = new ServerConnection(id);
        connection.addOnConnectionEventListener(this);
        service = Executors.newScheduledThreadPool(1);
        Runnable tryConnect = new Runnable() {
            @Override
            public void run() {
                if (!connection.isConnected()) {
                    connection.connect();
                }
            }
        };
        loginHandler = new LoginHandler(connection, this);

        service.scheduleAtFixedRate(tryConnect, 0, 1, TimeUnit.SECONDS);
        return mMessenger.getBinder();
    }

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            logger.info("Received message");
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
