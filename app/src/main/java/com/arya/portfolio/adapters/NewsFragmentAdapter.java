package com.arya.portfolio.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.portfolio.R;
import com.arya.portfolio.custom.JustifyWithMaxLines;
import com.arya.portfolio.dao.NewsData;
import com.arya.portfolio.utility.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 14/09/16.
 */
public class NewsFragmentAdapter extends BaseAdapter {
    Context context;
    List<NewsData> listNews;
    public Resources res;

    public NewsFragmentAdapter(Context context, List<NewsData> listNews) {
        this.context = context;
        this.listNews = listNews;
    }

    public int getCount() {
        return listNews.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_news_list_items, null);
            holder = new ViewHolder();
            holder.txtNewsPubDate = (TextView) convertView.findViewById(R.id.txtNewsPubDate);
            holder.txtNewsTitle = (TextView) convertView.findViewById(R.id.txtNewsTitle);
            holder.txtNewsDescription = (JustifyWithMaxLines) convertView.findViewById(R.id.txtNewsDescription);
            holder.imgNews = (ImageView) convertView.findViewById(R.id.imgNews);
            holder.imgFav = (ImageView) convertView.findViewById(R.id.imgFav);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            NewsData newsData = listNews.get(position);

            holder.txtNewsDescription.setTextSize(context.getResources().getDimension(R.dimen.font_14));
            holder.txtNewsDescription.setLineSpacing(10);
            holder.txtNewsDescription.setAlignment(Paint.Align.LEFT);
            holder.txtNewsDescription.setMaxLines(4);
            Picasso.with(Env.currentActivity)
                    .load(newsData.imagelink).placeholder(R.mipmap.ic_news_black)
                    .into(holder.imgNews);
            holder.txtNewsPubDate.setText(Utils.parsePubDateFormate(newsData.pubdate));
            holder.txtNewsTitle.setText(newsData.title);
            holder.txtNewsDescription.setText(newsData.description);
            if (newsData.favrouite == 1) {
                holder.imgFav.setVisibility(View.VISIBLE);
            } else {
                holder.imgFav.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView txtNewsPubDate;
        public TextView txtNewsTitle;
        public JustifyWithMaxLines txtNewsDescription;
        public ImageView imgNews, imgFav;
    }

}
