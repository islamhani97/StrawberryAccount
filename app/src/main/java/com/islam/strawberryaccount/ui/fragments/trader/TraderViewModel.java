package com.islam.strawberryaccount.ui.fragments.trader;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.data.Repository;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.ui.BaseViewModel;
import com.islam.strawberryaccount.utils.SingleLiveData;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class TraderViewModel extends BaseViewModel {

    private final MutableLiveData<Trader> traderLiveData;
    private final SingleLiveData<String> errorLiveData;

    private boolean isTraderDataRequested;

    @Inject
    public TraderViewModel(@ApplicationContext Context context, Repository repository) {
        super(context, repository);

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
                            errorLiveData.setValue(context.getString(R.string.error_data));
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
