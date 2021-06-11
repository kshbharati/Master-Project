package com.federation.masters.preuni.assignmentSubmission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.*;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.courseDetail.CourseDetail;
import com.federation.masters.preuni.models.Assignment;
import com.federation.masters.preuni.models.Submission;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AssignmentSubmission extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageButton addSubmissionButton;
    Spinner spinner;
    public static ArrayList<Submission> submissionList;
    public static Assignment assignment;
    Gson gson=new Gson();
    ProgressBar submissionProgressBar;
    RecyclerView assignmentListRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_submission);

        submissionProgressBar=findViewById(R.id.submissionProgressBar);

        Intent intent=getIntent();

        String intentExtra=intent.getStringExtra("ASSIGNMENT");
        if(intentExtra!=null)
        {
            assignment=gson.fromJson(intentExtra,Assignment.class);
        }
;       Log.d("ASSIGNMENT",gson.toJson(assignment).toString());

        addSubmissionButton=findViewById(R.id.submission_add_button);

        addSubmissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AssignmentSubmission.this,SubmissionAdd.class);
                startActivity(intent);
            }
        });

        ImageButton closeButton=findViewById(R.id.submissionCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinner=(Spinner) findViewById(R.id.spinnerAssignmentList);
        spinner.setOnItemSelectedListener(this);

        AssignmentListSpinnerAdapter spinnerAdapter=new AssignmentListSpinnerAdapter(this, R.layout.assignment_submission_spinner_item,CourseDetail.course.getAssignmentList());
        spinner.setAdapter(spinnerAdapter);

        if(assignment!=null)
        {
            spinner.setSelection(spinnerAdapter.getPositionOfItemIdForId(assignment.getId()));
        }

        assignmentListRecycler=findViewById(R.id.submissionListView);
        RecyclerView.LayoutManager reMng=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        assignmentListRecycler.setLayoutManager(reMng);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    private void getSubmissionList(Object item) {
        if(item instanceof Assignment)
        {
            assignment=(Assignment) item;
        }
        String url=getApplicationContext().getString(R.string.api_host)+"/submissions/"+
                assignment.getId();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        processSubmissionResponse(response);
                        handleRestUIUpdate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        singleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void handleRestUIUpdate() {

        if(submissionList.isEmpty() || submissionList ==null)
        {
            submissionProgressBar.setVisibility(View.GONE);
            return;
        }
        assignmentListRecycler.setAdapter(new SubmissionRecyclerViewAdapter(submissionList));
        submissionProgressBar.setVisibility(View.GONE);
    }

    private void processSubmissionResponse(JSONArray response) {

        Gson gson=new Gson();
        gson.toJson(response.toString());
        ArrayList<Submission> childList=new ArrayList<Submission>();

        for(int i=0;i<response.length();i++)
        {
            try {
                childList.add(gson.fromJson(response.getJSONObject(i).toString(),Submission.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        submissionList=childList;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        submissionProgressBar.setVisibility(View.VISIBLE);
        Assignment ass= (Assignment) adapterView.getAdapter().getItem(i);
        Snackbar.make(view,ass.getAssignmentTitle(),Snackbar.LENGTH_SHORT).show();

        getSubmissionList(spinner.getAdapter().getItem(spinner.getSelectedItemPosition()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}