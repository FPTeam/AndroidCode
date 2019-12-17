package com.example.littleworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsPasswordActivity extends AppCompatActivity{

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_password);

        /* 实现密码修改的功能 */
        boolean correct = false;
        final String userId = "";
        editText = (EditText) findViewById(R.id.oldPassword);
        final String oldPassword = editText.getText().toString();
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 传递userID和旧密码给DbHelper类 */
                Intent intent = new Intent(SettingsPasswordActivity.this, DbHelper.class);
                intent.putExtra("userId", userId);
                intent.putExtra("oldPassword", oldPassword);
                startActivity(intent);
            }
        });


        /* 账户名和密码验证成功 */
        if (correct == true){
            /* 显示提示信息 */
        }


        /*对旧密码进行修改*/
        editText = (EditText) findViewById(R.id.newPassword);
        final String newPassword = editText.getText().toString();
        Intent intent = new Intent(SettingsPasswordActivity.this, DbHelper.class);
        intent.putExtra("userId", userId);
        intent.putExtra("newPassword", newPassword);
        startActivity(intent);


    }
}

