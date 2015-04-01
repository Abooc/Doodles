package org.lee.android.doodles.fragment;

import android.support.v4.app.Fragment;

public interface FragmentRunningListener {
        public void onResume(Fragment fragment);
        public void onPause(Fragment fragment);
    }