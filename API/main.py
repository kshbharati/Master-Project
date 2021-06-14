from datetime import datetime
from typing import List
from urllib.request import Request

import sqltap
from fastapi import Depends, FastAPI, HTTPException, UploadFile, File
from pydantic import BaseModel
from sqlalchemy import Sequence
from sqlalchemy.orm import Session

from Database import crud, models, schemas
from Database.database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)
app = FastAPI()


# Dependency
def get_db():
    db = SessionLocal()
    print("Getting Database")
    try:
        yield db
    finally:
        db.close()


# Fetching Endpoints

@app.get("/")
def read_root(db: Session = Depends(get_db)):
    return crud.get_assigned_class_for_student(db=db, studentId=1)
    # return {"Basic API for Pre UNI"}


#############################
####User Data Fetch Data#####
#############################
@app.get("/categories/", response_model=List[schemas.Category])
async def get_categories(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    categories = crud.get_category(db, skip=skip, limit=limit)
    return categories


# @app.get("/users/", response_model=List[schemas.User])
# def get_users(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
#    users = crud.get_users(db, skip=skip, limit=limit)
#    return users


@app.get("/users/{user_id}", response_model=schemas.User)
async def get_user(user_id: int, db: Session = Depends(get_db)):
    db_user = crud.get_user(db, user_id=user_id)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return db_user


@app.get("/user/userdetail/{userid}", response_model=schemas.UserDetail)
async def get_userdetail(userid: int, db: Session = Depends(get_db)):
    userdetail = crud.get_user_details(db, userid=userid)
    if userdetail is None:
        raise HTTPException(status_code=404, detail="UserDetail not found")
    return userdetail

@app.get("/courselist/", response_model=List[schemas.Course])
async def get_courseList(db: Session = Depends(get_db)):
    model = db.query(models.Course).all()
    if model is None:
        raise HTTPException(status_code=404, detail="Courses not Found")
    return model;


########################
###Staff Data Fetch#####
########################
@app.get("/user/staff/assigned_classes/{staffid}", response_model=List[schemas.TeachingClass])
async def get_assigned_classes_for_staff(staffid: int, db: Session = Depends(get_db)):
    result = crud.get_assigned_class_for_staff(db, staffid)
    if result is None:
        raise HTTPException(status_code=404, detail="Classes Not Found")
    return result


########################
###Student Data Fetch###
########################
@app.get("/get_students_in_class/{classId}", response_model=List)
async def get_assigned_class_for_student(classId: int, db: Session = Depends(get_db)):
    result = crud.get_students_in_class(db=db, classId=classId)
    if result is None:
        raise HTTPException(status_code=404, detail="Course for Student not Found")
    return result


@app.get("/get_children/{parentId}")
async def get_children_for_parent(parentId: int, db: Session = Depends(get_db)):
    return crud.get_children_for_parent(db, parentId=parentId)


@app.get("/get_classes_for_student/{studentId}")
async def get_classes_for_student(studentId: int, db: Session = Depends(get_db)):
    return crud.get_classes_for_student(db, studentId=studentId)


@app.get("/get_assignment/{courseId}", response_model=List)
async def get_assignment_for_course(courseId: int, db: Session = Depends(get_db)):
    result = crud.get_assignment_for_course(db, courseId=courseId)
    if (result is None):
        return {{"Result": "Failed"}}
    return result


@app.get("/get_submission_for_student/{studentId}",response_model=dict)
async def get_submission_for_student(studentId: int, db: Session = Depends(get_db)):
    return crud.get_submission_and_grading_for_student(db, studentId=studentId)


@app.get("/get_attendance_for_student/{studentId}",response_model=list)
async def get_submission_for_student(studentId: int, db: Session = Depends(get_db)):
    return crud.get_attendance_for_student(db, studentId=studentId)


##########################
####Creating Endpoints####
##########################
@app.post("/submission/add")
async def add_submission(submission: schemas.SubmissionCreate, db: Session = Depends(get_db)):
    return crud.add_submission(db, submission=submission)


@app.post("/grading/add")
async def add_grade(grading: schemas.GradingCreate, db: Session = Depends(get_db)):
    return crud.add_grading(db, grading=grading)


@app.post("/users/createuserdetail/{userid}", response_model=schemas.UserDetail)
async def create_user_detail(userid: int, userdetail: schemas.UserDetailCreate, db: Session = Depends(get_db)):
    print(userid)
    return crud.create_user_detail(db=db, userdetail=userdetail, userid=userid)


@app.post("/create/category", response_model=schemas.Category)
async def create_category(category: schemas.CategoryCreate, db: Session = Depends(get_db)):
    if crud.check_if_category_exists(db, categoryNm=category.categoryName):
        raise HTTPException(status_code=400, detail="Category Already Exists")
    return crud.create_category(db=db, category=category)


@app.post("/create/user", response_model=schemas.User)
async def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    # print(user.email,user.password)
    db_user = crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email Already Registered")
    return crud.create_user(db=db, user=user)


@app.post("/create/course", response_model=schemas.Course)
async def create_course(course: schemas.CourseCreate, db: Session = Depends(get_db)):
    return crud.create_course(db=db, course=course)


@app.post("/create/student", response_model=schemas.Student)
async def create_student(student: schemas.StudentCreate, db: Session = Depends(get_db)):
    return crud.create_student(db=db, student=student)


@app.post("/teachingclass/assign", response_model=schemas.AssignedClass)
async def assign_course(asscourse: schemas.AssignedClassCreate, db: Session = Depends(get_db)):
    print(asscourse.dict())
    return crud.assign_course(db=db, asscourse=asscourse)


@app.post("/create/teaching_class", response_model=schemas.TeachingClass)
async def create_teaching_class(tchclass: schemas.TeachingClassCreate, db: Session = Depends(get_db)):
    return crud.create_teaching_class(db, teachingclass=tchclass)


@app.post("/assignment/add")
async def add_assignment(assignment: schemas.AssignmentCreate, db: Session = Depends(get_db)):
    print(assignment)
    return crud.add_assignment_for_course(db, assignment=assignment)


@app.post("/verify_user/{username}/{password}", response_model=schemas.User)
async def verify_user(username: str, password: str, db: Session = Depends(get_db)):
    result = crud.verify_user(db, username, password)
    if result is None:
        raise HTTPException(status_code=404, detaipl="User Not Found")

    return result


@app.post("/uploadfile/")
async def create_upload_file(file: UploadFile = File(...)):
    return {"filename": file.filename}


"""MESSAGE CRUD STATEMENTS"""


@app.post("/sendmail")
def add_message(message: schemas.MessageCreate, db: Session = Depends(get_db)):
    return crud.add_message(db, message=message)


@app.get("/get_mail/{userEmail}", response_model=List)
async def get_message(userEmail: str, db: Session = Depends(get_db)):
    return crud.get_messages_for_user(db, userEmail=userEmail)


@app.post("/updateEmailAsRead/{messageId}")
async def update_message_as_read(messageId: int, db: Session = Depends(get_db)):
    return crud.update_message_as_read(db, messageId=messageId)


@app.delete("/deleteMail/{messageId}")
async def delete_message(messageId: int, db: Session = Depends(get_db)):
    return crud.delete_message(db, messageId=messageId)


#########################
##Submission ENDPOINTS###
#########################
@app.get("/submissions/{assignmentId}", response_model=List)
async def get_submission(assignmentId: int, db: Session = Depends(get_db)):
    return crud.get_submission_for_assignment(db, assignmentId=assignmentId)


@app.post("/add_submission/")
async def add_submission(submission: schemas.SubmissionCreate, db: Session = Depends(get_db)):
    return crud.add_submission(db, submission=submission)


##########################
###Grading Endpoints######
##########################
@app.get("/get_grade/{submissionId}")
async def get_grade_for_submission(submissionId: int, db: Session = Depends(get_db)):
    return crud.get_grade_for_submission(db, submissionId=submissionId)


@app.post("/add_grade/")
async def add_grade_for_submission(grade: schemas.GradingCreate, db: Session = Depends(get_db)):
    return crud.add_grading(db, grading=grade)


@app.post("/update_grade/{gradeId}/{mark}/{remark}")
async def update_grade(gradeId: int, mark: str, remark: str, db: Session = Depends(get_db)):
    return crud.update_grade(db, gradeId=gradeId, mark=mark, remark=remark)


############################
####Attendance Endpoints####
############################
@app.post("/add_attendance/")
async def add_attendance(attendance: list, db: Session = Depends(get_db)):
    return crud.add_attendance(db, attendance=attendance)


@app.get("/get_attendance/{classId}/{date}")
async def get_attendence_for_class_date(classId: int, date: str, db: Session = Depends(get_db)):
    return crud.get_attendance(db, classId=classId, date=date)


@app.get("/get_attendance/{studentId}")
async def get_attendance_for_student(studentId: int, db: Session = Depends(get_db)):
    return crud.get_attendance_for_student(db, studentId=studentId)


#########################
##
@app.middleware("http")
async def add_sql_tap(request: Request, call_next):
    profiler = sqltap.start()
    response = await call_next(request)
    statistics = profiler.collect()
    sqltap.report(statistics, "report.txt", report_format="text")
    return response
