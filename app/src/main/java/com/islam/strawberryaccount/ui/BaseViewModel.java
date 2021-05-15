package com.islam.strawberryaccount.ui;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.islam.strawberryaccount.data.Repository;

public class BaseViewModel extends ViewModel {

    protected final Context context;
    protected final Repository repository;

    public BaseViewModel(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }
}
