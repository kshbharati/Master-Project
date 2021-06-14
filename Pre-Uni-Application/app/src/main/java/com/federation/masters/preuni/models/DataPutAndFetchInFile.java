package com.federation.masters.preuni.models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.federation.masters.preuni.GlobalApplication;
import com.federation.masters.preuni.R;
import com.federation.masters.preuni.api.singleton;
import com.federation.masters.preuni.login.ui.login.LoginActivity;
import com.federation.masters.preuni.staffHome.ClassAdapter;
import com.federation.masters.preuni.staffHome.StaffHomeActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataPutAndFetchInFile {
    private static DataPutAndFetchInFile instance;

    public DataPutAndFetchInFile()
    {
    }

    public static synchronized DataPutAndFetchInFile getInstance() {
        if (instance == null) {
            instance = new DataPutAndFetchInFile();
        }
        return instance;
    }

    public StaffUser getCurrentStaffUser()
    {
        StaffUser currentUser;
        File userFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.userFile));

        Gson gson=new Gson();

        try {
            Reader reader= Files.newBufferedReader(Paths.get(userFile.toString()));
            currentUser=gson.fromJson(reader, StaffUser.class);
            reader.close();
            return currentUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createStaffUserFile(String usr)
    {
        Gson userJson=new Gson();
        String email= usr;
        StaffUser currentUser=(StaffUser) userJson.fromJson(email,StaffUser.class);

        File userFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.userFile));

        String userDetailURL=GlobalApplication.getAppContext().getString(R.string.api_host)+
                "/user/userdetail/"+currentUser.getId();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.GET,
                userDetailURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserDetail detail=(UserDetail) userJson.fromJson(response.toString(),UserDetail.class);
                        currentUser.setUserdetail(detail);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonObjectRequest);

        String url= GlobalApplication.getAppContext().getString(R.string.api_host)+
                "/user/staff/assigned_classes/"+
                currentUser.getId();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                currentUser.setAssignedClasses(processClassesData(response));;
                try {
                    Writer write=new FileWriter(userFile);
                    userJson.toJson(currentUser,write);
                    write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        singleton.getInstance(GlobalApplication.getAppContext()).
                addToRequestQueue(jsonArrayRequest);

    }

    private ArrayList<TeachingClass> processClassesData(JSONArray classes)
    {
        Gson gson=new Gson();
        gson.toJson(classes.toString());
        ArrayList<TeachingClass> childList=new ArrayList<TeachingClass>();

        for(int i=0;i<classes.length();i++)
        {
            try {
                childList.add(gson.fromJson(classes.getJSONObject(i).toString(),TeachingClass.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return childList;
    }


    public  void generateCourseListFromServer()
    {
        File courseFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.courseListFile));
        Gson courseJson=new Gson();

        String url=GlobalApplication.getAppContext().getString(R.string.api_host)+"/courselist/";

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Course> cse=processCourseData(response);
                            CourseList cseList=new CourseList();
                            cseList.setCourseList(cse);

                            Writer write=new FileWriter(courseFile);
                            courseJson.toJson(cseList,write);
                            write.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonArrayRequest);
    }

    private ArrayList<Course>  processCourseData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Course> childList=new ArrayList<Course>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Course.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    public CourseList getCourseList()
    {
        File courseFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.courseListFile));
        generateCourseListFromServer();
        Gson gson=new Gson();
        try {
            Reader reader;
            reader= Files.newBufferedReader(Paths.get(courseFile.toString()));

            CourseList courseList=gson.fromJson(reader, CourseList.class);
            reader.close();

            return courseList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<Assignment>  processAssignmentData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Assignment> childList=new ArrayList<Assignment>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Assignment.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    public ArrayList<Student>  processStudentData(JSONArray array)
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

    public ArrayList<TeachingClass>  processTeachingClassData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<TeachingClass> childList=new ArrayList<TeachingClass>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),TeachingClass.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    public ArrayList<Submission>  processSubmissionData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Submission> childList=new ArrayList<Submission>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Submission.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    public ArrayList<Grading>  processGradingData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Grading> childList=new ArrayList<Grading>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Grading.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    public ArrayList<Attendance>  processAttendanceData(JSONArray array)
    {
        Gson gson=new Gson();
        gson.toJson(array.toString());
        ArrayList<Attendance> childList=new ArrayList<Attendance>();

        for(int i=0;i<array.length();i++)
        {
            try {
                childList.add(gson.fromJson(array.getJSONObject(i).toString(),Attendance.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return childList;
    }

    public Course getCourseForClass(CourseList courses,TeachingClass teachingClass)
    {
        Course cse=new Course();


        if(courses == null)
        {
            return null;
        }
        for(Course css: courses.getCourseList())
        {
            if(teachingClass.getCourseTaught()==css.getId())
            {
                cse=css;
            }
        }

        return cse;
    }



    public StudentList getStudentInClass(int classId)
    {
        File studentFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.studentInCourseFile));

        //generateStudentsInClassFile(classId);

        Gson gson=new Gson();
        try {
            Reader reader;
            reader= Files.newBufferedReader(Paths.get(studentFile.toString()));

            StudentList stuList=gson.fromJson(reader, StudentList.class);
            reader.close();

            return stuList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void generateStudentsInClassFile(int classId)
    {
        final StudentList stuList=new StudentList();

        File studentFile=new File(GlobalApplication.getAppContext().getFilesDir().toString()+
                GlobalApplication.getAppContext().getString(R.string.studentInCourseFile));

        Gson courseJson=new Gson();

        String url=GlobalApplication.getAppContext().getString(R.string.api_host)+
                "/get_students_in_class/"+classId;

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                            ArrayList<Student> stu=processStudentListInClassData(response);

                            stuList.setStudentInClassList(stu);

                        Writer write= null;
                        try {
                            write = new FileWriter(studentFile);
                            courseJson.toJson(stuList,write);
                            write.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        singleton.getInstance(GlobalApplication.getAppContext()).addToRequestQueue(jsonArrayRequest);
    }

    private ArrayList<Student>  processStudentListInClassData(JSONArray array)
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
