package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

/**
 * The main screen of the app.
 */
public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // XML elements on the screen.
    private Toolbar appbar;
    private BottomNavigationView navbar;

    // Intent key.
    public static final String TASK = "com.example.teh_k.ChoreMate.TASK";
    public static final String HOUSEMATE = "com.example.teh_k.ChoreMate.HOUSEMATE";
    public static final String FRAGMENT = "com.example.teh_k.ChoreMate.FRAGMENT";

    @Override
    public void onStart() {
        super.onStart();

        // add authStateListener
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Creates the main screen. Called when main page is loaded.
     * @param savedInstanceState    The last instance state that the activity is in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    // Brings the user to the login screen.
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        };

        // Creates the appbar.
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        // Gets the intent.
        Intent intent = getIntent();
        char fragmentToLoad = intent.getCharExtra(FRAGMENT, 't');

        // Create the bottom nav bar.
        navbar = findViewById(R.id.bottom_profile);
        navbar.setOnNavigationItemSelectedListener(this);

        // Selects which fragment to load.
        setSelectedItemId(fragmentToLoad);

    }

    @Override
    public void onBackPressed() {
        // Minimizes the app when back button is pressed.
        moveTaskToBack(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // The fragment to be loaded.
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_tasks:
                // Creates task fragment.
                fragment = new TaskFragment();
                break;
            case R.id.action_transactions:
                // Creates payment fragment.
                fragment = new PaymentFragment();
                break;
            case R.id.action_household:
                // Creates the household fragment.
                fragment = new HouseholdFragment();
                break;
            case R.id.action_profile:
                // Creates the user profile fragment.
                fragment = new UserProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }


    /**
     * Sets the selected item in the bottom nav bar.
     */
    private void setSelectedItemId(char fragmentSelected) {
        switch(fragmentSelected){
            case 't':
                navbar.setSelectedItemId(R.id.action_tasks);
                break;
            case 'p':
                navbar.setSelectedItemId(R.id.action_transactions);
                break;
            case 'h':
                navbar.setSelectedItemId(R.id.action_household);
                break;
            case 'u':
                navbar.setSelectedItemId(R.id.action_profile);
                break;
        }
    }

    /**
     * Loads the fragment to the screen.
     * @param fragment  The fragment to be loaded to the screen.
     * @return  true if load is successful, false otherwise.
     */
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
