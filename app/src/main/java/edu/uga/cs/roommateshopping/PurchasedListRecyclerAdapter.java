package edu.uga.cs.roommateshopping;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurchasedListRecyclerAdapter extends RecyclerView.Adapter<PurchasedListRecyclerAdapter.purchasedListHolder> {

    private ToBuyListRecycleAdapter.OnSelectedItemsChangedListener onSelectedItemsChangedListener;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference purchasedRef = db.getReference("purchasedList");
    private static Context context = null;
    private List<PurchasedItem> list;

    static List<ToBuyItem> itemList;

    String[] itemNameArray;

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

        TextView boughtBy, itemList, numOfPurchased;
        EditText cost;
        Button editButton;

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
            editButton = itemView.findViewById(R.id.editButton);
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

        // set up the click listener for the edit button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showEditItemPopup(item, holder.itemList, dataSnapshot);
//            }
            @Override
            public void onClick(View v) {
                purchasedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        showEditItemPopup(item, holder.itemList, dataSnapshot);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Database Error", error.getMessage());
                    }
                });
            }
        });
    }

    private void showEditItemPopup(PurchasedItem item, TextView itemList, DataSnapshot dataSnapshot) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.removepopup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Button removeButton = popupView.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText removeItemEditText = popupView.findViewById(R.id.removeItemEditText);
                String removeItem = removeItemEditText.getText().toString();
                DatabaseReference toBuyRef = db.getReference("toBuyList");
                DatabaseReference purchasedRef = db.getReference("purchasedList");

                List<ToBuyItem> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    ToBuyItem item = itemSnapshot.getValue(ToBuyItem.class);
                    item.setId(itemSnapshot.getKey());
                    items.add(item);
                    Log.d("PurchasedListRecyclerAd", "showEditItemPopup dataSnapshot: " + dataSnapshot);
                    Log.d("PurchasedListRecyclerAd", "showEditItemPopup dataSnapshot children: " + dataSnapshot.getChildren());
                    Log.d("item", "showEditItemPopup: " + items);
                }

                if (removeItem.equals("")) {
                    Toast.makeText(context, "Please enter an item name", Toast.LENGTH_SHORT).show();
                } else {
                    String id = null;
                    for (ToBuyItem toBuyItem : items) {
                        if (toBuyItem.getName().equals(removeItem)) {
                            Toast.makeText(context, "test: " + toBuyItem.getName(), Toast.LENGTH_SHORT).show();
                            id = toBuyItem.getId();
                            //add to toBuyList
                            toBuyRef.child(id).setValue(toBuyItem);
                            //remove from purchasedList
                            purchasedRef.child(item.getId()).removeValue();
                            break;
                        }
                    }
                    if (id == null) {
                        Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Item removed from the purchased list", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                }
//                EditText removeItemEditText = popupView.findViewById(R.id.removeItemEditText);
//                String removeItem = removeItemEditText.getText().toString();
//                DatabaseReference toBuyRef = db.getReference("toBuyList");
//                DatabaseReference purchasedRef = db.getReference("purchasedList");
//                List<ToBuyItem> items = item.getItems();
//
//                if (removeItem.equals("")) {
//                    Toast.makeText(context, "Please enter an item name", Toast.LENGTH_SHORT).show();
//                } else {
//                    for (int i = 0; i < itemNameArray.length; i++) {
//                        //Toast.makeText(context, "array =" + itemNameArray[i], Toast.LENGTH_SHORT).show();
//                        if (itemNameArray[i].equals(removeItem)) {
//                            //add to toBuyList
//                            String id = items.get(i).getId();
//                            ToBuyItem instance = items.get(i);
//
//                            toBuyRef.child(id).setValue(instance);
//                            //remove from purchasedList
//                            items.remove(i);
//                            break;
//                        } else if (i == items.size() - 1) {
//                            Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
//                    purchasedRef.child(item.getId()).child("items").setValue(items);
//                    itemList.setText(itemList(item));
//                    popupWindow.dismiss();
//                }
            }
        });
        popupWindow.showAtLocation(itemList, Gravity.CENTER, 0, 0);
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
        List<ToBuyItem> items = item.getItems();
        String str = "";
        for (int i = 0; i < items.size() - 1; i++) {
            str += items.get(i) + ", ";
        }
        str += items.get(items.size() - 1);

        String regex = "name=([a-zA-Z]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        List<String> itemNames = new ArrayList<>();
        while (matcher.find()) {
            String itemName = matcher.group(1);
            itemNames.add(itemName);
        }

        itemNameArray = itemNames.toArray(new String[0]);
        String names = "";
        for (int i = 0; i < itemNameArray.length - 1; i++) {
            names += itemNameArray[i] + ", ";
        }
        names += itemNameArray[itemNameArray.length - 1];

        return names;
    }
private static List<ToBuyItem> firebaseToBuyList(DataSnapshot dataSnapshot) {

    List<ToBuyItem> list = new ArrayList<>();
    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
        Map<String, Object> snapshotValue = (Map<String, Object>) snapshot.getValue();
        ToBuyItem toBuyItem = new ToBuyItem(
                snapshotValue.get("name").toString(),
                Integer.parseInt(snapshotValue.get("quantity").toString()),
                (Boolean) snapshotValue.get("selected")
        );
        toBuyItem.setId(snapshotValue.get("id").toString());
        list.add(toBuyItem);
    }
    return list;
}

}
