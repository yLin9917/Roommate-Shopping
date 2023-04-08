package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button login, exit, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference to the corresponding button
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        exit = findViewById(R.id.exit);

        // set up the login button
        loginButton();

        // set up the register button
        registerButton();

        // set up the exit button
        exitButton();

    }

    /**
     * set up the login button
     */
    private void loginButton() {
        login.setOnClickListener(e -> {

        });
    }

    /**
     * set up the register button
     */
    private void registerButton() {
        register.setOnClickListener(e -> {

        });
    }


    /**
     *  Set up the click listener for the exit button
     */
    private void exitButton() {
        exit.setOnClickListener(e -> {
            finish();
            System.exit(0);
        });
    }


}