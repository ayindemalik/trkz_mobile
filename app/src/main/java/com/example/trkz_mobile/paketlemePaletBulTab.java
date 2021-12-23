package com.example.trkz_mobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trkz_mobile.databinding.PaketlemeFragmentBinding;
import com.example.trkz_mobile.databinding.SevkiyatFragmentBinding;


public class paketlemePaletBulTab extends Fragment {



    public paketlemePaletBulTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.paketleme_palet_bul_tab_fragment, container, false);
    }
}