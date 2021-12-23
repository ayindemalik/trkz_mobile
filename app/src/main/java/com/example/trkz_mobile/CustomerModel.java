package com.example.trkz_mobile;

public class CustomerModel {
    private String musteriKodu;
    private String musteriAdi;
    private String musteriID;
    private String adres;
    private String ilce;
    private String sehir;
    private String ulke;

    public CustomerModel(String musteriID, String musteriAdi) {
        this.musteriAdi = musteriAdi; this.musteriID = musteriID;
    }

    public CustomerModel( String musteriKodu, String musteriAdi, String musteriID, String adres, String ilce, String sehir, String ulke) {
        this.musteriKodu = musteriKodu; this.musteriAdi = musteriAdi; this.musteriID = musteriID; this.adres = adres;
        this.ilce = ilce; this.sehir = sehir; this.ulke =  ulke;
    }

    public String getMusteriKodu() {
        return musteriKodu;
    }

    public void setMusteriKodu(String musteriKodu) {
        this.musteriKodu = musteriKodu;
    }

    public String getMusteriAdi() {
        return musteriAdi;
    }

    public void setMusteriAdi(String musteriAdi) {
        this.musteriAdi = musteriAdi;
    }

    public String getMusteriID() {
        return musteriID;
    }

    public void setMusteriID(String musteriID) {
        this.musteriID = musteriID;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getIlce() {
        return ilce;
    }

    public void setIlce(String ilce) {
        this.ilce = ilce;
    }

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getUlke() {
        return ulke;
    }

    public void setUlke(String ulke) {
        this.ulke = ulke;
    }

    @Override
    public String toString(){
        return " musteriID: "+ getMusteriID()+
                "\n Adi: "+ getMusteriAdi();
    }

}
