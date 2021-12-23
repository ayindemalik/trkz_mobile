package com.example.trkz_mobile;

import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trkz_mobile.databinding.SiparisFragmentBinding;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;

import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SiparisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SiparisFragment extends Fragment {

//    SiparisFragmentBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // BINDING
    public SiparisFragmentBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SiparisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SiparisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SiparisFragment newInstance(String param1, String param2) {
        SiparisFragment fragment = new SiparisFragment();
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

        binding = SiparisFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inflate the layout for this fragment & Initialize the data
        int[] imageId = {R.drawable.orders,R.drawable.orders,R.drawable.orders,R.drawable.orders,R.drawable.orders,
                R.drawable.orders,R.drawable.orders,R.drawable.orders,R.drawable.orders, R.drawable.orders};
        String[] siparisNo = {"TRKZ0001","TRKZ0002","TRKZ0003","TRKZ0004","TRKZ0005",
                "TRKZ0006","TRKZ0007","TRKZ0008","TRKZ0009", "TRKZ0010"};
        String[] musteriAdi = {"Toboroma Fas","Nabizi Avro","Target Filistin", "Hashisa Tunus","Natati Ukr",
                "Arabes ","MTN Bus", "Asadaka Lib", "Akakce Tr", "Seramik San"};
        String[] siparisTarihi = {"01-01-2021","01-02-2021", "03-01-2021", "04-02-2021", "05-04-2021",
                "04-01-2021","01-05-2021","01-07-2021", "06-06-2021", "08-07-2021"};

//        ArrayList<Siparisler> siparislerArrayList = new ArrayList<>();
//
//        for(int i = 0; i< imageId.length;i++){
//            Siparisler siparis = new Siparisler(siparisNo[i], musteriAdi[i], siparisTarihi[i], imageId[i]);
//            siparislerArrayList.add(siparis);
//        }
//
//        ListAdapter listAdapter = new ListAdapter(getContext(), siparislerArrayList) ;
//
//        binding.listView.setAdapter(listAdapter);
//        binding.listView.setClickable(true);
//        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent i = new Intent(SiparisFragment.this, UserActivity.class);
//                i.putExtra("name",name[position]);
//                i.putExtra("phone",phoneNo[position]);
//                i.putExtra("country",country[position]);
//                i.putExtra("imageid",imageId[position]);
//                startActivity(i);
//
//            }
//        });

        // USING MOBILE SERVICE AND SHIPMENT TAST CLASS

        return root;
    }


    // Other Methods



}