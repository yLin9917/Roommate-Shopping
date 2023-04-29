package edu.uga.cs.roommateshopping;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ToBuyListRecycleAdapter extends RecyclerView.Adapter<ToBuyListRecycleAdapter.toBuyListHolder>{

    public static final String DEBUG_TAG = "toBuyListRecycleAdapter";

    private OnSelectedItemsChangedListener onSelectedItemsChangedListener;
    // create collection for the program
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference toBuyRef = db.getReference("toBuyList");
    private final Context context;

    private static List<ToBuyItem> list;

    /**
     * initialize the context and list
     * @param context context
     * @param list ToBuyItem list
     */
    public ToBuyListRecycleAdapter(Context context, List<ToBuyItem> list ) {
        this.context = context;
        this.list = list;
    }

    /**
     * reset the list and call notifyDataSetChanged
     * @param list the new list
     */
    public void setData(List<ToBuyItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * Holder class
     */
    public static class toBuyListHolder extends RecyclerView.ViewHolder {

        CardView card_view;
        EditText itemName, itemQuantity;
        CheckBox checkBox;

        /**
         * initialize the elements
         * @param itemView view
         */
        public toBuyListHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            card_view = itemView.findViewById(R.id.card_view);

            itemName.setSingleLine(true);
            itemQuantity.setSingleLine(true);
        }
    }

    @NonNull
    @Override
    public toBuyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buylist, parent, false);
        return new toBuyListHolder(view);
    }

    @Override
    public void onBindViewHolder(toBuyListHolder holder, int position) {

        ToBuyItem item = list.get(position);
        String id = item.getId();
        holder.itemName.setText(item.getName());
        holder.checkBox.setChecked(item.isSelected());
        holder.itemQuantity.setText( item.getQuantity());

        // set up the enter handler for the itemName EditText
        holder.itemName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String newContent = holder.itemName.getText().toString();
                    toBuyRef.child(id).child("name").setValue(newContent);
                    return true;
                }
                return false;
            }
        });

        // set up the enter handler for the itemQuantity EditText
        holder.itemQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (holder.itemQuantity.getText().toString().equals("")) {
                        holder.itemQuantity.setText("1");
                    }
                    String newContent = holder.itemQuantity.getText().toString();
                    toBuyRef.child(id).child("quantity").setValue(newContent);
                    return true;
                }
                return false;
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setSelected(isChecked);
                if (onSelectedItemsChangedListener != null) {
                    onSelectedItemsChangedListener.onSelectedItemsChanged(getSelectedItems().size());
                }
            }
        });

    }

    /**
     * Return the number of selected items
     * @return the number of selected items
     */
    public List<ToBuyItem> getSelectedItems() {
        List<ToBuyItem> selectedItems = new ArrayList<>();
        for (ToBuyItem item : list) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemCount() {
        if( list != null )
            return list.size();
        else
            return 0;
    }


    /**
     * Sets an {@link OnSelectedItemsChangedListener} to be notified when the selected items change.
     */
    public interface OnSelectedItemsChangedListener {
        void onSelectedItemsChanged(int count);
    }

    /**
     * Interface definition for a callback to be invoked when the number of selected items changes.
     */
    public void setOnSelectedItemsChangedListener(OnSelectedItemsChangedListener listener) {
        this.onSelectedItemsChangedListener = listener;
    }


}
