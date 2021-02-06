package com.manymaidsinprovo.Model;

public class Slide {

    private int slideId;
    private String slideImage,slideCaption;
    private int slideStatus;

    public Slide() {
    }

    public Slide(int slideId, String slideImage, String slideCaption, int slideStatus) {
        this.slideId = slideId;
        this.slideImage = slideImage;
        this.slideCaption = slideCaption;
        this.slideStatus = slideStatus;
    }

    public int getSlideId() {
        return slideId;
    }

    public String getSlideImage() {
        return slideImage;
    }

    public String getSlideCaption() {
        return slideCaption;
    }

    public int getSlideStatus() {
        return slideStatus;
    }
}
