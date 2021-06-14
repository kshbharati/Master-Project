package com.federation.masters.preuni.parentHome.ui.grades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.federation.masters.preuni.R;
import com.federation.masters.preuni.databinding.SubmissionAddFormBinding;

public class GradesFragment extends Fragment {
    private SubmissionAddFormBinding binding;
    private GradesViewModel gradesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding=SubmissionAddFormBinding.inflate(inflater);
       /* gradesViewModel =
                new ViewModelProvider(this).get(GradesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_grades, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        gradesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return binding.getRoot();

    }

}
