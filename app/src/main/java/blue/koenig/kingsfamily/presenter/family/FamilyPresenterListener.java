package blue.koenig.kingsfamily.presenter.family;

/**
 * Created by Thomas on 18.09.2017.
 */

public interface FamilyPresenterListener {
    void resume();

    void pause();

    void onCreatingUser(String name);

    void createFamily(String name);

    void joinFamily(String name);
}
