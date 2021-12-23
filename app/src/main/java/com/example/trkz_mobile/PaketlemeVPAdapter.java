package com.example.trkz_mobile;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class PaketlemeVPAdapter extends FragmentStateAdapter {

    public PaketlemeVPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){

        switch (position){
            case 1:
                return new paketlemeYeniPaletTab();
            case 2:
                return new paketlemePaletBulTab();
        }
        return new paketlemePaletlerTab();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}



//public class PaketlemeVPAdapter extends FragmentPagerAdapter {
//
//    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
//    private final ArrayList<String> fragmentTitle = new ArrayList<>();
//
//    public PaketlemeVPAdapter(@NonNull FragmentManager fm, int behavior) {
//        super(fm, behavior);
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        return  fragmentArrayList.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return fragmentArrayList.size();
//    }
//
//    public  void  addFragment(Fragment fragment, String title){
//        fragmentArrayList.add(fragment);
//        fragmentTitle.add(title);
//    }
//
//    @NonNull
//    @Override
//    public CharSequence getPageTitle(int position){
//        return fragmentTitle.get(position);
//    }
//}
