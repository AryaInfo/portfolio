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
import com.arya.portfolio.dao.PortfolioData;
import com.arya.portfolio.utility.Utils;

import java.util.ArrayList;

/**
 * Created by user on 13/09/16.
 */
public class PortfolioAdapter extends BaseAdapter {
    Context context;
    ArrayList<PortfolioData> listProduct;
    public Resources res;

    public PortfolioAdapter(Context context, ArrayList<PortfolioData> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    public int getCount() {
        return listProduct.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_home_list_items, null);
            holder = new ViewHolder();
            holder.txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
            holder.imgProductPic = (ImageView) convertView.findViewById(R.id.imgProductPic);
            holder.imgChatWithUs = (ImageView) convertView.findViewById(R.id.imgChatWithUs);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PortfolioData productData = listProduct.get(position);
        holder.txtProductName.setText(productData.projectName);
        holder.imgProductPic.setImageResource(productData.projectImage);
        holder.imgChatWithUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   /* if (Env.currentActivity instanceof SlidingMenuActivity) {
                        ((SlidingMenuActivity) Env.currentActivity).onClick(((SlidingMenuActivity) Env.currentActivity).llChatWithUs);
                    }*/
                    Utils.openChatScreen();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView txtProductName;
        public ImageView imgProductPic, imgChatWithUs;
    }



}
