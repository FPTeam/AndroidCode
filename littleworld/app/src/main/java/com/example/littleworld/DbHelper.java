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
            String prefix = city.substring(0,2);
            Log.d("city",city);
            Log.d("prefix",prefix);
            int[] provinceList= {R.mipmap.zhejiang,R.mipmap.xinjiang,R.mipmap.xizang,R.mipmap.yunnan,R.mipmap.taiwan,R.mipmap.tianjin,R.mipmap.sichuan,R.mipmap.shandong,
                    R.mipmap.shanghai,R.mipmap.qinghai,R.mipmap.shan1xi,R.mipmap.shan3xi,R.mipmap.ningxia,R.mipmap.neimenggu,R.mipmap.liaoning,R.mipmap.jiangxi,
                    R.mipmap.jilin,R.mipmap.jiangsu,R.mipmap.hubei,R.mipmap.hunan,R.mipmap.heilongjiang,R.mipmap.henan,R.mipmap.guizhou,R.mipmap.hainan,
                    R.mipmap.hebei,R.mipmap.guangxi,R.mipmap.gansu,R.mipmap.guangdong,R.mipmap.chongqing,R.mipmap.fujian,R.mipmap.anhui,R.mipmap.beijing};

            switch(prefix)
            {
                case "浙江":provinces[0]=1;break;
                case "新疆":provinces[1]=1;break;
                case "西藏":provinces[2]=1;break;
                case "云南":provinces[3]=1;break;
                case "台湾":provinces[4]=1;break;
                case "天津":provinces[5]=1;break;
                case "四川":provinces[6]=1;break;
                case "山东":provinces[7]=1;break;
                case "上海":provinces[8]=1;break;
                case "青海":provinces[9]=1;break;
                case "山西":provinces[10]=1;break;
                case "陕西":provinces[11]=1;break;
                case "宁夏":provinces[12]=1;break;
                case "内蒙":provinces[13]=1;break;
                case "辽宁":provinces[14]=1;break;
                case "江西":provinces[15]=1;break;
                case "吉林":provinces[16]=1;break;
                case "江苏":provinces[17]=1;break;
                case "湖北":provinces[18]=1;break;
                case "湖南":provinces[19]=1;break;
                case "黑龙":provinces[20]=1;break;
                case "河南":provinces[21]=1;break;
                case "贵州":provinces[22]=1;break;
                case "海南":provinces[23]=1;break;
                case "河北":provinces[24]=1;break;
                case "广西":provinces[25]=1;break;
                case "甘肃":provinces[26]=1;break;
                case "广东":provinces[27]=1;break;
                case "重庆":provinces[28]=1;break;
                case "福建":provinces[29]=1;break;
                case "安徽":provinces[30]=1;break;
                case "北京":provinces[31]=1;break;
                default:break;
            }
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
}
