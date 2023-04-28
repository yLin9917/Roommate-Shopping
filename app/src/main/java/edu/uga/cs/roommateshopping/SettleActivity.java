package edu.uga.cs.roommateshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettleActivity extends AppCompatActivity {

    List<PurchasedItem> purchasedItem;
    Button settleViewList, settleButton;
    RecyclerView purchasedListRecyclerView;
    PurchasedListRecyclerAdapter purchasedListRecyclerAdapter;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference purchasedRef = db.getReference("purchasedList");

    TextView recentlyTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        recentlyTotal = findViewById(R.id.recentlyTotal);

        settleButton = findViewById(R.id.settleButton);
        settleButtonHandler();
        settleViewList = findViewById(R.id.settleViewList);
        settleViewListButtonHandler();

        // initialize adapter
        purchasedListRecyclerAdapter = new PurchasedListRecyclerAdapter(this, purchasedItem);

        // initialize recyclerView
        purchasedListRecyclerView = findViewById(R.id.settleList);
        purchasedListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchasedListRecyclerView.setAdapter(purchasedListRecyclerAdapter);

        purchasedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                purchasedItem = firebaseToBuyList(dataSnapshot);
                purchasedListRecyclerAdapter.setList(purchasedItem);
                purchasedListRecyclerAdapter.notifyDataSetChanged();
                updateTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void settleButtonHandler() {

        View popupView = getLayoutInflater().inflate(R.layout.settlepopup, null);
        TextView settleTotalCost = popupView.findViewById(R.id.settleTotalCost);
        TextView settleAverage = popupView.findViewById(R.id.settleAverage);
        Button finalSettleButton = popupView.findViewById(R.id.finalSettleButton);

        settleButton.setOnClickListener(e -> {
            settleTotalCost.setText("Total: $100");
            settleAverage.setText("Average Cost: $50");

            // create the popup window
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupView.setBackground(ContextCompat.getDrawable(this, R.drawable.popupstyle));
            popupWindow.showAtLocation(settleButton, Gravity.CENTER, 0, 0);

            finalSettleButton.setOnClickListener(ee -> {
//                String total = settleTotalCost.getText().toString();
//                String average = settleAverage.getText().toString();


            });

        });
    }

    /**
     * handler for the VIEW LIST button, to go back to the previous activity
     */
    private void settleViewListButtonHandler() {
        settleViewList.setOnClickListener(e -> {
            Intent intent = new Intent(this, toBuyActivity.class);
            startActivity(intent);
        });
    }

    /**
     * initialize the list from datasnapshot
     * @param dataSnapshot firebase's snapshot
     * @return List<ToBuyItem>
     */
    private List<PurchasedItem> firebaseToBuyList(DataSnapshot dataSnapshot) {

        List<PurchasedItem> list = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Map<String, Object> snapshotValue = (Map<String, Object>) snapshot.getValue();
            PurchasedItem purchasedItem = new PurchasedItem(
                    snapshotValue.get("name").toString(),
                    Integer.parseInt(snapshotValue.get("cost").toString()),
                    (List<String>)snapshotValue.get("items")
            );
            list.add(purchasedItem);
        }
        return list;
    }

    /**
     * update the total price
     */
    private void updateTotal() {
        int total = 0;
        for (int i = 0; i < purchasedItem.size(); i++) {
            total+=purchasedItem.get(i).getCost();
        }
        recentlyTotal.setText("Total: $" + total);
    }

}