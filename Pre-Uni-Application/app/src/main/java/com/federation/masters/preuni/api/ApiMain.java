package com.federation.masters.preuni.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class ApiMain {
    //URL url=new URL("http://localhost:8000/users/");


    public JSONObject getJsonFromURL(String url) {
        //RequestQueue queue = Volley.newRequestQueue();
        JSONObject returnJSON = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("HELLO", "Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        //singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        return returnJSON;
    }

}
