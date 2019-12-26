package com.example.littleworld.Notice;

public class Notice {
    private String name;
    private String msg;
    private String image;
    private int sendUserId;
    private  String time;

    public Notice() {
        this.name = "";
        this.msg = "";
        this.image = "";
    }
    public Notice(String name, String meg, String image) {
        this.name = name;
        this.msg = msg;
        this.image = image;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setSendUserId(int sendUserId){
        this.sendUserId = sendUserId;
    }

    public void setTime(String time){
        this.time = time;
    }



    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public String  getImage() {
        return image;
    }

    public int getSendUserId(){
        return sendUserId;
    }

    public String getTime(){
        return time;
    }
}
