package com.federation.masters.preuni.courseDetail;

import android.os.Bundle;

import com.federation.masters.preuni.databinding.ClassDetailBinding;
import com.federation.masters.preuni.login.ui.login.LoginActivity;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.GenericUser;
import com.federation.masters.preuni.models.StaffUser;
import com.federation.masters.preuni.models.StudentList;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.models.User;
import com.federation.masters.preuni.staffHome.ClassAdapter;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.federation.masters.preuni.courseDetail.ui.main.SectionsPagerAdapter;

import com.federation.masters.preuni.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CourseDetail extends AppCompatActivity {

    private ClassDetailBinding binding;

    public static StaffUser currentUser;
    public static TeachingClass teachingClass;
    public static StudentList studentInClass;
    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser= DataPutAndFetchInFile.getInstance().getCurrentStaffUser();

        String email= getIntent().getStringExtra(ClassAdapter.EXTRA_MESSAGE);

        teachingClass =(TeachingClass) new Gson().fromJson(email, TeachingClass.class);

        studentInClass=DataPutAndFetchInFile.getInstance().getStudentInClass(teachingClass.getId());


        Course course=DataPutAndFetchInFile.getInstance().getCourseForClass(teachingClass);

        binding = ClassDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

}