package com.federation.masters.preuni;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.models.Message;
import com.federation.masters.preuni.databinding.MessageItemBinding;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>{
    MessageItemBinding mBinding;
    private final List<Message> mValues;
    ViewGroup container;


    RecyclerView mRecyclerView;
    public MessageRecyclerViewAdapter(List<Message> items) {
        mValues = items;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView=recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        container=parent;
        mBinding=MessageItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(mBinding);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.senderName.setText(mValues.get(position).getSenderEmail());
        holder.messageSubject.setText(mValues.get(position).getMessageSubject());

        if(!holder.mItem.isMessageReadStatus())
        {
            holder.itemView.setBackgroundColor(GlobalApplication.getAppContext().getResources().getColor(R.color.teal_200,null));
        }
        //if(holder.mItem.isMessageReadStatus(MessageFragment.currentUser));
        holder.messageReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopForReplyView(container.getContext(),position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopUpForDetailView(container.getContext(),position,holder.itemView);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView senderName;
        public final TextView messageSubject;
        public final ImageButton messageReply;
        public Message mItem;

        public ViewHolder(MessageItemBinding binding) {
            super(binding.getRoot());
            senderName=binding.senderName;
            messageSubject=binding.messageSubject;
            messageReply=binding.messageReplyButton;
        }

    }

    private void handlePopUpForDetailView(Context context, int position,View recyclerItem)
    {
        Message item=mValues.get(position);

        if(!item.isMessageReadStatus())
        {
            if(item.getReceiverEmail().equals(MessageFragment.currentUser.getEmail()))
            {
                updateReadStatus(context,position,recyclerItem);
            }

        }


        View view=LayoutInflater.from(context).inflate(R.layout.message_detail_layout,container,false);
        TextView messageEmail=view.findViewById(R.id.messageSenderEmail);
        TextView messgeSubject=view.findViewById(R.id.messageSenderSubject);
        TextView messageBody=view.findViewById(R.id.messageSenderBody);
        messageEmail.setText(item.getSenderEmail());
        messgeSubject.setText(item.getMessageSubject());
        messageBody.setText(item.getMessageBody());


        PopupWindow popupWindow=new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(container.getRootView(), Gravity.BOTTOM,0,0);

        ImageButton closeButton=view.findViewById(R.id.messageDetailCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        ImageButton replyButton=view.findViewById(R.id.messageDetailReplyButton);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopForReplyView(context,position);
            }
        });

        ImageButton deleteButton=view.findViewById(R.id.messageDetailDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host=GlobalApplication.getAppContext().getResources().getString(R.string.api_host)
                        +"/deleteMail/"+item.getId();
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.DELETE,host, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.get("RESULT").equals("SUCCESS")) {
                                        mValues.remove(position);
                                        notifyItemRemoved(position);
                                        mRecyclerView.removeViewAt(position);
                                        notifyDataSetChanged();
                                        Snackbar.make(mRecyclerView,"Message Deleted Successfully",
                                                Snackbar.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(mBinding.getRoot().getRootView(),error.getMessage(),Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
                singleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

    private void handlePopForReplyView(Context context,int position)
    {
        if(!(MessageFragment.emailSendFab==null))
        {
            MessageFragment.emailSendFab.performClick();
        }
    }

    private void updateReadStatus(Context context,int position,View recyclerItem)
    {
        Message item=mValues.get(position);

        String host=GlobalApplication.getAppContext().getResources().getString(R.string.api_host)
                +"/updateEmailAsRead/"+item.getId();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,host, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.get("RESULT").equals("SUCCESS")) {
                        mValues.get(position).setMessageReadStatus("READ");

                        recyclerItem.setBackgroundColor(
                                GlobalApplication.getAppContext().getResources().getColor(R.color.white,null)
                        );
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mBinding.getRoot().getRootView(),error.getMessage(),Snackbar.LENGTH_LONG)
                        .show();

            }

        });
        singleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}