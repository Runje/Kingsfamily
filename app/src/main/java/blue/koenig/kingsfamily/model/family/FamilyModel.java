package blue.koenig.kingsfamily.model.family;

import android.content.Context;

import com.koenig.communication.Commands;
import com.koenig.communication.messages.FamilyMessage;
import com.koenig.communication.messages.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import blue.koenig.kingsfamily.R;
import blue.koenig.kingsfamily.model.FamilyConfig;
import blue.koenig.kingsfamily.model.communication.ConnectionEventListener;
import blue.koenig.kingsfamily.model.communication.ServerConnection;
import blue.koenig.kingsfamily.presenter.family.FamilyPresenter;
import blue.koenig.kingsfamily.presenter.family.FamilyPresenterListener;

/**
 * Created by Thomas on 18.09.2017.
 */

public class FamilyModel implements FamilyPresenterListener, ConnectionEventListener {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    ServerConnection connection;
    private Context context;

    public FamilyPresenter getPresenter() {
        return presenter;
    }

    private FamilyPresenter presenter;
    private ScheduledExecutorService service;

    @Inject
    public FamilyModel(ServerConnection connection, Context context, FamilyPresenter presenter) {
        this.connection = connection;
        this.context = context;
        this.presenter = presenter;

        connection.setOnConnectionEventListener(this);
    }

    public void start() {
        logger.info("Model started");
        String name = FamilyConfig.getUsername(context);
        if (name.equals(FamilyConfig.NO_NAME))
        {
            // first time

            //TODO Should be possible to import file/use existing user

            presenter.askForName();
        }
        else {
            connection.sendFamilyMessage(new TextMessage(Commands.LOGIN));
        }

        connection.sendFamilyMessage(new TextMessage("CLIENT"));
    }

    @Override
    public void resume() {
        service = Executors.newScheduledThreadPool(1);
        Runnable tryConnect = new Runnable() {
            @Override
            public void run() {
                if (!connection.isConnected()) {
                    connection.connect();
                }
            }
        };

        service.scheduleAtFixedRate(tryConnect, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void pause() {
        connection.disconnect();
        service.shutdown();
    }

    @Override
    public void onCreatingUser(String name) {
        presenter.joinOrCreateFamily();
    }

    @Override
    public void createFamily(String name) {
        connection.sendFamilyMessage(new TextMessage(Commands.CREATE_FAMILY + name));
    }

    @Override
    public void joinFamily(String name) {

    }

    @Override
    public void onConnectionStatusChange(boolean connected) {
        // TODO
    }

    @Override
    public void onReceiveMessage(FamilyMessage message) {
        switch(message.getName()) {
            case TextMessage.NAME:
                TextMessage textMessage = (TextMessage) message;
                logger.info("Received text: " + textMessage.getText());
                processCommand(textMessage.getText());
                break;
            default:
                logger.error("Unknown Message: " + message.getName());
        }

    }

    private void processCommand(String command) {
        String[] words = command.split(" ");
        switch (words[0]){
            case Commands.LOGIN_FAIL:
                logger.error("Login failed");
                // TODO: Show error
                break;

            case Commands.LOGIN_SUCCESS:
                presenter.say(context.getString(R.string.Hello) + FamilyConfig.getUsername(context));
                break;

            case Commands.CREATE_FAMILY_SUCCESS:
                String familyName = words[1];
                FamilyConfig.saveFamilyname(familyName, context);
                presenter.say(context.getString(R.string.createdNewFamily) + familyName);
                break;

            case Commands.CREATE_FAMILY_FAIL:
                logger.error("Create Family failed");
                break;
        }
    }
}
