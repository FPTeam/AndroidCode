package com.example.littleworld.BottomMenu;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class NavigationPagerAdapter extends FragmentPagerAdapter {
    //fragment的数量
    int nNumOfTabs;
    private ViewPager pvp;

    public NavigationPagerAdapter(FragmentManager fm, int nNumOfTabs, ViewPager pvp )
    {
        super(fm);
        this.pvp=pvp;
        this.nNumOfTabs=nNumOfTabs;
    }

    /**
     * 重写getItem方法
     * @param position 指定的位置
     * @return 特定的Fragment
     */
    public Fragment getItem(int position) {
        //return NavigationFragment.newInstance(position,pvp);
        Fragment fragment = new FollowFragment();
        switch (position)
        {
            case 0: //关注
//                fragment = new FollowFragment();
                break;
            case 1: //广场
//                fragment = new SquareFragment();
                break;
            case 2: //编辑
                break;
            case 3: //私信
                break;
            case 4: //关于我
                break;

        }
        return fragment;
    }

    /**
     * 重写getCount方法
     * @return fragment的数量
     */
    @Override
    public int getCount() {
        return nNumOfTabs;
    }
}