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

import java.util.Iterator;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurchasedListRecyclerAdapter extends RecyclerView.Adapter<PurchasedListRecyclerAdapter.purchasedListHolder> {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference purchasedRef = db.getReference("purchasedList");
    private static Context context = null;
    private List<PurchasedItem> list;

    String[] itemNameArray;

    /**
     * initialize the context and list
     *
     * @param context        context
     * @param purchasedItems ToBuyItem list
     */
    public PurchasedListRecyclerAdapter(Context context, List<PurchasedItem> purchasedItems) {
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
         *
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

    /**
     * on create view holder
     * @param parent parent
     * @param viewType viewType
     */
    @NonNull
    @Override
    public PurchasedListRecyclerAdapter.purchasedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settlecost, parent, false);
        return new purchasedListHolder(view);
    }

    /**
     * on bind view holder
     * @param holder holder
     * @param position position
     */
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
            // if the user presses enter, update the cost
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

    /**
     * show the popup window for editing the item list
     * @param item current item
     * @param itemList item list
     * @param dataSnapshot data snapshot
     */
    private void showEditItemPopup(PurchasedItem item, TextView itemList, DataSnapshot dataSnapshot) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.removepopup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Button removeButton = popupView.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            // if the user presses the remove button, remove the item from list and add to ToBuyList
            @Override
            public void onClick(View view) {

                EditText removeItemEditText = popupView.findViewById(R.id.removeItemEditText);
                String removeItem = removeItemEditText.getText().toString().trim();
                DatabaseReference toBuyRef = db.getReference("toBuyList");
                DatabaseReference purchasedRef = db.getReference("purchasedList");
                Query query = purchasedRef.child(item.getId()).child("items").orderByChild("name").equalTo(removeItem);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            String uniqueId = toBuyRef.push().getKey();
                            Map<String, Object> snapshotValue = (Map<String, Object>) itemSnapshot.getValue();
                            ToBuyItem toBuyItem = new ToBuyItem(
                                    snapshotValue.get("name").toString(),
                                    Integer.parseInt(snapshotValue.get("quantity").toString()),
                                    (Boolean) snapshotValue.get("selected"),
                                    uniqueId
                            );
                            toBuyRef.child(uniqueId).setValue(toBuyItem);

                            itemSnapshot.getRef().removeValue();
                        }
                        Toast.makeText(context, "Item removed from the purchased list", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });

                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0) {
                            purchasedRef.child(item.getId()).removeValue();
                            popupWindow.dismiss();
                            purchasedRef.child(item.getId()).child("items").removeEventListener(this);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                purchasedRef.child(item.getId()).child("items").addValueEventListener(listener);

                itemList.setText(itemList(item));
                //popupWindow.dismiss();

            }

        });
        popupWindow.showAtLocation(itemList, Gravity.CENTER, 0, 0);

    }

    /**
     * setter method for list
     *
     * @param list list
     */
    public void setList(List<PurchasedItem> list) {
        this.list = list;
    }

    /**
     * getter method for item array size
     *
     * @return list size
     */
    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    /**
     * getting the item array from the PurchasedItem object
     *
     * @param item PurchasedItem object
     * @return names string of item names
     */
    private String itemList(PurchasedItem item) {
        List<ToBuyItem> items = item.getItems();
        if (items == null) return null;
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

}
