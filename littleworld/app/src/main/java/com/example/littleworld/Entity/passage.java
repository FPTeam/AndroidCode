package com.example.littleworld.Entity;


public class passage {

    /**
     *
     */
    private String name,content,imgpath,PostTime,ChangeTime,PostPlace;
    private int passageid,userid,LikeNumber,CommentNumber,CollectNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgpath() { return imgpath; }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
    }

    public String getChangeTime() {
        return ChangeTime;
    }

    public void setChangeTime(String changeTime) {
        ChangeTime = changeTime;
    }

    public String getPostPlace() {
        return PostPlace;
    }

    public void setPostPlace(String postPlace) {
        PostPlace = postPlace;
    }

    public int getPassageid() {
        return passageid;
    }

    public void setPassageid(int passageid) {
        this.passageid = passageid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getLikeNumber() {
        return String.valueOf(LikeNumber);
    }

    public void setLikeNumber(int likeNumber) {
        LikeNumber = likeNumber;
    }

    public String getCommentNumber() {
        return String.valueOf(CommentNumber);
    }

    public void setCommentNumber(int commentNumber) {
        CommentNumber = commentNumber;
    }

    public String getCollectNumber() {
        return String.valueOf(CollectNumber);
    }

    public void setCollectNumber(int collectNumber) {
        CollectNumber = collectNumber;
    }

    @Override
    public String toString() {
        return "passage{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", imgpath='" + imgpath + '\'' +
                ", PostTime='" + PostTime + '\'' +
                ", ChangeTime='" + ChangeTime + '\'' +
                ", PostPlace='" + PostPlace + '\'' +
                ", passageid=" + passageid +
                ", userid=" + userid +
                ", LikeNumber=" + LikeNumber +
                ", CommentNumber=" + CommentNumber +
                ", CollectNumber=" + CollectNumber +
                '}';
    }


}
