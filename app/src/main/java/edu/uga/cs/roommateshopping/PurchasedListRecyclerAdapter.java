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

        PurchasedItem item = list.get(position);

        holder.boughtBy.setText("Bought by: " + item.getName());
        holder.numOfPurchased.setText("Purchased #" + ++position);
        holder.itemList.setText(itemList(item));
        holder.cost.setText(String.valueOf(item.getCost()));
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
