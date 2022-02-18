package com.example.trkz_mobile;

public class ProductModel {

    String yeniKodu;
    String yeniTanim;
    String eskiKod;
    Boolean hata;
    String hataMesaji;

    public ProductModel(String yeniKodu, String yeniTanim, String eskiKod) {
        this.yeniKodu = yeniKodu;
        this.yeniTanim = yeniTanim;
        this.eskiKod = eskiKod;
    }
    public ProductModel(String yeniKodu, String yeniTanim, Boolean hata, String hataMesaji) {
        this.yeniKodu = yeniKodu;
        this.yeniTanim = yeniTanim;
        this.hata = hata;
        this.hataMesaji = hataMesaji;
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

    public String getEskiKod() {
        return eskiKod;
    }

    public void setEskiKod(String eskiKod) {
        this.eskiKod = eskiKod;
    }

    public Boolean getHata() {
        return hata;
    }

    public void setHata(Boolean hata) {
        this.hata = hata;
    }

    public String getHataMesaji() {
        return hataMesaji;
    }

    public void setHataMesaji(String hataMesaji) {
        this.hataMesaji = hataMesaji;
    }

    @Override
    public String toString(){
        return " \neskiKodu: "+ getEskiKod()+
                " \nyeniKodu: "+ getYeniKodu()+
                "\n yeniTanim: "+ getYeniTanim()+
                "\n Hata: "+ getHata()+
                "\n Hata mesaji: "+ getHataMesaji();
    }
}
