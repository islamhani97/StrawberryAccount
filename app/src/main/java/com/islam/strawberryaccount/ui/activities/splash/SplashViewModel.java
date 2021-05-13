package com.islam.strawberryaccount.ui.activities.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Version;
import com.islam.strawberryaccount.utils.Constants;
import com.islam.strawberryaccount.utils.SingleLiveData;


public class SplashViewModel extends AndroidViewModel {

    private MutableLiveData<FirebaseUser> currentUserLiveData;
    private MutableLiveData<Boolean> signInStatusLiveData;
    private MutableLiveData<String> languageLiveData;
    private MutableLiveData<Version> versionLiveData;
    private SingleLiveData<Integer> errorLiveData;

    private Application application;
    private Repository repository;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();

        currentUserLiveData = new MutableLiveData<>();
        signInStatusLiveData = new MutableLiveData<>();
        languageLiveData = new MutableLiveData<>();
        versionLiveData = new MutableLiveData<>();
        errorLiveData = new SingleLiveData<>();
    }

    public void getCurrentUser() {
        FirebaseUser currentUser = repository.getCurrentUser();
        currentUserLiveData.setValue(currentUser);
    }


    public void setSignInStatus(boolean signInStatus) {
        repository.setSignInStatus(signInStatus);
    }

    public void getSignInStatus() {
        signInStatusLiveData.setValue(repository.getSignInStatus());
    }

    public void setLanguage(String language) {
        repository.setLanguage(language);
    }

    public void getLanguage() {
        languageLiveData.setValue(repository.getLanguage());
    }

    public void getLastVersion() {

        if (Constants.isInternetConnected(application.getApplicationContext())) {

            repository.getLastVersion().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Version version = documentSnapshot.toObject(Version.class);
                        if (version.getVersionCode() != null && version.getUrl() != null) {
                            versionLiveData.setValue(version);
                        } else {
                            errorLiveData.setValue(Constants.ERROR_CODE_VERSION_NOT_EXIST);
                        }
                    } else {
                        errorLiveData.setValue(Constants.ERROR_CODE_VERSION_NOT_EXIST);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if(e instanceof FirebaseNetworkException){
                        errorLiveData.setValue(Constants.ERROR_CODE_VERSION_NETWORK_CONNECTION);
                    }else{
                        errorLiveData.setValue(Constants.ERROR_CODE_VERSION_UNKNOWN);
                    }
                }
            });
        } else {
            errorLiveData.setValue(Constants.ERROR_CODE_VERSION_NO_INTERNET);
        }


    }


    public LiveData<FirebaseUser> getCurrentUserLiveData() {
        return currentUserLiveData;
    }

    public LiveData<Boolean> getSignInStatusLiveData() {
        return signInStatusLiveData;
    }

    public LiveData<String> getLanguageLiveData() {
        return languageLiveData;
    }

    public LiveData<Version> getVersionLiveData() {
        return versionLiveData;
    }

    public SingleLiveData<Integer> getErrorLiveData() {
        return errorLiveData;
    }
}
