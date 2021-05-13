package com.islam.strawberryaccount.ui.fragments.trader;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.utils.SingleLiveData;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TraderViewModel extends AndroidViewModel {

    private MutableLiveData<Trader> traderLiveData;
    private SingleLiveData<String> errorLiveData;

    private Repository repository;
    private Application application;
    private boolean isTraderDataRequested;

    public TraderViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = ((com.islam.strawberryaccount.utils.Application) application).getRepository();
        isTraderDataRequested = false;

        traderLiveData = new MutableLiveData<>();
        errorLiveData = new SingleLiveData<>();
    }


    public void getTrader(Long traderId) {
        if (!isTraderDataRequested) {
            isTraderDataRequested = true;
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


    public LiveData<Trader> getTraderLiveData() {
        return traderLiveData;
    }

    public SingleLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
