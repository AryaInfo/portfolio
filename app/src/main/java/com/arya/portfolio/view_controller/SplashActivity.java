package com.arya.portfolio.view_controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragmentActivity;
import com.arya.portfolio.R;

import java.util.Observable;

/**
 * Created by user on 15/10/16.
 */

public class SplashActivity extends AbstractFragmentActivity {
    private int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        openApp();
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    private void openApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, SlidingMenuActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
