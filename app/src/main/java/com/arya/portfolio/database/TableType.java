package com.arya.portfolio.database;

import com.arya.portfolio.dao.NewsData;
import com.arya.portfolio.dao.PortfolioData;

public enum TableType {
    CreatePortfolioTable(1, PortfolioData.TABLE_NAME),
    CreateNewsTable(2, NewsData.TABLE_NAME);

    private int index;
    private String tableName;

    TableType(int index, String tableName) {
        this.index = index;
        this.tableName = tableName;
    }

    public int getIndex() {
        return index;
    }

    public String getTableName() {
        return tableName;
    }
}
