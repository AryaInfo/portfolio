package com.arya.lib.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;

import java.util.Observer;

/**
 * Created by phoosaram on 10/13/2015.
 */
public abstract class AbstractDialogFragment extends DialogFragment implements Observer {
    protected BasicModel model;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        model = getModel();
        Env.currentActivity = getActivity();
        if (model != null) {
            model.addObserver(this);
        }
        return onCreateDialogPost(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Env.currentActivity = getActivity();
    }

    protected abstract Dialog onCreateDialogPost(Bundle savedInstanceState);

    protected abstract BasicModel getModel();

}
