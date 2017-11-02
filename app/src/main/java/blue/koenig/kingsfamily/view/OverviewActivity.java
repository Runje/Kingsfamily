package blue.koenig.kingsfamily.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.List;

import javax.inject.Inject;

import blue.koenig.kingsfamily.OverviewApplication;
import blue.koenig.kingsfamily.model.OverviewModel;
import blue.koenig.kingsfamilylibrary.R;
import blue.koenig.kingsfamilylibrary.model.family.FamilyModel;
import blue.koenig.kingsfamilylibrary.model.family.Plugin;
import blue.koenig.kingsfamilylibrary.view.family.FamilyActivity;

public class OverviewActivity extends FamilyActivity implements OverviewView {

    Button buFinance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((OverviewApplication) getApplication()).getOverviewAppComponent().inject(this);
        // Always cast your custom Toolbar here, and set it as the ActionBar.
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
        connectionStatus = findViewById(R.id.connectionStatus);
        buFinance = findViewById(R.id.buFinance);
    }

    @Override
    protected FamilyModel createModel() {
        return model;
    }

    @Inject
    protected void provideModel(OverviewModel model) {
        this.model = model;
    }

    OverviewModel getOverviewModel() {
        return (OverviewModel) model;
    }

    @Override
    public void setPluginsEnabled(List<Plugin> plugins) {
        for (Plugin plugin : plugins) {
            if (plugin.getName().equals(buFinance.getText().toString())) {
                buFinance.setEnabled(plugin.isInstalled());
            }
        }
    }

    public void clickFinance(View view) {
        getOverviewModel().startFinance();
    }
}
