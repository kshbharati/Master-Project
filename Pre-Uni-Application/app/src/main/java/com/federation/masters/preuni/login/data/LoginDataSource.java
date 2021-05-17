package com.federation.masters.preuni.login.data;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.login.ui.login.LoginActivity;
import com.federation.masters.preuni.models.User;
import com.federation.masters.preuni.models.UserDetail;
import com.federation.masters.preuni.R;
import org.json.JSONArray;

import java.io.IOException;
import com.federation.masters.preuni.login.ui.login.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public Result<User> login(String email, String password) {
            verify_user(email,password);

            Log.d("HELLO", "Response: ");
            try {
                return new Result.Success<>(new User());
            } catch (Exception e) {
                return new Result.Error(new IOException("Error logging in", e));
            }

            //return new Result.Failed<>("Invalid Email/Password");

    }

    public void handleResult(JSONArray response)
    {

    }

    public void verify_user(String email, String password)
    {
        String url="http://10.0.2.2:8000/verify_user";
        final JSONArray resultResponse;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest
                (Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("HELLO",response.toString());
                                handleResult(response);
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

        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void logout() {
        // TODO: revoke authentication
    }
}