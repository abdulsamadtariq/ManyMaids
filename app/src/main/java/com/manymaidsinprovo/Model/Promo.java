package com.manymaidsinprovo.Model;

public class Promo {

    private int promoCodeId;
    private String code;
    private  int percentage;
    private int status;

    public Promo() {

    }

    public Promo(int promoCodeId, String code, int percentage, int status) {
        this.promoCodeId = promoCodeId;
        this.code = code;
        this.percentage = percentage;
        this.status = status;
    }

    public int getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(int promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return code;
    }
}
