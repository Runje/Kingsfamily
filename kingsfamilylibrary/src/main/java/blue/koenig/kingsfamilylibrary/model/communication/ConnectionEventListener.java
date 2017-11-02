package blue.koenig.kingsfamilylibrary.model.communication;

import com.koenig.communication.messages.FamilyMessage;

/**
 * Created by Thomas on 12.01.2017.
 */
public interface ConnectionEventListener {
    void onConnectionStatusChange(boolean connected);

    /**
     * Is called whenever a message is received.
     *
     * @param message FamilyMessage
     * @return If the message is consumed
     */
    void onReceiveMessage(FamilyMessage message);
}
