package com.arya.portfolio.model;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.arya.lib.model.BasicModel;
import com.arya.portfolio.dao.IndustryData;
import com.arya.portfolio.dao.PortfolioData;
import com.arya.portfolio.dao.TechnologyUsedData;
import com.arya.portfolio.database.DBManagerAP;

import java.util.ArrayList;

/**
 * Created by arya on 20/09/16.
 */

public class PortfolioModel extends BasicModel {

    GetIndustryByNameTask getIndustryByNameTask;
    GetTechnologyByNameTask getTechnologyByNameTask;
    GetProductAccToTabSelectedTask getProductAccToTabSelectedTask;

    private ArrayList<IndustryData> listIndustryData;
    private ArrayList<TechnologyUsedData> listTechnologyData;
    private ArrayList<PortfolioData> listTabProductData;

    public void getIndustryByName(String searchString, ArrayList<IndustryData> listIndustryData) {
        if (getIndustryByNameTask == null) {
            getIndustryByNameTask = new GetIndustryByNameTask();
            getIndustryByNameTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchString, listIndustryData);
        }
    }

    public void getTechnologyByName(String searchString, ArrayList<TechnologyUsedData> listTechnologyData) {
        if (getTechnologyByNameTask == null) {
            getTechnologyByNameTask = new GetTechnologyByNameTask();
            getTechnologyByNameTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchString, listTechnologyData);
        }
    }

    public void getProductAccToTabSelected( String productName, String technology, String industryCategory) {
        if (getProductAccToTabSelectedTask == null) {
            getProductAccToTabSelectedTask = new GetProductAccToTabSelectedTask();
            getProductAccToTabSelectedTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  productName, technology, industryCategory);
        }
    }

    private class GetIndustryByNameTask extends AsyncTask<Object, Void, ArrayList<IndustryData>> {

        @Override
        protected ArrayList<IndustryData> doInBackground(Object... params) {
            try {
                String searchString = (String) params[0];
                if (TextUtils.isEmpty(searchString)) {
                    listIndustryData = (ArrayList<IndustryData>) params[1];
                    return listIndustryData;
                }

                ArrayList<IndustryData> filteredIndustryData = new ArrayList<>();
                searchString = searchString.trim().toLowerCase();
                for (IndustryData industryData : (ArrayList<IndustryData>) params[1]) {
                    if (industryData.categoryName.toLowerCase().contains(searchString)) {
                        filteredIndustryData.add(industryData);
                    }
                }
                listIndustryData = filteredIndustryData;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listIndustryData;
        }

        @Override
        protected void onPostExecute(ArrayList<IndustryData> industryDatas) {
            super.onPostExecute(industryDatas);
            getIndustryByNameTask = null;
            PortfolioModel.this.notifyObservers(industryDatas);
        }
    }

    private class GetTechnologyByNameTask extends AsyncTask<Object, Void, ArrayList<TechnologyUsedData>> {

        @Override
        protected ArrayList<TechnologyUsedData> doInBackground(Object... params) {
            try {
                String searchString = (String) params[0];
                if (TextUtils.isEmpty(searchString)) {
                    listTechnologyData = (ArrayList<TechnologyUsedData>) params[1];
                    return listTechnologyData;
                }

                ArrayList<TechnologyUsedData> filteredTechnologyData = new ArrayList<>();
                searchString = searchString.trim().toLowerCase();
                for (TechnologyUsedData technologyUsedData : (ArrayList<TechnologyUsedData>) params[1]) {
                    if (technologyUsedData.technologyName.toLowerCase().contains(searchString)) {
                        filteredTechnologyData.add(technologyUsedData);
                    }
                }
                listTechnologyData = filteredTechnologyData;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listTechnologyData;
        }

        @Override
        protected void onPostExecute(ArrayList<TechnologyUsedData> technologyUsedDatas) {
            super.onPostExecute(technologyUsedDatas);
            getTechnologyByNameTask = null;
            PortfolioModel.this.notifyObservers(technologyUsedDatas);
        }
    }

    private class GetProductAccToTabSelectedTask extends AsyncTask<String, Void, ArrayList<PortfolioData>> {

        @Override
        protected ArrayList<PortfolioData> doInBackground(String... params) {
            try {
                String productName = params[0];
                String technology = params[1];
                String industryCategory = params[2];

                listTabProductData = DBManagerAP.getInstance().getProductAccToCategory(productName,technology,industryCategory);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listTabProductData;
        }

        @Override
        protected void onPostExecute(ArrayList<PortfolioData> listProductData) {
            super.onPostExecute(listProductData);
            getProductAccToTabSelectedTask = null;
            PortfolioModel.this.notifyObservers(listProductData);
        }
    }
}
