package com.arya.portfolio.view_controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.utils.Util;
import com.arya.lib.view.AbstractFragmentActivity;
import com.arya.portfolio.R;
import com.arya.portfolio.adapters.adapter_portfolio_fragment.PortfolioSliderAdapter;
import com.arya.portfolio.dao.PortfolioData;
import com.arya.portfolio.utility.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

/**
 * Created by user on 15/09/16.
 */
public class PortfolioSingleActivity extends AbstractFragmentActivity implements View.OnClickListener {

    View inActionBarBack;
    ImageView imgBack, imgShareSingle;
    private ArrayList<PortfolioData> listData;
    public ViewPager mPager;
    private PagerAdapter mAdapter;
    int position;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.activity_portfolio_single);
        init();
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    private void init() {

        Bundle bundle = getIntent().getExtras();
        listData = ((ArrayList<PortfolioData>) bundle.getSerializable("listData"));
        position = bundle.getInt("position");
        inActionBarBack = findViewById(R.id.inActionBarBack);
        imgBack = (ImageView) inActionBarBack.findViewById(R.id.imgBack);
        imgShareSingle = (ImageView) inActionBarBack.findViewById(R.id.imgShareSingle);
        mPager = ((ViewPager) findViewById(R.id.viewPagerPortfolio));
        setAdapter(listData);
        mPager.setCurrentItem(position);
        imgBack.setOnClickListener(this);
        imgShareSingle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgShareSingle:
                shareProduct();
                break;
            case R.id.imgChatWithUs:
                Utils.openChatScreen();
                /*Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();*/
                break;
        }
    }

    private void setAdapter(ArrayList<PortfolioData> listData) {
        if (!listData.isEmpty()) {
            Collections.sort(listData, new PortfolioData());
            mAdapter = new PortfolioSliderAdapter(this.listData, this, PortfolioSingleActivity.this);
            mPager.setAdapter(mAdapter);
        } else {
            Util.showCenteredToast(Env.appContext, getResources().getString(R.string.nodetails));
        }
    }

    private void shareProduct() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = getResources().getString(R.string.productTitle) + listData.get(mPager.getCurrentItem()).projectName + "\n" + getResources().getString(R.string.productOverview) + listData.get(mPager.getCurrentItem()).projectOverview;
        if (!TextUtils.isEmpty(listData.get(mPager.getCurrentItem()).projectWeblink)) {
            shareBody = shareBody.concat("\n" + getResources().getString(R.string.productWebLink) + listData.get(mPager.getCurrentItem()).projectWeblink);
        } else if (!TextUtils.isEmpty(listData.get(mPager.getCurrentItem()).projectAndroidLink)) {
            shareBody = shareBody.concat("\n" + getResources().getString(R.string.productAndroidLink) + listData.get(mPager.getCurrentItem()).projectAndroidLink);
        } else if (!TextUtils.isEmpty(listData.get(mPager.getCurrentItem()).projectIOSLink)) {
            shareBody = shareBody.concat("\n" + getResources().getString(R.string.productiosLink) + listData.get(mPager.getCurrentItem()).projectIOSLink);
        }
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.shareSub) + "(" + listData.get(mPager.getCurrentItem()).projectName + ")");
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.shareVia)));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
//        intent.putExtra("position", mPager.getCurrentItem());
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}
