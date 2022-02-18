package com.example.trkz_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PaletIcerikDetayAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public PaletIcerikDetayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void addPaletIcerikDetay( @NonNull PaletIcerikModel object){
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position){
        return list.get(position);
    }

    @Nullable
    @Override    // ALL METHOD CALLING
    public View getView(int position, @Nullable View view, @Nullable ViewGroup parent){
        View row_element = view;
        View returnedView;
        // Mehodos from here

        returnedView = paletIcerikleri( position, row_element,  parent );
        return returnedView;
    }

    public View  paletIcerikleri(int position, View sentView, ViewGroup parent ) {
        PaletIcerikDetayHolder paletIcerikDetayHolder;
        if (sentView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sentView = layoutInflater.inflate(R.layout.palet_icerik_detay, parent, false);
            paletIcerikDetayHolder = new PaletIcerikDetayHolder();
            paletIcerikDetayHolder.paletlerRef = sentView.findViewById(R.id.paletRef);
            paletIcerikDetayHolder.uretimBarkodu = sentView.findViewById(R.id.uretimBarkodu);
            paletIcerikDetayHolder.uru_Stok_Kodu = sentView.findViewById(R.id.uru_stok_kodu);
            paletIcerikDetayHolder.ifs_Stok_Kodu = sentView.findViewById(R.id.ifs_stok_kodu);
            paletIcerikDetayHolder.ifs_Stok_Tanimi = sentView.findViewById(R.id.ifs_stok_tani);
            paletIcerikDetayHolder._adet = sentView.findViewById(R.id._adet);
            paletIcerikDetayHolder.tarih = sentView.findViewById(R.id.tarih);
            sentView.setTag(paletIcerikDetayHolder);
        }else{
            paletIcerikDetayHolder = (PaletIcerikDetayHolder) sentView.getTag();
        }
        // Call the helper class with the correspondent constructor
        PaletIcerikModel paletIcerikModel = (PaletIcerikModel) this.getItem(position);
        paletIcerikDetayHolder.paletlerRef.setText(paletIcerikModel.getPaletlerRef());
        paletIcerikDetayHolder.uretimBarkodu.setText(paletIcerikModel.getUretimBarkodu());
        paletIcerikDetayHolder.uru_Stok_Kodu.setText(paletIcerikModel.getUru_Stok_Kodu());
        paletIcerikDetayHolder.ifs_Stok_Kodu.setText(paletIcerikModel.getIfs_Stok_Kodu());
        paletIcerikDetayHolder.ifs_Stok_Tanimi.setText(paletIcerikModel.getIfs_Stok_Tanimi());
        paletIcerikDetayHolder._adet.setText(String.valueOf(paletIcerikModel.getAdet()));
        paletIcerikDetayHolder.tarih.setText(paletIcerikModel.getTarih());
        return  sentView;
    }


    static class  PaletIcerikDetayHolder {
        TextView lRef, paletlerRef, uretimBarkodu, urunTipi, ifs_Stok_Kodu, ifs_Stok_Tanimi, _adet, transferredToIfs,  uru_Stok_Kodu, urunAdi, tarih, kullanicilarRef;
    }

}
