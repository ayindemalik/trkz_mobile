package com.example.trkz_mobile;

public class PaletIcerikModel {
    private String lRef;
    private String paletlerRef ; //207568</PaletlerRef>
    private String uretimBarkodu; //>0189528968</UretimBarkodu>
    private int urunTipi; // >0</UrunTipi>
    private String ifs_Stok_Kodu ; // CS0060101003Y02123</IFS_stok_kodu>
    private String ifs_Stok_Tanimi ; // >SERAMİK REZERVUAR, MONA, ÜSTTEN BASMALI, SOL ALTTAN SU GİRİŞLİ, BEYAZ, TURKUAZ, ZAHRAT</IFS_stok_tanimi>
    private int adet ; //>1</Adet>
    private Boolean transferredToIfs; //>true</TransferredToIfs>
    private String uru_Stok_Kodu ; //>009000</uru_stok_kod>
    private String urunAdi ; //Rezervuar, Mona</UrunAdi>
    private String tarih; //>2021-03-20T02:19:26.383+03:00</Tarih>
    private String kullanicilarRef ; //>266</KullanicilarRef>

    public PaletIcerikModel(String lRef, String paletlerRef, String uretimBarkodu, int urunTipi, String ifs_Stok_Kodu, String ifs_Stok_Tanimi, int adet, Boolean transferredToIfs, String uru_Stok_Kodu, String urunAdi, String tarih, String kullanicilarRef) {
        this.lRef = lRef;
        this.paletlerRef = paletlerRef;
        this.uretimBarkodu = uretimBarkodu;
        this.urunTipi = urunTipi;
        this.ifs_Stok_Kodu = ifs_Stok_Kodu;
        this.ifs_Stok_Tanimi = ifs_Stok_Tanimi;
        this.adet = adet;
        this.transferredToIfs = transferredToIfs;
        this.uru_Stok_Kodu = uru_Stok_Kodu;
        this.urunAdi = urunAdi;
        this.tarih = tarih;
        this.kullanicilarRef = kullanicilarRef;
    }

    public PaletIcerikModel( String uru_Stok_Kodu, String ifs_Stok_Tanimi, String ifs_Stok_Kodu,  int adet) {
        this.uru_Stok_Kodu = uru_Stok_Kodu;
        this.ifs_Stok_Tanimi = ifs_Stok_Tanimi;
        this.ifs_Stok_Kodu = ifs_Stok_Kodu;
        this.adet = adet;
    }

    public String getlRef() {
        return lRef;
    }

    public void setlRef(String lRef) {
        this.lRef = lRef;
    }

    public String getPaletlerRef() {
        return paletlerRef;
    }

    public void setPaletlerRef(String paletlerRef) {
        this.paletlerRef = paletlerRef;
    }

    public String getUretimBarkodu() {
        return uretimBarkodu;
    }

    public void setUretimBarkodu(String uretimBarkodu) {
        this.uretimBarkodu = uretimBarkodu;
    }

    public int getUrunTipi() {
        return urunTipi;
    }

    public void setUrunTipi(int urunTipi) {
        this.urunTipi = urunTipi;
    }

    public String getIfs_Stok_Kodu() {
        return ifs_Stok_Kodu;
    }

    public void setIfs_Stok_Kodu(String ifs_Stok_Kodu) {
        this.ifs_Stok_Kodu = ifs_Stok_Kodu;
    }

    public String getIfs_Stok_Tanimi() {
        return ifs_Stok_Tanimi;
    }

    public void setIfs_Stok_Tanimi(String ifs_Stok_Tanimi) {
        this.ifs_Stok_Tanimi = ifs_Stok_Tanimi;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public Boolean getTransferredToIfs() {
        return transferredToIfs;
    }

    public void setTransferredToIfs(Boolean transferredToIfs) {
        this.transferredToIfs = transferredToIfs;
    }

    public String getUru_Stok_Kodu() {
        return uru_Stok_Kodu;
    }

    public void setUru_Stok_Kodu(String uru_Stok_Kodu) {
        this.uru_Stok_Kodu = uru_Stok_Kodu;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getKullanicilarRef() {
        return kullanicilarRef;
    }

    public void setKullanicilarRef(String kullanicilarRef) {
        this.kullanicilarRef = kullanicilarRef;
    }
}
