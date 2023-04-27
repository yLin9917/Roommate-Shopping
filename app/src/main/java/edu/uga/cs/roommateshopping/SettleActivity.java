package edu.uga.cs.roommateshopping;

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

public class SettleActivity extends AppCompatActivity {

    Button settleViewList, settleButton;
    PurchasedItem pi;
    RecyclerView purchasedListRecyclerView;
    PurchasedListRecyclerAdapter purchasedListRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        settleButton = findViewById(R.id.settleButton);
        settleButtonHandler();
        settleViewList = findViewById(R.id.settleViewList);
        settleViewListButtonHandler();

        pi = (PurchasedItem) getIntent().getSerializableExtra("PurchasedItem");

        // initialize adapter
        purchasedListRecyclerAdapter = new PurchasedListRecyclerAdapter(this, pi);

        // initialize recyclerView
        purchasedListRecyclerView = findViewById(R.id.settleList);
        purchasedListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchasedListRecyclerView.setAdapter(purchasedListRecyclerAdapter);

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
            popupView.setBackground(ContextCompat.getDrawable(this, R.drawable.popupstyle)); // set background
            popupWindow.showAtLocation(settleButton, Gravity.CENTER, 0, 0);

            finalSettleButton.setOnClickListener(ee -> {
//                String total = settleTotalCost.getText().toString();
//                String average = settleAverage.getText().toString();


            });

        });
    }

    private void settleViewListButtonHandler() {
        settleViewList.setOnClickListener(e -> {
            Intent intent = new Intent(this, toBuyActivity.class);
            startActivity(intent);
        });
    }

}