package com.example.trkz_mobile;

public class ProductModel {

    String yeniKodu;
    String yeniTanim;

    public ProductModel(String yeniKodu, String yeniTanim) {
        this.yeniKodu = yeniKodu;
        this.yeniTanim = yeniTanim;
    }

    public String getYeniKodu() {
        return yeniKodu;
    }

    public void setYeniKodu(String yeniKodu) {
        this.yeniKodu = yeniKodu;
    }

    public String getYeniTanim() {
        return yeniTanim;
    }

    public void setYeniTanim(String yeniTanim) {
        this.yeniTanim = yeniTanim;
    }

    @Override
    public String toString(){
        return " yeniKodu: "+ getYeniKodu()+
                "\n yeniTanim: "+ getYeniTanim();
    }
}
