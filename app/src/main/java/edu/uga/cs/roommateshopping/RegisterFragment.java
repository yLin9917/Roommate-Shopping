package edu.uga.cs.roommateshopping;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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

    /**
     * handler the register process
     */
    private void registerHandler() {
        String email = registerEmail.getText().toString();
        String name = registerUserName.getText().toString();
        String pass = regPassword.getText().toString();
        String cpass = regComPassword.getText().toString();
        if(pass.equals(cpass) && pass.length() >= 6){
            //register
            mAuth.createUserWithEmailAndPassword( email, pass )
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                FirebaseUser user = mAuth.getCurrentUser();
                                database = FirebaseDatabase.getInstance();
                                usersRef = database.getReference("users");
                                String userId = mAuth.getCurrentUser().getUid();
                                usersRef.child(userId).child("username").setValue(name);
                                usersRef.child(userId).child("password").setValue(pass);
                                usersRef.child(userId).child("email").setValue(email);
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