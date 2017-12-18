package blue.koenig.kingsfamilylibrary;

import com.koenig.commonModel.Byteable;
import com.koenig.commonModel.User;

import net.iharder.Base64;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void byteToString() throws IOException {
        User milena = new User("Milena", "König", new DateTime(1987, 8, 10, 0, 0));
        User thomas = new User("Thomas", "König", new DateTime(1987, 6, 14, 0, 0));
        List<User> members = new ArrayList<>(2);
        members.add(milena);
        members.add(thomas);
        ByteBuffer buffer = ByteBuffer.allocate(Byteable.getListLength(members));
        Byteable.writeList(members, buffer);

        String membersString = Base64.encodeBytes(buffer.array());
        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.decode(membersString));
        byte[] array = byteBuffer.array();
        byte[] expectedArray = buffer.array();
        Assert.assertEquals(array.length, expectedArray.length);
        for (int i = 0; i < array.length; i++) {
            Assert.assertEquals(expectedArray[i], array[i]);
        }
        short size = byteBuffer.getShort();
        List<User> members2 = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            members2.add(new User(byteBuffer));
        }

        Assert.assertEquals(members.size(), members2.size());
    }
}