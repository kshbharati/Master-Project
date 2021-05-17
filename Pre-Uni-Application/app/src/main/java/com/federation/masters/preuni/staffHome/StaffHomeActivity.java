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
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.*;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.login.ui.login.LoginActivity;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.StaffUser;
import com.federation.masters.preuni.models.Student;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class StaffHomeActivity extends AppCompatActivity {
    StaffUser currentUser;
    Gson userJson=new Gson();

    private TextView navViewName;
    private TextView navViewEmail;
    private ImageView navViewImage;

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        String message= getIntent().getStringExtra(LoginActivity.EXTRA_MESSAGE);
        DataPutAndFetchInFile.getInstance().createStaffUserFile(message);
        //generates user info form api


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
        currentUser= DataPutAndFetchInFile.getInstance().getCurrentStaffUser();
        if(currentUser.getUserdetail().getUserName() != null)
        {
            navViewName.setText(currentUser.getUserdetail().getUserName());
        }

        if(currentUser.getEmail()!=null)
        {
            navViewEmail.setText(currentUser.getEmail());
        }

    }

}