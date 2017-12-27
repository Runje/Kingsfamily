package blue.koenig.kingsfamilylibrary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import blue.koenig.kingsfamilylibrary.R;

/**
 * Created by Thomas on 02.11.2017.
 */

public class PickItemDialog {

    protected Context context;
    protected PickListener listener;
    protected String title;
    protected List<String> items;
    protected ListView listView;
    protected MultiSelectArrayAdapter adapter;
    protected AlertDialog dialog;
    private boolean multiselect;


    public PickItemDialog(Context context, String title, List<String> items, boolean multiselect, @NonNull PickListener listener) {
        this.context = context;
        this.multiselect = multiselect;
        this.listener = listener;
        this.title = title;
        this.items = items;
    }

    public void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View layout = LayoutInflater.from(context).inflate(R.layout.pick_item, null);

        builder.setView(layout);
        builder.setTitle(title);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            listener.onMultiPick(adapter.getSelectedObjects());
        });
        dialog = builder.create();


        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        adapter = new MultiSelectArrayAdapter(context, android.R.layout.simple_list_item_1, items, (item -> {
            if (!multiselect) {
                listener.onPick(item);
                dialog.cancel();
            } else {

                if (adapter.getSelectedObjects().size() > 0) {
                    positiveButton.setEnabled(true);
                } else {
                    positiveButton.setEnabled(false);
                }
            }
        }));
        listView = layout.findViewById(R.id.listView_names);

        listView.setAdapter(adapter);

        positiveButton.setEnabled(false);

    }

    public interface PickListener {
        void onPick(String item);

        void onMultiPick(List<String> items);
    }
}
