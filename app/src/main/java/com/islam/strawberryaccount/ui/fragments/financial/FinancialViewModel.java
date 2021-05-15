package com.islam.strawberryaccount.ui.fragments.financial;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Cash;
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
public class FinancialViewModel extends BaseViewModel {

    private final MutableLiveData<List<Cash>> cashesLiveData;
    private final SingleLiveData<Void> insertCashLiveData;
    private final SingleLiveData<Void> updateCashLiveData;
    private final SingleLiveData<Void> deleteCashLiveData;
    private final SingleLiveData<String> errorLiveData;

    private boolean isCashesDataRequested;

    @Inject
    public FinancialViewModel(@ApplicationContext Context context, Repository repository) {
        super(context, repository);

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
                            errorLiveData.setValue(context.getString(R.string.error_data));
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
                        errorLiveData.setValue(context.getString(R.string.error_add));
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
                        errorLiveData.setValue(context.getString(R.string.error_update));
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
                        errorLiveData.setValue(context.getString(R.string.error_delete));
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
