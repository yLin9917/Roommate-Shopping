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
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

        Button signup, back;
        EditText regPassword, regComPassword;

    /**
     * Required empty public constructor
     */
    public RegisterFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signup = view.findViewById(R.id.registerSignup);
        back = view.findViewById(R.id.registerBack);
        regPassword = view.findViewById(R.id.registerPassword);
        regComPassword = view.findViewById(R.id.registerComfirmPassword);

        signupButton();
        backButton();

    }

    /**
     * set up the sign up button
     */
    private void signupButton() {
        signup.setOnClickListener(e -> {
            String pass = regPassword.getText().toString();
            String cpass = regComPassword.getText().toString();
            if(pass.equals(cpass) && pass.length() != 0){
                Toast.makeText(getContext(),"Password matched",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(),"Password Not matching",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * set up the back button
     */
    private void backButton() {
        back.setOnClickListener(e -> {
            changeFragment(new SplashFragment());
        });
    }

    /**
     * change the framelayout's fragment
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