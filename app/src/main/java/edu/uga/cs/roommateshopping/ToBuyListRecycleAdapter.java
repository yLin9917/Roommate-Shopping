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
        holder.itemName.setText(item.getName());
        holder.checkBox.setChecked(item.isSelected());
        holder.itemQuantity.setText( item.getQuantity());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setSelected(isChecked);
                // update the selected for the item to the firebase
                String id = item.getId();
                DatabaseReference ref = FirebaseDatabase
                        .getInstance()
                        .getReference("toBuyList")
                        .child(id);
                ref.child("selected").setValue(isChecked);

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
