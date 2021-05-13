package com.islam.strawberryaccount.utils;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.islam.strawberryaccount.BuildConfig;
import com.islam.strawberryaccount.data.Repository;

import java.util.Locale;

import static android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES;

public class Application extends android.app.Application {

    private Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new Repository(this);
    }

    public Repository getRepository() {
        return repository;
    }

}
