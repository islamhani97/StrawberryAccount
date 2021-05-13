package com.islam.strawberryaccount.ui.fragments.financial;

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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FinancialViewModel extends AndroidViewModel {

    private MutableLiveData<List<Cash>> cashesLiveData;
    private SingleLiveData<Void> insertCashLiveData;
    private SingleLiveData<Void> updateCashLiveData;
    private SingleLiveData<Void> deleteCashLiveData;
    private SingleLiveData<String> errorLiveData;

    private Repository repository;
    private Application application;
    private boolean isCashesDataRequested;

    public FinancialViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();
        isCashesDataRequested = false;

        cashesLiveData = new MutableLiveData<>();
        insertCashLiveData = new SingleLiveData<>();
        updateCashLiveData = new SingleLiveData<>();
        deleteCashLiveData = new SingleLiveData<>();
        errorLiveData = new SingleLiveData<>();
    }

    // Requests
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

    public void insertCash(Cash cash) {

        repository.insertCash(cash)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        insertCashLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_add));
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

    public LiveData<List<Cash>> getCashesLiveData() {
        return cashesLiveData;
    }

    public SingleLiveData<Void> getInsertCashLiveData() {
        return insertCashLiveData;
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
}
