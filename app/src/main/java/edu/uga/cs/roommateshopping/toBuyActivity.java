package edu.uga.cs.roommateshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class toBuyActivity extends AppCompatActivity {

    private static final String TAG = "toBuyActivity";

    TextView itemSelected;
    Button addToCartButton;
    private static List<ToBuyItem> toBuyList;
    private RecyclerView toBuyListrecyclerView;
    private ToBuyListRecycleAdapter toBuyListRecycleAdapter;

    private static List<ToBuyItem> cartList;
    private RecyclerView cartListrecyclerView;
    private CartListRecyclerAdapter cartListRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_buy);

        if (savedInstanceState != null) {
            cartList = (List<ToBuyItem>) savedInstanceState.getSerializable("cartList");
            toBuyList = (List<ToBuyItem>) savedInstanceState.getSerializable("toBuyList");
        } else {
            cartList = new ArrayList<>();
            toBuyList = new ArrayList<>();
            initList();
        }

        // set up the adapter and recyclerview for the cartList
        cartListrecyclerView = findViewById(R.id.cartList);
        cartListrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartListRecyclerAdapter = new CartListRecyclerAdapter(this, cartList);
        ItemTouchHelper cartTouchHelper = new ItemTouchHelper(cartListTouchCallback);
        cartTouchHelper.attachToRecyclerView(cartListrecyclerView);
        cartListrecyclerView.setAdapter(cartListRecyclerAdapter);

        addToCartButton = findViewById(R.id.addToCartButton);
        addButtonHandler();
        itemSelected = findViewById(R.id.itemSelected);
        addToCartButtonHandler();





        // set up the adapter and recyclerview for the toBuyList
        toBuyListrecyclerView = findViewById(R.id.toBuyList);
        toBuyListrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toBuyListRecycleAdapter = new ToBuyListRecycleAdapter(this, toBuyList);
        toBuyListRecycleAdapter.setOnSelectedItemsChangedListener(new ToBuyListRecycleAdapter.OnSelectedItemsChangedListener() {
            @Override
            public void onSelectedItemsChanged(int count) {
                itemSelectedUpdate();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(toBuyItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(toBuyListrecyclerView);
        toBuyListrecyclerView.setAdapter(toBuyListRecycleAdapter);

    }

    /**
     * set up the handler for purcahsedButton
     */
    private void addToCartButtonHandler() {
        addToCartButton.setOnClickListener( e -> {
            ToBuyListRecycleAdapter adapter = (ToBuyListRecycleAdapter) toBuyListrecyclerView.getAdapter();
            List<ToBuyItem> selectedItems = adapter.getSelectedItems();
            if (selectedItems.size() == 0) {

            } else {
                for (ToBuyItem item : selectedItems) {
                    int position = toBuyList.indexOf(item);
                    cartList.add(item);
                    toBuyList.remove(position);
                    toBuyListRecycleAdapter.notifyItemRemoved(position);
                }
                Toast.makeText(toBuyActivity.this, "Items added to the cart", Toast.LENGTH_SHORT).show();
                itemSelectedUpdate();
                toBuyListRecycleAdapter.notifyDataSetChanged();
                cartListRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * set up the handler for addButton
     */
    private void addButtonHandler() {

        View popupView = getLayoutInflater().inflate(R.layout.popup_input, null);
        EditText n = popupView.findViewById(R.id.input_text);
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
                    ToBuyItem item = new ToBuyItem(name, Integer.parseInt(number));
                    toBuyList.add(item);
                    toBuyListRecycleAdapter.notifyDataSetChanged();
                    Toast.makeText(toBuyActivity.this, "Item added to the list", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, toastMess, Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            });

        });




    }

    /**
     * A method to handle tobuylist swipe, after the swipe, the item will be deleted.
     */
    ItemTouchHelper.SimpleCallback toBuyItemTouchCallback  = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            Toast.makeText(toBuyActivity.this, "Item deleted from the list", Toast.LENGTH_SHORT).show();
            int position = viewHolder.getAdapterPosition();
            toBuyList.remove(position);
            toBuyListRecycleAdapter.notifyItemRemoved(position);
            itemSelectedUpdate();

        }
    };

    /**
     * A method to handle cartlist swipe, after the swipe, the item will be deleted.
     */
    ItemTouchHelper.SimpleCallback cartListTouchCallback  = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            Toast.makeText(toBuyActivity.this, "Item added to the list", Toast.LENGTH_SHORT).show();
            int position = viewHolder.getAdapterPosition();
            ToBuyItem selectedItem = cartList.get(position);
            toBuyList.add(selectedItem);
            selectedItem.setSelected(false);
            toBuyListRecycleAdapter.notifyDataSetChanged();

            cartList.remove(position);
            cartListRecyclerAdapter.notifyItemRemoved(position);

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

    private void initList() {
        ToBuyItem one = new ToBuyItem("item 1", 1);
        ToBuyItem two = new ToBuyItem("item 2", 2);
        ToBuyItem three = new ToBuyItem("item 3", 3);
        toBuyList.add(one);
        toBuyList.add(two);
        toBuyList.add(three);
        ToBuyItem four = new ToBuyItem("item 4", 1);
        ToBuyItem five = new ToBuyItem("item 5", 2);
        ToBuyItem six = new ToBuyItem("item 6", 3);
        toBuyList.add(four);
        toBuyList.add(five);
        toBuyList.add(six);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("cartList", (Serializable) cartList);
        outState.putSerializable("toBuyList", (Serializable) toBuyList);
        Log.d("999999", String.valueOf(outState));

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cartList = (List<ToBuyItem>) savedInstanceState.getSerializable("cartList");
        toBuyList = (List<ToBuyItem>) savedInstanceState.getSerializable("toBuyList");
    }
}