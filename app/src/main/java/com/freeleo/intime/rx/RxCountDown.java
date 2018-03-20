package com.freeleo.intime.rx;

import android.content.Context;

import com.freeleo.intime.utils.DateUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Description: 
 * Author: lizhen
 * Create Date: 20/03/2018
 */
public class RxCountDown {

    public static Observable<Long> countDown(final long timeStamp){
        return Observable.interval(1, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return timeStamp - aLong;
                    }
                });
    }

}
