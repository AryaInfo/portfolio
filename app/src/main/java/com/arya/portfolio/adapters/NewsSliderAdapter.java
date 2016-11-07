package com.arya.portfolio.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.utils.Util;
import com.arya.portfolio.R;
import com.arya.portfolio.custom.JustifiedTextView;
import com.arya.portfolio.dao.NewsData;
import com.arya.portfolio.utility.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 06/10/16.
 */

public class NewsSliderAdapter extends PagerAdapter {
    private final Context context;
    ArrayList<NewsData> listNews;

    public NewsSliderAdapter(Context context, ArrayList<NewsData> listNews) {
        this.context = context;
        this.listNews = listNews;
    }

    @Override
    public int getCount() {
        return listNews.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        TextView txtNewsPubDate;
        JustifiedTextView txtNewsDescription, txtNewsTitle;
        ImageView imgNews, imgSupportEmail, imgChatWithUs;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.viewpager_news_slide, container, false);
        txtNewsPubDate = (TextView) view.findViewById(R.id.txtNewsPubDate);
        txtNewsTitle = (JustifiedTextView) view.findViewById(R.id.txtNewsTitle);
        txtNewsDescription = (JustifiedTextView) view.findViewById(R.id.txtNewsDescription);
        imgNews = (ImageView) view.findViewById(R.id.imgNews);
        imgSupportEmail = (ImageView) view.findViewById(R.id.imgSupportEmail);
        imgChatWithUs = (ImageView) view.findViewById(R.id.imgChatWithUs);
        NewsData newsData = listNews.get(position);

        //Set value
        Picasso.with(Env.currentActivity)
                .load(newsData.imagelink)
                .into(imgNews);
        txtNewsPubDate.setText(Utils.parsePubDateFormate(newsData.pubdate));
        txtNewsTitle.setText(newsData.title);
        txtNewsTitle.setTypeFace(Typeface.DEFAULT_BOLD);
        txtNewsTitle.setTextSize(context.getResources().getDimension(R.dimen.font_16));
        txtNewsTitle.setLineSpacing(10);
        txtNewsTitle.setAlignment(Paint.Align.LEFT);
        txtNewsDescription.setTextSize(context.getResources().getDimension(R.dimen.font_14));
        txtNewsDescription.setLineSpacing(10);
        txtNewsDescription.setAlignment(Paint.Align.LEFT);
        txtNewsDescription.setText(newsData.description);


        imgSupportEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    String shareBody = listNews.get(position).title + "\n" + listNews.get(position).link;
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.emailaddresssupport)});
                    i.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.shareSub) + "(" + listNews.get(position).title + ")");
                    i.putExtra(Intent.EXTRA_TEXT, shareBody);
                    context.startActivity(Intent.createChooser(i, context.getResources().getString(R.string.shareVia)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Util.showCenteredToast(Env.appContext, "There are no email clients installed.");
                }
            }
        });
        imgChatWithUs.setTag(position);
        imgChatWithUs.setOnClickListener((View.OnClickListener) context);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        container.getChildCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

}
