package blue.koenig.kingsfamilylibrary.dagger;

import android.content.Context;

import javax.inject.Named;

import blue.koenig.kingsfamilylibrary.model.Installation;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Thomas on 18.09.2017.
 */
@Module
public class InstallationModule {
    @Provides @Named("INSTALLATION_ID")
    String provideInstallationId(Context context)
    {
        return Installation.id(context);
    }
}
