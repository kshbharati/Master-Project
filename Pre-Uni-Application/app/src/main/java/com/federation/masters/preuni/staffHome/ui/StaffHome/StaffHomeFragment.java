package com.federation.masters.preuni.staffHome.ui.StaffHome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.federation.masters.preuni.R;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.staffHome.CourseAdapter;
import com.federation.masters.preuni.parentHome.ui.home.HomeViewModel;

import java.util.ArrayList;

public class StaffHomeFragment extends Fragment {
    public static View.OnClickListener studentListOnClickListener;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private ArrayList<Course> childList;
    private RecyclerView.LayoutManager layoutManager;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_staff_home, container, false);
        recyclerView = root.findViewById(R.id.course_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Course st1=new Course("Test COurse","Course Description");
        Course st2=new Course("ENGLISH","Reading English");
        childList = new ArrayList<Course>();
        childList.add(st1);
        childList.add(st2);
        adapter = new CourseAdapter(childList);

        recyclerView.setAdapter(adapter);
        return root;
    }
}
