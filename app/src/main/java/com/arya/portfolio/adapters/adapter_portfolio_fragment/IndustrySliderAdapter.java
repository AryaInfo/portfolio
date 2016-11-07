package com.arya.portfolio.adapters.adapter_portfolio_fragment;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.portfolio.R;
import com.arya.portfolio.custom.JustifiedTextView;
import com.arya.portfolio.dao.IndustryData;

import java.util.ArrayList;

/**
 * Created by arya on 06/10/16.
 */

public class IndustrySliderAdapter extends PagerAdapter {
    private ArrayList<IndustryData> listIndData;
    private Context mContext;

    public IndustrySliderAdapter(ArrayList<IndustryData> listIndData, Context ctx) {
        this.listIndData = listIndData;
        this.mContext = ctx;
    }

    @Override
    public int getCount() {
        return listIndData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.viewpager_industry_slide,container,false);
        ImageView imgProductPicS = (ImageView) view.findViewById(R.id.imgProductPicS);
        TextView txtIndustryCategoryName = (TextView) view.findViewById(R.id.txtIndustryCategoryName);
        JustifiedTextView txtIndustryOverView = (JustifiedTextView) view.findViewById(R.id.txtIndustryOverView);
        LinearLayout llIndustry = (LinearLayout) view.findViewById(R.id.llIndustry);

        txtIndustryOverView.setTextSize(mContext.getResources().getDimension(R.dimen.font_14));
        txtIndustryOverView.setLineSpacing(10);
        txtIndustryOverView.setAlignment(Paint.Align.LEFT);

        txtIndustryCategoryName.setText(listIndData.get(position).categoryName);
        txtIndustryOverView.setText(listIndData.get(position).categoryDetail);
        imgProductPicS.setImageResource(listIndData.get(position).categoryImageLarge);

        for (IndustryData industryData1 : listIndData.get(position).consultingNSolution) {

            View convertView = LayoutInflater.from(Env.appContext).inflate(R.layout.layout_industry_single_items, null);
            JustifiedTextView txtConsultingSolutions = (JustifiedTextView) convertView.findViewById(R.id.txtConsultingSolutions);
            TextView txtConsultingSolutionsTitle = (TextView) convertView.findViewById(R.id.txtConsultingSolutionsTitle);

            llIndustry.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            txtConsultingSolutions.setTextSize(mContext.getResources().getDimension(R.dimen.font_14));
            txtConsultingSolutions.setLineSpacing(10);
            txtConsultingSolutions.setAlignment(Paint.Align.LEFT);

            txtConsultingSolutionsTitle.setText(industryData1.industryTitle);
            txtConsultingSolutions.setText(industryData1.industryDescription);

            llIndustry.addView(convertView);
        }
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
        container.getChildCount();
    }
}
