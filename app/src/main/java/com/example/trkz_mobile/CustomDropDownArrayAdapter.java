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

public class CustomDropDownArrayAdapter extends ArrayAdapter {
    private final LayoutInflater mInflater;
    private final Context context;
    private ArrayList<CustomerModel> items;
    private ArrayList<HandlingUnitModel> handlingUnitTypeItems;
    private final int mResource;
    static String ACTION = "";
    List list = new ArrayList();

    public CustomDropDownArrayAdapter(Context context, int resource, ArrayList objects){
        super(context, resource, 0, objects);

        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mResource = resource;
        this.items = objects;


    }
    public CustomDropDownArrayAdapter(Context context, int resource, ArrayList objects, String action){
        super(context, resource, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mResource = resource;
        ACTION = action;
        this.handlingUnitTypeItems = objects;
    }

//    @Override
//    public int getCount(){
//        return list.size();
//    }
//
//    @Nullable
//    @Override
//    public Object getItem(int position){
//        return list.get(position);
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return createItemView(position, convertView, parent);
//        View neededView = null;
        View neededView = convertView;
        if(ACTION.equals("FIRMALAR")){
            neededView = firmalarItemView(position, convertView, parent);
        }
        else if(ACTION.equals("HANDLING_UNITS")){
            neededView = handtypeUnitItemView(position, convertView, parent);
        }
        return neededView;
    }
//
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return createItemView(position, convertView, parent);
        View neededView = null;
        if(ACTION.equals("FIRMALAR")){
            neededView = firmalarItemView(position, convertView, parent);
        }
        else if(ACTION.equals("HANDLING_UNITS")){
            neededView = handtypeUnitItemView(position, convertView, parent);
        }
        return neededView;
    }
//
    public View firmalarItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);
        TextView musteriId = (TextView) view.findViewById(R.id.musteriID);
        TextView musteriAdi = (TextView) view.findViewById(R.id.musteriAdi);

        CustomerModel customerModel = items.get(position);
        musteriId.setText(customerModel.getMusteriID());
        musteriAdi.setText(customerModel.getMusteriAdi());
        return view;
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



//    List list = new ArrayList();
//    public static String OPERATION = "OPERATION";
//
//    public CustomDropDownArrayAdapter(@NonNull Context context, int resource) {
//        super(context, resource);
//    }
//
//    public void add( @NonNull CustomerModel object){
//        super.add(object);
//        list.add(object);
//    }
//
//    @Override
//    public int getCount(){
//        return list.size();
//    }
//
//    @Nullable
//    @Override
//    public Object getItem(int position){
//        return list.get(position);
//    }
//
//    @Nullable
//    @Override    // ALL METHOD CALLING
//    public View getView(int position, @Nullable View view, @Nullable ViewGroup parent){
//        View row_element;
//        View returnedView;
//        row_element = view;
//
//        // Mehodos from here
//        if (OPERATION.equals("DROPDOWN_CUSTOMER_LIST")){
//            Log.d("DROPDOWN_CUSTOMER_LIST", "ADAPTER GET VIEW CALLED... Pos: "+position);
//            returnedView = GET_CUSTOMERLIST( position, row_element,  parent );
//            return returnedView;
//        }
//        else
//            return row_element ;
//    }
//
//    // PALETLER LISTESI
//    public View  GET_CUSTOMERLIST(int position, View sentView, ViewGroup parent ) {
//        CustomerListHolder customerHolder;
//        if (sentView == null){
//            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            sentView = layoutInflater.inflate(R.layout.yp_customers, parent, false);
//            customerHolder = new CustomerListHolder();
//            customerHolder.musteriID = sentView.findViewById(R.id.musteriID);
//            customerHolder.musteriAdi = sentView.findViewById(R.id.musteriAdi);
//            sentView.setTag(customerHolder);
//        }else{
//            customerHolder = (CustomerListHolder) sentView.getTag();
//        }
//        // Call the helper class with the correspondent constructor
//        CustomerModel customerModel = (CustomerModel)  this.getItem(position);
//        customerHolder.musteriID.setText(customerModel.getMusteriID());
//        customerHolder.musteriAdi.setText(customerModel.getMusteriAdi());
//        return  sentView;
//    }
//
//    // STATIC CLASS FOR VIEWS
//    static class CustomerListHolder{
//        TextView musteriID , musteriAdi;
//    }
}
