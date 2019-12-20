package com.example.littleworld;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SettingEditInfo extends AppCompatActivity {
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_edit_info);

        final EditText newName = findViewById(R.id.editInfoUserName);
        final EditText newIntroduction = findViewById(R.id.editTextIntroduction);

        Button affirmBtn = findViewById(R.id.editInfoAffirmBtn);
        affirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newName.getText().toString();
                String intro = newIntroduction.getText().toString();
                boolean rep=DbHelper.getInstance().repUser(name);
                if(rep) {
                    Toast toast = Toast.makeText(getApplicationContext(), "该用户名已被占用", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    DbHelper.getInstance().insertUserInfo(DbHelper.getInstance().getUserId(),name,intro,null,null);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                                /*Intent  intent=new Intent(RegActivity.this, LoginActivity.class);
                                startActivity(intent);*/
                            SettingEditInfo.this.finish();
                        }
                    }, 2000);
                }
            }
        });
    }
}

//created by ttl
