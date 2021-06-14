package com.federation.masters.preuni.parentHome.ui.studentDetail.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.federation.masters.preuni.databinding.ParentStudentDetailFragmentBinding;
import com.federation.masters.preuni.models.ParentHomeDetailViewPassingModel;
import com.federation.masters.preuni.models.Student;
import com.federation.masters.preuni.parentHome.ui.studentDetail.ParentStudentDetailActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class ParentStudentDetailHomeFragment extends Fragment {
    public static Student currentStudent;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private ParentStudentDetailFragmentBinding binding;
    public ParentHomeDetailViewPassingModel passedData;

    public static ParentStudentDetailHomeFragment newInstance(int index) {
        ParentStudentDetailHomeFragment fragment = new ParentStudentDetailHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);


    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = ParentStudentDetailFragmentBinding.inflate(inflater, container, false);

        if(ParentStudentDetailActivity.passedData.getStudent()!=null)
        {
            passedData=ParentStudentDetailActivity.passedData;
            currentStudent=passedData.getStudent();
            binding.studentDetailStudentName.setText(passedData.getStudent().getStudentName());
            if(passedData.getTeachingClasses()!=null)
            {
                if(!(passedData.getTeachingClasses().isEmpty()))
                {
                    binding.studentDetailRecyclerView.setAdapter(new ParentStudentDetailHomeRecyclerAdapter
                            (passedData.getTeachingClasses()));
                }
            }
        }

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        binding.studentDetailRecyclerView.setLayoutManager(layoutManager);
        binding.studentDetailRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ParentStudentDetailHomeRecyclerAdapter adapter= (ParentStudentDetailHomeRecyclerAdapter) binding.studentDetailRecyclerView.getAdapter();

        assert adapter != null;

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}