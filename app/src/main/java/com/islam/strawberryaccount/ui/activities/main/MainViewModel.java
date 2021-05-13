package com.islam.strawberryaccount.ui.activities.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.firestore.DocumentSnapshot;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.User;
import com.islam.strawberryaccount.utils.Constants;
import com.islam.strawberryaccount.utils.SingleLiveData;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<User> userLiveData;
    private SingleLiveData<Integer> errorLiveData;

private Application application ;
    private Repository repository;
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();

        userLiveData = new MutableLiveData<>();
        errorLiveData = new SingleLiveData<>();

    }


    public void getUser() {

        if(Constants.isInternetConnected(application.getApplicationContext())){

            repository.getUser().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user;
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(User.class);

                    } else {
                        user = new User();
                    }
                    userLiveData.setValue(user);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseNetworkException) {
                        errorLiveData.setValue(Constants.ERROR_CODE_USER_NETWORK_CONNECTION);
                    } else {
                        errorLiveData.setValue(Constants.ERROR_CODE_USER_UNKNOWN);
                    }
                }
            });

        }else{

            errorLiveData.setValue(Constants.ERROR_CODE_USER_NO_INTERNET);

        }
    }



    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public SingleLiveData<Integer> getErrorLiveData() {
        return errorLiveData;
    }
}
