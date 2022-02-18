package com.example.trkz_mobile;

public class PaletModel {
    //    PALET LISTESI ICIN
    private int ifsID;
    //    private String tarih;
    private String skapalimi;
    private String barkod;
    //    private String cariUnvan;
    private int lref;

    // PALET BILGILERI
    private int IfsHandlingUnitId;
    private String cari_kod;
    private String cari_unvan1;
    private Boolean AktifMi; //  true
    private Boolean KapaliMi; // true
    private String Etiketi; //Karışık
    private String uru_stok_kod; // 9SC1301001
    private String UrunAdi; // 9SC1301001
    private String IfsKodu; //SC00601S50002931
    private String IfsHandlingUnitType; //X
    private int PaletAdet ; // 0
    private int TakimAdet; //0
    private String IcerikDurumu ; // K
    private Boolean BoyMu; // true
    private Boolean TakimDetayliMi; // false
    private Boolean HollandaDepoMu; // false
    private String KullanicilarRef; //266
    private String Tarih; // 2021-03-20T02:16:48.443+03:00
    private String KapanmaTarihi; // 2021-03-20T02:22:32.43+03:00

    public PaletModel(int ifsID, String tarih, String kapalimi, String barkod, String cariUnvan, int lref) {
        this.ifsID = ifsID;
        this.Tarih = tarih;
        this.skapalimi = kapalimi;
        this.barkod = barkod;
        this.cari_unvan1 = cariUnvan;
        this.lref = lref;
    }

    public int getIfsID() {
        return ifsID;
    }

    public void setIfsID(int ifsID) {
        this.ifsID = ifsID;
    }

    public String getTarih() {
        return Tarih;
    }

    public void setTarih(String tarih) {
        this.Tarih = tarih;
    }

    public String getSKapalimi() {
        return skapalimi;
    }

    public void setSKapalimi(String kapalimi) {
        this.skapalimi = kapalimi;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

//    public String getCariUnvan() {
//        return cariUnvan;
//    }
//
//    public void setCariUnvan(String cariUnvan) {
//        this.cariUnvan = cariUnvan;
//    }

    public int getLref() {
        return lref;
    }

    public void setLref(int lref) {
        this.lref = lref;
    }

    @Override
    public String toString(){
        return " ifsID: "+ getIfsID()+
                "\n tarih: "+ getTarih()+
                "\n barkod: "+ getBarkod()+
                "\n cariUnvan1: "+ getCari_unvan1()+
                "\n lref: "+ getLref()+
                "\n AktifMi: "+ getAktifMi()+
                "\n sKapalimi: "+ String.valueOf(getSKapalimi())+
                "\n Kapalimi: "+ getKapaliMi()+
                "\n Etiketi: "+ getEtiketi()+
                "\n uru_stok_kod: "+ getUru_stok_kod()+
                "\n urun adi: "+ getUrunAdi()+
                "\n IfsKodu: "+ getIfsKodu()+
                "\n IfsHandlingUnitType: "+ getIfsHandlingUnitType()+
                "\n IfsHandlingUnitId: "+ getIfsHandlingUnitId()+
                "\n PaletAdet: "+ getPaletAdet()+
                "\n TakimAdet: "+ getTakimAdet()+
                "\n IcerikDurumu: "+ getIcerikDurumu()+
                "\n TakimDetayliMi: "+ getTakimDetayliMi()+
                "\n KullanicilarRef: "+ getKullanicilarRef()+
                "\n KapanmaTarihi: "+ getKapanmaTarihi();
    }

    public PaletModel(int ifsHandlingUnitId, int LRef, String barkod, String cari_kod,
                      String cari_unvan1, Boolean aktifMi, Boolean kapaliMi,
                      String etiketi, String uru_stok_kod, String urunAdi, String ifsKodu,
                      String ifsHandlingUnitType, int paletAdet, int takimAdet, String icerikDurumu,
                      Boolean boyMu, Boolean takimDetayliMi, Boolean hollandaDepoMu,
                      String kullanicilarRef, String tarih, String kapanmaTarihi) {
        this.IfsHandlingUnitId = ifsHandlingUnitId;
        this.lref = LRef;
        this.barkod = barkod;
        this.cari_kod = cari_kod;
        this.cari_unvan1 = cari_unvan1;
        this.AktifMi = aktifMi;
        this.KapaliMi = kapaliMi;
        this.Etiketi = etiketi;
        this.uru_stok_kod = uru_stok_kod;
        this.UrunAdi = UrunAdi;
        this.IfsKodu = ifsKodu;
        this.IfsHandlingUnitType = ifsHandlingUnitType;
        this.PaletAdet = paletAdet;
        this.TakimAdet = takimAdet;
        this.IcerikDurumu = icerikDurumu;
        this.BoyMu = boyMu;
        this.TakimDetayliMi = takimDetayliMi;
        this.HollandaDepoMu = hollandaDepoMu;
        this.KullanicilarRef = kullanicilarRef;
        this.Tarih = tarih;
        this.KapanmaTarihi = kapanmaTarihi;
    }

    public int getIfsHandlingUnitId() {
        return IfsHandlingUnitId;
    }

    public void setIfsHandlingUnitId(int ifsHandlingUnitId) {
        IfsHandlingUnitId = ifsHandlingUnitId;
    }

//    public String getPLRef() {
//        return pLRef;
//    }
//
//    public void setPLRef(String LRef) {
//        this.pLRef = LRef;
//    }

    public String getCari_kod() {
        return cari_kod;
    }

    public void setCari_kod(String cari_kod) {
        this.cari_kod = cari_kod;
    }

    public String getCari_unvan1() {
        return cari_unvan1;
    }

    public void setCari_unvan1(String cari_unvan1) {
        this.cari_unvan1 = cari_unvan1;
    }

    public Boolean getAktifMi() {
        return AktifMi;
    }

    public void setAktifMi(Boolean aktifMi) {
        AktifMi = aktifMi;
    }

    public Boolean getKapaliMi() {
        return KapaliMi;
    }

    public void setKapaliMi(Boolean kapaliMi) {
        KapaliMi = kapaliMi;
    }

    public String getEtiketi() {
        return Etiketi;
    }

    public void setEtiketi(String etiketi) {
        Etiketi = etiketi;
    }

    public String getUru_stok_kod() {
        return uru_stok_kod;
    }

    public void setUru_stok_kod(String uru_stok_kod) {
        this.uru_stok_kod = uru_stok_kod;
    }

    public String getUrunAdi() {
        return UrunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        UrunAdi = urunAdi;
    }

    public String getIfsKodu() {
        return IfsKodu;
    }

    public void setIfsKodu(String ifsKodu) {
        IfsKodu = ifsKodu;
    }

    public String getIfsHandlingUnitType() {
        return IfsHandlingUnitType;
    }

    public void setIfsHandlingUnitType(String ifsHandlingUnitType) {
        IfsHandlingUnitType = ifsHandlingUnitType;
    }

    public int getPaletAdet() {
        return PaletAdet;
    }

    public void setPaletAdet(int paletAdet) {
        PaletAdet = paletAdet;
    }

    public int getTakimAdet() {
        return TakimAdet;
    }

    public void setTakimAdet(int takimAdet) {
        TakimAdet = takimAdet;
    }

    public String getIcerikDurumu() {
        return IcerikDurumu;
    }

    public void setIcerikDurumu(String icerikDurumu) {
        IcerikDurumu = icerikDurumu;
    }

    public Boolean getBoyMu() {
        return BoyMu;
    }

    public void setBoyMu(Boolean boyMu) {
        BoyMu = boyMu;
    }

    public Boolean getTakimDetayliMi() {
        return TakimDetayliMi;
    }

    public void setTakimDetayliMi(Boolean takimDetayliMi) {
        TakimDetayliMi = takimDetayliMi;
    }

    public Boolean getHollandaDepoMu() {
        return HollandaDepoMu;
    }

    public void setHollandaDepoMu(Boolean hollandaDepoMu) {
        HollandaDepoMu = hollandaDepoMu;
    }

    public String getKullanicilarRef() {
        return KullanicilarRef;
    }

    public void setKullanicilarRef(String kullanicilarRef) {
        KullanicilarRef = kullanicilarRef;
    }

    public String getKapanmaTarihi() {
        return KapanmaTarihi;
    }

    public void setKapanmaTarihi(String kapanmaTarihi) {
        KapanmaTarihi = kapanmaTarihi;
    }

}
