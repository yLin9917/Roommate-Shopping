package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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
        settleButton.setOnClickListener(e -> {

        });
    }

    private void settleViewListButtonHandler() {
        settleViewList.setOnClickListener(e -> {
            Intent intent = new Intent(this, toBuyActivity.class);
            startActivity(intent);
        });
    }

}