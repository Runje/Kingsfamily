package com.koenig.communication;


import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.Component;
import com.koenig.communication.messages.AUDMessage;
import com.koenig.communication.messages.FamilyMessage;
import com.koenig.communication.messages.TextMessage;
import com.koenig.communication.messages.family.CreateUserMessage;
import com.koenig.communication.messages.family.FamilyMemberMessage;
import com.koenig.communication.messages.family.UserMessage;
import com.koenig.communication.messages.finance.CategorysMessage;
import com.koenig.communication.messages.finance.ExpensesMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class Parser {
    protected static Logger logger = LoggerFactory.getLogger("Parser");
    public static FamilyMessage parse(ByteBuffer buffer) {
        int version = buffer.getInt();
        Component component = Component.read(buffer);
        String name = Byteable.byteToString(buffer);
        String fromId = Byteable.byteToString(buffer);
        String toId = Byteable.byteToString(buffer);
        //DateTime timestamp = new DateTime(buffer.getLong());

        FamilyMessage msg = null;
        switch(name)
        {
            case TextMessage.NAME:
                msg = new TextMessage(version, component, fromId, toId, buffer);
                break;

            case FamilyMemberMessage.NAME:
                msg = new FamilyMemberMessage(version, component, fromId, toId, buffer);
                break;

            case CreateUserMessage.NAME:
                msg = new CreateUserMessage(version, component, fromId, toId, buffer);
                break;

            case UserMessage.NAME:
                msg = new UserMessage(version, component, fromId, toId, buffer);
                break;
            case ExpensesMessage.NAME:
                msg = new ExpensesMessage(version, component, fromId, toId, buffer);
                break;
            case CategorysMessage.NAME:
                msg = new CategorysMessage(version, component, fromId, toId, buffer);
                break;

            case AUDMessage.NAME:
                msg = AUDMessage.create(version, component, fromId, toId, buffer);
                break;

            default:
                logger.error("Unknown name: " + name);
                break;
        }

        return msg;
    }

}
