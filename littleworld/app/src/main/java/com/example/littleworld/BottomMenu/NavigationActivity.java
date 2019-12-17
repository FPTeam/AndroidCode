package com.example.littleworld.BottomMenu;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.littleworld.R;
import com.google.android.material.tabs.TabLayout;


public class NavigationActivity extends AppCompatActivity {
    TabLayout mytabs;
    ViewPager mViewPager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_menu);

        mytabs = (TabLayout)findViewById(R.id.mytab);

        mytabs.addTab(mytabs.newTab().setText("首页").setIcon(R.mipmap.shouye));
        mytabs.addTab(mytabs.newTab().setText("关注").setIcon(R.mipmap.xihuan));
        mytabs.addTab(mytabs.newTab().setText("笔记").setIcon(R.mipmap.tianjia1));
        mytabs.addTab(mytabs.newTab().setText("私信").setIcon(R.mipmap.tongzhi));
        mytabs.addTab(mytabs.newTab().setText("关于我").setIcon(R.mipmap.wode));
        mytabs.getTabAt(1).select();

        //       给ViewPager创建适配器，将Title和Fragment添加进ViewPager中
        mViewPager = findViewById(R.id.mypager);
        //        关联mytabs和viewPager
        //自定义的Adapter继承自FragmentPagerAdapter
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new NavigationPagerAdapter(fragmentManager,mytabs.getTabCount(),mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mytabs));

        mytabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}