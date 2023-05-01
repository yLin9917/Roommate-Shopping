package edu.uga.cs.roommateshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

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

    /**
     * onCreate method
     * @param savedInstanceState
     */
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

        String fragmentTag = getIntent().getStringExtra("FRAGMENT_TAG");
        if (fragmentTag != null && fragmentTag.equals("loggedFragment")) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainframelayout, new LoggedFragment(), "loggedFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * onSavedInstanceState method
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentFragment != null) {
            // Save the current fragment in the saved instance state
            getSupportFragmentManager().putFragment(outState, "currentFragment", currentFragment);
        }

    }

    /**
     * change the frame layout's fragment
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