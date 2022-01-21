package com.example.trkz_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;

public class Giris extends AppCompatActivity {

//  Arayüzü ELEMANERI BAĞLAMA
    EditText kullaniciID;
    Button girisButon;
    Context context;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris_activity);

        kullaniciID = findViewById(R.id.kullaniciID);
        girisButon = findViewById(R.id.girisButon);

        preferences =  getSharedPreferences("UserPreferences", MODE_PRIVATE);
        editor = preferences.edit();

        dialogBuilder =  new AlertDialog.Builder(this);

        if(preferences.contains("kallaniciAdi")){
            Intent anaMenu = new Intent(Giris.this,MainActivity.class);
            anaMenu.putExtra("ID", kullaniciID.getText().toString());
            startActivity(anaMenu);
        }
        else {
            girisButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String kullaniciAdi = kullaniciID.getText().toString();
//                    editor.putString("kullaniciID", kullanici_Id);
//                    editor.commit();
//                    Intent anaMenu = new Intent(Giris.this,MainActivity.class);
//                    anaMenu.putExtra("ID", kullaniciID.getText().toString());
//                    startActivity(anaMenu);
                    BackgroudTask task = new BackgroudTask(getBaseContext());
//                    BACKGROUND_ACTION = "getProductData";
                    task.execute("GIRIS", kullaniciAdi);
                }
            });
        }
    }

//    public void setUserData(){
//        UserModel user
//    }

    public void operationDialog(String opTitle, String message, Boolean noBtn, String noBtnText, Boolean yesBtn, String yesBtnText){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customDialogView = inflater.inflate(R.layout.dialog_layout, null, false);
        TextView operationTitle = customDialogView.findViewById(R.id.operationTitle);
        TextView messageContent = customDialogView.findViewById(R.id.dialogContent);

        messageContent.setText(message);
        operationTitle.setText(opTitle);
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

    public void setUserData(ArrayList<UserModel> userList){
        // Toast.makeText(getBaseContext(),   " User Data " + userList.size(),Toast.LENGTH_SHORT).show();
        int kullaniciRef = 0, depoNo = 0, sevkDepoNo = 0, bolumNo = 0;
        String barkodOnEk = "",  takimBarkodOnek = "";
        int barkodUzunluk  = 0, takimEtiketID = 0;
        String takimEtiketPrinter = "";
        int paletEtiketID = 0, paletDetayliEtiketID = 0;
        String paletEtiketPrinter = "", yeniUretimBarkodOnEk = "";
        int cerabathEtiketID = 0;
        String locationNo = "", contract = "";
        if(!userList.isEmpty()){
            Toast.makeText(getBaseContext(),   " Giris Basarili User Data Size" + userList.size(),Toast.LENGTH_SHORT).show();
            String all_data = "";
//            if(userList.size() == 1){
                for(int i = 0; i<userList.size(); i++){
                    kullaniciRef = userList.get(i).getKullaniciRef();
                    depoNo = userList.get(i).getDepoNo();
                    sevkDepoNo = userList.get(i).getSevkDepoNo();
                    bolumNo = userList.get(i).getBolumNo();
                    barkodOnEk = userList.get(i).getBarkodOnEk();
                    takimBarkodOnek = userList.get(i).getTakimBarkodOnek();
                    barkodUzunluk = userList.get(i).getBarkodUzunluk();
                    takimEtiketID = userList.get(i).getTakimEtiketID();
                    takimEtiketPrinter = userList.get(i).getTakimEtiketPrinter();
                    paletEtiketID = userList.get(i).getPaletEtiketID();
                    paletDetayliEtiketID = userList.get(i).getPaletDetayliEtiketID();
                    paletEtiketPrinter = userList.get(i).getPaletEtiketPrinter();
                    yeniUretimBarkodOnEk = userList.get(i).getYeniUretimBarkodOnEk();
                    cerabathEtiketID = userList.get(i).getCerabathEtiketID();
                    locationNo = userList.get(i).getLocationNo();
                    contract = userList.get(i).getContract();
                }

                editor.putInt("kullaniciRef", kullaniciRef);
                editor.putInt("depoNo", depoNo);
                editor.putInt("sevkDepoNo", sevkDepoNo);
                editor.putInt("bolumNo", bolumNo);
                editor.putString("barkodOnEk", barkodOnEk);
                editor.putString("takimBarkodOnek", takimBarkodOnek);
                editor.putInt("barkodUzunluk", barkodUzunluk);
                editor.putInt("takimEtiketID", takimEtiketID);
                editor.putString("takimEtiketPrinter", takimEtiketPrinter);
                editor.putInt("paletEtiketID", paletEtiketID);
                editor.putInt("paletDetayliEtiketID", paletDetayliEtiketID);
                editor.putString("paletEtiketPrinter", paletEtiketPrinter);
                editor.putString("yeniUretimBarkodOnEk", yeniUretimBarkodOnEk);
                editor.putInt("cerabathEtiketID", cerabathEtiketID);
                editor.putString("locationNo", locationNo);
                editor.putString("contract", contract);

                editor.commit();
                Intent anaMenu = new Intent(Giris.this,MainActivity.class);
                anaMenu.putExtra("ID", kullaniciID.getText().toString());
                startActivity(anaMenu);
                all_data = all_data +
                        "* " +kullaniciRef + " * " +depoNo
                        + "* " +  sevkDepoNo + " * " +  bolumNo
                        + " * " +  barkodOnEk;
                Toast.makeText(getBaseContext(), all_data,Toast.LENGTH_SHORT).show();
//            }
        }
        else {
            Toast.makeText(getBaseContext(),   "Giris Basarisiz, Bilgileri kontrol ederek deneyiniz",Toast.LENGTH_SHORT).show();
            operationDialog("Giris Mesajı", "Giris Basarisiz, Bilgileri kontrol ederek deneyiniz", false, "", true, "Tamam");
        }
    }

    class BackgroudTask extends AsyncTask<String, Void, String> {
        String result;
        Context ctex;
        String ACTION = "", kullaniciAdi = "";

        BackgroudTask(Context ctex) {
            this.ctex = ctex;
        }

        @Override
        protected String doInBackground(String... params) {
            ACTION = params[0]; kullaniciAdi = params[1];

            MobileService service = new MobileService();
            Log.d("ACTION:", ACTION);
            if (ACTION.equals("GIRIS")) {
                result = service.getUser(kullaniciAdi);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result2) {
            if (ACTION.equals("GIRIS")) {
                Log.d("GIRISR_ESULT :", result);
                ArrayList<UserModel> userList = KullaniciXMLParse(result);
                setUserData(userList);
            }
        }

        // PALET OLUSTUR RESULT
        public ArrayList<UserModel>  KullaniciXMLParse(String result_){
            ArrayList<UserModel> userList = new ArrayList<>();
            try {
                XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
                int event = xmlPullParser.getEventType();
                // Boolean
                Boolean kullaniciRefBool = false, depoNoBool = false, sevkDepoNoBool = false, bolumNoBool = false,
                 barkodOnEkBool = false, takimBarkodOnekBool = false, barkodUzunlukBool = false,
                 takimEtiketIDBool = false, takimEtiketPrinterBool = false, paletEtiketIDBool = false,
                 paletDetayliEtiketIDBool = false, paletEtiketPrinterBool = false, yeniUretimBarkodOnEkBool = false,
                 cerabathEtiketIDBool = false, locationNoBool = false, contractBool = false;
                // Data
                int kullaniciRef = 0, depoNo = 0, sevkDepoNo = 0, bolumNo = 0;
                String barkodOnEk = "",  takimBarkodOnek = "";
                int barkodUzunluk  = 0, takimEtiketID = 0;
                String takimEtiketPrinter = "";
                int paletEtiketID = 0, paletDetayliEtiketID = 0;
                String paletEtiketPrinter = "", yeniUretimBarkodOnEk = "";
                int cerabathEtiketID = 0;
                String locationNo = "", contract = "";
//                private int kullaniciRef ; //183</KullaniciRef>
//                private int depoNo; // >1</DepoNo>
//                private int sevkDepoNo; // >2</SevkDepoNo>
//                private int bolumNo; //>42</BolumNo>
//                private String barkodOnEk; //>KY</BarkodOnEk>
//                private String takimBarkodOnek ; //>TY</TakimBarkodOnek>
//                private int barkodUzunluk; // >10</BarkodUzunluk>
//                private int takimEtiketID ; //>81</TakimEtiketID>
//                private String takimEtiketPrinter; //>Takim1</TakimEtiketPrinter>
//                private int paletEtiketID; //>77</PaletEtiketID>
//                private int paletDetayliEtiketID; // >83</PaletDetayliEtiketID>
//                private String paletEtiketPrinter; //>Palet1</PaletEtiketPrinter>
//                private String yeniUretimBarkodOnEk ;// >CK</YeniUretimBarkodOnEk>
//                private int cerabathEtiketID; //>97</CerabathEtiketID>
//                private String locationNo; //>M020</LocationNo>
//                private String contract; //>MRK</Contract>

//                kullaniciRefBool = false, depoNoBool = false, sevkDepoNoBool = false, bolumNoBool = false,
//                        barkodOnEkBool = false, takimBarkodOnekBool = false, barkodUzunlukBool = false,
//                        takimEtiketIDBool = false, takimEtiketPrinterBool = false, paletEtiketIDBool = false,
//                        paletDetayliEtiketIDBool = false, paletEtiketPrinterBool = false, yeniUretimBarkodOnEkBool = false,
//                        cerabathEtiketIDBool = false, locationNoBool = false, contractBool = false;
                while (event != xmlPullParser.END_DOCUMENT)
                {
                    if ( event == xmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equals("KullaniciRef")) kullaniciRefBool = true;
                        if(xmlPullParser.getName().equals("DepoNo")) depoNoBool = true;
                        if(xmlPullParser.getName().equals("SevkDepoNo")) sevkDepoNoBool = true;
                        if(xmlPullParser.getName().equals("BolumNo")) bolumNoBool = true;
                        if(xmlPullParser.getName().equals("BarkodOnEk")) barkodOnEkBool = true;
                        if(xmlPullParser.getName().equals("TakimBarkodOnek")) takimBarkodOnekBool = true;
                        if(xmlPullParser.getName().equals("BarkodUzunluk")) barkodUzunlukBool = true;
                        if(xmlPullParser.getName().equals("TakimEtiketID")) takimEtiketIDBool = true;
                        if(xmlPullParser.getName().equals("TakimEtiketPrinter")) takimEtiketPrinterBool = true;
                        if(xmlPullParser.getName().equals("PaletEtiketID")) paletEtiketIDBool = true;
                        if(xmlPullParser.getName().equals("PaletDetayliEtiketID")) paletDetayliEtiketIDBool = true;
                        if(xmlPullParser.getName().equals("PaletEtiketPrinter")) paletEtiketPrinterBool = true;
                        if(xmlPullParser.getName().equals("YeniUretimBarkodOnEk")) yeniUretimBarkodOnEkBool = true;
                        if(xmlPullParser.getName().equals("CerabathEtiketID")) cerabathEtiketIDBool = true;
                        if(xmlPullParser.getName().equals("LocationNo")) locationNoBool = true;
                        if(xmlPullParser.getName().equals("Contract")) contractBool = true;
                    }
                    // int kullaniciRef = 0, depoNo = 0, sevkDepoNo = 0, bolumNo = 0;
                    if(event == xmlPullParser.TEXT){ //
                        if(kullaniciRefBool){
                            kullaniciRef = Integer.valueOf(xmlPullParser.getText()); kullaniciRefBool = false;
                        }
                    }
                    if(event == xmlPullParser.TEXT){ //
                        if(depoNoBool){
                            depoNo = Integer.valueOf(xmlPullParser.getText()); depoNoBool = false;
                        }
                    }
                    if(event == xmlPullParser.TEXT){ //
                        if(sevkDepoNoBool){
                            sevkDepoNo = Integer.valueOf(xmlPullParser.getText()); sevkDepoNoBool = false;
                        }
                    }
                    if(event == xmlPullParser.TEXT){ //
                        if(bolumNoBool){
                            bolumNo = Integer.valueOf(xmlPullParser.getText()); bolumNoBool = false;
                        }
                    }

                    // String barkodOnEk = "",  takimBarkodOnek = "";
                    if(event == xmlPullParser.TEXT){ //
                        if(barkodOnEkBool){
                            barkodOnEk = xmlPullParser.getText(); barkodOnEkBool = false;
                        }
                    }
                    if(event == xmlPullParser.TEXT){ //
                        if(takimBarkodOnekBool){
                            takimBarkodOnek = xmlPullParser.getText(); takimBarkodOnekBool = false;
                        }
                    }

                    // int barkodUzunluk  = 0, takimEtiketID = 0;
                    if(event == xmlPullParser.TEXT){ //
                        if(barkodUzunlukBool){
                            barkodUzunluk=Integer.valueOf(xmlPullParser.getText());  barkodUzunlukBool = false;
                        }
                    }
                    if(event == xmlPullParser.TEXT){ //
                        if(takimEtiketIDBool){
                            takimEtiketID=Integer.valueOf(xmlPullParser.getText());  takimEtiketIDBool= false;
                        }
                    }

                    // String takimEtiketPrinter = "";
                    if(event == xmlPullParser.TEXT){ //
                        if(takimEtiketPrinterBool){
                            takimEtiketPrinter = xmlPullParser.getText(); takimEtiketPrinterBool = false;
                        }
                    }
                    // int paletEtiketID = 0, paletDetayliEtiketID = 0;
                    if(event == xmlPullParser.TEXT){ //
                        if(paletEtiketIDBool){
                            paletEtiketID = Integer.valueOf(xmlPullParser.getText());  paletEtiketIDBool = false;
                        }
                    }
                    if(event == xmlPullParser.TEXT){ //
                        if(paletDetayliEtiketIDBool){
                            paletDetayliEtiketID = Integer.valueOf(xmlPullParser.getText());  paletDetayliEtiketIDBool = false;
                        }
                    }

                    // String paletEtiketPrinter = "", yeniUretimBarkodOnEk = "";
                    if(event == xmlPullParser.TEXT){ //
                        if(paletEtiketPrinterBool){
                            paletEtiketPrinter = xmlPullParser.getText(); paletEtiketPrinterBool = false;
                        }
                    }
                    if(event == xmlPullParser.TEXT){ //
                        if(yeniUretimBarkodOnEkBool){
                            yeniUretimBarkodOnEk = xmlPullParser.getText(); yeniUretimBarkodOnEkBool = false;
                        }
                    }

                    // int cerabathEtiketID = 0
                    if(event == xmlPullParser.TEXT){ //
                        if(cerabathEtiketIDBool){
                            cerabathEtiketID = Integer.valueOf(xmlPullParser.getText());  cerabathEtiketIDBool = false;
                        }
                    }

                    // String locationNo = "", contract = "";
                    if(event == xmlPullParser.TEXT){ //
                        if(locationNoBool){
                            locationNo = xmlPullParser.getText(); locationNoBool = false;
                        }
                    }
                    if(event == xmlPullParser.TEXT){ //
                        if(contractBool){
                            contract = xmlPullParser.getText(); contractBool = false;
                        }
                    }

                    if (kullaniciRef != 0 && depoNo != 0 && sevkDepoNo != 0 && bolumNo != 0){
                        UserModel user = new UserModel( kullaniciRef, depoNo, sevkDepoNo , bolumNo,
                                barkodOnEk, takimBarkodOnek, barkodUzunluk, takimEtiketID,
                                takimEtiketPrinter, paletEtiketID, paletDetayliEtiketID,
                                paletEtiketPrinter, yeniUretimBarkodOnEk, cerabathEtiketID,
                                locationNo, contract
                        );
                        Log.d("User MODEL DATA::", user.toString());
                    userList.add(user);
//                    kullaniciRef = 0; depoNo = 0; sevkDepoNo = 0; bolumNo = 0; barkodOnEk = "";
//                    takimBarkodOnek = ""; barkodUzunluk  = 0; takimEtiketID = 0; takimEtiketPrinter = "";
//                    paletEtiketID = 0; paletDetayliEtiketID = 0; paletEtiketPrinter = ""; yeniUretimBarkodOnEk = "";
//                    cerabathEtiketID = 0; locationNo = ""; contract = "";
                    }

                    if ( event == xmlPullParser.END_TAG){
                        if(xmlPullParser.getName().equals("KullaniciRef")) kullaniciRefBool = false;
                        if(xmlPullParser.getName().equals("DepoNo")) depoNoBool = false;
                        if(xmlPullParser.getName().equals("SevkDepoNo")) sevkDepoNoBool = false;
                        if(xmlPullParser.getName().equals("BolumNo")) bolumNoBool = false;
                        if(xmlPullParser.getName().equals("BarkodOnEk")) barkodOnEkBool = false;
                        if(xmlPullParser.getName().equals("TakimBarkodOnek")) takimBarkodOnekBool = false;
                        if(xmlPullParser.getName().equals("BarkodUzunluk")) barkodUzunlukBool = false;
                        if(xmlPullParser.getName().equals("TakimEtiketID")) takimEtiketIDBool = false;
                        if(xmlPullParser.getName().equals("TakimEtiketPrinter")) takimEtiketPrinterBool = false;
                        if(xmlPullParser.getName().equals("PaletEtiketID")) paletEtiketIDBool = false;
                        if(xmlPullParser.getName().equals("PaletDetayliEtiketID")) paletDetayliEtiketIDBool = false;
                        if(xmlPullParser.getName().equals("PaletEtiketPrinter")) paletEtiketPrinterBool = false;
                        if(xmlPullParser.getName().equals("YeniUretimBarkodOnEk")) yeniUretimBarkodOnEkBool = false;
                        if(xmlPullParser.getName().equals("CerabathEtiketID")) cerabathEtiketIDBool = false;
                        if(xmlPullParser.getName().equals("LocationNo")) locationNoBool = false;
                        if(xmlPullParser.getName().equals("Contract")) contractBool = false;
                    }

                    //
                    event = xmlPullParser.next();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
            return userList;
        }
    }

}