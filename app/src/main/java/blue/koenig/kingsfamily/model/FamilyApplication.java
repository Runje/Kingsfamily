package blue.koenig.kingsfamily.model;

import android.app.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blue.koenig.kingsfamily.dagger.AppComponent;
import blue.koenig.kingsfamily.dagger.AppModule;
import blue.koenig.kingsfamily.dagger.DaggerAppComponent;

/**
 * Created by Thomas on 17.09.2017.
 */

public class FamilyApplication extends Application {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.info("Create Application");
        appComponent = initDagger(this);
    }
    protected AppComponent initDagger(FamilyApplication application) {
        return DaggerAppComponent.builder().appModule(new AppModule(application)).build();
    }
}
