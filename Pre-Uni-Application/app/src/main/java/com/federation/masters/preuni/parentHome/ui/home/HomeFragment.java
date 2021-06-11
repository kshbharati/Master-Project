package com.federation.masters.preuni.parentHome.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.federation.masters.preuni.parentHome.StudentAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static View.OnClickListener studentListOnClickListener;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private ArrayList<Student> childList;
    private RecyclerView.LayoutManager layoutManager;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        childList = new ArrayList<Student>();
        Student st1 = new Student();
        Student st2 = new Student();

        if(ParentHomeActivity.currentUser.getChildrenList()==null)
        {
            TextView text=new TextView(GlobalApplication.getAppContext());
            text.setText("No Data Available");
            return text;
        }
        //homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final RecyclerView rcView = root.findViewById(R.id.student_list_recycler_view);
        recyclerView = root.findViewById(R.id.student_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        childList.add(st1);
        childList.add(st2);
        adapter = new StudentAdapter(childList);

        recyclerView.setAdapter(adapter);

        //studentListOnClickListener=new StudentListOnClickListener(this);

        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


            }
        });*/


        return root;
    }
}
