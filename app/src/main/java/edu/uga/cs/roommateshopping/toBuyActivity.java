package edu.uga.cs.roommateshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class toBuyActivity extends AppCompatActivity {

    private static final String TAG = "toBuyActivity";

    TextView itemSelected;

    Button addToCartButton, checkoutButton, toBuyHome;
    private static List<ToBuyItem> toBuyList, cartList, purchasedList;
    private RecyclerView toBuyListRecyclerView;
    private ToBuyListRecycleAdapter toBuyListRecycleAdapter;
    private RecyclerView cartListRecyclerView;
    private CartListRecyclerAdapter cartListRecyclerAdapter;


    // create collection for the program
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference toBuyRef = db.getReference("toBuyList");
    DatabaseReference cartRef = db.getReference("cartList");
    DatabaseReference purchasedRef = db.getReference("purchasedList");

    /**
     * on create method for the toBuyActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_buy);

        // set up the adapter and recyclerview for the cartList
        cartListRecyclerView = findViewById(R.id.cartList);
        cartListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartListRecyclerAdapter = new CartListRecyclerAdapter(this, cartList);
        ItemTouchHelper cartTouchHelper = new ItemTouchHelper(cartListTouchCallback);
        cartTouchHelper.attachToRecyclerView(cartListRecyclerView);
        cartListRecyclerView.setAdapter(cartListRecyclerAdapter);

        // set up all the buttons and implement their handlers
        addToCartButton = findViewById(R.id.addToCartButton);
        addButtonHandler();
        itemSelected = findViewById(R.id.itemSelected);
        addToCartButtonHandler();
        checkoutButton = findViewById(R.id.checkout);
        checkoutButtonHandler();
        toBuyHome = findViewById(R.id.toBuyHome);
        toBuyHomeButtonHandler();


        // set up the adapter and recyclerview for the toBuyList
        toBuyListRecyclerView = findViewById(R.id.toBuyList);
        toBuyListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toBuyListRecycleAdapter = new ToBuyListRecycleAdapter(this, toBuyList);
        toBuyListRecycleAdapter.setOnSelectedItemsChangedListener(new ToBuyListRecycleAdapter.OnSelectedItemsChangedListener() {
            @Override
            public void onSelectedItemsChanged(int count) {
                itemSelectedUpdate();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(toBuyItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(toBuyListRecyclerView);
        toBuyListRecyclerView.setAdapter(toBuyListRecycleAdapter);

        // add the eventListener for the toBuyRef
        toBuyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toBuyList = firebaseToBuyList(dataSnapshot);
                toBuyListRecycleAdapter.setData(toBuyList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        // add the eventListener for the cartRef
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartList = firebaseToBuyList(dataSnapshot);
                cartListRecyclerAdapter.setData(cartList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    /**
     * set up the handler for purchasedButton
     */
    private void addToCartButtonHandler() {
        addToCartButton.setOnClickListener( e -> {
            ToBuyListRecycleAdapter adapter = (ToBuyListRecycleAdapter) toBuyListRecyclerView.getAdapter();
            List<ToBuyItem> selectedItems = adapter.getSelectedItems();
            if (selectedItems.size() == 0) {
                Toast.makeText(toBuyActivity.this, "No item selected", Toast.LENGTH_SHORT).show();
                return;
            }

            for (ToBuyItem item : selectedItems) {

                String id = item.getId();
                item.setSelected(false);
                // add selected item to the cartList and ref
                cartRef.child(id).setValue(item);
                cartList.add(item);
                cartListRecyclerAdapter.setData(cartList);
                itemSelectedUpdate();

                // remove the selected item from the toBuyList and ref
                toBuyRef.child(id).removeValue();
                toBuyList.remove(item);
                toBuyListRecycleAdapter.notifyDataSetChanged();
                cartListRecyclerAdapter.notifyDataSetChanged();
            }


            Toast.makeText(toBuyActivity.this, "Items added to the cart", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * set up the handler for addButton
     */
    private void addButtonHandler() {

        View popupView = getLayoutInflater().inflate(R.layout.popup_input, null);
        EditText n = popupView.findViewById(R.id.itemName);
        EditText num = popupView.findViewById(R.id.input_quantity);
        Button submitButton = popupView.findViewById(R.id.submit_button);
        // add this line to reference the Add button
        ImageButton addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(e -> {

            n.setText("");
            num.setText("");

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupView.setBackground(ContextCompat.getDrawable(this, R.drawable.popupstyle)); // set background
            popupWindow.showAtLocation(addButton, Gravity.CENTER, 0, 0);

            submitButton.setOnClickListener(eee -> {
                String name = n.getText().toString();
                String number = num.getText().toString();
                String toastMess = "Please fill in the blank!";
                if (name.length() != 0 && number.length() != 0 ) {
                    // set the unique id for each added item
                    String uniqueId = toBuyRef.push().getKey();
                    ToBuyItem item = new ToBuyItem(name, Integer.parseInt(number), false);
                    item.setId(uniqueId);

                    toBuyList.add(item);
                    toBuyListRecycleAdapter.setData(toBuyList);
                    // push the item to the database
                    toBuyRef.child(uniqueId).setValue(item);
                    Toast.makeText(toBuyActivity.this, "Item added to the list", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, toastMess, Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            });

        });

    }

    /**
     * set up the handler for checkout button
     */
    private void checkoutButtonHandler() {

        View popupView = getLayoutInflater().inflate(R.layout.checkoutdialog, null);
        EditText ed = popupView.findViewById(R.id.totalPrice);
        Button buyButton = popupView.findViewById(R.id.buy_button);
        // add this line to reference the Add button

        checkoutButton.setOnClickListener(e -> {

            if (cartList.size() == 0) {
                Toast.makeText(toBuyActivity.this, "No item selected", Toast.LENGTH_SHORT).show();
                return;
            }

            CartListRecyclerAdapter adapter = (CartListRecyclerAdapter) cartListRecyclerView.getAdapter();
            List<ToBuyItem> selectedItems = adapter.getSelectedItems();

            ed.setText("");

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupView.setBackground(ContextCompat.getDrawable(this, R.drawable.popupstyle)); // set background
            popupWindow.showAtLocation(buyButton, Gravity.CENTER, 0, 0);

            buyButton.setOnClickListener(eee -> {
                String price = ed.getText().toString();
                String toastMess = "Please fill in the blank!";
                if (price.length() != 0) {
//                    purchasedList = new ArrayList<>();
                    List<ToBuyItem> itemNameList = new ArrayList<>();
                    for (ToBuyItem item : selectedItems) {
                        String id = item.getId();
                        ToBuyItem temp = new ToBuyItem(item.getName(), item.getIntQuantity(), false, item.getId());
                        item.setSelected(false);
                        // add selected item to the purchasedList
                        itemNameList.add(temp);
                        //temp.setId(itemNameList.indexOf(temp) + "");

                        // remove the selected item from the cartList and ref
                        cartRef.child(id).removeValue();
                        cartList.remove(item);

                        cartListRecyclerAdapter.notifyDataSetChanged();
                    }
                    itemSelectedUpdate();
                    String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().trim();
                    PurchasedItem pi = new PurchasedItem(name, Double.parseDouble(price), itemNameList);
                    String uniqueId = purchasedRef.push().getKey();
                    pi.setId(uniqueId);
                    purchasedRef.child(uniqueId).setValue(pi);
                    Log.d("999999", uniqueId);

                    Intent intent = new Intent(this, SettleActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, toastMess, Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            });

        });
    }

    /**
     * set up the handler for the toBuyHomeButton
     */
    private void toBuyHomeButtonHandler() {
        toBuyHome.setOnClickListener(e -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("FRAGMENT_TAG", "loggedFragment");
            startActivity(intent);
        });
    }

    /**
     * A method to handle TOBUYLIST swipe, after the swipe, the item will be deleted.
     */
    ItemTouchHelper.SimpleCallback toBuyItemTouchCallback  = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            Toast.makeText(toBuyActivity.this, "Item deleted from the ToBuy list", Toast.LENGTH_SHORT).show();

            // delete the item from recyclerview
            int position = viewHolder.getAdapterPosition();
            ToBuyItem selectedItem = toBuyList.get(position);
            toBuyList.remove(selectedItem);
            toBuyListRecycleAdapter.setData(toBuyList);

            // delete the item from firebase
            String id = selectedItem.getId();
            toBuyRef.child(id).removeValue();
            itemSelectedUpdate();

        }
    };

    /**
     * A method to handle cartlist swipe, after the swipe, the item will be moved back to ToBuy list.
     */
    ItemTouchHelper.SimpleCallback cartListTouchCallback  = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
//            ToBuyItem selectedItem = cartList.get(position);
            ToBuyItem tempItem = cartList.get(position);
            toBuyList.add(tempItem);
            tempItem.setSelected(false);
            toBuyListRecycleAdapter.notifyDataSetChanged();

            // add item to firebase toBuyList
            String id = tempItem.getId();
//            String uniqueId = toBuyRef.push().getKey();
            tempItem.setId(id);
            toBuyRef.child(id).setValue(tempItem);

            // delete the item from firebase cartList
            cartRef.child(id).removeValue();
            cartList.remove(position);
            cartListRecyclerAdapter.notifyItemRemoved(position);
            Toast.makeText(toBuyActivity.this, "Item added to the ToBuy list", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * by calling this method, it will update the selectedItem TextView
     */
    private void itemSelectedUpdate() {
        int count = toBuyListRecycleAdapter.getSelectedItems().size();
        if (count == 0) {
            itemSelected.setText("Item selected: 0");
        } else {
            itemSelected.setText("Item selected: " + count);
        }
    }

    /**
     * on save instance state method
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("cartList", (Serializable) cartList);
        outState.putSerializable("toBuyList", (Serializable) toBuyList);

    }

    /**
     * on restore instance state method
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cartList = (List<ToBuyItem>) savedInstanceState.getSerializable("cartList");
        toBuyList = (List<ToBuyItem>) savedInstanceState.getSerializable("toBuyList");
    }

    /**
     * initialize the list from datasnapshot
     * @param dataSnapshot firebase's snapshot
     * @return List<ToBuyItem>
     */
    private List<ToBuyItem> firebaseToBuyList(DataSnapshot dataSnapshot) {

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