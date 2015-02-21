package org.lee.android.doodles.fragment;

import android.content.Context;
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

    public DoodleAdapter(Context context, int resource, Doodle[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.fragment_main_list_item, null);
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

        ViewHolder(View convertView) {
            imageView = (NetworkImageView) convertView.findViewById(R.id.ImageView);
            titleText = (TextView) convertView.findViewById(R.id.Title);
            dateText = (TextView) convertView.findViewById(R.id.Date);

            imageView.setDefaultImageResId(R.drawable.logo11w);
            imageView.setErrorImageResId(R.drawable.unnamed);
        }

        private void attachData(Doodle doodle) {
            imageView.setImageUrl(doodle.alternate_url, VolleyLoader.getInstance().getImageLoader());
            titleText.setText(doodle.title);
            dateText.setText(doodle.getDate());
        }
    }
}
