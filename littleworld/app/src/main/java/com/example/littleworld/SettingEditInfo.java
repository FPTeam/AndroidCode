package com.example.littleworld;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingEditInfo extends AppCompatActivity {
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_edit_info);

        final EditText newName = findViewById(R.id.editInfoUserName);
        final EditText newIntroduction = findViewById(R.id.editTextIntroduction);
        PersonInfo personInfo = DbHelper.getInstance().getUserInfo(DbHelper.getInstance().getUserId());
        newName.setText(personInfo.name);
        newIntroduction.setText(personInfo.intro);

        Button affirmBtn = findViewById(R.id.editInfoAffirmBtn);
        affirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newName.getText().toString();
                String intro = newIntroduction.getText().toString();
                int nameSize = name.length();

                if(nameSize > 2) {

                    Toast toast = Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    DbHelper.getInstance().insertUserInfo(DbHelper.getInstance().getUserId(),name,intro,null,null);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
//                                Intent intent=new Intent(SettingEditInfo.this, SettingsActivity.class);
//                                startActivity(intent);
                            SettingEditInfo.this.finish();
                        }
                    }, 500);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "昵称太短!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });
    }
}

//created by ttl
