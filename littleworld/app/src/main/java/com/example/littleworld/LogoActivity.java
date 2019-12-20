package com.example.littleworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.littleworld.BottomMenu.NavigationActivity;

public class LogoActivity extends Activity {
    private int userId;
    private ProgressBar progressBar;
    private Button backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除标题
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logo);

        progressBar = (ProgressBar) findViewById(R.id.pgBar);
        backButton = (Button) findViewById(R.id.btn_back);
        progressBar.setMax(3000);

        Intent intentIn = getIntent();
        String username = intentIn.getStringExtra("USERNAME");
        String password = intentIn.getStringExtra("PASSWORD");
        int i=DbHelper.getInstance().testUser(username,password);
        if(i==-1)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "用户名/密码错误！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            finish();
        }
        else
        {
            userId =i;
            //在本地数据库中设置userId
            DbHelper.getInstance().setUserId(userId);
            //Toast toast = Toast.makeText(getApplicationContext(), "userId="+ userId, Toast.LENGTH_SHORT);
            //toast.setGravity(Gravity.CENTER, 0, 0);
            //toast.show();
            //跳转到主界面，并传递userid过去...

            Intent intent = new Intent(LogoActivity.this, NavigationActivity.class);
            intent.putExtra("user_id",userId);
            startActivity(intent);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

}
