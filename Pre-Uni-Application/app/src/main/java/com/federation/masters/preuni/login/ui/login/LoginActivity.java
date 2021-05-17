package com.federation.masters.preuni.login.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.ApiMain;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.models.User;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.federation.masters.preuni.MESSAGE";
    User resultUser;
    private LoginViewModel loginViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setEnabled(true);
       /* loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });*/

        loginButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               //String us = "{\"email\":\"teach@iibit.com\",\"category\":1,\"id\":1,\"userCreatedDate\":\"2021-05-12T05:14:38\"}";
                                               JSONObject objec=new JSONObject();
                                               try {
                                                   objec.put("email","teach@iibit.com");
                                                   objec.put("category",1);
                                                   objec.put("id",1);
                                               } catch (JSONException e) {
                                                   e.printStackTrace();
                                               }

                                               updateUiWithUser(objec);

                                           }
                /*loadingProgressBar.setVisibility(View.VISIBLE);
                String usernameText=usernameEditText.getText().toString();
                String passwordText=passwordEditText.getText().toString();
                if(isUserNameValid(usernameText) && isPasswordValid(passwordText))
                {
                    String url="http://10.0.2.2:8000/verify_user/"+usernameText+"/"+passwordText;
                    final JSONObject resultResponse;
                    JsonObjectRequest jsonArrayRequest=new JsonObjectRequest
                            (Request.Method.POST,
                                    url,
                                    null,
                                    new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("onResponse", response.toString());
                                            updateUiWithUser(response);
                                        }
                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loadingProgressBar.setVisibility(View.INVISIBLE);
                                    error.printStackTrace();
                                    /*if (error.networkResponse.statusCode == 404) {

                                        Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();
                                    }
                                    loadingProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Error Processing Request. Try Again Later", Toast.LENGTH_SHORT).show();
                                */
                                /*}

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
                }else
                {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Invalid Input. Try Again!!", Toast.LENGTH_SHORT).show();
                }
                //resultUser = loginViewModel.login(usernameEditText.getText().toString(),
                //        passwordEditText.getText().toString());
            }
        */
        });
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void updateUiWithUser(JSONObject response) {
        Log.d("WELCOME",response.toString());
        //String welcome = "Welcome " + model.getDisplayName();
        try {
            if(response.getInt("category") == 1)
            {
                Intent intent = new Intent(this, StaffHomeActivity.class);
                intent.putExtra(EXTRA_MESSAGE, response.toString());
                startActivity(intent);
            }else if(response.getInt("category")==3)
            {
                Intent intent = new Intent(this, ParentHomeActivity.class);
                intent.putExtra(EXTRA_MESSAGE, response.toString());
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

    }

    private void showLoginFailed(VolleyError error) {
        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }
}