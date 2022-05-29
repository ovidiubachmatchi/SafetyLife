package com.protect.safetylife.controller.stealprotection;

public class SliderItem {

    private String image;
    private int img = -1;

    public SliderItem(String image) {
        this.image = image;
    }

    public SliderItem(int image) {
        this.img = image;
    }

    public String getImage(){
        return image;
    }

    public int getImg(){
        return img;
    }
}
