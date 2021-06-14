package com.federation.masters.preuni.staffHome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.*;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.login.ui.login.LoginActivity;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.CourseList;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.StaffUser;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.models.User;
import com.federation.masters.preuni.models.UserDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StaffHomeActivity extends AppCompatActivity {
    public static StaffUser currentUser;
    public static CourseList allCourseList;
    Gson userJson=new Gson();
    ProgressBar progressBar;
    private TextView navViewName;
    private TextView navViewEmail;
    private ImageView navViewImage;

    private AppBarConfiguration mAppBarConfiguration;

    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message= getIntent().getStringExtra(LoginActivity.EXTRA_MESSAGE);
        //DataPutAndFetchInFile.getInstance().createStaffUserFile(message);

        //Front Point For Data Fetching From Server
        getCourseList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.staff_home, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_staff_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void handleUIUpdateWithData()
    {
        setContentView(R.layout.activity_staff_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.staff_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_staff_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_staff_home, R.id.nav_staff_attendance)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_staff_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView=navigationView.getHeaderView(0);

        navViewName=(TextView) headerView.findViewById(R.id.navStaffName);
        navViewEmail=(TextView) headerView.findViewById(R.id.navStaffEmail);
        navViewImage=(ImageView) headerView.findViewById(R.id.navStaffImage);

        if(currentUser.getUserdetail().getUserName() != null)
        {
            navViewName.setText(currentUser.getUserdetail().getUserName());
        }

        if(currentUser.getEmail()!=null)
        {
            navViewEmail.setText(currentUser.getEmail());
        }
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
        currentUser=(StaffUser) userJson.fromJson(email,StaffUser.class);

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

        String url= GlobalApplication.getAppContext().getString(R.string.api_host)+
                "/user/staff/assigned_classes/"+
                currentUser.getId();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                currentUser.setAssignedClasses(processClassesData(response));;
                handleUIUpdateWithData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        singleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private ArrayList<TeachingClass> processClassesData(JSONArray classes)
    {
        Gson gson=new Gson();
        gson.toJson(classes.toString());
        ArrayList<TeachingClass> childList=new ArrayList<TeachingClass>();

        for(int i=0;i<classes.length();i++)
        {
            try {
                childList.add(gson.fromJson(classes.getJSONObject(i).toString(),TeachingClass.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return childList;
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
}
