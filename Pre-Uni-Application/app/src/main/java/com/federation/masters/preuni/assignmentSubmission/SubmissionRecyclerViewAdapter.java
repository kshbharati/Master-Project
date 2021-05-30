package com.federation.masters.preuni.assignmentSubmission;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.assignmentSubmission.AssignmentSubmission;
import com.federation.masters.preuni.courseDetail.CourseDetail;
import com.federation.masters.preuni.databinding.AssignmentSubmissionRecyclerItemBinding;
import com.federation.masters.preuni.models.Assignment;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.Submission;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class SubmissionRecyclerViewAdapter extends RecyclerView.Adapter<SubmissionRecyclerViewAdapter.ViewHolder> {

    private Gson gson=new Gson();
    private final ArrayList<Submission> mValues;

    public SubmissionRecyclerViewAdapter(ArrayList<Submission> items) {
        mValues = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(AssignmentSubmissionRecyclerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        for (Student s: CourseDetail.studentInClass.getStudentInClassList())
        {
            if(s.getId() ==  mValues.get(position).getStudentId())
            {
                holder.student=s;
                return;
            }
        }
        holder.mSubmittedBy.setText(holder.student.getStudentName());
        holder.mSubmittedBy.setText(mValues.get(position).getStudentId());
        holder.mSubmittedDate.setText(mValues.get(position).getSubmittedDate());

        holder.viewSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Assignment",gson.toJson(holder.mItem).toString());
                GlobalApplication.getAppContext().startActivity(intent);
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
        Student student;

        public ViewHolder(@NonNull AssignmentSubmissionRecyclerItemBinding binding) {
            super(binding.getRoot());
            mSubmittedBy=binding.submissionSubmittedByName;
            mSubmittedDate=binding.submissionDate;
            viewSubmission=binding.subMissionViewButton;
        }

    }
}