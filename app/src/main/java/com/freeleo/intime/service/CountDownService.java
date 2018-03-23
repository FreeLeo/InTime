package com.freeleo.intime.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.freeleo.intime.Constants.CPreferences;
import com.freeleo.intime.Constants.Constants;
import com.freeleo.intime.rx.RxCountDown;
import com.freeleo.intime.ui.NotificationCenter;
import com.freeleo.intime.utils.DateUtils;
import com.freeleo.intime.utils.Logger;
import com.freeleo.intime.utils.PreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class CountDownService extends Service {
    private final String TAG = CountDownService.class.getSimpleName();
    private String desStr;
    public CompositeDisposable mCompositeDisposable;
    Observable<Long> mObservable;
    private Messenger replayMessenger;
    private boolean display;
    public CountDownService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCountDown(getLeftTime());
        return START_STICKY;
    }

    private long getLeftTime(){
        String endLine = PreferencesUtil.get(CPreferences.TIME_END_LINE,"");
        if(TextUtils.isEmpty(endLine)){
            return 60*365*24*60*60*1000;
        }else{
            return DateUtils.date2TimeStamp(endLine,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
    }

    private void startCountDown(long lefttime){
        if(mObservable != null){
            return;
        }
        mObservable =  RxCountDown.countDown(lefttime);
        mObservable.filter(new Predicate<Long>() {
            @Override
            public boolean test(Long aLong) throws Exception {
                return aLong >= 0;
            }
        }).doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if(aLong == 0){
                    stopSelf();
                }
                if(replayMessenger != null) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putLong("lefttime", aLong);
                    message.setData(bundle);
                    replayMessenger.send(message);
                }
            }
        }).throttleFirst(1,TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(Long aLong) {

                if(display){
                    NotificationCenter.showCountDown(CountDownService.this,DateUtils.formatInTimeNoneNoSceond(CountDownService.this,aLong),desStr);
                }else{
                    NotificationCenter.hide(CountDownService.this);
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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d(TAG,"onUnbind");
        replayMessenger = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG,"onDestroy");
        mCompositeDisposable.dispose();
    }

    Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                desStr = msg.getData().getString("data");
                if (msg.replyTo != null) {
                    replayMessenger = msg.replyTo;
                }
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("data", "你要说什么？");
                message.setData(bundle);
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 2){
                replayMessenger = null;
            }else if(msg.what == 3){
                desStr = msg.getData().getString("data");
                RxCountDown._timeStamp = msg.getData().getLong("lefttime",0);
            }else if(msg.what == 4){
                display = msg.getData().getBoolean(CPreferences.DISPLAY_NOTIFICATION);
            }
            super.handleMessage(msg);
        }
    });
}
