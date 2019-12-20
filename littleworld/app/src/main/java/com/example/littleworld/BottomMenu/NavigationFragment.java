package com.example.littleworld.BottomMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.littleworld.R;

public class NavigationFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private int mParam;//用来表示当前需要展示的是哪一页
    private View view;
    static ViewPager pvp;
    public NavigationFragment() {
        // Required empty public constructor
    }
    public static NavigationFragment newInstance(int param, ViewPager vp) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param);
        fragment.setArguments(args);
        pvp=vp;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getInt(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //根据mParam来判断当前展示的是哪一页，根据页数的不同展示不同的信息
        switch (mParam){
            case 0: //首页（关注）
                view = inflater.inflate(R.layout.activity_follow, container, false);
                break;
            case 1: //广场
                view = inflater.inflate(R.layout.activity_square, container, false);
                break;
            case 2: //发布动态
                view = inflater.inflate(R.layout.activity_new_notes, container, false);
                break;
            case 3: //私信
//                view = inflater.inflate(R.layout.activity_new_msg, container, false);
                view = inflater.inflate(R.layout.activity_notice, container, false);
                break;
            case 4: //设置
                view = inflater.inflate(R.layout.settings_main, container, false);
                break;
            default:break;
        }
        return view;
    }
}
