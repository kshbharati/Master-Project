import datetime

from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Text, Date, DateTime
from sqlalchemy.orm import relationship

from .database import Base


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String(50), unique=True, index=True)
    password = Column(String(50))
    category = Column(Integer, ForeignKey("category.id"))
    userCreatedDate = Column(DateTime, default=datetime.datetime.utcnow)
    # sessionid = Column(String(20), default=True)

    #category=relationship("Category")
    #student=relationship("Student")
    #userDetail=relationship("UserDetail")
    def __repr__(self):
        return "<User(name='%s %s')>" % (
            self, self.email)


class UserDetail(Base):
    __tablename__ = "userDetail"

    id = Column(Integer, primary_key=True, index=True)
    userName = Column(String(100), index=True)
    phoneNumber = Column(String(20))
    userId = Column(Integer, ForeignKey("users.id"))

    def __repr__(self):
        return "<User(name='%s %s')>" % (
            self, self.userName)


class Category(Base):
    __tablename__ = "category"
    id = Column(Integer, primary_key=True, index=True)
    categoryName = Column(String(50), index=True)
    categoryDetail = Column(String(50),nullable=True)
    #user=relationship("User",back_populates="users.category")
    def __repr__(self):
        return "<User(name='%s %s')>" % (
            self, self.categoryName)


class Student(Base):
    __tablename__ = "students"
    id = Column(Integer, primary_key=True, index=True)
    studentName = Column(String(100), index=True)
    studentEnrolledDate = Column(DateTime, default=datetime.datetime.utcnow)
    parent = Column(Integer, ForeignKey("users.id"))

    #parent= relationship("User",back_populates="user.student")
    assignedClass=relationship("AssignedClass",back_populates="studentInClass")
    #assignedCourse = relationship("AssignedCourse", backref="students")
    #assignment = relationship("Assignment", backref="students")

    def __repr__(self):
        return "<User(name='%s %s')>" % (
            self, self.studentName)

class Course(Base):
    __tablename__ = "course"

    id = Column(Integer, primary_key=True, index=True)
    courseTitle = Column(String(100), index=True)
    courseDesc = Column(Text, nullable=True)

    #assignedCourse=relationship("AssignedCourse")
    #assignments = relationship("Assignment", backref="course")


class TeachingClass(Base):
    __tablename__="teachingClass"

    id=Column(Integer,primary_key=True, index=True)
    classTitle=Column(String(100),index=True)
    courseTaught=Column(Integer, ForeignKey("course.id"),nullable=True)
    staffTeaching=Column(Integer, ForeignKey("users.id"))

class AssignedClass(Base):
    __tablename__ = "AssignedClass"

    id = Column(Integer, primary_key=True, index=True)
    student = Column(Integer, ForeignKey("students.id"),nullable=True)
    classId=Column(Integer,ForeignKey("teachingClass.id"))
    courseStartDate = Column(Date)
    courseEndDate = Column(Date)

    studentInClass=relationship("Student",back_populates="assignedClass")
    Class=relationship("TeachingClass",foreign_keys=[classId])

    #course = relationship("Course",backref="course.assignedCourse")


class Assignment(Base):
    __tablename__ = "assignments"
    id = Column(Integer, primary_key=True, index=True)
    assignmentTitle = Column(String(200), index=True)
    assignmentDesc = Column(Text)
    assignmentAddedDate = Column(DateTime, default=datetime.datetime.utcnow)
    assignmentAddedBy = Column(Integer, ForeignKey("users.id"))
    course = Column(Integer, ForeignKey("course.id"))
    assignmentStartDate = Column(DateTime)
    assignmentEndDate = Column(DateTime)

   #course=relationship("Course")
    #assignmentAddedBy=relationship("User")

