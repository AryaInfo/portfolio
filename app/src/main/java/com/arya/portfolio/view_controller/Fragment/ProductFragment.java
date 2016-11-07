package com.arya.portfolio.view_controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;
import com.arya.portfolio.R;
import com.arya.portfolio.adapters.adapter_portfolio_fragment.PortfolioAdapter;
import com.arya.portfolio.dao.PortfolioData;
import com.arya.portfolio.utility.Utils;
import com.arya.portfolio.view_controller.PortfolioSingleActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

/**
 * Created by arya on 13/10/16.
 */
public class ProductFragment extends AbstractFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final int REQUEST_CODE_PRODUCT = 105;
    View view;
    SwipeRefreshLayout swipeRefreshProduct;
    GridView gvProduct;
    private Animation animShow, animHide;
    private ArrayList<PortfolioData> listProductData = new ArrayList<>();
    private ArrayList<PortfolioData> listProData = new ArrayList<>();
    private PortfolioAdapter productAdapter;
    ImageView imgChatWithUs;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            view = inflater.inflate(R.layout.fragment_product, container, false);
        }
        init();
        return view;
    }

    private void init() {
        swipeRefreshProduct = ((SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshProduct));
        swipeRefreshProduct.setColorSchemeResources(R.color.color_green);
        gvProduct = ((GridView) view.findViewById(R.id.gvProduct));
        imgChatWithUs = (ImageView) view.findViewById(R.id.imgChatWithUs);

        initAnimation();
        createProductData();
        setOnClickListener();
        setAdapter(listProductData);

    }

    private void setAdapter(ArrayList<PortfolioData> listProduct) {
        listProData = listProduct;
        Collections.sort(listProData, new PortfolioData());
        productAdapter = new PortfolioAdapter(getActivity(), listProData);
        gvProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
        swipeRefreshProduct.setRefreshing(false);

    }

    private void createProductData() {

        PortfolioData productData = new PortfolioData();

        productData.projectId = "ProjectAryaConnect";
        productData.projectName = "Arya Connect";
        productData.projectImage = R.mipmap.arya_connect;
        productData.projectOverview = "Connect with your friends, family and stay in touch anytime, anywhere. Share your moments via image, video audio and feel always connected, don't miss a single moment.";
        productData.projectWeblink = "http://www.aryaconnect.com/";
        productData.projectCategory = "Product";
        productData.projectPlateform = "iOS, Android, Web app";
        productData.projectTechnology = "* Native Xcode 6 for iOS\n" +
                "* Php in YII framework on thin Web Client\n" +
                "* Amazon cloud\n" +
                "* Mysql\n" +
                "* Android with Android Studio\n" +
                "* Spring\n" +
                "* MQTT\n" +
                "* Java\n" +
                "* Sqlite";
        productData.projectAchievement = "Chat , Email, SMS , Apple Push Notifications, Google Push Notifications (with latest protocols) , as a service - Chat - Mobile- Mobile - Web & Vice Versa";
        productData.projectChallenges = "No single solution for communication integration for ARYA developers - Chat, Push Notifications, SMS, Email";
        productData.projectIOSLink = "";
        productData.projectAndroidLink = "";
        listProductData.add(productData);

        PortfolioData productData1 = new PortfolioData();
        productData1.projectId = "ProjectMyInvoice";
        productData1.projectName = "MyInvoice";
        productData1.projectImage = R.mipmap.my_invoice;
        productData1.projectOverview = "Business owners feel relaxed, now enjoy e-invoice for your customers and analyze the business sales report moreover keep the record of expenditure.";
        productData1.projectWeblink = "";
        productData1.projectCategory = "Product";
        productData1.projectPlateform = "Web app";
        productData1.projectTechnology = "";
        productData1.projectAchievement = "";
        productData1.projectChallenges = "";
        productData1.projectIOSLink = "";
        productData1.projectAndroidLink = "";
        listProductData.add(productData1);

        PortfolioData productData2 = new PortfolioData();
        productData2.projectId = "ProjectAryaQuiz";
        productData2.projectName = "AryaQuiz";
        productData2.projectImage = R.mipmap.arya_quiz;
        productData2.projectOverview = "Ease your HR process and save your time. Recruit candidates online via the app, it generates random questions based on technology & experience with the immediate scoreboard  after the test.";
        productData2.projectWeblink = "";
        productData2.projectCategory = "Product";
        productData2.projectPlateform = "iOS, Android, Web app";
        productData2.projectTechnology = "";
        productData2.projectAchievement = "";
        productData2.projectChallenges = "";
        productData2.projectIOSLink = "";
        productData2.projectAndroidLink = "";
        listProductData.add(productData2);

        PortfolioData productData3 = new PortfolioData();
        productData3.projectId = "ProjectTime'nTask";
        productData3.projectName = "Time'nTask";
        productData3.projectImage = R.mipmap.time_n_task;
        productData3.projectOverview = "Time'nTask: Real-Time.Work.Money: Productivity and Efficiency Tool, is a cloud-based time tracking, location tracking, task management and chatting app that allows employees to check in and out time inside the office during the meetings, outside the office or job site with their phones or tablets.";
        productData3.projectWeblink = "www.timentask.com";
        productData3.projectCategory = "Latest";
        productData3.projectPlateform = "Web, iOS, Android app";
        productData3.projectTechnology = "* Java\n* MQTT\n* Amazon cloud\n* MySQL\n* Php\n* API Integration\n* Collaboration Component\n - Mobile to Mobile \n - Mobile to Website \n - Mobile to Desktop\n* Chat Component\n* Push Notification\n* Single Server Component";
        productData3.projectAchievement = "";
        productData3.projectChallenges = "* Technically Complicated\n* Implemented single component work on multiple platform";
        productData3.projectIOSLink = "https://itunes.apple.com/us/app/timentask/id1080540342?ls=1&mt=8";
        productData3.projectAndroidLink = "https://play.google.com/store/apps/details?id=timentask.app&hl=en";
        listProductData.add(productData3);
    }

    private void setOnClickListener() {
        gvProduct.setOnItemClickListener(this);
        swipeRefreshProduct.setOnRefreshListener(this);
        imgChatWithUs.setOnClickListener(this);

    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(Env.currentActivity, R.anim.right_in);
        animHide = AnimationUtils.loadAnimation(Env.currentActivity, R.anim.right_out);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Intent intent = new Intent(getActivity(), PortfolioSingleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("listData", listProData);
        bundle.putInt("position", i);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_PRODUCT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Env.currentActivity = getActivity();
        if (requestCode == REQUEST_CODE_PRODUCT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("position")) {
                    int selectedPosition = data.getExtras().getInt("position");
//                    gvProduct.setItemChecked(selectedPosition, true);
//                    gvProduct.smoothScrollToPosition(selectedPosition);
                    gvProduct.setSelection(selectedPosition);
                    View v = gvProduct.getChildAt(selectedPosition);
                    if (v != null) {
                        v.requestFocus();
                    }
                } else {
                   /* if (Env.currentActivity instanceof SlidingMenuActivity) {
                        ((SlidingMenuActivity) Env.currentActivity).onClick(((SlidingMenuActivity) Env.currentActivity).llChatWithUs);
                    }*/
                    Utils.openChatScreen();
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        try {
            if (swipeRefreshProduct.isRefreshing()) {
                swipeRefreshProduct.setRefreshing(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.imgChatWithUs:
                Utils.openChatScreen();
                break;
        }
    }
}
