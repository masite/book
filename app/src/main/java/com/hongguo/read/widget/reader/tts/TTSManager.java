package com.hongguo.read.widget.reader.tts;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.List;

/**
 * Created by losg on 2017/7/10.
 */

public class TTSManager {

    private Context              mContext;
    private TTSService.TTSBinder mTTSBinder;
    //阅读的速度
    private int                  mSpeed;
    private String               mSpeekerName;
    private boolean              mBindSuccess;

    public TTSManager(Context context) {
        mContext = context;
        Intent intent = new Intent(mContext, TTSService.class);
        mContext.bindService(intent, mTTSService, Service.BIND_AUTO_CREATE);
    }

    public void startRead(String readContent, TTSService.TTSListener ttsListener) {
        mTTSBinder.startRead(readContent, mSpeekerName, mSpeed + "", ttsListener);
    }

    public List<TTSService.Speaker> findSpeekers() {
        return mTTSBinder.getSpeaker();
    }

    public void chooseSpeaker() {
        SpeechUtility utility = SpeechUtility.getUtility();
        if (utility == null) {
            return;
        }
        utility.openEngineSettings(SpeechConstant.ENG_TTS);
        new Handler().postDelayed(() -> mTTSBinder.initTTS(), 500);
    }

    public void pauseSpeek() {
        mTTSBinder.pauseReader();
    }

    //讯飞客户端是否安装
    public static boolean ttsInstall() {
        SpeechUtility utility = SpeechUtility.getUtility();
        if (utility == null) {
            return false;
        }
        return utility.checkServiceInstalled();
    }

    //下载讯飞客户端
    public static String getDownUrl() {
        return SpeechUtility.getUtility().getComponentUrl();
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public void setSpeekerName(String speekerName) {
        mSpeekerName = speekerName;
    }

    private ServiceConnection mTTSService = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mTTSBinder = (TTSService.TTSBinder) service;
            mBindSuccess = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void close() {
        if (mTTSBinder == null) return;
        mTTSBinder.stopReader();
    }

    public void destory() {
        if (mTTSBinder == null) return;
        mTTSBinder.stopReader();
        mContext.unbindService(mTTSService);
    }


    public void resumeReader() {
        mTTSBinder.resumeReader();
    }

    public boolean isBindSuccess() {
        return mBindSuccess;
    }
}
