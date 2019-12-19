package com.example.littleworld;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingsPasswordActivity extends AppCompatActivity{

    private EditText editTextOld;//旧密码
    private EditText editTextNew;//新密码
    private Button confirmButton;//确认修改按钮
    private ImageButton backBtn;//返回按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_password);

        editTextOld = findViewById(R.id.oldPassword);
        editTextNew = findViewById(R.id.newPassword);
        confirmButton = findViewById(R.id.confirmbutton);
        backBtn=findViewById(R.id.imageButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = editTextOld.getText().toString();
                String newPassword = editTextNew.getText().toString();
                if(TestPassword(oldPassword,newPassword) == 1){
                    DbHelper.getInstance().UpdateLogin(DbHelper.getInstance().getUserId(),newPassword);
                    Toast.makeText(getApplicationContext(), "修改成功!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                                /*Intent  intent=new Intent(RegActivity.this, LoginActivity.class);
                                startActivity(intent);*/
                            SettingsPasswordActivity.this.finish();
                        }
                    }, 2000);
                }else{

                }


            }
        });



        // 返回至上一界面
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsPasswordActivity.this.finish();
            }
        });
    }

    int TestPassword(String oldPassword,String newPassword){
        Cursor cursor = DbHelper.getInstance().getLoginBook(DbHelper.getInstance().getUserId());
        String password = new String();
        if(cursor.getCount()!=0){
            cursor.moveToNext();
            password = cursor.getString(2);
            if(oldPassword != password){
                Toast.makeText(getApplicationContext(), "旧密码输入错误!", Toast.LENGTH_SHORT).show();
                return 0;
            }else if(password == newPassword){
                Toast.makeText(getApplicationContext(), "新密码和旧密码相同!", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        cursor.close();
        return 1;
    }
}

