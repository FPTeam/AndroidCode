package com.example.littleworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class SettingsActivity extends Fragment {
/**  java tai cai bu hui chong fu diao yong    **/
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private int userId;
    private ImageView picture;
    private Uri imageUri;
    //图片
    private Bitmap bitmap;
    //保存的文件路径
    private File fileDir;
    // 声明PopupWindow
    private PopupWindow popupWindow;
    // 声明PopupWindow对应的视图
    private View popupView;
    // 声明平移动画
    private TranslateAnimation animation;
    /**    **/
    private String userName;
    private String introduction;
    Cursor cursor;

    public SettingsActivity(int userId){
        super();
        this.userId = userId;
    }





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View layout = inflater.inflate(R.layout.settings_main, container, false);

//        String DATABASE_PATH=getActivity().getApplicationContext().getFilesDir().toString();
//        String DATABASE_NAME="lw.db";
//        String databaseFilename=DATABASE_PATH+"/"+DATABASE_NAME;
//        db = DbHelper.getInstance().openDatabase(databaseFilename);//以后使用DbHelper务必先调用.getInstance()获取唯一对象
//        setting = DbHelper.getInstance().getUserBook(190001);

        Toast.makeText(getActivity().getApplicationContext(), "Holle World!", Toast.LENGTH_SHORT).show();
        cursor = DbHelper.getInstance().getUserBook(DbHelper.getInstance().getUserId());
        String name = new String();
        if(cursor.getCount()!=0){
            cursor.moveToNext();
            name = cursor.getString(1);
        }else{
            name = "null";
        }
//            String name = DbHelper.getInstance().getUserBook(190001);
//            String name = setting.getString(setting.getColumnIndex("UserName"));
        //String intro = cursor.getString(cursor.getColumnIndex("UserInfo"));
//            setting = DbHelper.getInstance().getUserBook(userId);

        if(name == null){
            name = ""+"kongdea";
        }


        TextView userName = layout.findViewById(R.id.username);
        TextView introduction = layout.findViewById(R.id.introduction);
        userName.setText(name);
        //          introduction.setText(intro);


        /* 跳转至个人主页 */
        ImageButton btn_sculpture = layout.findViewById(R.id.sculpture);
        btn_sculpture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("user_id",userId);
                startActivity(intent);
            }
        });

        /* 编辑用户信息 */
        ImageButton btn_edit = layout.findViewById(R.id.editInfoEnter);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingEditInfo.class);
                startActivity(intent);
            }
        });

        /* 实现账号管理的功能 */
        Button btn_alterAccount = (Button) layout.findViewById(R.id.alterAccount);
        btn_alterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsAccountsActivity.class);
                startActivity(intent);
            }
        });

        /* 实现密码设置的功能 */
        Button button2 = (Button) layout.findViewById(R.id.setPassword);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsPasswordActivity.class);
                startActivity(intent);
            }
        });

        /* 实现隐私设置的功能 */
        Button button3 = (Button) layout.findViewById(R.id.setPrivacy);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsPrivacyActivity.class);
                startActivity(intent);
            }
        });

        /* 实现联系我们的功能 */
        Button button4 = (Button) layout.findViewById(R.id.contactUs);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsContactusActivity.class);
                startActivity(intent);
            }
        });
        return layout;
    }

}

