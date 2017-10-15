package blue.koenig.kingsfamilylibrary.dagger;

import javax.inject.Singleton;

import blue.koenig.kingsfamilylibrary.view.family.LoginActivity;
import dagger.Component;

/**
 * Created by Thomas on 17.09.2017.
 */
@Singleton
@Component(modules = {AppModule.class, ConnectionModule.class, InstallationModule.class})
public interface AppComponent {
    void inject(LoginActivity target);
}
