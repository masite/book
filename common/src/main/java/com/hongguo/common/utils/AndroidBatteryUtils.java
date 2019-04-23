package com.hongguo.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Administrator on 2018/2/12.
 */

public class AndroidBatteryUtils {

    private Context                mContext;
    private BatteryChanageListener mBatteryChanageListener;
    private BatteryReceiver mBatteryReceiver;

    public AndroidBatteryUtils(Context context) {
        mContext = context;
    }

    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mBatteryReceiver = new BatteryReceiver(mBatteryChanageListener);
        mContext.registerReceiver(mBatteryReceiver, filter);
    }

    public void onPause() {
        mContext.unregisterReceiver(mBatteryReceiver);
    }

    public class BatteryReceiver extends BroadcastReceiver {
        private BatteryChanageListener mBatteryChanageListener;


        public BatteryReceiver(BatteryChanageListener batteryChanageListener) {
            this.mBatteryChanageListener = batteryChanageListener;
        }

        public void setBatteryChanageListener(BatteryChanageListener batteryChanageListener) {
            mBatteryChanageListener = batteryChanageListener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int current = intent.getExtras().getInt("level");// 获得当前电量
            int total = intent.getExtras().getInt("scale");// 获得总电量
            int percent = current * 100 / total;
            if(mBatteryChanageListener != null){
                mBatteryChanageListener.batteryChange(percent);
            }
        }
    }

    public void setBatteryChanageListener(BatteryChanageListener batteryChanageListener) {
        mBatteryChanageListener = batteryChanageListener;
        if(mBatteryReceiver != null){
            mBatteryReceiver.setBatteryChanageListener(mBatteryChanageListener);
        }
    }

    public interface BatteryChanageListener {
        void batteryChange(int percent);
    }

}
