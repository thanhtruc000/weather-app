package com.example.doanandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    String tenThanhPho = "Saigon";
    ImageView imgBack;
    TextView txtNameCity;
    ListView LV;
    CustomAdapter customAdapter;
    ArrayList<Thoitiet> mangthoitiet;

    Button btnHourly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imgBack = findViewById(R.id.imageViewBack);
        txtNameCity = findViewById(R.id.textViewNameCity);
        LV = findViewById(R.id.listViewMoreDay);
        mangthoitiet = new ArrayList<Thoitiet>();
        customAdapter = new CustomAdapter(MainActivity2.this, mangthoitiet);
        LV.setAdapter(customAdapter);
        btnHourly = findViewById(R.id.buttonHourly);
        Intent intent = getIntent();
        String city = intent.getStringExtra("CITY");

        if(city == null || city ==""){
            Get7DaysData(tenThanhPho);
        }else{
            tenThanhPho = city;
            Get7DaysData(city);
        }


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnHourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, HourlyActivity.class);
                startActivity(intent);
            }
        });
    }


    private void Get7DaysData(String data){
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+data+"&cnt=7&units=metric&appid=53fbf527d52d4d773e828243b90c1f8e&lang=vi";
//        String url = "https://samples.openweathermap.org/data/2.5/forecast/daily?id=524901&appid=b1b15e88fa797225412429c1c50c122a1";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Res: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            txtNameCity.setText(name);

                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for (int i = 0; i< jsonArrayList.length();i++){
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String ngay = jsonObjectList.getString("dt");

                                long l = Long.valueOf(ngay);
                                Date date = new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                                String Day = simpleDateFormat.format(date);
//
                                JSONObject jsonObjectMainTemp = jsonObjectList.getJSONObject("temp");
                                String max = jsonObjectMainTemp.getString("max");
                                String min = jsonObjectMainTemp.getString("min");
                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);

                                String NhietDoMax = String.valueOf(a.intValue());
                                String NhietDoMin = String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                mangthoitiet.add(new Thoitiet(Day,status,icon,NhietDoMax,NhietDoMin));
                            }
                            customAdapter.notifyDataSetChanged();


                        } catch (JSONException e){
                            Toast.makeText(getApplication(), "Thành Phố Không tồn tại", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), "Thành Phố Không tồn tại", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(stringRequest);
    }
}