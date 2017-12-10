package blue.koenig.kingsfamilylibrary.model.family;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;

import com.koenig.commonModel.User;
import com.koenig.communication.messages.FamilyMessage;
import com.koenig.communication.messages.TextMessage;
import com.koenig.communication.messages.family.CreateUserMessage;
import com.koenig.communication.messages.family.FamilyTextMessages;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import blue.koenig.kingsfamilylibrary.FamilyApplication;
import blue.koenig.kingsfamilylibrary.R;
import blue.koenig.kingsfamilylibrary.model.Utils;
import blue.koenig.kingsfamilylibrary.model.communication.ConnectionEventListener;
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection;
import blue.koenig.kingsfamilylibrary.view.family.FamilyView;
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler;
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler.LoginListener;
import blue.koenig.kingsfamilylibrary.view.family.NullFamilyView;

/**
 * Created by Thomas on 18.10.2017.
 */

public abstract class FamilyModel implements ConnectionEventListener, LoginListener {
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    protected Context context;
    protected ServerConnection connection;
    protected List<Plugin> plugins = new ArrayList<>();
    protected FamilyView view = new NullFamilyView();
    protected LoginHandler loginHandler;


    public FamilyModel(ServerConnection connection, Context context, LoginHandler loginHandler) {
        this.context = context;
        this.connection = connection;
        this.loginHandler = loginHandler;
        this.view = createNullView();
        connection.addOnConnectionEventListener(this);
        loginHandler.setLoginListener(this);

    }

    public List<User> getFamilyMembers() {
        return loginHandler.getMembers();
    }

    public void sendMessageToServer(FamilyMessage message) {
        connection.sendFamilyMessage(message);
    }
    public void onCreatingUser(String name, DateTime birthday) {
        connection.sendFamilyMessage(new CreateUserMessage(name, birthday));
    }

    public void createFamily(String name) {
        connection.sendFamilyMessage(FamilyTextMessages.CreateFamilyMessage(name));
    }

    public void joinFamily(String name) {
        connection.sendFamilyMessage(FamilyTextMessages.JoinFamilyMessage(name));
    }

    public void importUser(String userId) {
        userId = userId.trim();
        setUserId(userId);
        connection.sendFamilyMessage(FamilyTextMessages.loginMessage());
    }

    private void setUserId(String userId) {
        Utils.setUserId(connection, context, userId);
    }

    @Override
    public void onLogin() {
        // do nothing --> On FamilyMembers is called
    }

    @Override
    public void onLoginFailed() {
        view.showText(R.string.login_failed);

        // try again
        view.askForNameOrImport();
    }

    @Override
    public void onNoFamily() {

    }

    @Override
    public void onFamilyMembers(List<User> members) {
        updateFamilymembers(members);
        start();
    }

    @Override
    public void onFamilyMembersFailed() {
        logger.error("Get members failed");
        view.showText(R.string.get_familymembers_fail);
    }

    @Override
    public void onReceiveMessage(FamilyMessage message) {
        logger.debug("Received message: " + message.toString());
        logger.debug("Component: " + message.getComponent());
        String[] words = new String[0];
        boolean isTextMessage = false;
        if (message.getName().equals(TextMessage.NAME)) {
            TextMessage textMessage = (TextMessage) message;
            logger.info("Received text: " + textMessage.getText());
            words = textMessage.getText().split(FamilyMessage.SEPARATOR);
            isTextMessage = true;
        }

        switch (message.getComponent()) {
            case FINANCE:
                if (isTextMessage) {
                    processFinanceCommand(words);
                } else {
                    onReceiveFinanceMessage(message);
                }
                break;
            case CONTRACTS:
                if (isTextMessage) {
                    processContractsCommand(words);
                } else {
                    onReceiveContractsMessage(message);
                }
                break;
            case OWNINGS:
                if (isTextMessage) {
                    processOwningsCommand(words);
                } else {
                    onReceiveOwningsMessage(message);
                }
                break;
            case HEALTH:
                if (isTextMessage) {
                    processHealthCommand(words);
                } else {
                    onReceiveHealthMessage(message);
                }
                break;
            case WIKI:
                if (isTextMessage) {
                    processWikiCommand(words);
                } else {
                    onReceiveWikiMessage(message);
                }
                break;
            case FAMILY:
                if (isTextMessage) {
                    processFamilyCommand(words);
                } else {
                    onReceiveFamilyMessage(message);
                }
                break;
            case WORK:
                if (isTextMessage) {
                    processWorkCommand(words);
                } else {
                    onReceiveWorkMessage(message);
                }
                break;
            default:
                logger.error("Unknown component");
        }

    }

    protected void onReceiveFinanceMessage(FamilyMessage message) {
        // To be overridden in subclasses
    }

    protected void onReceiveContractsMessage(FamilyMessage message) {
        // To be overridden in subclasses
    }

    protected void onReceiveHealthMessage(FamilyMessage message) {
        // To be overridden in subclasses
    }

    protected void onReceiveWikiMessage(FamilyMessage message) {
        // To be overridden in subclasses
    }

    protected void onReceiveWorkMessage(FamilyMessage message) {
        // To be overridden in subclasses
    }

    protected void onReceiveOwningsMessage(FamilyMessage message) {
        // To be overridden in subclasses
    }

    protected void onReceiveFamilyMessage(FamilyMessage message) {
        switch (message.getName()) {
        }
    }

    protected abstract void updateFamilymembers(List<User> members);


    protected abstract void start();

    protected void processFinanceCommand(String[] words) {
        // to be overridden in subclass
    }

    protected void processHealthCommand(String[] words) {
        // to be overridden in subclass
    }

    protected void processWorkCommand(String[] words) {
        // to be overridden in subclass
    }

    protected void processWikiCommand(String[] words) {
        // to be overridden in subclass
    }

    protected void processOwningsCommand(String[] words) {
        // to be overridden in subclass
    }

    protected void processContractsCommand(String[] words) {
        // to be overridden in subclass
    }

    protected void processFamilyCommand(String[] words) {
        switch (words[0]){
            case FamilyTextMessages.CREATE_USER_SUCCESS:
                logger.info("Create user success");
                connection.sendFamilyMessage(FamilyTextMessages.loginMessage());
                view.showText(R.string.create_user_success);
                break;

            case FamilyTextMessages.CREATE_USER_FAIL:
                logger.error("create user failed");
                view.showText(R.string.create_user_failed);
                view.askForNameOrImport();
                break;

            case FamilyTextMessages.CREATE_FAMILY_SUCCESS:
                String familyName = words[1];
                String text = context.getString(R.string.createdNewFamily, familyName);
                view.showText(text);
                // login successfully
                connection.sendFamilyMessage(FamilyTextMessages.getFamilyMemberMessage());
                break;

            case FamilyTextMessages.CREATE_FAMILY_FAIL:
                logger.error("Create FAMILY failed");
                view.showText(R.string.family_already_exists);
                view.askJoinOrCreateFamily();
                break;

            case FamilyTextMessages.JOIN_FAMILY_SUCCESS:
                familyName = words[1];
                String string = context.getString(R.string.joinedFamily, familyName);
                view.showText(string);
                // login successfully
                connection.sendFamilyMessage(FamilyTextMessages.getFamilyMemberMessage());
                break;

            case FamilyTextMessages.JOIN_FAMILY_FAIL:
                logger.error("Join FAMILY failed");
                view.showText(R.string.family_doesnt_exists);
                view.askJoinOrCreateFamily();
                break;
        }
    }

    public void resume() {
        logger.info("Resume");
        view.showConnectionStatus(connection.isConnected());
        ((FamilyApplication) context.getApplicationContext()).start();
        if (!loginHandler.isLoggingIn()) {

            if (!loginHandler.isLogin()) {
                loginHandler.login();
            } else {
                start();
            }
        } else {
            logger.info("Login is already in process...");
        }
    }

    public void stop() {
        logger.info("Pause");
        ((FamilyApplication) context.getApplicationContext()).stop();
    }

    public void onConnectionStatusChange(boolean connected) {
        view.showConnectionStatus(connected);
    }

    public void attachView(FamilyView view) {
        this.view = view;
        resume();
    }

    public void detachView() {
        this.view = createNullView();
        stop();
    }

    protected abstract FamilyView createNullView();

    protected void startApp(@StringRes int app) {
        String appUri = context.getString(app);
        logger.info("Starting app: " + appUri);
        Intent LaunchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(appUri);
        context.startActivity(LaunchIntent);
    }
}
