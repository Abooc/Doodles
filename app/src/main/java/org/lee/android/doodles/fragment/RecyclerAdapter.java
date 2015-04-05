package org.lee.android.doodles.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerItemViewHolder> {

    private Context mContext;
    private Doodle[] mDoodles;
    private RecyclerItemViewHolder.ViewHolderClicks mViewClicks;

    public RecyclerAdapter(Context context, Doodle[] doodles, RecyclerItemViewHolder.ViewHolderClicks viewClicks) {
        mContext = context;
        mDoodles = doodles;
        mViewClicks = viewClicks;
    }

    @Override
    public RecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_doodles_list_item, parent, false);
        return RecyclerItemViewHolder.newInstance(view, mViewClicks);
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder viewHolder, int position) {
        Doodle doodle = mDoodles[position];
        viewHolder.attachData(doodle);
    }

    @Override
    public int getItemCount() {
        return mDoodles == null ? 0 : mDoodles.length;
    }

}
