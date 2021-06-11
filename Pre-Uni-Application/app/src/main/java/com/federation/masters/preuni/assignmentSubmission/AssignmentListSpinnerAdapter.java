package com.federation.masters.preuni.assignmentSubmission;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.courseDetail.ui.main.AssignmentRecyclerViewAdapter;
import com.federation.masters.preuni.models.Assignment;
import com.federation.masters.preuni.models.AssignmentList;
import com.federation.masters.preuni.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentListSpinnerAdapter extends ArrayAdapter<Assignment> {
    List<Assignment> mValues;
    LayoutInflater inflater;

    public AssignmentListSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Assignment> objects) {
        super(context, resource, objects);
        inflater=(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        mValues=objects;
    }


    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            view=inflater.inflate(R.layout.assignment_submission_spinner_dropdown,viewGroup,false);
        }

        TextView textView=view.findViewById(R.id.spinnerAssignmentListItemGroup);
        textView.setText(mValues.get(i).getAssignmentTitle());
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    public int getPositionOfItemIdForId(int id)
    {
        int i=0;
        for (Assignment ass: mValues)
        {
            if(ass.getId()==id)
            {
                return i;
            }
            i++;
        }
        return i;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public Assignment getItem(int i) {
        return mValues.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rView=inflater.inflate(R.layout.assignment_submission_spinner_item,null,true);
        TextView textView=rView.findViewById(R.id.spinnerAssignmentListItem);
        textView.setText(mValues.get(i).getAssignmentTitle());

        return rView;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public class ViewHolder{
    }
}
