package com.federation.masters.preuni.staffHome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.federation.masters.preuni.R;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Student;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseListViewHolder> {
    private ArrayList<Course> dataset;

    public CourseAdapter(ArrayList<Course> course) {
        this.dataset = course;
    }

    @Override
    public CourseListViewHolder onCreateViewHolder(ViewGroup staff, int viewType) {
        View view = LayoutInflater.from(staff.getContext())
                .inflate(R.layout.staff_home_course_list, staff, false);

        //view.setOnClickListener(HomeFragment.studentListOnClickListener);

        CourseListViewHolder vwHolder = new CourseListViewHolder(view);
        return vwHolder;
    }

    @Override
    public void onBindViewHolder(final CourseListViewHolder holder, final int listPosition) {

        TextView textName = holder.textName;
        TextView numOfStu=holder.textViewStudentQuantityInCourse;

        textName.setText(dataset.get(listPosition).getCourseName());

        numOfStu.setText("Students Enrolled In Course: 30");
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class CourseListViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textViewStudentQuantityInCourse;

        public CourseListViewHolder(View itemView) {
            super(itemView);
            this.textName = (TextView) itemView.findViewById(R.id.course_name);
            this.textViewStudentQuantityInCourse = (TextView) itemView.findViewById(R.id.number_of_students);
        }
    }
}
