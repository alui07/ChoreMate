package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * An Activity that allows user to create/join a household
 */
public class NoHouseholdActivity extends AppCompatActivity {

    // UI elements on the screen.
    private Button mCreateButton;
    private Button joinButton;
    private EditText inviteCode;

    // Appbar of the screen.
    private Toolbar appbar;

    //TODO: Replace all instances of dummyCode when database is set up
    public static final String dummyCode = "123";

    /**
     * Creates the main screen. Called when main page is loaded.
     * @param savedInstanceState    The last instance state that the activity is in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_household);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_no_household);
        setSupportActionBar(appbar);

        mCreateButton = (Button) findViewById(R.id.btn_create_household);
        joinButton = (Button) findViewById(R.id.btn_join_household);
        inviteCode = (EditText) findViewById(R.id.edit_invite_code);


        // Set up listener for the Create Household button
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Brings the user to the Create Household screen.
                Intent registerIntent = new Intent(view.getContext(), CreateHouseholdActivity.class);
                startActivity(registerIntent);

            }

        });


        // Set up listener for the Join Button
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inviteCode.getText().toString();

                if (checkCode(code)) {
                    // Updates current_balance field of every User in the Household to include new User
                    updateUserBalances();

                    // TODO: Take user to correct household task page
                    Intent toTask = new Intent(v.getContext(), MainActivity.class);
                    startActivity(toTask);
                }

            }
        });
    }


    /**
     * Function that handles the code inserted by the user in the text field
     * @param code: Code entered by the user.
     * @return true if the code is correct, false otherwise
     * TODO: Add database functionality
     */
    private boolean checkCode(String code){
        // Reset the error message
        inviteCode.setError(null);

        // Check if the code is correct
        if (isCorrectCode(code)){
            return true;
        }

        else {
            // Set the error
            inviteCode.setError("Please Enter a Valid Code");
            return false;
        }
    }


    /**
     * String compare the code entered with the actual household code
     * @param code: Code entered by user
     * @return true if the string matches, false otherwise.
     * TODO: Replace dummyCode with database code
     */
    private boolean isCorrectCode(String code){
        return (code.equals(dummyCode));
    }

    /**
     * Updates the current_balances field of every User in the current Household to include
     * the new User.
     * @return void
     */
    private void updateUserBalances() {
        HousemateBalance balance;
        ArrayList<User> housemateList = new ArrayList<>();
        ArrayList<HousemateBalance> userHousemateBalance = new ArrayList<>();
        ArrayList<HousemateBalance> housemateBalance;
        User housemate;

        // TODO: Get housemateList from database here

        // Update current_balances field of current user and existing users
        for (int i = 0; i < housemateList.size(); i++) {
            // Add new User to the current_balances field of each User in housemateList
            housemate = housemateList.get(i);

            // TODO: GET USER INFO FROM DATABASE
            //balance = new HousemateBalance(firstName, lastName, avatar, 0);
            balance = new HousemateBalance(); // TODO: REMOVE THIS LINE AFTER DB IMPLEMENTATION
            housemateBalance = housemate.getCurrent_balances();
            housemateBalance.add(balance);

            housemate.setCurrent_balances(housemateBalance);

            // Add existing Users to the current_balances field of current User
            balance = new HousemateBalance(housemate.getFirst_name(), housemate.getLast_name(),
                                           housemate.getAvatar(), 0);
            userHousemateBalance.add(balance);
        }

        // TODO: CURRENT USER.setCurrent_balances(userHousemateBalance); DATABASE NEEDED
    }
}
