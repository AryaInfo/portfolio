package com.arya.portfolio.view_controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.utils.Util;
import com.arya.lib.view.AbstractFragmentActivity;
import com.arya.portfolio.R;
import com.arya.portfolio.adapters.adapter_portfolio_fragment.PortfolioAdapter;
import com.arya.portfolio.dao.PortfolioData;
import com.arya.portfolio.utility.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

/**
 * Created by user on 28/10/16.
 */

public class PortfolioList extends AbstractFragmentActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    View inActionBarTilte;
    private ArrayList<PortfolioData> listData;
    private PortfolioAdapter portfolioAdapter;
    int position;
    private GridView gvPortfolio;
    private SwipeRefreshLayout swipeRefreshPortfolio;
    ImageView imgBack, imgBookMarkStar, imgShare;
    TextView txtActionBarTitle;
    String title;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.activity_portfolio_list);
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
        title = bundle.getString("title");
        swipeRefreshPortfolio = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshPortfolio);
        swipeRefreshPortfolio.setColorSchemeResources(R.color.color_green);
        gvPortfolio = ((GridView) findViewById(R.id.gvPortfolio));
        inActionBarTilte = findViewById(R.id.inActionBarTilte);
        imgBack = (ImageView) inActionBarTilte.findViewById(R.id.imgBack);
        txtActionBarTitle = (TextView) inActionBarTilte.findViewById(R.id.txtActionBarTitle);
        imgBookMarkStar = (ImageView) inActionBarTilte.findViewById(R.id.imgBookMarkStar);
        imgShare = (ImageView) inActionBarTilte.findViewById(R.id.imgShare);
        imgBookMarkStar.setVisibility(View.GONE);
        imgShare.setVisibility(View.GONE);

        setAdapter(listData);
        txtActionBarTitle.setText(title);
        imgBack.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        gvPortfolio.setOnItemClickListener(this);
        swipeRefreshPortfolio.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.imgBack:
                onBackPressed();
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
            Collections.sort(listData, new PortfolioData());
            portfolioAdapter = new PortfolioAdapter(this, listData);
            gvPortfolio.setAdapter(portfolioAdapter);
            portfolioAdapter.notifyDataSetChanged();
            swipeRefreshPortfolio.setRefreshing(false);
        } else {
            Util.showCenteredToast(Env.appContext, getResources().getString(R.string.nodetails));
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
//        intent.putExtra("position", mPager.getCurrentItem());
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        try {
            if (swipeRefreshPortfolio.isRefreshing()) {
                swipeRefreshPortfolio.setRefreshing(false);
            }
            // hideSearchBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(PortfolioList.this, PortfolioSingleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("listData", listData);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
