package np.com.manishtuladhar.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView t1_temp, t2_city, t3_description, t4_date;
    ImageView imageView;
    EditText cityName;
    String location = "Kathmandu";
    String app_id = "04402e294a942dab68a991dc735cf2e4";
    private RequestQueue mQueue;

    String API_ID = "04402e294a942dab68a991dc735cf2e4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1_temp = (TextView)findViewById(R.id.temptextView);
        t2_city = (TextView)findViewById(R.id.citytextView);
        t3_description = (TextView)findViewById(R.id.degreetextView);
        t4_date = (TextView)findViewById(R.id.datetextView);
        cityName = (EditText) findViewById(R.id.locationEditText);
        Button btn = (Button)findViewById(R.id.weatherButton);
        imageView= (ImageView)findViewById(R.id.imageView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("button","button tapped");

                try {
                    String encodedCityName = URLEncoder.encode(cityName.getText().toString(),"UTF-8");
                    if(encodedCityName.equals(""))
                    {
                        Toast.makeText(MainActivity.this, "Enter the weather location", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&units=metric&appid=" + app_id;
                        findWeather(url);
                    }
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Couldnt find the weather", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void findWeather(String url)
    {
        //String url = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&units=metric&APPID=04402e294a942dab68a991dc735cf2e4";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject mainObject = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");

                    JSONObject object = jsonArray.getJSONObject(0);

                    Log.i("desc is ",object.getString("description"));
                    Log.i("temp is",String.valueOf(mainObject.getDouble("temp")));

                    String temp = String.valueOf(mainObject.getDouble("temp"));
                    String desc = object.getString("description");
                    String city = response.getString("name");

                    t2_city.setText(city);
                    t3_description.setText(desc);


                    switch (desc){
                        case "scattered clouds":
                            imageView.setImageResource(R.drawable.icon_scatteredclouds);
                            break;
                        case "clear sky":
                            imageView.setImageResource(R.drawable.icon_clearsky);
                            break;
                        case "few clouds":
                            imageView.setImageResource(R.drawable.icon_fewclouds);
                            break;
                        case "broken clouds":
                            imageView.setImageResource(R.drawable.icon_brokenclouds);
                            break;
                        case "mist":
                            imageView.setImageResource(R.drawable.icon_mist);
                            break;
                        case "rain":
                            imageView.setImageResource(R.drawable.icon_rain);
                            break;
                        case "snow":
                            imageView.setImageResource(R.drawable.icon_snow);
                            break;
                        case "shower rain":
                            imageView.setImageResource(R.drawable.icon_showerrain);
                            break;
                        case "thunderstrom":
                            imageView.setImageResource(R.drawable.icon_thunderstorm);
                            break;

                        default:
                            imageView.setImageResource(R.drawable.icon_clearsky);
                    }

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                    String formatted_date = sdf.format(calendar.getTime());

                    t4_date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp);
                    t1_temp.setText(String.valueOf(temp_int));

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(jsonObjectRequest);
    }
}
