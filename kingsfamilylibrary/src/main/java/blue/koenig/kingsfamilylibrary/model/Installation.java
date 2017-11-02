package blue.koenig.kingsfamilylibrary.model;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by Thomas on 21.12.2015.
 */
public class Installation
{
    private static final String INSTALLATION = "INSTALLATION";
    protected static Logger logger = LoggerFactory.getLogger("Installation");
    private static String sID = null;

    public synchronized static String id(Context context)
    {
        if (sID == null)
        {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try
            {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException
    {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException
    {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        logger.info("New installation id: " + id);
        out.write(id.getBytes());
        out.close();
    }

    public static void setId(Context context, String userId) {
        logger.info("Setting installation id to " + userId);
        File installation = new File(context.getFilesDir(), INSTALLATION);
        try {
            FileOutputStream out = new FileOutputStream(installation);
            String id = userId;
            out.write(id.getBytes());
            out.close();
            sID = userId;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
