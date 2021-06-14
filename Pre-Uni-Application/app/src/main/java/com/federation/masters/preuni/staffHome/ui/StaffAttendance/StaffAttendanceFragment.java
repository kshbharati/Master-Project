package com.federation.masters.preuni.staffHome.ui.StaffAttendance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.assignmentadd.AssignmentAddActivity;
import com.federation.masters.preuni.databinding.FragmentStaffAttendanceBinding;
import com.federation.masters.preuni.databinding.MessageItemBinding;
import com.federation.masters.preuni.models.Assignment;
import com.federation.masters.preuni.models.Attendance;
import com.federation.masters.preuni.models.Grading;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StaffAttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public static CoordinatorLayout progressBar;
    FragmentStaffAttendanceBinding binding;
    final Calendar calendar=Calendar.getInstance();
    TeachingClass selectedClass;
    ArrayList<Student> studentList=new ArrayList<Student>();
    ArrayList<Attendance> attendanceList=new ArrayList<Attendance>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding= FragmentStaffAttendanceBinding.inflate(LayoutInflater.from(container.getContext()),
                container,
                false);
        progressBar=binding.attendanceProgressBar;
        handleUiUpdate();
        return binding.getRoot();

    }

    private void handleUiUpdate()
    {
        ArrayList<TeachingClass> classList= StaffHomeActivity.currentUser.getAssignedClasses();
        StaffAttendanceClassSpinnerAdapter adapter=new StaffAttendanceClassSpinnerAdapter(getContext(),
                R.layout.assignment_submission_spinner_item,classList
                );
        binding.attendanceClassSpinner.setAdapter(adapter);
        binding.attendanceClassSpinner.setSelection(0);
        selectedClass=classList.get(0);
        binding.attendanceClassSpinner.setOnItemSelectedListener(this);

        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        binding.attendanceDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        updateLabel();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.attendanceDatePicker.setText(sdf.format(calendar.getTime()));
        getStudentListInClass(selectedClass.getId());
    }

    private void getAttendanceData() {
        binding.attandanceStudentList.setAdapter(new AttendanceStudentListAdapter(new ArrayList<Student>(),null,null));
        String host=getString(R.string.api_host)+"/get_attendance/"+selectedClass.getId()+"/"+
                binding.attendanceDatePicker.getText().toString();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, host,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object=(JSONObject)response.get(0);
                            if(object.has("RESULT"))
                            {
                                Snackbar.make(binding.getRoot(),
                                        "No Students Matching Parameters!",
                                        Snackbar.LENGTH_SHORT).show();
                                binding.attendanceProgressBar.setVisibility(View.GONE);

                                return;
                            }
                            attendanceList=processAttendanceData(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            binding.attendanceProgressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.attendanceProgressBar.setVisibility(View.GONE);
                    }
                });
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void setUpRecylerView() {
        binding.attandanceStudentList.setAdapter(new AttendanceStudentListAdapter(studentList,selectedClass,
                binding.attendanceDatePicker.getText().toString()));
        binding.attendanceProgressBar.setVisibility(View.GONE);
    }

    private ArrayList<Attendance>  processAttendanceData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Attendance> childList=new ArrayList<Attendance>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Attendance.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    public void getStudentListInClass(int classId)
    {
        binding.attendanceProgressBar.setVisibility(View.VISIBLE);
        File studentFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.studentInCourseFile));

        Gson courseJson=new Gson();

        String url=GlobalApplication.getAppContext().getString(R.string.api_host)+
                "/get_students_in_class/"+classId;

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object=(JSONObject)response.get(0);
                            if(!object.has("RESULT"))
                            {
                                Log.d("HELlO",response.toString());
                                ArrayList<Student> stu=processStudentListInClassData(response);
                                studentList=stu;
                                setUpRecylerView();
                                //getAttendanceData();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==binding.attendanceClassSpinner.getId())
        {
            selectedClass=(TeachingClass)parent.getAdapter().getItem(position);
            getStudentListInClass(selectedClass.getId());
        }

                /*switch (parent.getId())
        {
            case R.id.spinnerStudentName:
                Snackbar.make(this,view,"Hello Student", BaseTransientBottomBar.LENGTH_SHORT).show();
                break;
            case R.id.spinnerAssignment:
                Snackbar.make(this,view,"Hello Assignment", BaseTransientBottomBar.LENGTH_SHORT).show();
                break;
            case R.id.spinnerSubmissionType:
                Snackbar.make(this,view,"Hello Type", BaseTransientBottomBar.LENGTH_SHORT).show();
                break;
        }*/
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
