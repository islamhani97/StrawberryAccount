package com.islam.strawberryaccount.ui.activities.signin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.SignInNavGraphDirections;
import com.islam.strawberryaccount.callbacks.SignInCallback;
import com.islam.strawberryaccount.databinding.ActivitySignInBinding;
import com.islam.strawberryaccount.pojo.User;
import com.islam.strawberryaccount.ui.activities.main.MainActivity;
import com.islam.strawberryaccount.ui.dialogs.LoadingDialog;
import com.islam.strawberryaccount.utils.Constants;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInActivity extends AppCompatActivity implements SignInCallback {

    private ActivitySignInBinding binding;
    private SignInViewModel signInViewModel;
    private NavController navController;
    private String verificationId;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.activity_sign_in_host);
        navController = navHostFragment.getNavController();

        loadingDialog = new LoadingDialog(this);
        if (signInViewModel.isDialogShowing()) {
            loadingDialog.show();
        }

        boolean completable = getIntent().getExtras().getBoolean(Constants.KEY_COMPLETABLE);

        if (completable) {
            signInViewModel.setDialogShowingStatus(loadingDialog.show());
            signInViewModel.getUser();
        }

        observeOnData();
    }


    private void observeOnData() {

        signInViewModel.getVerificationIdLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                verificationId = s;
                goToAuthCodeDestination();
                signInViewModel.setDialogShowingStatus(loadingDialog.dismiss());
            }
        });

        signInViewModel.getCredentialLiveData().observe(this, new Observer<PhoneAuthCredential>() {
            @Override
            public void onChanged(PhoneAuthCredential credential) {
                signInViewModel.setDialogShowingStatus(loadingDialog.show());
                signInViewModel.signInWithPhoneAuthCredential(credential);
            }
        });


        signInViewModel.getSignInResultLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                signInViewModel.getUser();
            }
        });

        signInViewModel.getPostUserResultLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                signInViewModel.setDialogShowingStatus(loadingDialog.dismiss());

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.putExtra(Constants.KEY_SHOW_ABOUT, true);
                startActivity(intent);

                signInViewModel.setSignInStatus(true);
                finish();
            }
        });

        signInViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                goToUserInfoDestination(user.getName());
                signInViewModel.setDialogShowingStatus(loadingDialog.dismiss());
            }
        });

        signInViewModel.getErrorLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer errorCode) {

                signInViewModel.setDialogShowingStatus(loadingDialog.dismiss());

                switch (errorCode) {
                    case Constants.ERROR_CODE_NO_INTERNET: {
                        Toast.makeText(SignInActivity.this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case Constants.ERROR_CODE_NETWORK_CONNECTION: {
                        Toast.makeText(SignInActivity.this, R.string.error_network_connection, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case Constants.ERROR_CODE_UNKNOWN: {
                        Toast.makeText(SignInActivity.this, R.string.error_unknown, Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case Constants.ERROR_CODE_SERVER: {
                        Toast.makeText(SignInActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case Constants.ERROR_CODE_MOBILE_NUMBER: {
                        Toast.makeText(SignInActivity.this, R.string.error_phone_number, Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case Constants.ERROR_CODE_VERIFICATION_CODE: {
                        Toast.makeText(SignInActivity.this, R.string.error_verification_code, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case Constants.ERROR_CODE_USER_NO_INTERNET: {
                        showDialog(R.string.error_no_internet,
                                R.string.retry,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        signInViewModel.setDialogShowingStatus(loadingDialog.show());
                                        signInViewModel.getUser();
                                    }
                                });
                        break;
                    }

                    case Constants.ERROR_CODE_USER_NETWORK_CONNECTION: {
                        showDialog(R.string.error_network_connection,
                                R.string.retry,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        signInViewModel.setDialogShowingStatus(loadingDialog.show());
                                        signInViewModel.getUser();
                                        ;
                                    }
                                });
                        break;
                    }
                    case Constants.ERROR_CODE_USER_UNKNOWN: {
                        showDialog(R.string.error_unknown,
                                R.string.retry,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        signInViewModel.setDialogShowingStatus(loadingDialog.show());
                                        signInViewModel.getUser();
                                    }
                                });
                        break;
                    }

                }

            }
        });

    }

    @Override
    public void verifyPhoneNumber(String phoneNumber) {

        signInViewModel.setDialogShowingStatus(loadingDialog.show());
        signInViewModel.sendVerificationCode(phoneNumber, this);
    }

    @Override
    public void onCodeSubmit(String code) {
        signInViewModel.setDialogShowingStatus(loadingDialog.show());
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInViewModel.signInWithPhoneAuthCredential(credential);
    }

    @Override
    public void submitUserInfo(User user) {
        signInViewModel.setDialogShowingStatus(loadingDialog.show());
        signInViewModel.postUser(user);
    }

    private void goToAuthCodeDestination() {

        if (navController.getCurrentDestination().getId() != R.id.authCodeFragment && navController.getCurrentDestination().getId() != R.id.userInfoFragment) {
            NavDirections action = SignInNavGraphDirections.actionGlobalAuthCodeFragment();
            navController.navigate(action);
        }

    }

    private void goToUserInfoDestination(String username) {
        if (navController.getCurrentDestination().getId() != R.id.userInfoFragment) {
            NavDirections action = SignInNavGraphDirections.actionGlobalUserInfoFragment(username);
            navController.navigate(action);
        }

    }

    private void showDialog(int message, int actionText, DialogInterface.OnClickListener listener) {
        new MaterialAlertDialogBuilder(SignInActivity.this, R.style.DialogTheme)
                .setMessage(message)
                .setPositiveButton(actionText, listener)
                .setCancelable(false)
                .setBackground(getDrawable(R.drawable.shape_dialog_background))
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingDialog.dismiss();
    }
}