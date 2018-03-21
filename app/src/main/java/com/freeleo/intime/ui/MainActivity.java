package com.freeleo.intime.ui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.freeleo.intime.Constants.CPreferences;
import com.freeleo.intime.Constants.Constants;
import com.freeleo.intime.base.BaseActivity;
import com.freeleo.intime.rx.RxCountDown;
import com.freeleo.intime.utils.DateUtils;
import com.freeleo.intime.utils.PreferencesUtil;
import com.freeleo.intime.view.SimpleToolbar;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.simple_toolbar)
    SimpleToolbar _toolbar;
    @BindView(R.id.tv_in_time)
    TextView _InTimeTv;

    MainReceiver mBroadcastReceiver;
    Observable<Long> mObservable;
    int type;
    private static final String FONT_DIGITAL_7 = "fonts" + File.separator + "digital-7.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        startCountDown(getLeftTime());
        switchShowType();
        registerReceiver();
    }

    private void init() {
        loadFont();
        _toolbar.setMainTitle(getResources().getString(R.string.app_name));
        _toolbar.getTxtRightTitle().setVisibility(View.VISIBLE);
        RxView.clicks(_toolbar.getTxtRightTitle()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
            }
        });
    }

    private void startCountDown(long lefttime){
        mObservable =  RxCountDown.countDown(lefttime);
        mObservable.doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                _InTimeTv.setText(type%2==1?DateUtils.formatInTime(MainActivity.this,aLong):DateUtils.formatInTimeNone(MainActivity.this,aLong));
            }
        }).throttleFirst(1,TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {

                        if(PreferencesUtil.get(CPreferences.DISPLAY_NOTIFICATION,false)){
                            NotificationCenter.showCountDown(MainActivity.this,DateUtils.formatInTimeNoneNoSceond(MainActivity.this,aLong));
                        }
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
    protected void onResume() {
        super.onResume();
        RxCountDown._timeStamp = getLeftTime();
    }

    private long getLeftTime(){
        String endLine = PreferencesUtil.get(CPreferences.TIME_END_LINE,"");
        if(TextUtils.isEmpty(endLine)){
            return 60*365*24*60*60*1000;
        }else{
            return DateUtils.date2TimeStamp(endLine,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void registerReceiver(){
        mBroadcastReceiver = new MainReceiver();
        IntentFilter intentFilter = new IntentFilter();

        // 2. 设置接收广播的类型
        intentFilter.addAction(Constants.ACTION_NOTIFICATION);

        // 3. 动态注册：调用Context的registerReceiver（）方法
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public class MainReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            boolean display = bundle.getBoolean(CPreferences.DISPLAY_NOTIFICATION);
            if(display){
                NotificationCenter.showCountDown(MainActivity.this,_InTimeTv.getText().toString());
            }else{
                NotificationCenter.hide(MainActivity.this);
            }
        }
    }
}
