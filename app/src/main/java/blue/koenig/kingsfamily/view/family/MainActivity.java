package blue.koenig.kingsfamily.view.family;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import blue.koenig.kingsfamily.R;
import blue.koenig.kingsfamily.model.FamilyApplication;
import blue.koenig.kingsfamily.model.family.FamilyModel;
import blue.koenig.kingsfamily.presenter.family.FamilyPresenter;

public class MainActivity  extends AppCompatActivity implements FamilyView{

    @Inject
    FamilyModel model;

    private FamilyPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((FamilyApplication) getApplication()).getAppComponent().inject(this);
        presenter = model.getPresenter();
        model.start();
    }

    @Override
    public void askForName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Name);
        //builder.setMessage(R.string.enter_name_or_import);
        final View layout = LayoutInflater.from(this).inflate(R.layout.enter_name, null);
        builder.setView(layout);
        final EditText editName = (EditText) layout.findViewById(R.id.editTextName);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                presenter.enteredName(name);
            }
        });

        builder.setNegativeButton(R.string.import_file, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.importFile();
            }
        });
        builder.show();
    }

    @Override
    public void askJoinOrCreateFamily() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Name);
        //builder.setMessage(R.string.enter_name_or_import);
        final View layout = LayoutInflater.from(this).inflate(R.layout.enter_name, null);
        builder.setView(layout);
        final EditText editName = (EditText) layout.findViewById(R.id.editTextName);
        builder.setPositiveButton(R.string.createFamily, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                presenter.createFamily(name);
            }
        });

        builder.setNegativeButton(R.string.joinFamily, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                presenter.joinFamily(name);
            }
        });
        builder.show();
    }

    @Override
    public void showText(String text) {

    }
}
