package com.federation.masters.preuni.assignmentSubmission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.federation.masters.preuni.*;
import com.federation.masters.preuni.courseDetail.CourseDetail;
import com.federation.masters.preuni.databinding.SubmissionAddFormBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;

import Utility.DecimalDigitsInputFilter;

public class SubmissionAdd extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_FILE = 1;
    SubmissionAddFormBinding binding;

    ImageButton backButton;
    ProgressBar progressBar;

    Spinner studentListSpinner;
    Spinner assignmentListSpinner;
    Spinner submissionTypeSpinner;
    EditText gradeText;
    EditText gradeRemarks;
    Button submitButton;

    Button submissionFilePicker;
    TextView submissionFileLink;

    private Bitmap bitmap;
    private String filePath;

    String[] submissionType={"HAND IN","ONLINE","EXAM"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=SubmissionAddFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backButton= binding.submissionAddCloseButton;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar=(ProgressBar) binding.submissionAddProgressBar;
        progressBar.setVisibility(View.VISIBLE);

        studentListSpinner= (Spinner) binding.spinnerStudentName;
        assignmentListSpinner=(Spinner) binding.spinnerAssignment;
        submissionTypeSpinner=(Spinner) binding.spinnerSubmissionType;
        gradeText=(EditText) binding.submissionFormGrade;
        gradeText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});

        gradeRemarks=(EditText) binding.submissionFormRemarks;
        submitButton=(Button)binding.submissionFormSubmitButton;
        submitButton.setOnClickListener(this);

        submissionFilePicker=binding.submissionFilePicker;
        submissionFileLink=binding.submissionFileLink;
        submissionFilePicker.setOnClickListener(this);

        setupSpinner();

        progressBar.setVisibility(View.GONE);
    }

    private void setupSpinner()
    {

        AssignmentListSpinnerAdapter assignmentSpinnerAdapter=new AssignmentListSpinnerAdapter(this, R.layout.assignment_submission_spinner_item,CourseDetail.course.getAssignmentList());
        assignmentListSpinner.setAdapter(assignmentSpinnerAdapter);
        assignmentListSpinner.setOnItemSelectedListener(this);

        StudentListSpinnerAdapter studentSpinnerAdapter=new StudentListSpinnerAdapter(this,R.layout.assignment_submission_spinner_item,CourseDetail.studentInClass.getStudentInClassList());
        studentListSpinner.setAdapter(studentSpinnerAdapter);
        studentListSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> submissionTypeAdapter=new ArrayAdapter<String>(this,R.layout.spinner_item,submissionType);
        //submissionTypeAdapter.setDropDownViewResource(R.layout.assignment_submission_spinner_dropdown);
        submissionTypeSpinner.setAdapter(submissionTypeAdapter);
        submissionTypeSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.spinnerStudentName:
                Snackbar.make(this,view,"Hello Student", BaseTransientBottomBar.LENGTH_SHORT).show();
                break;
            case R.id.spinnerAssignment:
                Snackbar.make(this,view,"Hello Assignment", BaseTransientBottomBar.LENGTH_SHORT).show();
                break;
            case R.id.spinnerSubmissionType:
                Snackbar.make(this,view,"Hello Type", BaseTransientBottomBar.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.submissionFormSubmitButton.getId())
        {
            progressBar.setVisibility(View.VISIBLE);
            if(areFormFieldInValid())
            {
                progressBar.setVisibility(View.GONE);
                return;
            }
            processFormSubmission();
        }

        if(v.getId()==submissionFilePicker.getId())
        {
            filePickerProcess();
        }
    }

    private void filePickerProcess()
    {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(SubmissionAdd.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(SubmissionAdd.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(SubmissionAdd.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            showFileChooser();
        }


    }

    private void showFileChooser() {
        Intent intent = new Intent();

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "text/plain",
                        "application/pdf"};


        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                String[] fileSplit=filePath.split("\\.(?=[^\\.]+$)");
                Toast.makeText(this,filePath.toString(),Toast.LENGTH_SHORT).show();
                if(fileSplit.length>1)
                {
                    for (String s:fileSplit) {
                        Log.d("HELLO",s);
                    }
                    String fileType=fileSplit[1];
                    if(fileType.equals("pdf") || fileType.equals("doc") || fileType.equals("docx"))
                    {
                        try {

                            submissionFileLink.setText(String.valueOf(filePath));
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else
                    {
                        submissionFileLink.setText("Only PDF/Word File Allowed!!");
                    }
                }
            }
            else
            {
                Toast.makeText(
                        this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }

    }
    public String getPath(Uri uri) {
        String path="";

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if(cursor!=null)
        {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            Log.d("HELLO",cursor.getString(nameIndex)+"   "+cursor.getString(sizeIndex));
            path=cursor.getString(nameIndex);
            /*cursor.moveToFirst();
            Log.d("STRING",cursor.toString());
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();

            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
           cursor.close();*/
        }



        return path;
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void processFormSubmission()
    {

    }

    /***********
     *
     * @return
     */
    private boolean areFormFieldInValid()
    {
        if(gradeText.getText().toString().trim().isEmpty()|| gradeRemarks.getText().toString().trim().isEmpty())
        {
            showEmptyFieldMessage(null);
            return true;
        }
        String grade=gradeText.getText().toString();
        int gradeInInt;
        try {
            gradeInInt=Integer.parseInt(grade);
        }catch (NumberFormatException ex)
        {
            showSnackBarMessage(null,"Input is Invalid. Try Again!!");
            return true;
        }

        if(gradeInInt<=0 || gradeInInt>100)
        {
            showSnackBarMessage(null,"Grade should be between 0-100!!");
            return true;
        }
        return false;
    }

    /*********************************4
     * ********************************
     * @param view
     */
    private void showEmptyFieldMessage(View view)
    {
        showSnackBarMessage(view,"One or Multiple Field Are Empty!!");
    }

    private void showSnackBarMessage(View view, String msg)
    {
        if(view==null)
            view=binding.getRoot();
        Snackbar snackbar=Snackbar.make(view, msg,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}