package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.mine.QuestionBean;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/7.
 */

public class QuestionAdapter extends IosRecyclerAdapter {

    private List<QuestionBean.DataBean> mDataBeans;

    public QuestionAdapter(Context context, List<QuestionBean.DataBean> dataBeans) {
        super(context);
        mDataBeans = dataBeans;
    }

    @Override
    protected int getCellTitleResource(int areaPosition) {
        return R.layout.adapter_question_title;
    }

    @Override
    protected void initCellTitleView(BaseHoder hoder, int areaPosition) {
        super.initCellTitleView(hoder, areaPosition);
        QuestionBean.DataBean dataBean = mDataBeans.get(areaPosition);
        if (dataBean.isOpen){
            hoder.getViewById(R.id.question_open).setRotation(180);
        }else{
            hoder.getViewById(R.id.question_open).setRotation(0);
        }
        hoder.setText(R.id.question_title, (areaPosition + 1)+"、"+dataBean.question);
        hoder.itemView.setOnClickListener(v->{
            dataBean.isOpen = !dataBean.isOpen;
            notifyChange();
        });
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_question_answer;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        QuestionBean.DataBean dataBean = mDataBeans.get(areaPosition);
        hoder.setText(R.id.question_answer, dataBean.answer);
    }

    @Override
    protected int getAreaSize() {
        return mDataBeans.size();
    }

    @Override
    protected int getCellCount(int areaPosition) {
        if (mDataBeans.get(areaPosition).isOpen) return 1;
        return 0;
    }

}
