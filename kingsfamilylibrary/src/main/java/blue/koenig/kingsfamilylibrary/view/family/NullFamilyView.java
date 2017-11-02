package blue.koenig.kingsfamilylibrary.view.family;

import android.support.annotation.StringRes;

/**
 * Created by Thomas on 18.10.2017.
 */

public class NullFamilyView implements FamilyView {
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
    public void askForNameOrImport() {

    }

    @Override
    public void askJoinOrCreateFamily() {

    }
}
