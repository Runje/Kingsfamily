package blue.koenig.kingsfamilylibrary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
    protected ArrayAdapter<String> adapter;
    protected AlertDialog dialog;
    private ListView sublistView;
    private ArrayAdapter<String> subAdapter;
    private String main;

    public PickItemDialog(Context context, String title, List<String> items, @NonNull PickListener listener) {
        this.context = context;
        this.listener = listener;
        this.title = title;
        this.items = items;
    }

    public void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View layout = LayoutInflater.from(context).inflate(R.layout.pick_item, null);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        subAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        builder.setView(layout);
        builder.setTitle(title);
        builder.setNegativeButton(R.string.cancel, null);
        dialog = builder.create();
        listView = layout.findViewById(R.id.listView_names);
        listView.setAdapter(adapter);
        sublistView = layout.findViewById(R.id.listView_subs);
        sublistView.setAdapter(subAdapter);
        modifyDialog(layout);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = adapter.getItem(position);

            if (listener.hasSubs()) {
                subAdapter.clear();
                subAdapter.addAll(listener.getSubs(item));
                main = item;
            } else {
                listener.onPick(item);
                dialog.cancel();
            }
        });

        sublistView.setOnItemClickListener((adapterView, view, i, l) -> listener.onPickSub(main, subAdapter.getItem(i)));
        dialog.show();
    }

    protected void modifyDialog(View layout) {
        // For subclasses
    }

    public interface PickListener {
        void onPick(String item);

        String getSubs(String item);

        boolean hasSubs();

        void onPickSub(String main, String sub);
    }
}
