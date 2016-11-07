package com.arya.lib.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;

import java.util.Observer;


public abstract class AbstractFragment extends Fragment implements Observer {
    protected BasicModel model;

        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            model = getModel();
            Env.currentActivity=getActivity();
            Env.appContext=getContext();
            if (model != null)
                model.addObserver(this);
        return onCreateViewPost(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Env.currentActivity=getActivity();
    }

    protected abstract View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    protected abstract BasicModel getModel();

}
