package com.findmysalon.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.findmysalon.R;
import com.findmysalon.view.adapters.PlaceAutoSuggestionAdapter;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.findmysalon.utils.abcConstants.GOOGLE_PLACES_API_KEY;

public class PlaceApi {

    public ArrayList<String> autoComplete(String input){
        ArrayList<String> arrayList=new ArrayList();
        HttpURLConnection connection=null;
        StringBuilder jsonResult=new StringBuilder();
        try {
            StringBuilder sb=new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            sb.append("input="+input);
            sb.append("&key="+GOOGLE_PLACES_API_KEY);
            URL url=new URL(sb.toString());
            connection=(HttpURLConnection)url.openConnection();
            InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream());

            int read;

            char[] buff=new char[1024];
            while ((read=inputStreamReader.read(buff))!=-1){
                jsonResult.append(buff,0,read);
            }

            Log.d("JSon",jsonResult.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(connection!=null){
                connection.disconnect();
            }
        }

        try {
            JSONObject jsonObject=new JSONObject(jsonResult.toString());
            JSONArray prediction=jsonObject.getJSONArray("predictions");
            for(int i=0;i<prediction.length();i++){
                arrayList.add(prediction.getJSONObject(i).getString("description"));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return arrayList;
    }

    public HashMap<String, Object> fetchAddressLatLng(AutoCompleteTextView textView, Context context){
        HashMap<String, Object> map = new HashMap<>();

        AutoCompleteTextView autoCompleteTextView = textView;
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestionAdapter(context, android.R.layout.simple_list_item_1));
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("Address : ",autoCompleteTextView.getText().toString());
            LatLng latLng = getLatLngFromAddress(context, autoCompleteTextView.getText().toString());
            if(latLng!=null) {

                //mAddress[0] = autoCompleteTextView.getText().toString();
               /* mLat[0] = latLng.latitude;
                mLng[0] = latLng.longitude;*/
                map.put("address", autoCompleteTextView.getText().toString());
                map.put("lat", latLng.latitude);
                map.put("lng", latLng.longitude);
                Log.d("Address : ", " " + latLng.latitude + " " + latLng.longitude+" "+map);
            }
            else {
                Log.d("Lat Lng","Lat Lng Not Found");
            }

        });

        return map;
    }

    // Method to get latitude and longitude from address
    private LatLng getLatLngFromAddress(Context context, String address){

        Geocoder geocoder=new Geocoder(context);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                Address singleAddress = addressList.get(0);
                LatLng latLng = new LatLng(singleAddress.getLatitude(),singleAddress.getLongitude());
                return latLng;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}