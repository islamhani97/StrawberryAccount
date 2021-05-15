package com.islam.strawberryaccount.data;

import android.app.Activity;
import android.content.Context;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.pojo.User;
import com.islam.strawberryaccount.utils.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Singleton
public class Repository {

    private Firebase firebase;
    private AppStorage appStorage;
    private Dao dao;


    @Inject
    public Repository(Firebase firebase, AppStorage appStorage, Dao dao) {
        this.firebase = firebase;
        this.appStorage = appStorage;
        this.dao = dao;
    }

    // App Storage
    public void setSignInStatus(boolean signInStatus) {
        appStorage.setSignInStatus(signInStatus);
    }

    public boolean getSignInStatus() {
        return appStorage.getSignInStatus();
    }

    public void setLanguage(String language) {

        appStorage.setLanguage(language);
    }

    public String getLanguage() {
        return appStorage.getLanguage();
    }

    // Firebase
    public FirebaseUser getCurrentUser() {
        return firebase.getCurrentUser();
    }

    public void sendVerificationCode(String phoneNumber, Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        firebase.sendVerificationCode(phoneNumber, activity, callbacks);
    }

    public Task<AuthResult> signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        return firebase.signInWithPhoneAuthCredential(credential);
    }

    public Task<Void> postUser(User user) {
        return firebase.postUser(user);
    }

    public Task<DocumentSnapshot> getUser() {
        return firebase.getUser();
    }

    public Task<DocumentSnapshot> getLastVersion() {
        return firebase.getLastVersion();
    }

    // Database

    public Observable<List<Trader>> getAllTraders() {
        return dao.getAllTraders();
    }

    public Observable<Trader> getTrader(Long traderId) {
        return dao.getTrader(traderId);
    }

    public Completable insertTrader(Trader trader) {
        return dao.insertTrader(trader);
    }

    public Completable updateTrader(Trader trader) {
        return dao.updateTrader(trader);
    }

    public Completable deleteTrader(Trader trader) {
        return dao.deleteTrader(trader);
    }

    //===========================================================

    public Observable<List<Package>> getAllPackages() {
        return dao.getAllPackages();
    }

    public Observable<List<Package>> getAllPackagesForTrader(Long traderId) {
        return dao.getAllPackagesForTrader(traderId);
    }

    public Single<List<Package>> searchInPackagesForTrader(Long traderId, Long startDate, Long endDate) {
        return dao.searchInPackagesForTrader(traderId, startDate, endDate);
    }

    public Completable insertPackage(Package aPackage) {
        return dao.insertPackage(aPackage);
    }

    public Completable updatePackage(Package aPackage) {
        return dao.updatePackage(aPackage);
    }

    public Completable deletePackage(Package aPackage) {
        return dao.deletePackage(aPackage);
    }

    //===========================================================

    public Observable<List<Cash>> getAllCashes() {
        return dao.getAllCashes();
    }

    public Observable<List<Cash>> getAllCashesForTrader(Long traderId) {
        return dao.getAllCashesForTrader(traderId);

    }

    public Single<List<Cash>> searchInCashesForTrader(Long traderId, Long startDate, Long endDate) {
        return dao.searchInCashesForTrader(traderId, startDate, endDate);
    }

    public Completable insertCash(Cash cash) {
        return dao.insertCash(cash);
    }

    public Completable updateCash(Cash cash) {
        return dao.updateCash(cash);
    }

    public Completable deleteCash(Cash cash) {
        return dao.deleteCash(cash);
    }


}
