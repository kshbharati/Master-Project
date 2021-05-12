package com.federation.masters.preuni.parentHome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.federation.masters.preuni.R;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentListViewHolder> {
    private ArrayList<Student> dataset;

    public StudentAdapter(ArrayList<Student> students) {
        this.dataset = students;
    }

    @Override
    public StudentListViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parent_home_student_list, parent, false);

        //view.setOnClickListener(HomeFragment.studentListOnClickListener);

        StudentListViewHolder vwHolder = new StudentListViewHolder(view);
        return vwHolder;
    }

    @Override
    public void onBindViewHolder(final StudentListViewHolder holder, final int listPosition) {

        TextView textName = holder.textName;
        TextView textCourseList = holder.textViewCourseList;


        textName.setText(dataset.get(listPosition).getStudentName());

        StringBuilder textFormat = new StringBuilder("Enrolled Course List: \n");
        for (Course cse : dataset.get(listPosition).getEnrolledCourseList()) {
            textFormat.append(cse.getCourseName()).append("\n");
        }

        textCourseList.setText(textFormat);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class StudentListViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textViewCourseList;

        public StudentListViewHolder(View itemView) {
            super(itemView);
            this.textName = (TextView) itemView.findViewById(R.id.student_name);
            this.textViewCourseList = (TextView) itemView.findViewById(R.id.student_assigned_course_list);
        }
    }
}
