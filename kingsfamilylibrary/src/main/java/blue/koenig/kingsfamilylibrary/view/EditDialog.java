package blue.koenig.kingsfamilylibrary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import blue.koenig.kingsfamilylibrary.R;

/**
 * Created by Thomas on 02.11.2017.
 */

public abstract class EditDialog<T> {

    protected Context context;
    protected EditListener<T> listener;
    protected T item;
    private AlertDialog dialog;
    private Button confirmButton;

    public EditDialog(Context context, T item, /*@NotNull*/ EditListener<T> listener) {
        this.context = context;
        this.listener = listener;
        this.item = item;
    }


    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(makeView(LayoutInflater.from(context)));

        builder.setPositiveButton(R.string.edit, null);

        builder.setNegativeButton(R.string.cancel, null);
        adaptBuilder(builder);
        dialog = builder.create();
        dialog.show();
        confirmButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        confirmButton.setOnClickListener((v) -> {

            // only dismiss item if its valid
            if (listener.validate(item)) {
                listener.onEdit(item);
                dialog.dismiss();
            } else {
                Toast.makeText(context, listener.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    protected void adaptBuilder(AlertDialog.Builder builder) {
        // to be overriden by subclasses
    }

    protected void setEnabledConfirmButton(boolean enabled) {
        if (confirmButton != null) {
            confirmButton.setEnabled(enabled);
        }

    }

    protected abstract View makeView(LayoutInflater inflater);

    public interface EditListener<T> {
        void onEdit(T item);

        boolean validate(T item);

        @StringRes
        int getErrorMessage();
    }
}
