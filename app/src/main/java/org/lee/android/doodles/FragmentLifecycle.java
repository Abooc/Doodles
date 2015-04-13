package org.lee.android.doodles;

import android.support.v4.app.Fragment;

/**
 * Fragment生命周期回调接口
 */
public interface FragmentLifecycle {

    void onFragmentStart(Fragment fragment);

    void onFragmentDestroy(Fragment fragment);

}