package com.example.trkz_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    SharedPreferences pref;

    // Kulla

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("UserPreferences", 0);
        int kullRef = pref.getInt("kullaniciRef", 0);
        Log.d("preferenc", String.valueOf(kullRef));

        intent = getIntent();
        String iD;
//        Intent getID = getIntent();
        Log.d("msg", "Trkz mobile");

        // Display navbar on toogle Click
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView  = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null); // Conserve image color

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        final TextView textTitle = findViewById(R.id.textTitle);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                textTitle.setText(destination.getLabel());
            }
        });

        // HIDE ELEMENT

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        Menu nav_Menu = navigationView.getMenu();

        iD =  intent.getStringExtra("ID");

        Log.d("Main kullaniciID", String.valueOf(iD));
         if(iD != null || iD != ""){
             if(iD.equals("p")){
                 nav_Menu.findItem(R.id.menuProcedur).setVisible(false);
                 nav_Menu.findItem(R.id.menuSevkiyat).setVisible(false);
             }else if(iD.equals("s")){
                 nav_Menu.findItem(R.id.menuProcedur).setVisible(false);
                 nav_Menu.findItem(R.id.menuPaketleme).setVisible(false);
             }
         }
//        navigationView = (NavigationView) findViewById(R.id.navigationView);
//        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.menuProcedur).setVisible(false);

//
//        Log.d("TRYING", "MOBILE SERVICE >>>>>>");
//        GetShipmentTask task = new GetShipmentTask();
//        task.execute();
    }


    public ArrayList<String> ParseXML(String result_){
        ArrayList<String> list = new ArrayList<>();
        try {
            XmlPullParserFactory xmlPullParserFactory =  XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(IOUtils.toInputStream(result_),"utf-8");
            int event = xmlPullParser.getEventType();
            Boolean ship_id_ = false;
            while (event != xmlPullParser.END_DOCUMENT)
            {
                if ( event == xmlPullParser.START_TAG){
                    if(xmlPullParser.getName().equals("RECIVER_NAME")){
                        ship_id_ =true;
                    }
                }
                if(event == xmlPullParser.TEXT){
                    if(ship_id_){
                        list.add(xmlPullParser.getText());
                        ship_id_=false;
                    }
                }
                if(event == xmlPullParser.END_TAG){
                    if(xmlPullParser.getName().equals("RECIVER_NAME")){
                        ship_id_ =false;
                    }
                }
                event = xmlPullParser.next();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;

    }

    class GetShipmentTask extends AsyncTask{
        String result;
        @Override
        protected Object doInBackground(Object[] objects) {

            MobileService service = new MobileService();
            result = service.GetShipments("MRK");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//        ArrayList<String> list = ParseXML(result.toString());
            ArrayList<String> list = ParseXML(result.toString());
            Log.d("DisplayArrayLis", list.toString());

//        ArrayAdapter<String> adapter = new ArrayAdapter<>
//                (getApplicationContext(), android.R.layout.simple_selectable_list_item,list);
//        ListView listView = (ListView)findViewById(R.id.lview);
//        listView.setAdapter(adapter);
        }
    }
}
