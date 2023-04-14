package edu.uga.cs.roommateshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

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