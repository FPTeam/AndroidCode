package com.example.littleworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
            Date:
            2019/12/1   注册界面xml界面+java代码调整头像显示为圆形
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        //加入从数据库中找到头像的方法，并存入drawable里面的defaultSculp.jpg

        //裁剪头像为圆形
        ImageView sculpture=findViewById(R.id.sculpture);
        sculpture.setImageResource(R.drawable.default_sculp);
        sculpture.setElevation(20f);
        sculpture.setClipToOutline(true);
        sculpture.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0,0,view.getWidth(), view.getHeight());
            }
        });

        // 插入头像，跳转至调用摄像头页面
        ImageButton photoBtn=findViewById(R.id.uploadSculp);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegActivity.this, ReleaseDynamics.class);
                startActivity(intent);
            }
        });

        // 返回至注册界面
        ImageButton backBtn=findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegActivity.this.finish();
            }
        });

    }
}
