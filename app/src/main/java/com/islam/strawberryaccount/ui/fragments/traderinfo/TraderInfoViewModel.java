package com.islam.strawberryaccount.ui.fragments.traderinfo;

import android.app.Application;
import android.widget.Toast;

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

public class TraderInfoViewModel extends AndroidViewModel {

    private MutableLiveData<Trader> traderLiveData;
    private SingleLiveData<Void> updateTraderLiveData;
    private SingleLiveData<Void> deleteTraderLiveData;
    private MutableLiveData<List<Cash>> cashesLiveData;
    private MutableLiveData<List<Package>> packagesLiveData;
    private SingleLiveData<String> errorLiveData;

    private Repository repository;
    private Application application;
    private boolean isTraderDateRequested, isPackagesDataRequested, isCashesDataRequested;

    public TraderInfoViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();
        isTraderDateRequested = isPackagesDataRequested = isCashesDataRequested = false;

        traderLiveData = new MutableLiveData<>();
        updateTraderLiveData = new SingleLiveData<>();
        deleteTraderLiveData = new SingleLiveData<>();
        packagesLiveData = new MutableLiveData<>();
        cashesLiveData = new MutableLiveData<>();
        errorLiveData = new SingleLiveData<>();
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
                            errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_data));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void getAllCashesForTrader(long traderId) {
        if (!isCashesDataRequested) {
            isCashesDataRequested = true;
            repository.getAllCashesForTrader(traderId)
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
                            errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_data));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    public void getTrader(Long traderId) {
        if (!isTraderDateRequested) {
            isTraderDateRequested = true;
            repository.getTrader(traderId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Trader>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull Trader trader) {
                            traderLiveData.setValue(trader);
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_data));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }

    public void updateTrader(Trader trader) {

        repository.updateTrader(trader)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        updateTraderLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_update_trader));
                    }
                });


    }

    public void deleteTrader(Trader trader) {

        repository.deleteTrader(trader)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        deleteTraderLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_delete_trader));
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

    public LiveData<Trader> getTraderLiveData() {
        return traderLiveData;
    }

    public SingleLiveData<Void> getUpdateTraderLiveData() {
        return updateTraderLiveData;
    }

    public SingleLiveData<Void> getDeleteTraderLiveData() {
        return deleteTraderLiveData;
    }

    public SingleLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
