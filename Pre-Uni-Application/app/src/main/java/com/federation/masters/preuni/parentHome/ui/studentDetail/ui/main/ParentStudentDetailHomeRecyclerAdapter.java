package com.federation.masters.preuni.parentHome.ui.studentDetail.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.databinding.MessageItemBinding;
import com.federation.masters.preuni.databinding.ParentStudentDetailHomeRecyclerItemBinding;
import com.federation.masters.preuni.models.Assignment;
import com.federation.masters.preuni.models.Attendance;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.Grading;
import com.federation.masters.preuni.models.Message;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.Submission;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.federation.masters.preuni.parentHome.ui.studentDetail.ParentStudentDetailActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ParentStudentDetailHomeRecyclerAdapter extends RecyclerView.Adapter<ParentStudentDetailHomeRecyclerAdapter.ViewHolder> {
    ArrayList<TeachingClass> mValues;
    ParentStudentDetailHomeRecyclerItemBinding binding;
    Student student;
    Context context;
    ArrayList<ViewHolder> allHolders=new ArrayList<ViewHolder>();
    RecyclerView mRecyclerView;


    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView=recyclerView;
    }

    public ParentStudentDetailHomeRecyclerAdapter(ArrayList<TeachingClass> classes)
    {
        mValues=classes;
        student=ParentStudentDetailHomeFragment.currentStudent;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        binding=ParentStudentDetailHomeRecyclerItemBinding.inflate(LayoutInflater.from(context));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(mValues.isEmpty()) return;
        allHolders.add(holder);
        holder.mItem=mValues.get(position);

        holder.course= DataPutAndFetchInFile.getInstance().getCourseForClass(ParentHomeActivity.allCourseList,
                holder.mItem);
        String className=holder.mItem.getClassTitle()+":  "+holder.course.getCourseTitle();

        holder.className.setText(className);

        setAssignmentList(holder);
        setUpAttendance(holder);

    }

    private void setUpAttendance(ViewHolder holder) {
        ArrayList<Attendance> attendances=ParentStudentDetailActivity.attendanceList;

        if(attendances.isEmpty()||attendances==null)
        {
            TextView text=new TextView(context);
            holder.attendance.setText("No Data Available");
            holder.attendanceView.setMinimumWidth(0);
            return;
        }

        ArrayList<Attendance> attendanceForCourse=new ArrayList<Attendance>();
        for(Attendance attendance:attendances)
        {
            if(attendance.getStudentId()==student.getId() &&
                attendance.getClassId()==holder.mItem.getId())
            {
                attendanceForCourse.add(attendance);
            }
        }

        if(attendanceForCourse.isEmpty())
        {
            holder.attendance.setText("No Record Recorded");
            holder.attendanceView.setMinimumWidth(0);
            return;
        }

        int total=attendanceForCourse.size();
        int attended=0;
        for(Attendance attendance: attendanceForCourse)
        {
            if(attendance.getAttendance()==1) attended++;
        }
        DecimalFormat df2 = new DecimalFormat("#.##");

        double percantage=0.0;
        if(attended!=0)
        {
            percantage=(double) attended/total;
            StringBuilder builder=new StringBuilder("Attendance(%): ");
            builder.append(df2.format(percantage*100)).append("%");
            holder.attendance.setText(builder);

            int totalWidth=holder.attendanceViewHolder.getMinimumWidth();
            int processedWidth= (int) ((int)totalWidth*percantage);
            holder.attendanceView.setMinimumWidth(processedWidth);
            return;
        }

        holder.attendance.setText("Attendance(%): 0%");
        holder.attendanceView.setMinimumWidth(0);
    }

    private void configureChartApperance(BarChart attendanceChart) {
        attendanceChart.setEnabled(true);
        attendanceChart.getDescription().setEnabled(false);
        attendanceChart.getLegend().setEnabled(false);
        createChartData(attendanceChart);
    }

    private void createChartData(BarChart attendanceChart) {

        List<BarEntry> values=new ArrayList<>();
        values.add(new BarEntry(0f,20));
        BarDataSet set1=new BarDataSet(values,"Attendance");
        set1.setColor(context.getColor(android.R.color.holo_red_dark));
        List<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(set1);
        BarData barData=new BarData(dataSets);
        attendanceChart.setData(barData);
        attendanceChart.invalidate();
    }

    private void setAssignmentList(ViewHolder holder) {
        getAssignmentList(holder);
    }

    private void getAssignmentList(ViewHolder holder) {
        String host=context.getString(R.string.api_host)+"/get_assignment/"+holder.course.getId();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(
                host, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    handleAssignmentResponse(response,holder);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(mRecyclerView, e.toString(),Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mRecyclerView, error.toString(),Snackbar.LENGTH_LONG).show();
            }
        }
        );
        singleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    private void handleAssignmentResponse(JSONArray response,ViewHolder holder) throws JSONException {
        JSONObject object=response.getJSONObject(0);
        if(object.has("RESULT")) return;
        holder.course.setAssignmentList(DataPutAndFetchInFile.getInstance().processAssignmentData(response));
        setUpAssignmentListView(holder);
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TeachingClass mItem;
        public final TextView className;
        public TextView assignmentList;
        public TextView attendance;
        LinearLayoutCompat attendanceViewHolder;
        LinearLayoutCompat attendanceView;
        public ArrayList<Submission> submissions=new ArrayList<Submission>();
        public ArrayList<Grading> grading=new ArrayList<Grading>();
        public Course course;
        public ViewHolder(ParentStudentDetailHomeRecyclerItemBinding binding) {
            super(binding.getRoot());
            className=binding.studentDetailHomeRecyclerStudentName;
            assignmentList=binding.studentDetailHomeRecyclerAssignmentList;
            attendance=binding.studentDetailHomeRecyclerAttendance;
            attendanceViewHolder=binding.studentDetailAttendanceViewHolder;
            attendanceView=binding.studentDetailAttendanceView;
        }

    }


    public void setUpAssignmentListView(ViewHolder holder)
    {
        StringBuilder textFormat=new StringBuilder();
        ArrayList<Submission> submissions= ParentStudentDetailActivity.studentSubmissions;
        ArrayList<Grading> gradings=ParentStudentDetailActivity.submissionGrades;

            for(Assignment assign: holder.course.getAssignmentList())
            {
                textFormat.append(assign.getAssignmentTitle()).append(":\n");
                ArrayList<Submission> addSubmission=new ArrayList<Submission>();
                for (Submission submission:submissions)
                {
                    if(submission.getAssignmentId()==assign.getId() && submission.getStudentId()==student.getId())
                    {

                        ArrayList<Grading> addGrade=new ArrayList<Grading>();
                        LocalDateTime date= LocalDateTime.parse(submission.getSubmittedDate());
                        textFormat.append("Submitted    On: ").append(date.format(DateTimeFormatter.ISO_DATE));

                        if(gradings==null || gradings.isEmpty())
                        {
                            textFormat.append("    NOT GRADED\n");
                            submission.setGradings(addGrade);
                            addSubmission.add(submission);
                            continue;
                        }
                        for(Grading grade:gradings)
                        {
                            if(grade.getSubmissionId()==submission.getId())
                            {
                                textFormat.append("    GRADED\n");
                                addGrade.add(grade);
                                break;
                            }
                        }
                        textFormat.append("\n");
                        submission.setGradings(addGrade);
                        addSubmission.add(submission);
                        break;
                    }
                    LocalDateTime date= LocalDateTime.parse(assign.getAssignmentSubmissionDate());
                    textFormat.append("NOT SUBMITTED    Submit Date:").append(date.format(DateTimeFormatter.ISO_DATE));
                    break;
                }
                textFormat.append("\n\n");
                assign.setSubmissions(addSubmission);
            }
            holder.assignmentList.setText(textFormat.toString());

    }
}
