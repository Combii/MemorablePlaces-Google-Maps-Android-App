package com.example.borisgrunwald.memorableplaces;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    Map<String, LatLng> locations;
    List<String> listNames = null;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sharedPreferences = this.getSharedPreferences("com.example.borisgrunwald.memorableplaces", Context.MODE_PRIVATE);

        ObjectSerializer serializer = new ObjectSerializer();
        locations = new HashMap<>();

        //sharedPreferences.edit().clear().apply();

        // serializer.saveMap(locations, this.getSharedPreferences("com.example.borisgrunwald.memorableplaces", Context.MODE_PRIVATE));


        Intent intent = getIntent();

        if (intent.hasExtra("Location")) {
            Log.i("Location:", intent.getStringExtra("Location"));
            locations.put(intent.getStringExtra("Location"), new LatLng(Double.parseDouble(intent.getStringExtra("Lng")), Double.parseDouble(intent.getStringExtra("Lat"))));
            //serializer.saveMap(locations, this.getSharedPreferences("com.example.borisgrunwald.memorableplaces", Context.MODE_PRIVATE));
        }

        //locations = serializer.getSavedMap(this.getSharedPreferences("com.example.borisgrunwald.memorableplaces", Context.MODE_PRIVATE));



        listView = (ListView) findViewById(R.id.listView);
        listNames = new ArrayList<>(locations.keySet());
        listNames.add(0, "Add new location...");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listNames);


        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                if (i != 0) {
                    intent.putExtra("LngLat", String.valueOf(locations.get(listNames.get(i))));
                }


                startActivity(intent);
            }
        });
    }









}

