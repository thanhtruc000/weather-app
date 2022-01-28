        package com.example.doanandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HourlyActivity extends AppCompatActivity {
    RecyclerView recyclerViewHourWeather;
    HourlyWeatherAdapter hourlyWeatherAdapter;
    ArrayList<ThoitietHourly> arrHourlyData = new ArrayList<ThoitietHourly>();
    Toolbar toolbar_hour;
    LineChart mpLineChart;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("CROOD", Context.MODE_PRIVATE);
        String strLat = sharedPreferences.getString("LAT", "");
        String strLon = sharedPreferences.getString("LON", "");
        GetHourlyData(strLat,strLon);

        setContentView(R.layout.activity_hourly);

        toolbar_hour = findViewById(R.id.toolbar_hour);
        toolbar_hour.setTitle("");


        recyclerViewHourWeather = findViewById(R.id.rcHourlyWeatherList);
        hourlyWeatherAdapter = new HourlyWeatherAdapter(HourlyActivity.this, arrHourlyData);
        recyclerViewHourWeather.setAdapter(hourlyWeatherAdapter);


        setSupportActionBar(toolbar_hour);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Line Chart
        mpLineChart = (LineChart) findViewById(R.id.linechart);
        mpLineChart.setDrawGridBackground(true);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return super.onOptionsItemSelected(item);
    }
    private void drawLineChart(ArrayList<Entry> arrTemp, ArrayList<String> arrDays){
        if(arrTemp.size() > 0){
            LineDataSet lineDataSet1 = new LineDataSet(arrTemp, "Temp");
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet1);

            LineData data = new LineData(dataSets);
            mpLineChart.setData(data);
            mpLineChart.invalidate();


            YAxis leftAxis = mpLineChart.getAxisLeft();

            lineDataSet1.setLineWidth(2);
            lineDataSet1.setColor(Color.BLUE);
            lineDataSet1.setDrawCircles(true);
            lineDataSet1.setDrawCircleHole(true);
            lineDataSet1.setCircleColor(Color.RED);
            lineDataSet1.setValueTextSize(10);
        }
    }
    private  void GetHourlyData(String strLat , String strLon){
        String url= "https://api.openweathermap.org/data/2.5/onecall?lat="+strLat+"&lon="+strLon+"&exclude=minutely&units=imperial&appid=75fa68c95ac7c880fa38de2b868466f5";
        RequestQueue requestQueue = Volley.newRequestQueue(HourlyActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arrHourly = jsonObject.getJSONArray("hourly");
                            ArrayList<String> arrDays = new ArrayList<>();
                            ArrayList<Entry> arrTemp = new ArrayList<>();
                          
                            for(int i = 0 ; i< arrHourly.length() ;i++){
                                JSONObject itemHour = arrHourly.getJSONObject(i);


                                String day = itemHour.getString("dt");
                                String temp = itemHour.getString("temp");
                                String img = itemHour.getJSONArray("weather").getJSONObject(0).getString("icon");

                                long l = Long.valueOf(day);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh aa");
                                SimpleDateFormat simpleDay = new SimpleDateFormat("EEEE");
                                String Day = simpleDay.format(date);
                                String hour = simpleDateFormat.format(date);
                                String convertToC = String.valueOf((Float.parseFloat(temp) - 32) / 1.8);
                                ThoitietHourly thoitietHourly = new ThoitietHourly(hour,img, convertToC.substring(0,2));

                                arrHourlyData.add(thoitietHourly);
                                //Cho bieu do hien thi nhiet do trong 12 tieng
                                if(i<12){
                                    CharSequence foo = Day;
                                    String bar = foo.toString();
                                    String formatDay = bar.substring(0,3);
                                    arrDays.add(formatDay);
                                    arrTemp.add(new Entry(i,Float.parseFloat(temp)));
                                }
                            }
                            hourlyWeatherAdapter.notifyDataSetChanged();
                            drawLineChart(arrTemp, arrDays);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Thành Phố Không Tồn Tại", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(stringRequest);
    }



}
