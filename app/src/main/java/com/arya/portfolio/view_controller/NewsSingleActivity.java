package com.arya.portfolio.view_controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragmentActivity;
import com.arya.portfolio.R;
import com.arya.portfolio.adapters.NewsSliderAdapter;
import com.arya.portfolio.dao.NewsData;
import com.arya.portfolio.database.DBManagerAP;
import com.arya.portfolio.utility.Utils;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by user on 15/09/16.
 */
public class NewsSingleActivity extends AbstractFragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    View inActionBarTilte;
    ImageView imgBack,
            imgBookMarkStar,
            imgShare;
    TextView txtActionBarTitle;
    ArrayList<NewsData> listNewsData;
    private ViewPager viewPagerNews;
    PagerAdapter newsPagerAdapter;
    int position;
    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.activity_news_single);
        init();
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {
        //do nothing.
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        listNewsData = (ArrayList<NewsData>) bundle.getSerializable("listNews");
        inActionBarTilte = findViewById(R.id.inActionBarTilte);
        imgBack = (ImageView) inActionBarTilte.findViewById(R.id.imgBack);
        txtActionBarTitle = (TextView) inActionBarTilte.findViewById(R.id.txtActionBarTitle);
        imgBookMarkStar = (ImageView) inActionBarTilte.findViewById(R.id.imgBookMarkStar);
        imgShare = (ImageView) inActionBarTilte.findViewById(R.id.imgShare);
        viewPagerNews = (ViewPager) findViewById(R.id.viewPagerNews);
        setAdapter();
        viewPagerNews.setCurrentItem(position);
        setClickOnView();
        setValue(listNewsData);
    }

    private void setValue(ArrayList<NewsData> listNewsData) {
        txtActionBarTitle.setText(listNewsData.get(0).newsType);
    }

    private void setAdapter() {
        newsPagerAdapter = new NewsSliderAdapter(this, listNewsData);
        viewPagerNews.setAdapter(newsPagerAdapter);
        newsPagerAdapter.notifyDataSetChanged();
    }


    private void setClickOnView() {
        imgBack.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgBookMarkStar.setOnClickListener(this);
        viewPagerNews.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgShare:
                shareNews();
                break;
            case R.id.imgBookMarkStar:
                setFavrouite();
                break;
            case R.id.imgChatWithUs:
                Utils.openChatScreen();
               /* Intent intent = new Intent();
             //   intent.putExtra(NewsData.TABLE_NAME, listNewsData);
                setResult(Activity.RESULT_OK, intent);
                finish();*/
                break;
            default:
                //do nothing.
                break;
        }
    }

    private void setFavrouite() {
        DBManagerAP.getInstance().updateTableNews(listNewsData.get(viewPagerNews.getCurrentItem()).articleid, imgBookMarkStar.isSelected() ? 0 : 1);
        imgBookMarkStar.setImageResource(imgBookMarkStar.isSelected() ? R.mipmap.ic_bookmark_star : R.mipmap.ic_bookmark_star_active);
        imgBookMarkStar.setSelected(!imgBookMarkStar.isSelected());
        listNewsData.get(viewPagerNews.getCurrentItem()).favrouite = imgBookMarkStar.isSelected() ? 1 : 0;
        int position = viewPagerNews.getCurrentItem();
        setAdapter();
        viewPagerNews.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
         intent.putExtra("position",position );

        // intent.putExtra("position", viewPagerNews.getCurrentItem());
       // intent.putExtra(NewsData.TABLE_NAME, listNewsData);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private void shareNews() {
        Intent intentShare = new Intent(android.content.Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        String shareBody = Html.fromHtml(listNewsData.get(viewPagerNews.getCurrentItem()).title).toString().trim() + "\n" + listNewsData.get(viewPagerNews.getCurrentItem()).link;
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.shareSub));
        intentShare.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intentShare, getResources().getString(R.string.shareVia)));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        imgBookMarkStar.setImageResource(listNewsData.get(viewPagerNews.getCurrentItem()).favrouite == 1 ? R.mipmap.ic_bookmark_star_active : R.mipmap.ic_bookmark_star);
        imgBookMarkStar.setSelected(listNewsData.get(viewPagerNews.getCurrentItem()).favrouite == 1 ? true : false);
    }

    @Override
    public void onPageSelected(int position) {
        //do nothing.
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //do nothing.
    }
}
