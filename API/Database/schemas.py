import datetime
from typing import List, Optional
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
    assignmentAddedDate: datetime.datetime
    assignmentAddedBy: str
    courseId: str


class AssignmentCreate(BaseModel):
    pass


class Assignment(BaseModel):
    id: int
    assignmentStartDate: datetime.datetime
    assignmentEndDate: datetime.datetime

    class Config:
        orm_mode = True


class userReturn(BaseModel):
    result: str
    data: User
