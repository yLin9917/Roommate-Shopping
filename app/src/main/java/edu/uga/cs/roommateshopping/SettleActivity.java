package edu.uga.cs.roommateshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
    Button settleViewList, settleButton, editButton;
    RecyclerView purchasedListRecyclerView;
    PurchasedListRecyclerAdapter purchasedListRecyclerAdapter;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference purchasedRef = db.getReference("purchasedList");

    TextView recentlyTotal;

    double total = 0;
    double roommate1 = 0;
    double roommate2 = 0;

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
        TextView settleRoomate1 = popupView.findViewById(R.id.settleRoomate1);
        TextView settleRoomate2 = popupView.findViewById(R.id.settleRoomate2);
        Button finalSettleButton = popupView.findViewById(R.id.finalSettleButton);

        settleButton.setOnClickListener(e -> {
            settleRoomate1.setText("Lin: $" + String.format("%.2f", roommate1));
            settleRoomate2.setText("Anna: $" + String.format("%.2f", roommate2));
            settleTotalCost.setText(recentlyTotal.getText().toString());
            settleAverage.setText("Average Cost: $" + String.format("%.2f", total / 2));


            // create the popup window
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupView.setBackground(ContextCompat.getDrawable(this, R.drawable.popupstyle));
            popupWindow.showAtLocation(settleButton, Gravity.CENTER, 0, 0);

            finalSettleButton.setOnClickListener(ee -> {
                purchasedRef.removeValue();
                popupWindow.dismiss();

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
                    Double.parseDouble(snapshotValue.get("cost").toString()),
                    (List<ToBuyItem>)snapshotValue.get("items")
            );
            purchasedItem.setId(snapshotValue.get("id").toString());
            list.add(purchasedItem);
        }
        return list;
    }

    /**
     * update the total price
     */
    private void updateTotal() {
        total = 0;
        roommate1 = 0;
        roommate2 = 0;
        for (int i = 0; i < purchasedItem.size(); i++) {
            total += purchasedItem.get(i).getCost();
            if (purchasedItem.get(i).getName().equals("Lin")) {
                roommate1 += purchasedItem.get(i).getCost();
            } else if (purchasedItem.get(i).getName().equals("az")) {
                roommate2 += purchasedItem.get(i).getCost();
            }
        }
        recentlyTotal.setText("Total: $" + String.format("%.2f", total));
    }


}