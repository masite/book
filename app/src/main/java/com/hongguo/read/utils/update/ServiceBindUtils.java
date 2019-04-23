package com.hongguo.read.utils.update;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hongguo.common.utils.LogUtils;

/**
 * Created by losg on 2017/7/27.
 */

public class ServiceBindUtils<T> {

    private Context         mContext;
    private BindListener<T> mBindListener;

    public ServiceBindUtils(Context context) {
        mContext = context;
    }

    public void bindService(Class serviceClazz, BindListener<T> bindListener) {
        mBindListener = bindListener;
        Intent intent = new Intent(mContext, serviceClazz);
        mContext.bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
        LogUtils.e("losg_log", "bind_service:"+serviceClazz);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.e("losg_log", "onServiceConnected:");
            mBindListener.bindSuccess((T) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void unBindSerive() {
        if(mBindListener != null)
        mContext.unbindService(mConnection);
    }

    public interface BindListener<T> {
        void bindSuccess(T t);
    }

}
