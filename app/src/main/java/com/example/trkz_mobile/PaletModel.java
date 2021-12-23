package com.example.trkz_mobile;

public class PaletModel {
    private String ifsID;
    private String tarih;
    private String kapalimi;
    private String barkod;
    private String cariUnvan;
    private String lref;

    public PaletModel(String ifsID, String tarih, String kapalimi, String barkod, String cariUnvan, String lref) {
        this.ifsID = ifsID;
        this.tarih = tarih;
        this.kapalimi = kapalimi;
        this.barkod = barkod;
        this.cariUnvan = cariUnvan;
        this.lref = lref;
    }

    public String getIfsID() {
        return ifsID;
    }

    public void setIfsID(String ifsID) {
        this.ifsID = ifsID;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getKapalimi() {
        return kapalimi;
    }

    public void setKapalimi(String kapalimi) {
        this.kapalimi = kapalimi;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getCariUnvan() {
        return cariUnvan;
    }

    public void setCariUnvan(String cariUnvan) {
        this.cariUnvan = cariUnvan;
    }

    public String getLref() {
        return lref;
    }

    public void setLref(String lref) {
        this.lref = lref;
    }

    @Override
    public String toString(){
        return " ifsID: "+ getIfsID()+
                "\n tarih: "+ getTarih()+ "  kapalimi :"+getKapalimi()+
                "\n barkod: "+ getBarkod()+
                "\n cariUnvan: "+ getCariUnvan()+
                "\n lref: "+ getLref();
    }

    // PALET BILGILERI
    private String IfsHandlingUnitId;
    private String pLRef;
    private String pBarkod;
    private String cari_kod;
    private String cari_unvan1;
    private Boolean AktifMi; //  true
    private Boolean KapaliMi; // true
    private String Etiketi; //Karışık
    private String uru_stok_kod; // 9SC1301001
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

    public PaletModel(String ifsHandlingUnitId, String LRef, String barkod, String cari_kod, String cari_unvan1, Boolean aktifMi, Boolean kapaliMi, String etiketi, String uru_stok_kod, String ifsKodu, String ifsHandlingUnitType, int paletAdet, int takimAdet, String icerikDurumu, Boolean boyMu, Boolean takimDetayliMi, Boolean hollandaDepoMu, String kullanicilarRef, String tarih, String kapanmaTarihi) {
        IfsHandlingUnitId = ifsHandlingUnitId;
        this.lref = LRef;
        this.barkod = barkod;
        this.cari_kod = cari_kod;
        this.cari_unvan1 = cari_unvan1;
        AktifMi = aktifMi;
        KapaliMi = kapaliMi;
        Etiketi = etiketi;
        this.uru_stok_kod = uru_stok_kod;
        IfsKodu = ifsKodu;
        IfsHandlingUnitType = ifsHandlingUnitType;
        PaletAdet = paletAdet;
        TakimAdet = takimAdet;
        IcerikDurumu = icerikDurumu;
        BoyMu = boyMu;
        TakimDetayliMi = takimDetayliMi;
        HollandaDepoMu = hollandaDepoMu;
        KullanicilarRef = kullanicilarRef;
        Tarih = tarih;
        KapanmaTarihi = kapanmaTarihi;
    }

    public String getIfsHandlingUnitId() {
        return IfsHandlingUnitId;
    }

    public void setIfsHandlingUnitId(String ifsHandlingUnitId) {
        IfsHandlingUnitId = ifsHandlingUnitId;
    }

    public String getPLRef() {
        return pLRef;
    }

    public void setPLRef(String LRef) {
        this.pLRef = LRef;
    }

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
