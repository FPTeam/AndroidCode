package com.example.littleworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private AppBarLayout appbar;
    private ProfileViewPager viewPager;
    private FragmentManager fm;
    private TextView title; // 页面标题设置为用户名
    private TabLayout tabLayout;
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        appbar = findViewById(R.id.appbar);
        viewPager = findViewById(R.id.viewPager);
        title = findViewById(R.id.title); // 页面标题设置为用户名
        tabLayout = findViewById(R.id.tabs);
        fm = getSupportFragmentManager();

        // 设置状态栏用户名可见性
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {// 折叠状态
                    title.setVisibility(View.VISIBLE); // 显示用户名
                } else {// 非折叠状态
                    title.setVisibility(View.GONE); // 隐藏用户名
                }
            }
        });

        // 设置adapter
        viewPager.setAdapter(new ProfilePageAdapter(ProfileActivity.this, fm, viewPager));

        // 关联tabLayout与viewPager
        tabLayout.setupWithViewPager(viewPager);

        // 设置页面切换监听器
        viewPager.addOnPageChangeListener(new ProfileViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.resetHeight(position);
                (findViewById(R.id.item_detail_container)).scrollTo(0, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        viewPager.resetHeight(0);

        /*
        // 跳转至设置页面
        ImageButton photoBtn=findViewById(R.id.settingsButton);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(); // 待补充
                startActivity(intent);
            }
        });
        */

        // 返回至上一界面
        ImageButton backBtn=findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });
    }

}
