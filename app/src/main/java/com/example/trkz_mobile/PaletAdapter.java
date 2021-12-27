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

public class PaletAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public static String OPERATION = "PALETLER";

    public PaletAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void add( @NonNull PaletModel object){
        super.add(object);
        list.add(object);
    }
    public void addPaletitem( @NonNull PaletItemsModel object){
        super.add(object);
        list.add(object);
    }
    public void addYeniKod( @NonNull ProductModel object){
        super.add(object);
        list.add(object);
    }

    public void addPaletIcerikDetay( @NonNull PaletIcerikModel object){
        super.add(object);
        list.add(object);
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
        // Mehodos from here
        if (OPERATION.equals("PALETLER")){
            Log.d("ADAPTER_CALLED", "ADAPTER GET VIEW CALLED... Pos: "+position);
            returnedView = AllPalets( position, row_element,  parent );
            return returnedView;
        }

        else if(OPERATION.equals("YENI_KODLAR")){
            returnedView = allYeniKodItems( position, row_element,  parent );
            return returnedView;
        }

        else if(OPERATION.equals("PALET_ICERIK_DETAY")){
            Log.d("PALET_ICERIK_DETAY", "ADAPTER GET VIEW CALLED... Pos: "+position);
            returnedView = paletIcerikleri( position, row_element,  parent );
            return returnedView;
        }
        else
            return row_element ;
    }

    // PALETLER LISTESI
    public View  AllPalets(int position, View sentView, ViewGroup parent ) {
        PaltlerHolder paltlerHolder;
        if (sentView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sentView = layoutInflater.inflate(R.layout.paletler_list_item, parent, false);
            paltlerHolder = new PaltlerHolder();
            paltlerHolder.ifsID = sentView.findViewById(R.id.ifsID);
            paltlerHolder.tarih = sentView.findViewById(R.id.tarih);
            paltlerHolder.kapalimi = sentView.findViewById(R.id.kapalimi);
            paltlerHolder.barkod = sentView.findViewById(R.id.barkod);
            paltlerHolder.cariUnvan = sentView.findViewById(R.id.cariUnvani);
            paltlerHolder.lref = sentView.findViewById(R.id.lref);
            sentView.setTag(paltlerHolder);
        }else{
            paltlerHolder = (PaltlerHolder) sentView.getTag();
        }
        // Call the helper class with the correspondent constructor
        PaletModel paletModel = (PaletModel) this.getItem(position);
        paltlerHolder.ifsID.setText(paletModel.getIfsID());
        paltlerHolder.tarih.setText(paletModel.getTarih());
        paltlerHolder.kapalimi.setText(paletModel.getKapalimi());
        paltlerHolder.barkod.setText(paletModel.getBarkod());
        paltlerHolder.cariUnvan.setText(paletModel.getCariUnvan());
        paltlerHolder.lref.setText(paletModel.getLref());
        return  sentView;
    }
    // PALET ITEMS
    public View  allYeniKodItems(int position, View sentView, ViewGroup parent ) {
        YeniKodItemsHolder kodItemsHolder;
        if (sentView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sentView = layoutInflater.inflate(R.layout.dialog_list_item, parent, false);
            kodItemsHolder = new YeniKodItemsHolder();
            kodItemsHolder.yeniKod = sentView.findViewById(R.id.yeniKod);
            kodItemsHolder.yeniTani = sentView.findViewById(R.id.yeniTani);
            sentView.setTag(kodItemsHolder);
        }else{
            kodItemsHolder = (YeniKodItemsHolder) sentView.getTag();
        }
        // Call the helper class with the correspondent constructor
        ProductModel productModel = (ProductModel) this.getItem(position);
        kodItemsHolder.yeniKod.setText(productModel.getYeniKodu());
        kodItemsHolder.yeniTani.setText(productModel.getYeniTanim());
        return  sentView;
    }

    // PALET ICERIKLERI
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




    // STATIC CLASS FOR VIEWS
    static class PaltlerHolder{
        TextView ifsID , tarih, kapalimi, barkod, cariUnvan, lref;
    }

    static class PaletItemsHolder{
        TextView urunBarkod, urunStokKodu,  ifsStokKodu,  ifsStokTani;
    }

    static class YeniKodItemsHolder{
        TextView yeniKod, yeniTani;
    }

    static class  PaletIcerikDetayHolder {
        TextView lRef, paletlerRef, uretimBarkodu, urunTipi, ifs_Stok_Kodu, ifs_Stok_Tanimi, _adet, transferredToIfs,  uru_Stok_Kodu, urunAdi, tarih, kullanicilarRef;
    }

}
