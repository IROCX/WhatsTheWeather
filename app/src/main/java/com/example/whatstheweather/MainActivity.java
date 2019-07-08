package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String baseURL1, baseURL2, completeURL;
    EditText city;
    Button button;
    TextView weatherDisplay;
    String testString;

    public class WeatherInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                testString = "";
                testString = connection.getResponseCode() + "";
                Log.i("TAG", connection.getResponseCode() + "");

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = reader.readLine();
                }
                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject = null;

            if (s == null) {
                if (testString.matches(""))
                    Toast.makeText(getApplicationContext(), "Check your internet connection please.", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), "Invalid city name", Toast.LENGTH_SHORT).show();
                }
            } else {

                try {
                    jsonObject = new JSONObject(s);
                    String weatherInfo = "";
                    weatherInfo = jsonObject.getString("weather");
                    JSONArray array = new JSONArray(weatherInfo);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject subObject = array.getJSONObject(i);
                        String temp = "";
                        temp = subObject.getString("description");
                        String sample = "";
                        sample = temp.substring(0, 1).toUpperCase() + temp.substring(1);
                        weatherDisplay.setText(sample + "");
                    }
                    Log.i("TAG", array.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseURL1 = "http://api.openweathermap.org/data/2.5/weather?q=";
        baseURL2 = ",in&appid=2b8e76f7cc002239461582a9e044407e";


        city = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        weatherDisplay = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherInfo weatherInfo = new WeatherInfo();
                String cityName = city.getText().toString();
                if (cityName.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a city name.", Toast.LENGTH_LONG).show();
                } else {

                    String sample = "";
                    sample = cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
                    completeURL = baseURL1 + sample + baseURL2;

                    Log.i("TAG", completeURL + "");
//
                    try {
                        weatherInfo.execute(completeURL);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("TAG", "Error#1");
                    }
                }
            }
        });


//        try {
//            String s = weatherInfo.execute("http://api.openweathermap.org/data/2.5/weather?q=Vadodara,in&appid=2b8e76f7cc002239461582a9e044407e").get();
//            Log.i("TAG", s + "");
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}