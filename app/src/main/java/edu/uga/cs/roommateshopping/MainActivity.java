package edu.uga.cs.roommateshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "RoommateShopping";
    private FirebaseAuth mAuth;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // deal with rotating, the activity will show the fragment before rotated
            currentFragment = getSupportFragmentManager().
                    getFragment(savedInstanceState, "currentFragment");
        } else {
            // init the splash fragment if in the first run
            changeFragment(new SplashFragment());
        }

        mAuth = FirebaseAuth.getInstance();
        String email = "dawg@mail.com";
        String password = "password";

        mAuth.signInWithEmailAndPassword( email, password )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success
                            Log.d( TAG, "signInWithEmail:success" );
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                        else {
// If sign in fails
                            Log.d( TAG, "signInWithEmail:failure", task.getException() );
                        }
                    }
                });

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference( "message" );
//
//        // Read from the database value for ”message”
//        myRef.addValueEventListener( new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot dataSnapshot ) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String message = dataSnapshot.getValue( String.class );
//                textView.setText( message );
//            }

//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w( TAG, "Failed to read value.", error.toException() );
//            }
//        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentFragment != null) {
            // Save the current fragment in the saved instance state
            getSupportFragmentManager().putFragment(outState, "currentFragment", currentFragment);
        }

    }

    /**
     * change the framelayout's fragment
     *
     * @param fragment the new fragment
     */
    protected void changeFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.mainframelayout, fragment);
        transaction.commit();
    }


}