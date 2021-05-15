package com.islam.strawberryaccount.data;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.islam.strawberryaccount.pojo.User;
import com.islam.strawberryaccount.utils.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Firebase {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Inject
    public Firebase(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
    }


    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void sendVerificationCode(String phoneNumber, Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        firebaseAuth.useAppLanguage();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public Task<AuthResult> signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        return firebaseAuth.signInWithCredential(credential);
    }

    public Task<Void> postUser(User user) {
        String userId = firebaseAuth.getUid();
        user.setId(userId);
        user.setPhone(firebaseAuth.getCurrentUser().getPhoneNumber());
        return firebaseFirestore.collection(Constants.COLLECTION_USERS).document(userId).set(user);
    }

    public Task<DocumentSnapshot> getUser() {
        String userId = firebaseAuth.getUid();
        return firebaseFirestore.collection(Constants.COLLECTION_USERS).document(userId).get();
    }

    public Task<DocumentSnapshot> getLastVersion() {
        return firebaseFirestore.collection(Constants.COLLECTION_VERSION).document(Constants.DOCUMENT_VERSION).get();
    }

}
