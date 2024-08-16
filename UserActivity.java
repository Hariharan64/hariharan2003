package com.example.qrstaff;



import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {

    private TextView textViewUserInfo;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize views
        textViewUserInfo = findViewById(R.id.textViewUserInfo);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Retrieve user data from Firebase
        retrieveUserData();
    }

    private void retrieveUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder userInfo = new StringBuilder();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userInfo.append("Name: ").append(user.getName()).append("\n");
                        userInfo.append("ID: ").append(user.getId()).append("\n");
                        userInfo.append("Designation: ").append(user.getDesignation()).append("\n");
                        userInfo.append("Phone Number: ").append(user.getPhoneNumber()).append("\n\n");
                    }
                }

                textViewUserInfo.setText(userInfo.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}