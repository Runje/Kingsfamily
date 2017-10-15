package blue.koenig.kingsfamilylibrary.dagger;

import android.util.Log;

import javax.inject.Named;
import javax.inject.Singleton;

import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Thomas on 17.09.2017.
 */
@Module
public class ConnectionModule {
    @Provides
    @Singleton
    ServerConnection provideServerConnection(@Named("INSTALLATION_ID") String id) {
        Log.d("ConnectionModule", "Installation ID: " + id);
        return new ServerConnection(id);
    }
}
