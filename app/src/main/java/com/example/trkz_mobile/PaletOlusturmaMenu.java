package com.example.trkz_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.trkz_mobile.databinding.PaletOlusturmaMenuBinding;

public class PaletOlusturmaMenu extends AppCompatActivity {
    public PaletOlusturmaMenuBinding binding;
    Button pOlYeniPaletOlustur;
    Button pOlPaletBul;
    Button pOlPaletAc;
    Button pOlEtiketYaz;
    Button pOlPaletBoz;

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.palet_olusturma_menu);
        binding = PaletOlusturmaMenuBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        dialogBuilder =  new AlertDialog.Builder(this);

        pOlYeniPaletOlustur =  binding.pOlYeniPaletOlustur;
        pOlPaletBul =  binding.pOlPaletBul;
        pOlPaletAc =  binding.pOlPaletAc;
        pOlEtiketYaz =  binding.pOlEtiketYaz;
        pOlPaletBoz =  binding.pOlPaletBoz;

        pOlYeniPaletOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), YeniPaletOlusturma.class);
                startActivity(i);
//                Toast.makeText(getBaseContext(), "Yeni palet oluşturma işlemine gidilecek",Toast.LENGTH_SHORT).show();
            }
        });

        pOlPaletBul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationDialog("Palet Bul");

            }
        });
        pOlPaletAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationDialog("Palet Aç");
            }
        });
        pOlEtiketYaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationDialog("Palet Etiketi Yaz");
            }
        });
        pOlPaletBoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationDialog("Palet Boz");
            }
        });
    }

    public void operationDialog(String opTitle){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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