package xgen.mobiroo.com.mobirooapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xgen.mobiroo.com.mobirooapp.entity.Country;
import xgen.mobiroo.com.mobirooapp.http.ApplicationController;
import xgen.mobiroo.com.mobirooapp.http.Helpers;

public class CountryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CountryAdapter countryAdapter;
    static Context context;
    private static List<Country> countryList;
    static Country clickedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        countryList = new ArrayList<>();
        getCountries(Helpers.COUNTRIES_URL);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_viewID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CountryActivity.context));
        countryAdapter = new CountryAdapter(countryList, this);
        mRecyclerView.setAdapter(countryAdapter);

        countryAdapter.SetOnItemClickListener(new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickedCountry = countryAdapter.item.get(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("country_code", clickedCountry.getCountryCode());
                Log.v("country_code_country", clickedCountry.getCountryCode());
                startActivity(intent);
            }
        });

    }

    private void getCountries(String endpoint){
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    getCountriesData(new JSONObject(response));
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

    private void getCountriesData(JSONObject jsonObject) {
        Country country;
        Gson gson = new Gson();
     //   String json = gson.toJson(jsonObject);
        Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
        Map<String,String> map = gson.fromJson(jsonObject.toString(), stringStringMap);

        for (Map.Entry<String,String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            country = new Country(value, key);
            countryList.add(country);
        }
        countryAdapter.notifyDataSetChanged();
    }
}
