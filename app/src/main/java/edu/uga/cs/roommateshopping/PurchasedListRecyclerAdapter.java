package edu.uga.cs.roommateshopping;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PurchasedListRecyclerAdapter extends RecyclerView.Adapter<PurchasedListRecyclerAdapter.purchasedListHolder> {

    private ToBuyListRecycleAdapter.OnSelectedItemsChangedListener onSelectedItemsChangedListener;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference purchasedRef = db.getReference("purchasedList");
    private final Context context;
    private List<PurchasedItem> list;

    /**
     * initialize the context and list
     * @param context context
     * @param purchasedItems ToBuyItem list
     */
    public PurchasedListRecyclerAdapter(Context context, List<PurchasedItem> purchasedItems ) {
        this.context = context;
        this.list = purchasedItems;
    }

    /**
     * Holder class
     */
    public static class purchasedListHolder extends RecyclerView.ViewHolder {

        TextView boughtBy, numOfPurchased, itemList;
        EditText cost;

        /**
         * initialize the elements
         * @param itemView view
         */
        public purchasedListHolder(@NonNull View itemView) {
            super(itemView);
            boughtBy = itemView.findViewById(R.id.boughtBy);
            cost = itemView.findViewById(R.id.cost);
            itemList = itemView.findViewById(R.id.itemList);
            numOfPurchased = itemView.findViewById(R.id.numOfPurchased);
        }
    }

    @NonNull
    @Override
    public PurchasedListRecyclerAdapter.purchasedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settlecost, parent, false);
        return new purchasedListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedListRecyclerAdapter.purchasedListHolder holder, int position) {

        PurchasedItem item = list.get(position);
        String id = item.getId();
        holder.boughtBy.setText("Bought by: " + item.getName());
        holder.numOfPurchased.setText("Purchased #" + ++position);
        holder.itemList.setText(itemList(item));
        holder.cost.setText(String.format("%.2f", item.getCost()));


        // set up the enter handler for the cost EditText
        holder.cost.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (holder.cost.getText().toString().equals("")) {
                        holder.cost.setText("1");
                    }
                    String newContent = holder.cost.getText().toString();
                    purchasedRef.child(id).child("cost").setValue(newContent);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * setter method for list
     * @param list list
     */
    public void setList(List<PurchasedItem> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    private String itemList(PurchasedItem item) {
        List<String> items = item.getItems();
        String str = "";
        for (int i = 0; i < items.size() - 1; i++) {
            str += items.get(i) + ", ";
        }
        str += items.get(items.size() - 1);
        return str;
    }


}
