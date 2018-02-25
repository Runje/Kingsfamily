package blue.koenig.kingsfamilylibrary.dagger

import android.content.Context
import blue.koenig.kingsfamilylibrary.model.FamilyConfig
import blue.koenig.kingsfamilylibrary.model.FamilyContextConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Thomas on 19.10.2017.
 */
@Module
class ModelModule {

    @Provides
    @Singleton
    internal fun provideFamilyConfig(context: Context): FamilyConfig {
        val config = FamilyContextConfig(context)
        config.init()
        return config
    }


}
