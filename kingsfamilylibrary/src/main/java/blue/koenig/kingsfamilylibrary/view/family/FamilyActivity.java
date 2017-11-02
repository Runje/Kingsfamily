package blue.koenig.kingsfamilylibrary.view.family;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blue.koenig.kingsfamilylibrary.R;
import blue.koenig.kingsfamilylibrary.model.family.FamilyModel;

/**
 * Created by Thomas on 18.10.2017.
 */

public abstract class FamilyActivity extends AppCompatActivity implements FamilyView {

    protected TextView connectionStatus;
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    protected FamilyModel model;

    public void showText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FamilyActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showText(@StringRes final int stringResource) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FamilyActivity.this, stringResource, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showConnectionStatus(final boolean connected) {
        if (connectionStatus == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connected) {
                    connectionStatus.setText(R.string.connected);
                    connectionStatus.setTextColor(getResources().getColor(R.color.green));
                } else {
                    connectionStatus.setText(R.string.not_connected);
                    connectionStatus.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = createModel();
    }

    protected abstract FamilyModel createModel();

    @Override
    public void askForNameOrImport() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CreateUserDialog dialog = new CreateUserDialog(FamilyActivity.this, new CreateUserDialog.CreateUserListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(FamilyActivity.this);
                builder.setTitle(R.string.Name);
                //builder.setMessage(R.string.enter_name_or_import);
                final View layout = LayoutInflater.from(FamilyActivity.this).inflate(R.layout.enter_name, null);
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
    protected void onStart() {
        logger.info("OnStart");
        super.onStart();
        model.attachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        logger.info("OnStop");
        model.detachView();
    }
}
