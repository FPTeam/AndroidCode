package com.example.littleworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        /* 编辑用户名和密码 */


        /* 实现账号管理的功能 */
        Button button1 = (Button) findViewById(R.id.alterAccount);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsAccountsActivity.class);
                startActivity(intent);
            }
        });

        /* 实现密码设置的功能 */
        Button button2 = (Button) findViewById(R.id.setPassword);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsPasswordActivity.class);
                startActivity(intent);
            }
        });

        /* 实现隐私设置的功能 */
        Button button3 = (Button) findViewById(R.id.setPrivacy);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsPrivacyActivity.class);
                startActivity(intent);
            }
        });

        /* 实现联系我们的功能 */
        Button button4 = (Button) findViewById(R.id.contactUs);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsContactusActivity.class);
                startActivity(intent);
            }
        });



    }
}

