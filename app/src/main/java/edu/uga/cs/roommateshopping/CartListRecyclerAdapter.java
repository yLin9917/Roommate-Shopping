package edu.uga.cs.roommateshopping;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartListRecyclerAdapter extends RecyclerView.Adapter<CartListRecyclerAdapter.cartListHolder>{

    public static final String DEBUG_TAG = "CartListRecyclerAdapter";

    private ToBuyListRecycleAdapter.OnSelectedItemsChangedListener onSelectedItemsChangedListener;

    private final Context context;

    private static List<ToBuyItem> list;

    /**
     * initialize the context and list
     * @param context context
     * @param list ToBuyItem list
     */
    public CartListRecyclerAdapter(Context context, List<ToBuyItem> list ) {
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
    public static class cartListHolder extends RecyclerView.ViewHolder {

        TextView cartItemName, cartItemQuantity;
        /**
         * initialize the elements
         * @param itemView view
         */
        public cartListHolder(View itemView) {
            super(itemView);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemQuantity = itemView.findViewById(R.id.cartItemQuantity);
        }
    }

    @NonNull
    @Override
    public CartListRecyclerAdapter.cartListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlist, parent, false);
        return new cartListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CartListRecyclerAdapter.cartListHolder holder, int position) {

        ToBuyItem item = list.get(position);
        holder.cartItemName.setText(item.getName());
        holder.cartItemQuantity.setText(item.getQuantity());
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }


    /**
     * Return the number of selected items
     * @return the number of selected items
     */
    public List<ToBuyItem> getSelectedItems() {
        return new ArrayList<>(list);
    }

}



