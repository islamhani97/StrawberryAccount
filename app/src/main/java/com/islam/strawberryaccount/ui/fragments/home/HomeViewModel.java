package com.islam.strawberryaccount.ui.fragments.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.ui.BaseViewModel;
import com.islam.strawberryaccount.utils.SingleLiveData;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class HomeViewModel extends BaseViewModel {

    private final MutableLiveData<List<Cash>> cashesLiveData;
    private final MutableLiveData<List<Package>> packagesLiveData;
    private final SingleLiveData<String> errorLiveData;

    private boolean isPackagesDataRequested, isCashesDataRequested;

    @Inject
    public HomeViewModel(@ApplicationContext Context context, Repository repository) {
        super(context, repository);

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
