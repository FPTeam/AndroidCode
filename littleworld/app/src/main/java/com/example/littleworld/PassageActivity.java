package com.example.littleworld;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.littleworld.Adapter.passageAdapter;
import com.example.littleworld.Entity.passage;

import java.util.ArrayList;

import java.util.List;

/**
 * 此页面用于关注的人，显示所有人动态，通过搜索框，可查询到相关用户的动态
 * 同时还可以点赞，收藏
 *
 *
 */

public class PassageActivity extends Fragment {

    private List<passage> passageList = new ArrayList<>();
    SearchView searchView;
    private int userId;

    public PassageActivity(int userId) {
        super();
        this.userId = userId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = inflater.inflate(R.layout.activity_follow, container, false);

        /*起初显示*/
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        passageList = DbHelper.getInstance().searchPassage(0, 15, null);
        Log.d("列表", passageList.toString());
        passageAdapter adapter = new passageAdapter(getActivity(), passageList);
        recyclerView.setAdapter(adapter);

        //搜索框
        searchView = (SearchView) layout.findViewById(R.id.searchView_f);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String query) {
                if (!TextUtils.isEmpty(query)) {
                    /*
                     *检索搜索的人的动态
                     *  */
                    passageList.clear();
                    passageList = DbHelper.getInstance().searchPassage(0, 15, query);
                    Log.d("列表", passageList.toString());
                    passageAdapter adapter = new passageAdapter(getActivity(), passageList);
                    recyclerView.setAdapter(adapter);
                } else {
                    /*
                     * 显示全部动态*/
                    passageList.clear();
                    passageList = DbHelper.getInstance().searchPassage(0, 15, null);
                    Log.d("列表", passageList.toString());
                    passageAdapter adapter = new passageAdapter(getActivity(), passageList);
                    recyclerView.setAdapter(adapter);
                }

                return false;
            }
        });

        return layout;
    }
}

