package blue.koenig.kingsfamily.view.family;

import android.support.annotation.StringRes;

/**
 * Created by Thomas on 18.09.2017.
 */

public interface FamilyView {
    void askForNameOrImport();

    void askJoinOrCreateFamily();

    void showText(String text);

    void showText(@StringRes int stringResource);

    void showConnectionStatus(boolean connected);

}
