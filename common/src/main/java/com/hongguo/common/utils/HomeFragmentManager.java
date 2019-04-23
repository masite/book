package com.hongguo.common.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.hongguo.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created time 2017/11/30.
 *
 * @author losg
 */

public class HomeFragmentManager {

    private FragmentManager    mFragmentManager;
    private List<BaseFragment> mFragments;

    public HomeFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mFragments = new ArrayList<>();
    }

    public void addFragments(Class<? extends BaseFragment>... clazz) {
        mFragments.clear();
        try {
            for (Class<? extends BaseFragment> fragmentExClass : clazz) {
                BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(fragmentExClass.getSimpleName());
                if (fragment == null) {
                    fragment = fragmentExClass.newInstance();
                }
                mFragments.add(fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTab(int fragmentPlaceRescource, int position) {

        if (position >= mFragments.size()) {
            return;
        }

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            BaseFragment fragmentEx = mFragments.get(i);
            if (position == i) {
                fragmentEx.showChange(true);
                if (fragmentEx.isAdded()) {
                    fragmentTransaction.show(fragmentEx);
                } else {
                    fragmentTransaction.add(fragmentPlaceRescource, fragmentEx, fragmentEx.getClass().getSimpleName());
                    fragmentTransaction.show(fragmentEx);
                }
            } else {
                fragmentEx.showChange(false);
                if (fragmentEx.isAdded()) {
                    fragmentTransaction.hide(fragmentEx);
                }
            }
        }
        fragmentTransaction.commit();
    }

    public List<BaseFragment> getFragments() {
        return mFragments;
    }
}
