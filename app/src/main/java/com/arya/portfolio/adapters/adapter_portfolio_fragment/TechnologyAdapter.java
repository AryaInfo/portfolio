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

import com.arya.portfolio.dao.TechnologyUsedData;

import java.util.ArrayList;

/**
 * Created by user on 27/09/16.
 */

public class TechnologyAdapter extends BaseAdapter {
    Context context;
    ArrayList<TechnologyUsedData> listTechnology;
    public Resources res;
    public TechnologyAdapter(Context context, ArrayList<TechnologyUsedData> listTechnology) {
        this.context = context;
        this.listTechnology = listTechnology;
    }

    public int getCount() {
        return listTechnology.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TechnologyAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_technology_list_items, null);
            holder = new TechnologyAdapter.ViewHolder();
            holder.txtTechnologyTitle = (TextView) convertView.findViewById(R.id.txtTechnologyTitle);
            holder.imgTechnology = (ImageView) convertView.findViewById(R.id.imgTechnology);
            convertView.setTag(holder);
        } else {
            holder = (TechnologyAdapter.ViewHolder) convertView.getTag();
        }
        TechnologyUsedData technologyUsedData = listTechnology.get(position);
        holder.txtTechnologyTitle.setText(technologyUsedData.technologyName);
        holder.imgTechnology.setImageResource(technologyUsedData.technologyImageLarge);
        return convertView;
    }

    public class ViewHolder {
        public TextView txtTechnologyTitle;
        public ImageView imgTechnology;
    }
}


