package blue.koenig.kingsfamilylibrary.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import blue.koenig.kingsfamilylibrary.R;

/**
 * Created by Thomas on 02.11.2017.
 */

public class PickAndEditItemDialog extends PickItemDialog {

    PickAndEditListener pickAndEditListener;

    public PickAndEditItemDialog(Context context, String title, List<String> items, @NotNull PickAndEditListener listener) {
        super(context, title, items, listener);
    }

    @Override
    protected void modifyDialog(View layout) {
        layout.findViewById(R.id.add_layout).setVisibility(View.VISIBLE);
        Button buttonAdd = layout.findViewById(R.id.button_add);
        final EditText categoryToAdd = layout.findViewById(R.id.edit_category);
        categoryToAdd.addTextChangedListener(new TextValidator(categoryToAdd) {
            @Override
            public void validate(TextView textView, String text) {
                if (pickAndEditListener.isValid(text)) {
                    textView.setError(null);
                } else {
                    textView.setError(context.getString(R.string.invalid));
                }
            }
        });
        buttonAdd.setOnClickListener(v -> {
            String newItem = categoryToAdd.getText().toString();
            if (pickAndEditListener.isValid(newItem)) {
                pickAndEditListener.addItem(newItem);
                adapter.add(newItem);
            }
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // TODO: add delete icon instead of long click
            String item = items.get(position);
            new DeleteDialog<String>(context, item, item, (i) -> pickAndEditListener.onDeleteItem(i));
            return true;
        });

    }

    public interface PickAndEditListener extends PickListener {
        void onDeleteItem(String item);

        boolean isValid(String newItem);

        void addItem(String newItem);
    }
}
