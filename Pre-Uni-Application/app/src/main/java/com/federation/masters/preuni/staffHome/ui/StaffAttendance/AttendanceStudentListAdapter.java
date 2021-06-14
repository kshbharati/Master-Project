package com.federation.masters.preuni.staffHome.ui.StaffAttendance;
import com.federation.masters.preuni.*;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.databinding.AttendancePopUpMenuBinding;
import com.federation.masters.preuni.databinding.FragmentStaffAttendanceBinding;
import com.federation.masters.preuni.databinding.FragmentStaffAttendanceRecyclerviewItemBinding;
import com.federation.masters.preuni.models.Attendance;
import com.federation.masters.preuni.models.Grading;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.Submission;
import com.federation.masters.preuni.models.TeachingClass;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;


public class AttendanceStudentListAdapter extends RecyclerView.Adapter<AttendanceStudentListAdapter.ViewHolder>{
    Timer timer;
    FragmentStaffAttendanceRecyclerviewItemBinding binding;
    ArrayList<AttendanceStudentListAdapter.ViewHolder> allHolders=new ArrayList<AttendanceStudentListAdapter.ViewHolder>();
    ArrayList<Student> mValues;
    RecyclerView mRecyclerView;
    AttendancePopUpMenuBinding popBinding;
    TeachingClass selectedClass;
    String selectedDate;
    PopupWindow popupWindow;
    CoordinatorLayout progressBar;
    Context context;

    public AttendanceStudentListAdapter(ArrayList<Student> values, TeachingClass tchClass,String date)
    {
        mValues=values;
        if(!(tchClass==null) || !(date==null))
        {
            selectedClass=tchClass;
            selectedDate=date;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull @NotNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        popupWindow.dismiss();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView=recyclerView;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(mValues.isEmpty()) return null;
        context=parent.getContext();
        binding=FragmentStaffAttendanceRecyclerviewItemBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,
                false);

        progressBar=StaffAttendanceFragment.progressBar;
        progressBar.setVisibility(View.GONE);

        popBinding=AttendancePopUpMenuBinding.inflate(
                LayoutInflater.from(context));
        popupWindow=new PopupWindow();
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popBinding.getRoot());

        popBinding.attendanceSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleselectAll();
            }
        });

        popBinding.attendanceSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpload();
            }
        });
        return new ViewHolder(binding.getRoot());
    }

    public void handleUpload()
    {
        activateProgressBar();
        JSONArray requestBody=new JSONArray();
        for(ViewHolder hold:allHolders)
        {
            Student student=hold.mItem;
            Attendance attendance=new Attendance();
            attendance.setClassId(selectedClass.getId());
            attendance.setDate(selectedDate);
            attendance.setStudentId(student.getId());

            if(hold.isSelected)
            {
                attendance.setAttendance(1);
            }

            if(!hold.isSelected) attendance.setAttendance(0);
            requestBody.put(attendance.getRequestBody());
        }

        Log.d("REQUEST",requestBody.toString());
        String host=context.getResources().getString(R.string.api_host)+"/add_attendance/";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST
                , host,
                requestBody, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object= (JSONObject) response.get(0);
                    if(object.get("RESULT").equals("SUCCESS"))
                    {
                        Snackbar.make(mRecyclerView,"Attendance Updated Successfully",Snackbar.LENGTH_SHORT).show();
                        deactivateProgressBar();
                        handleDeselectAll();
                        return;
                    }
                    Snackbar.make(mRecyclerView,"Something Went Wrong! Try Again Later!!",Snackbar.LENGTH_SHORT).show();
                    deactivateProgressBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        singleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }
    private void activateProgressBar()
    {
        progressBar.setVisibility(View.VISIBLE);
        popupWindow.dismiss();

    }

    public void deactivateProgressBar()
    {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(mValues.isEmpty()) return;
        holder.mItem=mValues.get(position);
        holder.studentName.setText(holder.mItem.getStudentName());
        holder.attendanceStatus.setVisibility(View.INVISIBLE);

        allHolders.add(holder);
        /*if(holder.isSelected)
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("CLICK","LONG CLICK");
                    return true;
                }
            });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(mRecyclerView, Gravity.BOTTOM,0,0);
                handleSelectionClick(holder);

            }
        });
    }

    public void handleselectAll()
    {
        for(ViewHolder hold:allHolders)
        {
            hold.isSelected=false;
            handleSelectionClick(hold);
            notifyDataSetChanged();
        }
    }

    public void handleDeselectAll()
    {
        for(ViewHolder hold:allHolders)
        {
            hold.isSelected=true;
            handleSelectionClick(hold);
            notifyDataSetChanged();
        }
    }
    public void handleSelectionClick(ViewHolder holder)
    {
        if(holder.isSelected)
        {
            holder.isSelected=false;
            holder.itemView.setSelected(false);
            holder.itemView.setBackgroundColor(GlobalApplication.getAppContext().getColor(R.color.white));
            holder.studentName.setTextColor(GlobalApplication.getAppContext().getColor(R.color.black));

            return;
        }

        holder.isSelected=true;
        holder.itemView.setSelected(true);
        holder.itemView.setBackgroundColor(GlobalApplication.getAppContext().getColor(R.color.black));
        holder.studentName.setTextColor(GlobalApplication.getAppContext().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        Student mItem;
        TextView studentName;
        ImageView attendanceStatus;
        boolean isSelected=false;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            studentName=binding.attendanceStudentName;
            attendanceStatus=binding.attandanceStatus;

        }
    }
}
