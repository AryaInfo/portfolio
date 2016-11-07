package com.arya.lib.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;

import java.util.Observer;

public abstract class AbstractAppCompatActivity extends AppCompatActivity implements Observer {

    protected BasicModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Env.currentActivity=this;
            model = getModel();
            if (model != null)
                model.addObserver(this);

            onCreatePost(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void onCreatePost(Bundle savedInstanceState);

    protected abstract BasicModel getModel();
}
