package blue.koenig.kingsfamilylibrary.view.family;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.joda.time.DateTime;

import blue.koenig.kingsfamilylibrary.R;
import blue.koenig.kingsfamilylibrary.model.Utils;
import blue.koenig.kingsfamilylibrary.view.BirthdayPickerFragment;
import blue.koenig.kingsfamilylibrary.view.ViewUtils;

/**
 * Created by Thomas on 13.10.2017.
 */

class CreateUserDialog {
    private final Context context;
    private CreateUserListener listener;
    private boolean createUser = true;

    public CreateUserDialog(Context context, CreateUserListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.Name);
        builder.setMessage(R.string.enter_name_or_id);
        final View layout = LayoutInflater.from(context).inflate(R.layout.create_user_dialog, null);
        builder.setView(layout);
        final EditText editName = layout.findViewById(R.id.editTextName);
        final EditText editBirthday = layout.findViewById(R.id.editBirthday);
        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BirthdayPickerFragment birthdayPickerFragment = new BirthdayPickerFragment();
                birthdayPickerFragment.setListener(new ViewUtils.DateListener() {
                    @Override
                    public void onDate(DateTime dateTime) {
                        editBirthday.setText(Utils.INSTANCE.dateToString(dateTime));
                    }
                });

                birthdayPickerFragment.show(context);
            }
        });
        // TODO: only enable button on entering username, birhtday / ID
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                if (createUser) {
                    listener.onCreateUser(name, Utils.INSTANCE.stringToDateTime(editBirthday.getText().toString()));
                } else {
                    // name = Id
                    listener.onImportUser(name);
                }

            }
        });

        AlertDialog dialog = builder.create();
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        RadioGroup radioGroup = layout.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radio_create) {
                    createUser = true;
                    editName.setHint(R.string.name);
                    editBirthday.setVisibility(View.VISIBLE);

                } else {
                    createUser = false;
                    editName.setHint(R.string.user_id);
                    editBirthday.setVisibility(View.INVISIBLE);
                }
            }
        });

        dialog.show();

    }

    public interface CreateUserListener {
        void onCreateUser(String userName, DateTime birthday);

        void onImportUser(String userId);
    }
}
