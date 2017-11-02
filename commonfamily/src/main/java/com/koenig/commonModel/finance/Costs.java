package com.koenig.commonModel.finance;

import com.koenig.StringFormats;
import com.koenig.commonModel.Byteable;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 22.10.2017.
 */

public class Costs extends Byteable {
    public int Real;
    public int Theory;

    public Costs(int real, int theory) {
        Real = real;
        Theory = theory;
    }

    public Costs(ByteBuffer buffer) {
        Real = buffer.getInt();
        Theory = buffer.getInt();
    }

    @Override
    public String toString() {
        if (Real != Theory) {
            return StringFormats.centsToCentString(Real) + "/" + StringFormats.centsToCentString(Theory);
        }

        return StringFormats.centsToCentString(Real);
    }

    public String toEuroString() {
        if (Real != Theory) {
            return StringFormats.centsToEuroString(Real) + "/" + StringFormats.centsToEuroString(Theory);
        }
        return StringFormats.centsToEuroString(Real);
    }

    @Override
    public int getByteLength() {
        return 8;
    }

    @Override
    public void writeBytes(ByteBuffer buffer) {
        buffer.putInt(Real);
        buffer.putInt(Theory);
    }
}
