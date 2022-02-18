package com.example.trkz_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.trkz_mobile.databinding.YeniPaletOlusturmaDevamBinding;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

public class YeniPaletOlusturmaDevam extends AppCompatActivity {
    public YeniPaletOlusturmaDevamBinding binding;
    ListView paletIcerikDetayListView, paletIcerikOzetListView;
    PaletIcerikDetayAdapter paletIcerikDetayAdapter;
    PaletIcerikOzetiAdapter palletIcerikOzetAdapter;
    EditText bKodInput;
    SharedPreferences pref;
    UserModel user;

    int itemCount = 0;
    private Timer timer;
    private  TextWatcher text = null;
    String inputText;

    long delay = 2000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    Intent intent;
    // DEVAMDAN GELEN VERILER
    String cariKod, lrf, etiketTipi, urunKodu, urunTani, eskiKod;
    String paletRef, urunEANBarkod, uretimBarkod;
    int paletAdetKontrol = 0, adet = 0;
    PaletModel paletBilgileri;
    ArrayList<PaletIcerikModel> paletIcerikList, paletIcerikOzet;
    ArrayList<ProductModel> urunlistesí;

    AlertDialog dialog, yesnoDialog, paletBilgiDialog;
    AlertDialog.Builder dialogBuilder, yesnoDialogBuilder, paletBilgiDialogBuilder;

    PaletAdapter yeniPaletAdapter;
    ListView yeniKodListview;

    String dateTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = YeniPaletOlusturmaDevamBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);
        intent = getIntent();
        dialogBuilder =  new AlertDialog.Builder(this);

        paletIcerikDetayListView = binding.paletItemsListView;
        paletIcerikOzetListView = binding.paletIcerikOzetListView;
        bKodInput = binding.input ;
//        bKodInput.setShowSoftInputOnFocus(false);

        // paletAdapter = new PaletAdapter(getBaseContext(), R.layout.palet_items_list);
        paletIcerikDetayAdapter = new PaletIcerikDetayAdapter(getBaseContext(), R.layout.palet_icerik_detay);
        paletIcerikDetayListView.setAdapter(paletIcerikDetayAdapter);

        palletIcerikOzetAdapter = new PaletIcerikOzetiAdapter(getBaseContext(), R.layout.palet_icerik_ozet_row);
        paletIcerikOzetListView.setAdapter(palletIcerikOzetAdapter);

        pref = getSharedPreferences("UserPreferences", 0);
        setUserData(pref);

        yesnoDialogBuilder = new AlertDialog.Builder(this);
        paletBilgiDialogBuilder = new AlertDialog.Builder(this);

        // setIntents(intent);
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

        // PALET ICERIKLERI
        // loadPaletIcerikleri(paletRef);

        // PALET ICERIKLERI OZET
        // loadPaletIcerikOzet(paletRef);
    }
    // To activate Menubar oprions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.palet_islem_menu, menu);
        // return super.onCreateOptionsMenu(menu);
        return true;
    }

    // On Menu Bar item options click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.paletBilgileri:
                paletBilgiGoster();
                break;
            case R.id.paletBitir:
                Toast.makeText(this, "Palet bitir islemi", Toast.LENGTH_SHORT).show();
                action = "PALET_BITIR";
                alertDialog("Palet Bitirme", "Bu palet bitirmek istediğinizden emin misiniz ?");

                Log.d("OPTION ACTION", action);
                break;
            case R.id.item2:
                Toast.makeText(this, "Uygulamadan cikis", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void setUserData(SharedPreferences pref){
        user = new UserModel(
                pref.getInt("kullaniciRef", 0),
                pref.getInt("depoNo", 0),
                pref.getInt("sevkDepoNo", 0),
                pref.getInt("bolumNo", 0),
                pref.getString("barkodOnEk", ""),
                pref.getString("takimBarkodOnek", ""),
                pref.getInt("barkodUzunluk", 0),
                pref.getInt("takimEtiketID", 0),
                pref.getString("takimEtiketPrinter", ""),
                pref.getInt("paletEtiketID", 0),
                pref.getInt("paletDetayliEtiketID", 0),
                pref.getString("paletEtiketPrinter", ""),
                pref.getString("yeniUretimBarkodOnEk", ""),
                pref.getInt("cerabathEtiketID", 0),
                pref.getString("locationNo", ""),
                pref.getString("contract", "")
        );
    }

    // FOR TIMING :  Solution link : https://www.py4u.net/discuss/625126
    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                inputText = bKodInput.getText().toString();
                operation(inputText);
            }
        }
    };

    public void setIntents(Intent intent){
        if(intent.getStringExtra("from").equals("PaletOlusturma")){
            paletRef = intent.getStringExtra("lrf");
            // PALET BILGILERI GETIR
            loadPaletBilgileri(paletRef);

            urunKodu = intent.getStringExtra("urunKodu");
            urunTani = intent.getStringExtra("urunTani");
            eskiKod = intent.getStringExtra("eskiKodu");
        }
        else if(intent.getStringExtra("from").equals("PaletListesi")) {
            paletRef = intent.getStringExtra("lrf");
            // PALET BILGILERI GETIR
            loadPaletBilgileri(paletRef);
            urunKodu = paletBilgileri.getIfsKodu();
        }
        else if (intent.getStringExtra("from").equals("PaletIslemleri")){
            paletRef = intent.getStringExtra("lrf");
            // PALET BILGILERI GETIR
            loadPaletBilgileri(paletRef);
        }
    }

    public void setProcessValues(){
        cariKod = paletBilgileri.getCari_kod();
        adet = paletBilgileri.getPaletAdet();
        etiketTipi =  paletBilgileri.getEtiketi();

        Log.d("GET ITENT DATA",
                "- paletRef: " + String.valueOf(paletRef)
                        +"- cari: " + cariKod
                        +"- adet: " + adet
                        +"- tip: " + etiketTipi
                        +"- sec eskkd: " + eskiKod
                        +"- ykod: " + urunKodu
                        + "- y tanim: " + urunTani);

    }

    public void operation(String inputText){
        if(inputText != "" && inputText.length() >= 10){
//            PaletItemsModel paletItemsModel = new PaletItemsModel(""+inputText,itemCount+"_USTKOD", itemCount+"_IFSTKOD", itemCount+"_IFS_ST_TANI" );
//            Log.d("P Item MODEL DATA::", paletItemsModel.toString());
//            PaletAdapter.OPERATION = "PALET_ITEM";
//            paletAdapter.addPaletitem(paletItemsModel);
//            paletAdapter.notifyDataSetChanged();
//            bKodInput.setText("");
            uretimBarkod = inputText;
            urunEkelemeKontrol(inputText);
        }
    }

    // Palet Bilgileri Getir
    public void loadPaletBilgileri(String paletRef_){
            BackgroudTask task = new BackgroudTask(this);
            Log.d("loadPaletBilgileri:->", paletRef_);
            task.execute("loadPaletBilgeri", paletRef_);
    }
    public void paletBilgiGoster() {
        LayoutInflater inflater3 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View paletBilgiDialogView = inflater3.inflate(R.layout.palet_bilgileri, null, false);
        TextView viewTitle = paletBilgiDialogView.findViewById(R.id.viewTitle);
        viewTitle.setText("Palet Bilgileri");



        TextView ifsId_ = paletBilgiDialogView.findViewById(R.id._ifsId);
        ifsId_.setText(String.valueOf(paletBilgileri.getIfsID()));

        TextView lref_ = paletBilgiDialogView.findViewById(R.id.lref);
        lref_.setText(String.valueOf(paletBilgileri.getLref()));

        TextView barkod = paletBilgiDialogView.findViewById(R.id.barkod);
        barkod.setText(String.valueOf(paletBilgileri.getBarkod()));

        TextView cari_kod = paletBilgiDialogView.findViewById(R.id.cari_kod);
        cari_kod.setText(String.valueOf(paletBilgileri.getCari_kod()));

        TextView cariUnvani = paletBilgiDialogView.findViewById(R.id.cariUnvani);
        cariUnvani.setText(String.valueOf(paletBilgileri.getCari_unvan1()));

        TextView aktifLabel = paletBilgiDialogView.findViewById(R.id.arkifMi);
            if(paletBilgileri.getAktifMi()) aktifLabel.setText("Evet");
            else aktifLabel.setText("Hayır");

        TextView kapaliLabel = paletBilgiDialogView.findViewById(R.id.kapaliMi);
            if(paletBilgileri.getKapaliMi()) kapaliLabel.setText("Evet");
            else kapaliLabel.setText("Hayır");

        TextView etiket = paletBilgiDialogView.findViewById(R.id.etiket);
        etiket.setText(String.valueOf(paletBilgileri.getEtiketi()));

        TextView urun_stok_kodu = paletBilgiDialogView.findViewById(R.id.urun_stok_kodu);
        urun_stok_kodu.setText(String.valueOf(paletBilgileri.getUru_stok_kod()));

        TextView urunAdi = paletBilgiDialogView.findViewById(R.id.urunAdi);
        urunAdi.setText(String.valueOf(paletBilgileri.getUrunAdi()));

        TextView ifsKodu = paletBilgiDialogView.findViewById(R.id.ifsKodu);
        ifsKodu.setText(String.valueOf(paletBilgileri.getIfsKodu()));

        TextView IfsHandlingUnitType = paletBilgiDialogView.findViewById(R.id.IfsHandlingUnitType);
        IfsHandlingUnitType.setText(String.valueOf(paletBilgileri.getIfsHandlingUnitType()));

        TextView IfsHandlingUnitId = paletBilgiDialogView.findViewById(R.id.IfsHandlingUnitId);
        IfsHandlingUnitId.setText(String.valueOf(paletBilgileri.getIfsHandlingUnitId()));

        TextView paletAdet = paletBilgiDialogView.findViewById(R.id.PaletAdet);
        paletAdet.setText(String.valueOf(paletBilgileri.getPaletAdet()));

        TextView TakimAdet = paletBilgiDialogView.findViewById(R.id.TakimAdet);
        TakimAdet.setText(String.valueOf(paletBilgileri.getTakimAdet()));

        TextView IcerikDurumu = paletBilgiDialogView.findViewById(R.id.IcerikDurumu);
        IcerikDurumu.setText(String.valueOf(paletBilgileri.getIcerikDurumu()));

        TextView BoyMu = paletBilgiDialogView.findViewById(R.id.BoyMu);
        if(paletBilgileri.getBoyMu()) BoyMu.setText("Evet");
        else BoyMu.setText("Hayır");
        // BoyMu.setText(String.valueOf(paletBilgileri.getBoyMu()));

        TextView TakimDetayliMi = paletBilgiDialogView.findViewById(R.id.TakimDetayliMi);
        if(paletBilgileri.getTakimDetayliMi()) TakimDetayliMi.setText("Evet");
        else aktifLabel.setText("Hayır");
        //TakimDetayliMi.setText(String.valueOf(paletBilgileri.getTakimDetayliMi()));

        TextView HollandaDepoMu = paletBilgiDialogView.findViewById(R.id.HollandaDepoMu);
        if(paletBilgileri.getHollandaDepoMu()) HollandaDepoMu.setText("Evet");
        else HollandaDepoMu.setText("Hayır");
        //HollandaDepoMu.setText(String.valueOf(paletBilgileri.getHollandaDepoMu()));

        TextView KullanicilarRef = paletBilgiDialogView.findViewById(R.id.KullanicilarRef);
        KullanicilarRef.setText(String.valueOf(paletBilgileri.getKullanicilarRef()));

        TextView tarih = paletBilgiDialogView.findViewById(R.id.tarih);
        tarih.setText(String.valueOf(paletBilgileri.getTarih()));

        Log.d("DIPLAY PAL: ", paletBilgileri.toString());

        paletBilgiDialogBuilder.setView(paletBilgiDialogView);
        paletBilgiDialog =  paletBilgiDialogBuilder.create();
        paletBilgiDialogBuilder.show();

        Button close = paletBilgiDialogView.findViewById(R.id.dialogClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paletBilgiDialog.dismiss();
            }
        });
    }

    // PALET ICERIKLERI YUKLEME
    public void loadPaletIcerikleri(String paletRef_){
        Toast.makeText(this, "Palet Icerik detay Called", Toast.LENGTH_SHORT).show();
//        paletIcerikDetayListView.invalidateViews();
//        paletAdapter.notifyDataSetChanged();
            paletIcerikDetayAdapter.clear();
            BackgroudTask task = new BackgroudTask(this);
            Log.d("loadPaletIcerikkeri:->", paletRef_);
            task.execute("loadPaletIcerikkeri", paletRef_);
    }

    // PALET ICERIK OZETI
    public void loadPaletIcerikOzet(String paletRef_){
        Toast.makeText(this, "Palet Icerik ozet Called", Toast.LENGTH_SHORT).show();
        paletIcerikOzetListView.invalidateViews();
        palletIcerikOzetAdapter.notifyDataSetChanged();
        BackgroudTask task = new BackgroudTask(this);
        task.execute("loadPaletIcerikOzeti", paletRef_);
    }

    // URUN EKELEME KONTROLU
    public void urunEkelemeKontrol(String urunBarkod){
            BackgroudTask task = new BackgroudTask(this);
            // Log.d("urunEkelemeKontrol:->", paletRef, );
            this.uretimBarkod = urunBarkod;
            Log.d("UKontr_SEND_DATA",
                " bkd: "+urunBarkod
                        +"- lrf: " + String.valueOf(paletBilgileri.getLref())
                        +"- cari: " + cariKod
                        + "- tip: " + etiketTipi
                        + "- sec eskkd: " + eskiKod
                        +"- ykod: " + urunKodu
                        + "- y tanim: " + urunTani);
            task.execute("urunEkelemeKontrol", urunBarkod, String.valueOf(paletBilgileri.getLref()),
                    paletBilgileri.getCari_kod(),
                    paletBilgileri.getEtiketi(), eskiKod, paletBilgileri.getIfsKodu(), urunTani);

    }

    // URUN EKELEM KONTROL SONUCU
    public void controlResult(ArrayList<ProductModel> urunlistesí){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customDialogView = inflater.inflate(R.layout.dialog_row_item, null, false);
        dialogBuilder.setView(customDialogView);
        yeniKodListview = (ListView) customDialogView.findViewById(R.id.listView);
        Log.d("TOTAL SIZE", ":-"+ urunlistesí.size());
        if(!urunlistesí.isEmpty()){
            yeniPaletAdapter = new PaletAdapter(this, R.layout.dialog_list_item);
            Log.d("RESULT_LIST: ", "IN NOT EMPTY");
            if(urunlistesí.size() == 1){
                Log.d("RESULT_LIST: ", "IN == 1");
                for(int i = 0; i < urunlistesí.size(); i++){
                    if(urunlistesí.get(i).getHata() == false){
                        operationDialog("Urun kontrolu",
                                urunlistesí.get(i).getHataMesaji(), false, "", true, "Tamam");
                        break;
                    }
                    else {
                        operationDialog("Urun kontrolu",
                                "dogru urun hatasiz", false, "", true, "Tamam");
                        // SET the values and Add
                    }
                }
            }
            else{
                Log.d("RESULT_LIST: ", "IN MORE TANT ONE");
                for(int i = 0; i < urunlistesí.size(); i++){
                    PaletAdapter.OPERATION = "YENI_KODLAR";
                    if((!urunlistesí.get(i).getYeniKodu().equals(null) || !urunlistesí.get(i).getYeniKodu().equals("null"))
                        && (!urunlistesí.get(i).getYeniTanim().equals(null) || !urunlistesí.get(i).getYeniTanim().equals("null"))
                    && (!urunlistesí.get(i).getEskiKod().equals(null) || !urunlistesí.get(i).getEskiKod().equals("null"))
                    ){
                        yeniPaletAdapter.addYeniKod(new ProductModel(
                                        urunlistesí.get(i).getYeniKodu(),
                                        urunlistesí.get(i).getYeniTanim(),
                                        urunlistesí.get(i).getEskiKod())
                        );
                        yeniKodListview.setAdapter(yeniPaletAdapter);
                    }
                }
                dialog =  dialogBuilder.create();
                dialog.setTitle("Uygun Ürün Seçin");
                dialog.show();
            }
        }
        else {
            // Call urun ekle / UrunEANBarkod
            urunEANBarkodGetir(urunKodu);
            urunEkleme();
        }
    }

    // URUN EAN BARKODU GETIRME
    public void urunEANBarkodGetir(String yeniKod){
        BackgroudTask task = new BackgroudTask(this);
        Log.d("EAN GETIR"," yeni kod: " + yeniKod);
        task.execute("urunEANGetir", yeniKod);
    }

    // URUN EKLEME
    public void urunEkleme() {
        //Palet adet kontrolü android tarafında kontrol edilecek!!
        if(paletBilgileri.getEtiketi().equals("Tek tip")){
            if(paletBilgileri.getPaletAdet() > 0) {
                if(paletAdetKontrol + 1 > paletBilgileri.getPaletAdet()){
                    operationDialog("Urun Eklem kontrolu",
                            "Paletlenecek Ürün Miktarını Aşan Okutma Yaptınız. Lütfen Paleti Kontrol Edin",
                            false, "", true, "Tamam");
                    return;
                }
            }
        }

        int _adet = 1;
        BackgroudTask task = new BackgroudTask(this);
        Log.d("EURUN EKLEME"," DATA: lrf: " + paletRef
                                    +" UrunEskiKod_: "+eskiKod
                +" Adet_: "+ String.valueOf(_adet)
                +" KullanicilarRef_: "+user.getKullaniciRef()
                +" EanBarkod_: "+urunEANBarkod
                +" UrunYabanciAdi_: "+urunTani
                +" UretimBarkodu_: "+ uretimBarkod
                +" UrunTipi_: "+etiketTipi
                +" IfsStokKodu_: "+urunKodu
                +" UrunTanimi_: "+urunTani);
        task.execute("URUN_EKLEME", paletRef, eskiKod, String.valueOf(_adet),
                String.valueOf(user.getKullaniciRef()), urunEANBarkod,
                urunTani, uretimBarkod, etiketTipi, urunKodu, urunTani);
    }

    public void alertDialog(String opTitle, String message){
        LayoutInflater inflater2 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View yesnoDialogView = inflater2.inflate(R.layout.dialog_layout, null, false);
        TextView operationTitle = yesnoDialogView.findViewById(R.id.operationTitle);
        operationTitle.setText(opTitle);
        TextView content = yesnoDialogView.findViewById(R.id.dialogContent);
        content.setText(message);
        Button noBtn = yesnoDialogView.findViewById(R.id.noButton),
                yesBtn = yesnoDialogView.findViewById(R.id.yesButton);
        if(action.equals("PALET_BITIR") ){
            noBtn.setText("Hayır/İptal");
            yesBtn.setText("Evet");
        }
        else if(action.equals("PALET_BOZ")){
            noBtn.setText("Hayır");
            yesBtn.setText("Evet");
        }
        else if(action.equals("PALET_KAPALI")){
            noBtn.setVisibility(View.GONE);
            yesBtn.setText("Tamam");
        }
        else if(action.equals("PALET_BITIR_RESULT")){
            noBtn.setVisibility(View.GONE);
            yesBtn.setText("Tamam");
        }

        yesnoDialogBuilder.setView(yesnoDialogView);
        yesnoDialog =  yesnoDialogBuilder.create();
        yesnoDialog.show();

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(action.equals("PALET_BITIR")){
                    yesnoDialog.dismiss();
                    action  = "";
                }
                Log.d("ALERT_BUTTON", action);
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(action.equals("PALET_BITIR")){
                    paletBitir();
                }
                else if(action.equals("PALET_KAPALI")){
                    Intent itent = new Intent(getBaseContext(), PaletlerListesi.class);
                     startActivity(itent);
                }
                else if(action.equals("PALET_BITIR_RESULT")){
                    Intent itent = new Intent(getBaseContext(), PaletOlusturmaMenu.class);
                    startActivity(itent);
                }
                Log.d("ALERT_BUTTON", action);
            }
        });
    }

    public void operationDialog(String opTitle, String message, Boolean noBtn, String noBtnText, Boolean yesBtn, String yesBtnText){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customDialogView = inflater.inflate(R.layout.dialog_layout, null, false);
        TextView operationTitle = customDialogView.findViewById(R.id.operationTitle);
        operationTitle.setText(opTitle);
        TextView messageContent = customDialogView.findViewById(R.id.dialogContent);
        messageContent.setText(message);

        Button noButton = customDialogView.findViewById(R.id.noButton);
        noButton.setText(noBtnText);
        if(!noBtn) noButton.setVisibility(View.GONE);
        Button yesButton = customDialogView.findViewById(R.id.yesButton);
        yesButton.setText(yesBtnText);
        if(!yesBtn) noButton.setEnabled(false);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBuilder.setView(customDialogView);
        dialog =  dialogBuilder.create();
        dialog.show();
    }

    public void paletAdetlerHesapla(ArrayList<PaletIcerikModel> paletIcerikList){
        if(!paletIcerikList.isEmpty() && paletIcerikList.size() > 0){
            Log.d("PALET ICERIK OZET SIZE", String.valueOf(paletIcerikList.size()));
            for(int i=0;  i < paletIcerikList.size(); i++){
                paletAdetKontrol += paletIcerikList.get(i).getAdet();
            }
            Log.d("paletAdetKontrol = ", String.valueOf(paletAdetKontrol));
        }
    }

    public void paletBitir(){
        BackgroudTask task = new BackgroudTask(this);
        Log.d("palet Bitir fc called ", "PALET BITTIRME");
        task.execute("PALET_BITIR");
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

            else if (ACTION.equals("loadPaletIcerikOzeti")) {
                result = service.paletIcerikOzet(params[1]);
            }

            else if (ACTION.equals("urunEkelemeKontrol")) {
                Log.d("UKontr_Data", " bkd: "+params[1]
                                +"- lrf: " + String.valueOf(Integer.valueOf(params[2]))
                                +"- cari: " + params[3] + "- tip: " + params[4]
                                + "- sec eskkd: " + params[5] +"- ykod" + params[6]
                                + "- y tanim: " + params[7]);
                result = service.UrunEklemeKontrol(params[1], Integer.valueOf(params[2]), params[3],
                        params[4], params[5], params[6], params[7]);
            }

            else if (ACTION.equals("urunEANGetir")) {
                result = service.urunEANBarkodGetir(params[1]);
            }

            else if (ACTION.equals("URUN_EKLEME")) {
                Log.d("U EKLEME TO SERVICE",
                "lrf: " + params[1] +" UrunEskiKod_: "+params[2] +" Adet_: "+ params[3]
                +" KullanicilarRef_: "+params[4] +" EanBarkod_: "+params[5]
                +" UrunYabanciAdi_: "+params[6] +" UretimBarkodu_: "+params[7]
                +" UrunTipi_: "+params[8] +" IfsStokKodu_: "+params[9] +" UrunTanimi_: "+params[10]);
                result = service.UrunEkle(Integer.valueOf(params[1]), params[2],
                        Integer.valueOf(params[3]), params[4], params[5],params[6],
                        params[7],params[8],params[9], params[10]);
            }

            else if(ACTION.equals("PALET_BITIR")){
                Log.d("PALET_BITIR Data", paletRef
                        +" cont: " + user.getContract()
                        +" -- loc: " +user.getLocationNo()
                        +" -- pKon: "+paletAdetKontrol
                        +" -- kapt: "+String.valueOf("true")
                        +" -- EtYaz: "+String.valueOf("false")
                        +" -- kRef: "+user.getKullaniciRef()
                        +" -- depN: "+ user.getDepoNo()
                        +" -- etId: "+ user.getPaletEtiketID()
                        +" -- yazAdi : "+ user.getPaletEtiketPrinter()
                );
                yesnoDialog.dismiss();
                result = service.paletBitir(Integer.valueOf(paletRef), user.getContract(),
                        user.getLocationNo(), paletAdetKontrol,
                        true,  false, user.getKullaniciRef(),
                        user.getDepoNo(),
                        user.getPaletEtiketID(),
                        user.getPaletEtiketPrinter());
            }
            return null;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String result2) {
            if (ACTION.equals("loadPaletBilgeri")) {
                Log.d("RESULT -> :", result);
                paletBilgileri = paletBilgiXMLParse(result);
                Log.d("Palet Bilgi -> :", paletBilgileri.toString());
                // PALET KAPALI DEGIL ISE AC
                if(!paletBilgileri.getKapaliMi()){
                    Toast.makeText(getApplicationContext(), "Palet kapali degil", Toast.LENGTH_SHORT).show();
                    loadPaletIcerikleri(paletRef);
                    setProcessValues();
                }
                else {
                    action = "PALET_KAPALI";
                    alertDialog("Palet İşlem Mesajı", "Bu palet kapalıdır, İşlem yapamazsınız");
                }
            }
            else if (ACTION.equals("loadPaletIcerikkeri")) {
                Log.d("RESULT_loadPaletIcerikkeri:", result);
                paletIcerikList = paletIcerikXMLParse(result);
                loadPaletIcerikOzet(paletRef);
            }
            else if (ACTION.equals("loadPaletIcerikOzeti")) {
                Log.d("RESULT _loadPaletIcerikOzeti:", result);
                paletIcerikList = paletIcerikOzetiXMLParse(result);
                paletAdetlerHesapla(paletIcerikList);
            }
            else if (ACTION.equals("urunEkelemeKontrol")) {
                Log.d("RESULT_urunEkelemeKontrol :", result);
                urunlistesí = urunKontrolXMLParser(result);
                controlResult(urunlistesí);
            }
            else if (ACTION.equals("urunEANGetir")) {
                Log.d("RESULT_urunEANGetir :", result);
                urunEANBarkod = urunEANGetirParser(result);
                Log.d("urunEANBarkod :", urunEANBarkod);
            }
            else if (ACTION.equals("URUN_EKLEME")) {
                Log.d("RESULT EURUN_EKLEME:", result);
//                urunEANBarkod = urunEANGetirParser(result);
//                Log.d("urunEANBarkod:", urunEANBarkod);

                finish();
                startActivity(getIntent());
            }
            else if(ACTION.equals("PALET_BITIR")){
                Log.d("PALET_BITIR Result :", result);
                String response  = paletBitirResponseXMLParse(result);
                if(response.equals("")){
                    Toast.makeText(getBaseContext(), "Palet bitirilmiştir", Toast.LENGTH_SHORT);
                    action = "PALET_BITIR_RESULT";
                    alertDialog("Palet Bitir Mesajı", "Palet bitirilmiştir");
                }
                else {
                    action = "PALET_BITIR_RESULT";
                    alertDialog("Palet Bitir Mesajı", response);
                }
            }
        }

        // PALETLER BILGILERI
        public PaletModel paletBilgiXMLParse(String result_){
            int elementCount = 0;
            PaletModel paletBilgisi = null;
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean IfsHandlingUnitIdBool = false, LRefBool = false, BarkodBool = false, cari_kodBool = false,
                        cari_unvan1Bool = false, AktifMiBool=false, KapaliMiBool = false,  EtiketiBool = false,
                        uru_stok_kodBool = false, UrunAdiBool=false, IfsKoduBool=false, IfsHandlingUnitTypeBool = false, PaletAdetBool = false,
                        TakimAdetBool = false, IcerikDurumuBool=false, BoyMuBool = false,  TakimDetayliMiBool = false,
                        HollandaDepoMuBool = false, KullanicilarRefBool=false, TarihBool = false,  KapanmaTarihiBool = false;

                int IfsHandlingUnitId = 0,  LRef = 0;
                String Barkod = "", cari_kod = "", cari_unvan1 = "";
                Boolean AktifMi = false, KapaliMi = false;

                String Etiketi = "", uru_stok_kod = "", UrunAdi ="", IfsKodu = "", IfsHandlingUnitType = ""; //X

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
                        if(xmlPullParser.getName().equals("UrunAdi")) UrunAdiBool = true;
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
                        { IfsHandlingUnitId = Integer.valueOf(xmlPullParser.getText()); IfsHandlingUnitIdBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(LRefBool)
                        { LRef = Integer.valueOf(xmlPullParser.getText());; LRefBool = false; }
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
                        if(EtiketiBool) { Etiketi = xmlPullParser.getText(); EtiketiBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(uru_stok_kodBool) { uru_stok_kod = xmlPullParser.getText(); uru_stok_kodBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(UrunAdiBool) { UrunAdi = xmlPullParser.getText(); UrunAdiBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(IfsKoduBool) { IfsKodu = xmlPullParser.getText(); IfsKoduBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(IfsHandlingUnitTypeBool) { IfsHandlingUnitType = xmlPullParser.getText(); IfsHandlingUnitTypeBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(PaletAdetBool) { PaletAdet = Integer.valueOf(xmlPullParser.getText()); PaletAdetBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(TakimAdetBool) { TakimAdet = Integer.valueOf(xmlPullParser.getText()); TakimAdetBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(IcerikDurumuBool) { IcerikDurumu = xmlPullParser.getText(); IcerikDurumuBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(BoyMuBool) { BoyMu = Boolean.valueOf(xmlPullParser.getText()); BoyMuBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(TakimDetayliMiBool) { TakimDetayliMi = Boolean.valueOf(xmlPullParser.getText()); TakimDetayliMiBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(HollandaDepoMuBool) { HollandaDepoMuBool = Boolean.valueOf(xmlPullParser.getText()); HollandaDepoMuBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(BoyMuBool) { BoyMu = Boolean.valueOf(xmlPullParser.getText()); BoyMuBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(KullanicilarRefBool) { KullanicilarRef = xmlPullParser.getText(); KullanicilarRefBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(TarihBool) { Tarih = xmlPullParser.getText(); TarihBool = false; }
                    }
                    if(event == xmlPullParser.TEXT){ // If
                        if(KapanmaTarihiBool) { KapanmaTarihi = xmlPullParser.getText(); KapanmaTarihiBool = false; }
                    }

                    if (LRef != 0 && !Barkod.equals("")){
                        elementCount = elementCount + 1;
                        paletBilgisi = new PaletModel(IfsHandlingUnitId, LRef, Barkod, cari_kod, cari_unvan1,
                                AktifMi, KapaliMi,Etiketi, uru_stok_kod, UrunAdi, IfsKodu, IfsHandlingUnitType,
                                PaletAdet, TakimAdet, IcerikDurumu, BoyMu, TakimDetayliMi,
                                HollandaDepoMu, KullanicilarRef, Tarih, KapanmaTarihi );
                        // customerArrayList.add(customerModel);
//                        Log.d("PALET BILGILERI", paletBilgisi.toString());
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
                        if(xmlPullParser.getName().equals("UrunAdi"))  UrunAdiBool = false;
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
//            Log.d("PALET BILGISI", paletBilgisi.toString());
            cariKod = paletBilgisi.getCari_kod();
            adet = paletBilgisi.getPaletAdet();
            etiketTipi =  paletBilgisi.getCari_kod();
            urunKodu = paletBilgisi.getIfsKodu();
            // urunTani = paletBilgileri.getI;
            eskiKod = paletBilgisi.getUru_stok_kod();
            return paletBilgisi;
        }

        // PALET ICERILERI
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

                        paletIcerikDetayAdapter.addPaletIcerikDetay(icerikModel);

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

        // Parse PALET ICERIK OZETI
        public ArrayList<PaletIcerikModel> paletIcerikOzetiXMLParse(String result_){
            ArrayList<PaletIcerikModel> paletIcerikOzet = new ArrayList<>();
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean ifs_Stok_KoduBool = false,  ifs_Stok_TanimiBool = false, uru_Stok_SodBool = false,
                        urunAdiBool = false,  adetBool = false ;

                String uru_Stok_Sod = "", ifs_Stok_Kodu = "", ifs_Stok_Tanimi = "", urunAdi = "" ;
                int adet = 0;

                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("IFS_stok_kodu")) ifs_Stok_KoduBool = true;
                        if(xmlPullParser.getName().equals("IFS_stok_tanimi")) ifs_Stok_TanimiBool = true;
                        if(xmlPullParser.getName().equals("uru_stok_kod")) uru_Stok_SodBool = true;
//                        if(xmlPullParser.getName().equals("UrunAdi")) urunAdiBool = true;
                        if(xmlPullParser.getName().equals("Adet")) adetBool = true;
                    }


                    if(event == xmlPullParser.TEXT)
                        if(ifs_Stok_KoduBool){ ifs_Stok_Kodu = xmlPullParser.getText(); ifs_Stok_KoduBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(ifs_Stok_TanimiBool){ ifs_Stok_Tanimi = xmlPullParser.getText(); ifs_Stok_TanimiBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(uru_Stok_SodBool){ uru_Stok_Sod = xmlPullParser.getText(); uru_Stok_SodBool = false; }
//                    if(event == xmlPullParser.TEXT)
//                        if(urunAdiBool){ urunAdi = xmlPullParser.getText(); urunAdiBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(adetBool){ adet = Integer.valueOf(xmlPullParser.getText()); adetBool = false; }

                    if (!uru_Stok_Sod.equals("") && !ifs_Stok_Tanimi.equals("") && !ifs_Stok_Kodu.equals("") && adet != 0){
                        PaletIcerikModel icerikOzetModel = new PaletIcerikModel(uru_Stok_Sod, ifs_Stok_Tanimi, ifs_Stok_Kodu, adet);
                        paletIcerikOzet.add(icerikOzetModel);
//                        PaletAdapter.OPERATION = "PALET_ICERIK_OZET";
                        palletIcerikOzetAdapter.addPaletIcerikOzeti(icerikOzetModel);

                        ifs_Stok_Kodu = ""; ifs_Stok_Tanimi = "";uru_Stok_Sod = ""; urunAdi = ""; adet = 0;
                    }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("IFS_stok_kodu")) ifs_Stok_KoduBool = false;
                        if(xmlPullParser.getName().equals("IFS_stok_tanimi")) ifs_Stok_TanimiBool = false;
                        if(xmlPullParser.getName().equals("uru_stok_kod")) uru_Stok_SodBool = false;
//                        if(xmlPullParser.getName().equals("UrunAdi")) urunAdiBool = false;
                        if(xmlPullParser.getName().equals("Adet")) adetBool = false;
                    }
                    event = xmlPullParser.next();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return paletIcerikOzet;
        }

        // Parse PALET ICERIK OZETI
        public ArrayList<ProductModel> urunKontrolXMLParser(String result_){
            ArrayList<ProductModel> productContent = new ArrayList<>();
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean yeniKodBool = false, yeniTanimBool = false,
                        hataBool = false, hataMesajiBool = false ;

                String yeniKod = "", yeniTanim = "", hataMesaji = "";
                Boolean hata = false;

                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("Hata")) hataBool = true;
                        if(xmlPullParser.getName().equals("HataMesaji")) hataMesajiBool = true;
                        if(xmlPullParser.getName().equals("YeniKod")) yeniKodBool = true;
                        if(xmlPullParser.getName().equals("YeniTanim")) yeniTanimBool = true;
                    }

                    if(event == xmlPullParser.TEXT)
                        if(hataBool){ hata = Boolean.valueOf(xmlPullParser.getText()); hataBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(hataMesajiBool){ hataMesaji = xmlPullParser.getText(); hataMesajiBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(yeniKodBool){ yeniKod = xmlPullParser.getText(); yeniKodBool = false; }
                    if(event == xmlPullParser.TEXT)
                        if(yeniTanimBool){ yeniTanim = xmlPullParser.getText(); yeniTanimBool = false; }

                    if ((hata || !hata && (!hataMesaji.equals("") || !yeniKod.equals("") || !yeniTanim.equals("")))){
                        Log.d("FUND_DATA:- ", yeniKod + "-" + yeniTanim + "-"+ String.valueOf(hata) + "-" + hataMesaji);
                        ProductModel productModel = new ProductModel(yeniKod, yeniTanim, hata, hataMesaji);
                        Log.d("Returned PoroductModel", productModel.toString());
                        productContent.add(productModel);
                        yeniKod = ""; yeniTanim = ""; hata = false; hataMesaji = "";
                    }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("Hata")) hataMesajiBool = false;
                        if(xmlPullParser.getName().equals("HataMesaji")) hataMesajiBool = false;
                        if(xmlPullParser.getName().equals("YeniKod")) yeniKodBool = false;
                        if(xmlPullParser.getName().equals("YeniTanim")) yeniTanimBool = false;
                    }
                    event = xmlPullParser.next();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return productContent;
        }

        public String urunEANGetirParser(String result_){
            String earnBarKod = "";
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean stringBool = false;

                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("string")) stringBool = true;
                        if(xmlPullParser.getName().equals("UrunEANBarkodunuGetirResult"))
                            stringBool = true;
                    }
                    if(event == xmlPullParser.TEXT)
                        if(stringBool){ earnBarKod = xmlPullParser.getText(); stringBool = false; }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("string")) stringBool = false;
                        if(xmlPullParser.getName().equals("UrunEANBarkodunuGetirResult")) stringBool = false;
                    }

                    event = xmlPullParser.next();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return earnBarKod;
        }

        public String paletBitirResponseXMLParse(String result_){
            String response = "";
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean stringBool = false;

                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("PaletBitirResponse")) stringBool = true;
                        if(xmlPullParser.getName().equals("PaletiBitirResult")) stringBool = true;
                    }

                    if(event == xmlPullParser.TEXT)
                        if(stringBool){ response = xmlPullParser.getText(); stringBool = false; }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("PaletBitirResponse")) stringBool = false;
                        if(xmlPullParser.getName().equals("PaletiBitirResult")) stringBool = false;
                    }
                    event = xmlPullParser.next();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return response;
        }
    }

}