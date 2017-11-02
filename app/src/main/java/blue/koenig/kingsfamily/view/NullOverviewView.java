package blue.koenig.kingsfamily.view;

import android.support.annotation.StringRes;

import java.util.List;

import blue.koenig.kingsfamilylibrary.model.family.Plugin;

/**
 * Created by Thomas on 18.09.2017.
 */

public class NullOverviewView implements OverviewView {
    @Override
    public void askForNameOrImport() {

    }

    @Override
    public void askJoinOrCreateFamily() {

    }

    @Override
    public void showText(String text) {

    }

    @Override
    public void showText(@StringRes int stringResource) {

    }

    @Override
    public void showConnectionStatus(boolean connected) {

    }

    @Override
    public void setPluginsEnabled(List<Plugin> plugins) {

    }
}
