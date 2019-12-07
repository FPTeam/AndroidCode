package com.example.littleworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//后期对数据库的操作请在此类中添加函数
public class DbHelper{
    private static SQLiteDatabase db;
    private static DbHelper dbhelper=null;
    private DbHelper(){}
    public static DbHelper getInstance()//单例模式
    {
        if (dbhelper == null) {
             dbhelper = new DbHelper();
        }
        return dbhelper;
    }
    public void openDatabase(String path)
    {
        this.db=SQLiteDatabase.openOrCreateDatabase(
                path, null);//获取当前数据库
    }
    public int testUser(String username,String password)//验证用户名和密码
    {
        Cursor login1 = db.rawQuery("select userid from login where name=?and password=?",new String[]{username,password});
        if(login1.getCount()!=0)
        {
            login1.moveToNext();
            return login1.getInt(0);
        }
        login1.close();
        return -1;
    }
    public void insertUser(String name,String password)//注册新用户插login表
    {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("password", password);
        //userid自动生成
        long i=db.insert("login",null,cv);
        if(i!=-1)
        {
            Log.d("insertusersuccessfully!","haha");
        }
    }
    public void insertUserInfo(){}//注册新用户插info表（包括图片、...）
    //...此处添加表的操作函数
    public void closeDb()
    {
        db.close();
    }
}
