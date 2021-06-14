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

public class ParentHomeActivity extends AppCompatActivity{
    public static ParentUser currentUser;
    public static CourseList allCourseList;
    public ArrayList<Student> childrens;

    private AppBarConfiguration mAppBarConfiguration;
    String message;


    Gson userGson;
    TextView navViewName;
    TextView navViewEmail;
    ImageView navViewImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCourseList();
        message= getIntent().getStringExtra(LoginActivity.EXTRA_MESSAGE);
    }

    private void handleUIUpdateWithData() {
        setContentView(R.layout.activity_parent_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.parent_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_parent_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_grades, R.id.nav_parent_messages)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        navViewName = (TextView) headerView.findViewById(R.id.navParentName);
        navViewEmail = (TextView) headerView.findViewById(R.id.navParentEmail);
        navViewImage = (ImageView) headerView.findViewById(R.id.navParentImage);

        if (currentUser.getUserdetail().getUserName() != null) {
            navViewName.setText(currentUser.getUserdetail().getUserName());
        }

        if (currentUser.getEmail() != null) {
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
                        handleUIUpdateWithData();
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

}