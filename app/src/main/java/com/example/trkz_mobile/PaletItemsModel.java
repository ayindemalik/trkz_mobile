package com.example.trkz_mobile;

public class PaletItemsModel {

    private String urunBarkod;
    private String urunStokKodu;
    private String ifsStokKodu;
    private String ifsStokTani;

    public PaletItemsModel(String urunBarkod, String urunStokKodu, String ifsStokKodu, String ifsStokTani) {
        this.urunBarkod = urunBarkod;
        this.urunStokKodu = urunStokKodu;
        this.ifsStokKodu = ifsStokKodu;
        this.ifsStokTani = ifsStokTani;
    }

    public String getUrunBarkod() {
        return urunBarkod;
    }

    public void setUrunBarkod(String urunBarkod) {
        this.urunBarkod = urunBarkod;
    }

    public String getUrunStokKodu() {
        return urunStokKodu;
    }

    public void setUrunStokKodu(String urunStokKodu) {
        this.urunStokKodu = urunStokKodu;
    }

    public String getIfsStokKodu() {
        return ifsStokKodu;
    }

    public void setIfsStokKodu(String ifsStokKodu) {
        this.ifsStokKodu = ifsStokKodu;
    }

    public String getIfsStokTani() {
        return ifsStokTani;
    }

    public void setIfsStokTani(String ifsStokTani) {
        this.ifsStokTani = ifsStokTani;
    }


    @Override
    public String toString(){
        return " urunBarkod: "+ getUrunBarkod()+
                "\n urunStokKodu: "+ getUrunStokKodu()+
                "\n  ifsStokKodu :"+getIfsStokKodu()+
                "\n ifsStokTani: "+ getIfsStokTani();
    }

}
