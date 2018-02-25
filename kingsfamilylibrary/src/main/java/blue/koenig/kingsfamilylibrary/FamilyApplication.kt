package blue.koenig.kingsfamilylibrary

import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.support.multidex.MultiDexApplication
import blue.koenig.kingsfamilylibrary.dagger.AppModule
import blue.koenig.kingsfamilylibrary.dagger.DaggerFamilyAppComponent
import blue.koenig.kingsfamilylibrary.dagger.FamilyAppComponent
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Thomas on 01.11.2017.
 */

abstract class FamilyApplication : MultiDexApplication() {
    var familyAppComponent: FamilyAppComponent? = null
        protected set
    @Inject
    lateinit var connection: ServerConnection
    @Inject
    lateinit var loginHandler: LoginHandler
    internal var messenger: Messenger? = null
    internal var serviceIsBound: Boolean = false
    /**
     * private ServiceConnection serviceConnection = new ServiceConnection() {
     * @Override public void onServiceConnected(ComponentName componentName, IBinder service) {
     * messenger = new Messenger(service);
     *
     * serviceIsBound = true;
     * connection = ((ConnectionService) service).getServerConnection();
     * }
     *
     * @Override public void onServiceDisconnected(ComponentName componentName) {
     * messenger = null;
     * serviceIsBound = false;
     * }
     * }; */
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private var service: ScheduledExecutorService? = null
    private var runningActivities: Int = 0

    override fun onCreate() {
        super.onCreate()
        logger.info("Create Application")

        initDagger()
        familyAppComponent!!.inject(this)


        // Bind to the service
        //bindService(new Intent(this, ConnectionService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    protected abstract fun initDagger()

    protected fun initDagger(application: FamilyApplication): FamilyAppComponent {
        return DaggerFamilyAppComponent.builder().appModule(AppModule(application)).build()
    }

    fun sayHello() {
        if (!serviceIsBound) return
        // Create and send a message to the service, using a supported 'what' value
        val msg = Message.obtain(null, 1, 0, 0)
        try {
            messenger!!.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }


    fun stop() {
        runningActivities--
        if (runningActivities <= 0) {
            logger.info("No more activities active, closing connection")
            connection.disconnect()
            service!!.shutdown()
            /**
             * // Unbind from the service
             * if (serviceIsBound) {
             * unbindService(serviceConnection);
             * serviceIsBound = false;
             * } */
        }
    }

    fun start() {
        runningActivities++
        if (runningActivities > 0) {
            logger.info("Starting...")
            service = Executors.newScheduledThreadPool(1)
            val tryConnect = {
                if (!connection.isConnected) {
                    connection.connect()
                }
            }
            service!!.scheduleAtFixedRate(tryConnect, 0, 1, TimeUnit.SECONDS)
        }
    }
}
