package edu.uga.cs.roommateshopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class toBuyListRecycleAdapter extends RecyclerView.Adapter<toBuyListRecycleAdapter.toBuyListHolder>{

    public static final String DEBUG_TAG = "toBuyListRecycleAdapter";

    private final Context context;

    private List<ToBuyItem> list;

    public toBuyListRecycleAdapter(Context context, List<ToBuyItem> list ) {
        this.context = context;
        this.list = list;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    public static class toBuyListHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemQuantity;

        public toBuyListHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
        }
    }

    @NonNull
    @Override
    public toBuyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buylist, parent, false);
        return new toBuyListHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(toBuyListHolder holder, int position) {

        ToBuyItem item = list.get(position);
        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText( item.getQuantity());
    }

    @Override
    public int getItemCount() {
        if( list != null )
            return list.size();
        else
            return 0;
    }

}
