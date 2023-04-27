package edu.uga.cs.roommateshopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchasedListRecyclerAdapter extends RecyclerView.Adapter<PurchasedListRecyclerAdapter.purchasedListHolder> {


    private final Context context;
    private PurchasedItem item;

    /**
     * initialize the context and list
     * @param context context
     * @param item ToBuyItem list
     */
    public PurchasedListRecyclerAdapter(Context context, PurchasedItem item ) {
        this.context = context;
        this.item = item;
    }

    /**
     * Holder class
     */
    public static class purchasedListHolder extends RecyclerView.ViewHolder {

        TextView boughtBy, cost, numOfPurchased, itemList;

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
        if (item != null) {
            holder.boughtBy.setText("Bought by: " + item.getName());
            holder.numOfPurchased.setText("Purchased #" + item.getPurchasedNum());
            holder.itemList.setText(itemList());
            holder.cost.setText(String.valueOf(item.getCost()));
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private String itemList() {
        List<ToBuyItem> items = item.getItems();
        String str = "";
        for (int i = 0; i < items.size() - 1; i++) {
            str += items.get(i).getName() + ", ";
        }
        str += items.get(items.size() - 1).getName();
        return str;
    }

}
