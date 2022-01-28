package com.example.doanandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AutoCompleteTextView search;
    TextView view_city;
    TextView view_temp;
    TextView  view_desc;
    TextView view_country;

    ImageView view_weather;
    Button btnNextDay;
    String City = "saigon";
    //    EditText search;
    FloatingActionButton search_floating;
    private static final String[] CITY = new String[] { "Saigon","Vung Tau","HaNoi","DaNang" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("CROOD", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        view_city = findViewById(R.id.town);
        view_temp = findViewById(R.id.temp);
        view_desc = findViewById(R.id.desc);

        view_weather=findViewById(R.id.wheather_image);
        view_country=findViewById(R.id.textViewCountry);

        search=findViewById(R.id.search_edit);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CITY);
        search.setAdapter(adapter);

        search_floating=findViewById(R.id.floating_search);

        btnNextDay = findViewById(R.id.btnNextDays);
        GetCurrentWeatherData(City);
        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                String city = search.getText().toString();
                if(!city.equals("")){
                    City=city;
                }
                intent.putExtra("CITY",City);

                startActivity(intent);
            }
        });
        search_floating.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
//                Hide heyboard
//                InputMethodManager imm=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(), 0);
                String city = search.getText().toString();
                if(!city.equals("")){
                    City=city;
                }
                GetCurrentWeatherData(City);
            }


        });



    }
    public  void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);


        String url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=d5611ebeec216d21f2644b55a7319a5c&lang=vi";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("onResponse: ", String.valueOf(jsonObject));
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            view_city.setText("Tên Thành Phố  : " + name);


                            JSONObject croods = jsonObject.getJSONObject("coord");
                            String lat = croods.getString("lat");
                            String lon = croods.getString("lon");

                            Log.d("lat", lat );
                            Log.d("lon ", lon );

                            editor = sharedPreferences.edit();
                            editor.putString("LAT", lat);
                            editor.putString("LON", lon);
                            editor.commit();

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            view_country.setText("Tên Quốc Gia     :  " + country);

                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                            String Day = simpleDateFormat.format(date);
//                            .setText(Day);


                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather =  jsonArrayWeather.getJSONObject(0);

                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + icon +".png").into(view_weather);
//                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(nhietdo);
                            String NhietDo = String.valueOf(a.intValue());

                            view_temp.setText(NhietDo + "°C");
                            view_desc.setText(doam+"%");


                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
//                            vie.setText(gio+ "m/s");

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectClouds.getString("all");
//                            txtClound.setText(may+ "%");




                        }catch (JSONException e) {
                            Toast.makeText(getApplication(), "Thành Phố Không tồn tại", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), "Thành Phố Không tồn tại", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(stringRequest);
    }


}