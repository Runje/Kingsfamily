package blue.koenig.kingsfamily.view.family;

import android.support.annotation.StringRes;

/**
 * Created by Thomas on 18.09.2017.
 */

public class NullFamilyView implements FamilyView {
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
}
