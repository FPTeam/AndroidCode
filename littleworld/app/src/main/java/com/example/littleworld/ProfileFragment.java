package com.example.littleworld;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.littleworld.Entity.ProfileViewPager;

public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters

    private int mParam;//用来表示当前需要展示的是哪一页
    private TextView detail_text;//展示的具体内容，这里为了简单只用一个TextView意思一下
    private TextView userId_text; //用户ID
    private int userId = 190001; //用户ID
    private TextView userSex_text; //用户性别
    private TextView userIntro_text; //用户介绍
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

    public void setUserid(int userId){
        this.userId = userId;
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

        // 根据mParam来判断当前展示的是哪一页，根据页数的不同展示不同的信息
        switch (mParam){
            case 0:
                // 显示动态部分的fragment layout
                view=inflater.inflate(R.layout.profile_fragment_posts, container, false);
                pvp.setObjectForPosition(view,0);

                // 添加按钮事件
                // 示例：返回按钮，可删除
                Button button_test = view.findViewById(R.id.backTest);
                button_test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });

                // 示例：显示文字，可删除
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

                // 示例：显示文字，可删除
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
                userId_text=view.findViewById(R.id.profileID); // 用户ID
                userSex_text=view.findViewById(R.id.profileSex); // 用户性别
                userIntro_text=view.findViewById(R.id.profileIntro); // 个人介绍

                DbHelper.getInstance();
                /*
                 *   从数据库查询用户信息并显示
                 */
                showMap();//显示点亮的地图
                break;
            default:break;

        }

        return view;
    }

    public void showMap(){
        ImageView imageView;
        FrameLayout frame=(FrameLayout)view.findViewById(R.id.frameLayout);

        int[] provinces = new int[32];//32个省市，去过置1，没去过置0
        provinces = DbHelper.getInstance().getProvices(userId);

        int[] provinceList= {R.mipmap.zhejiang,R.mipmap.xinjiang,R.mipmap.xizang,R.mipmap.yunnan,R.mipmap.taiwan,R.mipmap.tianjin,R.mipmap.sichuan,R.mipmap.shandong,
                R.mipmap.shanghai,R.mipmap.qinghai,R.mipmap.shan1xi,R.mipmap.shan3xi,R.mipmap.ningxia,R.mipmap.neimenggu,R.mipmap.liaoning,R.mipmap.jiangxi,
                R.mipmap.jilin,R.mipmap.jiangsu,R.mipmap.hubei,R.mipmap.hunan,R.mipmap.heilongjiang,R.mipmap.henan,R.mipmap.guizhou,R.mipmap.hainan,
                R.mipmap.hebei,R.mipmap.guangxi,R.mipmap.gansu,R.mipmap.guangdong,R.mipmap.chongqing,R.mipmap.fujian,R.mipmap.anhui,R.mipmap.beijing};

        for(int i=0;i<32;i++){
            if(provinces[i]==1){
                imageView= new ImageView(getContext());
                imageView.setImageResource(provinceList[i]);
                frame.addView(imageView);
            }
        }

    }

}
