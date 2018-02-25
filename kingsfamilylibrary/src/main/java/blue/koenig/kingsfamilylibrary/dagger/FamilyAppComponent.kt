package blue.koenig.kingsfamilylibrary.dagger

import blue.koenig.kingsfamilylibrary.FamilyApplication
import blue.koenig.kingsfamilylibrary.model.communication.ConnectionService
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Thomas on 17.09.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, ConnectionModule::class, InstallationModule::class, ModelModule::class))
interface FamilyAppComponent {
    fun inject(target: FamilyApplication)

    fun inject(connectionService: ConnectionService)
}
