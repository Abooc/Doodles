package org.lee.android.doodles;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * 监听Fragment生命周期，回调给Activity，便于Activity作出对应的处理
 *
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-13.
 */
public class LifecycleFragment extends Fragment {

    private FragmentLifecycle mFragmentLifecycle;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentLifecycle) {
            mFragmentLifecycle = (FragmentLifecycle) activity;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFragmentLifecycle != null) mFragmentLifecycle.onFragmentStart(this);
    }

    @Override
    public void onDestroyView() {
        if (mFragmentLifecycle != null) mFragmentLifecycle.onFragmentDestroy(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mFragmentLifecycle = null;
        super.onDestroy();
    }
}
