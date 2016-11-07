package com.arya.portfolio.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by arya on 27/09/16.
 */

public class IndustryData implements Serializable, Comparator<IndustryData> {

    public String categoryName;
    public int categoryImage;
    public int categoryImageLarge;
    public String categoryDetail;
    public String industryTitle;
    public String industryDescription;
    public ArrayList<IndustryData> consultingNSolution;

    @Override
    public int compare(IndustryData lhs, IndustryData rhs) {
        return lhs.categoryName.compareToIgnoreCase(rhs.categoryName);
    }
}
