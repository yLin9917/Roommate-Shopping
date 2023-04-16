package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class toBuyActivity extends AppCompatActivity {

    private static final String TAG = "toBuyActivity";

    Button purcahsedButton;
    private static List<ToBuyItem> toBuyList;
    private RecyclerView recyclerView;
    private toBuyListRecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_buy);

        purcahsedButton = findViewById(R.id.purcahsedButton);
        purcahsedButtonHandler();

        addButtonHandler();

        ToBuyItem one = new ToBuyItem("item 1", 1);
        ToBuyItem two = new ToBuyItem("item 2", 2);
        ToBuyItem three = new ToBuyItem("item 3", 3);
        toBuyList = new ArrayList<>();
        toBuyList.add(one);
        toBuyList.add(two);
        toBuyList.add(three);

        recyclerView = findViewById(R.id.toBuyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recycleAdapter = new toBuyListRecycleAdapter(this, toBuyList);
        recyclerView.setAdapter(recycleAdapter);

    }

    /**
     * set up the handler for purcahsedButton
     */
    private void purcahsedButtonHandler() {

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
                Log.d("999999", name);
                String toastMess = "Please fill in the blank!";
                if (name.length() != 0 && number.length() != 0 ) {
                    ToBuyItem item = new ToBuyItem(name, Integer.parseInt(number));
                    toBuyList.add(item);
                } else {
                    Toast.makeText(this, toastMess, Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            });

        });




    }


}