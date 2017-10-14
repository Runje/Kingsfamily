package blue.koenig.kingsfamily.model.family;

import android.content.Context;

import com.koenig.commonModel.User;
import com.koenig.communication.Commands;
import com.koenig.communication.messages.CreateUserMessage;
import com.koenig.communication.messages.FamilyMessage;
import com.koenig.communication.messages.TextMessage;
import com.koenig.communication.messages.UserMessage;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import blue.koenig.kingsfamily.R;
import blue.koenig.kingsfamily.model.FamilyConfig;
import blue.koenig.kingsfamily.model.Installation;
import blue.koenig.kingsfamily.model.communication.ConnectionEventListener;
import blue.koenig.kingsfamily.model.communication.ServerConnection;
import blue.koenig.kingsfamily.view.family.FamilyView;
import blue.koenig.kingsfamily.view.family.NullFamilyView;

/**
 * Created by Thomas on 18.09.2017.
 */

public class FamilyModel implements ConnectionEventListener {
    ServerConnection connection;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private Context context;
    private ScheduledExecutorService service;
    private FamilyView view = new NullFamilyView();

    @Inject
    public FamilyModel(ServerConnection connection, Context context) {
        this.connection = connection;
        this.context = context;
        connection.setOnConnectionEventListener(this);
    }

    public void start() {
        logger.info("Model started");
        String name = FamilyConfig.getUsername(context);
        if (name.equals(FamilyConfig.NO_NAME))
        {
            // first time
            view.askForNameOrImport();
        }
        else {
            connection.sendFamilyMessage(new TextMessage(Commands.LOGIN));
        }

    }

    public void resume() {
        logger.info("Resume");
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

    public void pause() {
        logger.info("Pause");
        connection.disconnect();
        service.shutdown();
    }

    public void onCreatingUser(String name, DateTime birthday) {
        connection.sendFamilyMessage(new CreateUserMessage(name, birthday));
    }

    public void createFamily(String name) {
        connection.sendFamilyMessage(FamilyMessage.CreateFamilyMessage(name));
    }

    public void joinFamily(String name) {
        connection.sendFamilyMessage(FamilyMessage.JoinFamilyMessage(name));
    }

    public void importUser(String userId) {
        Installation.setId(context, userId);
        connection.sendFamilyMessage(FamilyMessage.CreateLoginMessage());
    }

    @Override
    public void onConnectionStatusChange(boolean connected) {
        view.showConnectionStatus(connected);
        if (!connected) {
            logger.info("Disconnected");

        } else {
            logger.info("Connected");
            start();
        }
    }

    @Override
    public void onReceiveMessage(FamilyMessage message) {
        logger.info("Received message: " + message.getName());
        switch(message.getName()) {
            case TextMessage.NAME:
                TextMessage textMessage = (TextMessage) message;
                logger.info("Received text: " + textMessage.getText());
                processCommand(textMessage.getText());
                break;
            case UserMessage.NAME:
                UserMessage userMessage = (UserMessage) message;
                userInfo(userMessage.getUser());
                break;
            default:
                logger.error("Unknown Message: " + message.getName());
        }

    }

    private void userInfo(User user) {
        FamilyConfig.saveUsername(user.getName(), context);
        logger.info("Saved username " + user.getName());
        String string = context.getString(R.string.hello, FamilyConfig.getUsername(context));
        view.showText(string);
        if (user.getFamily().isEmpty()) {
            // no family
            logger.info("User has no family");
            view.askJoinOrCreateFamily();
        }
    }

    private void processCommand(String command) {
        String[] words = command.split(FamilyMessage.SEPARATOR);
        switch (words[0]){
            case Commands.LOGIN_FAIL:
                logger.error("Login failed");
                view.showText(R.string.login_failed);

                // try again
                view.askForNameOrImport();
                break;

            case Commands.CREATE_USER_SUCCESS:
                logger.info("Create user success");

                connection.sendFamilyMessage(new TextMessage(Commands.LOGIN));
                view.showText(R.string.create_user_success);


                break;

            case Commands.CREATE_USER_FAIL:
                logger.error("create user failed");
                view.showText(R.string.create_user_failed);
                view.askForNameOrImport();
                break;

            case Commands.CREATE_FAMILY_SUCCESS:
                String familyName = words[1];
                FamilyConfig.saveFamilyname(familyName, context);
                String text = context.getString(R.string.createdNewFamily, familyName);
                view.showText(text);
                break;

            case Commands.CREATE_FAMILY_FAIL:
                logger.error("Create Family failed");
                view.showText(R.string.family_already_exists);
                view.askJoinOrCreateFamily();
                break;

            case Commands.JOIN_FAMILY_SUCCESS:
                familyName = words[1];
                FamilyConfig.saveFamilyname(familyName, context);
                String string = context.getString(R.string.joinedFamily, familyName);
                view.showText(string);
                break;

            case Commands.JOIN_FAMILY_FAIL:
                logger.error("Join Family failed");
                view.showText(R.string.family_doesnt_exists);
                view.askJoinOrCreateFamily();
                break;
        }
    }

    public void attachView(FamilyView view) {
        this.view = view;
        resume();
    }

    public void detachView() {
        this.view = new NullFamilyView();
        pause();
    }
}
