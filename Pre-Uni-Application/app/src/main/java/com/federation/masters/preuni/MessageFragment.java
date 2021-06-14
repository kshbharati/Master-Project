package com.federation.masters.preuni;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Message;
import com.federation.masters.preuni.models.User;
import com.federation.masters.preuni.parentHome.ParentHomeActivity;
import com.federation.masters.preuni.placeholder.PlaceholderContent;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class MessageFragment extends Fragment {


    ImageButton emailPopUpCloseButton;
    PopupWindow popup;
    ProgressBar emailPopUpProgressBar;
    Button emailSubmitButton;
    public static FloatingActionButton emailSendFab;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment_layout, container, false);

        Context context =getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.messageListRecyclerView);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }


        emailSendFab = view.findViewById(R.id.emailSendFab);
        emailSendFab.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                handlePopUpView(getLayoutInflater(), view);
                                            }
                                        });

        fetchMessages();
        return view;
    }

    public void setUpMessageListView()
    {
        // Set the adapter
        if(messageList.isEmpty()) return;
        recyclerView.setAdapter(new MessageRecyclerViewAdapter(messageList));
    }

    public void fetchMessages()
    {
        String host=getContext().getResources().getString(R.string.api_host)+"/get_mail/"+
                currentUser.getEmail();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(host,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject json=(JSONObject)response.get(0);
                            if(!json.has("RESULT"))
                            {
                                messageList=processMessageResponse(response);
                                setUpMessageListView();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void handlePopUpView(LayoutInflater inflater, View rootView)
    {
        // inflate the layout of the popup window


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        View popupView=inflater.inflate(R.layout.email_send_form,null );

        PopupWindow popup=new PopupWindow(popupView,width,height, true);

        popupView.animate();
        popup.showAtLocation(rootView, Gravity.BOTTOM,10,10);

        ImageButton emailPopUpCloseButton=(ImageButton) popupView.findViewById(R.id.popUpEmailCloseButton);
        Button emailSubmitButton=popupView.findViewById(R.id.popUpEmailSubmitButton);
        ProgressBar emailPopUpProgressBar=popupView.findViewById(R.id.emailFormProgressBar);


        emailSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailPopUpProgressBar.setVisibility(View.VISIBLE);
                handleEmailForm(popupView,popup,emailPopUpProgressBar,rootView);
            }
        });

        emailPopUpCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

    }

    public void handleEmailForm(View v,PopupWindow popUpWindow,ProgressBar progressBar,View rootView)
    {
        EditText receiverEmail=v.findViewById(R.id.emailSendFormReceiverEmail);
        EditText subject=v.findViewById(R.id.emailSendFormSubject);
        EditText body=v.findViewById(R.id.emailSendFormBody);

        if(receiverEmail.getText().toString().isEmpty()
                || subject.getText().toString().isEmpty()
                ||body.getText().toString().isEmpty())
        {
            Snackbar.make(v,"One or More Field Empty",Snackbar.LENGTH_SHORT)
                    .show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(receiverEmail.getText().toString()).matches())
        {
            Snackbar.make(v,"Email is Invalid!",Snackbar.LENGTH_SHORT)
                    .show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        Message message=new Message();
        message.setSenderEmail(currentUser.getEmail());
        message.setReceiverEmail(receiverEmail.getText().toString());
        message.setMessageSubject(subject.getText().toString());
        message.setMessageBody(body.getText().toString());

        String host=GlobalApplication.getAppContext().getResources().getString(R.string.api_host)+"/sendmail";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, host,message.getRequestData(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.get("RESULT").toString().equals("SUCCESS"))
                            {
                                Snackbar.make(rootView,"Mail Sent Successfully.",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                                fetchMessages();
                                popUpWindow.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("accept","application/json");
                headers.put("charset","utf-8");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        singleton.getInstance(v.getContext()).addToRequestQueue(request);
    }

}