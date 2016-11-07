package com.arya.portfolio.adapters.adapter_portfolio_fragment;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.arya.portfolio.R;
import com.arya.portfolio.dao.IndustryData;
import java.util.ArrayList;

/**
 * Created by user on 27/09/16.
 */

public class IndustryAdapter extends BaseAdapter {
    Context context;
    ArrayList<IndustryData> listIndustry;
    public Resources res;
    public IndustryAdapter(Context context, ArrayList<IndustryData> listIndustry) {
        this.context = context;
        this.listIndustry = listIndustry;
    }

    public int getCount() {
        return listIndustry.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_industry_list_items, null);
            holder = new ViewHolder();
            holder.txtCategoryTitle = (TextView) convertView.findViewById(R.id.txtCategoryTitle);
            holder.imgCategory = (ImageView) convertView.findViewById(R.id.imgCategory);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IndustryData industryData = listIndustry.get(position);
        holder.txtCategoryTitle.setText(industryData.categoryName);
        holder.imgCategory.setImageResource(industryData.categoryImageLarge);

        return convertView;
    }

    public class ViewHolder {
        public TextView txtCategoryTitle;
        public ImageView imgCategory;
    }
}

