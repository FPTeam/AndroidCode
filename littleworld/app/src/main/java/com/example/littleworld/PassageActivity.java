package com.example.littleworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SearchView;

import com.example.littleworld.Adapter.passageAdapter;
import com.example.littleworld.Entity.passage;

import java.util.ArrayList;

import java.util.List;

/**
 * 此页面用于关注的人，显示所有人动态，通过搜索框，可查询到相关用户的动态
 * 同时还可以点赞，收藏
 *
 * 还未连数据库，数据库操作已空出
 *
 */

public class PassageActivity extends AppCompatActivity{

    private List<passage> passageList = new ArrayList<>();
    SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notes);

        searchView = (SearchView) findViewById(R.id.searchView_m);

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

                } else {
                    /*
                    * 显示全部动态*/

                }

                return false;
            }
        });

        initpassage();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("列表", passageList.toString());
        passageAdapter adapter = new passageAdapter(this,passageList);
        recyclerView.setAdapter(adapter);
    }

/*
* 以下用于个人测试页面，数据库操作加入后，可以删除以下数据
* */

    private final String names[] = {
            "张三",
            "李四"
    };
    private final String postime[] = {
            "2019-12-13 14:01",
            "2019-12-11 15:31"
    };
    private final String contents[] = {
            "风急天高猿啸哀 渚清沙白鸟飞回",
            "无边落木萧萧下 不尽长江滚滚来"
    };
    private final String imgpath[] = {
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576351733980&di=f047849190579b5d507a36edfe4f411e&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201409%2F26%2F20140926200958_Y8HWs.thumb.700_0.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576354413954&di=a3a688411d7f88b7a3501cda3a43dcbd&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D2478365145%2C1274253168%26fm%3D214%26gp%3D0.jpg"
    };

    private final int likenum[] = {1,2 };

    private final int collectnum[] = {6,7};

    private List<passage> initpassage(){

        for (int i = 0; i < names.length; i++ ){
            passage passage= new passage();
            passage.setName(names[i]);
            passage.setPostTime(postime[i]);
            passage.setContent( contents[i] );
            passage.setImgpath(imgpath[i]);
            passage.setLikeNumber(likenum[i]);
            passage.setCollectNumber(collectnum[i]);
            passageList.add(passage);
        }
        return passageList;
    }

}



