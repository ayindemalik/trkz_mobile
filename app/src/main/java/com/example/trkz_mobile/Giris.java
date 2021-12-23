package com.example.trkz_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Giris extends AppCompatActivity {

//  Arayüzü ELEMANERI BAĞLAMA
    EditText kullaniciID;
    Button girisButon;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris_activity);

        kullaniciID = findViewById(R.id.kullaniciID);
        girisButon = findViewById(R.id.girisButon);

        girisButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent anaMenu = new Intent(Giris.this,MainActivity.class);
                anaMenu.putExtra("ID", kullaniciID.getText().toString());
//                toAddDoctor.putExtra("userType", "doctor");
                Log.d("kullaniciID",  kullaniciID.getText().toString());
                startActivity(anaMenu);

//                Intent id = new Intent(Giris.this, MainActivity.class);
//                id.putExtra("ID",kullaniciID.getText());
//                i.putExtra("phone",phoneNo[position]);
//                i.putExtra("country",country[position]);
//                i.putExtra("imageid",imageId[position]);
//                startActivity(i);
            }
        });
    }
}