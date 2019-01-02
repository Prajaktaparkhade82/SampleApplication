package com.example.prajakta.apiparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    TextView txtName, txtCode, txtSunrise, txtSunset, txtWeatherDesc, txtWindSpeed, txtHumidity;
    private Button btnRequest;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    int a;
    private String url = "https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListeners();
    }

    private void initListeners() {
        btnRequest.setOnClickListener(this);

    }

    private void initView() {
        btnRequest = findViewById(R.id.buttonRequest);
        txtName = findViewById(R.id.textName);
        txtCode = findViewById(R.id.textCode);
        txtSunrise = findViewById(R.id.textSunrise);
        txtSunset = findViewById(R.id.textSunset);
        txtWeatherDesc = findViewById(R.id.textWeatherDesc);
        txtWindSpeed = findViewById(R.id.textWindSpeed);
        txtHumidity = findViewById(R.id.textHumidity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRequest: {
                sendAndRequestResponse();
                break;
            }
        }
    }

    private void sendAndRequestResponse() {
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //displaay the response on log
                Log.i("RESPONSE", response);
                //     Toast.makeText(getApplicationContext(), "Response :" + response, Toast.LENGTH_LONG).show();//display the response on screen
                //display the response in logcat
                System.out.println("RESPONSE " + response);
                try {
                    //convert  the response into json object for parsing
                    JSONObject jsonObjectRoot = new JSONObject(response);
                    //created the childjson object sys of rootobject
                    JSONObject jsonObjectSys = (JSONObject) jsonObjectRoot.get("sys");
                    //created the childjson object wind of rootobject
                    JSONObject jsonObjectWind = (JSONObject) jsonObjectRoot.get("wind");
                    //created the childjson object main of rootobject
                    JSONObject jsonObjectMain = (JSONObject) jsonObjectRoot.get("main");
                    //created the childjson array of object (weather) of rootobject
                    JSONArray jsonWeatherArray = jsonObjectRoot.getJSONArray("weather");
                    //accesing the 0th index object from array of objects
                    JSONObject jsonweather = jsonWeatherArray.getJSONObject(0);
                    //accessing the values from using key
                    txtName.setText(jsonObjectRoot.getString("name"));
                    txtCode.setText(jsonObjectRoot.getString("cod"));
                    txtSunrise.setText(String.valueOf(jsonObjectSys.getLong("sunrise")));
                    txtSunset.setText(String.valueOf(jsonObjectSys.getLong("sunset")));
                    txtWeatherDesc.setText(jsonweather.getString("description"));
                    txtWindSpeed.setText((String.valueOf(jsonObjectWind.getDouble("speed"))));
                    txtHumidity.setText(String.valueOf(jsonObjectMain.getInt("humidity")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
