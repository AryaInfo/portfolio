package com.arya.portfolio.adapters.adapter_portfolio_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.utils.Util;
import com.arya.portfolio.R;
import com.arya.portfolio.custom.JustifiedTextView;
import com.arya.portfolio.dao.PortfolioData;
import com.arya.portfolio.view_controller.PortfolioSingleActivity;

import java.util.ArrayList;

public class PortfolioSliderAdapter extends PagerAdapter {
    private ArrayList<PortfolioData> listData;
    private Context mContext;

    public PortfolioSliderAdapter(ArrayList<PortfolioData> listData, Context ctx, PortfolioSingleActivity portfolioSingleActivity) {
        this.listData = listData;
        this.mContext = ctx;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imgProductPicS;
        final TextView txtTechnologyUsed, txtAchievement, txtChallenges, txtProductNameS, txtWebLinkS, txtPlatformS;
        ImageView imgAndroid, imgIOS, imgSupportEmail, imgChatWithUs;
        final JustifiedTextView txtOverviewS, txtSelectedTab;
        final LinearLayout llSelectedTab;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.viewpager_portfolio_slide, container, false);

        imgProductPicS = (ImageView) view.findViewById(R.id.imgProductPicS);
        imgIOS = (ImageView) view.findViewById(R.id.imgIOS);
        imgAndroid = (ImageView) view.findViewById(R.id.imgAndroid);
        imgSupportEmail = (ImageView) view.findViewById(R.id.imgSupportEmail);
        imgChatWithUs = (ImageView) view.findViewById(R.id.imgChatWithUs);
        txtProductNameS = (TextView) view.findViewById(R.id.txtProductNameS);
        txtOverviewS = (JustifiedTextView) view.findViewById(R.id.txtOverviewS);
        txtWebLinkS = (TextView) view.findViewById(R.id.txtWebLinkS);
        txtPlatformS = (TextView) view.findViewById(R.id.txtPlatformS);
        txtSelectedTab = (JustifiedTextView) view.findViewById(R.id.txtSelectedTab);
        llSelectedTab = (LinearLayout) view.findViewById(R.id.llSelectedTab);
        txtTechnologyUsed = (TextView) view.findViewById(R.id.txtTechnologyUsed);
        txtAchievement = (TextView) view.findViewById(R.id.txtAchievement);
        txtChallenges = (TextView) view.findViewById(R.id.txtChallenges);

        txtOverviewS.setTextSize(mContext.getResources().getDimension(R.dimen.font_14));
        txtOverviewS.setLineSpacing(10);
        txtOverviewS.setAlignment(Paint.Align.LEFT);
        txtSelectedTab.setTextSize(mContext.getResources().getDimension(R.dimen.font_14));
        txtSelectedTab.setLineSpacing(10);
        txtSelectedTab.setAlignment(Paint.Align.LEFT);

        //set Selection
        txtTechnologyUsed.setSelected(true);
        txtAchievement.setSelected(false);
        txtChallenges.setSelected(false);

        //set Values
        txtProductNameS.setText(listData.get(position).projectName);
        imgProductPicS.setImageResource(listData.get(position).projectImage);
        if (TextUtils.isEmpty(listData.get(position).projectOverview)) {
            txtOverviewS.setText(mContext.getResources().getString(R.string.nodetails));
        } else {
            txtOverviewS.setText(listData.get(position).projectOverview);
        }
        if (TextUtils.isEmpty(listData.get(position).projectWeblink)) {
            txtWebLinkS.setText(mContext.getResources().getString(R.string.nolink));
        } else {
            txtWebLinkS.setText(listData.get(position).projectWeblink);
        }
        txtPlatformS.setText(listData.get(position).projectPlateform);
        if (TextUtils.isEmpty(listData.get(position).projectTechnology)) {
            txtSelectedTab.setText(mContext.getResources().getString(R.string.nodetails));
        } else {
            txtSelectedTab.setText(listData.get(position).projectTechnology);
            setHeight(llSelectedTab);
        }
        if (TextUtils.isEmpty(listData.get(position).projectIOSLink)) {
            imgIOS.setVisibility(View.GONE);
        } else {
            imgIOS.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(listData.get(position).projectAndroidLink)) {
            imgAndroid.setVisibility(View.GONE);
        } else {
            imgAndroid.setVisibility(View.VISIBLE);
        }

        //Click Listener's
        txtTechnologyUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTechnologyUsed.setSelected(true);
                txtAchievement.setSelected(false);
                txtChallenges.setSelected(false);
                if (listData.get(position).projectTechnology.equals("")) {
                    txtSelectedTab.setText(mContext.getResources().getString(R.string.nodetails));
                } else {
                    setHeight(llSelectedTab);
                    txtSelectedTab.setText(listData.get(position).projectTechnology);
                }
            }
        });
        txtAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTechnologyUsed.setSelected(false);
                txtAchievement.setSelected(true);
                txtChallenges.setSelected(false);
                if (listData.get(position).projectAchievement.equals("")) {
                    txtSelectedTab.setText(mContext.getResources().getString(R.string.nodetails));
                } else {
                    setHeight(llSelectedTab);
                    txtSelectedTab.setText(listData.get(position).projectAchievement);
                }
            }
        });
        txtChallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTechnologyUsed.setSelected(false);
                txtAchievement.setSelected(false);
                txtChallenges.setSelected(true);
                if (listData.get(position).projectChallenges.equals("")) {
                    txtSelectedTab.setText(mContext.getResources().getString(R.string.nodetails));
                } else {
                    setHeight(llSelectedTab);
                    txtSelectedTab.setText(listData.get(position).projectChallenges);
                }
            }
        });
        imgIOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(listData.get(position).projectIOSLink)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        imgAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(listData.get(position).projectAndroidLink)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        imgSupportEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{mContext.getResources().getString(R.string.emailaddresssupport)});
                    i.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.shareSub) + "(" + listData.get(position).projectName + ")");
                    i.putExtra(Intent.EXTRA_TEXT, "");
                    mContext.startActivity(Intent.createChooser(i, mContext.getResources().getString(R.string.shareVia)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Util.showCenteredToast(Env.appContext, "There are no email clients installed.");
                }
            }
        });
        imgChatWithUs.setTag(position);
        imgChatWithUs.setOnClickListener((View.OnClickListener) mContext);
       /* imgChatWithUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(mContext,SlidingMenuActivity.class);
                    intent.putExtra("openChatWithUs",mContext.getResources().getString(R.string.openChatWithUs));
                    mContext.startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });*/

        container.addView(view);
        return view;
    }

    private void setHeight(final LinearLayout llSelectedTab) {
        llSelectedTab.post(new Runnable() {
            @Override
            public void run() {
                llSelectedTab.setMinimumHeight(llSelectedTab.getHeight());
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
        container.getChildCount();
    }
}
