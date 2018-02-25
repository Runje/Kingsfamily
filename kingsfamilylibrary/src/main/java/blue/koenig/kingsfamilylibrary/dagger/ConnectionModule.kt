package blue.koenig.kingsfamilylibrary.dagger

import blue.koenig.kingsfamilylibrary.model.FamilyConfig
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Thomas on 17.09.2017.
 */
@Module
class ConnectionModule {
    @Provides
    @Singleton
    internal fun provideServerConnection(config: FamilyConfig): ServerConnection {
        return ServerConnection(config.userIdObservable)
    }

    @Provides
    @Singleton
    internal fun provideLoginHandler(connection: ServerConnection, config: FamilyConfig): LoginHandler {
        return LoginHandler(connection, config)
    }
}
