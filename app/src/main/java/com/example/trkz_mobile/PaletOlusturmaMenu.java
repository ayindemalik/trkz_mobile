package com.example.trkz_mobile;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.trkz_mobile.databinding.PaletOlusturmaMenuBinding;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class PaletOlusturmaMenu extends AppCompatActivity {
    public PaletOlusturmaMenuBinding binding;
    Button pOlYeniPaletOlustur;
    Button pOlPaletBul;
    Button pOlPaletAc;
    Button pOlEtiketYaz;
    Button pOlPaletBoz;
    EditText dialogBarkodInput;

    SharedPreferences pref;
    UserModel user;

    AlertDialog barkDialog, yesnoDialog;
    AlertDialog.Builder barkDialogBuilder, yesnoDialogBuilder;

    String paletBarkod, action, inputText, paletRef;
    Boolean barkodYazdir = false;
    private TextWatcher text = null;
    long delay = 2000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.palet_olusturma_menu);
        binding = PaletOlusturmaMenuBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        barkDialogBuilder = new AlertDialog.Builder(this);
        yesnoDialogBuilder = new AlertDialog.Builder(this);

        pOlYeniPaletOlustur =  binding.pOlYeniPaletOlustur;
        pOlPaletBul =  binding.pOlPaletBul;
        pOlPaletAc =  binding.pOlPaletAc;
        pOlEtiketYaz =  binding.pOlEtiketYaz;
        pOlPaletBoz =  binding.pOlPaletBoz;

        pref = getSharedPreferences("UserPreferences", 0);
        setUserData(pref);

        // setIntents(intent);
        pOlYeniPaletOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), YeniPaletOlusturma.class);
                startActivity(i);
//                Toast.makeText(getBaseContext(), "Yeni palet oluşturma işlemine gidilecek",Toast.LENGTH_SHORT).show();
            }
        });

        pOlPaletBul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationDialog("Palet Bul");
                action = "PALET_BUL";
            }
        });

        pOlPaletAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationDialog("Palet Aç");
//                paletAc
                action = "PALET_AC";
            }
        });

        pOlEtiketYaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationDialog("Palet Etiketi Yaz");
            }
        });

        pOlPaletBoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationDialog("Palet Boz");
                action = "PALET_BOZ_ONCE_BUL";
            }
        });

    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                inputText = dialogBarkodInput.getText().toString();
                callProcess(inputText);
            }
        }
    };

    public void callProcess(String inputText){
        if(inputText != "" && inputText.length() >= 10){
            this.inputText = inputText;
            if(action.equals("PALET_BUL")){
                Log.d(action, "Barkod: " + inputText);
                BackgroudTask task = new BackgroudTask(this);
                task.execute("PALET_BUL", inputText);
            }
            else if(action.equals("PALET_BOZ_ONCE_BUL")){
                Log.d(action, "Barkod: " + inputText);
//                dialog.dismiss();
                BackgroudTask task = new BackgroudTask(this);
                task.execute("PALET_BOZ_ONCE_BUL", inputText);
            }
            else if(action.equals("PALET_AC")){
                Log.d(action, "Barkod: " + inputText);
                BackgroudTask task = new BackgroudTask(this);
                task.execute(action, inputText);
            }
        }
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

    public void operationDialog(String opTitle){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customDialogView = inflater.inflate(R.layout.paketleme_operation_dialog, null, false);
        TextView operationTitle = customDialogView.findViewById(R.id.operationTitle);
        operationTitle.setText(opTitle);
//        EditText barkodInput = customDialogView.findViewById(R.id.barkodInput);
        Button iptal = customDialogView.findViewById(R.id.operationButton);
        iptal.setText("İptal");
        dialogBarkodInput = customDialogView.findViewById(R.id.barkodInput);
        barkDialogBuilder.setView(customDialogView);
        barkDialog =  barkDialogBuilder.create();
        barkDialog.setCanceledOnTouchOutside(false);
        barkDialog.show();
        dialogBarkodInput.addTextChangedListener(new TextWatcher() {
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
        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barkDialog.dismiss();
            }
        });
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
        if(action.equals("PALET_BOZ_ONCE_BUL") ){
            noBtn.setText("Hayır/İptal");
            yesBtn.setText("Evet");
        }
        else if(action.equals("PALET_BOZ")){
            noBtn.setText("Hayır");
            yesBtn.setText("Evet");
        }
        else if(action.equals("PALET_BOZ_BITTI") || action.equals("PALET_BOZ_ONCE_BUL_YOK")
                || action.equals("PALET_AC") ){
            noBtn.setVisibility(View.GONE);
            yesBtn.setText("Tamam");
        }

        yesnoDialogBuilder.setView(yesnoDialogView);
        yesnoDialog =  yesnoDialogBuilder.create();
        yesnoDialog.show();

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ALERT_BUTTON", action);
                if(action.equals("PALET_BOZ_ONCE_BUL")){
                    yesnoDialog.dismiss();
                }
                else if(action.equals("PALET_BOZ")){
                    barkodYazdir = false;
                    paletBoz(inputText);
//                    yesnoDialog.dismiss();
                }
                Log.d("ALERT_BUTTON", action);
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(action.equals("PALET_BOZ_ONCE_BUL")){
                    alertDialog("Palet Bozma/Etiket yazdırma ! ",
                            inputText
                                    + " Barkodlu paleti bozdurulacaktir. Barkodları yazdırılsın mı?");
                    action = "PALET_BOZ";
                }
                else if(action.equals("PALET_BOZ")){
                    barkodYazdir = true;
                    paletBoz(inputText);
//                    yesnoDialog.dismiss();
                }
                if(action.equals("PALET_BOZ_BITTI") || action.equals("PALET_BOZ_ONCE_BUL_YOK")
                        || action.equals("PALET_AC")){
                    yesnoDialog.dismiss();
                    // RECALL barkod okutmak icin dialog yeniden Çağır
                    finish();
                    startActivity(getIntent());
                }
                Log.d("ALERT_BUTTON", action);

            }
        });
    }

    public void paletBoz(String paletBarkod){
        BackgroudTask task = new BackgroudTask(this);
        Log.d("PaletBoz Called:->", paletBarkod);
        task.execute("PALET_BOZ", paletBarkod);
    }
//    public void paletAc(String paletBarkod){
//        BackgroudTask task = new BackgroudTask(this);
//        Log.d("PaletAc Called:->", paletBarkod);
//        task.execute("PALET_AC", paletBarkod);
//    }

    public void yonlendirmeKarar(String paletRef){
        if((!paletRef.equals("") || !paletRef.equals(null)) && Integer.valueOf(paletRef) != 0){
            if(action.equals("PALET_BUL")){
                Toast.makeText(this,
                        " Palet Bulundu, Ürün Ekleme sayfasına yönlendiriliyorsunuz",Toast.LENGTH_SHORT);
                Intent itent = new Intent(getBaseContext(), YeniPaletOlusturmaDevam.class);
                itent.putExtra("from", "PaletIslemleri");
                itent.putExtra("lrf", paletRef);
                startActivity(itent);
            }
        }
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
            MobileService service = new MobileService();
            Log.d("ACTION:", ACTION);
            if (ACTION.equals("PALET_BUL")) {
                Log.d(ACTION, "bkd: "+ params[1] + " cont: "+user.getContract() + "loc: "+user.getLocationNo());
                result = service.paletBul(params[1], user.getContract(), user.getLocationNo());
            }

            else if (ACTION.equals("PALET_BOZ_ONCE_BUL")) {
//                barkDialog.dismiss();
                Log.d(ACTION, "bkd: "+ params[1] + " cont: "+user.getContract() + "loc: "+user.getLocationNo());
                result = service.paletBul(params[1], user.getContract(), user.getLocationNo());
                Log.d(ACTION, "result: " + result);
            }

            else if (ACTION.equals("PALET_BOZ")) {
                Log.d(ACTION, "bkd: "+ params[1]
                        + " k_Id: "+user.getKullaniciRef() + " cont: " + user.getContract()
                        + " loc: "+user.getLocationNo() + " Ydir: "+barkodYazdir
                        +" tkm: "+user.getTakimEtiketID() + user.getTakimEtiketPrinter()) ;
                result = service.paletBoz(params[1], user.getKullaniciRef(), user.getDepoNo(),
                         user.getContract(), user.getLocationNo(), barkodYazdir, user.getTakimEtiketID(),
                        user.getTakimEtiketPrinter());
                Log.d(ACTION, "result: " + result);
            }

            else if (ACTION.equals("PALET_AC")) {
                Log.d(ACTION, "bkd: "+ params[1] + " cont: "+user.getContract() + " loc: "+ user.getLocationNo()
                        + "depoNo : "+ user.getDepoNo());
                result = service.paletAc(params[1], user.getContract(), user.getLocationNo(), user.getDepoNo());
                Log.d(ACTION, "result: " + result);
            }
            return null;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String result2) {
            if (ACTION.equals("PALET_BUL")) {
                Log.d("RESULT -> :", result);
                paletRef = paletBulResponseXMLParse(result);
                yonlendirmeKarar(paletRef);
            }
            else if (ACTION.equals("PALET_BOZ_ONCE_BUL")) {
                paletRef = paletBulResponseXMLParse(result);
                if(Integer.valueOf(paletRef)!= 0){
                    barkDialog.dismiss();
                    alertDialog("Palet Bozma Uyarısı", inputText
                            + " Barkodlu paleti bozmak istediğinizden emin misiniz ?");
                }
                else{
                    action = "PALET_BOZ_ONCE_BUL_YOK";
                    alertDialog("Palet Bozma İşlemi", inputText
                            + " Barkodlu paleti bulunamadı, Başka barkodu okuttun");
                }
            }
            if (ACTION.equals("PALET_BOZ")) {
                yesnoDialog.dismiss();
                action = "PALET_BOZ_BITTI";
                String response = paletBozResponseXMLParse(result);
                Log.d(ACTION, response);
                alertDialog("Palet Bozma işlemi", response);
            }
            else if (ACTION.equals("PALET_AC")) {
                String response = paletAcResponseXMLParse(result);
                Log.d(ACTION, response);
                if(!response.equals("")){
                    alertDialog("Palet Bozma işlemi", response);
                }

            }
        }



        public String paletBulResponseXMLParse(String result_){
            String paletRef = "";
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                Boolean stringBool = false;

                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("PaletBulResult"))
                            stringBool = true;
                    }
                    if(event == xmlPullParser.TEXT)
                        if(stringBool){ paletRef = xmlPullParser.getText(); stringBool = false; }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("string")) stringBool = false;
                        if(xmlPullParser.getName().equals("PaletBulResult")) stringBool = false;
                    }

                    event = xmlPullParser.next();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return paletRef;
        }

        public String paletBozResponseXMLParse(String result_){
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
                        if(xmlPullParser.getName().equals("PaletBozResponse"))
                            stringBool = true;
                    }
                    if(event == xmlPullParser.TEXT)
                        if(stringBool){ response = xmlPullParser.getText(); stringBool = false; }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("string")) stringBool = false;
                        if(xmlPullParser.getName().equals("PaletBozResponse")) stringBool = false;
                    }
                    event = xmlPullParser.next();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return response;
        }

        public String paletAcResponseXMLParse(String result_){
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
                        if(xmlPullParser.getName().equals("PaletAcResponse")) stringBool = true;
                    }
                    if(event == xmlPullParser.TEXT)
                        if(stringBool){ response = xmlPullParser.getText(); stringBool = false; }

                    if(event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("PaletAcResponse")) stringBool = false;
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