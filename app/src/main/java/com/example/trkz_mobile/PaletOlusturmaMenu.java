package com.example.trkz_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.trkz_mobile.databinding.PaletOlusturmaMenuBinding;

public class PaletOlusturmaMenu extends AppCompatActivity {
    public PaletOlusturmaMenuBinding binding;
    Button pOlYeniPaletOlustur;
    Button pOlPaletBul;
    Button pOlPaletAc;
    Button pOlEtiketYaz;
    Button pOlPaletBoz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.palet_olusturma_menu);
        binding = PaletOlusturmaMenuBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);
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
//        return root;
    }
}