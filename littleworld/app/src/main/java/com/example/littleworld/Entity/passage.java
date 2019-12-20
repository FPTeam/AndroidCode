package com.example.littleworld.Entity;


public class passage {

    /**
     *
     */
    private String name;//姓名
    private String content;//说说内容
    private String imgpath;//说说图片路径
    private String PostTime;//发布时间
    private String ChangeTime;//修改时间
    private String PostPlace;//发布地点
    private String Headpath;//用户头像
    private String Tag;//标签
    private int passageid;//文章标号
    private int userid;//用户名
    private int LikeNumber;//喜欢数量
    private int CommentNumber;//评论数
    private int CollectNumber;//收藏数

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

    public String getTag() {
        return Tag;
    }

    public void setTag(String Tag) {
        Tag = Tag;
    }

    public String getHeadpath() {
        return Headpath;
    }

    public void setHeadpath(String Headpath) {
        this.Headpath = Headpath;
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
                ", Headpath='" + Headpath + '\'' +
                ", Tag='" + Tag + '\'' +
                ", passageid=" + passageid +
                ", userid=" + userid +
                ", LikeNumber=" + LikeNumber +
                ", CommentNumber=" + CommentNumber +
                ", CollectNumber=" + CollectNumber +
                '}';
    }


}
