package blue.koenig.kingsfamily.dagger;

import javax.inject.Singleton;

import blue.koenig.kingsfamily.view.OverviewActivity;
import blue.koenig.kingsfamilylibrary.dagger.AppModule;
import blue.koenig.kingsfamilylibrary.dagger.ConnectionModule;
import blue.koenig.kingsfamilylibrary.dagger.FamilyAppComponent;
import blue.koenig.kingsfamilylibrary.dagger.InstallationModule;
import dagger.Component;

/**
 * Created by Thomas on 17.09.2017.
 */
@Singleton
@Component(modules = {AppModule.class, ConnectionModule.class, InstallationModule.class})
public interface OverviewAppComponent extends FamilyAppComponent {
    void inject(OverviewActivity target);
}
