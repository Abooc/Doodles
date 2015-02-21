package org.lee.android.demos;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

class VolleyDemo {

    private Context mContext;

    VolleyDemo(Context context){
        mContext = context;
    }


        String url = "";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
//        queue.add(stringRequest);

    }