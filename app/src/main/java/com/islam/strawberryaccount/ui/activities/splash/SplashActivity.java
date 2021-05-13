package com.islam.strawberryaccount.ui.activities.splash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseUser;
import com.islam.strawberryaccount.pojo.Version;
import com.islam.strawberryaccount.ui.activities.main.MainActivity;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.databinding.ActivitySplashBinding;
import com.islam.strawberryaccount.ui.activities.signin.SignInActivity;
import com.islam.strawberryaccount.ui.dialogs.LoadingDialog;
import com.islam.strawberryaccount.utils.Constants;

import java.util.Locale;


public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private SplashViewModel splashViewModel;
    private String selectedLanguage;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        // animate splash view
        binding.splashView.startAnimation();
        binding.splashView.endAnimation();

        loadingDialog = new LoadingDialog(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.show();
                splashViewModel.getLastVersion();
            }
        }, 4100);

        observeOnData();

    }


    private void observeOnData() {

        splashViewModel.getVersionLiveData().observe(this, new Observer<Version>() {
            @Override
            public void onChanged(Version version) {
                loadingDialog.dismiss();
                try {
                    int currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                    if (currentVersionCode < version.getVersionCode()) {
                        showDialog(R.string.message_new_version,
                                R.string.update,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(version.getUrl()));
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                    } else {
                        splashViewModel.getLanguage();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    showDialog(R.string.message_check_last_version,
                            R.string.go,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(version.getUrl()));
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });


        splashViewModel.getLanguageLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String language) {

                if (language.equals(Constants.NONE)) {
                    selectedLanguage = Constants.LANGUAGE_EN;
                    String s[] = {getString(R.string.language_en), getString(R.string.language_ar)};
                    new MaterialAlertDialogBuilder(SplashActivity.this, R.style.DialogTheme)
                            .setTitle(R.string.message_choose_language)
                            .setSingleChoiceItems(s, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0: {
                                            selectedLanguage = Constants.LANGUAGE_EN;
                                            break;
                                        }
                                        case 1: {
                                            selectedLanguage = Constants.LANGUAGE_AR;
                                            break;
                                        }

                                    }
                                }
                            })
                            .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    splashViewModel.setLanguage(selectedLanguage);
                                    setLocale(selectedLanguage);
                                    splashViewModel.getCurrentUser();
                                }
                            })
                            .setCancelable(false)
                            .setBackground(getDrawable(R.drawable.shape_dialog_background))
                            .show();
                } else {
                    setLocale(language);
                    splashViewModel.getCurrentUser();
                }
            }
        });

        splashViewModel.getCurrentUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    splashViewModel.setSignInStatus(false);
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    intent.putExtra(Constants.KEY_COMPLETABLE, false);
                    startActivity(intent);
                    finish();
                } else {
                    splashViewModel.getSignInStatus();
                }
            }
        });

        splashViewModel.getSignInStatusLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean signedIn) {
                Intent intent;
                if (!signedIn) {
                    intent = new Intent(SplashActivity.this, SignInActivity.class);
                    intent.putExtra(Constants.KEY_COMPLETABLE, true);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra(Constants.KEY_SHOW_ABOUT, false);
                }
                startActivity(intent);
                finish();
            }
        });

        splashViewModel.getErrorLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer errorCode) {
                loadingDialog.dismiss();

                if (errorCode.equals(Constants.ERROR_CODE_VERSION_NOT_EXIST)) {
                    showDialog(R.string.message_check_last_version,
                            R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                } else if (errorCode.equals(Constants.ERROR_CODE_VERSION_NO_INTERNET)) {
                    showDialog(R.string.error_no_internet,
                            R.string.retry,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loadingDialog.show();
                                    splashViewModel.getLastVersion();
                                }
                            });
                }else if (errorCode.equals(Constants.ERROR_CODE_VERSION_NETWORK_CONNECTION)) {
                    showDialog(R.string.error_network_connection,
                            R.string.retry,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loadingDialog.show();
                                    splashViewModel.getLastVersion();
                                }
                            });
                }else if (errorCode.equals(Constants.ERROR_CODE_VERSION_UNKNOWN)) {
                    showDialog(R.string.error_unknown,
                            R.string.retry,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loadingDialog.show();
                                    splashViewModel.getLastVersion();
                                }
                            });
                }
            }
        });
    }

    private void showDialog(int message, int actionText, DialogInterface.OnClickListener listener) {
        new MaterialAlertDialogBuilder(SplashActivity.this, R.style.DialogTheme)
                .setMessage(message)
                .setPositiveButton(actionText, listener)
                .setCancelable(false)
                .setBackground(getDrawable(R.drawable.shape_dialog_background))
                .show();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }



}