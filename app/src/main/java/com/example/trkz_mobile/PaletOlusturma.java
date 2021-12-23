package com.example.trkz_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class PaletOlusturma extends AppCompatActivity {
    TabLayout tabLayout;
//    private ViewPager viewPager;
    ViewPager2 viewPager;
    PaketlemeVPAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palet_olusturma);


        tabLayout = findViewById(R.id.paketlemeTabLayout);
        viewPager = findViewById(R.id.paketlemeViewPager);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new PaketlemeVPAdapter(fm, getLifecycle());
        viewPager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Paletler"));
        tabLayout.addTab(tabLayout.newTab().setText("Yeni Palet Oluştur"));
        tabLayout.addTab(tabLayout.newTab().setText("Palet Bul"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


//        tabLayout = findViewById(R.id.paketlemeTabLayout);
//        viewPager = findViewById(R.id.paketlemeViewPager);
//
//        tabLayout.setupWithViewPager(viewPager);
//        PaketlemeVPAdapter  vAdapter = new PaketlemeVPAdapter(getSupportFragmentManager(),
//                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        vAdapter.addFragment(new paketlemePaletlerTab(), "Paletler");
//        vAdapter.addFragment(new paketlemeYeniPaletTab(), "Yeni Palet Oluştur");
//        vAdapter.addFragment(new paketlemePaletBulTab(), "Palet Bul");
//        viewPager.setAdapter(vAdapter);
    }
}