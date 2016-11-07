package com.arya.portfolio.view_controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.AryaConnect.dao.UserData;
import com.AryaConnect.utils.AryaConnectClient;
import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;
import com.arya.portfolio.R;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by user on 12/10/16.
 */

public class ChatWithUsFragment extends AbstractFragment implements View.OnClickListener {
    private View view;
    private TextView txtStartChat;
    private AryaConnectClient aryaConnectClient;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            view = inflater.inflate(R.layout.fragment_chat_with_us, container, false);
        }
        init();
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
        txtStartChat = ((TextView) view.findViewById(R.id.txtStartChat));
        txtStartChat.setOnClickListener(this);

        //String android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        TelephonyManager telephonyManager = (TelephonyManager) Env.currentActivity.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.getDeviceId();

        //long timeStamp= System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        int vID = v.getId();
        switch (vID) {
            case R.id.txtStartChat:
                openChatScreen();
                break;
            default:
                //do nothing.
                break;

        }
    }

    private void openChatScreen() {
        try {
            aryaConnectClient = AryaConnectClient.getInstance(Env.appContext);
            //aryaConnectClient.goToChatUI(Env.currentActivity, Preferences.getUserId(), Preferences.getUserImageUrl(), Preferences.getUserName(), filterUserList());
            aryaConnectClient.goToChatUI(Env.currentActivity, 2, "http://www.planetware.com/photos-large/F/france-paris-eiffel-tower.jpg", "User", UserList());
            aryaConnectClient.setThemeColor(R.color.color_green);
            aryaConnectClient.setTabTextColor(R.color.white);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<UserData> UserList() {
        ArrayList<UserData> arrayList = new ArrayList<>();
        UserData aryaSupport = new UserData();
        aryaSupport.userId = 1;
        aryaSupport.imageUrl = "http://www.aryausa.com/images/arya-logo.png";
        aryaSupport.userType = "Support Team";
        aryaSupport.userName = "Support Team";
        aryaSupport.userDeviceId = 2 + "";
        arrayList.add(aryaSupport);
        return arrayList;
    }

}
