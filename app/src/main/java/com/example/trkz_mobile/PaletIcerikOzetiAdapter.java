package com.example.trkz_mobile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class PaletIcerikOzetiAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public PaletIcerikOzetiAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void addPaletIcerikOzeti( @NonNull PaletIcerikModel object){
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
        View row_element;
        row_element = view;
        View returnedView;
        Log.d("PALET_ICERIK_OZET", "ADAPTER GET VIEW CALLED... Pos: "+position);
        returnedView = paletIcerikOzeti( position, row_element,  parent );
        return returnedView;
    }

    // PALET ICERIK OZET
    public View  paletIcerikOzeti(int position, View sentView, ViewGroup parent ) {
        PaletIcerikOzetHolder paletIcerikOzetHolder;
        if (sentView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sentView = layoutInflater.inflate(R.layout.palet_icerik_ozet_row, parent, false);
            paletIcerikOzetHolder = new PaletIcerikOzetHolder();
            paletIcerikOzetHolder.uru_Stok_Kodu = sentView.findViewById(R.id.uru_Stok_Kodu);
            paletIcerikOzetHolder.ifs_Stok_Kodu = sentView.findViewById(R.id.ifs_stok_kodu);
            paletIcerikOzetHolder.ifs_Stok_Tanimi = sentView.findViewById(R.id.ifs_stok_tani);
            paletIcerikOzetHolder._adet = sentView.findViewById(R.id._adet);
            sentView.setTag(paletIcerikOzetHolder);
        }else{
            paletIcerikOzetHolder = (PaletIcerikOzetHolder) sentView.getTag();
        }
        // Call the helper class with the correspondent constructor
        PaletIcerikModel paletIcerikModel = (PaletIcerikModel) this.getItem(position);
        paletIcerikOzetHolder.uru_Stok_Kodu.setText(paletIcerikModel.getUru_Stok_Kodu());
        paletIcerikOzetHolder.ifs_Stok_Kodu.setText(paletIcerikModel.getIfs_Stok_Kodu());
        paletIcerikOzetHolder.ifs_Stok_Tanimi.setText(paletIcerikModel.getIfs_Stok_Tanimi());
        paletIcerikOzetHolder._adet.setText(String.valueOf(paletIcerikModel.getAdet()));
        return  sentView;
    }

    // STATIC CLASS FOR VIEWS

    static class  PaletIcerikOzetHolder {
        TextView  uru_Stok_Kodu, ifs_Stok_Kodu, ifs_Stok_Tanimi,  _adet;
    }
}

