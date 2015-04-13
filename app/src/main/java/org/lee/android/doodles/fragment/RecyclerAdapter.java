package org.lee.android.doodles.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.fragment.RecyclerItemViewHolder.OnRecyclerItemChildClickListener;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Doodle[] mDoodles;
    private OnRecyclerItemChildClickListener mOnItemClickListener;
    private LayoutInflater mInflater;

    public RecyclerAdapter(Context context, Doodle[] doodles, OnRecyclerItemChildClickListener viewClicks) {
        mDoodles = doodles;
        mOnItemClickListener = viewClicks;
        mInflater = LayoutInflater.from(context);
    }

    final int TYPE_HEADER = 0;
    final int TYPE_DOODLE = 1;

    private boolean hasHeaderView = false;

    public void setHasHeader(boolean hasHeader) {
        hasHeaderView = hasHeader;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeaderView && position == 0)
            return TYPE_HEADER;
        else
            return TYPE_DOODLE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = mInflater.inflate(R.layout.doodle_list_item_header, parent, false);
            return HeaderViewHolder.newInstance(view, mOnItemClickListener);
        }

        View view = mInflater.inflate(R.layout.fragment_doodles_list_item, parent, false);
        return RecyclerItemViewHolder.newInstance(view, mOnItemClickListener);
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
        Doodle doodle = mDoodles[position];
        ((Attachable) viewHolder).attachData(doodle);
    }

    @Override
    public int getItemCount() {
        return mDoodles == null ? 0 : mDoodles.length;
    }

}
