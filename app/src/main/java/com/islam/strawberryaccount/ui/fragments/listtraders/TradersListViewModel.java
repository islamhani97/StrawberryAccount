package com.islam.strawberryaccount.ui.fragments.listtraders;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.ui.BaseViewModel;
import com.islam.strawberryaccount.utils.SingleLiveData;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class TradersListViewModel extends BaseViewModel {

    private final MutableLiveData<List<Trader>> tradersListLiveData;
    private final SingleLiveData<Void> insertTraderLiveData;
    private final SingleLiveData<String> errorLiveData;

    private boolean isTradersDataRequested;

    @Inject
    public TradersListViewModel(@ApplicationContext Context context, Repository repository) {
        super(context, repository);

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
                            errorLiveData.setValue(context.getString(R.string.error_data));
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
                        errorLiveData.setValue(context.getString(R.string.error_add_trader));
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
