package com.example.littleworld.Notice;

public class Notice {
    private String name;
    private String meg;
    private String image;

    public Notice(String name, String meg, String image) {
        this.name = name;
        this.meg = meg;
        this.image = image;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setMeg(String meg){
        this.meg = meg;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getMeg() {
        return meg;
    }

    public String  getImage() {
        return image;
    }
}
