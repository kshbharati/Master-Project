package com.federation.masters.preuni.assignmentSubmission;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.assignmentSubmission.AssignmentSubmission;
import com.federation.masters.preuni.courseDetail.CourseDetail;
import com.federation.masters.preuni.databinding.AssignmentSubmissionRecyclerItemBinding;
import com.federation.masters.preuni.databinding.SubmissionAddFormBinding;
import com.federation.masters.preuni.models.Assignment;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Grading;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.Submission;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class SubmissionRecyclerViewAdapter extends RecyclerView.Adapter<SubmissionRecyclerViewAdapter.ViewHolder> {

    private Gson gson=new Gson();
    private final ArrayList<Submission> mValues;
    RecyclerView mRecyclerView;
    ViewGroup mParent;
    public SubmissionRecyclerViewAdapter(ArrayList<Submission> items) {
        mValues = items;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView=recyclerView;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if(mValues.isEmpty()) return null;
        mParent=parent;
        return new ViewHolder(AssignmentSubmissionRecyclerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues.isEmpty()) return;
        holder.mItem = mValues.get(position);

        for (Student s: CourseDetail.studentInClass.getStudentInClassList())
        {
            if(s.getId() ==  mValues.get(position).getStudentId())
            {
                holder.student=s;
                break;
            }
        }
        holder.mSubmittedBy.setText(holder.student.getStudentName());
        holder.mSubmittedDate.setText(mValues.get(position).getSubmittedDate());

        LocalDateTime date= LocalDateTime.parse(holder.mItem.getSubmittedDate());
        holder.mSubmittedDate.setText(date.format(DateTimeFormatter.ISO_DATE));

        holder.viewSubmission.setImageResource(R.drawable.ic_baseline_grading_not_24);
        updateGrading(holder);

        holder.viewSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSubmissionDetailPopUpView(holder);
            }
        });
    }

    public void updateGrading(ViewHolder holder)
    {
        String host=GlobalApplication.getAppContext().getResources().getString(R.string.api_host)
                +"/get_grade/"+holder.mItem.getId();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, host, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("HELLO",response.toString());
                        if(response.has("RESULT"))
                        {
                            holder.grading=null;
                            return;
                        }
                        holder.grading= new Gson().fromJson(response.toString(),Grading.class);
                        holder.viewSubmission.setImageResource(R.drawable.ic_baseline_grading_done_24);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        singleton.getInstance(mRecyclerView.getContext()).addToRequestQueue(jsonObjectRequest);

    }

    public void getSubmissionDetailPopUpView(ViewHolder holder)
    {
        SubmissionAddFormBinding sBinding=SubmissionAddFormBinding.inflate(LayoutInflater.from(mRecyclerView.getContext()));

        PopupWindow submissionDetail=new PopupWindow();
        submissionDetail.setContentView(sBinding.getRoot());
        submissionDetail.setWidth(ConstraintLayout.LayoutParams.MATCH_PARENT);
        submissionDetail.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
        submissionDetail.setBackgroundDrawable(ResourcesCompat.getDrawable(mRecyclerView.getResources(),R.color.white,null));
        submissionDetail.showAtLocation(mRecyclerView, Gravity.BOTTOM,0,0);

        sBinding.submissionAddAppBarTitle.setText("Submission");
        sBinding.submissionAddCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submissionDetail.dismiss();
            }
        });

        sBinding.spinnerStudentName.setEnabled(false);
        sBinding.spinnerStudentName.setClickable(false);
        ArrayList<Student> stuList=new ArrayList<Student>();
        stuList.add(holder.student);
        sBinding.spinnerStudentName.setAdapter(new StudentListSpinnerAdapter(mParent.getContext(),R.layout.assignment_submission_spinner_item,stuList));


        ArrayList<Assignment> assignmentList=new ArrayList<Assignment>();
        for(Assignment a: CourseDetail.course.getAssignmentList())
        {
            if(holder.mItem.getAssignmentId()==a.getId())
            {
                assignmentList.add(a);
            }
        }
        sBinding.spinnerAssignment.setEnabled(false);
        sBinding.spinnerAssignment.setClickable(false);
        sBinding.spinnerAssignment.setAdapter(new AssignmentListSpinnerAdapter(mParent.getContext(), R.layout.assignment_submission_spinner_item,assignmentList));

        ArrayList<String> strings=new ArrayList<String>();
        strings.add("HAND IN");
        strings.add("EXAM");
        strings.add("ONLINE");

        String str=strings.get(holder.mItem.getSubmissionType());
        String[] spinnerData={str};
        sBinding.spinnerSubmissionType.setEnabled(false);
        sBinding.spinnerSubmissionType.setClickable(false);
        sBinding.spinnerSubmissionType.setAdapter(new ArrayAdapter<String>(mParent.getContext(), R.layout.spinner_item,spinnerData));

        sBinding.submissionFileLink.setText(holder.mItem.getSubmissionFile());
        sBinding.submissionFilePicker.setVisibility(View.GONE);

        if(holder.grading!=null)
        {
            sBinding.submissionFormGrade.setText(holder.grading.getMarkGiven());
            sBinding.submissionFormRemarks.setText(holder.grading.getFeedback());
            sBinding.submissionFormSubmitButton.setText("Update");
            sBinding.submissionFormSubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sBinding.submissionFormGrade.getText().toString().equals("") ||
                    sBinding.submissionFormRemarks.getText().toString().equals(""))
                    {
                        Snackbar.make(mParent,
                                "One or Multiple Field are Empty!!",
                                Snackbar.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    JSONObject object=holder.grading.requestData();
                    String host=GlobalApplication.getAppContext().getResources().
                            getString(R.string.api_host)+"/update_grade/"
                            +holder.grading.getId()+"/"
                            +sBinding.submissionFormGrade.getText().toString()+"/"
                            +sBinding.submissionFormRemarks.getText().toString();

                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, host,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.get("RESULT").equals("SUCCESS"))
                                        {
                                            Snackbar.make(mParent,"Record Updated Successfully",Snackbar.LENGTH_SHORT).show();
                                            submissionDetail.dismiss();
                                            return;
                                        }
                                        Snackbar.make(mParent,"Something went Wrong. Try Again Later!",Snackbar.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        Snackbar.make(mParent, e.toString(),Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(mParent, error.toString(),Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    singleton.getInstance(mParent.getContext()).addToRequestQueue(jsonObjectRequest);
                }
            });
            return;
        }

        sBinding.submissionFormGrade.setClickable(true);
        sBinding.submissionFormRemarks.setClickable(true);
        sBinding.submissionFormGrade.setEnabled(true);
        sBinding.submissionFormRemarks.setEnabled(true);
        sBinding.submissionFormSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sBinding.submissionFormGrade.getText().toString().equals("") ||
                        sBinding.submissionFormRemarks.getText().toString().equals(""))
                {
                    Snackbar.make(mParent,
                            "One or Multiple Field are Empty!!",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                Grading grading=new Grading();
                grading.setSubmissionId(holder.mItem.getId());
                grading.setMarkGiven(sBinding.submissionFormGrade.getText().toString());
                grading.setFeedback(sBinding.submissionFormRemarks.getText().toString());


                JSONObject object=grading.requestData();

                String host=GlobalApplication.getAppContext().getResources().
                        getString(R.string.api_host)+"/add_grade/";

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, host,
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if(!response.has("RESULT"))
                                {
                                    Snackbar.make(mParent,"Grade Added Successfully",Snackbar.LENGTH_SHORT).show();
                                    Grading grade=new Gson().fromJson(response.toString(),Grading.class);
                                    holder.grading=grade;
                                    notifyItemChanged(holder.getBindingAdapterPosition());
                                    notifyDataSetChanged();
                                    submissionDetail.dismiss();
                                    return;
                                }
                                Snackbar.make(mParent,"Something went Wrong. Try Again Later!",Snackbar.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(mParent, error.toString(),Snackbar.LENGTH_SHORT).show();
                    }
                });
                singleton.getInstance(mRecyclerView.getContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mSubmittedBy;
        public final TextView mSubmittedDate;
        public Submission mItem;
        public final ImageButton viewSubmission;
        Grading grading;
        Student student;

        public ViewHolder(@NonNull AssignmentSubmissionRecyclerItemBinding binding) {
            super(binding.getRoot());
            mSubmittedBy=binding.submissionSubmittedByName;
            mSubmittedDate=binding.submissionDate;
            viewSubmission=binding.subMissionViewButton;
        }

    }
}