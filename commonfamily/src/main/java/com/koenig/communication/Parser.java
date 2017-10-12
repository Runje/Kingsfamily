package com.koenig.communication;


import com.koenig.BinaryConverter;
import com.koenig.communication.messages.AskForUserMessage;
import com.koenig.communication.messages.CreateUserMessage;
import com.koenig.communication.messages.FamilyMessage;
import com.koenig.communication.messages.TextMessage;
import com.koenig.communication.messages.UserMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 11.01.2017.
 */

public class Parser {
    protected static Logger logger = LoggerFactory.getLogger("Parser");
    public static FamilyMessage parse(ByteBuffer buffer) {
        String name = BinaryConverter.byteToString(buffer);
        String fromId = BinaryConverter.byteToString(buffer);
        String toId = BinaryConverter.byteToString(buffer);
        //DateTime timestamp = new DateTime(buffer.getLong());

        FamilyMessage msg = null;
        switch(name)
        {
            case TextMessage.NAME:
                msg = new TextMessage(fromId, toId, buffer);
                break;

            case AskForUserMessage.NAME:
                msg = new AskForUserMessage(fromId, toId, buffer);
                break;

            case UserMessage.NAME:
                msg = new UserMessage(fromId, toId, buffer);
                break;

            case CreateUserMessage.NAME:
                msg = new CreateUserMessage(fromId, toId, buffer);
                break;

            default:
                logger.error("Unknown name: " + name);
                break;
        }

        return msg;
    }

}
