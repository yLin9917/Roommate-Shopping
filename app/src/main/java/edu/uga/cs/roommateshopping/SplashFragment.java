package edu.uga.cs.roommateshopping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SplashFragment extends Fragment {

    Button login, exit, register;


    /**
     * Required empty public constructor
     */
    public SplashFragment() {

    }

    /**
     * Create a new instance of the fragment
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplashFragment.
     */
    public static SplashFragment newInstance(String param1, String param2) {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    /**
     * Called when the fragment is created, initialize the fragment's state
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when the fragment's view is created, initialize the fragment's view
     * @param inflater the layout inflater
     * @param container the view group container
     * @param savedInstanceState the saved instance state
     * @return the fragment's view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    /**
     * Called when the fragment's view is created, initialize the fragment's view
     * @param view the fragment's view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // reference to the corresponding button
        login = view.findViewById(R.id.mainLogin);
        register = view.findViewById(R.id.mainRegister);
        exit = view.findViewById(R.id.mainExit);

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
            changeFragment(new LoginFragment());
        });
    }

    /**
     * set up the register button
     */
    private void registerButton() {
        register.setOnClickListener(e -> {
            changeFragment(new RegisterFragment());
        });
    }


    /**
     *  Set up the click listener for the exit button
     */
    private void exitButton() {
        exit.setOnClickListener(e -> {
            getActivity().finish();
            System.exit(0);
        });
    }

    /**
     * change the frame layout's fragment
     *
     * @param fragment the new fragment
     */
    protected void changeFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.mainframelayout, fragment);
        transaction.commit();
    }

}