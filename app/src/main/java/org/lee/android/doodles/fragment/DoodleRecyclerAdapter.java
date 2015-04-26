package org.lee.android.doodles.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.fragment.RecyclerItemViewHolder.OnRecyclerItemChildClickListener;

public class DoodleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Doodle[] mDoodles;
    private OnRecyclerItemChildClickListener mOnItemClickListener;
    private LayoutInflater mInflater;

    /**
     * 最新涂鸦列表-顶部项View
     */
    private final int TYPE_VIEW_HEADER = 0;
    /**
     * 最新涂鸦列表-今日Doodle View
     */
    private final int TYPE_VIEW_TODAY_DOODLE = 3;
    /**
     * 涂鸦View
     */
    private final int TYPE_VIEW_DOODLE = 1;
    /**
     * 广告View
     */
    private final int TYPE_VIEW_ADVIEW = 2;

    private boolean mHasHeaderView = false;
    private Toolbar.OnMenuItemClickListener mOnMenuItemClickListener;

    public DoodleRecyclerAdapter(Context context, Doodle[] doodles,
                                 OnRecyclerItemChildClickListener viewClicks,
                                 Toolbar.OnMenuItemClickListener menuItemClickListener) {
        mDoodles = doodles;
        mOnItemClickListener = viewClicks;
        mInflater = LayoutInflater.from(context);
        mOnMenuItemClickListener = menuItemClickListener;
    }

    public void setHasHeader(boolean hasHeader) {
        mHasHeaderView = hasHeader;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasHeaderView && position == 0)
            return TYPE_VIEW_HEADER;
        if (mHasHeaderView && position == 1)
            return TYPE_VIEW_TODAY_DOODLE;
        else if (position % 4 == 3)
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
            case TYPE_VIEW_TODAY_DOODLE:
                view = mInflater.inflate(R.layout.fragment_today_doodle_item, parent, false);
                return new RecyclerItemViewHolder(view, mOnItemClickListener, mOnMenuItemClickListener);
            case TYPE_VIEW_ADVIEW:
                view = mInflater.inflate(R.layout.doodle_list_item_adview, parent, false);
                return new AdViewHolder(view, mOnMenuItemClickListener);
            default:
                TYPE_VIEW_DOODLE:
                view = mInflater.inflate(R.layout.fragment_doodles_list_item, parent, false);
                return new RecyclerItemViewHolder(view, mOnItemClickListener, mOnMenuItemClickListener);
        }
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {

        public AdViewHolder(View itemView, Toolbar.OnMenuItemClickListener menuItemClickListener) {
            super(itemView);

            Toolbar toolbar = (Toolbar) itemView.findViewById(R.id.ToolbarMenu);
            toolbar.setOnMenuItemClickListener(menuItemClickListener);
            toolbar.inflateMenu(R.menu.adview_menu);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder implements Attachable, View.OnClickListener {

        private OnRecyclerItemChildClickListener mListener;

        public HeaderViewHolder(View itemView, OnRecyclerItemChildClickListener listener) {
            super(itemView);
            mListener = listener;
            itemView.findViewById(R.id.ArchiveDoodles).setOnClickListener(this);
            itemView.findViewById(R.id.AdView).setOnClickListener(this);
        }

        public static RecyclerView.ViewHolder newInstance(View view, OnRecyclerItemChildClickListener listener) {
            return new HeaderViewHolder(view, listener);
        }

        @Override
        public void attachData(Object o) {

        }

        @Override
        public void onClick(View v) {
            if (mListener != null)
                mListener.onItemChildClick(v, getAdapterPosition());
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
