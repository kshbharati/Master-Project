package com.federation.masters.preuni;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Message;
import com.federation.masters.preuni.models.User;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.federation.masters.preuni.placeholder.PlaceholderContent;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class MessageFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static User currentUser;
    // TODO: Customize parameters
    private int mColumnCount = 1;

    ArrayList <Message> messageList;
    RecyclerView recyclerView;

    public MessageFragment() {
        super();
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MessageFragment newInstance(int columnCount) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        if(getActivity().getClass().equals(ParentHomeActivity.class))
        {
            currentUser=ParentHomeActivity.currentUser;
        }

        if(getActivity().getClass().equals(StaffHomeActivity.class))
        {
            currentUser=StaffHomeActivity.currentUser;
        }
        Log.d("EMAIL",currentUser.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment_layout, container, false);
        fetchMessages(view);
        return view;
    }

    public void setUpMessageListView(View view)
    {
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        recyclerView.setAdapter(new MessageRecyclerViewAdapter(messageList));

    }

    private void fetchMessages(View view)
    {
        String host=getContext().getResources().getString(R.string.api_host)+"/get_mail/"+
                currentUser.getEmail();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(host,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        messageList=processMessageResponse(response);
                        setUpMessageListView(view);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getView(),error.getMessage(),Snackbar.LENGTH_LONG).show();
                    }
                });
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonArrayRequest);
    }

    private ArrayList<Message>  processMessageResponse(JSONArray array)
    {
        Log.d("HELLLO",new Gson().toJson(array));
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Message> childList=new ArrayList<Message>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Message.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }
}