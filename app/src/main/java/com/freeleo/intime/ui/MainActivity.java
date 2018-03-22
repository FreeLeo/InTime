package com.freeleo.intime.ui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.freeleo.intime.Constants.CPreferences;
import com.freeleo.intime.Constants.Constants;
import com.freeleo.intime.base.BaseActivity;
import com.freeleo.intime.rx.RxCountDown;
import com.freeleo.intime.service.CountDownService;
import com.freeleo.intime.utils.DateUtils;
import com.freeleo.intime.utils.Logger;
import com.freeleo.intime.utils.PreferencesUtil;
import com.freeleo.intime.utils.Utils;
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
    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.simple_toolbar)
    SimpleToolbar _toolbar;
    @BindView(R.id.tv_in_time)
    TextView _InTimeTv;
    @BindView(R.id.des_tv)
    TextView _desTv;

    MainReceiver mBroadcastReceiver;
    Observable<Long> mObservable;
    ServiceConnection mServiceConnection;
    Messenger mMessenger;
    int type;
    private static final String FONT_DIGITAL_7 = "fonts" + File.separator + "digital-7.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        startService();
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
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
    }

    private void startService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mMessenger = new Messenger(iBinder);
                sendMessageToService(1,PreferencesUtil.get(CPreferences.DESCRIPTION_COUNTDOWN,getResources().getString(R.string.edit_des)));
//                myBinder = (CountDownService.Binder) iBinder;
//                myBinder.setNextListener(new CountDownService.NextListener() {
//                    @Override
//                    public void onNext(long leftTime) {
//                        _InTimeTv.setText(type % 2 == 1 ? DateUtils.formatInTime(MainActivity.this, leftTime) : DateUtils.formatInTimeNone(MainActivity.this, leftTime));
//                    }
//                });
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(new Intent(this, CountDownService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
        startService(new Intent(this, CountDownService.class));
    }

    public void sendMessageToService(int what,String str) {
        if(mMessenger == null){
            return;
        }
        Message message = Message.obtain(null, what);
        message.replyTo = replyMessenger;
        Bundle bundle = new Bundle();
        bundle.putString("data", str);
        message.setData(bundle);
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToService(int what,String str,long lefttime) {
        if(mMessenger == null){
            return;
        }
        Message message = Message.obtain(null, what);
        message.replyTo = replyMessenger;
        Bundle bundle = new Bundle();
        bundle.putString("data", str);
        bundle.putLong("lefttime", lefttime);
        message.setData(bundle);
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    Messenger replyMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long leftTime = msg.getData().getLong("lefttime");
            _InTimeTv.setText(type % 2 == 1 ? DateUtils.formatInTime(MainActivity.this, leftTime) : DateUtils.formatInTimeNone(MainActivity.this, leftTime));
            super.handleMessage(msg);
        }
    });

    private void switchShowType() {
        RxView.clicks(_InTimeTv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                type = (type % 2) + 1;
            }
        });
    }

    private void loadFont() {
        AssetManager assets = getAssets();
        final Typeface font = Typeface.createFromAsset(assets, FONT_DIGITAL_7);
        _InTimeTv.setTypeface(font);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxCountDown._timeStamp = getLeftTime();
        sendMessageToService(3,PreferencesUtil.get(CPreferences.DESCRIPTION_COUNTDOWN,getResources().getString(R.string.edit_des)),getLeftTime());
        Constants.DES_COUNTDOWN = PreferencesUtil.get(CPreferences.DESCRIPTION_COUNTDOWN, "");
        _desTv.setText(PreferencesUtil.get(CPreferences.DESCRIPTION_COUNTDOWN, ""));
    }

    private long getLeftTime() {
        String endLine = PreferencesUtil.get(CPreferences.TIME_END_LINE, "");
        if (TextUtils.isEmpty(endLine)) {
            return 60 * 365 * 24 * 60 * 60 * 1000;
        } else {
            return DateUtils.date2TimeStamp(endLine, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy");
        if (!PreferencesUtil.get(CPreferences.DISPLAY_NOTIFICATION, false)) {
            stopService(new Intent(this, CountDownService.class));
        }
        sendMessageToService(2,"");
        unbindService(mServiceConnection);
        unregisterReceiver(mBroadcastReceiver);
    }

    private void registerReceiver() {
        mBroadcastReceiver = new MainReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_NOTIFICATION);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public class MainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_NOTIFICATION)) {
                Bundle bundle = intent.getExtras();
                boolean display = bundle.getBoolean(CPreferences.DISPLAY_NOTIFICATION);
                if (display) {
                    NotificationCenter.showCountDown(MainActivity.this, _InTimeTv.getText().toString(), _desTv.getText().toString());
                } else {
                    NotificationCenter.hide(MainActivity.this);
                }
            } else {
                long leftTime = intent.getLongExtra("lefttime", 0);
                _InTimeTv.setText(type % 2 == 1 ? DateUtils.formatInTime(MainActivity.this, leftTime) : DateUtils.formatInTimeNone(MainActivity.this, leftTime));
            }
        }
    }
}
