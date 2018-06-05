package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class CreateHouseholdActivity extends AppCompatActivity {
    // Declare Text Fields
    private EditText editHouseholdName;
    private EditText editHousematesList;

    private Button buttonCreate;

    // Declare instance variables
    private Household house = new Household();

    private boolean cancel = false;

    private String first_name;

    /**
     * Database references.
     */
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);

        // Set up user database reference.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrUser = mAuth.getCurrentUser();

        // Initialize the views
        editHouseholdName = (EditText) findViewById(R.id.edit_household_name);
        editHousematesList = (EditText) findViewById(R.id.edit_roommates_list);

        buttonCreate = (Button) findViewById(R.id.btn_create_household);

        // Add click listener
        buttonCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel = false;

                MyUtils.HideSoftKeyboard(CreateHouseholdActivity.this);
                attemptCreate();

                // If House object wasn't created, break out of code
                if (cancel) {
                    return;
                }

                attemptInvite();

                // If invites were not sent, break out of code
                if (cancel) {
                    return;
                }

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // get my user name
        DatabaseReference mUser = mDatabase.child("Users").child(mCurrUser.getUid());
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                first_name = dataSnapshot.getValue(User.class).getFirst_name();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreateHouseholdActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void attemptCreate() {
        // Initialize local variables
        View focusView;

        // Parse household name
        String householdName = editHouseholdName.getText().toString().trim();

        // Checks if the Household name is not empty
        if(TextUtils.isEmpty(householdName)) {
            editHouseholdName.setError(getString(R.string.error_field_required));
            focusView = editHouseholdName;
            focusView.requestFocus();
            cancel = true;

            return;
        }

        // Generate random invite code
        InviteCodeGenerator code = new InviteCodeGenerator(getRandomLength());
        String house_code = code.nextString();

        // Create new Household object and update fields
        house.setHouse_code(house_code);
        house.setHouse_name(householdName);

        String user_id = mAuth.getCurrentUser().getUid();
        String key = mDatabase.child("Households").push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Households/"+key, house.toMap());
        childUpdates.put("/Users/" + user_id + "/household", key);

        mDatabase.updateChildren(childUpdates);
    }

    private void attemptInvite() {
        // Initialize local variables
        View focusView;

        // Getting content for email
        String emails = editHousematesList.getText().toString().trim();
        String subject = first_name + " is inviting you to " + house.getHouse_name() + " on ChoreMate!";
        String message = "Your invite code to " + house.getHouse_name() + " is: "
                         + house.getHouse_code();

        // Parse the String of emails
        String emailsList[] = emails.split(", ");

        // If no emails are entered, no invites need to be sent.
        if (noEmailsEntered(emailsList)) {
            return;
        }

        // Check if emails are valid
        if (isInvalidEmail(emailsList)) {
            editHousematesList.setError(getString(R.string.error_invalid_email_list));
            focusView = editHousematesList;
            focusView.requestFocus();

            cancel = true;
        }

        if (!cancel) {
            // Create SendMail object and send invites
            for (String email : emailsList) {
                SendMail sm = new SendMail(this, email, subject, message);
                sm.execute();
            }
        }
    }

    // Generates a random number between [6, 12], inclusive
    private int getRandomLength() {
        Random rand = new Random();
        int high = 13;
        int low = 6;
        return rand.nextInt(high - low) + low;
    }

    // Returns true if any email in a list of emails does not end with @ucsd.edu
    private boolean isInvalidEmail(String[] emailsList) {
        for (String email : emailsList) {
            if (!email.contains("@")) {
                return true;
            }
        }

        return false;
    }

    // Returns true if no emails were entered
    private boolean noEmailsEntered(String[] emailsList) {
        if (emailsList[0].equals("")) {
            return true;
        }

        return false;
    }
}
