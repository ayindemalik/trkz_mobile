package com.example.trkz_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.trkz_mobile.databinding.YeniPaletOlusturmaBinding;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YeniPaletOlusturma extends AppCompatActivity {
    SharedPreferences pref;
    UserModel user;
    //    VARIABLES
    public YeniPaletOlusturmaBinding binding;
//    Spinner ypFirmaAdi, ypPaletTipi, ypEtiket;
    Spinner  ypPaletTipi, ypEtiket;
    SearchableSpinner ypFirmaAdi;
    EditText ypUrunBarKodu, ypUrunKodu, ypUrunTani, ypAdet;
    Button ypBtnDevam;
    CustomDropDownArrayAdapter customDropDownArrayAdapter, pTcustomDropDownArrayAdapter;
    PaletTipiSpinnerAdapter paletTipiSpinnerAdapter;
    String musteriID, musteriAdi, etiketTipi, urunBarKodu, urunKodu, paletTuru, eskiKod;
    int adet ;
    RadioGroup ypKisaBoy;

//    FOR ALERT DIALOG
    PaletAdapter paletAdapter;
    ListView yeniKodListview;

    long delay = 2000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();
    AlertDialog dialog;
    AlertDialog.Builder yes_noBuilder;

    /* VAR To GET HANDLING UNIT TYPES */
    String categoryID = "11", kisa_boy = "";

    /* TO CAlll BACKGROUND TASK*/
    BackgroudTask task;
    String BACKGROUND_ACTION = "";

    String paletLrf = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.yeni_palet_olusturma);
        binding = YeniPaletOlusturmaBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);
//        pref = getSharedPreferences("UserPreferences", 0);
        // CASTING
        setBindings();

        pref = getSharedPreferences("UserPreferences", 0);
        setUserData(pref);

//        customDropDownArrayAdapter = new CustomDropDownArrayAdapter(this, R.layout.yp_customers);
//        pTcustomDropDownArrayAdapter = new CustomDropDownArrayAdapter()
//        ypFirmaAdi.setAdapter(customDropDownArrayAdapter);

//        int kullRef = pref.getInt("kullaniciRef", 0);

        task = new BackgroudTask(this);
        BACKGROUND_ACTION = "loadCustomer";
        task.execute(BACKGROUND_ACTION);

        loadEtiket();

        //
        ypFirmaAdi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                musteriID = ((TextView) view.findViewById(R.id.musteriID)).getText().toString();
                musteriAdi = ((TextView) view.findViewById(R.id.musteriAdi)).getText().toString();
                Toast.makeText(getBaseContext(), musteriID + " / " +musteriAdi + " Secçildi.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /* On Etiket change */
        ypEtiket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                etiketTipi = parent.getItemAtPosition(i).toString();;
                Toast.makeText(getBaseContext(),  " Etiket : " +etiketTipi,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //
        ypUrunBarKodu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

        /* PALET TIPI SECILINCE */
        ypPaletTipi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                adet = Integer.parseInt(((TextView) view.findViewById(R.id.qty)).getText().toString());
                adet = Integer.valueOf(String.valueOf(((TextView) view.findViewById(R.id.qty)).getText().toString()));
//                ypAdet.setText(adet);
                ypAdet.setText(String.valueOf(adet));
                paletTuru = ((TextView) view.findViewById(R.id.handlingTypeId)).getText().toString();
                Toast.makeText(getBaseContext(), adet + " / " +paletTuru + " p turu olarak secçildi.",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // CASTING AND LIST ITEM IN DIALOG
        ypKisaBoy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selected = findViewById(R.id.pkisa);
                if (selected.isChecked()){
                    kisa_boy = "K";
                    Toast.makeText(getBaseContext(), kisa_boy+" is secildi", Toast.LENGTH_SHORT).show();
                    // LOAD HANDLIND TYPES
                }
                else{
                    selected = findViewById(R.id.pboy);
                    if (selected.isChecked()){
                        kisa_boy = "B";
                        Toast.makeText(getBaseContext(), kisa_boy+" is secildi", Toast.LENGTH_SHORT).show();
                        // LOAD HANDLIND TYPES
                    }
                }

                // LOAD HANDLIND TYPES
//                Toast.makeText(getBaseContext(),  " HU Data: Cat :" +  categoryID +"-- KB: "+kisa_boy
//                        +"--  kod: "+ ypUrunKodu.getText().toString(),Toast.LENGTH_SHORT).show();
                loadHandlingUnitTypes(categoryID, kisa_boy, ypUrunKodu.getText().toString());
            }
        });

        // PALET OLUSTURMA
        ypBtnDevam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  dialogBody;
                // CONTROLS
                if(etiketTipi.equals("Palet tipi seç")){
                    displayDialog("Lütfen palet tipi seçin", "dataControl");
                    return;
                }

                if(kisa_boy.equals("")){
//                    yes_noBuilder.setMessage("");
                    displayDialog("Lütfen Palet tipi Boy veya Kısa seçin", "dataControl");
                    return;
                }

                try {
                    adet = Integer.valueOf(String.valueOf(ypAdet.getText()));
                }catch (NumberFormatException ex){
//                    yes_noBuilder.setMessage(ex.getMessage());
//                    yes_noBuilder.show();
                }

                if(ypAdet.getText().toString().equals("") || adet == 0){
                    yes_noBuilder.setMessage("Lütfen ürün adetini girin");
                    displayDialog("Lütfen ürün adetini girin, 0 veya boş olamaz", "dataControl");
                    return;
                }

                urunKodu = ypUrunKodu.getText().toString();
                if(etiketTipi.equals("Tek tipi") && urunKodu.equals("")){
//                    yes_noBuilder.setMessage("Lütfen ürün barkodunu okutarak ürün kodudu bulun girin");
                    displayDialog("Lütfen ürün barkodunu okutarak ürün kodudu bulun girin", "dataControl");
                    return;
                }
//                if(musteriAdi.equals("Müşteri Seçin") || musteriID.equals("")){
//                    displayDialog("Lütfen Müşteriyi seçin", "dataControl");
//                    return;
//                }

                // DISPLAY MESSAGE BEFORE DEVAM
                dialogBody = "Bu bilgilere göre palet oluşturulmasını emin misiniz ?"
                        + "\nMüşteri adı: " + musteriAdi
                        + "\nPalet Tipi: " + etiketTipi
                        + "\nÜrün kodu: " + urunKodu
                        + "\nPalet Boyu: " + kisa_boy
                        + "\nPalet Türü: " + paletTuru
                        + "\nÜrün adeti: " + adet
                        ;
                displayDialog(dialogBody, "Devam");
            }
        });



        Toast.makeText(getBaseContext(), "User data " + user.getKullaniciRef(), Toast.LENGTH_LONG);
    }
    public void setBindings(){
        // SPINNERS
        ypFirmaAdi = binding.ypFirmaAdi;
        ypPaletTipi = binding.ypPaletTipi;
        ypEtiket = binding.ypEtiket;
        //  EDITEX
        ypUrunBarKodu = binding.ypUrunBarKodu;
        ypUrunKodu = binding.ypUrunkodu;
        ypUrunTani = binding.ypUrunTani;
        ypUrunKodu.setEnabled(false);
        ypUrunTani.setEnabled(false);
        // Radio Group
        ypKisaBoy = binding.ypKisaBoy;
        ypAdet = binding.ypAdet;
        // Buttons
        ypBtnDevam = binding.ypBtnDevam;

        yes_noBuilder =  new AlertDialog.Builder(YeniPaletOlusturma.this);
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

    public void displayDialog(String body, String action){
        yes_noBuilder =  new AlertDialog.Builder(YeniPaletOlusturma.this);
        yes_noBuilder.setTitle("Uyarı");
        yes_noBuilder.setMessage(body);
        if(action.equals("dataControl")){
            yes_noBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(YeniPaletOlusturma.this, "tamam seçildi", Toast.LENGTH_SHORT);
                    return;
                }
            });
        }
        else if(action.equals("Devam")){
            yes_noBuilder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getBaseContext(), "Evet seçildi", Toast.LENGTH_SHORT);
                    paletOlustur();
//                     Intent itent = new Intent(getBaseContext(), YeniPaletOlusturmaDevam.class);
//                     startActivity(itent);
                }
            });
            yes_noBuilder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getBaseContext(), "Hayır seçildi devam yok", Toast.LENGTH_SHORT);
                    return;
                }
            });
//            yes_noBuilder.show();
        }
        else if(action.equals("olusturDevam")){
            yes_noBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getBaseContext(), "Evet seçildi", Toast.LENGTH_SHORT);

                    Intent itent = new Intent(getBaseContext(), YeniPaletOlusturmaDevam.class);
                    itent.putExtra("from", "PaletOlusturma");
                    itent.putExtra("cariKod", musteriID);
                    itent.putExtra("lrf", paletLrf);
                    itent.putExtra("adet", String.valueOf(adet));
                    itent.putExtra("paletTipi", etiketTipi);
                    itent.putExtra("urunKodu", urunKodu);
                    itent.putExtra("urunTani", ypUrunTani.getText().toString());
                    itent.putExtra("eskiKodu", eskiKod);
                    startActivity(itent);
                }
            });
        }
        yes_noBuilder.show();
    }

    public void paletOlustur(){
        int PaletRef_ = 0; int KullaniciRef_ = user.getKullaniciRef();
        String cariKod_ = musteriID;
        String cariAdi_ = musteriAdi;
            if(musteriAdi.equals("Müşteri Seçin"))
                musteriAdi = "";

        Boolean BoyMu_ = false;
        if(kisa_boy.equals("B")){ BoyMu_= true; }
        if(kisa_boy.equals("K")){ BoyMu_= false; }

        String PaletTipi_ = etiketTipi, SecilmisUrunKodu_ = eskiKod;
        int takimAdet_ = 0;
        Boolean takimDetayli_ = false;
        int Adet_ = adet;
        Boolean hollandaDepoMu_ = false;
        String YeniKod_ = urunKodu, IfsHandlingUnitType_ = paletTuru, BarkodOnEk_ = "KY";
        int BarkodUzunluk_ = 10;
        String kullaniciLocation_ =  user.getLocationNo();

        BackgroudTask task = new BackgroudTask(this);
        task.execute("PALET_OLUSTUR",
            String.valueOf(PaletRef_),
            String.valueOf(KullaniciRef_),
            cariKod_,  cariAdi_, String.valueOf(BoyMu_) ,
            PaletTipi_,  SecilmisUrunKodu_,  String.valueOf(takimAdet_),
            String.valueOf(takimDetayli_), String.valueOf(Adet_), String.valueOf(hollandaDepoMu_) ,
            YeniKod_, IfsHandlingUnitType_,  BarkodOnEk_,  String.valueOf(BarkodUzunluk_),
            kullaniciLocation_);
    }

    // To activate Menubar oprions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.palet_islem_menu, menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    // On Menu Bar item options click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.paletBitir:
                Toast.makeText(this, "Malet bit islemi", Toast.LENGTH_SHORT).show();
            case R.id.item2:
                Toast.makeText(this, "Uygulamadan cikis", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // Etiketler getirme
    void loadEtiket(){
//        String[] etiketler = new String[]{"Turkuaz", "Logosuz", "Majestik", "Açık Ürün", "Ekonomik", "Karışık"};
        String[] etiketler = new String[]{"Palet tipi seç", "Tek tipi", "Karışık"};
        final List<String> etiketList = new ArrayList<>(Arrays.asList(etiketler));
        final ArrayAdapter<String> spinnerArrayAdapter =  new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, etiketList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_value);
        ypEtiket.setAdapter(spinnerArrayAdapter);
    }

    // Musterileri getirme
    void loadCustomer(ArrayList<CustomerModel> customerArrayList){
        customDropDownArrayAdapter = new CustomDropDownArrayAdapter(this, R.layout.yp_customers, customerArrayList);
        CustomDropDownArrayAdapter.ACTION = "FIRMALAR";
        ypFirmaAdi.setAdapter(customDropDownArrayAdapter);
    }

    void loadProductData(ArrayList<ProductModel> yeniProdArrayList){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.dialog_row_item, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);
        yeniKodListview = (ListView) customView.findViewById(R.id.listView);

        if(!yeniProdArrayList.isEmpty()){
            Toast.makeText(getBaseContext(),  yeniProdArrayList.size() + " urun bulundu ",Toast.LENGTH_SHORT).show();
            paletAdapter = new PaletAdapter(this, R.layout.dialog_list_item);
            if(yeniProdArrayList.size() == 1){
                for(int i = 0; i<yeniProdArrayList.size(); i++){
                    ypUrunKodu.setText(yeniProdArrayList.get(i).getYeniKodu());
                    ypUrunTani.setText(yeniProdArrayList.get(i).getYeniTanim());
                    eskiKod = yeniProdArrayList.get(i).getEskiKod();
                    // NOT ACTUALLY NEEDED
                    PaletAdapter.OPERATION = "YENI_KODLAR";
                    paletAdapter.addYeniKod(
                            new ProductModel(yeniProdArrayList.get(i).getYeniKodu(),
                                    yeniProdArrayList.get(i).getYeniTanim(),
                                    yeniProdArrayList.get(i).getEskiKod()));

                    yeniKodListview.setAdapter(paletAdapter);
//                    dialog =  builder.create();
//                    dialog.show();
                }
            }
            else {
                for(int i = 0; i<yeniProdArrayList.size(); i++){
//                    ypUrunKodu.setText(yeniProdArrayList.get(i).getYeniKodu());
//                    ypUrunTani.setText(yeniProdArrayList.get(i).getYeniTanim());
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

//                    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View row = inflater.inflate(R.layout.dialog_row_item, null, false);

//                    yeniKodListview = (ListView) row.findViewById(R.id.listView);
                    PaletAdapter.OPERATION = "YENI_KODLAR";
                    paletAdapter.addYeniKod(new ProductModel(
                                yeniProdArrayList.get(i).getYeniKodu(),
                                yeniProdArrayList.get(i).getYeniTanim(),
                                yeniProdArrayList.get(i).getEskiKod()
                            )
                    );

                    yeniKodListview.setAdapter(paletAdapter);
//                    builder.setView(row);
                }
            }
            dialog =  builder.create();
            dialog.setTitle("Uygun Ürün Seçin");
            dialog.show();
            yeniKodListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getBaseContext(),  " Alert list item clicked ", Toast.LENGTH_SHORT).show();
                    ProductModel productModel = (ProductModel) paletAdapter.getItem(position);
                    ypUrunKodu.setText(productModel.getYeniKodu());
                    ypUrunTani.setText(productModel.getYeniTanim());
                    eskiKod = productModel.getEskiKod();
                    dialog.dismiss();
                }
            });
        }
        else{
            Toast.makeText(getBaseContext(), " Urin bulanamadi",Toast.LENGTH_SHORT).show();
        }
    }

    // FOR TIMING Solution link : https://www.py4u.net/discuss/625126
    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                urunBarKodu = ypUrunBarKodu.getText().toString();
//                if(!musteriID.equals("")){
//                    Toast.makeText(getBaseContext(), musteriID + " / " +musteriAdi + "/ " + urunBarKodu ,Toast.LENGTH_SHORT).show()
                    getProductData(musteriID, urunBarKodu);
//                }
            }
        }
    };

    public void getProductData(String musteriCari, String urunBarKodu){
        BackgroudTask task = new BackgroudTask(this);
        BACKGROUND_ACTION = "getProductData";
        task.execute(BACKGROUND_ACTION, musteriCari, urunBarKodu);
    }

    public void loadHandlingUnitTypes(String categoryID, String kisa_boy, String urunKodu){
        BACKGROUND_ACTION = "Get_Handling_Unit_Types";
        BackgroudTask task = new BackgroudTask(this);
        task.execute(BACKGROUND_ACTION, categoryID, kisa_boy,urunKodu);
    }

    public  void loadPaletlerTipi(ArrayList<HandlingUnitModel> paletlerTipiArrayList){
//        pTcustomDropDownArrayAdapter = new CustomDropDownArrayAdapter(this, R.layout.yp_palet_tipi_list, paletlerTipiArrayList, "HANDLING_UNITS");
//        CustomDropDownArrayAdapter.ACTION = "HANDLING_UNITS";
        paletTipiSpinnerAdapter = new PaletTipiSpinnerAdapter(this, R.layout.yp_palet_tipi_list, paletlerTipiArrayList);
        ypPaletTipi.setAdapter(paletTipiSpinnerAdapter);
    }

    class BackgroudTask extends AsyncTask <String, Void, String> {
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
            if (ACTION.equals("loadCustomer")) {
                result = service.getCustomers();
            }
            else if (ACTION.equals("getProductData")) {
                 musteriCari = params[1];
                 urunBarKodu = params[2];
                Log.d("RETURNED_PROD", musteriCari +" --- " +urunBarKodu);
                result = service.getPROD_DATA(musteriCari, urunBarKodu);
            }
            else if (ACTION.equals("Get_Handling_Unit_Types")) {
                String category_id_ = params[1], kisa_boy_ = params[2], part_no_ = params[3];
                Log.d("PROPERTIES_:", category_id_ +" -- " +kisa_boy_ +" -- " +part_no_);
                result = service.getHandlingUnitType(category_id_, kisa_boy_, part_no_);
                Log.d("HUnit RETURNED_PROD", result);
            }
            else if(ACTION.equals("PALET_OLUSTUR")){
                int PaletRef_ = Integer.valueOf(params[1]);
                int KullaniciRef_ = Integer.valueOf(params[2]);
                String cariKod_ = params[3], cariAdi_ = params[4];
                Boolean BoyMu_ = Boolean.valueOf(params[5]);
                String PaletTipi_ = params[6], SecilmisUrunKodu_ = params[7];
                int takimAdet_ = Integer.valueOf(params[8]);
                Boolean takimDetayli_ = Boolean.valueOf(params[9]);
                int Adet_ = Integer.valueOf(params[10]);
                Boolean hollandaDepoMu_ = Boolean.valueOf(params[11]);
                String YeniKod_ = params[12];
                String IfsHandlingUnitType_ = params[13]; String BarkodOnEk_ = params[14];
                int BarkodUzunluk_ = Integer.valueOf(params[15]); String kullaniciLocation = params[16];
//
                Log.d("P_OLUSTUR_PROP:", PaletRef_ +" -- " +KullaniciRef_ +" -- " +cariKod_
                        +" -- " +cariAdi_ +" -- " +BoyMu_ +" -- " +PaletTipi_ +" -- " +SecilmisUrunKodu_
                        +" -- " +takimAdet_ +" -- " +takimDetayli_
                        +" -- " +Adet_ +" -- " +hollandaDepoMu_ +" -- " +YeniKod_ +" -- " +IfsHandlingUnitType_
                        +" -- " +BarkodOnEk_ +" -- " +BarkodUzunluk_ +" -- " +kullaniciLocation );
                result = service.paletOlustur(
                        PaletRef_, KullaniciRef_, cariKod_, cariAdi_, BoyMu_, PaletTipi_,
                        SecilmisUrunKodu_, takimAdet_, takimDetayli_, Adet_, hollandaDepoMu_,
                        YeniKod_, IfsHandlingUnitType_, BarkodOnEk_, BarkodUzunluk_, kullaniciLocation);
                Log.d("P OLUSTUR RESULT", result);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result2) {
            if (ACTION.equals("loadCustomer")) {
                ArrayList<CustomerModel> customerArrayList = customersXMLParse(result);
                loadCustomer(customerArrayList);
            } else if (ACTION.equals("getProductData")) {
                ArrayList<ProductModel> productArrayList = yeniProdXMLParse(result);
                loadProductData(productArrayList);
            }
            else if (ACTION.equals("Get_Handling_Unit_Types")) {
                ArrayList<HandlingUnitModel> paletlerTipiArrayList = paletlertipiXMLParse(result);
                loadPaletlerTipi(paletlerTipiArrayList);
            }
            else if(ACTION.equals("PALET_OLUSTUR")){
//                displayDialog(result, "dataControl");
                 paletLrf = parsePaletXmlResult(result);
                 Log.d("PALET_OLUSTUR_BACK", paletLrf);
                 if(!paletLrf.equals(null) || !paletLrf.equals("") ){
                     displayDialog(paletLrf + " nolu LRF olan bir paleti başarıyla oluşturulmuştur."
                             +" \"Tamam\" tıklayarak palete ürünleri eklemek için sonraki sayfaya yönlendireceksiniz.", "olusturDevam");
                 }
                 else {
                     displayDialog("İşlem başrısız. Palet oluşturulamadı. Lütfen bigileri kontrol ederek tekrar deneyin.", "dataControl");
                 }
            }
        }

    public ArrayList<CustomerModel> customersXMLParse(String result_){
        ArrayList<CustomerModel> customerArrayList = new ArrayList<>();
        try {
            XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
            int event = xmlPullParser.getEventType();
            Boolean musteriKoduBool = false, musteriAdiBool = false, musteriIDBool = false, adresBool = false, ilceBool=false, sehirBool = false,  ulkeBool = false;
            String musteriKodu= "", musteriAdi = "",musteriID = "", adres = "",  ilce = "", sehir = "", ulke = "";
            int count = 0;

            CustomerModel customerModel0 = new CustomerModel("", "Müşteri Seçin");
            customerArrayList.add(customerModel0);

            while (event != xmlPullParser.END_DOCUMENT)
            {
                if ( event == xmlPullParser.START_TAG){
                    if(xmlPullParser.getName().equals("CUSTOMER_ID"))
                        musteriIDBool = true;
                    if(xmlPullParser.getName().equals("NAME"))
                        musteriAdiBool = true;
                }

                if(event == xmlPullParser.TEXT){ // If
                    if(musteriIDBool){
                        musteriID = xmlPullParser.getText();
                        musteriIDBool = false;
                    }
                }

                if(event == xmlPullParser.TEXT){ //
                    if(musteriAdiBool){
                        musteriAdi = xmlPullParser.getText();
                        musteriAdiBool=false;
                    }
                }

                if (!musteriID.equals("") && !musteriAdi.equals("")){
                    count++;
                    CustomerModel customerModel = new CustomerModel(musteriID, musteriAdi);
                    customerArrayList.add(customerModel);
//                    Log.d("CUST MODEL DATA::", customerModel.toString());
                    musteriID = ""; musteriAdi ="";
                }

                if(event == xmlPullParser.END_TAG){
                    if(xmlPullParser.getName().equals("CUSTOMER_ID"))
                        musteriIDBool =false;
                    if(xmlPullParser.getName().equals("NAME"))
                        musteriAdiBool =false;
                }

                event = xmlPullParser.next();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return customerArrayList;
    }

    // Parse Product
    public ArrayList<ProductModel> yeniProdXMLParse(String result_){
        ArrayList<ProductModel> productArrayList = new ArrayList<>();

        try {
            XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
            int event = xmlPullParser.getEventType();
            Boolean yeniKodBool = false, yeniTanimBool = false, eskiKodBool = false;
            String yeniKod= "", yeniTanim = "", eskiKod = "";
            int count = 0;

            while (event != xmlPullParser.END_DOCUMENT)
            {
                if ( event == xmlPullParser.START_TAG){
                    if(xmlPullParser.getName().equals("YeniKod"))
                        yeniKodBool = true;
                    if(xmlPullParser.getName().equals("YeniTanim"))
                        yeniTanimBool = true;
                    if(xmlPullParser.getName().equals("EskiKod"))
                        eskiKodBool = true;
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

                if(event == xmlPullParser.TEXT){ //
                    if(eskiKodBool){
                        eskiKod = xmlPullParser.getText();
                        eskiKodBool=false;
                    }
                }

                if (!yeniKod.equals("") && !yeniTanim.equals("") && !eskiKod.equals("")){
                    count++;
                    ProductModel productModel = new ProductModel(yeniKod, yeniTanim, eskiKod);
                    Log.d("Prod MODEL DATA::", productModel.toString());
                    productArrayList.add(productModel);
                    yeniKod = ""; yeniTanim =""; eskiKod = "";
                }

                if(event == xmlPullParser.END_TAG){
                    if(xmlPullParser.getName().equals("YeniKod"))
                        yeniKodBool =false;
                    if(xmlPullParser.getName().equals("YeniTanim"))
                        yeniTanimBool =false;
                    if(xmlPullParser.getName().equals("EskiKod"))
                        yeniKodBool =false;
                }
                event = xmlPullParser.next();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return productArrayList;
    }

    // PALETLER TIPI
    public ArrayList<HandlingUnitModel> paletlertipiXMLParse(String result_){
        ArrayList<HandlingUnitModel> handlingUnArrayList = new ArrayList<>();
        try {
            XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
            int event = xmlPullParser.getEventType();
            Boolean qtyBool = false, unitTypeIDBool = false, tanimBool=false;
            String qty= "", unitTypeID = "", tanim = ""; int count = 0;

            while (event != xmlPullParser.END_DOCUMENT)
            {
                if ( event == xmlPullParser.START_TAG){
                    if(xmlPullParser.getName().equals("QTY"))
                        qtyBool = true;
                    if(xmlPullParser.getName().equals("HANDLING_UNIT_TYPE_ID"))
                        unitTypeIDBool = true;
                    if(xmlPullParser.getName().equals("TANIM"))
                        tanimBool = true;
                }

                if(event == xmlPullParser.TEXT){ //
                    if(qtyBool){
                        qty = xmlPullParser.getText();
                        qtyBool = false;
                    }
                }

                if(event == xmlPullParser.TEXT){ //
                    if(unitTypeIDBool){
                        unitTypeID = xmlPullParser.getText();
                        unitTypeIDBool=false;
                    }
                }
                if(event == xmlPullParser.TEXT){ //
                    if(tanimBool){
                        tanim = xmlPullParser.getText();
                        tanimBool=false;
                    }
                }

                if (!qty.equals("") && !unitTypeID.equals("") && !tanim.equals("")){
                    count++;
                    HandlingUnitModel handlingUnitModel = new HandlingUnitModel( qty, unitTypeID, tanim, "", "", "");
//                    Log.d("Prod MODEL DATA::", handlingUnitModel.toString());
                    handlingUnArrayList.add(handlingUnitModel);
                    qty = ""; unitTypeID ="" ; tanim = "";
                }

                if(event == xmlPullParser.END_TAG){
                    if(xmlPullParser.getName().equals("QTY")) qtyBool =false;
                    if(xmlPullParser.getName().equals("HANDLING_UNIT_TYPE_ID")) unitTypeIDBool =false;
                    if(xmlPullParser.getName().equals("TANIM")) tanimBool =false;
                }
                event = xmlPullParser.next();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return handlingUnArrayList;
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