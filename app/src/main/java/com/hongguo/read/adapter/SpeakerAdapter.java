package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.widget.reader.tts.TTSService;

import java.util.List;


/**
 * Created by losg on 2017/7/12.
 */

public class SpeakerAdapter extends IosRecyclerAdapter {

    private List<TTSService.Speaker> mSpeakers;
    private SpeakerClickListner      mSpeakerClickListner;

    private String mReaderName = "";

    public SpeakerAdapter(Context context, List<TTSService.Speaker> speakers) {
        super(context);
        mSpeakers = speakers;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.view_speaker;
    }

    public void setReaderName(String readerName) {
        mReaderName = readerName;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {

        hoder.itemView.setOnClickListener(v -> {
            if (mSpeakerClickListner != null) {
                if (index < mSpeakers.size()) {
                    mReaderName = mSpeakers.get(index).speekerName;
                    notifyDataSetChanged();
                }
                mSpeakerClickListner.click(index);
            }
        });

        if (index == mSpeakers.size()) {
            hoder.setText(R.id.speaker_name, "其他");
            hoder.getViewById(R.id.speaker_name).setSelected(false);
            return;
        }

        TTSService.Speaker speaker = mSpeakers.get(index);


        hoder.setText(R.id.speaker_name, speaker.speekerChineseName);
        if (mReaderName.equals(speaker.speekerName)) {
            hoder.getViewById(R.id.speaker_name).setSelected(true);
        } else {
            hoder.getViewById(R.id.speaker_name).setSelected(false);
        }
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected boolean widthIsWrap() {
        return true;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mSpeakers.size() + 1;
    }

    public interface SpeakerClickListner {
        void click(int position);
    }

    public void setSpeakerClickListner(SpeakerClickListner speakerClickListner) {
        mSpeakerClickListner = speakerClickListner;
    }
}
