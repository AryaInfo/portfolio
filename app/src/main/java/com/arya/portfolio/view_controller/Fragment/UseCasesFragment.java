package com.arya.portfolio.view_controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;
import com.arya.portfolio.R;
import com.arya.portfolio.adapters.adapter_portfolio_fragment.IndustryAdapter;
import com.arya.portfolio.adapters.adapter_portfolio_fragment.PortfolioAdapter;
import com.arya.portfolio.adapters.adapter_portfolio_fragment.TechnologyAdapter;
import com.arya.portfolio.dao.IndustryData;
import com.arya.portfolio.dao.PortfolioData;
import com.arya.portfolio.dao.TechnologyUsedData;
import com.arya.portfolio.database.DBManagerAP;
import com.arya.portfolio.model.PortfolioModel;
import com.arya.portfolio.utility.Utils;
import com.arya.portfolio.view_controller.PortfolioList;
import com.arya.portfolio.view_controller.PortfolioSingleActivity;
import com.arya.portfolio.view_controller.SlidingMenuActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;


public class UseCasesFragment extends AbstractFragment implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, SwipeRefreshLayout.OnRefreshListener {

    View view;
    TextView txtProductEngineering, txtIndustry, txtTechnologyUsed, txtLastSelectedView;
    GridView gvProductEngineering;
    private ArrayList<PortfolioData> listProduct = new ArrayList<>();
    private String searchString;
    private PortfolioAdapter portfolioAdapter;
    private IndustryAdapter industryAdapter;
    private TechnologyAdapter technologyAdapter;
    private PortfolioModel portfolioModel = new PortfolioModel();
    ArrayList<PortfolioData> listData = new ArrayList<>();
    ArrayList<IndustryData> listIndustryData = new ArrayList<>();
    ArrayList<IndustryData> listIndData = new ArrayList<>();
    ArrayList<TechnologyUsedData> listTechnologyData = new ArrayList<>();
    ArrayList<TechnologyUsedData> listTechData = new ArrayList<>();
    ArrayList<PortfolioData> portfolioList = new ArrayList<>();
    ArrayList<PortfolioData> portfolioListIndustry = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshPortfolio;
    private static final int REQUEST_CODE = 100;
    ImageView imgChatWithUs;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        }
        init();
        return view;
    }

    @Override
    protected BasicModel getModel() {
        return portfolioModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            if (data instanceof ArrayList) {
                if (txtProductEngineering.isSelected()) {
                    ArrayList<PortfolioData> filteredList = ((ArrayList<PortfolioData>) data);
                    setPortfolioAdapter(filteredList);
                } else if (txtIndustry.isSelected()) {
                    ArrayList<IndustryData> filterIndustrData = (ArrayList<IndustryData>) data;
                    setIndustryAdapter(filterIndustrData);
                } else if (txtTechnologyUsed.isSelected()) {
                    ArrayList<TechnologyUsedData> filterTechnologyData = (ArrayList<TechnologyUsedData>) data;
                    setTechnologyAdapter(filterTechnologyData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {

        swipeRefreshPortfolio = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshPortfolio);
        swipeRefreshPortfolio.setColorSchemeResources(R.color.color_green);
        gvProductEngineering = (GridView) view.findViewById(R.id.gvProductEngineering);
        txtProductEngineering = (TextView) view.findViewById(R.id.txtProductEngineering);
        imgChatWithUs = (ImageView) view.findViewById(R.id.imgChatWithUs);

        txtIndustry = (TextView) view.findViewById(R.id.txtIndustry);
        txtTechnologyUsed = (TextView) view.findViewById(R.id.txtTechnologyUsed);

        txtLastSelectedView = txtProductEngineering;
        setViewSelected(txtLastSelectedView);
        tabSelected();
        setClickOnView();
        createProjectData();
        createIndustryData();
        createTechnologyData();
        portfolioModel.getProductAccToTabSelected("", "", "");
        // Utils.writeDBfileOnSdcard(Env.appContext);//write DB
    }

    private void tabSelected() {
        txtProductEngineering.setSelected(true);
        txtIndustry.setSelected(false);
        txtTechnologyUsed.setSelected(false);
    }

    private void setClickOnView() {

        txtTechnologyUsed.setOnClickListener(this);
        txtProductEngineering.setOnClickListener(this);
        txtIndustry.setOnClickListener(this);
        imgChatWithUs.setOnClickListener(this);
        gvProductEngineering.setOnItemClickListener(this);
        swipeRefreshPortfolio.setOnRefreshListener(this);
    }

    private void setPortfolioAdapter(ArrayList<PortfolioData> listProductData) {
        listData = listProductData;
        Collections.sort(listData, new PortfolioData());
        portfolioAdapter = new PortfolioAdapter(getActivity(), listData);
        gvProductEngineering.setAdapter(portfolioAdapter);
        portfolioAdapter.notifyDataSetChanged();
        swipeRefreshPortfolio.setRefreshing(false);
    }

    private void setIndustryAdapter(ArrayList<IndustryData> listIndustryData) {
        listIndData = listIndustryData;
        Collections.sort(listIndData, new IndustryData());
        industryAdapter = new IndustryAdapter(getActivity(), listIndData);
        gvProductEngineering.setAdapter(industryAdapter);
        industryAdapter.notifyDataSetChanged();
        swipeRefreshPortfolio.setRefreshing(false);
    }

    private void setTechnologyAdapter(ArrayList<TechnologyUsedData> listTechnologyData) {
        listTechData = listTechnologyData;
        Collections.sort(listTechData, new TechnologyUsedData());
        technologyAdapter = new TechnologyAdapter(getActivity(), listTechData);
        gvProductEngineering.setAdapter(technologyAdapter);
        technologyAdapter.notifyDataSetChanged();
        swipeRefreshPortfolio.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.txtProductEngineering:
                setLastViewDeselected();
                setViewSelected(txtProductEngineering);
                hideSearchBar();
                portfolioModel.getProductAccToTabSelected("", "", "");
                break;
            case R.id.txtTechnologyUsed:
                setLastViewDeselected();
                setViewSelected(txtTechnologyUsed);
                hideSearchBar();
                setTechnologyAdapter(listTechnologyData);
                break;
            case R.id.txtIndustry:
                setLastViewDeselected();
                setViewSelected(txtIndustry);
                hideSearchBar();
                setIndustryAdapter(listIndustryData);
                break;
            case R.id.imgChatWithUs:
                Utils.openChatScreen();
                break;


        }
    }


    private void hideSearchBar() {
        if (Env.currentActivity instanceof SlidingMenuActivity) {
            if (((SlidingMenuActivity) Env.currentActivity).inSearchBar.getVisibility() == View.VISIBLE) {
                ((SlidingMenuActivity) Env.currentActivity).onClick(((SlidingMenuActivity) Env.currentActivity).txtCancel);
            }
        }
    }

    private void setLastViewDeselected() {
        txtLastSelectedView.setTextColor(ContextCompat.getColor(Env.appContext, R.color.color_black));
        txtLastSelectedView.setBackgroundColor(ContextCompat.getColor(Env.appContext, R.color.color_white));
        txtLastSelectedView.setSelected(false);
    }

    private void setViewSelected(TextView selectedView) {
        selectedView.setTextColor(ContextCompat.getColor(Env.appContext, R.color.color_white));
        selectedView.setBackgroundColor(ContextCompat.getColor(Env.appContext, R.color.color_light_green));
        selectedView.setSelected(true);
        txtLastSelectedView = selectedView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;
        Bundle bundle = new Bundle();
        if (txtProductEngineering.isSelected()) {
            intent = new Intent(getActivity(), PortfolioSingleActivity.class);
            bundle.putSerializable("listData", listData);
            bundle.putInt("position", i);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (txtIndustry.isSelected()) {
           /* intent = new Intent(getActivity(), IndustrySingleActivity.class);
            bundle.putSerializable("listIndData", listIndData);
            bundle.putInt("position", i);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE);*/
            intent = new Intent(getActivity(), PortfolioList.class);
            String industryCategory = listIndData.get(i).categoryName.toString().trim();
            portfolioListIndustry = DBManagerAP.getInstance().getProductAccToCategory("", "", industryCategory);
            bundle.putSerializable("listData", portfolioListIndustry);
            bundle.putString("title", listIndData.get(i).categoryName);
            bundle.putInt("position", i);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            intent = new Intent(getActivity(), PortfolioList.class);
            String technologyName = listTechData.get(i).technologyName.toLowerCase().trim();
            portfolioList = DBManagerAP.getInstance().getProductAccToCategory("", technologyName, "");
            bundle.putSerializable("listData", portfolioList);
            bundle.putString("title", listTechData.get(i).technologyName);
            bundle.putInt("position", i);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CODE) {
                if (resultCode == getActivity().RESULT_OK) {
                    int selectedPosition = data.getExtras().getInt("position");
//                    gvProductEngineering.setItemChecked(selectedPosition, true);
//                    gvProductEngineering.smoothScrollToPosition(selectedPosition);
                    gvProductEngineering.setSelection(selectedPosition);
                    View v = gvProductEngineering.getChildAt(selectedPosition);
                    if (v != null) {
                        v.requestFocus();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        searchString = s.toString().trim();
        if (!TextUtils.isEmpty(searchString)) {
            searchByTitle(searchString);
        } else {
            searchString = null;
            searchByTitle(searchString);
        }
    }

    private void searchByTitle(String searchString) {
        if (txtProductEngineering.isSelected()) {
            portfolioModel.getProductAccToTabSelected(searchString, "", "");

        } else if (txtIndustry.isSelected()) {
            portfolioModel.getIndustryByName(searchString, listIndustryData);
        } else if (txtTechnologyUsed.isSelected()) {
            portfolioModel.getTechnologyByName(searchString, listTechnologyData);
        }
    }

    @Override
    public void onRefresh() {
        try {
            if (swipeRefreshPortfolio.isRefreshing()) {
                swipeRefreshPortfolio.setRefreshing(false);
            }
            hideSearchBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTechnologyData() {

        TechnologyUsedData technologyData1 = new TechnologyUsedData();
        technologyData1.technologyName = "JAVA";
        technologyData1.technologyDescription = "Java is yet another highly popular and widely " +
                "used language that you can consider for web development. " +
                "This language is an object-oriented, class-based and concurrent " +
                "language that was developed by Sun Microsystems in the 1990s. Since then, " +
                "the language continues to be the most in-demand language that also acts as " +
                "a standard platform for enterprises and several mobile and games developers " +
                "across the world. The app has been designed in such a way that it works across " +
                "several types of platforms. This means that if a program is written on Mac Operating " +
                "system then it can also run on Windows based operating systems.\n" +
                "Java, when it was designed originally, was developed for interactive television," +
                " but the developers realized that this language and technology was way too forward " +
                "for this industry. It was only later that it was incorporated into the use it serves today.\n" +
                "Every language is created with a certain mission, goal or objective in mind. " +
                "The following are the 5 major principles or goals that were kept in mind during the creation of this language:\n" +
                "\t•\tIt must be a secure and robust programming language\n" +
                "\t•\tIt must be an object-oriented, simple language which becomes familiar soon.\n" +
                "\t•\tIt must be capable of being implemented and executed with high performance.\n" +
                "\t•\tIt must be threaded, dynamic and interpreted.\n" +
                "\t•\tIt must be portable and architecture-neutral.";
        technologyData1.technologyImageLarge = R.mipmap.java_1920_1080;
        listTechnologyData.add(technologyData1);

        TechnologyUsedData technologyData2 = new TechnologyUsedData();
        technologyData2.technologyName = "PHP";
        technologyData2.technologyDescription = "The term ‘PHP’ is used to define PHP Hypertext Processor language that is a free server-side scripting language that has been designed for not just web development but also as a general-purpose programming platform. This is a widely used language that was created in the year 2004 and now powers over 200 million websites worldwide. Some popular examples of websites powered by this platform include Facebook, WordPress, and Digg.com.\n" +
                "PHP is an interpreted script language which means that it is usually processed by an interpreter. For this reason, the language is most suitable for server-side programming that have server tasks being repeatedly performed when the website development process is on.\n" +
                "The following are some more points that shall help you understand the language better:\n" +
                "\t•\tPHP is an open source language and fast prototyping language.\n" +
                "\t•\tThis language is compatible with UNIX based OS as well as Windows OS.\n" +
                "\t•\tSome industries where PHP is mostly used include startup businesses, advertising apps, and small software organizations as well as media agencies.\n" +
                "\t•\tThe language can be embedded in HTML directly.";
        technologyData2.technologyImageLarge = R.mipmap.php_1920_1080;
        listTechnologyData.add(technologyData2);

        TechnologyUsedData technologyData3 = new TechnologyUsedData();
        technologyData3.technologyName = "MYSQL";
        technologyData3.technologyDescription = "MySQL is a full-featured relational database management system (RDBMS) that competes with the likes of Oracle DB and Microsoft’s SQL Server. MySQL is sponsored by the Swedish company MySQL AB, which is owned by Oracle Corp. However, the MySQL source code is freely available because it was originally developed as freeware. MySQL is written in C and C++ and is compatible with all major operating systems.\n" +
                "\n" +
                "MySQL was a free-software database engine originally developed and first released in 1995. MySQL is named after My, the daughter Michael Widenius, of one of the product’s originators. It was originally produced under the GNU General Public License, in which source code is made freely available.\n" +
                "\n" +
                "MySQL is very popular for Web-hosting applications because of its plethora of Web-optimized features like HTML data types, and because it's available for free. It is part of the Linux, Apache, MySQL, PHP (LAMP) architecture, a combination of platforms that is frequently used to deliver and support advanced Web applications. MySQL runs the back-end databases of some famous websites, including Wikipedia, Google and Facebook- a testament to its stability and robustness despite its decentralized, free-for-all philosophy.\n" +
                "\n" +
                "MySQL was originally owned by Sun Microsystems; when the company was purchased by Oracle Corp. in 2010, MySQL was part of the package. Although MySQL is technically considered a competitor of Oracle DB, Oracle DB is mainly used by large enterprises, while MySQL is used by smaller, more Web-oriented databases. In addition, MySQL differs from Oracle's product because it's in the public domain.";
        technologyData3.technologyImageLarge = R.mipmap.mysql_1243_861;
        listTechnologyData.add(technologyData3);

        TechnologyUsedData technologyData4 = new TechnologyUsedData();
        technologyData4.technologyName = "ANDROID";
        technologyData4.technologyDescription = "Android is an open source and Linux-based operating system for mobile devices such as smartphones and tablet computers. Android was developed by the Open Handset Alliance, led by Google, and other companies.\n\nAndroid is an open source and Linux-based Operating System for mobile devices such as smartphones and tablet computers. Android was developed by the Open Handset Alliance, led by Google, and other companies.\n\n" +
                "Android offers a unified approach to application development for mobile devices which means developers need only develop for Android, and their applications should be able to run on different devices powered by Android.";
        technologyData4.technologyImageLarge = R.mipmap.android_1920x1080;
        listTechnologyData.add(technologyData4);


        TechnologyUsedData technologyData5 = new TechnologyUsedData();
        technologyData5.technologyName = "iOS";
        technologyData5.technologyDescription = "Objective C is a general-purpose, object-oriented programming language that adds Smalltalk-style messaging to the C programming language. It was the main programming language used by Apple for the OS X and iOS operating systems, and their respective application programming interfaces (APIs): Cocoa and Cocoa Touch prior to the introduction of Swift.\n\n" +
                "Objective-C implementations use a thin runtime system written in C, which adds little to the size of the application. In contrast, most object-oriented systems at the time that it was created used large virtual machine runtimes. Programs written in Objective-C tend to be not much larger than the size of their code and that of the libraries (which generally do not need to be included in the software distribution), " +
                "in contrast to Smalltalk systems where a large amount of memory was used just to open a window. Objective-C applications tend to be larger than similar C or C++ applications because Objective-C dynamic typing does not allow methods to be stripped or inlined." +
                " Since the programmer has such freedom to delegate, forward calls, build selectors on the fly and pass them to the runtime system, the Objective-C compiler cannot assume it is safe to remove unused methods or to inline calls.\n\nA common criticism is that Objective-C does not have language support for namespaces. Instead, programmers are forced to add prefixes to their class names, which are traditionally shorter than namespace names and thus more prone to collisions. " +
                "As of 2007, all Mac OS X classes and functions in the Cocoa programming environment are prefixed with 'NS' (e.g. NSObject, NSButton) to identify them as belonging to the Mac OS X or iOS core; the 'NS' derives from the names of the classes as defined during the development of NeXTSTEP.\n\nSince Objective-C is a strict superset of C, it does not treat C primitive types as first-class objects.";
        technologyData5.technologyImageLarge = R.mipmap.objective_c_900_600;
        listTechnologyData.add(technologyData5);


    }

    private void createIndustryData() {
        //        first record
        IndustryData industryData1 = new IndustryData();
        ArrayList<IndustryData> arrayDataList1 = new ArrayList<>();

        industryData1.categoryName = "Communications";
        industryData1.categoryImage = R.mipmap.communication;
        industryData1.categoryImageLarge = R.mipmap.communication_large;
        industryData1.categoryDetail = "Revolutionize the customer experience, build trust with partners, and network more effectively with our cutting-edge communications technology solutions.";

        IndustryData industryArray = new IndustryData();
        industryArray.industryTitle = "* VOIP Mobile Platform for Voice and Video Calling:";
        industryArray.industryDescription = "Even when employees, customers, and investors are thousands of miles away, you can stay connected, allowing you to make time-critical decisions when it counts most.";
        arrayDataList1.add(industryArray);

        IndustryData industryArray1 = new IndustryData();
        industryArray1.industryTitle = "* SIP Trunking Calling Systems:";
        industryArray1.industryDescription = "Create, collaborate, and innovate with partners and personnel through our cloud-based infrastructures that guarantee easy and effective communication.";
        arrayDataList1.add(industryArray1);

        IndustryData industryArray2 = new IndustryData();
        industryArray2.industryTitle = "* Billing and Distribution:";
        industryArray2.industryDescription = "Take charge of billing and distribution analysis with high-end software systems maintained by our expert team of IT professionals.";
        arrayDataList1.add(industryArray2);

        IndustryData industryArray3 = new IndustryData();
        industryArray3.industryTitle = "* Call Center Support:";
        industryArray3.industryDescription = "Track call center trends and optimize customer relationships, creating customer loyalty and cementing your excellence in the industry.";
        arrayDataList1.add(industryArray3);

        industryData1.consultingNSolution = arrayDataList1;
        listIndustryData.add(industryData1);

        //  Second record
        IndustryData industryData2 = new IndustryData();
        ArrayList<IndustryData> arrayDataList2 = new ArrayList<>();

        industryData2.categoryName = "Education";
        industryData2.categoryImage = R.mipmap.education;
        industryData2.categoryImageLarge = R.mipmap.education_large;
        industryData2.categoryDetail = "Create stronger and better student learning environments, and push your academic vision into the digital age through our innovative software approaches.";

        IndustryData industryArray11 = new IndustryData();
        industryArray11.industryTitle = "* Platform for Schools, Teachers and Learning Habits:";
        industryArray11.industryDescription = "Instill an excitement for learning in students by tapping into our unique apps that gamify the learning process, making it fun, exciting, and efficient for pupils of all ages.";
        arrayDataList2.add(industryArray11);

        IndustryData industryArray12 = new IndustryData();
        industryArray12.industryTitle = "* Online Video, PDF content Distribution:";
        industryArray12.industryDescription = "Managing the digital aspects of e-learning, but we make it easier with a host of solutions for video and content distribution and integration.";
        arrayDataList2.add(industryArray12);

        IndustryData industryArray13 = new IndustryData();
        industryArray13.industryTitle = "* Book Readers on Mobile, Desktop and Web:";
        industryArray13.industryDescription = "Adopting cloud-based book reading technologies is a reliable means to enhance the learning experience, and our mobile, desktop, and web portals will assure that your educational program is ahead of the crowd.";
        arrayDataList2.add(industryArray13);

        industryData2.consultingNSolution = arrayDataList2;
        listIndustryData.add(industryData2);


        //  Third recoerd
        IndustryData industryData3 = new IndustryData();
        ArrayList<IndustryData> arrayDataList3 = new ArrayList<>();

        industryData3.categoryName = "Media Industry";
        industryData3.categoryImage = R.mipmap.media;
        industryData3.categoryImageLarge = R.mipmap.media_large;
        industryData3.categoryDetail = "Enjoy enhanced media and print asset management and enterprise content management by taking advantage of our world-class industry expertise.";

        IndustryData industryArray21 = new IndustryData();
        industryArray21.industryTitle = "* Online, Desktop and Mobile Platforms to manage   and Distribute Digital Content through Cloud:";
        industryArray21.industryDescription = "Digital content is all the rage in today’s world of laptops, tablets, and mobile phones, and it is essential that you keep the pace with the most recent trends by meeting consumer needs through stable IT infrastructures.";
        arrayDataList3.add(industryArray21);

        IndustryData industryArray22 = new IndustryData();
        industryArray22.industryTitle = "* Book Readers on Mobile, Desktop and Web:";
        industryArray22.industryDescription = "Adopting cloud-based book reading technologies is a reliable means to reach a wide audience, and our mobile, desktop, and web portals will assure that your media assets is ahead of the crowd.";
        arrayDataList3.add(industryArray22);

        IndustryData industryArray23 = new IndustryData();
        industryArray23.industryTitle = "* ERP solution for Print Industry for Manufacturing   Units:";
        industryArray23.industryDescription = "Unlock the power of our business management software suites, creating an integrated approach to your business operations and manufacturing.";
        arrayDataList3.add(industryArray23);

        IndustryData industryArray24 = new IndustryData();
        industryArray24.industryTitle = "* PAAS for Online e-commerce, Printing and Order   of Magnetic and Business Cards:";
        industryArray24.industryDescription = "Test and deploy e-commerce and printing applications and services through our best-of-breed cloud-computing platforms, giving you the speed that is needed to outrace the clock and match consumer desires and expectations.";
        arrayDataList3.add(industryArray24);

        industryData3.consultingNSolution = arrayDataList3;
        listIndustryData.add(industryData3);

        //  Forth record
        IndustryData industryData4 = new IndustryData();
        ArrayList<IndustryData> arrayDataList4 = new ArrayList<>();

        industryData4.categoryName = "Human Resource & Staffing";
        industryData4.categoryImage = R.mipmap.humanresource;
        industryData4.categoryImageLarge = R.mipmap.human_large;
        industryData4.categoryDetail = "Develop a world-class team of human resources and staff members with the confidence that our sophisticated management systems will get you there.";

        IndustryData industryArray31 = new IndustryData();
        industryArray31.industryTitle = "* 360 Degree Feedback System:";
        industryArray31.industryDescription = "Dramatically enhance employee task execution and goal-completion through our unique 360 degree feedback systems that pave the way for company success.";
        arrayDataList4.add(industryArray31);

        IndustryData industryArray32 = new IndustryData();
        industryArray32.industryTitle = "* Online working platforms:";
        industryArray32.industryDescription = "Deploying workstations in the cloud means that employees can accomplish objectives anytime, anywhere.";
        arrayDataList4.add(industryArray32);

        IndustryData industryArray33 = new IndustryData();
        industryArray33.industryTitle = "* Staffing Service and Support:";
        industryArray33.industryDescription = "Navigating the maze of emerging technological systems and processes will require superior IT support, and that is what we are here for.";
        arrayDataList4.add(industryArray33);

        IndustryData industryArray34 = new IndustryData();
        industryArray34.industryTitle = "* Project Management and Tracking System:";
        industryArray34.industryDescription = "Maximizing productivity in the workplace is a certain way to outstrip the competition, and leveraging our project management and tracking systems is a path that will get you there.";
        arrayDataList4.add(industryArray34);

        industryData4.consultingNSolution = arrayDataList4;
        listIndustryData.add(industryData4);


        //  Fifth record
        IndustryData industryData5 = new IndustryData();
        ArrayList<IndustryData> arrayDataList5 = new ArrayList<>();

        industryData5.categoryName = "Retail";
        industryData5.categoryImage = R.mipmap.retail_large;
        industryData5.categoryImageLarge = R.mipmap.retail_large;
        industryData5.categoryDetail = "Make use of our software-defined tools to lead the retailing market, cultivate consumer loyalty, and maintain a superior value chain.";

        IndustryData industryArray41 = new IndustryData();
        industryArray41.industryTitle = "* Billing and Invoicing at Retail Counter:";
        industryArray41.industryDescription = "Produce invoices at the retail counter immediately and on-the-spot, smoothing over the entire customer experience and strengthening customer loyalty and retention.";
        arrayDataList5.add(industryArray41);

        IndustryData industryArray42 = new IndustryData();
        industryArray42.industryTitle = "* Inventory System:";
        industryArray42.industryDescription = "Make use of a sophisticated inventory database, tracking prices, quotes, and product lists, thereby driving the creation of well-informed business strategies.";
        arrayDataList5.add(industryArray42);

        IndustryData industryArray43 = new IndustryData();
        industryArray43.industryTitle = "* Manufacturing and Distribution of Jewelry   Industry:";
        industryArray43.industryDescription = "Jewelry manufacturing and distribution is computationally intensive and resource heavy, but we are here to help you get on the fast-track towards optimal management of your jewelry operations.";
        arrayDataList5.add(industryArray43);

        industryData5.consultingNSolution = arrayDataList5;
        listIndustryData.add(industryData5);


        //  Sixth record
        IndustryData industryData6 = new IndustryData();
        ArrayList<IndustryData> arrayDataList6 = new ArrayList<>();

        industryData6.categoryName = "Technology";
        industryData6.categoryImage = R.mipmap.technology;
        industryData6.categoryImageLarge = R.mipmap.technology_large;
        industryData6.categoryDetail = "Keep up to speed with the latest in software and IT architectures and take your business and engineering endeavors to a dominating position in the industry.";

        IndustryData industryArray51 = new IndustryData();
        industryArray51.industryTitle = "* Consulting:";
        industryArray51.industryDescription = "Our extensive experience in solving software management, integration, and development issues will give you the exceptional consulting your company requires to forge ahead towards realizing its vision.";
        arrayDataList6.add(industryArray51);

        IndustryData industryArray52 = new IndustryData();
        industryArray52.industryTitle = "* Analysis:";
        industryArray52.industryDescription = "We will take the lead in the IT development needs of your particular business, providing you with the meaningful data analysis and research that allow you to outpace your competition.";
        arrayDataList6.add(industryArray52);

        IndustryData industryArray53 = new IndustryData();
        industryArray53.industryTitle = "* Development:";
        industryArray53.industryDescription = "Our experienced team of developers will maximize your infrastructure’s computing and analytic abilities, securing a safe and productive environment for your organization’s developmental operations.";
        arrayDataList6.add(industryArray53);

        IndustryData industryArray54 = new IndustryData();
        industryArray54.industryTitle = "* Testing:";
        industryArray54.industryDescription = "We use advanced cloud-based platforms to test your applications and infrastructures in a fast and reliable manner.";
        arrayDataList6.add(industryArray54);

        IndustryData industryArray55 = new IndustryData();
        industryArray55.industryTitle = "* Deployment, Maintenance, and Support:";
        industryArray55.industryDescription = "Move your product from initial concept and into the hands of consumers through our comprehensive experience and knowledge that will allow us to deploy and maintain your software systems, and we will always be there for you if and when they break down.";
        arrayDataList6.add(industryArray55);

        IndustryData industryArray56 = new IndustryData();
        industryArray56.industryTitle = "* Promotion through Channel Partners:";
        industryArray56.industryDescription = "The importance of meaningful communication with your channel partners cannot be overemphasized, as this forms the basic building block for honing clear and definable marketing and sales objectives. We are here to support you every step of the way.";
        arrayDataList6.add(industryArray56);

        industryData6.consultingNSolution = arrayDataList6;
        listIndustryData.add(industryData6);

    }

    private void createProjectData() {

        PortfolioData productData1 = new PortfolioData();
        productData1.projectId = "ProjectErandooLatest";
        productData1.projectName = "Erandoo";
        productData1.projectImage = R.mipmap.erandoo;
        productData1.projectOverview = "Erandoo is an online and mobile marketplace that connects individuals who have time to spare with people who have projects they need to get done. This platform introduces Doers with Posters. Users can also easily transit from Poster to Doer depending on their availability and needs. Projects are posted on Erandoo, Doers and Posters make a connection, and the work is completed to everyone’s satisfaction.";
        productData1.projectWeblink = "www.erandoo.aryahelp.com";
        productData1.projectCategory = "Latest";
        productData1.projectPlateform = "iOS, Android, Web app";
        productData1.projectTechnology = "* MQTT - Chat Server’s \n* Java Server Side component\n* Phone no. text verificationss\n* MQTT notifications\n* Php in YII framework on thin Web Client\n* Native Xcode 6 for iOS\n* Eclipse for Android\n* Cassandra on chat history\n* Amazon cloud\n* MySQL\n* Sterling - background check\n* Propay payment gateway with e-wallet management\n* Nexmo - SMS delivery\n* Web socket through JSON";
        productData1.projectAchievement = "* MQTT Servers which are used by the companies like Facebook\n* Data Analytic Tools to serve Reports";
        productData1.projectChallenges = "Non- defined requirements with vast scope. Moreover, client does not have domain knowledge. Integration of third party tools of Sterling and Propay as their systems are not matured to handle the big platform like Erandoo. Heavy data usage, maintaining battery life, working in off line mode. \n Non- defined requirements with vast scope. Moreover, client does not have domain knowledge. Integration of third party tools of Sterling and Propay as their systems are not matured to handle the big platform like Erandoo. Heavy data usage, maintaining battery life, working in off line mode.";
        productData1.projectIOSLink = "";
        productData1.projectAndroidLink = "";
        productData1.projectIndstryCategory = "Technology";
        listProduct.add(productData1);

        PortfolioData productData2 = new PortfolioData();
        productData2.projectId = "ProjectIntegritycubesLatest";
        productData2.projectName = "Integrity cubes";
        productData2.projectImage = R.mipmap.integrity_cubes;
        productData2.projectOverview = "The Cube is a social recognition app built exclusively for the Integrity Staffing team. It’s a place for all of us to come together to share, acknowledge and read about how our core values manifest themselves in gestures, big and small, across the organization.";
        productData2.projectWeblink = "www.thecube.integritystaffing.com";
        productData2.projectCategory = "Latest";
        productData2.projectPlateform = "Web and iOS app";
        productData2.projectTechnology = "* Native iOS app in Xcode\n* Php\n* My Sql\n* Jquery / JavaScript\n* Css,html\n* Ajax\n* Amazon web server\n* Yii framework\n* Google pie chart API’s\n* Mobile application web services";
        productData2.projectAchievement = "* Finding winner by performing calculation of likes & comments of 300-400 staff members on a single cube.\n* Maintaining Hierarchy of members & their reports.";
        productData2.projectChallenges = "* Lots of slowing and freezing/setting permissions for multiple views according to users.\n* Management of multiple user comments.";
        productData2.projectIOSLink = "https://itunes.apple.com/us/app/integrity-the-cube/id897464602?mt=8";
        productData2.projectAndroidLink = "";
        productData2.projectIndstryCategory = "Retail";
        listProduct.add(productData2);

        PortfolioData productData3 = new PortfolioData();
        productData3.projectId = "ProjectTime'nTask";
        productData3.projectName = "Time'nTask";
        productData3.projectImage = R.mipmap.time_n_task;
        productData3.projectOverview = "Time'nTask: Real-Time.Work.Money: Productivity and Efficiency Tool, is a cloud-based time tracking, location tracking, task management and chatting app that allows employees to check in and out time inside the office during the meetings, outside the office or job site with their phones or tablets.";
        productData3.projectWeblink = "www.timentask.com";
        productData3.projectCategory = "Latest";
        productData3.projectPlateform = "Web, iOS, Android app";
        productData3.projectTechnology = "* Java\n* MQTT\n* Amazon cloud\n* MySQL\n* Php\n* API Integration\n* Collaboration Component\n - Mobile to Mobile \n - Mobile to Website \n - Mobile to Desktop\n* Chat Component\n* Push Notification\n* Single Server Component";
        productData3.projectAchievement = "";
        productData3.projectChallenges = "* Technically Complicated\n* Implemented single component work on multiple platform";
        productData3.projectIOSLink = "https://itunes.apple.com/us/app/timentask/id1080540342?ls=1&mt=8";
        productData3.projectAndroidLink = "https://play.google.com/store/apps/details?id=timentask.app&hl=en";
        productData3.projectIndstryCategory = "Retail";
        listProduct.add(productData3);

        PortfolioData productData4 = new PortfolioData();
        productData4.projectId = "ProjectSpliro";
        productData4.projectName = "Spliro";
        productData4.projectImage = R.mipmap.spliro;
        productData4.projectOverview = "Spliro is a one of a kind app that allows you to share and save, getting the best prices in the market. Buying things one at a time can be hefty and whether you’re a college student, working adult or a homemaker saving money never hurts. Right? We take the concept of buying things in bulk in order to save money, and provide you with the platform to divide those purchases so you can get the deals without the clutter. Say goodbye to having all those unwanted rolls of paper towels sitting around the house, or feeling like you have to finish that discount extra large pizza all by yourself. Spliro is the solution to it all.";
        productData4.projectWeblink = "www.spliro.com";
        productData4.projectCategory = "Latest";
        productData4.projectPlateform = "iOS, Android app";
        productData4.projectTechnology = "* Object C\n* Push notification-internal/external\n* Core data\n* Data sharing\n - One to one\n - One to many\n* Java\n* MQTT- Chat Server’s\n* Java Server Side component";
        productData4.projectAchievement = "MQTT Servers which are used by the companies like Facebook";
        productData4.projectChallenges = "";
        productData4.projectIOSLink = "https://itunes.apple.com/us/app/spliro/id1070084141?mt=8";
        productData4.projectAndroidLink = "https://play.google.com/store/apps/details?id=arya.spliro";
        productData4.projectIndstryCategory = "Retail";
        listProduct.add(productData4);

        PortfolioData productData5 = new PortfolioData();
        productData5.projectId = "ProjectW2P";
        productData5.projectName = "W2P";
        productData5.projectImage = R.mipmap.w2p;
        productData5.projectOverview = "B2B, B2C ERP solution for printing companies doing business card , letter head, magnets, etc. ERP supports, designers as one of the stack holders to publish their design through online tool for designing, Manufacturing Section for sheet procurement, machine setting, distribution channel through small vendors, and customer e-commerce for selling. This complete system supports, different third party shipping tools, currency conversation and creative designer tools.";
        productData5.projectWeblink = "www.3-mag.net/Application/HomeCategoryManagement.aspx";
        productData5.projectCategory = "Latest";
        productData5.projectPlateform = "It is a Web based platform";
        productData5.projectTechnology = "* Asp .Net framework 4.0\n* C#\n* SQL Server 2008\n* Ajax\n* JavaScript/ Jquery\n* Amazon Server\n* Flex";
        productData5.projectAchievement = "Complex system for sheet calculation, manufacturing units, integration of different servers to make it work.\n Complex online designer tools.\n Completely customized CMS.";
        productData5.projectChallenges = "System is too vast to change anything and to do impact analysis of change request";
        productData5.projectIOSLink = "";
        productData5.projectAndroidLink = "";
        productData5.projectIndstryCategory = "Retail";
        listProduct.add(productData5);


        PortfolioData productData6 = new PortfolioData();
        productData6.projectId = "ProjectCoachviewer";
        productData6.projectName = "Coach viewer";
        productData6.projectImage = R.mipmap.coach_viewer;
        productData6.projectOverview = "Coach Viewer is a must have application for football coaches. It is the official SoccerTutor.com app where you can access free football coaching drills and stream training videos. Existing SoccerTutor.com customers can also download their eBooks and stream videos from the ‘My Library’ area. The app targets every level of football, from youth development to professional, so you can learn from some of the best coaches across the world. It is B2C model for providing training and related products through Web interface.";
        productData6.projectWeblink = "www.soccertutor.com";
        productData6.projectCategory = "Education";
        productData6.projectPlateform = "iOS, Android, Web app";
        productData6.projectTechnology = "* .Net \n* API’s through web socket and JSON \n* Flex Tool \n* Xcode 5.5 \n* Eclipse \n* Tools to convert pdf files to Flex \n* SQL Server 2008";
        productData6.projectAchievement = "* Successful and timely delivery of readers on all platforms.\n* Sync of data between all the platforms smoothly managing bookmarks and other important features of the readers* Video sync and custom players\"";
        productData6.projectChallenges = "* Language support with special characters\n* Video streaming and video’s play in Offline mode\n* Security of data with encryption and decryption without completely encrypting and decrypting videos and pdf’ files due to memory limitations on mobile devices\n* Memory management of bigger size books with lot of media content\n* API data transfer security";
        productData6.projectIOSLink = "https://itunes.apple.com/us/app/coach-viewer/id692671007?ls=1&mt=8";
        productData6.projectAndroidLink = "https://play.google.com/store/apps/details?id=soccer.tutor.reader&hl=en";
        productData6.projectIndstryCategory = "Education";
        listProduct.add(productData6);


        PortfolioData productData7 = new PortfolioData();
        productData7.projectId = "ProjectAbfutbol";
        productData7.projectName = "Abfutbol";
        productData7.projectImage = R.mipmap.abfutbol;
        productData7.projectOverview = "Abfutbol Viewer is an application for coaches and trainers of football. With it you can download the technical journals and books Abfutbol. You can also access free exercises and training videos. With this tool we can learn all the secrets of football training today, recommended to those who want to be professional coaches.";
        productData7.projectWeblink = "";
        productData7.projectCategory = "Education";
        productData7.projectPlateform = "iOS, Android app";
        productData7.projectTechnology = "* .Net\n* API’s through web socket and JSON\n* Flex Tool\n* Xcode 5.5\n* Eclipse\n* Tools to convert pdf files to Flex\n* SQL Server 2008";
        productData7.projectAchievement = "* Successful and timely delivery of readers on all platforms.\n* Sync of data between all the platforms, smoothly managing bookmarks and other important features of the readers\n* Video sync and custom players";
        productData7.projectChallenges = "* Language support with special characters\n* Video streaming and video’s play in Offline mode\n* Security of data with encryption and decryption without completely encrypting and decrypting videos and pdf’ files due to memory limitations on mobile devices\n* Memory management of bigger size books with lot of media content\n* API data transfer security";
        productData7.projectIOSLink = "https://itunes.apple.com/us/app/abfutbol/id751219898?mt=8";
        productData7.projectAndroidLink = "https://play.google.com/store/apps/details?id=abfutbol.reader&hl=en";
        productData7.projectIndstryCategory = "Education";
        listProduct.add(productData7);


        PortfolioData productData8 = new PortfolioData();
        productData8.projectId = "ProjectArcadiaprep";
        productData8.projectName = "Arcadia prep";
        productData8.projectImage = R.mipmap.arcadiaprep;
        productData8.projectOverview = "The Arcadia GRE Course is a comprehensive GRE course conveniently available on the iPad and iPhone. Based on materials created by Nova Press, it includes hundreds of GRE problems. It is a self-paced course, and there is help available if you are stuck. Now users have everything they need to prepare for entering the graduate school of their dream, in their pocket, on the go.";
        productData8.projectWeblink = "";
        productData8.projectCategory = "Education";
        productData8.projectPlateform = "iOS app";
        productData8.projectTechnology = "Local database iOS app";
        productData8.projectAchievement = "";
        productData8.projectChallenges = "";
        productData8.projectIOSLink = "https://itunes.apple.com/in/app/gre-+/id481932398?mt=8";
        productData8.projectAndroidLink = "";
        productData8.projectIndstryCategory = "Education";
        listProduct.add(productData8);


        PortfolioData productData9 = new PortfolioData();
        productData9.projectId = "ProjectGorillamagnets";
        productData9.projectName = "Gorilla magnets";
        productData9.projectImage = R.mipmap.gorilla_magnets;
        productData9.projectOverview = "Printers, designers, advertising agencies and anyone related to advertising and promotional articles can register to get full access to our special prices, discounts and voordelen.Deze are strictly reserved for resellers and distributors. Gorillamagnets offers its customers selected printed advertising products that will boost customer interaction will strengthen the sales and performance will improve plus the ability of each company.";
        productData9.projectWeblink = "www.gorillamagnets.nl";
        productData9.projectCategory = "Retail";
        productData9.projectPlateform = "Web app";
        productData9.projectTechnology = "* Asp .Net Framework\n* C#\n* SQL Server 2008\n* Ajax\n* Javacript/Jquery\n* Amazon Server\n* Shared server – Go Daddy\n* UPS Integration for shipping\n* WCF Authentication";
        productData9.projectAchievement = "Complex system for sheet calculation, manufacturing units, integration of different servers to make it work. Complex online designer tools. Completely customized CMS.";
        productData9.projectChallenges = "System is too vast to change anything and to do impact analysis of change request.";
        productData9.projectIOSLink = "";
        productData9.projectAndroidLink = "";
        productData9.projectIndstryCategory = "Retail";
        listProduct.add(productData9);


        PortfolioData productData10 = new PortfolioData();
        productData10.projectId = "ProjectMousetoprint";
        productData10.projectName = "Mouse to print";
        productData10.projectImage = R.mipmap.mouse_to_print;
        productData10.projectOverview = "B2B, B2C ERP solution for printing companies doing business card , letter head, magnets, etc. ERP supports, designers as one of the stack holders to publish their design through online tool for designing, Manufacturing Section for sheet procurement, machine setting, distribution channel through small vendors, and customer e-commerce for selling. This complete system supports, different third party shipping tools, currency conversation and creative designer tools.";
        productData10.projectWeblink = "www.mousetoprint.com";
        productData10.projectCategory = "Retail";
        productData10.projectPlateform = "It is a Web based platform";
        productData10.projectTechnology = "* Asp .Net framework 4.0\n* C#\n* SQL Server 2008\n* Ajax\n* JavaScript/ Jquery\n* Amazon Server\n* Flex";
        productData10.projectAchievement = "Complex system for sheet calculation, manufacturing units, integration of different servers to make it work. Complex online designer tools. Completely customized CMS.";
        productData10.projectChallenges = "System is too vast to change anything and to do impact analysis of change request.";
        productData10.projectIOSLink = "";
        productData10.projectAndroidLink = "";
        productData10.projectIndstryCategory = "Retail";
        listProduct.add(productData10);


        PortfolioData productData11 = new PortfolioData();
        productData11.projectId = "Project_e_cigarette";
        productData11.projectName = "e-cigarette";
        productData11.projectImage = R.mipmap.e_cigarette;
        productData11.projectOverview = "Electronic Cigarettes, Inc. is the top retailer of name brand electronic cigarettes and vapor supplies. We carry the widest selection of products from all the top manufacturers including kits, batteries, tanks, atomizers, mods, wraps, juices and more - we have it all at the lowest prices on the market! Inventory is updated daily and all items ship from our 20,000 sq ft warehouse located in Upstate NY.";
        productData11.projectWeblink = "www.electroniccigarettesinc.com";
        productData11.projectCategory = "Retail";
        productData11.projectPlateform = "It is a Web based platform";
        productData11.projectTechnology = "* Opencart magento";
        productData11.projectAchievement = "";
        productData11.projectChallenges = "";
        productData11.projectIOSLink = "";
        productData11.projectAndroidLink = "";
        productData11.projectIndstryCategory = "Retail";
        listProduct.add(productData11);


        PortfolioData productData12 = new PortfolioData();
        productData12.projectId = "ProjectTravelprice";
        productData12.projectName = "Travel price";
        productData12.projectImage = R.mipmap.travel_price;
        productData12.projectOverview = "MyTravel Price is the only travel portal in India which facilitates travelling in groups. The more number of people in groups, the less is the price of travel per person. Find here best deals for different destinations across the world, Book them completely online, and also check how many have booked that package already.";
        productData12.projectWeblink = "www.electroniccigarettesinc.com";
        productData12.projectCategory = "Retail";
        productData12.projectPlateform = "It is a Web based platform";
        productData12.projectTechnology = "* Asp .Net framework\n* SQL Server";
        productData12.projectAchievement = "";
        productData12.projectChallenges = "";
        productData12.projectIOSLink = "";
        productData12.projectAndroidLink = "";
        productData12.projectIndstryCategory = "Retail";
        listProduct.add(productData12);


        PortfolioData productData13 = new PortfolioData();
        productData13.projectId = "ProjectLadada";
        productData13.projectName = "Ladada";
        productData13.projectImage = R.mipmap.ladada;
        productData13.projectOverview = "It is a personalized fashion service for women. Each month we fill your box with the latest fashion for up and coming designer brands. It finds your exact style and fit, save you time and money and only pay for what you keep!";
        productData13.projectWeblink = "www.electroniccigarettesinc.com";
        productData13.projectCategory = "Retail";
        productData13.projectPlateform = " Web app";
        productData13.projectTechnology = "* Opencart with MYSQL";
        productData13.projectAchievement = "";
        productData13.projectChallenges = "";
        productData13.projectIOSLink = "";
        productData13.projectAndroidLink = "";
        productData13.projectIndstryCategory = "Retail";
        listProduct.add(productData13);


        PortfolioData productData14 = new PortfolioData();
        productData14.projectId = "ProjectWorkoutbuddyx";
        productData14.projectName = "workout buddyx";
        productData14.projectImage = R.mipmap.workoutbuddyx;
        productData14.projectOverview = "Workout Buddy X is the first and only online service that fully integrates technology, science, support, social media, tracking with personal training. We call it the 3 T's and 3 S's. The result is a revolutionary fitness platform that delivers highly effective, interactive, and socially rewarding weight loss experience! In reality, losing weight is hard, especially if you are going it alone. This is why we go a few steps beyond in every aspect to provide the most effective, and \"ACHIEVABLE\" weight loss solution for people of ALL FITNESS LEVELS.";
        productData14.projectWeblink = "www.workoutbuddyx.com";
        productData14.projectCategory = "Health & Fitness";
        productData14.projectPlateform = " Web app";
        productData14.projectTechnology = "* Php with Zend framework \n * SQL Server \n * Authorize. Net payment gateway";
        productData14.projectAchievement = "Data Analytic Tools to serve Reports";
        productData14.projectChallenges = "Maintaining heavy data with videos, text and images.";
        productData14.projectIOSLink = "";
        productData14.projectAndroidLink = "";
        productData14.projectIndstryCategory = "Human Resource & Staffing";
        listProduct.add(productData14);

        PortfolioData productData15 = new PortfolioData();
        productData15.projectId = "ProjectSTC";
        productData15.projectName = "STC";
        productData15.projectImage = R.mipmap.stc;
        productData15.projectOverview = "Sports Timing Calculator is a multi-function stopwatch capable of creating and storing splits from every event and exporting and sending them to your computer for review once the event is done.\n" +
                "STC also functions as a base 60 calculator capable of adding, subtracting, multiplying, and dividing time to calculate anything from workout legnth, to breaking down a race into even splits to keep your race on pace.";
        productData15.projectWeblink = "";
        productData15.projectCategory = "Health & Fitness";
        productData15.projectPlateform = "iOS and Android app";
        productData15.projectTechnology = "* Native iOS app in Xcode \n* Native Android with Eclipse \n* MySQL";
        productData15.projectAchievement = "We have successfully created a multi functional stopwatch and calculator.";
        productData15.projectChallenges = "Its really challenging task to calculate the time & performing addtion, subtraction , multiplication & division from workout length.";
        productData15.projectIOSLink = "";
        productData15.projectAndroidLink = "";
        productData15.projectIndstryCategory = "Human Resource & Staffing";
        listProduct.add(productData15);

        PortfolioData productData16 = new PortfolioData();
        productData16.projectId = "ProjectSupremesystem";
        productData16.projectName = "Supreme system";
        productData16.projectImage = R.mipmap.supreme_system;
        productData16.projectOverview = "Supreme Systems provides expert IT support in Birmingham and the West Midlands. Our focus is on providing our clients with a unique IT support company experience bespoke to them. We strive to become an “extension of your team” providing IT services in Birmingham and the West Midlands that will make your organization more effective.";
        productData16.projectWeblink = "www.supremesystems.co.uk";
        productData16.projectCategory = "Social Model";
        productData16.projectPlateform = "Web app";
        productData16.projectTechnology = "* Joomla components \n* PHP \n * My SQL";
        productData16.projectAchievement = "";
        productData16.projectChallenges = "";
        productData16.projectIOSLink = "";
        productData16.projectAndroidLink = "";
        productData16.projectIndstryCategory = "Technology";
        listProduct.add(productData16);

        PortfolioData productData17 = new PortfolioData();
        productData17.projectId = "ProjectSavemysystem";
        productData17.projectName = "Save my system";
        productData17.projectImage = R.mipmap.save_my_system;
        productData17.projectOverview = "This is a fantastic job search service for local and international job seekers looking for a new career at home or abroad. We have designed this easy to use free job search application that will in turn help find jobs opportunities through 15 popular destinations and the countries include: UK, France, USA, UAE, Germany, Mexico, India, Pakistan, China, New Zealand, Saudi Arabia, Switzerland, Australia, Canada and Netherland.";
        productData17.projectWeblink = "www.savemysystem.co.uk";
        productData17.projectCategory = "Social Model";
        productData17.projectPlateform = "Web app";
        productData17.projectTechnology = "* Joomla components \n* PHP \n * My SQL";
        productData17.projectAchievement = "";
        productData17.projectChallenges = "";
        productData17.projectIOSLink = "";
        productData17.projectAndroidLink = "";
        productData17.projectIndstryCategory = "Technology";
        listProduct.add(productData17);

    /*    PortfolioData productData18 = new PortfolioData();
        productData18.projectId = "ProjectErandooSocialModel";
        productData18.projectName = "Erandoo";
        productData18.projectImage = R.mipmap.erandoo;
        productData18.projectOverview = "Erandoo is an online and mobile marketplace that connects individuals who have time to spare with people who have projects they need to get done. This platform introduces Doers with Posters. Users can also easily transit from Poster to Doer depending on their availability and needs. Projects are posted on Erandoo, Doers and Posters make a connection, and the work is completed to everyone’s satisfaction.";
        productData18.projectWeblink = "www.erandoo.aryahelp.com";
        productData18.projectCategory = "Social Model";
        productData18.projectPlateform = "iOS, Android, Web app";
        productData18.projectTechnology = "* MQTT - Chat Server’s \n* Java Server Side component\n* Phone no. text verificationss\n* MQTT notifications\n* Php in YII framework on thin Web Client\n* Native Xcode 6 for iOS\n* Eclipse for Android\n* Cassandra on chat history\n* Amazon cloud\n* MySQL\n* Sterling - background check\n* Propay payment gateway with e-wallet management\n* Nexmo - SMS delivery\n* Web socket through JSON";
        productData18.projectAchievement = "* MQTT Servers which are used by the companies like Facebook\n* Data Analytic Tools to serve Reports";
        productData18.projectChallenges = "Non- defined requirements with vast scope. Moreover, client does not have domain knowledge. Integration of third party tools of Sterling and Propay as their systems are not matured to handle the big platform like Erandoo. Heavy data usage, maintaining battery life, working in off line mode. \\n Non- defined requirements with vast scope. Moreover, client does not have domain knowledge. Integration of third party tools of Sterling and Propay as their systems are not matured to handle the big platform like Erandoo. Heavy data usage, maintaining battery life, working in off line mode.";
        productData18.projectIOSLink = "";
        productData18.projectAndroidLink = "";
        listProduct.add(productData18);*/


        PortfolioData productData19 = new PortfolioData();
        productData19.projectId = "ProjectCollegebasketball";
        productData19.projectName = "College basketball";
        productData19.projectImage = R.mipmap.college_basketball;
        productData19.projectOverview = "College Basketball Scoreboard gives you up to the minute college basketball scores, schedules, news stories & standings for the following conferences: American, ACC, Big 12, Big East, Big Ten, Conference USA, Mountain West, PAC-12 and SEC- plus Top 25 schools. Scores, Schedules and News are displayed by conference. Keep track of your favorite teams and your rivals all at a glance. Real time scores are officially licensed from Sports Direct.";
        productData19.projectWeblink = "";
        productData19.projectCategory = "Sports";
        productData19.projectPlateform = "iOS app";
        productData19.projectTechnology = "* Native iOS app in Xcode \n* Native Android with Eclipse \n* JSON API \n* .Net server";
        productData19.projectAchievement = "Successfully displaying updated news, schedule and scores of teams user selects.";
        productData19.projectChallenges = "Heavy data, maintain lists of college basketball teams.";
        productData19.projectIOSLink = "https://itunes.apple.com/us/app/college-basketball-scoreboard/id397279736?mt=8";
        productData19.projectAndroidLink = "";
        productData19.projectIndstryCategory = "Media Industry";
        listProduct.add(productData19);


        PortfolioData productData20 = new PortfolioData();
        productData20.projectId = "ProjectMFS";
        productData20.projectName = "MFS";
        productData20.projectImage = R.mipmap.mfs;
        productData20.projectOverview = "It provides access to all MFS Survivor Pools and Fantasy Football teams that you are currently participating in for the current NFL season. You will be able to check in on your Survivor Pool standings as well as to make game selections for the current week, up until the Lock Deadline, or for future weeks. For your head-to-head non-in game change fantasy football leagues, you will be able to check the real-time performance of your team as well as player-by-player Matchups against your opponent for the week. You will also be able to use our Score Zone feature to view the other Matchup in your league for any week of the NFL season.";
        productData20.projectWeblink = "www.maximumfantasysports.com";
        productData20.projectCategory = "Sports";
        productData20.projectPlateform = "Android, Web app";
        productData20.projectTechnology = "* Native Android with Eclipse \n* JSON API \n* .Net server";
        productData20.projectAchievement = "Successfully implemented check in on Survivor Pool standings as well as to make game selections for the current week, up until the Lock Deadline, or for future weeks.";
        productData20.projectChallenges = "Heavy data, Memory Managed successfully, maintain lists of Fantasy football teams.";
        productData20.projectIOSLink = "";
        productData20.projectAndroidLink = "https://play.google.com/store/apps/details?id=com.sports.mfs&hl=en";
        productData20.projectIndstryCategory = "Media Industry";
        listProduct.add(productData20);


        PortfolioData productData21 = new PortfolioData();
        productData21.projectId = "ProjectCollegesuperfans";
        productData21.projectName = "College superfans";
        productData21.projectImage = R.mipmap.college_superfans;
        productData21.projectOverview = "College Super Fans app gives you up-to-the-minute college football and basketball scores, news stories, schedules, AP & BCS polls, conference standings, game previews and game recaps for up to 10 of your favorite teams. Check real time scores throughout the game, read previews of upcoming games, and keep track of your favorite college teams all year.";
        productData21.projectWeblink = "";
        productData21.projectCategory = "Sports";
        productData21.projectPlateform = "ios app";
        productData21.projectTechnology = "* Native iOS app in Xcode \n* Native Android with Eclipse \n* JSON API \n* .Net server";
        productData21.projectAchievement = "Successfully displaying updated news, schedule and scores of teams user selects.";
        productData21.projectChallenges = "Heavy data, maintain lists of college basketball teams";
        productData21.projectIOSLink = "https://itunes.apple.com/in/app/college-superfans/id399730635?mt=8";
        productData21.projectAndroidLink = "";
        productData21.projectIndstryCategory = "Media Industry";
        listProduct.add(productData21);


        PortfolioData productData22 = new PortfolioData();
        productData22.projectId = "ProjectTempletower";
        productData22.projectName = "Temple tower";
        productData22.projectImage = R.mipmap.temple_tower;
        productData22.projectOverview = "You have to build the ancient tower as high as possible, placing a block on top of another in a precise way. Try not to unbalance and collapse the tower. Use the power of the sun to help you in this adventure and you will be the next Lord of the Mayan Towers.";
        productData22.projectWeblink = "";
        productData22.projectCategory = "Games";
        productData22.projectPlateform = "ios app";
        productData22.projectTechnology = "* Xcode 5.5";
        productData22.projectAchievement = "";
        productData22.projectChallenges = "";
        productData22.projectIOSLink = "https://itunes.apple.com/us/app/temple-tower/id650465497?mt=8";
        productData22.projectAndroidLink = "";
        productData22.projectIndstryCategory = "Media Industry";

        listProduct.add(productData22);


        PortfolioData productData23 = new PortfolioData();
        productData23.projectId = "ProjectSyeknom";
        productData23.projectName = "Syeknom";
        productData23.projectImage = R.mipmap.syeknom;
        productData23.projectOverview = "A fantastic new board game. This is a strategic battle between 2 players. Move your characters around the island in a strategic manner. Use the hidden land mines that are placed in random places on the island to your advantage in order to outwit your opponent. Use your moveable carrier pad to launch more reinforcements on to the island The first player to remove all of their opponents characters wins the war and therefore gains control of the island of Syeknom.";
        productData23.projectWeblink = "";
        productData23.projectCategory = "Games";
        productData23.projectPlateform = "ios app";
        productData23.projectTechnology = "* Open GL, Leaderboard";
        productData23.projectAchievement = "";
        productData23.projectChallenges = "";
        productData23.projectIOSLink = "https://itunes.apple.com/us/app/battle-of-syeknom/id645811557?mt=8";
        productData23.projectAndroidLink = "";
        productData23.projectIndstryCategory = "Media Industry";
        listProduct.add(productData23);

        PortfolioData productData24 = new PortfolioData();
        productData24.projectId = "ProjectJnihangman";
        productData24.projectName = "Jni hangman";
        productData24.projectImage = R.mipmap.jni_hangman;
        productData24.projectOverview = "The hangman game we all grew up loving! Three new categories for all to enjoy!";
        productData24.projectWeblink = "";
        productData24.projectCategory = "Games";
        productData24.projectPlateform = "ios app";
        productData24.projectTechnology = "* Local database iOS app";
        productData24.projectAchievement = "";
        productData24.projectChallenges = "";
        productData24.projectIOSLink = "https://itunes.apple.com/in/app/jni-hangman/id474190538?mt=8";
        productData24.projectAndroidLink = "";
        productData24.projectIndstryCategory = "Media Industry";
        listProduct.add(productData24);

        PortfolioData productData25 = new PortfolioData();
        productData25.projectId = "ProjectTictactoe";
        productData25.projectName = "Tic tac toe";
        productData25.projectImage = R.mipmap.tic_tac_toe;
        productData25.projectOverview = "The classic game that we all grew up on. Put your logo on this familiar game and give your customers the chance to turn idle time into enjoyment with friends and family.";
        productData25.projectWeblink = "";
        productData25.projectCategory = "Games";
        productData25.projectPlateform = "ios app";
        productData25.projectTechnology = "* Local database iOS app";
        productData25.projectAchievement = "";
        productData25.projectChallenges = "";
        productData25.projectIOSLink = "https://itunes.apple.com/in/app/csgtictactoe/id458586283?mt=8";
        productData25.projectAndroidLink = "";
        productData25.projectIndstryCategory = "Media Industry";
        listProduct.add(productData25);


        PortfolioData productData26 = new PortfolioData();
        productData26.projectId = "ProjectWord2win";
        productData26.projectName = "Word 2 win";
        productData26.projectImage = R.mipmap.word_2_win;
        productData26.projectOverview = "Word2Win is an exciting trivia game played like Hangman. Download words and categories based on a password you set up or just play with our default words!";
        productData26.projectWeblink = "";
        productData26.projectCategory = "Games";
        productData26.projectPlateform = "ios app";
        productData26.projectTechnology = "* Local database iOS app";
        productData26.projectAchievement = "";
        productData26.projectChallenges = "";
        productData26.projectIOSLink = "https://itunes.apple.com/in/app/word2win/id479464907?mt=8";
        productData26.projectAndroidLink = "";
        productData26.projectIndstryCategory = "Media Industry";
        listProduct.add(productData26);

        PortfolioData productData27 = new PortfolioData();
        productData27.projectId = "ProjectPreciouscargo";
        productData27.projectName = "Precious cargo";
        productData27.projectImage = R.mipmap.precious_cargo;
        productData27.projectOverview = "Precious Cargo was created to prevent the senseless deaths of innocent children. The App uses the Bluetooth function in the user’s vehicle. Once paired with the vehicles Bluetooth the alarm with ask if you are traveling with Precious Cargo each time you start your vehicle. (Children, Pets, Perishable items). The App then sends you an alert whenever the vehicle is turned off to remind you about you Precious Cargo. This is to ensure you that you have left nothing behind.";
        productData27.projectWeblink = "";
        productData27.projectCategory = "Productivity";
        productData27.projectPlateform = "ios app";
        productData27.projectTechnology = "* Native iOS app in Xcode \n* Bluetooth low energy level app";
        productData27.projectAchievement = "Mobile device to app Bluetooth connection is successfully implemented.\n GPS connectivity which alert user with alarm when he reached a particular location.";
        productData27.projectChallenges = "Challenge in handling Bluetooth connectivity through app.\n" +
                " Device & app should be connected / disconnected when they are in range and out of range.";
        productData27.projectIOSLink = "";
        productData27.projectAndroidLink = "";
        productData27.projectIndstryCategory = "Retail";
        listProduct.add(productData27);


        PortfolioData productData28 = new PortfolioData();
        productData28.projectId = "ProjectM2catalyst";
        productData28.projectName = "M2 catalyst";
        productData28.projectImage = R.mipmap.m2_catalyst;
        productData28.projectOverview = "M2 App Monitor is a crowd sourced Android analytics tool that provides you with simple reporting to help you understand the performance of your apps, your application usage, and helps you avoid bad apps that slow down your phone. It alerts you of applications that drain your battery, slow down your phone, increase your data usage, and more. M2 App Monitor looks at all of the applications on your device, and compares that data to every other device in the world that has M2 App Monitor installed.";
        productData28.projectWeblink = "";
        productData28.projectCategory = "Productivity";
        productData28.projectPlateform = "Android app";
        productData28.projectTechnology = "* Eclipse with Native C++ compiler";
        productData28.projectAchievement = "Successfully showing apps using battery life. Support max. of Android devices.";
        productData28.projectChallenges = "Technically complicated to identify the battery life usage and other consumptions in different android structures.";
        productData28.projectIOSLink = "";
        productData28.projectAndroidLink = "https://play.google.com/store/apps/details?id=com.m2catalyst.m2appmonitor&hl=en";
        productData28.projectIndstryCategory = "Retail";
        listProduct.add(productData28);


        PortfolioData productData29 = new PortfolioData();
        productData29.projectId = "ProjectMy_stuff_finder";
        productData29.projectName = "My stuff finder";
        productData29.projectImage = R.mipmap.my_stuff_finder;
        productData29.projectOverview = "Stuff Finder is a \"lost and found\" app. It provides a quick, easy, and fun way to \"record\" the location of today's most commonly misplaced and lost \"items.\" These include cars, glasses, shoes, gloves, keys, and wallets. You can also record the location of your hotel, meeting spot, and up to four other user-defined items. The item location is automatically determined through GPS, with the ability for the user to further pinpoint small items by adding an audio note and picture.";
        productData29.projectWeblink = "";
        productData29.projectCategory = "Productivity";
        productData29.projectPlateform = "ios app";
        productData29.projectTechnology = "* Xcode 5.5 \n* Open GL";
        productData29.projectAchievement = "";
        productData29.projectChallenges = "";
        productData29.projectIOSLink = "https://itunes.apple.com/us/app/my-stufffinder/id542000800?mt=8";
        productData29.projectAndroidLink = "";
        productData29.projectIndstryCategory = "Retail";
        listProduct.add(productData29);


        PortfolioData productData30 = new PortfolioData();
        productData30.projectId = "ProjectHidden_camera_detector";
        productData30.projectName = "Hidden camera detector";
        productData30.projectImage = R.mipmap.hidden_camera_detector;
        productData30.projectOverview = "Quickly scans any room for hidden cameras and make sure no one is spying on you! Uses the iPhone camera and flash to find potential hidden spy cameras. Save images with GPS location of suspected spy cameras.";
        productData30.projectWeblink = "";
        productData30.projectCategory = "Productivity";
        productData30.projectPlateform = "ios app";
        productData30.projectTechnology = "* Xcode 5.5 \n* Open GL";
        productData30.projectAchievement = "";
        productData30.projectChallenges = "";
        productData30.projectIOSLink = "https://itunes.apple.com/us/app/hidden-camera-detector/id532882360?mt=8";
        productData30.projectAndroidLink = "";
        productData30.projectIndstryCategory = "Retail";
        listProduct.add(productData30);


     /*   PortfolioData productData31 = new PortfolioData();
        productData31.projectId = "ProjectIntegritycubesProductivity";
        productData31.projectName = "Integrity cubes";
        productData31.projectImage = R.mipmap.integrity_cubes;
        productData31.projectOverview = "The Cube is a social recognition app built exclusively for the Integrity Staffing team. It’s a place for all of us to come together to share, acknowledge and read about how our core values manifest themselves in gestures, big and small, across the organization.";
        productData31.projectWeblink = "www.thecube.integritystaffing.com";
        productData31.projectCategory = "Productivity";
        productData31.projectPlateform = "Web and iOS app";
        productData31.projectTechnology = "* Native iOS app in Xcode\n* Php\n* My Sql\n* Jquery / JavaScript\n* Css,html\n* Ajax\n* Amazon web server\n* Yii framework\n* Google pie chart API’s\n* Mobile application web services";
        productData31.projectAchievement = "* Finding winner by performing calculation of likes & comments of 300-400 staff members on a single cube.\n* Maintaining Hierarchy of members & their reports.";
        productData31.projectChallenges = "* Lots of slowing and freezing/setting permissions for multiple views according to users.\n* Management of multiple user comments.";
        productData31.projectIOSLink = "https://itunes.apple.com/us/app/integrity-the-cube/id897464602?mt=8";
        productData31.projectAndroidLink = "";
        listProduct.add(productData31);*/


        PortfolioData productData32 = new PortfolioData();
        productData32.projectId = "ProjectMyRoadworkout";
        productData32.projectName = "My Road workout";
        productData32.projectImage = R.mipmap.myroad_workout;
        productData32.projectOverview = "It helps you to stay fit and stick to your goals when you're on the road, whether you're a seasoned business traveler or just headed out on vacation. The app uses GPS to find everything close to you the second you land in your travel destination, with maps and directions to every location. Save locations in multiple cities by marking places as favorites and storing them for your next visit.";
        productData32.projectWeblink = "";
        productData32.projectCategory = "GPS";
        productData32.projectPlateform = "Android, iOS app";
        productData32.projectTechnology = "* Native iOS app in Xcode \n* Native Android with Eclipse \n* My Sql Server \n*JSON web services";
        productData32.projectAchievement = "Successfully displaying list of events in Canada, list , map & satellite view of events & places.";
        productData32.projectChallenges = "With over 10,000 listings for fitness centers, grocery stores, health food stores and day spas for tracking any city across Canada.";
        productData32.projectIOSLink = "https://itunes.apple.com/us/app/my-road-workout-canada/id768018754?mt=8";
        productData32.projectAndroidLink = "https://play.google.com/store/apps/details?id=app.road.workout&hl=en";
        productData32.projectIndstryCategory = "Technology";
        listProduct.add(productData32);

        PortfolioData productData33 = new PortfolioData();
        productData33.projectId = "ProjectPlaceme";
        productData33.projectName = "Place me";
        productData33.projectImage = R.mipmap.place_me;
        productData33.projectOverview = "Placeme always remembers the places you visit. With Placeme, you can enjoy your mobile life while the app automatically records all your place visits for you. It auto-detects places by name without any input from you, and figures out the duration of each visit. No manual check-in is needed. Placeme also supports Evernote integration. You can always search for past visits by name or category.";
        productData33.projectWeblink = "";
        productData33.projectCategory = "GPS";
        productData33.projectPlateform = "iOS app";
        productData33.projectTechnology = "* Native iOS app in Xcode \n* Web socket server programming\n* Google Places";
        productData33.projectAchievement = "Use of Hybrid mode in Map view, location recording.";
        productData33.projectChallenges = "Managing list of places user visit & recordings.";
        productData33.projectIOSLink = "";
        productData33.projectAndroidLink = "";
        productData33.projectIndstryCategory = "Technology";
        listProduct.add(productData33);


        PortfolioData productData34 = new PortfolioData();
        productData34.projectId = "ProjectGoogle_spot";
        productData34.projectName = "Google spot";
        productData34.projectImage = R.mipmap.google_spot;
        productData34.projectOverview = "It helps user to find his nearest places like fitness,restaurants, bars,shopping & outdoors. It facilitates user to view list of places they have recently checked in. User can find, follow, view details of different users who has checked in by relation to time.";
        productData34.projectWeblink = "";
        productData34.projectCategory = "GPS";
        productData34.projectPlateform = "iOS app";
        productData34.projectTechnology = "* Native iOS app in Xcode\n*  Web socket server programming \n* Google Places";
        productData34.projectAchievement = "Less time consumption when data is stored in database which is being fetched from google API's \n A check is applied to resolve 2nd challenge. Check condition is - if data is 90 days older than it will send request to google & fetch data according to condition(of address change & updation)";
        productData34.projectChallenges = "Consuming more time in searching of a location because data is directly retrieved from google API's, filtered & displayed in app. \n There are 2 conditions which are creating problems in app if data is fetched from database:\n" +
                "- If a new restaurant, fitness centre, shopping malls are registered in google , then how it will be displayed in app .\n" +
                "- If addresses of any location is changed then how it will be displayed in app.";
        productData34.projectIOSLink = "";
        productData34.projectAndroidLink = "";
        productData34.projectIndstryCategory = "Technology";
        listProduct.add(productData34);


        PortfolioData productData35 = new PortfolioData();
        productData35.projectId = "ProjectE-safe_move";
        productData35.projectName = "E-safe move";
        productData35.projectImage = R.mipmap.e_safe_move;
        productData35.projectOverview = "This application is a communication tool that will help children's , parents or anyone to Take care of their loved ones. a push notification message via email /phone is send , when the vehicle is moving away from boundary lines. It Tracks the user through GPS,Showing the sub users list,Showing the curve path / line on map & Sending invitation. It Calculates the distance travelled by the users.";
        productData35.projectWeblink = "";
        productData35.projectCategory = "GPS";
        productData35.projectPlateform = "iOS app";
        productData35.projectTechnology = "* Native iOS app in Xcode\n*  Native Android with Eclipse\n*  Web services\n* MySQL";
        productData35.projectAchievement = "Less time consumption when data is stored in database which is being fetched from google API's \n A check is applied to resolve 2nd challenge. Check condition is - if data is 90 days older than it will send request to google & fetch data according to condition(of address change & updation)";
        productData35.projectChallenges = "Consuming more time in searching of a location because data is directly retrieved from google API's, filtered & displayed in app. img There are 2 conditions which are creating problems in app if data is fetched from database:\n" +
                "- If a new restaurant, fitness centre, shopping malls are registered in google , then how it will be displayed in app .\n" +
                "- If addresses of any location is changed then how it will be displayed in app.";
        productData35.projectIOSLink = "";
        productData35.projectAndroidLink = "";
        productData35.projectIndstryCategory = "Technology";
        listProduct.add(productData35);


        PortfolioData productData36 = new PortfolioData();
        productData36.projectId = "ProjectSimplyshia";
        productData36.projectName = "Simplyshia";
        productData36.projectImage = R.mipmap.simplyshia;
        productData36.projectOverview = "Simplyshia is a site for all Shia Muslims who want to meet their partner. It offers a safe, secure and discreet environment where one can search for marriage partner, communicate and find soul mate. It lets user search all the profiles and find people who best match their preferences.";
        productData36.projectWeblink = "www.simplyshia.com";
        productData36.projectCategory = "Social";
        productData36.projectPlateform = "Web app";
        productData36.projectTechnology = "* Php with Zend framework\n* MYSQL";
        productData36.projectAchievement = "Successful launching of portal on time and successful running last 3 years.";
        productData36.projectChallenges = "Limited hardware resources and budget.";
        productData36.projectIOSLink = "";
        productData36.projectAndroidLink = "";
        productData36.projectIndstryCategory = "Media Industry";
        listProduct.add(productData36);

        PortfolioData productData37 = new PortfolioData();
        productData37.projectId = "ProjectKhojamatch";
        productData37.projectName = "Khojamatch";
        productData37.projectImage = R.mipmap.khojamatch;
        productData37.projectOverview = "Khojamatch is an online matrimonial service exclusively for Shia Ithna'Asheri Muslims belonging to Khoja communities which are members of the World Federation of Khoja Shia Ithna'Asheri Muslim Communities. Khojamatch is confidential and puts you in control of your search for a marriage partner.";
        productData37.projectWeblink = "www.khojamatch.com";
        productData37.projectCategory = "Social";
        productData37.projectPlateform = "Web app";
        productData37.projectTechnology = "* Php with Zend framework\n* MYSQL\n* Pay Pal Payment Gateway";
        productData37.projectAchievement = "Successful launching of portal on time and successful running last 3 years.";
        productData37.projectChallenges = "Limited hardware resources and budget.";
        productData37.projectIOSLink = "";
        productData37.projectAndroidLink = "";
        productData37.projectIndstryCategory = "Media Industry";
        listProduct.add(productData37);


        PortfolioData productData38 = new PortfolioData();
        productData38.projectId = "ProjectOnly_one";
        productData38.projectName = "Only one";
        productData38.projectImage = R.mipmap.only_one;
        productData38.projectOverview = "The Only one application allows a user to send messages containing text, photos, videos, and audio clips over a proprietary channel to another registered user. User accounts contain a unique mobile phone number. A message can only be sent if a stamp is available. Stamps expire, and new stamps are periodically provided to each user account by the service. An unlimited number of text replies can be exchanged between a message recipient and sender.";
        productData38.projectWeblink = "";
        productData38.projectCategory = "Social";
        productData38.projectPlateform = "iOS, Android app";
        productData38.projectTechnology = "* Native iOS app in Xcode\n* Native Android with Eclipse\n* Parse & Amazon as backend server\n* Third party tools- custom video player,Dropbox integration, amazon SDK, Parse SDK";
        productData38.projectAchievement = "Customization of text, audio, videos are acheived successfully.\nData structured Managed .\nIts super master messenger app.";
        productData38.projectChallenges = "Customization of text , video , audio etc.\nParallel processing of threads.\nMultiple downloading of Pdfs, audio, image, video,animations handling.";
        productData38.projectIOSLink = "";
        productData38.projectAndroidLink = "";
        productData38.projectIndstryCategory = "Media Industry";
        listProduct.add(productData38);


        PortfolioData productData39 = new PortfolioData();
        productData39.projectId = "ProjectGive_a_heart";
        productData39.projectName = "Give a heart";
        productData39.projectImage = R.mipmap.give_a_heart;
        productData39.projectOverview = "Donate in your favorite charity using Give a heart charity app. Search for charities by category, learn more about them, and make donations. After creating an account, sign in and make a PayPal donation or play the game to get more hearts for free. You receive 10 hearts for every dollar donated which you can then distribute to your favorite charities. At the end of the month, we send checks to all the charities based on how many hearts each received, 10 cents for each heart.";
        productData39.projectWeblink = "";
        productData39.projectCategory = "Hospitality";
        productData39.projectPlateform = "iOS, Android app";
        productData39.projectTechnology = "* Native iOS app in Xcode\n* Native Android with Eclipse\n* Web services MySQL";
        productData39.projectAchievement = "Implemented heart collection game that will let user to collect hearts & donate to their favorite charity.";
        productData39.projectChallenges = "Collecting & managing database of different charities.\nPay pal payment gateway.";
        productData39.projectIOSLink = "";
        productData39.projectAndroidLink = "";
        productData39.projectIndstryCategory = "Human Resource & Staffing";
        listProduct.add(productData39);


        PortfolioData productData40 = new PortfolioData();
        productData40.projectId = "ProjectSharemeister";
        productData40.projectName = "Sharemeister";
        productData40.projectImage = R.mipmap.sharemeister;
        productData40.projectOverview = "Sharemeister connects you with the marketplace to benefit nonprofits. Discover fun ways to Integrate Giving into Everyday Life and earn Giving Rewards with Sharemeister. Find a competition, join a team and give directly to the causes you care about directly or indirectly through competitions. Save your debit or credit card to file and use your card at participating merchants to earn money to benefit your cause and help your team win. We invite you to join us and Share in the Fun of Giving.";
        productData40.projectWeblink = "www.sharemeister.com/";
        productData40.projectCategory = "Hospitality";
        productData40.projectPlateform = "iOS, Android app";
        productData40.projectTechnology = "* Native iOS app in Xcode\n* Native Android with Eclipse\n* Ruby on Rails\n* JSON API";
        productData40.projectAchievement = "Memory Managed successfully.";
        productData40.projectChallenges = "Collecting & managing database of different charities.\nIntegration of payment gateway.";
        productData40.projectIOSLink = "";
        productData40.projectAndroidLink = "";
        productData40.projectIndstryCategory = "Human Resource & Staffing";
        listProduct.add(productData40);


        PortfolioData productData41 = new PortfolioData();
        productData41.projectId = "ProjectPetwatchman";
        productData41.projectName = "Pet watchman";
        productData41.projectImage = R.mipmap.pet_watchman;
        productData41.projectOverview = "It allows pet owners to easily stay in touch with their dog walkers and cat sitters and lets them know exactly when their pet sitter has arrived and departed from their home. It also alerts the pet owner when a visit has been completed.";
        productData41.projectWeblink = "";
        productData41.projectCategory = "Hospitality";
        productData41.projectPlateform = "iOS, Android app";
        productData41.projectTechnology = "* Native iOS app in Xcode\n* Asp .Net Server\n* SQL Server\n* JSON API";
        productData41.projectAchievement = "Heavy data usage, maintaining battery life.";
        productData41.projectChallenges = "Maintaining history of all visits and communications between sitters and clients.\n Managing time according to time zone in counter.";
        productData41.projectIOSLink = "https://itunes.apple.com/us/app/pet-watchman/id532900127?mt=8";
        productData41.projectAndroidLink = "";
        productData41.projectIndstryCategory = "Human Resource & Staffing";
        listProduct.add(productData41);


        PortfolioData productData42 = new PortfolioData();
        productData42.projectId = "ProjectOye_dialer";
        productData42.projectName = "Oye dialer";
        productData42.projectImage = R.mipmap.oye_dialer;
        productData42.projectOverview = "Oye Dialer is the ultimate application for low-cost Long Distance calls. No more pins to remember, no access number to dial or phone numbers to remember, not even the speed dial numbers to be remembered or dialed. You can even use it to make local calls over your internet connection when you are running out of allowed minutes in your wireless plan. It provides you different calling methods to meet your wireless plan and calling needs. Calling methods include Local Access, Callback and Internet Calling. Oye Dialer also allows you to transfer your account balance to friends and family.";
        productData42.projectWeblink = "";
        productData42.projectCategory = "Telecom";
        productData42.projectPlateform = "iOS, Android app";
        productData42.projectTechnology = "* .Net server’s\n* Native Xcode apps with PJSIP library compiled on C++ compilers\n* Native Android app with PJSIP compiled with C++ compilers\n* Web socket API’s\n* Apple pay stores\n* SQL server 2005";
        productData42.projectAchievement = "Successful white labeling of apps for 30 more companies. Smooth and better voice communication.";
        productData42.projectChallenges = "Integration of 729 codec of audio , technically too much complicate\nCreated custom dialer and activate with libraries\nManaging Address books of the phones\nManaging TOP up of payment cards through the app with apple limitations";
        productData42.projectIOSLink = "https://itunes.apple.com/tc/app/oye-dialer/id575833949?mt=8";
        productData42.projectAndroidLink = "https://play.google.com/store/apps/details?id=ca.oyetoronto&hl=en";
        productData42.projectIndstryCategory = "Communications";
        listProduct.add(productData42);


        PortfolioData productData43 = new PortfolioData();
        productData43.projectId = "ProjectCallture";
        productData43.projectName = "Callture";
        productData43.projectImage = R.mipmap.callture;
        productData43.projectOverview = "This app allows you to make phone calls from your cell phone with your business phone caller id. It works with Callture business phone service. With this service, phone calls to your business number can be forwarded to your cell phone. And with this app, you can call back with your business number. You will see missed calls and business contacts within the app, so you can call back with one click. This allows you to have a separate business phone line to work on your cell phone.";
        productData43.projectWeblink = "www.callture.com";
        productData43.projectCategory = "Telecom";
        productData43.projectPlateform = "iOS, Android app";
        productData43.projectTechnology = "* .Net server’s\n* Native Xcode apps with PJSIP library compiled on C++ compilers\n* Native Android app with PJSIP compiled with C++ compilers\n* Web socket API’s\n* Apple pay stores\n* SQL server 2005";
        productData43.projectAchievement = "Successful white labeling of apps for 30 more companies. Smooth and better voice communication.";
        productData43.projectChallenges = "Integration of 729 codec of audio , technically too much complicated\nCreated custom dialer and activate with libraries\nManaging Address books of the phones\nManaging TOP up of payment cards through the app with apple limitations";
        productData43.projectIOSLink = "https://itunes.apple.com/in/app/callture/id508796795?mt=8";
        productData43.projectAndroidLink = "";
        productData43.projectIndstryCategory = "Communications";
        listProduct.add(productData43);


        PortfolioData productData44 = new PortfolioData();
        productData44.projectId = "ProjectVoice_conexion";
        productData44.projectName = "Voice conexion";
        productData44.projectImage = R.mipmap.voice_conexion;
        productData44.projectOverview = "Voice conexion is the ultimate application for low-cost Long Distance calls. No more pins to remember, no access number to dial or phone numbers to remember, not even the speed dial numbers to be remembered or dialed. You can even use it to make local calls over your internet connection when you are running out of allowed minutes in your wireless plan. It provides you different calling methods to meet your wireless plan and calling needs. Calling methods include Local Access, Callback and Internet Calling. Oye Dialer also allows you to transfer your account balance to friends and family.";
        productData44.projectWeblink = "";
        productData44.projectCategory = "Telecom";
        productData44.projectPlateform = "iOS app";
        productData44.projectTechnology = "* .Net server’s\n* Native Xcode apps with PJSIP library compiled on C++ compilers\n* Native Android app with PJSIP compiled with C++ compilers\n* Web socket API’s\n* Apple pay stores\n* SQL server 2005";
        productData44.projectAchievement = "Successful white labeling of apps for 30 more companies. Smooth and better voice communication.";
        productData44.projectChallenges = "Integration of 729 codec of audio , technically too much complicated\nCreated custom dialer and activate with libraries\nManaging Address books of the phones\nManaging TOP up of payment cards through the app with apple limitations";
        productData44.projectIOSLink = "https://itunes.apple.com/in/app/voice-conexion/id586198200?mt=8";
        productData44.projectAndroidLink = "";
        productData44.projectIndstryCategory = "Communications";
        listProduct.add(productData44);


        PortfolioData productData45 = new PortfolioData();
        productData45.projectId = "ProjectJustpinless";
        productData45.projectName = "Just pinless";
        productData45.projectImage = R.mipmap.just_pinless;
        productData45.projectOverview = "Just pinless is the ultimate application for low-cost Long Distance calls. No more pins to remember, no access number to dial or phone numbers to remember, not even the speed dial numbers to be remembered or dialed. You can even use it to make local calls over your internet connection when you are running out of allowed minutes in your wireless plan. It provides you different calling methods to meet your wireless plan and calling needs. Calling methods include Local Access, Callback and Internet Calling. Oye Dialer also allows you to transfer your account balance to friends and family.";
        productData45.projectWeblink = "";
        productData45.projectCategory = "Telecom";
        productData45.projectPlateform = "iOS app";
        productData45.projectTechnology = "* .Net server’s\n* Native Xcode apps with PJSIP library compiled on C++ compilers\n* Native Android app with PJSIP compiled with C++ compilers\n* Web socket API’s\n* Apple pay stores\n* SQL server 2005";
        productData45.projectAchievement = "Successful white labeling of apps for 30 more companies. Smooth and better voice communication.";
        productData45.projectChallenges = "Integration of 729 codec of audio , technically too much complicated\nCreated custom dialer and activate with libraries\nManaging Address books of the phones\nManaging TOP up of payment cards through the app with apple limitations";
        productData45.projectIOSLink = "https://itunes.apple.com/in/app/just-pinless/id787668503?mt=8";
        productData45.projectAndroidLink = "";
        productData45.projectIndstryCategory = "Communications";
        listProduct.add(productData45);

        DBManagerAP.getInstance().insertDataInPortfolioTable(listProduct);
    }


}