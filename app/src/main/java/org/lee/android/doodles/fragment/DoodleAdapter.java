package org.lee.android.doodles.fragment;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.VolleyLoader;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-2-22.
 */
public class DoodleAdapter extends ArrayAdapter {

    public DoodleAdapter(Context context, Doodle[] objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.fragment_doodles_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Doodle doodle = (Doodle) getItem(position);
        holder = (ViewHolder) convertView.getTag();
        holder.attachData(doodle);

        return convertView;
    }

    class ViewHolder {

        private NetworkImageView imageView;
        private TextView titleText;
        private TextView dateText;
        private TextView hoverText;

        ViewHolder(View convertView) {
            imageView = (NetworkImageView) convertView.findViewById(R.id.ImageView);
            titleText = (TextView) convertView.findViewById(R.id.Title);
            dateText = (TextView) convertView.findViewById(R.id.Date);
            hoverText = (TextView) convertView.findViewById(R.id.HoverText);

            imageView.setDefaultImageResId(R.drawable.ic_doodle_error);
            imageView.setErrorImageResId(R.drawable.ic_google_birthday);

        }

        private void attachData(Doodle doodle) {
            imageView.setImageUrl(doodle.hires_url, VolleyLoader.getInstance().getImageLoader());
            titleText.setText(doodle.title);
            dateText.setText(doodle.getDate());
//            hoverText.setText(doodle.getTranslations().getItem(0).hover_text);
        }
    }
}
