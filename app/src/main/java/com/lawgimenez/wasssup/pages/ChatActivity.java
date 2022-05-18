package com.lawgimenez.wasssup.pages;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawgimenez.wasssup.R;
import com.lawgimenez.wasssup.WasssupApplication;
import com.lawgimenez.wasssup.adapters.MessageAdapter;
import com.lawgimenez.wasssup.databinding.ActivityChatBinding;
import com.lawgimenez.wasssup.models.Message;
import com.lawgimenez.wasssup.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Lawrence Gimenez on 02/05/2017.
 * Copyright wasssup
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityChatBinding mActivityBinding;
    private ArrayList<Message> mListMessages;
//    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        getMessages();
    }

    private void initViews() {
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        // Configure RecyclerView as vertical
        mActivityBinding.recyclerviewChat.setHasFixedSize(true);
        mActivityBinding.recyclerviewChat.setLayoutManager(new LinearLayoutManager(this));
        // Click listener for send message button
        mActivityBinding.buttonSendMessage.setOnClickListener(this);
        // Click listener for sign out button
        mActivityBinding.toolbar.buttonLogout.setOnClickListener(this);
    }

    private void getMessages() {
        mListMessages = WasssupApplication.getInstance().getAllMessages();
        updateList();
        DatabaseReference messageDatabase = FirebaseDatabase.getInstance()
                .getReference().child(Constants.KEY_DATABASE_MESSAGE);
        messageDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    // Create Message object based on the data from the API
                    Message message = new Message();
                    message.setMessageId(messageSnapshot.getKey());
                    message.setDate(messageSnapshot.child(Message.KEY_DATE)
                            .getValue().toString());
                    message.setContent(messageSnapshot.child(Message.KEY_CONTENT)
                            .getValue().toString());
                    message.setUsername(messageSnapshot.child(Message.KEY_USERNAME)
                            .getValue().toString());
                    message.setUserId(messageSnapshot.child(Message.KEY_USER_ID)
                            .getValue().toString());
                    // Then save to our database
                    WasssupApplication.getInstance().saveMessage(message);
                }
                // Update list
                mListMessages.clear();
                mListMessages = WasssupApplication.getInstance().getAllMessages();
                updateList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateList() {
        MessageAdapter adapter = new MessageAdapter(mListMessages);
        mActivityBinding.recyclerviewChat.setAdapter(adapter);
    }

    private void sendMessage() {
        // Format current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        String formattedDate = simpleDateFormat.format(calendar.getTime());
        DatabaseReference messageDatabase = FirebaseDatabase.getInstance()
                .getReference().child(Constants.KEY_DATABASE_MESSAGE).push();
        messageDatabase.child(Message.KEY_DATE)
                .setValue(formattedDate);
        messageDatabase.child(Message.KEY_USER_ID)
                .setValue(WasssupApplication.getInstance().getUserId());
        messageDatabase.child(Message.KEY_USERNAME)
                .setValue(WasssupApplication.getInstance().getUsername());
        messageDatabase.child(Message.KEY_CONTENT)
                .setValue(mActivityBinding.edittextSendMessage.getText().toString());
        // Clear text
        mActivityBinding.edittextSendMessage.setText("");
        // Update the message list
        getMessages();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void logOut() {
        // Logout from Firebase
        WasssupApplication.getInstance().getFirebaseAuth().signOut();
        // Open login page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.button_send_message) {
            hideKeyboard();
            sendMessage();
        } else if (viewId == R.id.button_logout) {
            logOut();
        }
    }
}
