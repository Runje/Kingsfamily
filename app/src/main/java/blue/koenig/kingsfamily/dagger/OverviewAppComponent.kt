package blue.koenig.kingsfamily.dagger

import blue.koenig.kingsfamily.view.OverviewActivity
import blue.koenig.kingsfamilylibrary.dagger.*
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Thomas on 17.09.2017.
 */
@Singleton
@Component(modules = [(AppModule::class), (ConnectionModule::class), (InstallationModule::class), (ModelModule::class)])
interface OverviewAppComponent : FamilyAppComponent {
    fun inject(target: OverviewActivity)
}
