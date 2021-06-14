package com.federation.masters.preuni.parentHome;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.databinding.ParentHomeStudentListBinding;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.ParentHomeDetailViewPassingModel;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.parentHome.ui.studentDetail.ParentStudentDetailActivity;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentListViewHolder> {
    private final ArrayList<Student> dataset;
    Context context;
    RecyclerView mRecyclerView;
    ParentHomeStudentListBinding binding;
    public StudentAdapter(ArrayList<Student> students) {
        this.dataset = students;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        mRecyclerView=recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public StudentListViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        binding=ParentHomeStudentListBinding.inflate(LayoutInflater.from(parent.getContext()));
        if(dataset.isEmpty()) return null;
        context=parent.getContext();
        return new StudentListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final StudentListViewHolder holder, final int listPosition) {
        if(dataset.isEmpty()) return;

        holder.mItem=dataset.get(listPosition);
        TextView textName = holder.studentName;

        textName.setText(dataset.get(listPosition).getStudentName());
        getClassList(holder);
        //for (Course cse : dataset.get(listPosition).getEnrolledCourseList()) {
         //   textFormat.append(cse.getCourseTitle()).append("\n");
        //}
    }

    private void getClassList(StudentListViewHolder holder) {
        String host=context.getString(R.string.api_host)+"/get_classes_for_student/"+holder.mItem.getId();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(host, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object=(JSONObject)response.get(0);
                    if(object.has("RESULT"))
                    {
                        Snackbar.make(mRecyclerView,"No classes Assigned For Student!",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    holder.classList= DataPutAndFetchInFile.getInstance().processTeachingClassData(response);
                    updateClassList(holder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mRecyclerView,error.toString(),Snackbar.LENGTH_SHORT).show();
            }
        });
        singleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    private void updateClassList(StudentListViewHolder holder) {
        StringBuilder textFormat = new StringBuilder("Class List: \n");
        for(TeachingClass tchClass: holder.classList)
        {
            Course course=DataPutAndFetchInFile.getInstance().getCourseForClass(
                    ParentHomeActivity.allCourseList,tchClass
            );
            textFormat.append(tchClass.getClassTitle());
            textFormat.append(": ");
            textFormat.append(course.getCourseTitle());
            textFormat.append("\n");
        }
        holder.textViewCourseList.setText(textFormat.toString());
        setUpDetailButtonClickListener(holder);
    }

    private void setUpDetailButtonClickListener(StudentListViewHolder holder) {
        holder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ParentStudentDetailActivity.class);

                ParentHomeDetailViewPassingModel passer=new ParentHomeDetailViewPassingModel();
                passer.setStudent(holder.mItem);
                passer.setTeachingClasses(holder.classList);
                String message=new Gson().toJson(passer);
                intent.putExtra("MESSAGE",message);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class StudentListViewHolder extends RecyclerView.ViewHolder {
        public Student mItem;
        public TextView studentName;
        public TextView textViewCourseList;
        public ArrayList<TeachingClass> classList;
        public Button detailButton;
        public StudentListViewHolder(ParentHomeStudentListBinding binding) {
            super(binding.getRoot());
            this.studentName = binding.studentName;
            this.textViewCourseList=binding.studentAssignedCourseList;
            this.detailButton=binding.btnStudentViewDetail;
        }
    }

}
