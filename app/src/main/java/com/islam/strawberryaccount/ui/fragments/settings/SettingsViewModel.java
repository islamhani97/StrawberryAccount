package com.islam.strawberryaccount.ui.fragments.settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.data.Repository;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<String> languageLiveData;


    private Repository repository;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();

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
