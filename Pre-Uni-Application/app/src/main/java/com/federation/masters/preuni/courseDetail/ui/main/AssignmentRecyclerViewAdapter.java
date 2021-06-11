package com.federation.masters.preuni.courseDetail.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.assignmentSubmission.AssignmentSubmission;
import com.federation.masters.preuni.databinding.AssignmentFragmentItemBinding;
import com.federation.masters.preuni.models.Assignment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class AssignmentRecyclerViewAdapter extends RecyclerView.Adapter<AssignmentRecyclerViewAdapter.ViewHolder> {

    private Gson gson=new Gson();
    private final ArrayList<Assignment> mValues;

    public AssignmentRecyclerViewAdapter(ArrayList<Assignment> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(AssignmentFragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAssNum.setText("Assignment "+(position+1));
        holder.mAssTitle.setText(mValues.get(position).getAssignmentTitle());
        holder.mAssDesc.setText("Submission Date: "+mValues.get(position).getAssignmentSubmissionDate());

        holder.viewSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GlobalApplication.getAppContext(), AssignmentSubmission.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ASSIGNMENT",gson.toJson(holder.mItem).toString());
                GlobalApplication.getAppContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAssNum;
        public final TextView mAssTitle;
        public final TextView mAssDesc;
        public Assignment mItem;
        public final Button viewSubmission;

        public ViewHolder(AssignmentFragmentItemBinding binding) {
            super(binding.getRoot());
            mAssNum=binding.assignmentNumber;
            mAssTitle=binding.assignmentTitle;
            mAssDesc=binding.assignmentDescription;
            viewSubmission=binding.assignmentViewSubmissionButton;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAssTitle.getText() + "'";
        }
    }
}