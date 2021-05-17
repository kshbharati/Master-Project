package com.federation.masters.preuni.courseDetail.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.federation.masters.preuni.databinding.FragmentItemBinding;
import com.federation.masters.preuni.models.Student;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Student}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StudentListRecyclerViewAdapter extends RecyclerView.Adapter<StudentListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Student> mValues;

    public StudentListRecyclerViewAdapter(ArrayList<Student> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getStudentName());
        holder.mContentView.setText(mValues.get(position).getStudentEnrolledDate());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Student mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.studentName;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}