package com.example.trkz_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.trkz_mobile.databinding.PaketlemeFragmentBinding;
import com.example.trkz_mobile.databinding.SevkiyatFragmentBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaketlemeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaketlemeFragment extends Fragment {
    public PaketlemeFragmentBinding binding;
    Button toPaletOlusturma, toTakimOlusturma;
    Button toPaletketTab, btnPaletBul, btnPaletBoz;

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PaketlemeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PaketlemeFragment newInstance(String param1, String param2) {
        PaketlemeFragment fragment = new PaketlemeFragment();
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
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.paketleme_fragment, container, false);

        binding = PaketlemeFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dialogBuilder =  new AlertDialog.Builder(getContext());

        toPaletketTab = binding.paketMenuBtnPaletlerTab;
        toPaletketTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getContext(), PaletOlusturma.class);
                Intent i = new Intent(getContext(), PaletlerListesi.class);
                startActivity(i);
            }
        });

        toPaletOlusturma = binding.paketMenuBtnPolsturma;
        toPaletOlusturma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), PaletOlusturmaMenu.class);
                startActivity(i);
            }
        });

//        paketMenuBtnTolsturma

//        btnPaletBoz = ;
        btnPaletBul = binding.paketMenuBtnPBulma;
        btnPaletBul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationDialog("Palet Bul");
            }
        });
        return root;
    }

    public void operationDialog(String opTitle){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customDialogView = inflater.inflate(R.layout.paketleme_operation_dialog, null, false);
        TextView operationTitle = customDialogView.findViewById(R.id.operationTitle);
            operationTitle.setText(opTitle);
        Button operationButton = customDialogView.findViewById(R.id.operationButton);
            operationButton.setText("Tamam");
        dialogBuilder.setView(customDialogView);
        dialog =  dialogBuilder.create();
//        dialog.setTitle(opTitle);
        dialog.show();
    }
}