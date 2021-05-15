package com.islam.strawberryaccount.ui.activities.signin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
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
public class SignInViewModel extends BaseViewModel {

    // Live Data
    private final MutableLiveData<String> verificationIdLiveData;
    private final MutableLiveData<PhoneAuthCredential> credentialLiveData;
    private final MutableLiveData<Void> signInResultLiveData;
    private final MutableLiveData<Void> postUserResultLiveData;
    private final MutableLiveData<User> userLiveData;
    private final SingleLiveData<Integer> errorLiveData;

    private boolean dialogShowingStatus;

    @Inject
    public SignInViewModel(@ApplicationContext Context context, Repository repository) {
        super(context, repository);

        verificationIdLiveData = new MutableLiveData<>();
        credentialLiveData = new MutableLiveData<>();
        signInResultLiveData = new MutableLiveData<>();
        postUserResultLiveData = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new SingleLiveData<>();

        dialogShowingStatus = false;
    }

    // Requests

    public void sendVerificationCode(String phoneNumber, Activity activity) {


        if (Constants.isInternetConnected(context)) {

            repository.sendVerificationCode(phoneNumber, activity,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential credential) {
                            credentialLiveData.setValue(credential);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                // Invalid request
                                errorLiveData.setValue(Constants.ERROR_CODE_MOBILE_NUMBER);
                            } else if (e instanceof FirebaseTooManyRequestsException) {
                                // The SMS quota for the project has been exceeded
                                errorLiveData.setValue(Constants.ERROR_CODE_SERVER);
                            } else if (e instanceof FirebaseNetworkException) {
                                errorLiveData.setValue(Constants.ERROR_CODE_NETWORK_CONNECTION);
                            } else {
                                errorLiveData.setValue(Constants.ERROR_CODE_UNKNOWN);
                            }
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                            verificationIdLiveData.setValue(verificationId);
                        }


                    });
        } else {
            errorLiveData.setValue(Constants.ERROR_CODE_NO_INTERNET);
        }

    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        if (Constants.isInternetConnected(context)) {
            repository.signInWithPhoneAuthCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    signInResultLiveData.setValue(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        errorLiveData.setValue(Constants.ERROR_CODE_VERIFICATION_CODE);
                    } else if (e instanceof FirebaseNetworkException) {
                        errorLiveData.setValue(Constants.ERROR_CODE_NETWORK_CONNECTION);
                    } else {
                        errorLiveData.setValue(Constants.ERROR_CODE_UNKNOWN);
                    }

                }
            });

        } else {
            errorLiveData.setValue(Constants.ERROR_CODE_NO_INTERNET);
        }
    }

    public void postUser(User user) {

        if (Constants.isInternetConnected(context)) {
            repository.postUser(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    postUserResultLiveData.setValue(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseNetworkException) {
                        errorLiveData.setValue(Constants.ERROR_CODE_NETWORK_CONNECTION);
                    } else {
                        errorLiveData.setValue(Constants.ERROR_CODE_UNKNOWN);
                    }
                }
            });
        } else {
            errorLiveData.setValue(Constants.ERROR_CODE_NO_INTERNET);

        }

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

    public void setSignInStatus(boolean signInStatus) {
        repository.setSignInStatus(signInStatus);
    }


    // Live Data Getters

    public LiveData<String> getVerificationIdLiveData() {
        return verificationIdLiveData;
    }

    public LiveData<PhoneAuthCredential> getCredentialLiveData() {
        return credentialLiveData;
    }

    public LiveData<Void> getSignInResultLiveData() {
        return signInResultLiveData;
    }

    public LiveData<Void> getPostUserResultLiveData() {
        return postUserResultLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public SingleLiveData<Integer> getErrorLiveData() {
        return errorLiveData;
    }


    // dialog

    public boolean isDialogShowing() {
        return dialogShowingStatus;
    }

    public void setDialogShowingStatus(boolean dialogShowingStatus) {
        this.dialogShowingStatus = dialogShowingStatus;
    }
}
