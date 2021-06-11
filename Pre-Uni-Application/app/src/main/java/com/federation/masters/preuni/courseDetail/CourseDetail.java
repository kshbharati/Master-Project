package com.federation.masters.preuni.courseDetail;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.courseDetail.ui.main.AssignmentFragment;
import com.federation.masters.preuni.databinding.ClassDetailBinding;
import com.federation.masters.preuni.models.Assignment;
import com.federation.masters.preuni.models.AssignmentList;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.StaffUser;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.StudentList;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.staffHome.ClassAdapter;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.federation.masters.preuni.courseDetail.ui.main.SectionsPagerAdapter;

import com.federation.masters.preuni.*;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CourseDetail extends AppCompatActivity {

    private ClassDetailBinding binding;
    public static Course course;
    public static StaffUser currentUser;
    public static TeachingClass teachingClass;
    public static StudentList studentInClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser= StaffHomeActivity.currentUser;

        String email= getIntent().getStringExtra(ClassAdapter.EXTRA_MESSAGE);
        teachingClass =(TeachingClass) new Gson().fromJson(email, TeachingClass.class);

        studentInClass=DataPutAndFetchInFile.getInstance().getStudentInClass(teachingClass.getId());
        course=DataPutAndFetchInFile.getInstance().getCourseForClass(teachingClass);
        studentInClass=new StudentList();

        binding = ClassDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getStudentListInClass(teachingClass.getId());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    private void handleUIUpdateWithData() {

        TextView className=binding.courseTitle;
        TextView courseDesc=binding.courseDescription;

        TextView courseId=binding.courseIdHolder;
        courseId.setText(String.valueOf(teachingClass.getId()));
        className.setText("Class: "+teachingClass.getClassTitle());
        courseDesc.setText("Course: "+course.getCourseTitle());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.courseDetailTabs;

        tabs.setupWithViewPager(viewPager);

        Toolbar toolbar=(Toolbar) findViewById(R.id.courseDetailToolbar);
        if(toolbar!=null)
        {
            setSupportActionBar(toolbar);
        }
    }


    public void getAssignmentListForCourse(Course course) {
        File assignmentFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                "/assignmentForCourse"+course.getId()+".json");

        Gson courseJson=new Gson();
        final AssignmentList assignmentList=new AssignmentList();

        String url=GlobalApplication.getAppContext().getString(R.string.api_host)+"/get_assignment/";
        url=url+course.getId();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<Assignment> cse=processAssignmentData(response);
                        assignmentList.setAssignmentList(cse);
                        course.setAssignmentList(cse);

                        handleUIUpdateWithData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        course.setAssignmentList(new ArrayList<Assignment>());
                    }
                }
        );
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonArrayRequest);
    }


    private ArrayList<Assignment> processAssignmentData(JSONArray response) {

        Gson gson=new Gson();
        gson.toJson(response.toString());
        ArrayList<Assignment> childList=new ArrayList<Assignment>();

        for(int i=0;i<response.length();i++)
        {
            try {
                childList.add(gson.fromJson(response.getJSONObject(i).toString(),Assignment.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    public void getStudentListInClass(int classId)
    {
        File studentFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.studentInCourseFile));
        Log.d("Student List",studentFile.toString());

        Gson courseJson=new Gson();

        String url=GlobalApplication.getAppContext().getString(R.string.api_host)+
                "/get_students_in_class/"+classId;

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("COURSES",response.toString());

                        ArrayList<Student> stu=processStudentListInClassData(response);
                        studentInClass.setStudentInClassList(stu);

                        getAssignmentListForCourse(course);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonArrayRequest);
    }

    private ArrayList<Student>  processStudentListInClassData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Student> childList=new ArrayList<Student>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Student.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }
}