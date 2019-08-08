package com.example.alc_40_challenge_20;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    public static FirebaseAuth.AuthStateListener mAuthListner;
    public static ArrayList<TravelDeal> mDeals;
    private static ListActivity caller;
    private static final int RC_SIGN_IN = 123;
//    public static boolean isAdmin;


    private FirebaseUtil(){}

    public static void openFbReference(String ref, final Context context){
        if (firebaseUtil == null){
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null){
                    FirebaseUtil.signIn();
                    }
//                    else{
//                        String userId = firebaseAuth.getUid();
//                        checkAdmin(userId);
//                    }
                    Toast.makeText(context,"welcome", Toast.LENGTH_LONG).show();
                }
            };

            connectStorage();
        }
        mDeals = new ArrayList<TravelDeal>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

//    private static void checkAdmin(String uid){
//
//        FirebaseUtil.isAdmin = false;
//        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators")
//                .child(uid);
//        ChildEventListener listener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                FirebaseUtil.isAdmin = true;
//                caller.showMenu();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        ref.addChildEventListener(listener);
//    }

    private static void signIn(){

    }

    public static void attachListener() {

        mFirebaseAuth.addAuthStateListener(mAuthListner);
    }

    public static void detachListener() {

        mFirebaseAuth.removeAuthStateListener(mAuthListner);
    }

    public static void connectStorage(){

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("deals_pictures");
    }
}
