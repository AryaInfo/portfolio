package com.arya.portfolio.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arya.lib.utils.MarshMallowPermissions;
import com.arya.portfolio.R;
import com.arya.portfolio.custom.JustifiedTextView;
import com.arya.portfolio.dao.ExpertiseData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by arya on 14/10/16.
 */
public class ExpertiseAdapter extends BaseAdapter {

    private ArrayList<ExpertiseData> listData;
    private Context mContext;
    private MarshMallowPermissions marshMallowPermissions;

    public ExpertiseAdapter(Context ctx, ArrayList<ExpertiseData> expertiseListData) {
        listData = expertiseListData;
        mContext = ctx;
        marshMallowPermissions = null;
        marshMallowPermissions = new MarshMallowPermissions((Activity) mContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_expertise_list_items, null);
            viewHolder = new ViewHolder();
            viewHolder.txtProductNameExpertise = ((TextView) convertView.findViewById(R.id.txtProductNameExpertise));
            viewHolder.txtProductDetailsExpertise = ((JustifiedTextView) convertView.findViewById(R.id.txtProductDetailsExpertise));
            viewHolder.txtReadMoreExpertise = ((TextView) convertView.findViewById(R.id.txtReadMoreExpertise));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }

        viewHolder.txtProductDetailsExpertise.setTextSize(mContext.getResources().getDimension(R.dimen.font_14));
        viewHolder.txtProductDetailsExpertise.setLineSpacing(10);
        viewHolder.txtProductDetailsExpertise.setAlignment(Paint.Align.LEFT);

        final ExpertiseData expertiseData = listData.get(position);
        viewHolder.txtProductNameExpertise.setText(expertiseData.productName);
        viewHolder.txtProductDetailsExpertise.setText(expertiseData.productDescription);
        viewHolder.txtReadMoreExpertise.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   if (!marshMallowPermissions.checkPermissionForExternalStorage()) {
                                                                       marshMallowPermissions.requestPermissionForExternalStorage();
                                                                   } else {
                                                                       InputStream in = null;
                                                                       OutputStream out = null;

                                                                       try {

                                                                           AssetManager assetManager = mContext.getAssets();
                                                                           File file = new File(Environment.getExternalStorageDirectory() + "/" + expertiseData.productFileName);
                                                                           in = assetManager.open(file.getName());
                                                                           out = new FileOutputStream(file);

                                                                           copyFile(in, out);
                                                                           in.close();
                                                                           in = null;
                                                                           out.flush();
                                                                           out.close();
                                                                           out = null;

                                                                           Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                           intent.setDataAndType(Uri.fromFile(file), "application/pdf");

                                                                           //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                           //Utils.startActAnimation(((Activity) mContext));
                                                                           mContext.startActivity(intent);
                                                                       } catch (Exception e) {
                                                                           e.printStackTrace();
                                                                       }
                                                                   }
                                                               }
                                                           }

        );

        return convertView;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public class ViewHolder {
        public TextView txtProductNameExpertise;
        public JustifiedTextView txtProductDetailsExpertise;
        public TextView txtReadMoreExpertise;
    }
}