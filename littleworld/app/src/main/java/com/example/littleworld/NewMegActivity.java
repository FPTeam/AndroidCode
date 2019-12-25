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

    String s = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576836279108&di=fb5764a8ca3c623d1bab186a52db4acb&imgtype=0&src=http%3A%2F%2Fpic3.zhimg.com%2F50%2Fv2-4b98cf7fcde6bdad4b6c189473afd7d7_hd.jpg";

    private void initNotices() {
        for (int i = 0; i < 2; i++) {
            Notice apple = new Notice("Apple","Apple", s);
            noticeList.add(apple);
            Notice banana = new Notice("Apple","Banana",s);
            noticeList.add(banana);
            Notice orange = new Notice("Apple","Orange", s);
            noticeList.add(orange);
            Notice watermelon = new Notice("Apple","Watermelon",s);
            noticeList.add(watermelon);
            Notice pear = new Notice("Apple","Pear",s);
            noticeList.add(pear);
            Notice grape = new Notice("Apple","Grape",s);
            noticeList.add(grape);
            Notice pineapple = new Notice("Apple","Pineapple", s);
            noticeList.add(pineapple);
            Notice strawberry = new Notice("Apple","Strawberry", s);
            noticeList.add(strawberry);
            Notice cherry = new Notice("Apple","Cherry", s);
            noticeList.add(cherry);
            Notice mango = new Notice("Apple","Mango", s);
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
