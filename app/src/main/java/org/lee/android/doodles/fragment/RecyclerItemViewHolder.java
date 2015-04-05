package org.lee.android.doodles.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.VolleyLoader;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static interface ViewHolderClicks {
        public void onItemClick(View parent, int position);

        public void onSearch(TextView searchView);
    }

    private final NetworkImageView imageView;
    private final TextView titleText;
    private final TextView dateText;
    private final TextView hoverText;
    private final ViewHolderClicks mViewHolderClicks;

    public RecyclerItemViewHolder(final View parent,
                                  NetworkImageView imageView,
                                  TextView titleText,
                                  TextView dateText,
                                  TextView hoverText, ViewHolderClicks clicks) {
        super(parent);
        this.imageView = imageView;
        this.titleText = titleText;
        this.dateText = dateText;
        this.hoverText = hoverText;
        this.mViewHolderClicks = clicks;

        parent.setOnClickListener(this);
        parent.findViewById(R.id.Search).setOnClickListener(this);
    }

    public static RecyclerItemViewHolder newInstance(View convertView, ViewHolderClicks clicks) {
        NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.ImageView);
        TextView titleText = (TextView) convertView.findViewById(R.id.Title);
        TextView dateText = (TextView) convertView.findViewById(R.id.Date);
        TextView hoverText = (TextView) convertView.findViewById(R.id.HoverText);


        imageView.setDefaultImageResId(R.drawable.ic_doodle_error);
        imageView.setErrorImageResId(R.drawable.ic_google_birthday);
        return new RecyclerItemViewHolder(convertView, imageView, titleText, dateText, hoverText, clicks);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Search:
                mViewHolderClicks.onSearch((TextView) v);
                return;
            default:
                mViewHolderClicks.onItemClick(v, getAdapterPosition());
                return;
        }
    }

    public void attachData(Doodle doodle) {
        dateText.setText(doodle.getDate());
        imageView.setImageUrl(doodle.hires_url, VolleyLoader.getInstance().getImageLoader());
        titleText.setText(doodle.title);
        hoverText.setText("#" + doodle.query + "#");
//        hoverText.setText(doodle.getTranslations().getItem(0).hover_text);
    }
}