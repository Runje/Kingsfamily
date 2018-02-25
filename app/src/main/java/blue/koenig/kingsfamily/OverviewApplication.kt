package blue.koenig.kingsfamily

import blue.koenig.kingsfamily.dagger.DaggerOverviewAppComponent
import blue.koenig.kingsfamily.dagger.OverviewAppComponent
import blue.koenig.kingsfamilylibrary.FamilyApplication
import blue.koenig.kingsfamilylibrary.dagger.AppModule
import org.slf4j.LoggerFactory

/**
 * Created by Thomas on 17.09.2017.
 */

class OverviewApplication : FamilyApplication() {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)


    val overviewAppComponent: OverviewAppComponent
        get() = familyAppComponent as OverviewAppComponent

    override fun initDagger() {
        familyAppComponent = initDagger(this)
    }

    protected fun initDagger(application: OverviewApplication): OverviewAppComponent {
        return DaggerOverviewAppComponent.builder().appModule(AppModule(application)).build()
    }
}
