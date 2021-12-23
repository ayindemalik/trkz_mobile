package com.example.trkz_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.trkz_mobile.databinding.YeniPaletOlusturmaDevamBinding;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class YeniPaletOlusturmaDevam extends AppCompatActivity {
    public YeniPaletOlusturmaDevamBinding  binding;
    ListView paletItemsList;
    PaletAdapter paletAdapter;
    EditText bKodInput;

    int itemCount = 0;
    private Timer timer;
    private  TextWatcher text = null;
    String inputText;

    long delay = 2000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    Intent intent;
    String lrf, adet, etiketTipi, urunKodu;
    String paletRef;

    TextView showData;

    PaletModel paletBilgileri;
    ArrayList<PaletIcerikModel> paletIcerikList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = YeniPaletOlusturmaDevamBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);
        intent = getIntent();
        paletItemsList = binding.paletItemsListView;
        bKodInput = binding.input;

//        paletAdapter = new PaletAdapter(getBaseContext(), R.layout.palet_items_list);
        paletAdapter = new PaletAdapter(getBaseContext(), R.layout.palet_icerik_detay);
        paletItemsList.setAdapter(paletAdapter);
//        for (int i=1; i < 11; i++ ){
//            PaletItemsModel paletItemsModel = new PaletItemsModel(i+"_UBKOD",i+"_USTKOD", i+"_IFSTKOD", i+"_IFS_ST_TANI" );
//            Log.d("P Item MODEL DATA::", paletItemsModel.toString());
//            PaletAdapter.OPERATION = "PALET_ITEM";
//            paletAdapter.addPaletitem(paletItemsModel);
//            paletAdapter.notifyDataSetChanged();
//        }

        showData = binding.mmmmm;
        paletRef = intent.getStringExtra("lrf");
        lrf =  intent.getStringExtra("lrf");
        adet = intent.getStringExtra("adet") ;
        etiketTipi =  intent.getStringExtra("paletTipi");
        urunKodu = intent.getStringExtra("urunKodu");
        String data = "lrf : "+lrf
                +"\n"+ "adet : "+adet
                +"\n"+ "paletTipi : "+etiketTipi
                +"\n"+ "urunKodu : "+urunKodu;
        showData.setText(data);

        bKodInput.addTextChangedListener(new TextWatcher() {
            String inputText;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                itemCount++;
                handler.removeCallbacks(input_finish_checker);
            }
            @Override
            public void afterTextChanged(Editable e) {
                //avoid triggering event when text is empty
                if (e.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }
        });
        // NULL THE INPUT

        // PALET BILGILERI GETIR
        loadPaletBilgileri(paletRef);

        // PALET ICERIKLERI
        loadPaletIcerikleri(paletRef);

    }
    // FOR TIMING
    //  Solution link : https://www.py4u.net/discuss/625126
    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                inputText = bKodInput.getText().toString();
                operation(inputText);
                bKodInput.setText("");
            }
        }
    };
    public void operation(String inputText){
        if(inputText != ""){
            PaletItemsModel paletItemsModel = new PaletItemsModel(""+inputText,itemCount+"_USTKOD", itemCount+"_IFSTKOD", itemCount+"_IFS_ST_TANI" );
            Log.d("P Item MODEL DATA::", paletItemsModel.toString());
            PaletAdapter.OPERATION = "PALET_ITEM";
            paletAdapter.addPaletitem(paletItemsModel);
            paletAdapter.notifyDataSetChanged();
//            bKodInput.setText("");
        }
    }

    // Palet Bilgileri Getir
    public void loadPaletBilgileri(String paletRef_){
        BackgroudTask task = new BackgroudTask(this);
        Log.d("loadPaletBilgileri:->", paletRef_);
        task.execute("loadPaletBilgeri", paletRef_);
    }
    public void loadPaletIcerikleri(String paletRef_){
        BackgroudTask task = new BackgroudTask(this);
        Log.d("loadPaletBilgileri:->", paletRef_);
        task.execute("loadPaletIcerikkeri", paletRef_);
    }


    class BackgroudTask extends AsyncTask<String, Void, String> {
        String result;
        Context ctex;
        public String paletResult;
        String ACTION = "";

        BackgroudTask(Context ctex) {
            this.ctex = ctex;
        }

        @Override
        protected String doInBackground(String... params) {
            ACTION = params[0];
            String musteriCari, urunBarKodu;
            MobileService service = new MobileService();
            Log.d("ACTION:", ACTION);
            if (ACTION.equals("loadPaletBilgeri")) {
                result = service.paletBilgileri(params[1]);
            }
            else if (ACTION.equals("loadPaletIcerikkeri")) {
                result = service.paletIcerikDetay(params[1]);
            }


            return null;
        }
        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String result2) {
            if (ACTION.equals("loadPaletBilgeri")) {
                Log.d("RESULT -> :", result);
                int element = paletBilgiXMLParse(result);
                Log.d("TOTAL EL:->", String.valueOf(element));
                Log.d("Palet DAta:->", paletBilgileri.getIfsHandlingUnitId()
                        + paletBilgileri.getLref() + paletBilgileri.getBarkod() );
            }
            if (ACTION.equals("loadPaletIcerikkeri")) {
                Log.d("RESULT_loadPaletIcerikkeri:", result);
                paletIcerikList = paletIcerikXMLParse(result);
            }

        }

        // PALETLER BILGILERI
        public int paletBilgiXMLParse(String result_){
            int elementCount = 0;
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean IfsHandlingUnitIdBool = false, LRefBool = false, BarkodBool = false, cari_kodBool = false,
                        cari_unvan1Bool = false, AktifMiBool=false, KapaliMiBool = false,  EtiketiBool = false,
                        uru_stok_kodBool = false, IfsKoduBool=false, IfsHandlingUnitTypeBool = false, PaletAdetBool = false,
                        TakimAdetBool = false, IcerikDurumuBool=false, BoyMuBool = false,  TakimDetayliMiBool = false,
                        HollandaDepoMuBool = false, KullanicilarRefBool=false, TarihBool = false,  KapanmaTarihiBool = false;

                String IfsHandlingUnitId = "",  LRef = "", Barkod = "", cari_kod = "", cari_unvan1 = "";
                Boolean AktifMi = false, KapaliMi = false;

                String Etiketi = "", uru_stok_kod = "", IfsKodu = "", IfsHandlingUnitType = ""; //X

                int PaletAdet = 0, TakimAdet = 0; //0
                String IcerikDurumu = ""; // K

                Boolean BoyMu = false, TakimDetayliMi = false, HollandaDepoMu = false; // false
                String KullanicilarRef = "", Tarih = "", KapanmaTarihi = ""; // 2021-03-20T02:22:32.43+03:00

                int count = 0;
                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("IfsHandlingUnitId")) IfsHandlingUnitIdBool = true;
                        if(xmlPullParser.getName().equals("LRef")) LRefBool = true;
                        if(xmlPullParser.getName().equals("Barkod")) BarkodBool = true;
                        if(xmlPullParser.getName().equals("cari_kod")) cari_kodBool = true;
                        if(xmlPullParser.getName().equals("cari_unvan1")) cari_unvan1Bool = true;
                        if(xmlPullParser.getName().equals("AktifMi")) AktifMiBool = true;
                        if(xmlPullParser.getName().equals("KapaliMi")) KapaliMiBool = true;
                        if(xmlPullParser.getName().equals("Etiketi")) EtiketiBool = true;
                        if(xmlPullParser.getName().equals("uru_stok_kod")) uru_stok_kodBool = true;
                        if(xmlPullParser.getName().equals("IfsKodu")) IfsKoduBool = true;
                        if(xmlPullParser.getName().equals("IfsHandlingUnitType")) IfsHandlingUnitTypeBool = true;
                        if(xmlPullParser.getName().equals("PaletAdet")) PaletAdetBool = true;
                        if(xmlPullParser.getName().equals("TakimAdet")) TakimAdetBool = true;
                        if(xmlPullParser.getName().equals("IcerikDurumu")) IcerikDurumuBool = true;
                        if(xmlPullParser.getName().equals("BoyMu")) BoyMuBool = true;
                        if(xmlPullParser.getName().equals("TakimDetayliMi")) TakimDetayliMiBool = true;
                        if(xmlPullParser.getName().equals("HollandaDepoMu")) HollandaDepoMuBool = true;
                        if(xmlPullParser.getName().equals("KullanicilarRef")) KullanicilarRefBool = true;
                        if(xmlPullParser.getName().equals("Tarih")) TarihBool = true;
                        if(xmlPullParser.getName().equals("KapanmaTarihi")) KapanmaTarihiBool = true;
                    }

                    if(event == xmlPullParser.TEXT){ // If
                        if(IfsHandlingUnitIdBool)
                        { IfsHandlingUnitId = xmlPullParser.getText(); IfsHandlingUnitIdBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(LRefBool)
                        { LRef = xmlPullParser.getText(); LRefBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(BarkodBool)
                        { Barkod = xmlPullParser.getText(); BarkodBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(cari_kodBool)
                        { cari_kod = xmlPullParser.getText(); cari_kodBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(cari_unvan1Bool)
                        { cari_unvan1 = xmlPullParser.getText(); cari_unvan1Bool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(AktifMiBool)
                        { AktifMi = Boolean.valueOf(xmlPullParser.getText()); AktifMiBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(KapaliMiBool)
                        { KapaliMi = Boolean.valueOf(xmlPullParser.getText()); KapaliMiBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(EtiketiBool)
                        { Etiketi = xmlPullParser.getText(); EtiketiBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(uru_stok_kodBool)
                        { uru_stok_kod = xmlPullParser.getText(); uru_stok_kodBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(IfsKoduBool)
                        { IfsKodu = xmlPullParser.getText(); IfsKoduBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(IfsHandlingUnitTypeBool)
                        { IfsHandlingUnitType = xmlPullParser.getText(); IfsHandlingUnitTypeBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(PaletAdetBool)
                        { PaletAdet = Integer.valueOf(xmlPullParser.getText()); PaletAdetBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(TakimAdetBool)
                        { TakimAdet = Integer.valueOf(xmlPullParser.getText()); TakimAdetBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(IcerikDurumuBool)
                        { IcerikDurumu = xmlPullParser.getText(); IcerikDurumuBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(BoyMuBool)
                        { BoyMu = Boolean.valueOf(xmlPullParser.getText()); BoyMuBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(TakimDetayliMiBool)
                        { TakimDetayliMi = Boolean.valueOf(xmlPullParser.getText()); TakimDetayliMiBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(HollandaDepoMuBool)
                        { HollandaDepoMuBool = Boolean.valueOf(xmlPullParser.getText()); HollandaDepoMuBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(BoyMuBool)
                        { BoyMu = Boolean.valueOf(xmlPullParser.getText()); BoyMuBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(KullanicilarRefBool)
                        { KullanicilarRef = xmlPullParser.getText(); KullanicilarRefBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(TarihBool)
                        { Tarih = xmlPullParser.getText(); TarihBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(KapanmaTarihiBool)
                        { KapanmaTarihi = xmlPullParser.getText(); KapanmaTarihiBool = false; }
                    }


                    if (!IfsHandlingUnitId.equals("") && !LRef.equals("") && !Barkod.equals("") && !cari_kod.equals("")
                            && !cari_unvan1.equals("") && !uru_stok_kod.equals("") && !IfsKodu.equals("")){
                        elementCount = elementCount + 1;
                        paletBilgileri = new PaletModel(IfsHandlingUnitId, LRef, Barkod, cari_kod, cari_unvan1, AktifMi, KapaliMi,
                                 Etiketi, uru_stok_kod, IfsKodu, IfsHandlingUnitType, PaletAdet, TakimAdet, IcerikDurumu, BoyMu,
                                TakimDetayliMi, HollandaDepoMu, KullanicilarRef, Tarih, KapanmaTarihi );
//                        customerArrayList.add(customerModel);
                    }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("IfsHandlingUnitId"))  IfsHandlingUnitIdBool = false;
                        if(xmlPullParser.getName().equals("LRef"))  LRefBool = false;
                        if(xmlPullParser.getName().equals("Barkod"))  BarkodBool = false;
                        if(xmlPullParser.getName().equals("cari_kod"))  cari_kodBool = false;
                        if(xmlPullParser.getName().equals("cari_unvan1"))  cari_unvan1Bool = false;
                        if(xmlPullParser.getName().equals("AktifMi"))  AktifMiBool = false;
                        if(xmlPullParser.getName().equals("KapaliMi"))  KapaliMiBool = false;
                        if(xmlPullParser.getName().equals("Etiketi"))  EtiketiBool = false;
                        if(xmlPullParser.getName().equals("uru_stok_kod"))  uru_stok_kodBool = false;
                        if(xmlPullParser.getName().equals("IfsKodu"))  IfsKoduBool = false;
                        if(xmlPullParser.getName().equals("IfsHandlingUnitType"))  IfsHandlingUnitTypeBool = false;
                        if(xmlPullParser.getName().equals("PaletAdet"))  PaletAdetBool = false;
                        if(xmlPullParser.getName().equals("TakimAdet"))  TakimAdetBool = false;
                        if(xmlPullParser.getName().equals("IcerikDurumu"))  IcerikDurumuBool = false;
                        if(xmlPullParser.getName().equals("BoyMu"))  BoyMuBool = false;
                        if(xmlPullParser.getName().equals("TakimDetayliMi"))  TakimDetayliMiBool = false;
                        if(xmlPullParser.getName().equals("HollandaDepoMu"))  HollandaDepoMuBool = false;
                        if(xmlPullParser.getName().equals("KullanicilarRef"))  KullanicilarRefBool = false;
                        if(xmlPullParser.getName().equals("Tarih"))  TarihBool = false;
                        if(xmlPullParser.getName().equals("KapanmaTarihi"))  KapanmaTarihiBool = false;
                    }
                    event = xmlPullParser.next();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return elementCount;
        }

        // PALETLER TIPI
        public ArrayList<PaletIcerikModel> paletIcerikXMLParse(String result_){
            ArrayList<PaletIcerikModel> paletIcerikList = new ArrayList<>();
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean lRefBool = false, paletlerRefBool = false , uretimBarkoduBool = false, urunTipiBool = false,
                        ifs_Stok_KoduBool = false,  ifs_Stok_TanimiBool = false, adetBool = false, transferredToIfsBool = false,
                        uru_Stok_SodBool = false, urunAdiBool = false, tarihBool = false, kullanicilarRefBool = false;

                String lRef = "", paletlerRef = "", uretimBarkodu = "";
                int urunTipi = 0;
                String ifs_Stok_Kodu = "", ifs_Stok_Tanimi = "";
                int adet = 0;
                Boolean transferredToIfs = false;
                String uru_Stok_Sod = "", urunAdi = "", tarih = "",  kullanicilarRef =  "";

                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("LRef")) lRefBool = true;
                        if(xmlPullParser.getName().equals("PaletlerRef")) paletlerRefBool = true;
                        if(xmlPullParser.getName().equals("UretimBarkodu")) uretimBarkoduBool = true;
                        if(xmlPullParser.getName().equals("UrunTipi")) urunTipiBool = true;
                        if(xmlPullParser.getName().equals("IFS_stok_kodu")) ifs_Stok_KoduBool = true;
                        if(xmlPullParser.getName().equals("IFS_stok_tanimi")) ifs_Stok_TanimiBool = true;
                        if(xmlPullParser.getName().equals("Adet")) adetBool = true;
                        if(xmlPullParser.getName().equals("TransferredToIfs")) transferredToIfsBool = true;
                        if(xmlPullParser.getName().equals("uru_stok_kod")) uru_Stok_SodBool = true;
                        if(xmlPullParser.getName().equals("UrunAdi")) urunAdiBool = true;
                        if(xmlPullParser.getName().equals("Tarih")) tarihBool = true;
                        if(xmlPullParser.getName().equals("KullanicilarRef")) kullanicilarRefBool = true;
                    }

                    if(event == xmlPullParser.TEXT)
                        if(lRefBool){ lRef = xmlPullParser.getText(); lRefBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(paletlerRefBool){ paletlerRef = xmlPullParser.getText(); paletlerRefBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(uretimBarkoduBool){ uretimBarkodu = xmlPullParser.getText(); uretimBarkoduBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(urunTipiBool){ urunTipi = Integer.valueOf(xmlPullParser.getText()); urunTipiBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(ifs_Stok_KoduBool){ ifs_Stok_Kodu = xmlPullParser.getText(); ifs_Stok_KoduBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(ifs_Stok_TanimiBool){ ifs_Stok_Tanimi = xmlPullParser.getText(); ifs_Stok_TanimiBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(adetBool){ adet = Integer.valueOf(xmlPullParser.getText()); adetBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(transferredToIfsBool){ transferredToIfs = Boolean.valueOf(xmlPullParser.getText()); transferredToIfsBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(uru_Stok_SodBool){ uru_Stok_Sod = xmlPullParser.getText(); uru_Stok_SodBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(urunAdiBool){ urunAdi = xmlPullParser.getText(); urunAdiBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(tarihBool){ tarih = xmlPullParser.getText(); tarihBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(kullanicilarRefBool){ kullanicilarRef = xmlPullParser.getText(); kullanicilarRefBool = false; }

                    if (!lRef.equals("") && !paletlerRef.equals("") && !uretimBarkodu.equals("") && !ifs_Stok_Kodu.equals("")){
                        PaletIcerikModel icerikModel = new PaletIcerikModel(lRef, paletlerRef, uretimBarkodu, urunTipi,
                                ifs_Stok_Kodu, ifs_Stok_Tanimi,adet, transferredToIfs, uru_Stok_Sod, urunAdi,
                                tarih, kullanicilarRef);
                        paletIcerikList.add(icerikModel);

                        PaletAdapter.OPERATION = "PALET_ICERIK_DETAY";
                        paletAdapter.addPaletIcerikDetay(icerikModel);
                        paletAdapter.notifyDataSetChanged();

                        lRef = ""; paletlerRef = ""; uretimBarkodu = "";
                        urunTipi = 0; ifs_Stok_Kodu = ""; ifs_Stok_Tanimi = ""; adet = 0;
                        transferredToIfs = false; uru_Stok_Sod = ""; urunAdi = ""; tarih = "";  kullanicilarRef =  "";
                    }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("LRef")) lRefBool = false;
                        if(xmlPullParser.getName().equals("PaletlerRef")) paletlerRefBool = false;
                        if(xmlPullParser.getName().equals("UretimBarkodu")) uretimBarkoduBool = false;
                        if(xmlPullParser.getName().equals("UrunTipi")) urunTipiBool = false;
                        if(xmlPullParser.getName().equals("IFS_stok_kodu")) ifs_Stok_KoduBool = false;
                        if(xmlPullParser.getName().equals("IFS_stok_tanimi")) ifs_Stok_TanimiBool = false;
                        if(xmlPullParser.getName().equals("Adet")) adetBool = false;
                        if(xmlPullParser.getName().equals("TransferredToIfs")) transferredToIfsBool = false;
                        if(xmlPullParser.getName().equals("uru_stok_kod")) uru_Stok_SodBool = false;
                        if(xmlPullParser.getName().equals("UrunAdi")) urunAdiBool = false;
                        if(xmlPullParser.getName().equals("Tarih")) tarihBool = false;
                        if(xmlPullParser.getName().equals("KullanicilarRef")) kullanicilarRefBool = false;
                    }
                    event = xmlPullParser.next();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
            return paletIcerikList;
        }

        // Parse Product
        public ArrayList<ProductModel> yeniProdXMLParse(String result_){
            ArrayList<ProductModel> productArrayList = new ArrayList<>();

            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean yeniKodBool = false, yeniTanimBool = false;
                String yeniKod= "", yeniTanim = ""; int count = 0;

                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("YeniKod"))
                            yeniKodBool = true;
                        if(xmlPullParser.getName().equals("YeniTanim"))
                            yeniTanimBool = true;
                    }

                    if(event == xmlPullParser.TEXT){ // If
                        if(yeniKodBool){
                            yeniKod = xmlPullParser.getText();
                            yeniKodBool = false;
                        }
                    }

                    if(event == xmlPullParser.TEXT){ //
                        if(yeniTanimBool){
                            yeniTanim = xmlPullParser.getText();
                            yeniTanimBool=false;
                        }
                    }

                    if (!yeniKod.equals("") && !yeniTanim.equals("")){
                        count++;
                        ProductModel productModel = new ProductModel(yeniKod, yeniTanim);
                        Log.d("Prod MODEL DATA::", productModel.toString());
                        productArrayList.add(productModel);
                        yeniKod = ""; yeniTanim ="";
                    }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("YeniKod"))
                            yeniKodBool =false;
                        if(xmlPullParser.getName().equals("YeniTanim"))
                            yeniTanimBool =false;
                    }
                    event = xmlPullParser.next();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
            return productArrayList;
        }



        // PALET OLUSTUR RESULT
        public String parsePaletXmlResult(String result_){
            String paletLrf= "";
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean paletLrfBool = false;

                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("PaletOlusturResult"))
                            paletLrfBool = true;
                    }

                    if(event == xmlPullParser.TEXT){ //
                        if(paletLrfBool){
                            paletLrf = xmlPullParser.getText();
                            paletLrfBool = false;
                        }
                    }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("PaletOlusturResult")) paletLrfBool =false;
                    }
                    event = xmlPullParser.next();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
            return paletLrf;
        }
    }

}