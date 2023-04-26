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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    Button login, back;
    String username;
    EditText loginEmail, loginPassword;
    FirebaseDatabase database;
    DatabaseReference usersRef;

    /**
     * Required empty public constructor
     */
    public LoginFragment() {

    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();

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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);
        login = view.findViewById(R.id.loginLogin);
        back = view.findViewById(R.id.loginBack);
        backButton();
        loginButton();

    }

    /**
     * set up the login button
     */
    private void loginButton() {
        login.setOnClickListener(e -> {
            loginHandler();
//            changeFragment(new LoggedFragment(), username);
        });
    }

    /**
     * set up the back button
     */
    private void backButton() {
        back.setOnClickListener(e -> {
            changeFragment(new SplashFragment(), "");
        });
    }

    /**
     * change the framelayout's fragment
     *
     * @param fragment the new fragment
     */
    private void changeFragment(Fragment fragment, String message) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (message != "") {
            Bundle bundle = new Bundle();
            bundle.putString("username", message);
        }
        transaction.replace(R.id.mainframelayout, fragment);
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void loginHandler() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            database = FirebaseDatabase.getInstance();
                            usersRef = database.getReference("users");
                            usersRef.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.d("999999", "onDataChange");
                                    if (snapshot.exists()) {
                                        String username = snapshot.getValue(String.class);
                                        Log.d("999999", "username: " + username);
                                    } else {
                                        Log.d("999999", "onDataChange");

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("999999", "onCancelled");
                                    Log.d("999999", "Error reading username value");
                                }
                            });

                        } else {
                            Toast.makeText(getActivity(),"The email address or password is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}