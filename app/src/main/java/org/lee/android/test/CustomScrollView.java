package org.lee.android.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-24.
 */
public class CustomScrollView extends ScrollView {

    private static final int INVALID_POINTER = -1;

    /**
     * The RecyclerView is not currently scrolling.
     *
     * @see #getScrollState()
     */
    public static final int SCROLL_STATE_IDLE = 0;

    /**
     * The RecyclerView is currently being dragged by outside input such as user touch input.
     *
     * @see #getScrollState()
     */
    public static final int SCROLL_STATE_DRAGGING = 1;

    /**
     * The RecyclerView is currently animating to a final position while not under
     * outside control.
     *
     * @see #getScrollState()
     */
    public static final int SCROLL_STATE_SETTLING = 2;

    // Touch/scrolling handling

    private int mScrollState = SCROLL_STATE_IDLE;

    /**
     * An OnScrollListener can be set on a RecyclerView to receive messages
     * when a scrolling event has occurred on that RecyclerView.
     *
     * @see CustomScrollView#setOnScrollListener(OnScrollListener)
     */
    abstract static public class OnScrollListener {
        /**
         * Callback method to be invoked when RecyclerView's scroll state changes.
         *
         * @param recyclerView The RecyclerView whose scroll state has changed.
         * @param newState     The updated scroll state. One of {@link #SCROLL_STATE_IDLE},
         *                     {@link #SCROLL_STATE_DRAGGING} or {@link #SCROLL_STATE_SETTLING}.
         */
        public void onScrollStateChanged(CustomScrollView recyclerView, int newState) {
        }

        /**
         * Callback method to be invoked when the RecyclerView has been scrolled. This will be
         * called after the scroll has completed.
         * <p/>
         * This callback will also be called if visible item range changes after a layout
         * calculation. In that case, dx and dy will be 0.
         *
         * @param recyclerView The RecyclerView which scrolled.
         * @param dx           The amount of horizontal scroll.
         * @param dy           The amount of vertical scroll.
         */
        public void onScrolled(CustomScrollView recyclerView, int dx, int dy) {
        }
    }


    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mScrollListener != null) {
            mScrollListener.onScrolled(this, l, t);
        }
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        if (mScrollListener != null) {
            mScrollListener.onScrolled(this, 0, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        if (mScrollListener != null) {
//            mScrollListener.onScrolled(this, (int)x, (int)y);
        }
        return super.onTouchEvent(event);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    private OnScrollListener mScrollListener;

    /**
     * Return the current scrolling state of the RecyclerView.
     *
     * @return {@link #SCROLL_STATE_IDLE}, {@link #SCROLL_STATE_DRAGGING} or
     * {@link #SCROLL_STATE_SETTLING}
     */
    public int getScrollState() {
        return mScrollState;
    }
}
