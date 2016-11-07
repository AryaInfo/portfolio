package com.arya.portfolio.dao;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by user on 27/09/16.
 */

public class TechnologyUsedData implements Serializable, Comparator<TechnologyUsedData> {

    public String technologyName;
    public String technologyDescription;
    public int technologyImageLarge;

    @Override
    public int compare(TechnologyUsedData lhs, TechnologyUsedData rhs) {
        return lhs.technologyName.compareToIgnoreCase(rhs.technologyName);
    }
}
