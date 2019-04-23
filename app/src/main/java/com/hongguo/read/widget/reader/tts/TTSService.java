package com.hongguo.read.widget.reader.tts;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2017/7/10.
 */

public class TTSService extends Service {

    private SpeechSynthesizer mTts;
    private AudioManager      mAudioManager;

    private boolean mTTSInitSuccess = false;

    private InitListener mTtsInitListener = i -> {
        if (i != ErrorCode.SUCCESS) {
            mTTSInitSuccess = false;
        } else {
            if (TTSManager.ttsInstall()) {
                mTTSInitSuccess = true;
            }
        }
    };
    private String      mSpeakerName;
    private String      mSpeed;
    private String      mReadContent;
    private TTSListener mTtsListener;

    @Override
    public void onCreate() {
        super.onCreate();
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    private void setParam(String speakName, String speed) {
        if (TextUtils.isEmpty(speakName)) {
            speakName = "xiaoyan";
        }

        if (TextUtils.isEmpty(speed)) {
            speed = "50";
        }

        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, speakName);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, speed);
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, 50 + "");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, 50 + "");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTts != null) {
            mTts.destroy();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new TTSBinder();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        mTts.stopSpeaking();
    }


    private void initTTS() {
        if (!mTTSInitSuccess) {
            mTts.destroy();
            mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        }
    }

    private List<Speaker> getSpeaker() {
        List<Speaker> speakers = new ArrayList<>();
        if (!mTTSInitSuccess) {
            mTts.destroy();
            mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
            return speakers;
        }
        String parameter = mTts.getParameter(SpeechConstant.LOCAL_SPEAKERS);
        String[] split = parameter.split(";");
        for (String s : split) {
            Speaker speaker = new Speaker();
            String[] names = s.split(":");
            speaker.speekerName = names[0];
            speaker.speekerChineseName = names[1];
            speakers.add(speaker);
        }
        return speakers;
    }

    private void startRead(String readContent, String speakName, String speed, TTSListener ttsListener) {
        if (!mTTSInitSuccess) {
            return;
        }
        mSpeed = speed;
        mSpeakerName = speakName;
        mReadContent = readContent;
        mTtsListener = ttsListener;
        mTts.stopSpeaking();
        setParam(speakName, mSpeed);
        mTts.startSpeaking(mReadContent, mTtsListener);
    }

    private void setSpeed(int speed) {
        if (!mTTSInitSuccess) {
            return;
        }
        mSpeed = speed + "";
        startRead(mReadContent, mSpeakerName, mSpeed, mTtsListener);
    }

    private void resumeReader() {
        if (!mTTSInitSuccess) {
            return;
        }
        mTts.resumeSpeaking();
    }


    private void pauseReader() {
        if (!mTTSInitSuccess) {
            return;
        }
        mTts.pauseSpeaking();
    }

    private void stopReader() {
        if (!mTTSInitSuccess) {
            return;
        }
        mTts.stopSpeaking();
    }

    public class TTSBinder extends Binder {
        public void stopReader() {
            TTSService.this.stopReader();
        }

        public void pauseReader() {
            TTSService.this.pauseReader();
        }

        public void initTTS() {
            TTSService.this.initTTS();
        }

        public void resumeReader() {
            TTSService.this.resumeReader();
        }

        public void startRead(String readContent, String speakName, String speed, TTSListener ttsListener) {
            TTSService.this.startRead(readContent, speakName, speed, ttsListener);
        }

        public void setSpeed(int speed) {
            TTSService.this.setSpeed(speed);
        }

        public List<Speaker> getSpeaker() {
            return TTSService.this.getSpeaker();
        }
    }

    public abstract static class TTSListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public abstract void onCompleted(SpeechError speechError);

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    }

    public static class Speaker {
        public String speekerName;
        public String speekerChineseName;
        public boolean choose = false;
    }

}
