package com.haishu.SevenBreakFast.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by henry on 2016/4/6.
 */
public class TimerCount extends CountDownTimer {
    private TextView textView;

    public TimerCount(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    public TimerCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onFinish() {
        textView.setClickable(true);
        textView.setText(" 重新获取 ");
    }

    @Override
    public void onTick(long arg0) {
        textView.setClickable(false);
        textView.setText( " (" + arg0 / 1000 + "s) ");
    }
}

