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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.databinding.FragmentHomeBinding;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.federation.masters.preuni.parentHome.StudentAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static View.OnClickListener studentListOnClickListener;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private ArrayList<Student> childList;
    private HomeViewModel homeViewModel;
    FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater);
        //homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        //final RecyclerView rcView = root.findViewById(R.id.student_list_recycler_view);
        recyclerView = binding.studentListRecyclerView;
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.generateDefaultLayoutParams();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getChildrenList();
        //studentListOnClickListener=new StudentListOnClickListener(this);

        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


            }
        });*/


        return binding.getRoot();
    }

    private void getChildrenList()
    {
        String host=getString(R.string.api_host)+"/get_children/"+ParentHomeActivity.currentUser.getId();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(host, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject=null;
                try {
                    jsonObject=(JSONObject)response.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject.has("RESULT"))
                {
                    Snackbar.make(binding.getRoot(),"No Student Allocated!!",Snackbar.LENGTH_SHORT).show();
                    recyclerView.setAdapter(new StudentAdapter(new ArrayList<Student>()));
                    recyclerView.getAdapter().notifyDataSetChanged();
                    return;
                }

                childList=processStudentData(response);
                recyclerView.setAdapter(new StudentAdapter(childList));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recyclerView.setAdapter(new StudentAdapter(new ArrayList<Student>()));
                Snackbar.make(binding.getRoot(),error.toString(),Snackbar.LENGTH_SHORT).show();
            }
        });
        singleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private ArrayList<Student>  processStudentData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Student> childList=new ArrayList<Student>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Student.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }
}
