package com.example.trkz_mobile;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;

//import class Shipment

import com.example.trkz_mobile.databinding.SiparisFragmentBinding;

public class ShipmentTask extends AsyncTask {
        String result;

        Context ctex;
    private SiparisFragmentBinding binding;

    ShipmentTask(Context ctex){
                this.ctex = ctex;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            Log.d("DoInBackGrounCAll", "......... DoInBackGroun Calllled ........");
            MobileService service = new MobileService();
            result = service.GetShipments("MRK");
            Log.d("DoInBackGrounRESULT", result);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ArrayList<Siparisler> list = ModelBasedXMLParse(result);
            Log.d("DisplayArrayLis", list.toString());

            ArrayList<Siparisler> siparislerArrayList = ModelBasedXMLParse(result);
            ListAdapter listAdapter = new ListAdapter(ctex, siparislerArrayList) ;

//            binding.listView.setAdapter(listAdapter);
//            binding.listView.setClickable(true);

        }
    public ArrayList<Siparisler> ModelBasedXMLParse(String result_){
        ArrayList<Siparisler> siparislerArrayList = new ArrayList<>();
        try {
            XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
            int event = xmlPullParser.getEventType();
            Boolean ship_id = false , ship_rec_name = false, ship_rec_addr = false;
            String ship_idValue = "", ship_rec_nameValue= "", ship_rec_addrValue = "";
            // SHIPMENT_ID RECEIVER_ID  RECIVER_NAME RECEIVER_ADDR_ID
            int total = 0;
            while (event != xmlPullParser.END_DOCUMENT)
            {
                if ( event == xmlPullParser.START_TAG){

                    if(xmlPullParser.getName().equals("SHIPMENT_ID")){
                        ship_id =true;
                    }
                    if(event == xmlPullParser.TEXT){
                        if(ship_id){
                            ship_idValue = xmlPullParser.getText();
                            ship_id=false;
                        }
                    }

                    if(xmlPullParser.getName().equals("RECIVER_NAME")){
                        ship_rec_name =true;
                    }
                    if(event == xmlPullParser.TEXT){
                        if(ship_rec_name){
                            ship_rec_nameValue = xmlPullParser.getText();
                            ship_rec_name=false;
                        }
                    }

                    if(xmlPullParser.getName().equals("RECEIVER_ADDR_ID")){
                        ship_rec_addr =true;
                    }
                    if(event == xmlPullParser.TEXT){
                        if(ship_rec_addr){
                            ship_rec_addrValue = xmlPullParser.getText();
                            ship_rec_addr=false;
                        }
                    }

                    Siparisler siparis = new Siparisler(ship_idValue, ship_rec_nameValue, ship_rec_addrValue, R.drawable.orders);
                    siparislerArrayList.add(siparis);
                }
                if(event == xmlPullParser.END_TAG){
                    if(xmlPullParser.getName().equals("SHIPMENT_ID")){
                        ship_id=false;
                    }
                    if(xmlPullParser.getName().equals("RECIVER_NAME")){
                        ship_rec_name =false;
                    }
                    if(xmlPullParser.getName().equals("RECEIVER_ADDR_ID")){
                        ship_rec_addr =false;
                    }
                }
                event = xmlPullParser.next();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return siparislerArrayList;

    }
}
