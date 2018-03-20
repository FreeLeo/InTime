package com.freeleo.intime.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Description: <对此类的描述> 
 * Author: lizhen
 * Create Date: 20/03/2018
 * Modified By: lizhen
 * Modified Date: 20/03/2018
 * Why & What is modified: <修改原因描述> 
 */
public class BaseActivity extends AppCompatActivity {
    public CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}
