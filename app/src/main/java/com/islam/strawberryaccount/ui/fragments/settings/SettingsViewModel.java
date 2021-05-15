package com.islam.strawberryaccount.ui.fragments.settings;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.ui.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class SettingsViewModel extends BaseViewModel {

    private final MutableLiveData<String> languageLiveData;


    @Inject
    public SettingsViewModel(@ApplicationContext Context context, Repository repository) {
        super(context,repository);

        languageLiveData = new MutableLiveData<>();
    }

    public void getLanguage() {

        languageLiveData.setValue(repository.getLanguage());

    }

    public void setLanguage(String language){
        repository.setLanguage(language);
    }


    public MutableLiveData<String> getLanguageLiveData() {
        return languageLiveData;
    }
}
