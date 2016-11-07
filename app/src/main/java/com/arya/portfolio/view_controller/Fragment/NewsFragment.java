package com.arya.portfolio.view_controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.utils.Util;
import com.arya.lib.view.AbstractFragment;
import com.arya.portfolio.R;
import com.arya.portfolio.adapters.NewsFragmentAdapter;
import com.arya.portfolio.dao.NewsData;
import com.arya.portfolio.model.NewsModel;
import com.arya.portfolio.utility.Utils;
import com.arya.portfolio.view_controller.NewsSingleActivity;
import com.arya.portfolio.view_controller.SlidingMenuActivity;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by user on 14/09/16.
 */
public class NewsFragment extends AbstractFragment implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, SwipeRefreshLayout.OnRefreshListener {

    View view;
    TextView txtTechnologyNews, txtAryaNews, txtFavNews, txtLastSelectedView;
    GridView gvNews;
    NewsModel newsModel = new NewsModel();
    private String searchString;
    private NewsFragmentAdapter newsFragmentAdapter;
    private SwipeRefreshLayout swipeRefreshNews;
    private ArrayList<NewsData> listTechNews;
    private ArrayList<NewsData> listAdapter = new ArrayList<>();
    private static final int REQUEST_CODE_NEWS = 102;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, container, false);
        }
        init();
        return view;
    }

    private void init() {
        swipeRefreshNews = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshNews);
        swipeRefreshNews.setColorSchemeResources(R.color.color_green);
        gvNews = (GridView) view.findViewById(R.id.gvNews);
        txtTechnologyNews = (TextView) view.findViewById(R.id.txtTechnologyNews);
        txtAryaNews = (TextView) view.findViewById(R.id.txtAryaNews);
        txtFavNews = (TextView) view.findViewById(R.id.txtFavNews);
        setClickOnView();
        onClick(txtTechnologyNews);
    }

    @Override
    protected BasicModel getModel() {
        return newsModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            if (data instanceof ArrayList) {
                ArrayList<NewsData> listFilter = ((ArrayList<NewsData>) data);
                setAdapter(listFilter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setClickOnView() {
        txtTechnologyNews.setOnClickListener(this);
        txtAryaNews.setOnClickListener(this);
        txtFavNews.setOnClickListener(this);
        gvNews.setOnItemClickListener(this);
        swipeRefreshNews.setOnRefreshListener(this);
    }

    private void setAdapter(ArrayList<NewsData> arrayList) {
        listAdapter = arrayList;
        if (!listAdapter.isEmpty()) {
            newsFragmentAdapter = new NewsFragmentAdapter(getActivity(), listAdapter);
            gvNews.setAdapter(newsFragmentAdapter);
        } else {
            gvNews.setAdapter(null);
            Util.showCenteredToast(Env.appContext, getResources().getString(R.string.no_news));
        }
        newsFragmentAdapter.notifyDataSetChanged();
        swipeRefreshNews.setRefreshing(false);
    }


    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.txtTechnologyNews:
                hideSearchBar();
                setSelectedView(txtTechnologyNews);
                newsModel.setGetRecentNewsTask(getResources().getString(R.string.technology_used), "", "");
                break;
            case R.id.txtAryaNews:
                hideSearchBar();
                setSelectedView(txtAryaNews);
                newsModel.setGetRecentNewsTask("ARYANEWS", "", "");
                break;
            case R.id.txtFavNews:
                hideSearchBar();
                setSelectedView(txtFavNews);
                newsModel.setGetRecentNewsTask("", "favNews", "");
                break;
            default:
                //do nothing.
                break;

        }
    }

    private void setSelectedView(TextView selectedView) {
        Utils.showProDialog("");
        if (txtLastSelectedView != null) {
            txtLastSelectedView.setSelected(false);
        }
        selectedView.setSelected(true);
        txtLastSelectedView = selectedView;
        gvNews.setAdapter(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), NewsSingleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("listNews", listAdapter);
        bundle.putInt("position", i);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, REQUEST_CODE_NEWS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Env.currentActivity = getActivity();
            if (requestCode == REQUEST_CODE_NEWS && resultCode == Activity.RESULT_OK) {
               /* if (data.hasExtra(NewsData.TABLE_NAME)) {
                    ArrayList<NewsData> listNewsWithFav = (ArrayList<NewsData>) data.getExtras().getSerializable(NewsData.TABLE_NAME);
                    setDataToAllList(listNewsWithFav);
                }*/
                if (data.hasExtra("position")) {
                    int selectedPosition = data.getExtras().getInt("position");
                    //  listAllNews = DBManagerAP.getInstance().getNewsData();
                    //  setDataToAllList(listAllNews);
                    setNews();
                    //  gvNews.setItemChecked(selectedPosition, true);
                    //  gvNews.smoothScrollToPosition(selectedPosition);
                    gvNews.setSelection(selectedPosition);
                    View v = gvNews.getChildAt(selectedPosition);
                    if (v != null) {
                        v.requestFocus();
                    }
                } else {
                    /*if (Env.currentActivity instanceof SlidingMenuActivity) {
                        ((SlidingMenuActivity) Env.currentActivity).onClick(((SlidingMenuActivity) Env.currentActivity).llChatWithUs);
                    }*/
                    Utils.openChatScreen();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //do nothing.
        //overrided method.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //do nothing.
        //overrided method.
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchString = s.toString().trim();
        if (!TextUtils.isEmpty(searchString)) {
            searchByTitle(searchString);
        } else {
            searchString = null;
            searchByTitle(searchString);
        }
    }

    private void searchByTitle(String searchString) {
        if (txtTechnologyNews.isSelected()) {
            newsModel.setGetRecentNewsTask(getResources().getString(R.string.technology_used), "", searchString);
        } else if (txtAryaNews.isSelected()) {
            newsModel.setGetRecentNewsTask("ARYANEWS", "", searchString);
        } else if (txtFavNews.isSelected()) {
            newsModel.setGetRecentNewsTask("", "favNews", searchString);
        }
    }

    private void setNews() {
        if (txtTechnologyNews.isSelected()) {
            newsModel.setGetRecentNewsTask(getResources().getString(R.string.technology_used), "", "");
        } else if (txtAryaNews.isSelected()) {
            newsModel.setGetRecentNewsTask("ARYANEWS", "", "");
        } else {
            newsModel.setGetRecentNewsTask("", "favNews", "");
        }
    }

    @Override
    public void onRefresh() {
        try {
            if (!Util.isDeviceOnline()) {
                if (swipeRefreshNews.isRefreshing()) {
                    swipeRefreshNews.setRefreshing(false);
                }
                Util.showCenteredToast(Env.appContext, Env.appContext.getResources().getString(R.string.no_network));
                return;
            }
            hideSearchBar();
            if (txtTechnologyNews.isSelected()) {
                newsModel.refreshNews(getResources().getString(R.string.technology_used), "", "");
            } else if (txtAryaNews.isSelected()) {
                newsModel.refreshNews("ARYANEWS", "", "");
            } else {
                newsModel.refreshNews("", "favNews", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideSearchBar() {
        if (Env.currentActivity instanceof SlidingMenuActivity && ((SlidingMenuActivity) Env.currentActivity).inSearchBar.getVisibility() == View.VISIBLE) {
            ((SlidingMenuActivity) Env.currentActivity).onClick(((SlidingMenuActivity) Env.currentActivity).txtCancel);
        }
    }
}
