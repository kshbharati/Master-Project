package com.federation.masters.preuni.staffHome;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.courseDetail.CourseDetail;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.CourseList;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.google.gson.Gson;
import com.federation.masters.preuni.models.Course;
import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.CourseListViewHolder> {
    public static final String EXTRA_MESSAGE = "com.federation.masters.preuni/com.federation.masters.preuni.courseDetail.CourseDetail.MESSAGE";
    private final ArrayList<TeachingClass> dataset;

    public ClassAdapter(ArrayList<TeachingClass> course) {
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


        textName.setText(dataset.get(listPosition).getClassTitle());

        Course courseForClass= DataPutAndFetchInFile.getInstance().
                getCourseForClass(StaffHomeActivity.allCourseList,dataset.get(listPosition));

        numOfStu.setText(courseForClass.getCourseTitle());

        Button clickBtn=holder.itemView.findViewById(R.id.btn_course_view_detail);
        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeachingClass tchClass=(TeachingClass) dataset.get(listPosition);
                Gson gson=new Gson();
                String cou=gson.toJson(tchClass);
                Intent intent = new Intent(GlobalApplication.getAppContext(), CourseDetail.class);
                intent.putExtra(EXTRA_MESSAGE, cou);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                GlobalApplication.getAppContext().startActivity(intent);
                //Toast.makeText(GlobalApplication.getAppContext(), dataset.get(listPosition).getClassTitle(),
                //Toast.LENGTH_SHORT).show();
            }
        });
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
