package edu.uga.cs.roommateshopping;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoggedFragment extends Fragment {

    String username;
    TextView welcomeText;
    Button viewList, recentlyPurchased, logout;

    public LoggedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoggedFragment newInstance(String param1, String param2) {
        LoggedFragment fragment = new LoggedFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logged, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        welcomeText = view.findViewById(R.id.welcomeText);
        if (username != "") welcomeText.setText("Hello " + username + ", ");
        Log.d("999999", username);

        viewList = view.findViewById(R.id.viewList);
        viewListButton();

        recentlyPurchased = view.findViewById(R.id.recentlyPurchased);
        recentlyPurchasedButton();

        logout = view.findViewById(R.id.logout);
        logoutButton();

    }

    /**
     * set up the login button
     */
    private void viewListButton() {
        viewList.setOnClickListener(e -> {
            Intent intent = new Intent(this.getActivity(), toBuyActivity.class);
            startActivity(intent);
        });
    }


    /**
     * set up the recentlyPurchased button
     */
    private void recentlyPurchasedButton() {
        recentlyPurchased.setOnClickListener(e -> {
            Intent intent = new Intent(this.getActivity(), SettleActivity.class);
            startActivity(intent);
        });
    }

    /**
     * set up the logout button
     */
    private void logoutButton() {
        logout.setOnClickListener(e -> {
            changeFragment(new SplashFragment());
        });
    }


    /**
     * change the framelayout's fragment
     *
     * @param fragment the new fragment
     */
    private void changeFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.mainframelayout, fragment);
        transaction.commit();
    }

}