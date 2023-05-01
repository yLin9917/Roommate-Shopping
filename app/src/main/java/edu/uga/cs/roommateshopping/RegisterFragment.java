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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    Button signup, back;
    EditText regPassword, regComPassword, registerUserName, registerEmail;

    /**
     * Required empty public constructor
     */
    public RegisterFragment() {
    }

    // TODO: Rename and change types and number of parameters
    /**
     * Create a new instance of this fragment
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();;
        return fragment;
    }

    /**
     * onCreate method
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * onCreateView method
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    /**
     * onViewCreated method
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signup = view.findViewById(R.id.registerSignup);
        back = view.findViewById(R.id.registerBack);
        registerUserName = view.findViewById(R.id.registerUserName);
        registerEmail = view.findViewById(R.id.registerEmail);
        regPassword = view.findViewById(R.id.registerPassword);
        regComPassword = view.findViewById(R.id.registerComfirmPassword);

        // call the button handler method
        signupButton();
        backButton();

    }

    /**
     * set up the sign up button
     */
    private void signupButton() {
        signup.setOnClickListener(e -> {
            registerHandler();
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

    /**
     * handler the register process
     */
    private void registerHandler() {
        String email = registerEmail.getText().toString().trim();
        String name = registerUserName.getText().toString().trim();
        String pass = regPassword.getText().toString().trim();
        String cpass = regComPassword.getText().toString().trim();
        if(pass.equals(cpass) && pass.length() >= 6){
            //register
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                FirebaseUser user = mAuth.getCurrentUser();
                                database = FirebaseDatabase.getInstance();
                                usersRef = database.getReference("users");
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                user.updateProfile(profileUpdates);
                                changeFragment(new SplashFragment());
                                Toast.makeText(getContext(),"Account Successfully Created",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // If register in fails
                                Toast.makeText(getContext(),"Email is Already Registered",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else if (pass.length() < 6)
            Toast.makeText(getContext(),"Password needs at least 6 characters",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(),"Password Not matching",Toast.LENGTH_SHORT).show();
    }

}