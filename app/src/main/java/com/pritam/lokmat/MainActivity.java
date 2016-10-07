package com.pritam.lokmat;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// find url by debug lokmat app in adb mode & search fiter by E/com.lokmat.app.common.DownloadNewsTask:
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();

        final List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        String[] dataarr = getResources().getStringArray(R.array.cat_arrays);
        for(int i=0; i<dataarr.length; i++)
                {
                    HashMap<String, String> hm = new HashMap<String,String>();
                    hm.put(  "txt",dataarr[i].split("~")[0]);
                    hm.put(  "cur",dataarr[i].split("~")[1]);
                    aList.add(hm);
                }


        // Keys used in Hashmap
        String[] from = { "txt","cur" };

        // Ids of views in listview_layout
        int[] to = { R.id.txt,R.id.cur};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_row1, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) findViewById(R.id.list);
        listView.setFastScrollEnabled(true);
        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Intent in = new Intent(getApplicationContext(), CustomizedListView.class);
                    in.putExtra("category", aList.get(position).get("cur"));
                    in.putExtra("catname", aList.get(position).get("txt"));
                startActivity(in);

            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            //Exit When Back and Set no History
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back button again for exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
