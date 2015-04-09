package org.lee.android.doodles.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.lee.android.doodles.R;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;

/**
 * 搜索栏
 */
public class SearchBar extends FrameLayout implements View.OnClickListener, View.OnFocusChangeListener,
        TextView.OnEditorActionListener {


    /**
     * 搜索事件监听器
     */
    public interface OnSearchEventListener {
        /**
         * 当软键盘响应IME_ACTION_SEARCH事件时，回调此方法
         *
         * @param q 要搜索的关键词
         */
        void onSearch(String q);
    }

    /**
     * 取消搜索事件监听器
     */
    public interface OnCancleClickListener {
        /**
         * 当点击搜索栏中左侧取消按钮时，回调此方法
         */
        void onCancel();
    }

    /**
     * '取消'事件View
     */
    private View mCancelView;

    private OnSearchEventListener mOnSearchEventListener;

    private OnCancleClickListener mOnCancleClickListener;

    public SearchBar(Context context) {
        this(context, null, 0);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.app_searcher, this, true);

        EditText iEditText = (EditText) findViewById(R.id.EditText);
        mCancelView = findViewById(R.id.Cancel);
        mCancelView.setOnClickListener(this);
        iEditText.setOnFocusChangeListener(this);
        iEditText.setOnEditorActionListener(this);
        findViewById(R.id.VoiceSearch).setOnClickListener(this);
    }

    /**
     * 设置搜索事件
     * @param listener
     */
    public void setOnSearchEventListener(OnSearchEventListener listener) {
        mOnSearchEventListener = listener;
    }

    /**
     * 设置取消搜索事件
     * @param listener
     */
    public void setOnCancleClickListener(OnCancleClickListener listener) {
        mOnCancleClickListener = listener;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String q = v.getText().toString();
            if (mOnSearchEventListener != null) {
                mOnSearchEventListener.onSearch(q);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.anchor("hasFocus" + hasFocus);
        if (hasFocus) {
//            mToolbarContainer.animate().translationY(-mToolbarHeight).setInterpolator(new AccelerateInterpolator(2)).start();
            mCancelView.setVisibility(View.VISIBLE);
        } else {
            mCancelView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Menu:
                if (mOnCancleClickListener != null) {
                    mOnCancleClickListener.onCancel();
                }
                return;
            case R.id.VoiceSearch:
                Toast.show("建设中...");
                return;
        }
    }


}