package com.example.littleworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.littleworld.Entity.passage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//后期对数据库的操作请在此类中添加函数
public class DbHelper{
    private static SQLiteDatabase db;
    private static DbHelper dbhelper=null;
    private int userId;
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

    public void setUserId(int userId){
        this.userId = userId;
    }

    //调用这个函数获取userId,使用方法int i = DbHelper.getInstance().getUserId(); created by tll;
    public int getUserId(){
        return userId;
    }

    //获取登录表中指定userId的数据
    public Cursor getLoginBook(int userId){
        Cursor cursor = db.query("login",null,"Password=?",new String[]{Integer.toString(userId)},null,null,null);
        return cursor;
    }

    //获取用户表中指定userId的数据
    public Cursor getUserBook(int userId){
        Cursor cursor = db.query("user",null,"UserId=?",new String[]{Integer.toString(userId)},null,null,null);
        return cursor;
    }

    /*******登陆时验证用户名和密码*******/
    public int testUser(String username,String password)//验证用户名和密码
    {
        Cursor login1 = db.rawQuery("select UserId from login where Name=?and Password=?",new String[]{username,password});
        if(login1.getCount()!=0)
        {
            login1.moveToNext();
            userId = login1.getInt(0);
            return userId;
        }
        login1.close();
        return -1;
    }

    /*******验证注册或者修改时用户名是否重复*******/
    //重复返回真值，不重复返回假值
    public boolean repUser(String name)
    {
        Cursor login2 = db.rawQuery("select UserId from login where Name=?",new String[]{name});//验证用户名是否重复
        if(login2.getCount()!=0)
        {
            login2.moveToNext();
            return true;
        }
        login2.close();
        return false;
    }

    /*******注册新用户*******/
    public void insertUser(String name,String password)//注册新用户插login表
    {
        ContentValues cv = new ContentValues();
        cv.put("Name", name);          //这是登录名,不是用户名
        cv.put("Password", password);
        cv.put("DisplayedName", name); //默认显示的账户名为登录名
        //userid自动生成
        long i=db.insert("login",null,cv);
        if(i!=-1)
        {
            Log.d("insertusersuccessfully!","haha");
        }
    }

    /*******验证密码*******/
    //    传参说明：用户号userid，原用户密码password
    public boolean testPassword(Integer userid,String password)
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

    /*******修改密码*******/
    //    传参说明：用户号userid，要修改的用户密码password
    public void UpdateLogin(Integer userid,String password)
    {
        ContentValues cv = new ContentValues();
        cv.put("Password", password);
        String[] args={String.valueOf(userid)};
        long p=db.update("login",cv,"userid=?",args);
        if(p!=-1)
        {
            Log.d("update user password!","haha");
        }
    }

    /*******发布文章*******/
    //    传参说明：用户号userid，文章内容content，图片路径：imgPath，文章发表时间：posttime，文章修改时间：changetime，发表地点：postplace
    //    注意userid，content，postplace为非空！
    //    返回文章号
    public int insertPassage(Integer userid, String content, String imgPath, String posttime, String changetime , String postplace)
    {
        ContentValues cv = new ContentValues();
        cv.put("UserId",userid);
        cv.put("Content", content);
        cv.put("PostImgPath", imgPath);
        cv.put("PostTime",posttime);
        cv.put("ChangeTime",changetime);
        cv.put("PostPlace",postplace);

        long p=db.insert("passage",null,cv);
        if(p!=-1)
        {
            Log.d("insert passage!","haha");
        }
        //passageId自动生成
        Cursor cursor = db.rawQuery("select last_insert_rowid() from passage", null);
        int passageid=0;
        if (cursor.moveToFirst()) passageid= cursor.getInt(0);
        Log.i("testAuto", passageid+"");

        return passageid;
    }

    /*******添加和更改用户的性别、头像图片、个人介绍等信息*******/
    //  传参说明：用户号userid，用户信息UserInfo，性别：Sex，头像图片链接：head
    //  注册时用户号和用户名已经插入到user表，所以其余信息都是更新时补充添加进去的
    public void insertUserInfo(Integer userid, String UserName, String UserInfo, String Sex, String head)//添加或更改用户信息
    {
        ContentValues cv = new ContentValues();
        if(UserName!=null)
            cv.put("UserName", UserName);
        if(UserInfo!=null)
            cv.put("UserInfo", UserInfo);
        if(Sex!=null)
            cv.put("Sex", Sex);
        if(head!=null)
            cv.put("Head",head);
        String[] args={String.valueOf(userid)};
        long p=db.update("user",cv,"userid=?",args);
        if(p!=-1)
        {
            Log.d("insert user info!","haha");
        }
    }

    /*******获取个人动态信息，可根据特定用户查询*******/
    //    传参说明：偏移量offset，一次返回文章的数量row
    // DbHelper.getInstance().searchownPassage(0, 5, Integer.toString（userid）);
    //    返回值：动态信息的List

    public List<passage> searchownPassage(int offset, int row, String userid){
        List<passage> passageinfo=new ArrayList<>();

        String table = "passage";//表名
        String[] columns = null;//返回的列名，null返回所有列
        String selection = null;//SQL WHERE子句（不包括WHERE本身），null返回所有行
        String[] selectionArgs = null;
        String groupBy = null;//分组过滤器
        String having = null;
        String orderBy = "PostTime DESC";//排序
        String limit =offset+","+row;//查询的偏移量和返回的行数
        selection = "UserId=?";
        selectionArgs =new String[]{userid};


        Cursor curpassage= db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        while(curpassage.moveToNext())
        {
            passage passage= new passage();
            userid=Integer.toString(curpassage.getInt(1));
            Cursor curuser= db.rawQuery("select * from user where UserId=?",new String[]{userid});
            if(curuser.getCount()!=0)//查找用户名和用户头像并添加
            {
                curuser.moveToNext();
                passage.setName(curuser.getString(1));
                passage.setHead(curuser.getString(4));
            }

            /*添加文章信息*/
            passage.setPassageid(curpassage.getInt(0));
            passage.setUserid(curpassage.getInt(1));
            passage.setContent(curpassage.getString(2 ));
            passage.setImgpath(curpassage.getString(3));
            passage.setPostTime(curpassage.getString(4));
            passage.setChangeTime(curpassage.getString(5));
            passage.setPostPlace(curpassage.getString(6));
            passage.setLikeNumber(curpassage.getInt(7));
            passage.setCommentNumber(curpassage.getInt(8));
            passage.setCollectNumber(curpassage.getInt(9));
            passageinfo.add(passage);
        }
        Log.d("列表", passageinfo.toString());
        return passageinfo;
    }

    /*******获取全部动态信息，可根据特定用户查询*******/
    //    传参说明：偏移量offset，一次返回文章的数量row
    //    调用时最好设row为常量，offset每次都要加上row
    //    例如：DbHelper.getInstance().allPassage(0,5,null);
    //          DbHelper.getInstance().allPassage(5,5,null);
    //          DbHelper.getInstance().allPassage(10,5,null);
    //          DbHelper.getInstance().allPassage(15,5,null);
    //    返回值：动态信息的List
    public List<passage> searchPassage(int offset, int row, String username){
        List<passage> passageinfo=new ArrayList<>();

        String table = "passage";//表名
        String[] columns = null;//返回的列名，null返回所有列
        String selection = null;//SQL WHERE子句（不包括WHERE本身），null返回所有行
        String[] selectionArgs = null;
        String groupBy = null;//分组过滤器
        String having = null;
        String orderBy = "PostTime DESC";//排序
        String limit =offset+","+row;//查询的偏移量和返回的行数

        String userid="";
        if(username!=null){//当要查询特定用户时，获取用户id，查询条件修改
            Cursor curuser= db.rawQuery("select * from user where UserName=?",new String[]{username});
            if(curuser.getCount()!=0)
            {
                curuser.moveToNext();
                userid=curuser.getString(0);
            }
            selection = "UserId=?";
            selectionArgs =new String[]{userid};
        }

        Cursor curpassage= db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        while(curpassage.moveToNext())
        {
            passage passage= new passage();
            userid=Integer.toString(curpassage.getInt(1));
            Cursor curuser= db.rawQuery("select * from user where UserId=?",new String[]{userid});
            if(curuser.getCount()!=0)//查找用户名和用户头像并添加
            {
                curuser.moveToNext();
                passage.setName(curuser.getString(1));
                passage.setHeadpath(curuser.getString(4));
            }

            /*添加文章信息*/
            passage.setPassageid(curpassage.getInt(0));
            passage.setUserid(curpassage.getInt(1));
            passage.setContent(curpassage.getString(2 ));
            passage.setImgpath(curpassage.getString(3));
            passage.setPostTime(curpassage.getString(4));
            passage.setChangeTime(curpassage.getString(5));
            passage.setPostPlace(curpassage.getString(6));
            passage.setLikeNumber(curpassage.getInt(7));
            passage.setCommentNumber(curpassage.getInt(8));
            passage.setCollectNumber(curpassage.getInt(9));
            passageinfo.add(passage);
        }
        Log.d("列表", passageinfo.toString());
        return passageinfo;
    }

    /*******查找点赞表*******/
    /*使用说明：Boolean check=DbHelper.getInstance().checkliked(userid, passageid);
                if(check)
            DbHelper.getInstance().insertliked(userid, passageid);
                else
                        DbHelper.getInstance().deleteliked(userid, passageid);
                        */
    //  传参说明：用户号userid，文章号passageid
    //返回真表明还没有记录，可添加，返回假表明已点赞
    public boolean checkliked(Integer userid, Integer passageid)
    {
        Cursor curchecklike= db.rawQuery("select * from liked where LikePassageId=?",new String[]{String.valueOf(passageid)});
        while(curchecklike.moveToNext())
        {
            if(userid==curchecklike.getInt(0))
                return false;
        }
        return true;
    }
    /*******查找收藏表*******/
    public boolean ifCollect(int userid,int passageid)
    {
        Cursor cursor = db.rawQuery("select * from collect where PassageId=?and CollectUserId=?",new String[]{Integer.toString(passageid),Integer.toString(userid)});
        if(cursor.getCount()!=0) {
            return true;
        }
        return false;
    }

    /*******添加点赞表*******/
    //  传参说明：用户号userid，文章号passageid
    public void insertliked(Integer userid, Integer passageid)//添加或更改用户信息
    {
        ContentValues cv = new ContentValues();
        cv.put("LikeUserId", userid);
        cv.put("LikePassageId", passageid);
        long p=db.insert("liked",null,cv);
        if(p!=-1)
        {
            Log.d("insert liked!","haha");
        }
    }
    /*******添加收藏表*******/
    public void insertCollect(int userid,int passageid)
    {
        ContentValues cv = new ContentValues();
        cv.put("CollectPassageId", passageid);
        cv.put("CollectUserId", userid);
        //userid自动生成
        long i=db.insert("collect",null,cv);
        if(i!=-1)
        {
            Log.d("insert collect!","haha");
        }
    }

    /*******删除点赞表*******/
    //  传参说明：用户号userid，文章号passageid
    public void deleteliked(Integer userid, Integer passageid)
    {
        long p=db.delete("liked","LikeUserId = ? and LikePassageId =?",
                new String[]{String.valueOf(userid),String.valueOf(passageid)});
        if(p!=-1)
        {
            Log.d("delete liked!","haha");
        }
    }
    /*******删除收藏表*******/
    public void deletecollect(int userid, int passageid)
    {
        ContentValues cv = new ContentValues();
        cv.put("CollectUserId", userid);
        cv.put("CollectPassageId", passageid);
        long p=db.delete("collect","CollectUserId = ? and CollectPassageId =?",
                new String[]{String.valueOf(userid),String.valueOf(passageid)});
        if(p!=-1)
        {
            Log.d("delete collect!","haha");
        }
    }

    
    /*******获取文章省份*******/
    //    传参说明：用户号userid
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

    public PersonInfo getUserInfo(int id){//获取用户名、头像、性别、介绍等
        //id = 190002;
        PersonInfo info0 = new PersonInfo();
        Cursor curcity = db.rawQuery("select * from user where UserId=?",new String[]{String.valueOf(id)});
        while(curcity.moveToNext()) {
            info0.name = curcity.getString(1);
            info0.intro = curcity.getString(2);
            info0.sex = curcity.getString(3);
            info0.img = curcity.getString(4);

            Log.d("用户名", info0.name);
            if(info0.intro == null)
                Log.d("自我介绍", "无");
            else
                Log.d("自我介绍", info0.intro);
            if(info0.sex==null)
                info0.sex = "保密";
            Log.d("性别", info0.sex);
            if(info0.img == null)
                Log.d("头像链接", "无");
            //else
                //Log.d("头像链接", info0.img);
        }
        curcity.close();
        return info0;
        /*

        String name = "";
        //Cursor info = db.rawQuery("select * from user where UserId=?",new String[]{String.valueOf(id)});
        Cursor info = db.rawQuery("select * from passage where UserId=?",new String[]{String.valueOf(id)});
        if(info.getCount()!=0)
        {

            info.getString(1);
           //Log.d("用户信息",name);
            Log.d("总列数",String.valueOf(info.getColumnCount()));
        }
        info.close();
        return name;*/
    }

    public void insertUserInfo(){}//注册新用户插info表（包括图片、...）
    //...此处添加表的操作函数
    public void closeDb()
    {
        db.close();
    }

    /*
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

     */
}
