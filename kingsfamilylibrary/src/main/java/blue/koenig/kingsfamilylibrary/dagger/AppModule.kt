package blue.koenig.kingsfamilylibrary.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Thomas on 17.09.2017.
 */
@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }
}
