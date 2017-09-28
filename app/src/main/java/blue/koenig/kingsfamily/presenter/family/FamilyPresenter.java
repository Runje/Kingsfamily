package blue.koenig.kingsfamily.presenter.family;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import blue.koenig.kingsfamily.view.family.FamilyView;
import blue.koenig.kingsfamily.view.family.NullFamilyView;

/**
 * Created by Thomas on 18.09.2017.
 */

public class FamilyPresenter {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private FamilyView view;

    public void setListener(FamilyPresenterListener listener) {
        this.listener = listener;
    }

    private FamilyPresenterListener listener;

    @Inject
    public FamilyPresenter() {
        this.view = new NullFamilyView();
        listener = new NullFamilyPresenterListener();
    }

    public void askForName() {
        logger.info("Asking for a name...");
        view.askForName();

    }

    public void say(String text) {
        view.showText(text);
    }

    public void attachView(FamilyView overview) {
        this.view = overview;
        listener.resume();
    }

    public void detachView() {
        this.view = new NullFamilyView();
        listener.pause();
    }

    public void enteredName(String name) {
        listener.onCreatingUser(name);
    }

    public void importFile() {
        // TODO:
    }

    public void joinOrCreateFamily() {
        view.askJoinOrCreateFamily();
    }

    public void createFamily(String name) {
        listener.createFamily(name);
    }

    public void joinFamily(String name) {
        listener.joinFamily(name);
    }
}
