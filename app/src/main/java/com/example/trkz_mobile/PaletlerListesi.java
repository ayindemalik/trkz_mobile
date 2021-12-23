package com.example.trkz_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trkz_mobile.databinding.PaletItemsListBinding;
import com.example.trkz_mobile.databinding.PaletlerListesiBinding;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;

public class PaletlerListesi extends AppCompatActivity {

    public PaletlerListesiBinding binding;
    PaletAdapter paletAdapter;
    ListView paletlerListview;
    EditText paletAra;
    String searchText;
    // Data Variables
    String ifsID = "", tarih ="", kapalimi ="", barkod ="",  cariUnvan="",  lref="";

    long delay = 2000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.paletler_listesi);
        binding = PaletlerListesiBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        paletlerListview =  binding.paletlerListView;
        paletAra = binding.paletAra;


        paletAdapter = new PaletAdapter(this, R.layout.paletler_list_item);
        paletlerListview.setAdapter(paletAdapter);

        // On itemClick Listview
        paletlerListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ifsID = ((TextView) view.findViewById(R.id.ifsID)).getText().toString();
                tarih = ((TextView) view.findViewById(R.id.tarih)).getText().toString();
                kapalimi =((TextView) view.findViewById(R.id.kapalimi)).getText().toString();
                barkod =((TextView) view.findViewById(R.id.barkod)).getText().toString();
                cariUnvan=((TextView) view.findViewById(R.id.cariUnvani)).getText().toString();
                lref=((TextView) view.findViewById(R.id.lref)).getText().toString();
                Toast.makeText(getBaseContext(), "Veriler \n" +ifsID + " / "
                        + tarih + " / "
                        + kapalimi + " / "
                        + barkod + " / "
                        + cariUnvan + " / "
                        + lref,Toast.LENGTH_SHORT).show();
                Intent itent = new Intent(getBaseContext(), YeniPaletOlusturmaDevam.class);
                itent.putExtra("lrf", lref);
                itent.putExtra("adet", "0");
                itent.putExtra("paletTipi", "paletipi");
                itent.putExtra("urunKodu", "000000");
                startActivity(itent);
            }
        });

        paletAra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                handler.removeCallbacks(input_finish_checker);
                paletAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable e) {
                //avoid triggering event when text is empty
//                if (e.length() > 0) {
//                    last_text_edit = System.currentTimeMillis();
//                    handler.postDelayed(input_finish_checker, delay);
//                } else {
//
//                }
            }
        });

        PaletlerTask task = new PaletlerTask(this);
        task.execute();
    }
    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                searchText = paletAra.getText().toString();
//                if(!musteriID.equals("")){
//                    Toast.makeText(getBaseContext(), musteriID + " / " +musteriAdi + "/ " + urunBarKodu ,Toast.LENGTH_SHORT).show()

//                }
            }
        }
    };

    public class PaletlerTask extends AsyncTask {
        String result, result2;
        Context ctex;
        public String paletResult;

        PaletlerTask(Context ctex){
            this.ctex = ctex;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            MobileService service = new MobileService();
//            result = service.GetPaletler("1");
            result2 = service.xmlGetPaletler("1");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("OnPostTRYING_BKG", ".....OnPostTRYING_BKG....");
//            Log.d("RESTURNED_RESULT --> ", result2.toString());
//             loadPaletler(result);
            ArrayList<PaletModel> list = paletModelXMLParse(result2);
//            Log.d("DisplayPlatterArrayList", list.toString());

            ArrayList<PaletModel> palterArrayList = paletModelXMLParse(result2);
//            ListAdapter listAdapter = new ListAdapter(ctex, siparislerArrayList) ;
//            binding.sevkiyatListView.setAdapter(listAdapter);
//            binding.sevkiyatListView.setClickable(true);
//            for( int i= 0; i < palterArrayList.size(); i++){
//                PaletAdapter.OPERATION = "PALETLER";
//                paletAdapter.add(palterArrayList.get(i));
//            }

        }


    }

    public ArrayList<PaletModel> paletModelXMLParse(String result_){
        ArrayList<PaletModel> paletlerArrayList = new ArrayList<>();
        try {
            XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
            int event = xmlPullParser.getEventType();
            Boolean ifsIDBool = false, tarihBool = false, kapalimiBool = false, barkodBool = false,  cariUnvanBool = false,  lrefBool = false;
            String ifsID = "", tarih ="", kapalimi ="", barkod ="",  cariUnvan="",  lref="";
            int count = 0;
            while (event != xmlPullParser.END_DOCUMENT)
            {
                if ( event == xmlPullParser.START_TAG){
                    if(xmlPullParser.getName().equals("IfsId"))
                        ifsIDBool =true;
                    if(xmlPullParser.getName().equals("Tarih"))
                        tarihBool =true;
                    if(xmlPullParser.getName().equals("KapaliMi"))
                        kapalimiBool =true;
                    if(xmlPullParser.getName().equals("Barkod"))
                        barkodBool =true;
                    if(xmlPullParser.getName().equals("cari_unvan1"))
                        cariUnvanBool =true;
                    if(xmlPullParser.getName().equals("LRef"))
                        lrefBool =true;
                }
                if(event == xmlPullParser.TEXT){ // IfsId
                    if(ifsIDBool){
                        ifsID = xmlPullParser.getText();
                        ifsIDBool=false;
                    }
                }
                if(event == xmlPullParser.TEXT){ // Tarih
                    if(tarihBool){
                        tarih = xmlPullParser.getText();
                        tarihBool=false;
                    }
                }
                if(event == xmlPullParser.TEXT){ // KapaliMi
                    if(kapalimiBool){
                        kapalimi = xmlPullParser.getText();
                        kapalimiBool=false;
                    }
                }
                if(event == xmlPullParser.TEXT){ // Barkod
                    if(barkodBool){
                        barkod = xmlPullParser.getText();
                        barkodBool=false;
                    }
                }
                if(event == xmlPullParser.TEXT){ // cari_unvan1
                    if(cariUnvanBool){
                        cariUnvan = xmlPullParser.getText();
                        cariUnvanBool=false;
                    }
                }
                if(event == xmlPullParser.TEXT){ // LRef
                    if(lrefBool){
                        lref = xmlPullParser.getText();
                        lrefBool=false;
                    }
                }
                if (ifsID != "" && barkod != "" && lref != ""){
                    count++;
                    PaletModel paletModel = new PaletModel(ifsID, tarih, kapalimi, barkod, cariUnvan, lref);
                    paletlerArrayList.add(paletModel);
                    Log.d("MODEL DATA::", paletModel.toString());
                    PaletAdapter.OPERATION = "PALETLER";
                    paletAdapter.add(paletModel);
                    ifsID = ""; tarih =""; kapalimi =""; barkod ="";  cariUnvan="";  lref="";
                }
                if(event == xmlPullParser.END_TAG){
                    if(xmlPullParser.getName().equals("IfsId"))
                        ifsIDBool =false;
                    if(xmlPullParser.getName().equals("Tarih"))
                        tarihBool =false;
                    if(xmlPullParser.getName().equals("KapaliMi"))
                        kapalimiBool =false;
                    if(xmlPullParser.getName().equals("Barkod"))
                        barkodBool =false;
                    if(xmlPullParser.getName().equals("cari_unvan1"))
                        cariUnvanBool =false;
                    if(xmlPullParser.getName().equals("LRef"))
                        lrefBool =false;
                }
                event = xmlPullParser.next();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return paletlerArrayList;
    }
}