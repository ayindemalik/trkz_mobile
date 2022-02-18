package com.example.trkz_mobile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.trkz_mobile.databinding.PaketlemePaletlerTabFragmentBinding;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;

public class paketlemePaletlerTab extends Fragment {

    public PaketlemePaletlerTabFragmentBinding binding;
    PaletAdapter paletAdapter;
    ListView paletlerListview;
    JSONObject JO;
    JSONArray jsonArray;

    public paketlemePaletlerTab() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static paketlemePaletlerTab newInstance(String param1, String param2) {
        paketlemePaletlerTab fragment = new paketlemePaletlerTab();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = PaketlemePaletlerTabFragmentBinding.inflate(inflater, container, false);
        paletlerListview =  binding.paletlerListView;
        View root = binding.getRoot();

        paletAdapter = new PaletAdapter(getContext(), R.layout.paletler_list_item);
        paletlerListview.setAdapter(paletAdapter);

        Log.d("TRYING", "MOBILE SERVICE >>>>>>");
        PaletlerTask task = new PaletlerTask(getContext());
        task.execute();

        return root;
    }

//    public void loadPaletler(String json_string){
//        try {
////            JO = new JSONObject(json_string);
////            jsonArray = new JSONArray(JO);
//            jsonArray = new JSONArray(json_string);
//            Log.d("JSON_ArrayTotal->",  ""+jsonArray.length());
//
//            int count = 0;
//            String ifsID, tarih, kapalimi, barkod,  cariUnvan,  lref;
//            while(count < jsonArray.length()){
////                 JO = jsonArray.getJSONObject(count);
////                ifsID = JO.getString("IfsId");
////                tarih = JO.getString("Tarih");
////                kapalimi = JO.getString("KapaliMi");
////                barkod = JO.getString("Barkod");
////                cariUnvan = JO.getString("CariUnvan");
//                ifsID = jsonArray.getJSONObject(count).getString("IfsId");
//                tarih = jsonArray.getJSONObject(count).getString("Tarih");
//                kapalimi = jsonArray.getJSONObject(count).getString("KapaliMi");
//                barkod = jsonArray.getJSONObject(count).getString("Barkod");
//                cariUnvan = jsonArray.getJSONObject(count).getString("CariUnvan");
//                lref = jsonArray.getJSONObject(count).getString("LRef");
//
//                Log.d("JSON DATA",  "Palet ["+count+"] -> ifsID : "+ ifsID
//                        +" tarih: " + tarih
//                        +" kapalimi : "+ kapalimi
//                        + " barkod:" + barkod
//                        + " cariUnvan: "+ cariUnvan
//                        + " lref: "+ lref
//                        );
//                PaletModel paletModel = new PaletModel(ifsID, tarih, kapalimi, barkod, cariUnvan, lref);
//                Log.d("MODEL DATA::", paletModel.toString());
//                PaletAdapter.OPERATION = "PALETLER";
//                paletAdapter.add(paletModel);
//                count++;
//            }
////                "IfsId":"89040",
////                "Tarih":"27.11.2021 09:31:06",
////                "KapaliMi":"E",
////                "Barkod":"KY000244851",
////                "CariUnvan":"ZAHRAT AL KARMEL FOR TRADING",
////                "LRef":"244851"
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    // Class
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
            int ifsID = 0, lref = 0 ;
            String tarih ="", barkod ="",  cariUnvan="";
            String kapalimi = "";
            int count = 0;
            while (event != xmlPullParser.END_DOCUMENT)
            {
                if ( event == xmlPullParser.START_TAG){
                    if(xmlPullParser.getName().equals("IfsId"))  ifsIDBool =true;
                    if(xmlPullParser.getName().equals("Tarih")) tarihBool =true;
                    if(xmlPullParser.getName().equals("KapaliMi")) kapalimiBool =true;
                    if(xmlPullParser.getName().equals("Barkod")) barkodBool =true;
                    if(xmlPullParser.getName().equals("cari_unvan1")) cariUnvanBool =true;
                    if(xmlPullParser.getName().equals("LRef")) lrefBool =true;
                }
                if(event == xmlPullParser.TEXT){ // IfsId
                    if(ifsIDBool){
                        ifsID = Integer.valueOf(xmlPullParser.getText());
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
                        lref = Integer.valueOf(xmlPullParser.getText());
                        lrefBool=false;
                    }
                }
                if (ifsID != 0 && barkod != "" && lref != 0){
                    count++;
                    PaletModel paletModel = new PaletModel(ifsID, tarih, kapalimi, barkod, cariUnvan, lref);
                    paletlerArrayList.add(paletModel);
                    Log.d("MODEL DATA::", paletModel.toString());
                    PaletAdapter.OPERATION = "PALETLER";
                    paletAdapter.add(paletModel);
                    ifsID = 0; tarih =""; kapalimi = ""; barkod ="";  cariUnvan="";  lref=0;
                }
                if(event == xmlPullParser.END_TAG){
                    if(xmlPullParser.getName().equals("IfsId")) ifsIDBool =false;
                    if(xmlPullParser.getName().equals("Tarih")) tarihBool =false;
                    if(xmlPullParser.getName().equals("KapaliMi")) kapalimiBool =false;
                    if(xmlPullParser.getName().equals("Barkod")) barkodBool =false;
                    if(xmlPullParser.getName().equals("cari_unvan1")) cariUnvanBool =false;
                    if(xmlPullParser.getName().equals("LRef")) lrefBool =false;
                }
                event = xmlPullParser.next();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return paletlerArrayList;

    }
}
