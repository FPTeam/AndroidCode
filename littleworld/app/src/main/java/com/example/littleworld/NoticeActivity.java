package com.example.littleworld;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/* 通知的Activity */
public class NoticeActivity extends AppCompatActivity {

    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongzhi);

        initUser();
        UserAdapter adapter = new UserAdapter(TongzhiActivity.this, R.layout.user_item, userList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }


    private void initUser(){
        for (int i = 0; i < 2; i++){
            User one = new User("One", R.drawable.bear_pic);
            userList.add(one);

            User two = new User("Two", R.drawable.bear_pic);
            userList.add(two);
        }
    }
}
