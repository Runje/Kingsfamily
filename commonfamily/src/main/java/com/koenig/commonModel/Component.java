package com.koenig.commonModel;

import java.nio.ByteBuffer;

public enum Component {
    FINANCE, CONTRACTS, OWNINGS, HEALTH, WIKI, FAMILY, WORK;


    public static Component read(ByteBuffer buffer) {
        return Component.valueOf(Byteable.Companion.byteToString(buffer));
    }

    public byte[] toBytes() {
        return Byteable.Companion.stringToByte(name());
    }

    public int getBytesLength() {
        return Byteable.Companion.getStringLength(name());
    }
}
