package com.example.littleworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingsAccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_accounts);

        Button button = (Button) findViewById(R.id.newAccountButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteToPre(getBaseContext());
                Intent intent = new Intent(SettingsAccountsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 返回至上一界面
        ImageButton backBtn=findViewById(R.id.imageButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsAccountsActivity.this.finish();
            }
        });

    }

    public static void deleteToPre(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("userInfo",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        //editor.clear();
        editor.putString("PASSWORD","");
        editor.commit();
    }


}
