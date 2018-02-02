package blue.koenig.kingsfamily.view

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.TextView
import blue.koenig.kingsfamily.OverviewApplication
import blue.koenig.kingsfamily.model.OverviewModel
import blue.koenig.kingsfamilylibrary.R
import blue.koenig.kingsfamilylibrary.model.family.FamilyModel
import blue.koenig.kingsfamilylibrary.model.family.Plugin
import blue.koenig.kingsfamilylibrary.view.family.FamilyActivity
import javax.inject.Inject

class OverviewActivity : FamilyActivity(), OverviewView {

    private lateinit var buFinance: Button

    internal val overviewModel: OverviewModel
        get() = model as OverviewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as OverviewApplication).overviewAppComponent.inject(this)
        // Always cast your custom Toolbar here, and set it as the ActionBar.
        val tb = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(tb)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)
        connectionStatus = findViewById<TextView>(R.id.connectionStatus)
        buFinance = findViewById(R.id.buFinance)
    }

    override fun createModel(): FamilyModel {
        return model
    }

    @Inject
    fun provideModel(model: OverviewModel) {
        this.model = model
    }

    override fun setPluginsEnabled(plugins: List<Plugin>) {
        for (plugin in plugins) {
            if (plugin.name == buFinance.text.toString()) {
                buFinance.isEnabled = plugin.isInstalled
            }
        }
    }

    fun clickFinance(view: View) {
        overviewModel.startFinance()
    }
}
