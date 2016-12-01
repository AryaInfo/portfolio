package com.arya.portfolio.view_controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragmentActivity;
import com.arya.portfolio.R;
import com.arya.portfolio.adapters.adapter_portfolio_fragment.IndustrySliderAdapter;
import com.arya.portfolio.dao.IndustryData;

import java.util.ArrayList;
import java.util.Observable;

public class IndustrySingleActivity extends AbstractFragmentActivity implements View.OnClickListener {
    public static final String TAG_SCREEN_UI = "Industry Screen";
    View inActionBarBack;
    ImageView imgBack,
            imgShareSingle;
    int position;
    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.activity_industry_single);
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
        ArrayList<IndustryData> listIndData = (ArrayList<IndustryData>) bundle.getSerializable("listIndData");
        position = bundle.getInt("position");
        inActionBarBack = findViewById(R.id.inActionBarBack);
        imgBack = (ImageView) inActionBarBack.findViewById(R.id.imgBack);
        imgShareSingle = (ImageView) inActionBarBack.findViewById(R.id.imgShareSingle);
        imgShareSingle.setVisibility(View.GONE);
        ViewPager mPager = (ViewPager) findViewById(R.id.viewPagerIndustry);
        PagerAdapter mAdapter = new IndustrySliderAdapter(listIndData, this);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(position);

        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.imgBack:
              onBackPressed();
                break;
            default:
                //do nothing.
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=this.getIntent();
        intent.putExtra("position",position);
//        intent.putExtra("position",mPager.getCurrentItem());
        setResult(Activity.RESULT_OK,intent);
        finish();
        super.onBackPressed();
    }
}
