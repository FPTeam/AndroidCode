package com.example.littleworld.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.littleworld.R;
import com.example.littleworld.overlay.PoiOverlay;
import com.example.littleworld.overlay.DrivingRouteOverLay;
import com.example.littleworld.util.AMapUtil;
import com.example.littleworld.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * poi并在地图上标识,OK
 */
public class Hotel extends AppCompatActivity implements
        OnMarkerClickListener, InfoWindowAdapter,
        OnPoiSearchListener, OnClickListener, InputtipsListener, RouteSearch.OnRouteSearchListener {
    private AMap aMap;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "酒店";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private String cityCode;// 要输入的城市名字或者城市区号
    private boolean success =false;//定位成功标志
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private MapView mapView;
    private ArrayList<Marker> trip_Marker_list = new ArrayList<Marker>();//用户选择希望去的地点
    private ArrayList<Marker> through_marker = new ArrayList<Marker>();//途经点marker
    private int mode = 1;//选择查询景区mode=0，其他mode=1
    private LatLonPoint mStartPoint;//路线起点
    private LatLonPoint mEndPoint;//路线终点
    private DriveRouteResult mDriveRouteResult;//驾车路线查询结果
    private Context mContext;
    private RelativeLayout mBottomLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    ArrayList<LatLonPoint> passedByPoints;
    private MarkerOptions myLocation;
    private Marker startMarker,endMarker;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    cityCode = new String(amapLocation.getCityCode());//解析地址获取省份
                    success = true;

                    mStartPoint = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());//起始点设为定位位置

                    myLocation = (new MarkerOptions())
                            .position(
                                    new LatLng(mStartPoint.getLatitude(),mStartPoint.getLongitude()))
                            .visible(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start))
                            .title("我的位置").snippet(amapLocation.getPoiName());//记录当前位置markOption

                    startMarker = new Marker(myLocation);
                    deactivate();//定位成功就停止定位

                    LinearLayout option;
                    option=(LinearLayout)findViewById(R.id.option_list);
                    option.setVisibility(View.VISIBLE);

                    hotelButton();

                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    ToastUtil.show(Hotel.this,  errText);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel);
        mContext = this.getApplicationContext();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

        // 返回至上一界面
        ImageButton backBtn=findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hotel.this.finish();
            }
        });
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap =  mapView.getMap();
            setUpMap();
        }

        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {

        Button nextButton = (Button) findViewById(R.id.nextButton);//下一页
        nextButton.setOnClickListener(this);

        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }

    /**
     * 点击景点按钮
     */
    public void interestButton() {
        keyWord ="景点";
        doSearchQuery();
    }

    /**
     * 点击酒店按钮
     */
    public void hotelButton() {
        keyWord ="酒店";
        doSearchQuery();
    }

    /**
     * 点击下一页按钮
     */
    public void nextButton() {
        if (query != null && poiSearch != null && poiResult != null) {
            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                ToastUtil.show(Hotel.this,
                        R.string.no_result);
            }
        }
    }

    /**
     * 点击规划按钮
     */
    public void planButton() {
        if(trip_Marker_list.size()<1)
            ToastUtil.show(Hotel.this, "您未选择任何景点！");
        else{
            planTrip();
        }
    }

    /**
     * 旅行规划
     */
    public void planTrip(){

        if (mStartPoint == null) {
            ToastUtil.show(Hotel.this, "定位中，稍后再试...");
            return;
        }

        passedByPoints = new ArrayList<LatLonPoint>();
        LatLng latLng_start = AMapUtil.convertToLatLng(mStartPoint);
        ArrayList <LatLng> latLngPonit = new ArrayList<LatLng>();
        for(int i=0;i<trip_Marker_list.size();i++)
            latLngPonit.add(new LatLng(trip_Marker_list.get(i).getPosition().latitude, trip_Marker_list.get(i).getPosition().longitude));

        do {//每次将离当前点最近的景点作为下一个途经点
            float distance = -1;
            float minDistance = -1;
            int min = 0;
            for (int k = 0; k < latLngPonit.size(); k++) {
                distance = AMapUtils.calculateLineDistance(latLng_start, latLngPonit.get(k));
                if (k == 0)
                    minDistance = distance;
                else {
                    if (minDistance > distance) {
                        minDistance = distance;
                        min = k;
                    }
                }
            }
            passedByPoints.add(new LatLonPoint(latLngPonit.get(min).latitude, latLngPonit.get(min).longitude));//添加途径点
            through_marker.add(trip_Marker_list.get(min));
            latLng_start = latLngPonit.get(min);//将该点设为起点
            latLngPonit.remove(min);//移除该点
            trip_Marker_list.remove(min);//移除该点
        }while(latLngPonit.size()>1);

        mEndPoint = new LatLonPoint(latLngPonit.get(0).latitude, latLngPonit.get(0).longitude);//最后一个景点作为终点
        endMarker = trip_Marker_list.get(0);

        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);

        //驾车路线搜索
        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);

        int drivingMode = RouteSearch.DrivingDefault;
        // fromAndTo包含路径规划的起点和终点，drivingMode表示驾车模式
        // 第三个参数表示途经点（最多支持16个），第四个参数表示避让区域（最多支持32个），第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, drivingMode, passedByPoints, null, "");
        //发送请求
        routeSearch.calculateDriveRouteAsyn(query);
    }

    //搜索公交路线
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    //搜索驾车路线
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), passedByPoints, through_marker, startMarker, endMarker);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约"+taxiCost+"元");
                    mBottomLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //跳转到详细路线页面
                            Intent intent = new Intent(mContext,
                                    DriveRouteDetailActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result",
                                    mDriveRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }

    }

    //搜索步行路线
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    //搜索骑行路线
    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        if(success=true)
            query = new PoiSearch.Query(keyWord, "", cityCode);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        else
            query = new PoiSearch.Query(keyWord, "","");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    boolean inList;
    //定义marker信息窗口
    @Override
    public View getInfoWindow(final Marker marker) {
        View view;//选择景点模式
        if(mode==0) {
            view = getLayoutInflater().inflate(R.layout.poi_click_marker, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(marker.getTitle());

            TextView snippet = (TextView) view.findViewById(R.id.snippet);
            snippet.setText(marker.getSnippet());

            final ImageButton add_button = (ImageButton) view
                    .findViewById(R.id.add_intrest);

            inList=false;
            for(int m=0;m<trip_Marker_list.size();m++)
            {
                if(trip_Marker_list.get(m).getId().equals(marker.getId()))//已添加该景点
                {
                    inList = true;
                    TextView add_hint = (TextView) view.findViewById(R.id.add_hint);
                    add_hint.setText("已添加");
                    add_button.setImageResource(R.drawable.add_icon);//已添加图标
                    break;
                }
            }

            // 点击按钮将景点加入行程或删除
            add_button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inList==false) {

                        add_button.setImageResource(R.drawable.add_icon);//改变图标

                        TextView add_hint = (TextView) findViewById(R.id.add_hint);
                        add_hint.setText("已添加");

                        trip_Marker_list.add(marker);//加入旅行列表

                        TextView interestView = new TextView(getApplicationContext());
                        LinearLayout list = (LinearLayout) findViewById(R.id.interest_list);//显示列表

                        interestView.setText(marker.getTitle());

                        list.addView(interestView);

                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_red));//标记变红
                        inList = true;
                    }else{
                        add_button.setImageResource(R.drawable.not_add_icon);//改变图标

                        TextView add_hint = (TextView) findViewById(R.id.add_hint);
                        add_hint.setText("添加景点");

                        trip_Marker_list.remove(marker);//从旅行列表删除

                        //更新显示列表
                        LinearLayout list = (LinearLayout) findViewById(R.id.interest_list);
                        list.removeAllViews();//清空

                        TextView interestView = new TextView(getApplicationContext());
                        interestView.setText("加入旅程的景点：");
                        list.addView(interestView);

                        for(int j = 0;j<trip_Marker_list.size();j++) {
                            interestView = new TextView(getApplicationContext());
                            interestView.setText(trip_Marker_list.get(j).getTitle());
                            list.addView(interestView);
                        }
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_pressed));//标记变蓝
                        inList = false;
                    }
                }
            });


        }
        else{//其他模式，导航
            view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(marker.getTitle());

            TextView snippet = (TextView) view.findViewById(R.id.snippet);
            snippet.setText(marker.getSnippet());

            ImageButton button = (ImageButton) view
                    .findViewById(R.id.start_amap_app);
            // 调起高德地图app
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAMapNavi(marker);
                }
            });
        }
        return view;
    }


    // 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面

    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps2d.AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());

        }

    }


    //判断高德地图app是否已经安装

    public boolean getAppIn() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(
                    "com.autonavi.minimap", 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        // 本手机没有安装高德地图app
        if (packageInfo != null) {
            return true;
        }
        // 本手机成功安装有高德地图app
        else {
            return false;
        }
    }


    //获取当前app的应用名字

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager
                .getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(Hotel.this, infomation);

    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        aMap.addMarker(myLocation);//加上当前定位
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(Hotel.this,
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(Hotel.this,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(Hotel.this, rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        LinearLayout list;
        LinearLayout option;
        Button aPlan;
        switch (v.getId()) {


            /**
             * 点击下一页按钮
             */
            case R.id.nextButton:
                nextButton();
                break;
            default:
                break;
        }
    }



    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(Hotel.this, rCode);
        }


    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 停止定位
     */
    public void deactivate() {
        mLocationListener=null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }
}
