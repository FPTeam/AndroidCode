package com.example.littleworld;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.littleworld.Adapter.SquarePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SquareActivity extends Fragment {
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;
    private SearchView searchView;
    View layout;
    int userId;

    private ImageView places;//景点按钮
    private ImageView hotels;//酒店按钮
    private ImageView more;//其他按钮

    private List<ImageView> mImageList;//轮播的图片集合
    private String[] mImageTitles;//标题集合
    private int previousPosition = 0;//前一个被选中的position
    private List<View> mDots;//小点

    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 5000;//间隔时间

    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] imgae_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3};

    public SquareActivity(int userId){
        super();
        this.userId = userId;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = inflater.inflate(R.layout.activity_square, container, false);
        init();
        return layout;
    }

    /**
     * 第一步、初始化控件
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void init() {
        mViewPager = (ViewPager) layout.findViewById(R.id.img_changer);
        mTvPagerTitle = (TextView) layout.findViewById(R.id.tv_pager_title);

        initData();//初始化数据
        initView();//初始化View，设置适配器
        autoPlayView();//开启线程，自动播放

        places = (ImageView) layout.findViewById(R.id.places);
        hotels = (ImageView) layout.findViewById(R.id.hotels);
        more = (ImageView) layout.findViewById(R.id.more);

        places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.littleworld.map.Places.class);
                startActivity(intent);
            }
        });

        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.littleworld.map.Hotel.class);
                startActivity(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.littleworld.map.Others.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 第二步、初始化数据（图片、标题、点击事件）
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initData() {
        //初始化标题列表和图片
        mImageTitles = new String[]{"编辑推荐", "热门景点", "日常活动"};
        int[] imageRess = new int[]{R.mipmap.poster11, R.mipmap.poster12, R.mipmap.poster13};

        //添加图片到图片列表里
        mImageList = new ArrayList<>();
        ImageView iv;
        for (int i = 0; i < 3; i++) {
            iv = new ImageView(getActivity());
            iv.setBackgroundResource(imageRess[i]);//设置图片
            iv.setId(imgae_ids[i]);//顺便给图片设置id
            iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
            mImageList.add(iv);
        }

        //添加轮播点
//        LinearLayout linearLayoutDots = (LinearLayout) getActivity().findViewById(R.id.lineLayout_dot);
//        mDots = addDots(linearLayoutDots, fromResToDrawable(getActivity(), R.drawable.change_points), mImageList.size());//其中fromResToDrawable()方法是我自定义的，目的是将资源文件转成Drawable
    }

    //图片点击事件
    private class pagerImageOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pager_image1:
                    Toast.makeText(getActivity(), "图片1被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image2:
                    Toast.makeText(getActivity(), "图片2被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image3:
                    Toast.makeText(getActivity(), "图片3被点击", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * 第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initView() {
        SquarePagerAdapter viewPagerAdapter = new SquarePagerAdapter(mImageList, mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                // 把当前选中的点给切换了, 还有描述信息也切换
                mTvPagerTitle.setText(mImageTitles[newPosition]);//图片下面设置显示文本
                /*
                //设置轮播点
                LinearLayout.LayoutParams newDotParams = (LinearLayout.LayoutParams) mDots.get(newPosition).getLayoutParams();
                newDotParams.width = 24;
                newDotParams.height = 24;

                LinearLayout.LayoutParams oldDotParams = (LinearLayout.LayoutParams) mDots.get(previousPosition).getLayoutParams();
                oldDotParams.width = 16;
                oldDotParams.height = 16;
                */
                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setFirstLocation();
    }

    /**
     * 第四步：设置刚打开app时显示的图片和文字
     */
    private void setFirstLocation() {
        mTvPagerTitle.setText(mImageTitles[previousPosition]);
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);
    }

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private void autoPlayView() {
        //自动播放图片
        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isStop) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                            }
                        });
                        SystemClock.sleep(PAGER_TIOME);
                    }
                }
            }).start();
        }catch (NullPointerException e)
        {

        }
    }


    /**
     * 资源图片转Drawable
     *
     * @param context
     * @param resId
     * @return
     */
    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }


    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return
     *
     @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
     @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
     public int addDot(final LinearLayout linearLayout, Drawable backgount) {
     final View dot = new View(getActivity());
     LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
     ViewGroup.LayoutParams.WRAP_CONTENT);
     dotParams.width = 16;
     dotParams.height = 16;
     dotParams.setMargins(4, 0, 4, 0);
     dot.setLayoutParams(dotParams);
     dot.setBackground(backgount);
     dot.setId(View.generateViewId());
     linearLayout.addView(dot);
     return dot.getId();
     }

     /**
      * 添加多个轮播小点到横向线性布局
      *
      * @param linearLayout
     * @param backgount
     * @param number
     * @return
     *
     @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
     @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
     public List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number) {
     List<View> dots = new ArrayList<>();
     for (int i = 0; i < number; i++) {
     int dotId = addDot(linearLayout, backgount);
     dots.add(getActivity().findViewById(dotId));
     }
     return dots;
     }*/
}