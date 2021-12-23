package com.example.trkz_mobile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trkz_mobile.databinding.SevkiyatFragmentBinding;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;

public class SevkiyatFragment extends Fragment {

    // BINDING
    public SevkiyatFragmentBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SevkiyatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SevkiyatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SevkiyatFragment newInstance(String param1, String param2) {
        SevkiyatFragment fragment = new SevkiyatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentr
        binding = SevkiyatFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.d("TRYING", "MOBILE SERVICE >>>>>>");
        ShipmentTask task = new ShipmentTask(getContext());
        task.execute();
        return root;
    }

    public class ShipmentTask extends AsyncTask {
        String result;
        Context ctex;

        ShipmentTask(Context ctex){
            this.ctex = ctex;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            MobileService service = new MobileService();
            result = service.GetShipments("MRK");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ArrayList<Siparisler> list = ModelBasedXMLParse(result);
            Log.d("DisplayArrayLis", list.toString());

            ArrayList<Siparisler> siparislerArrayList = ModelBasedXMLParse(result);
            ListAdapter listAdapter = new ListAdapter(ctex, siparislerArrayList) ;
            binding.sevkiyatListView.setAdapter(listAdapter);
            binding.sevkiyatListView.setClickable(true);
        }
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

            while (event != xmlPullParser.END_DOCUMENT)
            {
                if ( event == xmlPullParser.START_TAG){

                    if(xmlPullParser.getName().equals("SHIPMENT_ID")){
                        ship_id =true;
                    }

                    if(xmlPullParser.getName().equals("RECIVER_NAME")){
                        ship_rec_name =true;
                    }

                    if(xmlPullParser.getName().equals("RECEIVER_ADDR_ID")){
                        ship_rec_addr =true;
                    }
                }
                if(event == xmlPullParser.TEXT){
                    if(ship_id){
                        ship_idValue = xmlPullParser.getText();
                        ship_id=false;
                    }
                }
                if(event == xmlPullParser.TEXT){
                    if(ship_rec_name){
                        ship_rec_nameValue = xmlPullParser.getText();
                        ship_rec_name=false;
                    }
                }
                if(event == xmlPullParser.TEXT){
                    if(ship_rec_addr){
                        ship_rec_addrValue = xmlPullParser.getText();
                        ship_rec_addr=false;
                    }
                }
                if (ship_idValue != "" && ship_rec_nameValue != "" && ship_rec_addrValue != ""){
                    Siparisler siparis = new Siparisler(ship_idValue, ship_rec_nameValue, ship_rec_addrValue, R.drawable.orders);
                    siparislerArrayList.add(siparis);
                    ship_idValue = "" ; ship_rec_nameValue = "" ; ship_rec_addrValue = "";
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