package com.example.littleworld;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.littleworld.BottomMenu.NavigationActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private SQLiteDatabase db;//方法内对象能否访问？
    private int userId;//方法内对象能否访问？
    private Button login,logup;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            BACK_LOCATION_PERMISSION
    };

    private static final int PERMISSON_REQUESTCODE = 0;
    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private boolean needCheckBackLocation = false;
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACK_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    public LoginActivity() {

    }
    private String opendb() throws IOException
    {//这个函数必须放在LoginActivity 因为要找路径 不能放到数据库类==！
        String DATABASE_PATH=this.getApplicationContext().getFilesDir().toString();
        String DATABASE_NAME="lw.db";
        String databaseFilename=DATABASE_PATH+"/"+DATABASE_NAME;
        //配置文件或者文件夹路径参数
        File dir = new File(DATABASE_PATH);
        // 如果目录不存在，创建这个目录
        if (!dir.exists())
            dir.mkdir();
        if (!(new File(databaseFilename)).exists())
        {
            // 获得封装lw.db文件的InputStream对象
            InputStream is = getResources().openRawResource(
                    R.raw.lw);
            FileOutputStream fos = new FileOutputStream(databaseFilename);
            byte[] buffer = new byte[8192];
            int count = 0;
            // 开始复制dictionary.db文件
            while ((count = is.read(buffer)) > 0)
            {
                fos.write(buffer, 0, count);
            }

            fos.close();
            is.close();
            //数据库路径：data/data/< package name >/files/
        }
        return databaseFilename;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACK_LOCATION_PERMISSION
            };
            needCheckBackLocation = true;
        }

        setContentView(R.layout.login);
        try {
            DbHelper.getInstance().openDatabase(opendb());//以后使用DbHelper务必先调用.getInstance()获取唯一对象
        } catch (IOException e) {
            e.printStackTrace();
        }

        login = findViewById(R.id.login_button);
        logup = findViewById(R.id.cancel);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText un = findViewById(R.id.username_login);
                EditText pw = findViewById(R.id.password_login);
                String username = un.getText().toString();
                String password = pw.getText().toString();
                int i=DbHelper.getInstance().testUser(username,password);
                if(i==-1)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "用户名/密码错误！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else
                {
                    userId =i;
                    Toast toast = Toast.makeText(getApplicationContext(), "userId="+ userId, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    //跳转到主界面，并传递userid过去...
                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(intent);
                }
        }});

        // 跳转到注册界面
        Button signup = findViewById(R.id.signUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });

        // 退出程序
        logup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
                //Intent intent = new Intent(LoginActivity.this, SquareActivity.class);
                //startActivity(intent);
            }
        });
    }


    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        try{
            super.onResume();
            if (Build.VERSION.SDK_INT >= 23) {
                if (isNeedCheck) {
                    checkPermissions(needPermissions);
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    /**
     * @param
     * @since 2.5.0
     */
    @TargetApi(23)
    private void checkPermissions(String... permissions) {
        try{
            if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    try {
                        String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                        Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class, int.class});
                        method.invoke(this, array, 0);
                    } catch (Throwable e) {

                    }
                }
            }

        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    @TargetApi(23)
    private List<String> findDeniedPermissions(String[] permissions) {
        try{
            List<String> needRequestPermissonList = new ArrayList<String>();
            if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
                for (String perm : permissions) {
                    if (checkMySelfPermission(perm) != PackageManager.PERMISSION_GRANTED
                            || shouldShowMyRequestPermissionRationale(perm)) {
                        if(!needCheckBackLocation
                                && BACK_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            }
            return needRequestPermissonList;
        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    private int checkMySelfPermission(String perm) {
        try {
            Method method = getClass().getMethod("checkSelfPermission", new Class[]{String.class});
            Integer permissionInt = (Integer) method.invoke(this, perm);
            return permissionInt;
        } catch (Throwable e) {
        }
        return -1;
    }

    private boolean shouldShowMyRequestPermissionRationale(String perm) {
        try {
            Method method = getClass().getMethod("shouldShowRequestPermissionRationale", new Class[]{String.class});
            Boolean permissionInt = (Boolean) method.invoke(this, perm);
            return permissionInt;
        } catch (Throwable e) {
        }
        return false;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        try{
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
        return true;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        try{
            if (Build.VERSION.SDK_INT >= 23) {
                if (requestCode == PERMISSON_REQUESTCODE) {
                    if (!verifyPermissions(paramArrayOfInt)) {
                        showMissingPermissionDialog();
                        isNeedCheck = false;
                    }
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限");

            // 拒绝, 退出应用
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });

            builder.setPositiveButton("设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startAppSettings();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });

            builder.setCancelable(false);

            builder.show();
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        try{
            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
