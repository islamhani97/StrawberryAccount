package com.islam.strawberryaccount.ui.fragments.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.utils.SingleLiveData;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Cash>> cashesLiveData;
    private MutableLiveData<List<Package>> packagesLiveData;
    private SingleLiveData<String> errorLiveData;

    private Repository repository;
    private Application application;
    private boolean  isPackagesDataRequested, isCashesDataRequested;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();
         isPackagesDataRequested = isCashesDataRequested = false;

        packagesLiveData = new MutableLiveData<>();
        cashesLiveData = new MutableLiveData<>();
        errorLiveData = new SingleLiveData<>();
    }


    // Requests

    public void getAllPackages() {
        if (!isPackagesDataRequested) {
            isPackagesDataRequested = true;
            repository.getAllPackages()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Package>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull List<Package> packages) {
                            packagesLiveData.setValue(packages);
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                            errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_data));
                            errorLiveData.setValue(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void getAllCashes() {
        if (!isCashesDataRequested) {
            isCashesDataRequested = true;
            repository.getAllCashes()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Cash>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull List<Cash> cashList) {
                            cashesLiveData.setValue(cashList);
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                            errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_data));
                            errorLiveData.setValue(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }


    // Live Data Getters
    public LiveData<List<Package>> getPackagesLiveData() {
        return packagesLiveData;
    }

    public LiveData<List<Cash>> getCashesLiveData() {
        return cashesLiveData;
    }

    public SingleLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
