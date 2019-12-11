package com.example.littleworld;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters

    private int mParam;//用来表示当前需要展示的是哪一页
    private TextView detail_text;//展示的具体内容，这里为了简单只用一个TextView意思一下
    private TextView userId; //用户ID
    private TextView userSex; //用户性别
    private TextView userIntro; //用户介绍
    private View view;
    static ProfileViewPager pvp;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int param, ProfileViewPager vp) {
        ProfileFragment fragment = new ProfileFragment();
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
            case 0:
                // 显示动态部分的fragment layout
                view=inflater.inflate(R.layout.profile_fragment_posts, container, false);
                pvp.setObjectForPosition(view,0);
                detail_text = view.findViewById(R.id.detail_info);
                String text0 = "";
                for (int i = 0; i < 100; i++) {
                    text0 += "动态" + "\n";
                }
                detail_text.setText(text0);
                break;
            case 1:
                // 显示时光轴部分的fragment layout
                view=inflater.inflate(R.layout.profile_fragment_timeline, container, false);
                pvp.setObjectForPosition(view,1);
                detail_text = view.findViewById(R.id.detail_info);
                String text1 = "";
                for (int i = 0; i < 100; i++) {
                    text1 += "时光轴" + "\n";
                }
                detail_text.setText(text1);
                break;
            case 2:
                // 显示关于我部分的fragment layout
                view=inflater.inflate(R.layout.profile_fragment_aboutme, container, false);
                pvp.setObjectForPosition(view,2);
                userId=view.findViewById(R.id.profileID); // 用户ID
                userSex=view.findViewById(R.id.profileSex); // 用户性别
                userIntro=view.findViewById(R.id.profileIntro); // 个人介绍

                /*
                    从数据库查询用户信息并显示
                 */

                break;
            default:break;

        }

        return view;
    }
}
