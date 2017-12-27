package blue.koenig.kingsfamilylibrary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import blue.koenig.kingsfamilylibrary.R;

/**
 * Created by Thomas on 27.12.2017.
 */

public class MultiSelectArrayAdapter extends ArrayAdapter<String> {


    private final boolean[] checked;
    private ItemClickListener listener;

    public MultiSelectArrayAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, ItemClickListener listener) {
        super(context, resource, objects);
        checked = new boolean[objects.size()];
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setOnClickListener(ignored -> {
            checked[position] = !checked[position];
            listener.onClick(getItem(position));
            notifyDataSetChanged();
        });
        if (checked[position])
            view.setBackgroundColor(view.getResources().getColor(R.color.selected));
        else view.setBackgroundColor(view.getResources().getColor(R.color.background));
        return view;
    }

    public List<String> getSelectedObjects() {
        ArrayList<String> selected = new ArrayList<>();
        for (int i = 0; i < checked.length; i++) {
            if (checked[i]) {
                selected.add(getItem(i));
            }
        }

        return selected;
    }

    public interface ItemClickListener {
        void onClick(String item);
    }
}
