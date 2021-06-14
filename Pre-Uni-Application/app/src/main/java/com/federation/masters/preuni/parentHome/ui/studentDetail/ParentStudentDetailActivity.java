package com.federation.masters.preuni.parentHome.ui.studentDetail;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.models.Attendance;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.Grading;
import com.federation.masters.preuni.models.ParentHomeDetailViewPassingModel;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.Submission;
import com.federation.masters.preuni.parentHome.StudentAdapter;
import com.federation.masters.preuni.parentHome.ui.studentDetail.ui.main.ParentStudentDetailHomeRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.federation.masters.preuni.parentHome.ui.studentDetail.ui.main.SectionsPagerAdapter;
import com.federation.masters.preuni.databinding.ParentStudentDetailBinding;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParentStudentDetailActivity extends AppCompatActivity {

    private ParentStudentDetailBinding binding;
    Student currentStudent;
    public static ArrayList<Submission> studentSubmissions;
    public static ArrayList<Grading> submissionGrades;
    public static ArrayList<Attendance> attendanceList;
    public static ParentHomeDetailViewPassingModel passedData=new ParentHomeDetailViewPassingModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ParentStudentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String message=getIntent().getStringExtra("MESSAGE");

        passedData=new Gson().fromJson(message,ParentHomeDetailViewPassingModel.class);
        currentStudent=passedData.getStudent();
        binding.studentDetailCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        get_grades_and_submission();
        get_attendance();
    }

    public void handleRestUiUpdate()
    {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.parentStudentDetailTab;
        tabs.setupWithViewPager(viewPager);
    }
    public void get_grades_and_submission()
    {
        String host=getString(R.string.api_host)+"/get_submission_for_student/"+currentStudent.getId();

        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(host,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE",response.toString());
                        try {
                            handleSubmissionData(response);
                        } catch (JSONException e) {
                            Snackbar.make(binding.getRoot(),e.toString(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(binding.getRoot(),error.toString(),Snackbar.LENGTH_LONG).show();
                    }
                });
        singleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void handleSubmissionData(JSONObject response) throws JSONException {
        if(response.length()==0) return;
        if(response.has("RESULT"))
        {
            Snackbar.make(binding.getRoot(),
                    "No Submission",Snackbar.LENGTH_SHORT).show();
            return;
        }

        JSONArray submission= (JSONArray) response.get("SUBMISSION");
        JSONArray grades=(JSONArray)response.get("GRADES");
        if(submission.length() == 0) return;
        studentSubmissions= DataPutAndFetchInFile.getInstance().processSubmissionData(submission);
        Log.d("SUBMISSION",studentSubmissions.toString());
        if(grades.length() == 0) return;
        submissionGrades=DataPutAndFetchInFile.getInstance().processGradingData(grades);
        Log.d("GRADES",submissionGrades.toString());
    }


    public void get_attendance()
    {
        String host=getString(R.string.api_host)+"/get_attendance_for_student/"+currentStudent.getId();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(host, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    handleAdmissionData(response);
                } catch (JSONException e) {
                    Snackbar.make(binding.getRoot(),e.toString(),Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(binding.getRoot(),error.toString(),Snackbar.LENGTH_LONG).show();
            }
        });
        singleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void handleAdmissionData(JSONArray response) throws JSONException {
        JSONObject object= (JSONObject) response.get(0);
        if(object.has("RESULT")) return;

        attendanceList=DataPutAndFetchInFile.getInstance().processAttendanceData(response);

        handleRestUiUpdate();
    }
}