package com.lawgimenez.wasssup;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.lawgimenez.wasssup.models.Message;

import java.util.ArrayList;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;

/**
 * Created by Lawrence Gimenez on 02/05/2017.
 * Copyright wasssup
 */

public class WasssupApplication extends Application {

    private static final String SHARED_PREF_NAME = "com.wasssup.default";
    private static final String KEY_USERNAME = "username";
    private static WasssupApplication mInstance;
    private SharedPreferences mSharedPref;
    private FirebaseAuth mFirebaseAuth;
    private Realm mRealm;
    private boolean mIsLoggedIn = false;

    public static WasssupApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize instance of this Application class
        mInstance = this;
        // Initialize shared preference
        mSharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        // Initialize Firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Check if user is logged in
        mIsLoggedIn = mFirebaseAuth.getCurrentUser() != null;
        // Initialize Realm
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
        // For Realm migration
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(1).migration(new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            }
        }).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    public void setUsername(String username) {
        mSharedPref.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return mSharedPref.getString(KEY_USERNAME, "");
    }

    public ArrayList<Message> getAllMessages() {
        RealmResults<Message> realmResults = mRealm.where(Message.class).findAll();
        return new ArrayList<>(realmResults);
    }

    public void saveMessage(Message message) {
        mRealm.beginTransaction();
        mRealm.insertOrUpdate(message);
        mRealm.commitTransaction();
    }

    public String getUserId() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            return mFirebaseAuth.getCurrentUser().getUid();
        } else {
            return "";
        }
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }
}
