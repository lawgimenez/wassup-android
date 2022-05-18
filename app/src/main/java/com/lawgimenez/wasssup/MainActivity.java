package com.lawgimenez.wasssup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lawgimenez.wasssup.pages.ChatActivity;
import com.lawgimenez.wasssup.pages.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        if (WasssupApplication.getInstance().isLoggedIn()) {
            intent = new Intent(this, ChatActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
