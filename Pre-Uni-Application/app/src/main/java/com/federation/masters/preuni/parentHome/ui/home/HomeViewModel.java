package com.federation.masters.preuni.parentHome.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.federation.masters.preuni.models.Course;
import com.federation.masters.preuni.models.Student;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> stuName;
    private MutableLiveData<String> courseList;

    public HomeViewModel(Student student) {
        if (!student.getStudentName().isEmpty()) {
            stuName = new MutableLiveData<String>();
            stuName.setValue(student.getStudentName());

            StringBuilder textFormat = new StringBuilder("Enrolled Course List: \n");
            /*for (Course cse : student.getEnrolledCourseList()) {
                textFormat.append(cse.getCourseTitle()).append("\n");
            }*/

            courseList = new MutableLiveData<String>();
            courseList.setValue(textFormat.toString());
        }
    }

    public LiveData<String> getText() {
        return stuName;
    }

    public LiveData<String> getCourseList() {
        return courseList;
    }
}
