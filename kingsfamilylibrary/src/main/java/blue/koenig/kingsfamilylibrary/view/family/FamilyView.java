package blue.koenig.kingsfamilylibrary.view.family;

import android.support.annotation.StringRes;

/**
 * Created by Thomas on 18.10.2017.
 */

public interface FamilyView {
    void showText(String text);

    void showText(@StringRes int stringResource);

    void showConnectionStatus(boolean connected);

    void askForNameOrImport();

    void askJoinOrCreateFamily();
}
