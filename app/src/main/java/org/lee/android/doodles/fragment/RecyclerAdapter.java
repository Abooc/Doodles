package org.lee.android.doodles.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.fragment.RecyclerItemViewHolder.OnRecyclerItemChildClickListener;
import org.lee.android.util.Log;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Doodle[] mDoodles;
    private OnRecyclerItemChildClickListener mOnItemClickListener;
    private LayoutInflater mInflater;

    private final int TYPE_VIEW_HEADER = 0;
    private final int TYPE_VIEW_DOODLE = 1;
    private final int TYPE_VIEW_ADVIEW = 2;

    private boolean mHasHeaderView = false;

    public RecyclerAdapter(Context context, Doodle[] doodles, OnRecyclerItemChildClickListener viewClicks) {
        mDoodles = doodles;
        mOnItemClickListener = viewClicks;
        mInflater = LayoutInflater.from(context);
    }

    public void setHasHeader(boolean hasHeader) {
        mHasHeaderView = hasHeader;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasHeaderView && position == 0)
            return TYPE_VIEW_HEADER;
        else if (position % 4 == 0)
            return TYPE_VIEW_ADVIEW;
        else
            return TYPE_VIEW_DOODLE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_VIEW_HEADER:
                view = mInflater.inflate(R.layout.doodle_list_item_header, parent, false);
                return HeaderViewHolder.newInstance(view, mOnItemClickListener);
            case TYPE_VIEW_ADVIEW:
                view = mInflater.inflate(R.layout.doodle_list_item_adview, parent, false);
                return HeaderViewHolder.newInstance(view, mOnItemClickListener);
            default:
                TYPE_VIEW_DOODLE:
                view = mInflater.inflate(R.layout.fragment_doodles_list_item, parent, false);
                return RecyclerItemViewHolder.newInstance(view, mOnItemClickListener);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder implements Attachable {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        public static RecyclerView.ViewHolder newInstance(View view, OnRecyclerItemChildClickListener listener) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void attachData(Object o) {

        }
    }

    public interface Attachable<T> {
        void attachData(T t);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = viewHolder.getItemViewType();
        switch (viewType) {
            case TYPE_VIEW_HEADER:
                return;
            case TYPE_VIEW_ADVIEW:
                return;
            default:
                // TYPE_VIEW_DOODLE:
                Doodle doodle = mDoodles[position];
                ((Attachable) viewHolder).attachData(doodle);
                return;

        }
    }

    @Override
    public int getItemCount() {
        return mDoodles == null ? 0 : mDoodles.length;
    }

}
