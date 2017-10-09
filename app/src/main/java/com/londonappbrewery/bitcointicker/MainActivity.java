package com.londonappbrewery.bitcointicker;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Bitcoin", "Currency : " + parent.getItemAtPosition(position));
                //letsDoSomeNetworking(BASE_URL + "BTC" + parent.getItemAtPosition(position));
                try {
                    run(BASE_URL+parent.getItemAtPosition(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Bitcoin", BASE_URL + "BTC" + parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoin", "Nothing Selected");
            }
        });


    }

    void run(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Bitcoin", myResponse);
                        //txtString.setText(myResponse);
                        //String price = myResponse.
                        /*try {
                            JSONObject Jobject = new JSONObject(myResponse);
                            JSONArray Jarray = Jobject.getJSONArray("last");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        String resStr = myResponse.toString();
                        try {
                            JSONObject json = new JSONObject(resStr);
                            String price = json.getString("last");
                            Log.d("Bitcoin", "Price" + price);
                            mPriceTextView.setText(price);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    /*// TODO: complete the letsDoSomeNetworking() method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void letsDoSomeNetworking(String url) {

        *//*AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Bitcoin", "JSON: " + response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());
            }
        });*//*
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();


    }*/


}
