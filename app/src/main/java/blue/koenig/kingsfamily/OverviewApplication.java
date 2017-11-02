package blue.koenig.kingsfamily;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blue.koenig.kingsfamily.dagger.DaggerOverviewAppComponent;
import blue.koenig.kingsfamily.dagger.OverviewAppComponent;
import blue.koenig.kingsfamilylibrary.FamilyApplication;
import blue.koenig.kingsfamilylibrary.dagger.AppModule;

/**
 * Created by Thomas on 17.09.2017.
 */

public class OverviewApplication extends FamilyApplication {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());


    public OverviewAppComponent getOverviewAppComponent() {
        return (OverviewAppComponent) familyAppComponent;
    }

    @Override
    protected void initDagger() {
        familyAppComponent = initDagger(this);
    }

    protected OverviewAppComponent initDagger(OverviewApplication application) {
        return DaggerOverviewAppComponent.builder().appModule(new AppModule(application)).build();
    }
}
