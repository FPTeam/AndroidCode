package com.example.littleworld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class NewMegActivity extends Fragment {

    private int userId;

    View layout;

    private List<User> userList = new ArrayList<>();

    public NewMegActivity(int userId){
        super();
        this.userId = userId;
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_notice);
//        layout = inflater.inflate(R.layout.activity_notice, container, false);
//        initUser();
//        UserAdapter adapter = new UserAdapter(getActivity(), R.layout.user_item, userList);
//        ListView listView = layout.findViewById(R.id.list_view);
//        listView.setAdapter(adapter);
//
//        return layout;
//    }
//
//    private void initUser(){
//        for (int i = 0; i < 2; i++){
//            User one = new User(001, R.drawable.white_bear);
//            userList.add(one);
//
//            User two = new User(002, R.drawable.white_bear);
//            userList.add(two);
//        }
//    }


}
