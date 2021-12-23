package com.example.trkz_mobile;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class ListAdapter extends ArrayAdapter<Siparisler> {

    public ListAdapter(@NonNull Context context, ArrayList<Siparisler> siparislerArrayList) {
        super(context, R.layout.siparis_list_item, siparislerArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Siparisler siparis = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.siparis_list_item,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.customPic);
        TextView siparisNo = convertView.findViewById(R.id.siparisNo);
        TextView musteriAdi = convertView.findViewById(R.id.musteriAdi);
        TextView siparisTarihi = convertView.findViewById(R.id.siparisTarihi);

        imageView.setImageResource(siparis.imageId);
        siparisNo.setText(siparis.siparisNo);
        musteriAdi.setText(siparis.musteriAdi);
        siparisTarihi.setText(siparis.siparisTarihi);

        return convertView;
    }

}
