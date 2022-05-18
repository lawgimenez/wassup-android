package com.lawgimenez.wasssup.pages;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawgimenez.wasssup.R;
import com.lawgimenez.wasssup.WasssupApplication;
import com.lawgimenez.wasssup.databinding.ActivityLoginBinding;
import com.lawgimenez.wasssup.utils.Constants;
import com.lawgimenez.wasssup.utils.Tools;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by Lawrence Gimenez on 02/05/2017.
 * Copyright wasssup
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding mActivityBinding;
    private ACProgressFlower mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // Click listener for button login / sign up
        mActivityBinding.buttonLogin.setOnClickListener(this);
        // Hide logout button
        mActivityBinding.toolbar.buttonLogout.setVisibility(View.GONE);
    }

    private void goToChat() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    private void showLoginIndicator() {
        mProgressDialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE).build();
        mProgressDialog.show();
    }

    private void hideLoginIndicator() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.button_login) {
            if (Tools.isConnected(this)) {
                // Display sign in indicator
                showLoginIndicator();
                final String username = mActivityBinding.edittextUsername.getText().toString();
                final String password = mActivityBinding.edittextPassword.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    // If both username and password is not empty, we are good to go
                    // Get Firebase auth
                    final FirebaseAuth firebaseAuth = WasssupApplication.getInstance().getFirebaseAuth();
                    firebaseAuth.signInWithEmailAndPassword(
                            username + "@wasssup.com", password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Get user ID
                                    final String userId = task.getResult().getUser().getUid();
                                    if (task.isSuccessful()) {
                                        // Get username from API
                                        DatabaseReference usernameDatabase = FirebaseDatabase.getInstance()
                                                .getReference().child(Constants.KEY_USERNAME);
                                        usernameDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String username = dataSnapshot.child(userId)
                                                        .child(Constants.KEY_USERNAME).getValue().toString();
                                                WasssupApplication.getInstance().setUsername(username);
                                                // Open up chat page
                                                goToChat();
                                                // Hide sign in indicator
                                                hideLoginIndicator();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        // If sign in not successful, sign up user
                                        firebaseAuth.createUserWithEmailAndPassword(username + "@wasssup.com", password)
                                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // Save username
                                                            WasssupApplication.getInstance().setUsername(username);
                                                            // Open up chat page
                                                            goToChat();
                                                        }
                                                        // Hide sign in indicator
                                                        hideLoginIndicator();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            } else {
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
