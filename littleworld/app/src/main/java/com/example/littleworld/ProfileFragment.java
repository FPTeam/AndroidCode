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

    public ProfileFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int param) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param);
        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.profile_fragment, container, false);
        detail_text = view.findViewById(R.id.detail_info);


        //根据mParam来判断当前展示的是哪一页，根据页数的不同展示不同的信息
        switch (mParam){
            case 0:
                String text0 = "";
                for (int i = 0; i < 100; i++) {
                    text0 += "动态" + "\n";
                }
                detail_text.setText(text0);
                break;
            case 1:
                String text1 = "";
                for (int i = 0; i < 100; i++) {
                    text1 += "时光轴" + "\n";
                }
                detail_text.setText(text1);
                break;
            case 2:
                String text2 = "";
                for (int i = 0; i < 100; i++) {
                    text2 += "关于我" + "\n";
                }
                detail_text.setText(text2);
                break;
            default:break;

        }

        return view;
    }
}
