package com.example.trkz_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class PaletTipiSpinnerAdapter extends ArrayAdapter {
    private final LayoutInflater mInflater;
    private final Context context;
    private ArrayList<HandlingUnitModel> handlingUnitTypeItems;
    private final int mResource;

    public PaletTipiSpinnerAdapter(Context context, int resource, ArrayList objects){
        super(context, resource, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mResource = resource;
        this.handlingUnitTypeItems = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View neededView = convertView;
        neededView = handtypeUnitItemView(position, convertView, parent);
        return neededView;
    }
    //
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View neededView = null;
        return  neededView = handtypeUnitItemView(position, convertView, parent);
    }

    public View handtypeUnitItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);
        TextView qty = (TextView) view.findViewById(R.id.qty);
        TextView handlingUnitTypeID = (TextView) view.findViewById(R.id.handlingTypeId);
        TextView tanim = (TextView) view.findViewById(R.id.tanim);

        HandlingUnitModel handlingUnitModel = handlingUnitTypeItems.get(position);
        qty.setText(handlingUnitModel.getQty());
        handlingUnitTypeID.setText(handlingUnitModel.getHandlingUnitTypeID());
        tanim.setText(handlingUnitModel.getTanim());
        return view;
    }
}
