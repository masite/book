package com.hongguo.read.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hongguo.common.utils.SeekBarProgressChange;
import com.hongguo.read.R;
import com.hongguo.read.adapter.SpeakerAdapter;
import com.hongguo.read.widget.reader.tts.TTSService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by losg on 2018/4/8.
 */

public class ReadModePanelDialog extends Dialog implements SpeakerAdapter.SpeakerClickListner {

    @BindView(R.id.read_speed)
    AppCompatSeekBar mReadSpeed;
    @BindView(R.id.read_voice)
    RecyclerView     mReadVoice;
    @BindView(R.id.pre_chapter)
    TextView         mPreChapter;
    @BindView(R.id.exit_voice)
    TextView         mExitVoice;
    @BindView(R.id.next_chapter)
    TextView         mNextChapter;

    private Unbinder                 mBind;
    private Context                  mContext;
    private ReadModePanelListener    mReadModePanelListener;
    private List<TTSService.Speaker> mSpeakers;
    private SpeakerAdapter           mSpeakerAdapter;

    public ReadModePanelDialog(@NonNull Context context) {
        super(context, R.style.ReadModePanel);
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        mContext = context;
        setContentView(R.layout.dialog_read_mode_panel);
        mBind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mSpeakers = new ArrayList<>();
        mSpeakerAdapter = new SpeakerAdapter(mContext, mSpeakers);
        mSpeakerAdapter.setSpeakerClickListner(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mReadVoice.setLayoutManager(linearLayoutManager);
        mReadVoice.setAdapter(mSpeakerAdapter);

        mReadSpeed.setOnSeekBarChangeListener(new SeekBarProgressChange() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (mReadModePanelListener != null) {
                    mReadModePanelListener.readSpeedChange(mReadSpeed.getProgress());
                }
            }
        });
    }

    public void setSpeakers(List<TTSService.Speaker> speakers) {
        mSpeakers.clear();
        mSpeakers.addAll(speakers);
        mSpeakerAdapter.notifyChange();
    }

    public void setSpeakerName(String speakerName){
        mSpeakerAdapter.setReaderName(speakerName);
        mSpeakerAdapter.notifyDataSetChanged();
    }

    public void setSpeakerSpeed(int speed){
        mReadSpeed.setProgress(speed);
    }

    public void destory() {
        mBind.unbind();
    }

    @OnClick(R.id.pre_chapter)
    void preChapter() {
        if (mReadModePanelListener != null) {
            mReadModePanelListener.perChapter();
        }
    }

    @OnClick(R.id.exit_voice)
    void exitVoice() {
        if (mReadModePanelListener != null) {
            mReadModePanelListener.exitReadMode();
        }
        dismiss();
    }

    @OnClick(R.id.next_chapter)
    void nextChapter() {
        if (mReadModePanelListener != null) {
            mReadModePanelListener.nextChapter();
        }
    }

    @Override
    public void click(int position) {
        if (mReadModePanelListener == null) {
            return;
        }

        if (position == mSpeakers.size()) {
            mReadModePanelListener.chooseOtherSpeaker();
        } else {
            mReadModePanelListener.readSpeakerChange(mSpeakers.get(position).speekerName);
        }
    }

    public void setReadModePanelListener(ReadModePanelListener readModePanelListener) {
        mReadModePanelListener = readModePanelListener;
    }

    public interface ReadModePanelListener {
        void perChapter();

        void nextChapter();

        void exitReadMode();

        void readSpeedChange(int speed);

        void readSpeakerChange(String name);

        void chooseOtherSpeaker();
    }
}
