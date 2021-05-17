package com.federation.masters.preuni.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.models.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

public class ApiMain {
    String hostUrl="http://10.0.2.2:8000";
    Context context;
    public ApiMain(Context con)
    {
        context=con;
    }

    JSONArray returnData;

    public JSONArray fetchDataFromUrlUsingGet(String subUrl, JSONArray Data) {
        //RequestQueue queue = Volley.newRequestQueue();
        //JSONObject returnJSON = new JSONObject();
        String requestUrl=hostUrl.concat(subUrl);

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest
                (Request.Method.GET,
                        requestUrl,
                        null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {

                                returnData=response;
                            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR",error.toString());
                error.printStackTrace();
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };



// Access the RequestQueue through your singleton class.
        singleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
        return returnData;
    }

    public JSONArray fetchDataFromUrl(Map<String,Object> parameters, Method method) {
        //RequestQueue queue = Volley.newRequestQueue();
        //JSONObject returnJSON = new JSONObject();
        String requestUrl = hostUrl;
        int requestMethod=Request.Method.GET;

        if(parameters.containsKey("subUrl"))
        {
            if(parameters.get("subUrl") instanceof String)
            {
                requestUrl=hostUrl.concat((String)parameters.get("subUrl"));
                Log.d("HELLO",requestUrl);
            }
        }else
        {
            return null;
        }

        if(parameters.containsKey("requestMethod"))
        {
            int rMethod=(int)parameters.get("requestMethod");
            requestMethod=rMethod;
        }
        Log.d("HELLO",String.valueOf(requestMethod));

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest
                (requestMethod,
                        requestUrl,
                        null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("HELLO",response.toString());
                                returnData=response;
                            }
                        }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("ERROR",error.toString());
                                    error.printStackTrace();
                                }
                })
                {
                    @Override
                    public String getBodyContentType()
                    {
                        return "application/json";
                    }
                };



// Access the RequestQueue through your singleton class.
        singleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

        return returnData;
    }
}
