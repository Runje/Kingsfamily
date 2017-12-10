package test;

import com.koenig.commonModel.Family;
import com.koenig.commonModel.Operation;
import com.koenig.commonModel.Operator;
import com.koenig.commonModel.database.DatabaseItem;

import org.joda.time.DateTime;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ConverterTests {
    @Test
    public void operation() throws Exception {
        Family family = new Family("König");
        Operation<Family> familyOperation = new Operation<Family>("id", Operator.ADD, family);
        ByteBuffer buffer = ByteBuffer.wrap(familyOperation.getBytes());
        Operation<Family> result = new Operation(buffer);


        assertEquals("id", result.getId());
        assertEquals(Operator.ADD, result.getOperator());
        assertEquals(family.getName(), result.getItem().getName());
    }

    @Test
    public void databaseItem() throws Exception {
        Family family = new Family("König");

        String insertId = "insertId";
        String modifiedId = "modifiedId";
        boolean deleted = true;
        DateTime insertDate = DateTime.now();
        DateTime modifiedDate = DateTime.now().withDate(2017, 7, 6);
        DatabaseItem<Family> familyDatabaseItem = new DatabaseItem<Family>(family, insertDate, modifiedDate, deleted, insertId, modifiedId);
        ByteBuffer buffer = ByteBuffer.wrap(familyDatabaseItem.getBytes());
        DatabaseItem<Family> result = new DatabaseItem(buffer);

        assertTrue(result.getItem() instanceof Family);
        assertEquals(family.getId(), result.getId());
        assertEquals(family.getName(), result.getItem().getName());
        assertEquals(result.getInsertDate(), insertDate);
        assertEquals(result.getLastModifiedDate(), modifiedDate);
        assertEquals(result.getLastModifiedId(), modifiedId);
        assertEquals(result.getInsertId(), insertId);
        assertEquals(result.isDeleted(), deleted);
    }
}