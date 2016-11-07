package com.arya.portfolio.view_controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.utils.MarshMallowPermissions;
import com.arya.lib.utils.Util;
import com.arya.lib.view.AbstractFragment;
import com.arya.portfolio.R;

import java.util.Observable;


public class ConnectFragment extends AbstractFragment implements View.OnClickListener {
    private View view;
    private ImageView imgUSAContactNo1, imgUSAContactNo2, imgINContactNo1, imgINContactNo2;
    private LinearLayout llEmailBusiness, llEmailSupport, llEmailInfo;
    private ImageView imgFacebook, imgLinkedin, imgTwitter;
    private MarshMallowPermissions marshMallowPermissions;


    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            view = inflater.inflate(R.layout.fragment_connect, container, false);
        }
        init();
        marshMallowPermissions=null;
        marshMallowPermissions=new MarshMallowPermissions((Activity) Env.currentActivity);
        return view;
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object data) {
        //do nothing.
    }

    private void init() {
        imgUSAContactNo1 = ((ImageView) view.findViewById(R.id.imgUSAContactNo1));
        imgUSAContactNo2 = ((ImageView) view.findViewById(R.id.imgUSAContactNo2));
        imgINContactNo1 = ((ImageView) view.findViewById(R.id.imgINContactNo1));
        imgINContactNo2 = ((ImageView) view.findViewById(R.id.imgINContactNo2));
        llEmailInfo = ((LinearLayout) view.findViewById(R.id.llEmailInfo));
        llEmailSupport = ((LinearLayout) view.findViewById(R.id.llEmailSupport));
        llEmailBusiness = ((LinearLayout) view.findViewById(R.id.llEmailBusiness));
        imgFacebook = ((ImageView) view.findViewById(R.id.imgFacebook));
        imgLinkedin = ((ImageView) view.findViewById(R.id.imgLinkedin));
        imgTwitter = ((ImageView) view.findViewById(R.id.imgTwitter));
        setClickOnView();
    }

    private void setClickOnView() {
        imgUSAContactNo1.setOnClickListener(this);
        imgUSAContactNo2.setOnClickListener(this);
        imgINContactNo1.setOnClickListener(this);
        imgINContactNo2.setOnClickListener(this);
        llEmailInfo.setOnClickListener(this);
        llEmailSupport.setOnClickListener(this);
        llEmailBusiness.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);
        imgLinkedin.setOnClickListener(this);
        imgTwitter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        String url;
        Intent intent;
        switch (vId) {
            case R.id.imgUSAContactNo1:
                callDialIntent(getResources().getString(R.string.usaContactNumber1));
                break;
            case R.id.imgUSAContactNo2:
                callDialIntent(getResources().getString(R.string.usaContactNumber2));
                break;
            case R.id.imgINContactNo1:
                callDialIntent(getResources().getString(R.string.indiaContactNumber1));
                break;
            case R.id.imgINContactNo2:
                callDialIntent(getResources().getString(R.string.indiaContactNumber2));
                break;
            case R.id.llEmailInfo:
                sendEmail(getResources().getString(R.string.emailinfo));
                break;
            case R.id.llEmailSupport:
                sendEmail(getResources().getString(R.string.emailaddresssupport));
                break;
            case R.id.llEmailBusiness:
                sendEmail(getResources().getString(R.string.emailaddressquery));
                break;
            case R.id.imgFacebook:
                url = "http://www.facebook.com/aryausa/?fref=ts";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.imgLinkedin:
                url = "http://www.linkedin.com/company/655583";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.imgTwitter:
                url = "http://twitter.com/aryavrat_01";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
            default:
                //do nothing.
                break;
        }
    }

    private void callDialIntent(String contactNumber) {
        if(!marshMallowPermissions.checkPermissionForPhone()){
            marshMallowPermissions.requestPermissionForPhone();
        }else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactNumber));
            startActivity(intent);
        }
    }

    private void sendEmail(String emailAddress) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.shareSub));
            i.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Util.showCenteredToast(Env.appContext, "There are no email clients installed.");
        }
    }
}