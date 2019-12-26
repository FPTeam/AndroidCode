package com.example.littleworld.Notice;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.littleworld.DbHelper;
import com.example.littleworld.R;

import java.util.ArrayList;
import java.util.List;


public class NewMegActivity extends Fragment {

    private List<Notice> noticeList = new ArrayList<>();
    private int userId;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View layout = inflater.inflate(R.layout.activity_notice, container, false);

        // 初始化
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        int userId = DbHelper.getInstance().getUserId();
        noticeList = DbHelper.getInstance().getMessage(0, 15, userId);
        Log.d("列表", noticeList.toString());
        NoticeAdapter adapter = new NoticeAdapter(getActivity(), noticeList);
        recyclerView.setAdapter(adapter);

        return layout;
    }


    public NewMegActivity(int userId){
        super();
        this.userId = userId;
    }



}
