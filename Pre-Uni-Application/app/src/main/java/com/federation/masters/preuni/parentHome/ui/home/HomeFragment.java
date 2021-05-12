package com.federation.masters.preuni.parentHome.ui.home;

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
import com.federation.masters.preuni.models.Student;
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
        Student st1 = new Student(1, "Test Student");
        Student st2 = new Student(2, "Test Student 2");

        //homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final RecyclerView rcView = root.findViewById(R.id.student_list_recycler_view);
        recyclerView = root.findViewById(R.id.student_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        childList = new ArrayList<Student>();
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

    /*public static class StudentListOnClickListener implements View.OnClickListener {

        private final Context context;

        private StudentListOnClickListener(HomeFragment context) {
            this.context = (Context) context;
        }

        @Override
        public void onClick(View v) {

        }*/

        /*private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textName);
            String selectedName = (String) textViewName.getText();
            int selectedItemId = -1;
            for (int i = 0; i < childList.length; i++) {
                if (selectedName.equals(MyData.nameArray[i])) {
                    selectedItemId = MyData.id_[i];
                }
            }
            removedItems.add(selectedItemId);
            data.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    }*/
}
