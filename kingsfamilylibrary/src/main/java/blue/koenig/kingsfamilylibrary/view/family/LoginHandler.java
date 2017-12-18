package blue.koenig.kingsfamilylibrary.view.family;

import android.content.Context;

import com.koenig.commonModel.User;
import com.koenig.communication.messages.FamilyMessage;
import com.koenig.communication.messages.TextMessage;
import com.koenig.communication.messages.family.FamilyMemberMessage;
import com.koenig.communication.messages.family.FamilyTextMessages;
import com.koenig.communication.messages.family.UserMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import blue.koenig.kingsfamilylibrary.model.FamilyConfig;
import blue.koenig.kingsfamilylibrary.model.Utils;
import blue.koenig.kingsfamilylibrary.model.communication.ConnectionEventListener;
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection;

/**
 * Created by Thomas on 01.11.2017.
 */

public class LoginHandler implements ConnectionEventListener {
    ServerConnection connection;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private boolean login = false;
    private Context context;
    private LoginListener listener;
    private boolean loggingIn;
    private List<User> members;

    public LoginHandler(ServerConnection connection, Context context) {
        this.connection = connection;
        this.context = context;
        connection.addOnConnectionEventListener(this);
        members = FamilyConfig.getFamilyMembers(context);
    }

    public List<User> getMembers() {
        return members;
    }

    public boolean isLogin() {
        return login;
    }

    protected void processFamilyCommand(String[] words) {
        switch (words[0]) {
            case FamilyTextMessages.LOGIN_FAIL:
                logger.error("Login failed");
                login = false;
                loggingIn = false;
                if (listener != null) listener.onLoginFailed();
                break;


            case FamilyTextMessages.GET_FAMILY_MEMBER_FAIL:
                logger.error("Get members failed");
                if (listener != null) listener.onFamilyMembersFailed();
                break;
        }
    }

    @Override
    public void onConnectionStatusChange(boolean connected) {
        if (!connected) {
            logger.info("Disconnected");
            login = false;
            loggingIn = false;
        } else {
            logger.info("Connected");
            login = false;
            loggingIn = false;
            login();
        }
    }

    @Override
    public void onReceiveMessage(FamilyMessage message) {
        String messageName = message.getName();
        //logger.info("Received message: " + messageName);
        //logger.info("Component: " + message.getComponent());
        if (messageName.equals(TextMessage.NAME)) {
            TextMessage textMessage = (TextMessage) message;
            //logger.info("Received text: " + textMessage.getText());
            String[] words = textMessage.getText().split(FamilyMessage.SEPARATOR);
            processFamilyCommand(words);
        }

        switch (message.getName()) {

            case UserMessage.NAME:
                UserMessage userMessage = (UserMessage) message;
                userInfo(userMessage.getUser());
                break;

            case FamilyMemberMessage.NAME:
                FamilyMemberMessage familyMemberMessage = (FamilyMemberMessage) message;
                members = familyMemberMessage.getMembers();
                if (listener != null) {
                    listener.onFamilyMembers(familyMemberMessage.getMembers());
                }
                FamilyConfig.setFamilyMembers(context, members);
                login = true;
                loggingIn = false;
                if (listener != null) listener.onLogin();
                break;
        }
    }

    private void userInfo(User user) {
        Utils.setUserId(connection, context, user.getId());

        if (user.getFamily().isEmpty()) {
            // no family
            logger.info("User has no family");
            login = false;
            loggingIn = false;
            if (listener != null) listener.onNoFamily();
        } else {

            // login successfully, get all family members
            connection.sendFamilyMessage(FamilyTextMessages.getFamilyMemberMessage());
        }
    }

    public void login() {
        if (loggingIn) {
            logger.info("Already logging in");
            return;
        }

        if (!connection.isConnected()) {
            logger.info("Can not login because there is no connection to the server");
            return;
        }

        logger.info("Login from LoginHandler");
        loggingIn = true;
        String userId = FamilyConfig.getUserId(context);
        if (!userId.equals(FamilyConfig.NO_ID)) {
            Utils.setUserId(connection, context, userId);
            connection.sendFamilyMessage(FamilyTextMessages.loginMessage());
        } else {
            logger.info("No id for user found");
            loggingIn = false;
            login = false;
            listener.onLoginFailed();
        }
    }

    public void setLoginListener(LoginListener onLoginListener) {
        this.listener = onLoginListener;
    }

    public boolean isLoggingIn() {
        return loggingIn;
    }

    public void setLoggingIn(boolean loggingIn) {
        this.loggingIn = loggingIn;
    }

    public interface LoginListener {
        void onLogin();

        void onLoginFailed();

        void onNoFamily();

        void onFamilyMembers(List<User> members);

        void onFamilyMembersFailed();
    }
}
