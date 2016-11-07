package com.arya.portfolio.view_controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;
import com.arya.portfolio.R;
import com.arya.portfolio.adapters.ExpertiseAdapter;
import com.arya.portfolio.dao.ExpertiseData;

import java.util.ArrayList;
import java.util.Observable;


public class ExpertiseFragment extends AbstractFragment {
    private View view;

    private GridView gvExpertise;
    private ArrayList<ExpertiseData> expertiseListData = new ArrayList<>();

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            view = inflater.inflate(R.layout.fragment_expertise, container, false);
        }
        init();
        return view;
    }

    public void init() {
        gvExpertise = ((GridView) view.findViewById(R.id.gvExpertise));
        createExpertiseData();
        setAdapter(expertiseListData);

    }

    private void setAdapter(ArrayList<ExpertiseData> expertiseListData) {
        ExpertiseAdapter expertiseAdapter = new ExpertiseAdapter(getActivity(), expertiseListData);
        gvExpertise.setAdapter(expertiseAdapter);
        expertiseAdapter.notifyDataSetChanged();
    }

    private void createExpertiseData() {

        ExpertiseData expertiseData1 = new ExpertiseData();
        expertiseData1.productName = "Product Engineering";
        expertiseData1.productDescription = "Product engineering refers to the process of designing and developing a device, assembly, or system such that it be produced as an item for sale through some production manufacturing process. Product engineering usually entails activity dealing with issues of cost, producibility, quality, performance, reliability, serviceability, intended lifespan and user features.";
        expertiseData1.productFileName = "product_engineering.pdf";
        expertiseListData.add(expertiseData1);

        ExpertiseData expertiseData2 = new ExpertiseData();
        expertiseData2.productName = "Chat Bots";
        expertiseData2.productDescription = "Chat bots are computer programs that do mimic conversation with people using artificial intelligence, they are more human and speak real language, therefore can do better interaction. Faster, easier to build and make the experience of performing multiple tasks or handling multiple clients seems easy.";
        expertiseData2.productFileName = "chat_bots.pdf";
        expertiseListData.add(expertiseData2);

        ExpertiseData expertiseData3 = new ExpertiseData();
        expertiseData3.productName = "Internet Of Things (IOT)";
        expertiseData3.productDescription = "A network of interconnected things/devices embeded with sensors, software, and network that enables them to collect, exchange data and make them responsive. Interestingly it is being enabled by the presence of other independent technologies.";
        expertiseData3.productFileName = "internet_of_things_iot.pdf";
        expertiseListData.add(expertiseData3);

        ExpertiseData expertiseData4 = new ExpertiseData();
        expertiseData4.productName = "BigData";
        expertiseData4.productDescription = "Big Data is characterized by 3V's i.e. Volume, Variety and Velocity. It is described as the large volume of structured and unstructured variable data with greater complexity. By using this, data can be streamed at an unprecedented speed and dealt in a timely manner.";
        expertiseData4.productFileName = "big_data.pdf";
        expertiseListData.add(expertiseData4);

    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object data) {
        //do nothing.
    }

}
