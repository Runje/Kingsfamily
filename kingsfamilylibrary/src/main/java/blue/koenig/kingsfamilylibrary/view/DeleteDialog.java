package blue.koenig.kingsfamilylibrary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.jetbrains.annotations.NotNull;

import blue.koenig.kingsfamilylibrary.R;

/**
 * Created by Thomas on 02.11.2017.
 */

public class DeleteDialog<T> {

    private Context context;
    private DeleteListener<T> listener;
    private String name;
    private T item;

    public DeleteDialog(Context context, String name, T item, @NotNull DeleteListener<T> listener) {
        this.context = context;
        this.listener = listener;
        this.name = name;
        this.item = item;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String string = context.getString(R.string.confirm_delete, name);

        builder.setMessage(string);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                listener.onDelete(item);
            }
        });

        builder.setNegativeButton(R.string.no, null);
        builder.create().show();
    }

    public interface DeleteListener<T> {
        void onDelete(T item);
    }
}
