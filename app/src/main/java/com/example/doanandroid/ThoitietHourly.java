package com.example.doanandroid;

public class ThoitietHourly {

    private  String Hourly;
    private  String Image;
    private   String Temp;


    public ThoitietHourly(String Hourly, String Image, String Temp) {
        this.Hourly = Hourly;
        this.Image = Image;
        this.Temp = Temp;


    }

    public  String getHourly() {
        return Hourly;
    }

    public  void setHourly(String hourly) {
        Hourly = hourly;
    }

    public  String getImage() {
        return Image;
    }

    public  void setImage(String image) {
        Image = image;
    }

    public  String getTemp() {
        return Temp;
    }

    public  void setTemp(String temp) {
        Temp = temp;
    }

}
