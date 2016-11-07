package com.arya.portfolio.dao;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by user on 13/09/16.
 */
public class PortfolioData implements Serializable ,Comparator<PortfolioData> {
    public static String TABLE_NAME = "portfolio";

    public static final String KEY_ID = "id";
    public static final String KEY_PROJECT_NAME = "project_name";
    public static final String KEY_PROJECT_ID = "project_id";
    public static final String KEY_PROJECT_IMAGE = "project_image";
    public static final String KEY_PROJECT_OVERVIEW = "project_overview";
    public static final String KEY_PROJECT_WEB_LINK = "project_web_link";
    public static final String KEY_PROJECT_CATEGORY = "project_category";
    public static final String KEY_PROJECT_PLATFORM = "project_plateform";
    public static final String KEY_PROJECT_TECHNOLOGY = "project_technology";
    public static final String KEY_PROJECT_ACHIEVEMENT = "project_achievement";
    public static final String KEY_PROJECT_CHALLENGES = "project_challenges";
    public static final String KEY_PROJECT_IOS_LINK = "project_ios_link";
    public static final String KEY_PROJECT_ANDROID_LINK = "project_android_link";
    public static final String KEY_PROJECT_INDUSTRY_CATEGORY = "project_industry_category";

    public String projectName;
    public String projectId;
    public int projectImage;
    public String projectOverview;
    public String projectWeblink;
    public String projectCategory;
    public String projectPlateform;
    public String projectTechnology;
    public String projectAchievement;
    public String projectChallenges;
    public String projectIOSLink;
    public String projectAndroidLink;
    public String projectIndstryCategory;


    @Override
    public int compare(PortfolioData lhs, PortfolioData rhs) {
        return lhs.projectName.compareToIgnoreCase(rhs.projectName);
    }
}
