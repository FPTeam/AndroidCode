package com.example.littleworld.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.littleworld.ProfileFragment;
import com.example.littleworld.Entity.ProfileViewPager;

public class ProfilePageAdapter extends FragmentStatePagerAdapter {

    private static int PAGE_COUNT;//表示要展示的页面数量
    private Context mContext;
    private ProfileViewPager pvp;
    private int userId;

    public ProfilePageAdapter(Context context, FragmentManager fm, ProfileViewPager pvp, int userId) {
        super(fm);
        this.mContext = context;
        this.pvp=pvp;
        this.userId = userId;
        PAGE_COUNT=3;
    }

    @Override
    public Fragment getItem(int position) {
        ProfileFragment p = ProfileFragment.newInstance(position,this.pvp);
        p.setUserid(userId);
        return p;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {//设置标题
        switch (position) {
            case 0:
                return "动态";
            case 1:
                return "时光轴";
            case 2:
                return "关于我";
            default:break;

        }
        return null;
    }
}
