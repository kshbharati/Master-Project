package com.federation.masters.preuni.courseDetail.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.assignmentadd.AssignmentAddActivity;
import com.federation.masters.preuni.courseDetail.CourseDetail;
import com.federation.masters.preuni.models.Assignment;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class AssignmentFragment extends Fragment {
    private PopupWindow mPopupWindow;
    private CoordinatorLayout cld;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    public static AssignmentRecyclerViewAdapter mAdaper;

    public static ArrayList<Assignment> assignments;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AssignmentFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AssignmentFragment newInstance(int columnCount) {
        AssignmentFragment fragment = new AssignmentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mAdaper.notifyDataSetChanged();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(CourseDetail.course.getAssignmentList()!= null)
        {
            Log.d("RESPONSE",new Gson().toJson(CourseDetail.course.getAssignmentList()).toString());
            assignments=CourseDetail.course.getAssignmentList();
        }

        View rview = inflater.inflate(R.layout.assignment_fragment, container, false);

        cld= rview.findViewById(R.id.staff_class_view);
        View view=rview.findViewById(R.id.assignmentList);
        // Set the adapter
        if(assignments == null)
        {
            view.setVisibility(View.INVISIBLE);
            TextView text=new TextView(GlobalApplication.getAppContext());
            text.setText("No Assignment Available! Add Assignment!!");
            text.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if(cld!=null)
            {
                cld.addView(text);
            }
        }else
        {
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;

            /*
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }*/
                mAdaper=new AssignmentRecyclerViewAdapter(assignments);
                recyclerView.setAdapter(mAdaper);
            }
        }


        ImageButton addButton=(ImageButton) rview.findViewById(R.id.assignment_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GlobalApplication.getAppContext(), AssignmentAddActivity.class);
                startActivity(intent);
            }
        });

        return rview;
    }
}