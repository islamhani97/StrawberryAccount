package com.islam.strawberryaccount.ui.fragments.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.utils.SingleLiveData;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends AndroidViewModel {

    private MutableLiveData<List<Package>> packagesLiveData;
    private MutableLiveData<List<Cash>> cashesLiveData;

    private SingleLiveData<Void> updatePackageLiveData;
    private SingleLiveData<Void> deletePackageLiveData;
    private SingleLiveData<Void> updateCashLiveData;
    private SingleLiveData<Void> deleteCashLiveData;

    private SingleLiveData<String> errorLiveData;

    private Repository repository;
    private Application application;


    private Long startSearchDate;
    private Long endSearchDate;
    private String searchType;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();

        packagesLiveData = new MutableLiveData<>();
        cashesLiveData = new MutableLiveData<>();
        updatePackageLiveData = new SingleLiveData<>();
        deletePackageLiveData = new SingleLiveData<>();

        updateCashLiveData = new SingleLiveData<>();
        deleteCashLiveData = new SingleLiveData<>();

        errorLiveData = new SingleLiveData<>();

    }



    public void searchInPackagesForTrader(Long traderId) {
        repository.searchInPackagesForTrader(traderId, startSearchDate, endSearchDate)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Package>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<Package> packages) {
                        packagesLiveData.setValue(packages);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_data));
                    }
                });


    }

    public void searchInCashesForTrader(Long traderId) {
        repository.searchInCashesForTrader(traderId, startSearchDate, endSearchDate)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Cash>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<Cash> cashList) {
                        cashesLiveData.setValue(cashList);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_data));
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
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_update));
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
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_delete));
                    }
                });
    }


    public void updateCash(Cash cash) {

        repository.updateCash(cash)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        updateCashLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_update));
                    }
                });
    }

    public void deleteCash(Cash cash) {

        repository.deleteCash(cash)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        deleteCashLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_delete));
                    }
                });
    }

    // Live Data Getters


    public LiveData<List<Package>> getPackagesLiveData() {
        return packagesLiveData;
    }

    public LiveData<List<Cash>> getCashesLiveData() {
        return cashesLiveData;
    }

    public SingleLiveData<Void> getUpdatePackageLiveData() {
        return updatePackageLiveData;
    }

    public SingleLiveData<Void> getDeletePackageLiveData() {
        return deletePackageLiveData;
    }

    public SingleLiveData<Void> getUpdateCashLiveData() {
        return updateCashLiveData;
    }

    public SingleLiveData<Void> getDeleteCashLiveData() {
        return deleteCashLiveData;
    }

    public SingleLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    // Temp Data


    public Long getStartSearchDate() {
        return startSearchDate;
    }

    public void setStartSearchDate(Long startSearchDate) {
        this.startSearchDate = startSearchDate;
    }

    public Long getEndSearchDate() {
        return endSearchDate;
    }

    public void setEndSearchDate(Long endSearchDate) {
        this.endSearchDate = endSearchDate;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}
