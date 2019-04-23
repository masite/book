package com.hongguo.common.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created time 2017/11/27.
 *
 * @author losg
 */

public class CommonPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    public CommonPagerAdapter(FragmentManager fm, Class<? extends Fragment>... fragments) {
        super(fm);
        mFragments = new ArrayList<Fragment>();
        for (Class<? extends Fragment> fragment : fragments) {
            try {
                mFragments.add(fragment.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Fragment> getFragments() {
        return mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
