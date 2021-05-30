import datetime
import decimal
from typing import List, Optional, Any
from pydantic import BaseModel

# User Schema
from sqlalchemy import DateTime


class UserBase(BaseModel):
    email: str
    category: int


class UserCreate(UserBase):
    password: str


class User(UserBase):
    id: int
    userCreatedDate: datetime.datetime

    # sessionid: str

    class Config:
        orm_mode = True


# UserDetail Schema
class UserDetailBase(BaseModel):
    userName: str
    phoneNumber: Optional[str] = None


class UserDetailCreate(UserDetailBase):
    pass


class UserDetail(UserDetailBase):
    id: int
    userId: int

    class Config:
        orm_mode = True


# Category Schema
class CategoryBase(BaseModel):
    categoryName: str
    categoryDetail: Optional[str] = None


class CategoryCreate(CategoryBase):
    pass


class Category(CategoryBase):
    id: int

    class Config:
        orm_mode = True


# Student Schemas
class StudentBase(BaseModel):
    studentName: str
    studentEnrolledDate: datetime.datetime
    parent: int


class StudentCreate(StudentBase):
    pass


class Student(StudentBase):
    id: int

    class Config:
        orm_mode = True


# Course Schemas
class CourseBase(BaseModel):
    courseTitle: str
    courseDesc: str


class CourseCreate(CourseBase):
    pass


class Course(CourseBase):
    id: int

    class Config:
        orm_mode = True


# TeachingClass Schemas
class TeachingClassBase(BaseModel):
    classTitle: str
    courseTaught: int
    staffTeaching: int


class TeachingClassCreate(TeachingClassBase):
    pass


class TeachingClass(TeachingClassBase):
    id: int

    class Config:
        orm_mode = True


# AssignedCourse Schemas
class AssignedClassBase(BaseModel):
    student: int
    classId: int
    courseStartDate: datetime.date
    courseEndDate: datetime.date


class AssignedClassCreate(AssignedClassBase):
    pass


class AssignedClass(AssignedClassBase):
    id: int

    class Config:
        orm_mode = True


# AssignmentSchemas
class AssignmentBase(BaseModel):
    assignmentTitle: str
    assignmentDesc: str
    assignmentSubmissionDate: datetime.date
    assignmentAddedBy: int
    courseId: int


class AssignmentCreate(AssignmentBase):
    pass


class Assignment(AssignmentBase):
    id: int
    assignmentAddedDate: datetime.date

    class Config:
        orm_mode = True


# SubmissionSchemas
class SubmissionBase(BaseModel):
    studentId: int
    classId: int
    assignmentId: int
    submissionFile: str
    submissionType: int


class SubmissionCreate(SubmissionBase):
    pass


class Submission(SubmissionBase):
    id: int
    submittedDate: datetime.date

    class Config:
        orm_mode = True


# Grading Schemas
class GradingBase(BaseModel):
    submissionId: int
    markGiven: decimal.Decimal
    feedback: str


class GradingCreate(GradingBase):
    pass


class Grading(GradingBase):
    id: int
    gradedDate: datetime.date

    class Config:
        orm_mode = True


class userReturn(BaseModel):
    result: str
    data: User


class result(BaseModel):
    result: str
    message: str
