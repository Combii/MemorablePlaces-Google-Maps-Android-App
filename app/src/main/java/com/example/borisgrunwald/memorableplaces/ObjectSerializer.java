package com.example.borisgrunwald.memorableplaces;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.HashMap;
import java.util.Map;


public class ObjectSerializer {

    public void saveMap(Map<String, LatLng> map, SharedPreferences sharedPreferences) {

        sharedPreferences.edit().clear().apply();

        //Get saved map
        Map <String,LatLng> SavedMap = getSavedMap(sharedPreferences);

        //Append data to saved map
        for (Map.Entry<String,LatLng> e : map.entrySet()) {
            SavedMap.put(e.getKey(),e.getValue());
        }

        Gson gson = new Gson();
        String hashMapString = gson.toJson(SavedMap);

        //----Saving Map----
        sharedPreferences.edit().putString("hashMapLocation", hashMapString).apply();

        Log.i("SAVING: ","Saved Map!");
    }


     public Map<String, LatLng> getSavedMap(SharedPreferences sharedPreferences){
         Gson gson = new Gson();

         //----Get Map-------
        String storedHashMapString = sharedPreferences.getString("hashMapLocation", "oopsDintWork");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, LatLng>>(){}.getType();
        HashMap<String, LatLng> locationMap = gson.fromJson(storedHashMapString, type);

        Log.i("Getting saved Map: ",locationMap.toString());
        return locationMap;
    }

}
