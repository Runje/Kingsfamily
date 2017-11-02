package blue.koenig.kingsfamily.model;

import android.content.Context;

import com.koenig.commonModel.User;

import java.util.List;

import javax.inject.Inject;

import blue.koenig.kingsfamily.view.NullOverviewView;
import blue.koenig.kingsfamily.view.OverviewView;
import blue.koenig.kingsfamilylibrary.R;
import blue.koenig.kingsfamilylibrary.model.Utils;
import blue.koenig.kingsfamilylibrary.model.communication.ConnectionEventListener;
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection;
import blue.koenig.kingsfamilylibrary.model.family.FamilyModel;
import blue.koenig.kingsfamilylibrary.model.family.Plugin;
import blue.koenig.kingsfamilylibrary.view.family.FamilyView;
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler;

/**
 * Created by Thomas on 18.09.2017.
 */

public class OverviewModel extends FamilyModel implements ConnectionEventListener {


    @Inject
    public OverviewModel(ServerConnection connection, Context context, LoginHandler handler) {
        super(connection, context, handler);
        initPlugins();

    }

    @Override
    protected void updateFamilymembers(List<User> members) {
        // TODO: save to config/cache
        logger.info("Updating members");
    }

    @Override
    protected void start() {
        logger.info("Model started");
        initPlugins();
    }

    OverviewView getOverviewView() {
        return (OverviewView) view;
    }

    @Override
    protected FamilyView createNullView() {
        return new NullOverviewView();
    }

    protected void initPlugins() {
        plugins.add(new Plugin(context.getString(R.string.finance_name), context.getString(R.string.finance_uri), false));

        for (Plugin plugin : plugins) {
            plugin.setInstalled(Utils.isAppInstalled(context, plugin.getUri()));
        }

        getOverviewView().setPluginsEnabled(plugins);
    }


    public void startFinance() {
        startApp(R.string.finance_uri);
    }


}
