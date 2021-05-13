package com.islam.strawberryaccount.ui.activities.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.callbacks.SettingsCallback;
import com.islam.strawberryaccount.databinding.ActivityMainBinding;
import com.islam.strawberryaccount.pojo.User;
import com.islam.strawberryaccount.utils.Constants;


public class MainActivity extends AppCompatActivity implements SettingsCallback {

    private ActivityMainBinding binding;

    private MainViewModel mainViewModel;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private boolean showAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        showAbout = getIntent().getExtras().getBoolean(Constants.KEY_SHOW_ABOUT);
        mainViewModel.getUser();
        observeOnData();
        setSupportActionBar(binding.appToolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment,
                R.id.tradersListFragment,
                R.id.aboutFragment,
                R.id.settingsFragment)
                .setOpenableLayout(binding.mainDrawerLayout)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.mainNavigationDrawer, navController);

        if(showAbout){
            navController.navigate(R.id.aboutFragment);
        }


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if (destination.getId() == R.id.traderFragment) {
                    binding.mainSearch.setVisibility(View.VISIBLE);
                } else {
                    binding.mainSearch.setVisibility(View.GONE);
                }
            }
        });

    }

    private void observeOnData() {


        mainViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                TextView username = binding.mainNavigationDrawer.getHeaderView(0).findViewById(R.id.menu_header_username);
                username.setText(user.getName());
            }
        });


        mainViewModel.getErrorLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer errorCode) {

                switch (errorCode){
                    case Constants.ERROR_CODE_USER_NO_INTERNET: {
                        showDialog(R.string.error_no_internet,
                                R.string.retry,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mainViewModel.getUser();
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
                                        mainViewModel.getUser();

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
                                        mainViewModel.getUser();
                                    }
                                });
                        break;
                    }
                }

            }
        });
    }

    public View getSearchView() {
        return binding.mainSearch;
    }

    private void showDialog(int message, int actionText, DialogInterface.OnClickListener listener) {
        new MaterialAlertDialogBuilder(MainActivity.this, R.style.DialogTheme)
                .setMessage(message)
                .setPositiveButton(actionText, listener)
                .setCancelable(false)
                .setBackground(getDrawable(R.drawable.shape_dialog_background))
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }


    @Override
    public void refreshActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.KEY_SHOW_ABOUT, false);
        finish();
        startActivity(intent);
    }
}