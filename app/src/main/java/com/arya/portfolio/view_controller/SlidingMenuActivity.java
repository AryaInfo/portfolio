package com.arya.portfolio.view_controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragmentActivity;
import com.arya.portfolio.R;
import com.arya.portfolio.utility.Utils;
import com.arya.portfolio.view_controller.fragment.ChatWithUsFragment;
import com.arya.portfolio.view_controller.fragment.ConnectFragment;
import com.arya.portfolio.view_controller.fragment.ExpertiseFragment;
import com.arya.portfolio.view_controller.fragment.NewsFragment;
import com.arya.portfolio.view_controller.fragment.ProductFragment;
import com.arya.portfolio.view_controller.fragment.UseCasesFragment;

import java.util.Observable;

/**
 * Created by user on 13/09/16.
 */
public class SlidingMenuActivity extends AbstractFragmentActivity implements View.OnClickListener, SlidingPaneLayout.PanelSlideListener {
    private SlidingPaneLayout mSlidingPanel;
    private Bundle newsavedInstanceState;
    public ImageView imgMenu,
            imgSearch;
    public View inSearchBar,
            inActionBar;
    public TextView txtCancel,
            txtActionBarTitle;
    private LinearLayout llExpertise,
            llProducts,
            llNews,
            llUseCases;
    public LinearLayout llChatWithUs;
    private int selectedItemPos = 0;
    private LinearLayout llConnect;
    public AutoCompleteTextView autoTxtSearch;
    private Fragment fragment = null;
    private Animation animShow,
            animHide;
    private String actionBarTitle;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    public static final int PERMISSION_ALL = 11;
    private ImageView[] listImageViews;
    private TextView[] listTextViews;


    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.activity_slide_menu);
        newsavedInstanceState = savedInstanceState;
        init();
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {
        //Override update method.
        //do nothing
    }

    private void init() {

        mSlidingPanel = (SlidingPaneLayout) findViewById(R.id.SlidingPanel);
        //   mSlidingPanel.setSliderFadeColor(Color.TRANSPARENT);
        llNews = (LinearLayout) findViewById(R.id.llNews);
        llProducts = (LinearLayout) findViewById(R.id.llProducts);
        llExpertise = (LinearLayout) findViewById(R.id.llExpertise);
        llUseCases = (LinearLayout) findViewById(R.id.llUseCases);
        llChatWithUs = (LinearLayout) findViewById(R.id.llChatWithUs);
        llConnect = (LinearLayout) findViewById(R.id.llConnect);

        inSearchBar = findViewById(R.id.inSearchBar);
        txtCancel = (TextView) inSearchBar.findViewById(R.id.txtCancel);
        inActionBar = findViewById(R.id.inActionBar);
        imgMenu = (ImageView) inActionBar.findViewById(R.id.imgMenu);
        txtActionBarTitle = (TextView) inActionBar.findViewById(R.id.txtActionBarTitle);
        imgSearch = (ImageView) inActionBar.findViewById(R.id.imgSearch);
        autoTxtSearch = (AutoCompleteTextView) findViewById(R.id.autoTxtSearch);
        listImageViews = new ImageView[6];
        listImageViews[0] = (ImageView) findViewById(R.id.imvNews);
        listImageViews[1] = (ImageView) findViewById(R.id.imvProducts);
        listImageViews[2] = (ImageView) findViewById(R.id.imvExpertise);
        listImageViews[3] = (ImageView) findViewById(R.id.imvUseCases);
        listImageViews[4] = (ImageView) findViewById(R.id.imvChat);
        listImageViews[5] = (ImageView) findViewById(R.id.imvConnect);
        listTextViews = new TextView[6];
        listTextViews[0] = (TextView) findViewById(R.id.txtNews);
        listTextViews[1] = (TextView) findViewById(R.id.txtProducts);
        listTextViews[2] = (TextView) findViewById(R.id.txtExpertise);
        listTextViews[3] = (TextView) findViewById(R.id.txtUseCases);
        listTextViews[4] = (TextView) findViewById(R.id.txtChat);
        listTextViews[5] = (TextView) findViewById(R.id.txtConnect);
        setClickOnView();
        setEnabledViews(false);
        initAnimation();
        openFragment();
    }


    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void openFragment() {
        try {
            if (newsavedInstanceState == null) {
                displayView(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(Env.currentActivity, R.anim.right_in);
        animHide = AnimationUtils.loadAnimation(Env.currentActivity, R.anim.right_out);
    }

    private void setClickOnView() {
        llNews.setOnClickListener(this);
        llProducts.setOnClickListener(this);
        llExpertise.setOnClickListener(this);
        llUseCases.setOnClickListener(this);
        llChatWithUs.setOnClickListener(this);
        llConnect.setOnClickListener(this);
        imgMenu.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        mSlidingPanel.setPanelSlideListener(this);
        mSlidingPanel.setParallaxDistance(200);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.imgMenu:
                menuOpenClose();
                break;
            case R.id.imgSearch:
                inSearchBar.setVisibility(View.VISIBLE);
                inSearchBar.startAnimation(animShow);
                autoTxtSearch.requestFocus();
                Utils.showKeyboard(Env.currentActivity, autoTxtSearch);
                break;
            case R.id.txtCancel:
                autoTxtSearch.getText().clear();
                inSearchBar.startAnimation(animHide);
                inSearchBar.setVisibility(View.GONE);
                break;
            case R.id.llNews:
                displayView(0);
                break;
            case R.id.llProducts:
                displayView(1);
                break;
            case R.id.llExpertise:
                displayView(2);
                break;
            case R.id.llUseCases:
                displayView(3);
                break;
            case R.id.llChatWithUs:
                displayView(4);
                break;
            case R.id.llConnect:
                displayView(5);
                break;
            default:
                //do nothing
                break;
        }
    }

    private void displayView(int position) {
        listImageViews[selectedItemPos].clearColorFilter();
        listTextViews[selectedItemPos].setTextColor(ContextCompat.getColor(this, R.color.color_black));
        listImageViews[position].setColorFilter(ContextCompat.getColor(this, R.color.color_green));
        listTextViews[position].setTextColor(ContextCompat.getColor(this, R.color.color_green));
        selectedItemPos = position;

        switch (position) {
            case 0:
                fragment = new NewsFragment();
                imgSearch.setVisibility(View.VISIBLE);
                autoTxtSearch.addTextChangedListener((TextWatcher) fragment);
                actionBarTitle = getResources().getString(R.string.titleNews);
                break;
            case 1:
                fragment = new ProductFragment();
                imgSearch.setVisibility(View.GONE);
                actionBarTitle = getResources().getString(R.string.titleProduct);
                break;
            case 2:
                fragment = new ExpertiseFragment();
                imgSearch.setVisibility(View.GONE);
                actionBarTitle = getResources().getString(R.string.titleExpertise);
                break;
            case 3:
                fragment = new UseCasesFragment();
                imgSearch.setVisibility(View.VISIBLE);
                autoTxtSearch.addTextChangedListener(((TextWatcher) fragment));
                actionBarTitle = getResources().getString(R.string.titleUseCases);
                break;
            case 4:
                fragment = new ChatWithUsFragment();
                imgSearch.setVisibility(View.GONE);
                actionBarTitle = getResources().getString(R.string.titleChat);
                break;
            case 5:
                fragment = new ConnectFragment();
                imgSearch.setVisibility(View.GONE);
                actionBarTitle = getResources().getString(R.string.titleConnect);
                break;
            default:
                //do nothing
                break;
        }
        replaceFragment(fragment, actionBarTitle);
    }


    private void menuOpenClose() {
        if (mSlidingPanel.isOpen()) {
            mSlidingPanel.closePane();
        } else {
            mSlidingPanel.openPane();
        }
    }

    public void replaceFragment(Fragment fragment, String actionBarTitle) {
        txtActionBarTitle.setText(actionBarTitle);
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.commit();
            mSlidingPanel.closePane();
        } else {
            Log.e("AryaPortfolio", "Error in creating fragment");
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        //do nothing
    }

    @Override
    public void onPanelOpened(View panel) {
        Utils.hideKeyBoardMethod(Env.appContext, panel);
        if (inSearchBar.getVisibility() == View.VISIBLE) {
            inSearchBar.setVisibility(View.GONE);
        }
        setEnabledViews(true);
    }

    @Override
    public void onPanelClosed(View panel) {
        setEnabledViews(false);
    }

    private void setEnabledViews(boolean isEnabled) {
        llNews.setEnabled(isEnabled);
        llProducts.setEnabled(isEnabled);
        llExpertise.setEnabled(isEnabled);
        llChatWithUs.setEnabled(isEnabled);
        llUseCases.setEnabled(isEnabled);
        llConnect.setEnabled(isEnabled);
    }
}