package com.example.trkz_mobile;

public class UserModel {
    private int kullaniciRef ; //183</KullaniciRef>
    private int depoNo; // >1</DepoNo>
    private int sevkDepoNo; // >2</SevkDepoNo>
    private int bolumNo; //>42</BolumNo>
    private String barkodOnEk; //>KY</BarkodOnEk>
    private String takimBarkodOnek ; //>TY</TakimBarkodOnek>
    private int barkodUzunluk; // >10</BarkodUzunluk>
    private int takimEtiketID ; //>81</TakimEtiketID>
    private String takimEtiketPrinter; //>Takim1</TakimEtiketPrinter>
    private int paletEtiketID; //>77</PaletEtiketID>
    private int paletDetayliEtiketID; // >83</PaletDetayliEtiketID>
    private String paletEtiketPrinter; //>Palet1</PaletEtiketPrinter>
    private String yeniUretimBarkodOnEk ;// >CK</YeniUretimBarkodOnEk>
    private int cerabathEtiketID; //>97</CerabathEtiketID>
    private String locationNo; //>M020</LocationNo>
    private String contract; //>MRK</Contract>

    public UserModel(int kullaniciRef, int depoNo, int sevkDepoNo, int bolumNo, String barkodOnEk,
                     String takimBarkodOnek, int barkodUzunluk, int takimEtiketID,
                     String takimEtiketPrinter, int paletEtiketID, int paletDetayliEtiketID,
                     String paletEtiketPrinter, String yeniUretimBarkodOnEk, int cerabathEtiketID,
                     String locationNo, String contract) {
        this.kullaniciRef = kullaniciRef;
        this.depoNo = depoNo;
        this.sevkDepoNo = sevkDepoNo;
        this.bolumNo = bolumNo;
        this.barkodOnEk = barkodOnEk;
        this.takimBarkodOnek = takimBarkodOnek;
        this.barkodUzunluk = barkodUzunluk;
        this.takimEtiketID = takimEtiketID;
        this.takimEtiketPrinter = takimEtiketPrinter;
        this.paletEtiketID = paletEtiketID;
        this.paletDetayliEtiketID = paletDetayliEtiketID;
        this.paletEtiketPrinter = paletEtiketPrinter;
        this.yeniUretimBarkodOnEk = yeniUretimBarkodOnEk;
        this.cerabathEtiketID = cerabathEtiketID;
        this.locationNo = locationNo;
        this.contract = contract;
    }

    @Override
    public String toString(){
        return " kullaniciRef: "+ getKullaniciRef()+
                "\n depoNo: "+ getDepoNo()+
                "\n sevkDepoNo :"+getSevkDepoNo()+
                "\n bolumNo: "+ getBolumNo()+
                "\n barkodOnEk: "+ getBarkodOnEk()+
                "\n takimBarkodOnek: "+ getTakimBarkodOnek()+
                "\n barkodUzunluk: "+ getBarkodUzunluk()+
                "\n takimEtiketID: "+ getTakimEtiketID()+
                "\n takimEtiketPrinter: "+ getTakimEtiketPrinter()+
                "\n paletEtiketID: "+ getPaletEtiketID()+
                "\n paletDetayliEtiketID: "+ getPaletDetayliEtiketID()+
                "\n paletEtiketPrinter: "+ getPaletEtiketPrinter()+
                "\n yeniUretimBarkodOnEk: "+ getYeniUretimBarkodOnEk()+
                "\n cerabathEtiketID: "+ getCerabathEtiketID()+
                "\n locationNo: "+ getLocationNo()+
                "\n contract: "+ getContract();
    }

    public int getKullaniciRef() {
        return kullaniciRef;
    }

    public void setKullaniciRef(int kullaniciRef) {
        this.kullaniciRef = kullaniciRef;
    }

    public int getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(int depoNo) {
        this.depoNo = depoNo;
    }

    public int getSevkDepoNo() {
        return sevkDepoNo;
    }

    public void setSevkDepoNo(int sevkDepoNo) {
        this.sevkDepoNo = sevkDepoNo;
    }

    public int getBolumNo() {
        return bolumNo;
    }

    public void setBolumNo(int bolumNo) {
        this.bolumNo = bolumNo;
    }

    public String getBarkodOnEk() {
        return barkodOnEk;
    }

    public void setBarkodOnEk(String barkodOnEk) {
        this.barkodOnEk = barkodOnEk;
    }

    public String getTakimBarkodOnek() {
        return takimBarkodOnek;
    }

    public void setTakimBarkodOnek(String takimBarkodOnek) {
        this.takimBarkodOnek = takimBarkodOnek;
    }

    public int getBarkodUzunluk() {
        return barkodUzunluk;
    }

    public void setBarkodUzunluk(int barkodUzunluk) {
        this.barkodUzunluk = barkodUzunluk;
    }

    public int getTakimEtiketID() {
        return takimEtiketID;
    }

    public void setTakimEtiketID(int takimEtiketID) {
        this.takimEtiketID = takimEtiketID;
    }

    public String getTakimEtiketPrinter() {
        return takimEtiketPrinter;
    }

    public void setTakimEtiketPrinter(String takimEtiketPrinter) {
        this.takimEtiketPrinter = takimEtiketPrinter;
    }

    public int getPaletEtiketID() {
        return paletEtiketID;
    }

    public void setPaletEtiketID(int paletEtiketID) {
        this.paletEtiketID = paletEtiketID;
    }

    public int getPaletDetayliEtiketID() {
        return paletDetayliEtiketID;
    }

    public void setPaletDetayliEtiketID(int paletDetayliEtiketID) {
        this.paletDetayliEtiketID = paletDetayliEtiketID;
    }

    public String getPaletEtiketPrinter() {
        return paletEtiketPrinter;
    }

    public void setPaletEtiketPrinter(String paletEtiketPrinter) {
        this.paletEtiketPrinter = paletEtiketPrinter;
    }

    public String getYeniUretimBarkodOnEk() {
        return yeniUretimBarkodOnEk;
    }

    public void setYeniUretimBarkodOnEk(String yeniUretimBarkodOnEk) {
        this.yeniUretimBarkodOnEk = yeniUretimBarkodOnEk;
    }

    public int getCerabathEtiketID() {
        return cerabathEtiketID;
    }

    public void setCerabathEtiketID(int cerabathEtiketID) {
        this.cerabathEtiketID = cerabathEtiketID;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}
