package com.example.littleworld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.littleworld.Notice.Notice;
import com.example.littleworld.Notice.NoticeAdapter;
import java.util.ArrayList;
import java.util.List;


public class NewMegActivity extends Fragment {

    private int userId;
    private View layout;

    private List<Notice> noticeList = new ArrayList<Notice>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        layout = inflater.inflate(R.layout.activity_notice, container, false);

        initNotices(); // 初始化数据
        NoticeAdapter adapter = new NoticeAdapter(getActivity(), R.layout.activity_notice_item, noticeList);
        ListView listView =layout.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Notice notice = noticeList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), notice.getMeg(), Toast.LENGTH_SHORT).show();
            }
        });
        return layout;
    }

    private void initNotices() {
        for (int i = 0; i < 2; i++) {
            Notice apple = new Notice("Apple", R.drawable.dir1);
            noticeList.add(apple);
            Notice banana = new Notice("Banana", R.drawable.dir1);
            noticeList.add(banana);
            Notice orange = new Notice("Orange", R.drawable.dir1);
            noticeList.add(orange);
            Notice watermelon = new Notice("Watermelon", R.drawable.dir1);
            noticeList.add(watermelon);
            Notice pear = new Notice("Pear", R.drawable.dir1);
            noticeList.add(pear);
            Notice grape = new Notice("Grape", R.drawable.dir1);
            noticeList.add(grape);
            Notice pineapple = new Notice("Pineapple", R.drawable.dir1);
            noticeList.add(pineapple);
            Notice strawberry = new Notice("Strawberry", R.drawable.dir1);
            noticeList.add(strawberry);
            Notice cherry = new Notice("Cherry", R.drawable.dir1);
            noticeList.add(cherry);
            Notice mango = new Notice("Mango", R.drawable.dir1);
            noticeList.add(mango);
        }
    }

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
