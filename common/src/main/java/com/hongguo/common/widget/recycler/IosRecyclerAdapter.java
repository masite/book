package com.hongguo.common.widget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by losg on 2016/10/25.
 */

public abstract class IosRecyclerAdapter extends RecyclerView.Adapter<IosRecyclerAdapter.BaseHoder> {


    private   List<RecyclerItemInfo> mRecyclerItemInfos;
    protected Context                mContext;

    //只有一个header
    private View       mHeaderView;
    //可有多个Footer
    private List<View> mFooterView;

    private int mHeaderStartTag = 1;
    private int mFooterStartTag = 10;

    private boolean mSizeChange = true;
    private int     mTotalSize  = 0;

    public IosRecyclerAdapter(Context context) {
        mRecyclerItemInfos = new ArrayList<>();
        mFooterView = new ArrayList<>();
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return mRecyclerItemInfos.get(position).resource;
    }

    @Override
    public BaseHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return null;
        }
        View view = null;
        //头部
        if (mHeaderView != null && (int) mHeaderView.getTag() == viewType) {
            view = mHeaderView;
        }
        //尾部
        for (View footer : mFooterView) {
            if ((int) footer.getTag() == viewType) {
                view = footer;
                break;
            }
        }

        //通过viewType创建加载view
        if (view == null) {
            view = View.inflate(mContext, viewType, null);
        }
        if (view.getLayoutParams() == null) {
            if (!widthIsWrap()) {
                view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            } else {
                view.setLayoutParams(new RecyclerView.LayoutParams(-2, -2));
            }
        }
        return new BaseHoder(view);
    }

    @Override
    public void onBindViewHolder(BaseHoder holder, int position) {
        RecyclerItemInfo recyclerItemInfo = mRecyclerItemInfos.get(position);
        //头部和尾部，在外部初始化
        if (recyclerItemInfo.areaPosition == -1 && recyclerItemInfo.index == -1) return;

        //cell 头部
        if (recyclerItemInfo.isTitle) {
            initCellTitleView(holder, recyclerItemInfo.areaPosition);
            return;
        }

        //cell 尾部
        if (recyclerItemInfo.isFooter) {
            initFooterView(holder, recyclerItemInfo.areaPosition);
            return;
        }

        initContentView(holder, recyclerItemInfo.areaPosition, recyclerItemInfo.index);

    }

    protected abstract int getContentResource(int areaPosition);

    protected abstract void initContentView(BaseHoder hoder, int areaPosition, int index);

    //cellTitle
    protected int getCellTitleResource(int areaPosition) {
        return 0;
    }

    protected void initCellTitleView(BaseHoder hoder, int areaPosition) {
    }

    //cellFooter
    protected int getFooterResource(int areaPosition) {
        return 0;
    }

    protected void initFooterView(BaseHoder hoder, int areaPosition) {
    }

    protected abstract int getAreaSize();

    protected abstract int getCellCount(int areaPosition);


    @Override
    public int getItemCount() {
        if (mSizeChange) {
            mTotalSize = computerItemCount();
            mSizeChange = false;
        }
        return mTotalSize;
    }

    public void notifyChange() {
        mSizeChange = true;
        notifyDataSetChanged();
    }

    private int computerItemCount() {

        mRecyclerItemInfos.clear();

        if (mHeaderView != null) {
            mRecyclerItemInfos.add(new RecyclerItemInfo(-1, -1, (int) mHeaderView.getTag(), true, false));
        }

        for (int i = 0; i < getAreaSize(); i++) {

            int titleResource = getCellTitleResource(i);
            int footerResource = getFooterResource(i);
            int cellCount = getCellCount(i);

            //cell有标题
            if (titleResource != 0) {
                mRecyclerItemInfos.add(new RecyclerItemInfo(i, -1, titleResource, true, false));
            }
            //cell
            if (cellCount != 0) {
                int resource = getContentResource(i);
                for (int j = 0; j < cellCount; j++) {
                    mRecyclerItemInfos.add(new RecyclerItemInfo(i, j, resource, false, false));
                }
            }
            //cell Footer
            if (footerResource != 0) {
                mRecyclerItemInfos.add(new RecyclerItemInfo(i, -1, footerResource, false, true));
            }
        }

        for (View view : mFooterView) {
            mRecyclerItemInfos.add(new RecyclerItemInfo(-1, -1, (int) view.getTag(), false, true));
        }
        return mRecyclerItemInfos.size();
    }


    public void addHeader(View headerView) {
        if (mHeaderView == null) {
            mHeaderView = headerView;
            if (mHeaderView.getTag() == null) {
                if (mHeaderStartTag == 10) {
                    mHeaderView = null;
                    Log.e("LOSG_lOG", "headerView must less then ten");
                    return;
                }
                mHeaderView.setTag(mHeaderStartTag);
                mHeaderStartTag++;
            }
        } else {
            Log.e("LOSG_lOG", "adapter_book_shelf must has one header");
        }
    }

    public void removeHeader() {
        mHeaderView = null;
    }

    public void addFooter(View view) {
        if (view.getTag() == null) {
            view.setTag(mFooterStartTag);
            mFooterStartTag++;
        }
        if (!mFooterView.contains(view)) {
            mFooterView.add(view);
        }
    }

    public void addFooter(View view, int index) {
        if (index > mFooterView.size()) {
            return;
        }
        if (view.getTag() == null) {
            view.setTag(mFooterStartTag);
            mFooterStartTag++;
        }
        if (!mFooterView.contains(view)) {
            mFooterView.add(index, view);
        }
    }

    public void removeFooter(View view) {
        if (mFooterView.contains(view)) {
            mFooterView.remove(view);
        }
    }

    public int getHeaderSize() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterSize() {
        return mFooterView.size();
    }

    protected boolean widthIsWrap() {
        return false;
    }

    static class RecyclerItemInfo {
        public int     areaPosition;
        public int     index;
        public int     resource;
        public boolean isTitle;
        public boolean isFooter;

        public RecyclerItemInfo(int areaPosition, int index, int resource, boolean isTitle, boolean isFooter) {
            this.areaPosition = areaPosition;
            this.index = index;
            this.resource = resource;
            this.isTitle = isTitle;
            this.isFooter = isFooter;
        }
    }


    public static class BaseHoder extends RecyclerView.ViewHolder {

        public Map<Integer, Object> map = new HashMap<>();
        public View itemView;

        public BaseHoder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public View getItemView() {
            return itemView;
        }

        public <T extends View> T getViewById(int id) {
            T view = (T) map.get(id);
            if (view == null) {
                view = (T) itemView.findViewById(id);
                map.put(id, view);
            }
            return view;
        }

        public void setText(int id, CharSequence text) {
            TextView textView = (TextView) map.get(id);
            if (textView == null) {
                textView = (TextView) itemView.findViewById(id);
                map.put(id, textView);
            }
            textView.setText(text);
        }
    }
}
