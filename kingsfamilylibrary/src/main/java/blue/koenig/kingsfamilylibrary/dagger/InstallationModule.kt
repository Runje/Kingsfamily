package blue.koenig.kingsfamilylibrary.dagger

import android.content.Context
import blue.koenig.kingsfamilylibrary.model.Installation
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Thomas on 18.09.2017.
 */
@Module
class InstallationModule {
    @Provides
    @Named("INSTALLATION_ID")
    internal fun provideInstallationId(context: Context): String {
        return Installation.id(context)
    }
}
