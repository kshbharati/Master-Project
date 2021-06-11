package com.federation.masters.preuni.staffHome.ui.StaffHome;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.DataPutAndFetchInFile;
import com.federation.masters.preuni.models.StaffUser;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.models.TeachingClass;
import com.federation.masters.preuni.models.User;
import com.federation.masters.preuni.staffHome.ClassAdapter;
import com.federation.masters.preuni.parentHome.ui.home.HomeViewModel;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class StaffHomeFragment extends Fragment {
    public static View.OnClickListener studentListOnClickListener;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private ArrayList<TeachingClass> childList;
    private RecyclerView.LayoutManager layoutManager;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_staff_home, container, false);
        recyclerView = root.findViewById(R.id.course_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        StaffUser usr= StaffHomeActivity.currentUser;
        if(usr!=null)
        {
            if(usr.getAssignedClasses().isEmpty())
            {

            }else
            {
                recyclerView.setAdapter(new ClassAdapter(usr.getAssignedClasses()));
            }

        }
        return root;
    }

}

