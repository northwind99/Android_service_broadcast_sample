package xgen.mobiroo.com.mobirooapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import xgen.mobiroo.com.mobirooapp.http.ApplicationController;
import xgen.mobiroo.com.mobirooapp.http.Helpers;
import xgen.mobiroo.com.mobirooapp.service.StartService;

public class DetailActivity extends AppCompatActivity{
    static String country_code; // get country code after clicking on country;
    static Map<String,String> map_capital = new HashMap<>();
    String capital;
    TextView capital_name;
    Button start_service;
    TextView result;
    private Intent intent;
    private BroadcastReceiver mReceiver;
    private static final String TAG = DetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        capital_name = (TextView) findViewById(R.id.capital);
        start_service = (Button) findViewById(R.id.start_service);
        result = (TextView) findViewById(R.id.result);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            country_code = extras.getString("country_code");
        }
        getCapitals(Helpers.CAPITAL_URL);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int num = intent.getIntExtra("calculation_result", 0);
                Log.v(TAG, num + "number received onReceive");
                result.setText(num + "");
            }
        };

      registerReceiver(mReceiver, new IntentFilter("serviceAction"));

        start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplication(), StartService.class);
                startService(intent);
            }
        });
    }

    private void getCapitals(String endpoint){
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    getCapitalsData(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getBaseContext(), "Oops! Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        ApplicationController.getInstance().addToRequestQueue(jsonObjRequest);
    }

    private void getCapitalsData(JSONObject jsonObject) {
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
        map_capital = gson.fromJson(jsonObject.toString(), stringStringMap);
        Log.v("capital_detail1", map_capital.toString());
        setTextDetail();
    }

    private void setTextDetail(){
        capital = map_capital.get(country_code);
        capital_name.setText(capital);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            unregisterReceiver(this.mReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
