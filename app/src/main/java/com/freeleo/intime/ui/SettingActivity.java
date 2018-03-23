package com.freeleo.intime.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.freeleo.intime.Constants.CPreferences;
import com.freeleo.intime.Constants.Constants;
import com.freeleo.intime.base.BaseActivity;
import com.freeleo.intime.utils.DateUtils;
import com.freeleo.intime.utils.PreferencesUtil;
import com.freeleo.intime.view.SimpleToolbar;
import com.jakewharton.rxbinding2.view.RxView;
import com.suke.widget.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.pick_time_tv)
    TextView pickTimeTv;
    @BindView(R.id.switch_button)
    SwitchButton switchButton;
    @BindView(R.id.des_tv)
    TextView desTv;
    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initNav();
        initTimePicker();
    }

    private void initNav() {
        SimpleToolbar toolbar = findViewById(R.id.simple_toolbar);
        toolbar.mBackIv.setVisibility(View.VISIBLE);
        toolbar.setMainTitle(getResources().getString(R.string.setting));
        toolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RxView.clicks(pickTimeTv).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                String date = PreferencesUtil.get(CPreferences.TIME_END_LINE, "");
                if (!TextUtils.isEmpty(date)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
                    pvTime.setDate(calendar);
                }
                pvTime.show();
            }
        });
        RxView.clicks(desTv).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                editDesDialog();
            }
        });
        switchButton.setChecked(PreferencesUtil.get(CPreferences.DISPLAY_NOTIFICATION, false));
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                PreferencesUtil.put(CPreferences.DISPLAY_NOTIFICATION, isChecked);
                Intent intent = new Intent(Constants.ACTION_NOTIFICATION);
                intent.putExtra(CPreferences.DISPLAY_NOTIFICATION, isChecked);
                sendBroadcast(intent);
            }
        });
        pickTimeTv.setText(PreferencesUtil.get(CPreferences.TIME_END_LINE,getResources().getString(R.string.left_time)));
        desTv.setText(PreferencesUtil.get(CPreferences.DESCRIPTION_COUNTDOWN,getResources().getString(R.string.target_des)));
    }


    private void editDesDialog(){
        final EditText et = new EditText(SettingActivity.this);
        et.setText(PreferencesUtil.get(CPreferences.DESCRIPTION_COUNTDOWN,""));
        new  AlertDialog.Builder(SettingActivity.this)
                .setTitle(getResources().getString(R.string.input) )
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferencesUtil.put(CPreferences.DESCRIPTION_COUNTDOWN,et.getText().toString());
                        desTv.setText(PreferencesUtil.get(CPreferences.DESCRIPTION_COUNTDOWN,getResources().getString(R.string.target_des)));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void initTimePicker() {

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                PreferencesUtil.put(CPreferences.TIME_END_LINE, getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
