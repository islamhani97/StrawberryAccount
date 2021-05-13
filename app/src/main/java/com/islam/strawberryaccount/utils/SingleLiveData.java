package com.islam.strawberryaccount.utils;

import android.nfc.Tag;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveData<T> {

    private final MutableLiveData<T> liveData = new MutableLiveData<>();
    private final AtomicBoolean pending = new AtomicBoolean(false);

    public void observe(LifecycleOwner owner, Observer<T> observer) {
        liveData.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (pending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    public void setValue(@Nullable T t) {
        pending.set(true);
        liveData.setValue(t);
    }



}
