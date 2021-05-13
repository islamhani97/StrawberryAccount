package com.islam.strawberryaccount.ui.fragments.sales;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.utils.SingleLiveData;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SalesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Package>> packagesLiveData;
    private SingleLiveData<Void> insertPackageLiveData;
    private SingleLiveData<Void> updatePackageLiveData;
    private SingleLiveData<Void> deletePackageLiveData;
    private SingleLiveData<String> errorLiveDate;

    private Repository repository;
    private Application application;
    private boolean isPackagesDataRequested;

    public SalesViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();
        isPackagesDataRequested = false;

        packagesLiveData = new MutableLiveData<>();
        insertPackageLiveData = new SingleLiveData<>();
        updatePackageLiveData = new SingleLiveData<>();
        deletePackageLiveData = new SingleLiveData<>();
        errorLiveDate = new SingleLiveData<>();
    }

    // Requests

    public void getAllPackagesForTrader(long traderId) {
        if (!isPackagesDataRequested) {
            isPackagesDataRequested = true;
            repository.getAllPackagesForTrader(traderId)
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
                            errorLiveDate.setValue(application.getApplicationContext().getString(R.string.error_data));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void insertPackage(Package aPackage) {

        repository.insertPackage(aPackage)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }
                    @Override
                    public void onComplete() {
                        insertPackageLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveDate.setValue(application.getApplicationContext().getString(R.string.error_add));
                    }
                });
    }

    public void updatePackage(Package aPackage) {
        repository.updatePackage(aPackage)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        updatePackageLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveDate.setValue(application.getApplicationContext().getString(R.string.error_update));
                    }
                });
    }

    public void deletePackage(Package aPackage) {
        repository.deletePackage(aPackage)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        deletePackageLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveDate.setValue(application.getApplicationContext().getString(R.string.error_delete));
                    }
                });
    }

    // Live Data Getters

    public LiveData<List<Package>> getPackagesLiveData() {
        return packagesLiveData;
    }

    public SingleLiveData<Void> getInsertPackageLiveData() {
        return insertPackageLiveData;
    }

    public SingleLiveData<Void> getUpdatePackageLiveData() {
        return updatePackageLiveData;
    }

    public SingleLiveData<Void> getDeletePackageLiveData() {
        return deletePackageLiveData;
    }

    public SingleLiveData<String> getErrorLiveDate() {
        return errorLiveDate;
    }
}
