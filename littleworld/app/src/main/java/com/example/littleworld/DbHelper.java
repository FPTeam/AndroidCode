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
import java.util.ArrayList;

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
        Cursor login1 = db.rawQuery("select userid from login where Name=?and Password=?",new String[]{username,password});
        if(login1.getCount()!=0)
        {
            login1.moveToNext();
            return login1.getInt(0);
        }
        login1.close();
        return -1;
    }

    public int repUser(String name)//验证注册时用户名是否重复
    {
        Cursor login2 = db.rawQuery("select userid from login where Name=?",new String[]{name});//验证用户名是否重复
        if(login2.getCount()!=0)
        {
            login2.moveToNext();
            return login2.getInt(0);
        }
        login2.close();
        return -1;
    }

    public void insertUser(String name,String password)//注册新用户插login表
    {
        ContentValues cv = new ContentValues();
        cv.put("Name", name);
        cv.put("Password", password);
        //userid自动生成
        long i=db.insert("login",null,cv);
        if(i!=-1)
        {
            Log.d("insertusersuccessfully!","haha");
        }
    }

    //    传参说明：用户号userid，文章内容content，图片路径：imgPath，文章发表时间：posttime，文章修改时间：changetime，发表地点：postplace
    //    注意userid，content，postplace为非空！
    public void insertPassage(Integer userid, String content, String imgPath, String posttime, String changetime , String postplace)//发布文章
    {
        ContentValues cv = new ContentValues();
        cv.put("UserId",userid);
        cv.put("Content", content);
        cv.put("PostImgPath", imgPath);
        cv.put("PostTime",posttime);
        cv.put("ChangeTime",changetime);
        cv.put("PostPlace",postplace);
        //passageId自动生成
        long p=db.insert("passage",null,cv);
        if(p!=-1)
        {
            Log.d("insert passage!","haha");
        }
    }

    public int[] getProvices(Integer userid){
        int[] provinces = new int[32];//32个省市，去过置1，没去过置0
        String city;
        //查userid的所有帖子
        Cursor curcity = db.rawQuery("select * from passage where UserId=?",new String[]{String.valueOf(userid)});
        while(curcity.moveToNext())
        {
            city=curcity.getString(6);
            Log.d("city success!",city);
            //for 每一个帖子 get城市city
            /*{
                //处理city字符串
                //switch（city）
                {
                case city1:
                    provinces[0] = 1;
                    break;
                case city1:
                    provinces[0] = 1;
                    break;
                case city1:
                    provinces[0] = 1;
                    break;
                case city1:
                    provinces[0] = 1;
                    break;
            ...
                defalut：；
                }
            }*/
        }
        curcity.close();
        return provinces;
    }
    public void insertUserInfo(){}//注册新用户插info表（包括图片、...）
    //...此处添加表的操作函数
    public void closeDb()
    {
        db.close();
    }

    //    传参说明：用户号userid，原用户密码password
    public boolean testPassword(Integer userid,String password)//验证密码
    {
        Cursor login3 = db.rawQuery("select userid from login where UserId=?and Password=?",new String[]{String.valueOf(userid),password});
        if(login3.getCount()!=0)
        {
            login3.moveToNext();
            return true;
        }
        login3.close();
        return false;
    }
}
