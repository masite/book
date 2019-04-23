package com.hongguo.read.mvp.ui.booktype;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.repertory.data.ClassifyRepertory;
import com.losg.library.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 手动创建头部信息
 */

public class BookTypeDetailHeaderHelper implements View.OnClickListener {

    private Context                          mContext;
    private LinearLayout                     mTypeContentVIew;
    private BookTypeDetailHeaderListener     mBookTypeDetailHeaderListener;
    private List<ClassifyRepertory.Classify> mClassifies;

    private String mStatus = "s9";
    private String mSort   = "s9";

    private String       mTypeId;
    private LinearLayout mHeaderView;


    public BookTypeDetailHeaderHelper(Context context) {
        mContext = context;
    }

    public void setInitPosition(String typeId) {
        mTypeId = typeId;
    }

    public View createHeader(List<ClassifyRepertory.Classify> classifies) {

        mClassifies = classifies;
        String statusStr ="s9";
        String sortStr ="s9";
        if (mTypeId.contains("status")) {
            statusStr = mTypeId.substring(mTypeId.indexOf("-") + 1);
            mTypeId = classifies.get(0).id;
        } else if (mTypeId.contains("sort")) {
            sortStr = mTypeId.substring(mTypeId.indexOf("-") + 1);
            mTypeId = classifies.get(0).id;
        }

        mHeaderView = new LinearLayout(mContext);
        mHeaderView.setOrientation(LinearLayout.VERTICAL);
        //总分类
        mTypeContentVIew = new LinearLayout(mContext);
        mTypeContentVIew.setOrientation(LinearLayout.VERTICAL);

        createTypes();

        mHeaderView.addView(mTypeContentVIew, new LinearLayout.LayoutParams(-1, -2));
        mHeaderView.addView(createSplitLine());

        //书本状态
        LinearLayout status = createStatus();
        mHeaderView.addView(status, new LinearLayout.LayoutParams(-1, -2));

        mHeaderView.addView(createSplitLine());

        //排序
        LinearLayout sort = createSort();
        mHeaderView.addView(sort, new LinearLayout.LayoutParams(-1, -2));

        View view = new View(mContext);
        view.setBackgroundColor(0xfff2f2f2);
        mHeaderView.addView(view, new LinearLayout.LayoutParams(-1, DisplayUtil.dip2px(mContext, 8)));

        mHeaderView.findViewWithTag("id-" + mTypeId).setSelected(true);
        mHeaderView.findViewWithTag("status-"+statusStr).setSelected(true);
        mHeaderView.findViewWithTag("sort-"+sortStr).setSelected(true);
        return mHeaderView;
    }

    private LinearLayout createStatus() {
        //s0:连载,s1:完结,s9:全部
        List<String> names = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        names.add("全部");
        tags.add("status-s9");
        names.add("完结");
        tags.add("status-s1");
        names.add("连载");
        tags.add("status-s0");
        return createLine(names, tags);
    }

    private LinearLayout createSort() {
        //s0:更新,s1:畅销(暂为收藏量),s9:默认
        List<String> names = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        names.add("全部");
        tags.add("sort-s9");
        names.add("畅销");
        tags.add("sort-s1");
        names.add("更新");
        tags.add("sort-s0");
        return createLine(names, tags);
    }

    /**
     * 创建所属类型 tag id_bid
     */
    private void createTypes() {
        mTypeContentVIew.removeAllViews();
        int lines = mClassifies.size() % 5 == 0 ? mClassifies.size() / 5 : mClassifies.size() / 5 + 1;
        for (int i = 0; i < lines; i++) {
            List<String> names = new ArrayList<>();
            List<String> tags = new ArrayList<>();
            for (int j = i * 5; j < i * 5 + 5; j++) {
                if (j >= mClassifies.size()) break;
                ClassifyRepertory.Classify classify = mClassifies.get(j);
                names.add(classify.name);
                tags.add("id-" + classify.id);
            }
            mTypeContentVIew.addView(createLine(names, tags));
        }
    }


    private View createSplitLine() {
        View line = View.inflate(mContext, R.layout.view_line, null);
        line.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
        return line;
    }

    private LinearLayout createLine(List<String> names, List<String> tags) {
        int width = mContext.getResources().getDisplayMetrics().widthPixels / 5;
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < names.size(); i++) {
            LinearLayout lable = createLable(names.get(i), tags.get(i));
            linearLayout.addView(lable, new LinearLayout.LayoutParams(width, -2));
        }
        int padding = DisplayUtil.dip2px(mContext, 4);
        linearLayout.setPadding(0, padding, 0, padding);
        return linearLayout;
    }

    private LinearLayout createLable(String text, String tag) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(mContext);
        textView.setOnClickListener(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.bg_book_type_detail);
        textView.setTextColor(mContext.getResources().getColorStateList(R.color.cr_book_type_detail));
        textView.setTextSize(14);
        textView.setTag(tag);
        int top = DisplayUtil.dip2px(mContext, 4);
        int left = DisplayUtil.dip2px(mContext, 8);
        textView.setPadding(left, top, left, top);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(-2, -2));
        return linearLayout;
    }


    private void clearNormalAll(View textView) {
        LinearLayout content = (LinearLayout) textView.getParent().getParent();
        for (int i = 0; i < content.getChildCount(); i++) {
            LinearLayout child = (LinearLayout) content.getChildAt(i);
            child.getChildAt(0).setSelected(false);
        }
    }

    private void clearContentAll(View textView) {
        LinearLayout content = (LinearLayout) textView.getParent().getParent().getParent();
        for (int i = 0; i < content.getChildCount(); i++) {
            LinearLayout child = (LinearLayout) content.getChildAt(i);
            for (int j = 0; j < child.getChildCount(); j++) {
                ((LinearLayout) child.getChildAt(j)).getChildAt(0).setSelected(false);
            }
        }
    }

    public void setBookTypeDetailHeaderListener(BookTypeDetailHeaderListener bookTypeDetailHeaderListener) {
        mBookTypeDetailHeaderListener = bookTypeDetailHeaderListener;
    }

    @Override
    public void onClick(View v) {
        String tag = ((String) v.getTag());
        if (tag.startsWith("id-")) {
            clearContentAll(v);
        } else {
            clearNormalAll(v);
        }
        v.setSelected(true);

        if (mBookTypeDetailHeaderListener != null) {
            //type更改
            if (tag.startsWith("id-")) {
                mTypeId = tag.split("-")[1];
            } else if (tag.startsWith("sort-")) {
                //sort
                mSort = tag.split("-")[1];
            } else if (tag.startsWith("status-")) {
                //status
                mStatus = tag.split("-")[1];
            }
            mBookTypeDetailHeaderListener.bookTypeClick(this, mTypeId, mStatus, mSort);
        }
    }

    public String getSelectedInfo() {
        String type = ((TextView) mHeaderView.findViewWithTag("id-" + mTypeId)).getText().toString();
        String status = ((TextView) mHeaderView.findViewWithTag("status-" + mStatus)).getText().toString();
        String sort = ((TextView) mHeaderView.findViewWithTag("sort-" + mSort)).getText().toString();
        return type + " " + status + " " + sort;
    }

    public void setSelected(String typeid, String status, String sort) {
        mHeaderView.findViewWithTag("id-" + mTypeId).setSelected(false);
        mHeaderView.findViewWithTag("status-" + mStatus).setSelected(false);
        mHeaderView.findViewWithTag("sort-" + mSort).setSelected(false);

        mHeaderView.findViewWithTag("id-" + typeid).setSelected(true);
        mHeaderView.findViewWithTag("status-" + status).setSelected(true);
        mHeaderView.findViewWithTag("sort-" + sort).setSelected(true);

        mTypeId = typeid;
        mStatus = status;
        mSort = sort;
    }

    public interface BookTypeDetailHeaderListener {
        void bookTypeClick(BookTypeDetailHeaderHelper bookTypeDetailHeaderHelper, String typeid, String status, String sort);
    }
}
