package org.lee.android.doodles.fragment;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.lee.android.doodles.ApiClient;
import org.lee.android.doodles.AppApplication;
import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.WebViewActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.VolleyLoader;
import org.lee.android.util.Toast;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements
        DoodleRecyclerAdapter.Attachable, View.OnClickListener, Toolbar.OnMenuItemClickListener {

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

    public RecyclerItemViewHolder(final View convertView, OnRecyclerItemChildClickListener clicks) {
        super(convertView);
        imageView = (NetworkImageView) convertView.findViewById(R.id.ImageView);
        titleText = (TextView) convertView.findViewById(R.id.Title);
        dateText = (TextView) convertView.findViewById(R.id.Date);
        hoverText = (TextView) convertView.findViewById(R.id.HoverText);
        imageView.setDefaultImageResId(R.drawable.GRAY_DARK);
        imageView.setErrorImageResId(R.drawable.ic_google_birthday);
        this.mOnRecyclerItemClickListener = clicks;
        convertView.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) convertView.findViewById(R.id.ToolbarMenu);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.doodles_list_menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.Favorite:
                return true;
            case R.id.Search:
                String hover = hoverText.getText().toString();
                String q = Uri.encode(hover);
                WebViewActivity.launch(AppContext.getContext(), ApiClient.GOOGLE_SEARCH + q, hover);
                return true;
            case R.id.Share:
                Toast.show("" + menuItem.getTitle());
                AppApplication.share(AppContext.getContext());
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerItemClickListener != null)
            mOnRecyclerItemClickListener.onItemClick(v, getAdapterPosition());
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