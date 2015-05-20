package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Month;
import org.lee.android.doodles.fragment.DoodleRecyclerItemHolder.OnRecyclerItemChildClickListener;
import org.lee.android.doodles.widget.AdViewHolder;
import org.lee.android.util.Log;

public class DoodleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Card[] mCards;
    private OnRecyclerItemChildClickListener mOnItemClickListener;
    private LayoutInflater mInflater;
    private Activity mActivity;

    public static class Card {
        public int type;
        public int resourceId;
        public Object obj;

        public Card(int type, int resourceId, Object obj) {
            this.type = type;
            this.resourceId = resourceId;
            this.obj = obj;
        }

        /**
         * 最新涂鸦列表-顶部页眉View
         */
        public static final int TYPE_VIEW_HEADER = 0;
        /**
         * 最新涂鸦列表-底部页脚View
         */
        public static final int TYPE_VIEW_FOOTER = 4;
        /**
         * 最新涂鸦列表-今日Doodle View
         */
        public static final int TYPE_VIEW_TODAY_DOODLE = 3;
        /**
         * 涂鸦View
         */
        public static final int TYPE_VIEW_DOODLE = 1;
        /**
         * 广告View
         */
        public static final int TYPE_VIEW_ADVIEW = 2;


    }

    public DoodleRecyclerAdapter(Activity activity, Card[] cards,
                                 OnRecyclerItemChildClickListener viewClicks) {
        mCards = cards;
        mOnItemClickListener = viewClicks;
        mInflater = LayoutInflater.from(activity);
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return mCards[position].type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Card.TYPE_VIEW_HEADER:
                view = mInflater.inflate(R.layout.doodle_list_item_header, parent, false);
                return new HeaderViewHolder(view, mOnItemClickListener);
            case Card.TYPE_VIEW_FOOTER:
                view = mInflater.inflate(R.layout.doodle_list_item_footer, parent, false);
                return new FooterViewHolder(view, mOnItemClickListener);
            case Card.TYPE_VIEW_TODAY_DOODLE:
                view = mInflater.inflate(R.layout.fragment_today_doodle_item, parent, false);
                return new DoodleRecyclerItemHolder(view, mOnItemClickListener);
            case Card.TYPE_VIEW_ADVIEW:
                view = mInflater.inflate(R.layout.doodle_list_item_adview, parent, false);
                return new AdViewHolder(view, mActivity);
            default:
                TYPE_VIEW_DOODLE:
                view = mInflater.inflate(R.layout.fragment_doodles_list_item, parent, false);
                return new DoodleRecyclerItemHolder(view, mOnItemClickListener);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if(holder.getItemViewType() == Card.TYPE_VIEW_ADVIEW){
            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.resume();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if(holder.getItemViewType() == Card.TYPE_VIEW_ADVIEW){
            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.pause();
        }
    }

    /**
     * 底部页脚
     */
    public static class FooterViewHolder extends RecyclerView.ViewHolder implements Attachable, View.OnClickListener {

        private final OnRecyclerItemChildClickListener mOnRecyclerItemClickListener;
        private TextView mArchiveDate;

        public FooterViewHolder(View itemView, OnRecyclerItemChildClickListener clicks) {
            super(itemView);
            mOnRecyclerItemClickListener = clicks;
            itemView.findViewById(R.id.Share).setOnClickListener(this);
            itemView.findViewById(R.id.Search).setOnClickListener(this);
            mArchiveDate = (TextView) itemView.findViewById(R.id.ArchiveDate);
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerItemClickListener != null)
                mOnRecyclerItemClickListener.onItemChildClick(v, getAdapterPosition());
        }

        @Override
        public void attachData(Object o) {
            Month month = (Month) o;
            String date = (String) mArchiveDate.getText();
            date = String.format(date, Integer.valueOf(month.year), month.month);
            mArchiveDate.setText(date);
        }
    }

    /**
     * 顶部页眉
     */
    private static class HeaderViewHolder extends RecyclerView.ViewHolder implements Attachable, View.OnClickListener {

        private OnRecyclerItemChildClickListener mListener;

        public HeaderViewHolder(View itemView, OnRecyclerItemChildClickListener listener) {
            super(itemView);
            mListener = listener;
            itemView.findViewById(R.id.ArchiveDoodles).setOnClickListener(this);
            itemView.findViewById(R.id.AdView).setOnClickListener(this);
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
            case Card.TYPE_VIEW_HEADER:
                return;
            case Card.TYPE_VIEW_FOOTER:
                ((Attachable) viewHolder).attachData(mCards[position].obj);
                return;
            case Card.TYPE_VIEW_ADVIEW:
                return;
            default:
                // TYPE_VIEW_DOODLE:
                ((Attachable) viewHolder).attachData(mCards[position].obj);
                return;

        }
    }

    @Override
    public int getItemCount() {
        return mCards == null ? 0 : mCards.length;
    }

}
