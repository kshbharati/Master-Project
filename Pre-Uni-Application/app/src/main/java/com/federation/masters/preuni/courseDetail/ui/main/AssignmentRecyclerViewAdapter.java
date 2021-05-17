package com.federation.masters.preuni.courseDetail.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.federation.masters.preuni.courseDetail.ui.main.placeholder.PlaceholderContent.PlaceholderItem;
import com.federation.masters.preuni.databinding.AssignmentFragmentItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AssignmentRecyclerViewAdapter extends RecyclerView.Adapter<AssignmentRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public AssignmentRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(AssignmentFragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAssTitle.setText(mValues.get(position).id);
        holder.mAssDesc.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAssNum;
        public final TextView mAssTitle;
        public final TextView mAssDesc;
        public PlaceholderItem mItem;

        public ViewHolder(AssignmentFragmentItemBinding binding) {
            super(binding.getRoot());
            mAssNum=binding.assignmentNumber;
            mAssTitle=binding.assignmentTitle;
            mAssDesc=binding.assignmentDescription;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAssTitle.getText() + "'";
        }
    }
}