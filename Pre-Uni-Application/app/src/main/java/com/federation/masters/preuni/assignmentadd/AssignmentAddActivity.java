package com.federation.masters.preuni.assignmentadd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.courseDetail.CourseDetail;
import com.federation.masters.preuni.databinding.AssignmentAddFormBinding;
import com.federation.masters.preuni.models.Assignment;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AssignmentAddActivity extends AppCompatActivity {
    private AssignmentAddFormBinding binding;
    private TeachingClass teachingClass;
    private User currentUser;
    private Course course;

    private EditText assignmentTitle;
    private EditText assignmentDescription;
    private EditText assignmentSubmissionDate;
    private Button assignmentSubmitButton;
    private ProgressBar loadingBar;
    final Calendar calendar=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AssignmentAddFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        teachingClass= CourseDetail.teachingClass;
        currentUser=CourseDetail.currentUser;
        course= DataPutAndFetchInFile.getInstance().getCourseForClass(teachingClass);

        assignmentTitle=binding.assignmentAddTitle;
        assignmentDescription=binding.assignmentAddDescription;
        assignmentSubmissionDate=binding.assignmentAddDate;
        assignmentSubmitButton=binding.assignmentAddSubmit;
        loadingBar=binding.assignmentAddLoading;

        Toolbar toolbar=(Toolbar) findViewById(R.id.assignmentAddToolbar);
        toolbar.setTitle("Add Assignment");
        if(toolbar!=null)
        {
            setSupportActionBar(toolbar);
        }


        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        assignmentSubmissionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AssignmentAddActivity.this,
                        date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        assignmentSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                if(areFormFieldEmpty())
                {
                    showEmptyFieldMessage(null);
                    loadingBar.setVisibility(View.INVISIBLE);
                    return;
                }
                processFormSubmission();
            }
        });

    }

    private void processFormSubmission()
    {
        String host=getString(R.string.api_host)+"/assignment/add";

        Assignment assignment=new Assignment();
        assignment.setAssignmentTitle(assignmentTitle.getText().toString());
        assignment.setAssignmentDesc(assignmentDescription.getText().toString());
        assignment.setAssignmentSubmissionDate(assignmentSubmissionDate.getText().toString());
        assignment.setAssignmentAddedBy(currentUser.getId());
        assignment.setCourseId(course.getId());

        JSONObject object=assignment.getAssignmentPutObject();
        String requestBody=object.toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, host,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("MESSAGE",response.toString());
                            if(response.get("Result").toString().equals("Success"))
                            {

                                showSnackBarMessage(null,"Assignment Created Successfully.");
                                closeActivity();
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

        singleton.getInstance(this).addToRequestQueue(request);
    }
    private boolean areFormFieldEmpty()
    {
        Log.d("Check","Form field are empty or not");
        return assignmentTitle.getText().toString().trim().isEmpty()||
                assignmentDescription.getText().toString().trim().isEmpty() ||
                assignmentSubmissionDate.getText().toString().trim().isEmpty();
    }
    private void showEmptyFieldMessage(View view)
    {
        showSnackBarMessage(view,"One or Multiple Field Are Empty!!");
    }

    private void showSnackBarMessage(View view, String msg)
    {
        if(view==null)
            view=binding.getRoot();
        Snackbar snackbar=Snackbar.make(view, msg,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        assignmentSubmissionDate.setText(sdf.format(calendar.getTime()));
    }

    private void closeActivity()
    {
        finish();
    }
}