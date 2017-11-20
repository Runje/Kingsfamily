package com.koenig.commonModel;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 17.11.2017.
 */

public class Category extends Item {
    private String main;
    private List<String> subs;

    public Category(String main, List<String> subs) {
        super();
        this.main = main;
        this.subs = subs;
    }

    public Category(String main) {
        this.main = main;
        this.subs = new ArrayList<>();
    }

    public Category(String id, String main, List<String> subs) {
        super(id);
        this.main = main;
        this.subs = subs;
    }

    public Category(ByteBuffer buffer) {
        super(buffer);
        main = byteToString(buffer);
        short size = buffer.getShort();
        subs = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            subs.add(byteToString(buffer));
        }
    }

    @Override
    public int getByteLength() {
        int subsLength = 2;
        for (String sub : subs) {
            subsLength += getStringLength(sub);
        }

        return super.getByteLength() + getStringLength(main) + subsLength;
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        writeString(main, buffer);
        buffer.putShort((short) subs.size());
        for (String sub : subs) {
            writeString(sub, buffer);
        }
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public List<String> getSubs() {
        return new ArrayList<>(subs);
    }

    public void setSubs(List<String> subs) {
        this.subs = subs;
    }

    public boolean hasSubs() {
        return subs.size() > 0;
    }

    public void addSub(String sub) {
        subs.add(sub);
    }
}
