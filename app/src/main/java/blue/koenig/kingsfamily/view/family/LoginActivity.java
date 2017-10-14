package blue.koenig.kingsfamily.view.family;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import blue.koenig.kingsfamily.R;
import blue.koenig.kingsfamily.model.FamilyApplication;
import blue.koenig.kingsfamily.model.family.FamilyModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements FamilyView {
    @Inject
    FamilyModel model;
    @BindView(R.id.connectionStatus)
    TextView textConnectionStatus;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((FamilyApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        // Always cast your custom Toolbar here, and set it as the ActionBar.
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
    }

    @Override
    public void askForNameOrImport() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CreateUserDialog dialog = new CreateUserDialog(LoginActivity.this, new CreateUserDialog.CreateUserListener() {
                    @Override
                    public void onCreateUser(String userName, DateTime birthday) {
                        model.onCreatingUser(userName, birthday);
                    }

                    @Override
                    public void onImportUser(String userId) {
                        model.importUser(userId);

                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public void askJoinOrCreateFamily() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(R.string.Name);
                //builder.setMessage(R.string.enter_name_or_import);
                final View layout = LayoutInflater.from(LoginActivity.this).inflate(R.layout.enter_name, null);
                builder.setView(layout);
                final EditText editName = layout.findViewById(R.id.editTextName);
                builder.setPositiveButton(R.string.createFamily, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editName.getText().toString();
                        model.createFamily(name);
                    }
                });

                builder.setNegativeButton(R.string.joinFamily, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editName.getText().toString();
                        model.joinFamily(name);
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public void showText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void showText(@StringRes final int stringResource) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, stringResource, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void showConnectionStatus(final boolean connected) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connected) {
                    textConnectionStatus.setText(R.string.connected);
                    textConnectionStatus.setTextColor(getResources().getColor(R.color.green));
                } else {
                    textConnectionStatus.setText(R.string.not_connected);
                    textConnectionStatus.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        logger.info("OnResume");
        super.onResume();
        model.attachView(this);
    }

    @Override
    protected void onPause() {
        logger.info("OnPause");
        super.onPause();
        model.detachView();
    }
}