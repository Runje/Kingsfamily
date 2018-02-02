package com.koenig.communication.messages;

import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.Component;
import com.koenig.commonModel.ItemType;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 30.11.2017.
 */

public class AskForUpdatesMessage extends FamilyMessage {
    public static final String NAME = "AskForUpdatesMessage";
    DateTime lastSyncDate;
    ItemType updateType;

    public AskForUpdatesMessage(Component component, DateTime lastSyncDate, ItemType updateType) {
        super(component);
        this.lastSyncDate = lastSyncDate;
        this.updateType = updateType;
    }

    public AskForUpdatesMessage(int version, Component component, String fromId, String toId, ByteBuffer buffer) {
        super(component);
        this.version = version;
        this.fromId = fromId;
        this.toId = toId;
        this.lastSyncDate = new DateTime(buffer.getLong());
        this.updateType = Byteable.Companion.byteToEnum(buffer, ItemType.class);
    }

    public static AskForUpdatesMessage askForExpenses(DateTime lastSyncDate) {
        return new AskForUpdatesMessage(Component.FINANCE, lastSyncDate, ItemType.EXPENSES);
    }

    public static AskForUpdatesMessage askForStandingOrders(DateTime lastSyncDate) {
        return new AskForUpdatesMessage(Component.FINANCE, lastSyncDate, ItemType.STANDING_ORDER);
    }

    public static AskForUpdatesMessage askForCategorys(DateTime lastSyncDate) {
        return new AskForUpdatesMessage(Component.FINANCE, lastSyncDate, ItemType.CATEGORY);
    }

    public ItemType getUpdateType() {
        return updateType;
    }

    public String getName() {
        return NAME;
    }

    public DateTime getLastSyncDate() {
        return lastSyncDate;
    }


    @Override
    protected int getContentLength() {
        return Byteable.Companion.getDateLength() + Byteable.Companion.getEnumLength(updateType);
    }

    @Override
    protected void writeContent(ByteBuffer buffer) {
        Byteable.Companion.writeDateTime(lastSyncDate, buffer);
        Byteable.Companion.writeEnum(updateType, buffer);
    }

    @Override
    public String toString() {
        return "AskForUpdatesMessage{" +
                "lastSyncDate=" + lastSyncDate +
                ", updateType='" + updateType + '\'' +
                '}';
    }
}
