package com.federation.masters.preuni.parentHome;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.login.ui.login.LoginActivity;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.CourseList;
import com.federation.masters.preuni.models.Message;
import com.federation.masters.preuni.models.ParentUser;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.UserDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParentHomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static ParentUser currentUser;
    public static CourseList allCourseList;
    public static FloatingActionButton fab;

    private AppBarConfiguration mAppBarConfiguration;
    String message;
    View root;


    Gson userGson;
    TextView navViewName;
    TextView navViewEmail;
    ImageView navViewImage;

    ImageButton emailPopUpCloseButton;
    PopupWindow popup;
    ProgressBar emailPopUpProgressBar;
    Button emailSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCourseList();
        message= getIntent().getStringExtra(LoginActivity.EXTRA_MESSAGE);
    }

    private void handleUIUpdateWithData()
    {
        setContentView(R.layout.activity_parent_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.parent_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_parent_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_grades,R.id.nav_parent_messages)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView=navigationView.getHeaderView(0);
        navViewName=(TextView) headerView.findViewById(R.id.navParentName);
        navViewEmail=(TextView) headerView.findViewById(R.id.navParentEmail);
        navViewImage=(ImageView) headerView.findViewById(R.id.navParentImage);

        if(currentUser.getUserdetail().getUserName() != null)
        {
            navViewName.setText(currentUser.getUserdetail().getUserName());
        }

        if(currentUser.getEmail()!=null)
        {
            navViewEmail.setText(currentUser.getEmail());
        }

        fab = findViewById(R.id.fab);
        final LinearLayout lyt=new LinearLayout(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePopUpView(getLayoutInflater(),view);
    }
    public void handlePopUpView(LayoutInflater inflater, View rootView)
    {
        // inflate the layout of the popup window


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        View popupView=inflater.inflate(R.layout.email_send_form,null );

        PopupWindow popup=new PopupWindow(popupView,width,height, true);

        popupView.animate();
        popup.showAtLocation(rootView, Gravity.BOTTOM,10,10);

        ImageButton emailPopUpCloseButton=(ImageButton) popupView.findViewById(R.id.popUpEmailCloseButton);
        Button emailSubmitButton=popupView.findViewById(R.id.popUpEmailSubmitButton);
        ProgressBar emailPopUpProgressBar=popupView.findViewById(R.id.emailFormProgressBar);


        emailSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailPopUpProgressBar.setVisibility(View.VISIBLE);
                handleEmailForm(popupView,popup,emailPopUpProgressBar,rootView);
            }
        });

        emailPopUpCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

    }
        });
    }
    public static void handleEmailForm(View v,PopupWindow popUpWindow,ProgressBar progressBar,View rootView)
    {
        EditText receiverEmail=v.findViewById(R.id.emailSendFormReceiverEmail);
        EditText subject=v.findViewById(R.id.emailSendFormSubject);
        EditText body=v.findViewById(R.id.emailSendFormBody);

        if(receiverEmail.getText().toString().isEmpty()
            || subject.getText().toString().isEmpty()
            ||body.getText().toString().isEmpty())
        {
            Snackbar.make(v,"One or More Field Empty",Snackbar.LENGTH_SHORT)
                    .show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(receiverEmail.getText().toString()).matches())
        {
            Snackbar.make(v,"Email is Invalid!",Snackbar.LENGTH_SHORT)
                    .show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        Message message=new Message();
        message.setSenderEmail(currentUser.getEmail());
        message.setReceiverEmail(receiverEmail.getText().toString());
        message.setMessageSubject(subject.getText().toString());
        message.setMessageBody(body.getText().toString());

        String host=GlobalApplication.getAppContext().getResources().getString(R.string.api_host)+"/sendmail";

        Log.d("HELLO",message.getRequestData().toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, host,message.getRequestData(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("MESSAGE",response.toString());
                            if(response.get("RESULT").toString().equals("SUCCESS"))
                            {
                                Snackbar.make(rootView,"Mail Sent Successfully.",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                                //emailPopUpProgressBar.setVisibility(View.GONE);
                                popUpWindow.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("accept","application/json");
                headers.put("charset","utf-8");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        Log.d("HELLO",request.getBodyContentType().toString());

        try {
            Log.d("HELLO",request.getHeaders().toString());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        singleton.getInstance(v.getContext()).addToRequestQueue(request);
    }

    public  void getCourseList()
    {
        File courseFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.courseListFile));
        Gson courseJson=new Gson();

        String url=GlobalApplication.getAppContext().getString(R.string.api_host)+"/courselist/";

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Course> cse=processCourseData(response);
                        allCourseList=new CourseList(cse);

                        getUser(message);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void getUser(String usr)
    {
        Gson userJson=new Gson();
        String email= usr;
        currentUser=(ParentUser) userJson.fromJson(email,ParentUser.class);

        File userFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.userFile));

        String userDetailURL=GlobalApplication.getAppContext().getString(R.string.api_host)+
                "/user/userdetail/"+currentUser.getId();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.GET,
                userDetailURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserDetail detail=(UserDetail) userJson.fromJson(response.toString(),UserDetail.class);
                        currentUser.setUserdetail(detail);
                        generateAdditionalData();
                        currentUser.setChildrenList(new ArrayList<Student>());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void generateAdditionalData() {
        handleUIUpdateWithData();

        String url= GlobalApplication.getAppContext().getString(R.string.api_host)+
                "/user/staff/assigned_classes/"+
                currentUser.getId();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        //singleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private ArrayList<Course>  processCourseData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Course> childList=new ArrayList<Course>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Course.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.popUpEmailCloseButton:
                popup.dismiss();
                break;
            case R.id.popUpEmailSubmitButton:
                Toast.makeText(v.getContext(),"SUBMIT",Toast.LENGTH_SHORT).show();
        }
    }
}