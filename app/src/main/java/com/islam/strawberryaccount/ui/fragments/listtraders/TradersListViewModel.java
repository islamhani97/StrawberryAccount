package com.islam.strawberryaccount.ui.fragments.listtraders;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.utils.SingleLiveData;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TradersListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Trader>> tradersListLiveData;
    private SingleLiveData<Void> insertTraderLiveData;
    private SingleLiveData<String> errorLiveData;

    private Repository repository;
    private Application application;
    private boolean isTradersDataRequested;

    public TradersListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();
        isTradersDataRequested = false;

        tradersListLiveData = new MutableLiveData<>();
        insertTraderLiveData = new SingleLiveData<>();
        errorLiveData = new SingleLiveData<>();
    }

    // Requests
    public void getAllTraders() {
        if (!isTradersDataRequested) {
            isTradersDataRequested = true;
            repository.getAllTraders()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Trader>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull List<Trader> traders) {
                            tradersListLiveData.setValue(traders);
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

    public void insertTrader(Trader trader) {
        repository.insertTrader(trader)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        insertTraderLiveData.setValue(null);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        errorLiveData.setValue(application.getApplicationContext().getString(R.string.error_add_trader));
                    }
                });
    }

    // live Data Getters

    public LiveData<List<Trader>> getTradersListLiveData() {
        return tradersListLiveData;
    }

    public SingleLiveData<Void> getInsertTraderLiveData() {
        return insertTraderLiveData;
    }

    public SingleLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
