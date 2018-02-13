package blue.koenig.kingsfamilylibrary;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.multidex.MultiDexApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import blue.koenig.kingsfamilylibrary.dagger.AppModule;
import blue.koenig.kingsfamilylibrary.dagger.DaggerFamilyAppComponent;
import blue.koenig.kingsfamilylibrary.dagger.FamilyAppComponent;
import blue.koenig.kingsfamilylibrary.model.FamilyConfig;
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection;
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler;

/**
 * Created by Thomas on 01.11.2017.
 */

public abstract class FamilyApplication extends MultiDexApplication {
    protected FamilyAppComponent familyAppComponent;
    @Inject
    ServerConnection connection;
    @Inject
    LoginHandler loginHandler;
    Messenger messenger;
    boolean serviceIsBound;
    /**
     private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName componentName, IBinder service) {
    messenger = new Messenger(service);

    serviceIsBound = true;
    connection = ((ConnectionService) service).getServerConnection();
    }

    @Override public void onServiceDisconnected(ComponentName componentName) {
    messenger = null;
    serviceIsBound = false;
    }
    };**/
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private ScheduledExecutorService service;
    private int runningActivities;

    public FamilyAppComponent getFamilyAppComponent() {
        return familyAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.info("Create Application");
        FamilyConfig.INSTANCE.init(this);
        initDagger();
        familyAppComponent.inject(this);


        // Bind to the service
        //bindService(new Intent(this, ConnectionService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    protected abstract void initDagger();

    protected FamilyAppComponent initDagger(FamilyApplication application) {
        return DaggerFamilyAppComponent.builder().appModule(new AppModule(application)).build();
    }

    public void sayHello() {
        if (!serviceIsBound) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, 1, 0, 0);
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        runningActivities--;
        if (runningActivities <= 0) {
            logger.info("No more activities active, closing connection");
            connection.disconnect();
            service.shutdown();
            /**
             // Unbind from the service
             if (serviceIsBound) {
             unbindService(serviceConnection);
             serviceIsBound = false;
             }**/
        }
    }

    public void start() {
        runningActivities++;
        if (runningActivities > 0) {
            logger.info("Starting...");
            service = Executors.newScheduledThreadPool(1);
            Runnable tryConnect = () -> {
                if (!connection.isConnected()) {
                    connection.connect();
                }
            };
            service.scheduleAtFixedRate(tryConnect, 0, 1, TimeUnit.SECONDS);
        }
    }
}
