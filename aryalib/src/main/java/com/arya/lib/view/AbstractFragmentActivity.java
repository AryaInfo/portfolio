package com.arya.lib.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.utils.Util;

import java.util.Observer;


public abstract class AbstractFragmentActivity extends FragmentActivity implements Observer {
    protected BasicModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getModel();
        if (model != null)
            model.addObserver(this);
        Env.currentActivity = this;
        Env.appContext = getApplicationContext();
        onCreatePost(savedInstanceState);
    }

    protected abstract void onCreatePost(Bundle savedInstanceState);

    protected abstract BasicModel getModel();

    @Override
    protected void onResume() {
        super.onResume();
        Env.application_state = Env.AppState.STATE_VISIBLE;
        Env.currentActivity = this;
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
        if (Util.toast != null) {
            Util.toast.cancel();
        }
        return super.dispatchTouchEvent(ev);

    }
}
