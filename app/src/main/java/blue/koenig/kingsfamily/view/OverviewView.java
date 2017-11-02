package blue.koenig.kingsfamily.view;

import java.util.List;

import blue.koenig.kingsfamilylibrary.model.family.Plugin;
import blue.koenig.kingsfamilylibrary.view.family.FamilyView;

/**
 * Created by Thomas on 18.09.2017.
 */

public interface OverviewView extends FamilyView {


    void setPluginsEnabled(List<Plugin> plugins);
}
