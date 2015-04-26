package org.lee.android.doodles.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.VolleyLoader;
import org.lee.android.util.Toast;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements
        DoodleRecyclerAdapter.Attachable, View.OnClickListener {

    public interface OnRecyclerItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnRecyclerItemChildClickListener extends OnRecyclerItemClickListener {
        void onItemChildClick(View clickView, int position);
    }


    private ImageLoader mImageLoader = VolleyLoader.getInstance().getImageLoader();
    private final NetworkImageView imageView;
    private final TextView titleText;
    private final TextView dateText;
    private final TextView hoverText;
    private final OnRecyclerItemChildClickListener mOnRecyclerItemClickListener;

    public RecyclerItemViewHolder(final View convertView, OnRecyclerItemChildClickListener clicks,
                                  Toolbar.OnMenuItemClickListener menuItemClickListener) {
        super(convertView);
        imageView = (NetworkImageView) convertView.findViewById(R.id.ImageView);
        titleText = (TextView) convertView.findViewById(R.id.Title);
        dateText = (TextView) convertView.findViewById(R.id.Date);
        hoverText = (TextView) convertView.findViewById(R.id.HoverText);
        imageView.setDefaultImageResId(R.drawable.GRAY_DARK);
        imageView.setErrorImageResId(R.drawable.ic_google_birthday);
        this.mOnRecyclerItemClickListener = clicks;

        convertView.setOnClickListener(this);
        convertView.findViewById(R.id.Search).setOnClickListener(this);

        Toolbar toolbar = (Toolbar) convertView.findViewById(R.id.ToolbarMenu);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        toolbar.inflateMenu(R.menu.doodles_list_menu);
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerItemClickListener != null)
            switch (v.getId()) {
                case R.id.Search:
                    mOnRecyclerItemClickListener.onItemChildClick(v, getAdapterPosition());
                    return;
                default:
                    mOnRecyclerItemClickListener.onItemClick(v, getAdapterPosition());
                    return;
            }
    }

    @Override
    public void attachData(Object o) {
        Doodle doodle = (Doodle) o;
        dateText.setText(doodle.getDate());
        imageView.setImageUrl(doodle.hires_url, mImageLoader);
        titleText.setText(doodle.title);
        hoverText.setText("#" + doodle.query + "#");
    }
}