package blue.koenig.kingsfamilylibrary.view.family;

import android.support.annotation.StringRes;

import java.util.List;

import blue.koenig.kingsfamilylibrary.model.family.Plugin;

/**
 * Created by Thomas on 18.09.2017.
 */

public interface FamilyView {
    void askForNameOrImport();

    void askJoinOrCreateFamily();

    void showText(String text);

    void showText(@StringRes int stringResource);

    void showConnectionStatus(boolean connected);

    void setPluginsEnabled(List<Plugin> plugins);
}
