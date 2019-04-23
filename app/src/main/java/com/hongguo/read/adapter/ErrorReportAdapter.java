package com.hongguo.read.adapter;

import android.content.Context;
import android.widget.TextView;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;

/**
 * Created by losg on 2018/4/11.
 */

public class ErrorReportAdapter extends IosRecyclerAdapter {

    public static final String[] sErrors = new String[]{"内容加载失败", "乱码错别字", "目录顺序错误", "排版混乱", "内容空白或缺失", "重复内容或章节", "文不对题", "不良信息", "分类错误", "标签错误"};

    private int selectedPosition = -1;
    private ErrorReportItemClick mErrorReportItemClick;

    public ErrorReportAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_error_report;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        TextView textView = hoder.getViewById(R.id.report_message);
        textView.setText(sErrors[index]);
        textView.setSelected(selectedPosition == index);
        hoder.getItemView().setOnClickListener(v -> {
            mErrorReportItemClick.errorSelected(sErrors[index]);
            selectedPosition = index;
            notifyDataSetChanged();
        });
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return sErrors.length;
    }


    public void setErrorReportItemClick(ErrorReportItemClick errorReportItemClick) {
        mErrorReportItemClick = errorReportItemClick;
    }

    public interface ErrorReportItemClick {
        void errorSelected(String message);
    }
}
