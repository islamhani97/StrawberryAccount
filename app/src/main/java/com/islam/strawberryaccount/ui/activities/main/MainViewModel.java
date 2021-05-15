package com.islam.strawberryaccount.ui.activities.main;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.User;
import com.islam.strawberryaccount.ui.BaseViewModel;
import com.islam.strawberryaccount.utils.Constants;
import com.islam.strawberryaccount.utils.SingleLiveData;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MainViewModel extends BaseViewModel {

    // Live Data
    private final MutableLiveData<User> userLiveData;
    private final SingleLiveData<Integer> errorLiveData;

    @Inject
    public MainViewModel(@ApplicationContext Context context, Repository repository) {
        super(context,repository);
        userLiveData = new MutableLiveData<>();
        errorLiveData = new SingleLiveData<>();
    }


    public void getUser() {

        if (Constants.isInternetConnected(context)) {

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

        } else {

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
