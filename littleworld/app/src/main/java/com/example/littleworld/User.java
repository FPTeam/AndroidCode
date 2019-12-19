package com.example.littleworld;

/* 创建一个用户类，BaiYunpeng写的一个类，别搞错了 */
public class User {
    private int userId;
    private int imageId;

    public User(int userId, int imageId){
        this.userId = userId;
        this.imageId = imageId;
    }

    public int getUserId(){

        return userId;
    }

    public int getImageId(){

        return imageId;
    }
}
