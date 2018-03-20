package com.freeleo.intime.ui;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.freeleo.intime.base.BaseActivity;
import com.freeleo.intime.rx.RxCountDown;
import com.freeleo.intime.utils.DateUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_in_time)
    TextView _InTimeTv;

    Observable<Long> mObservable;
    int type;
    private static final String FONT_DIGITAL_7 = "fonts" + File.separator + "digital-7.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadFont();
        startCountDown();
        switchShowType();
    }

    private void startCountDown(){
        mObservable =  RxCountDown.countDown(1000000000);
        mObservable.subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        _InTimeTv.setText(DateUtils.formatInTimeNone(MainActivity.this,aLong));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void switchShowType() {
        RxView.clicks(_InTimeTv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                type = (type%2)+1;
            }
        });
    }

    private void loadFont(){
        AssetManager assets = getAssets();
        final Typeface font = Typeface.createFromAsset(assets, FONT_DIGITAL_7);
        _InTimeTv.setTypeface(font);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
