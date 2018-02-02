package blue.koenig.kingsfamily.model

import android.content.Context
import blue.koenig.kingsfamily.view.NullOverviewView
import blue.koenig.kingsfamily.view.OverviewView
import blue.koenig.kingsfamilylibrary.R
import blue.koenig.kingsfamilylibrary.model.Utils
import blue.koenig.kingsfamilylibrary.model.communication.ConnectionEventListener
import blue.koenig.kingsfamilylibrary.model.communication.ServerConnection
import blue.koenig.kingsfamilylibrary.model.family.FamilyModel
import blue.koenig.kingsfamilylibrary.model.family.Plugin
import blue.koenig.kingsfamilylibrary.view.family.FamilyView
import blue.koenig.kingsfamilylibrary.view.family.LoginHandler
import com.koenig.commonModel.User
import javax.inject.Inject

/**
 * Created by Thomas on 18.09.2017.
 */

class OverviewModel @Inject
constructor(connection: ServerConnection, context: Context, handler: LoginHandler) : FamilyModel(connection, context, handler), ConnectionEventListener {

    internal val overviewView: OverviewView
        get() = view as OverviewView


    init {
        initPlugins()

    }

    override fun updateFamilymembers(members: List<User>) {
        // TODO: save to config/cache
        logger.info("Updating members")
    }

    override fun start() {
        logger.info("Model started")
        initPlugins()
    }

    override fun createNullView(): FamilyView {
        return NullOverviewView()
    }

    protected fun initPlugins() {
        plugins.add(Plugin(context.getString(R.string.finance_name), context.getString(R.string.finance_uri), false))

        for (plugin in plugins) {
            plugin.isInstalled = Utils.isAppInstalled(context, plugin.uri)
        }

        overviewView.setPluginsEnabled(plugins)
    }


    fun startFinance() {
        startApp(R.string.finance_uri)
    }


}
