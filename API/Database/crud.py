from datetime import datetime
from typing import List

from sqlalchemy.orm import Session

from . import models, schemas
import hashlib

successMsg = {"RESULT": "SUCCESS"}
errorMsg = {"RESULT": "FAILED"}


#####################
####Hash Function####
#####################

def hashstr(text: str):
    return hashlib.md5(text.encode()).hexdigest()


####################
###Login Function###
####################
def verify_user(db: Session, email: str, password: str):
    model = db.query(models.User)
    result = model.filter(models.User.email == email, models.User.password == hashstr(password)).first()
    if result:
        return result
    return None


#####################
###Query Functions###
#####################

def get_user(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()


def get_user_by_email(db: Session, email: str):
    query = db.query(models.User)
    return query.filter(models.User.email == email).first()


def get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.User).offset(skip).limit(limit).all()


def get_user_details(db: Session, userid: int):
    model = db.query(models.UserDetail).filter(models.UserDetail.userId == userid).first()
    if model:
        print(model.userId)
    return model


def get_category(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Category).offset(skip).limit(limit).all()


def get_courselist(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Course).offset(skip).limit(limit).all()


def get_assigned_class_for_student(db: Session, studentId: int, skip: int = 0, limit: int = 10):
    model = db.query(models.AssignedClass).filter(models.AssignedClass.student == studentId).all()

    # print(model)
    return model


def get_assigned_class_for_staff(db: Session, staffId: int):
    model = db.query(models.TeachingClass).filter(models.TeachingClass.staffTeaching == staffId).all()
    return model


def get_students_in_class(db: Session, classId: int):
    returnList = []
    model = db.query(models.AssignedClass).filter(models.AssignedClass.classId == classId).all()
    for result in model:
        stu = db.query(models.Student).filter(models.Student.id == result.student).first()
        returnList.append(stu)
    return returnList


def get_assignment_for_course(db: Session, courseId: int):
    list = []
    model = db.query(models.Assignment).filter(models.Assignment.courseId == courseId).all()
    return model


def get_submission_for_assignment(db: Session, assignmentId: int):
    model = db.query(models.Submission) \
        .filter(models.Submission.assignmentId == assignmentId) \
        .all()
    if not model:
        return [errorMsg]
    return model


def get_grade_for_submission(db: Session, submissionId: int):
    model = db.query(models.Grading) \
        .filter(models.Grading.submissionId == submissionId) \
        .first()
    if not model:
        return [errorMsg]
    return model


def get_messages_for_user(db: Session, userEmail: str):
    model = db.query(models.Message).filter((models.Message.senderEmail == userEmail) |
                                            (models.Message.receiverEmail == userEmail)).all();
    responseList = [errorMsg]
    if not model:
        return responseList
    return model


def get_attendance(db: Session, classId: int, date: datetime.date):
    model = db.query(models.Attendance).filter((models.Attendance.classId == classId) &
                                               (date == models.Attendance.date)).all()
    if not model:
        return errorMsg
    return model


def get_attendance_for_student(db: Session, studentId: id):
    model = db.query(models.Attendance).filter(models.Attendance.studentId == studentId).all()

    if not model:
        return [errorMsg]
    return model


def get_children_for_parent(db: Session, parentId: id):
    model = db.query(models.Student).filter(models.Student.parent == parentId).all()
    if not model:
        return [errorMsg]
    return model


def get_classes_for_student(db: Session, studentId: id):
    returnList = []
    model = db.query(models.AssignedClass).filter(models.AssignedClass.student == studentId).all()
    for mod in model:
        classes = db.query(models.TeachingClass) \
            .filter(models.TeachingClass.id == mod.classId).first()
        if not classes:
            continue

        returnList.append(classes)
    if not returnList:
        returnList.append(errorMsg)
    return returnList


def get_submission_and_grading_for_student(db: Session, studentId: id):
    returnList = []
    gradeList = []
    subItem = db.query(models.Submission) \
        .filter(models.Submission.studentId == studentId).all()
    if subItem:
        returnList.append(subItem)
        for sub in subItem:
            gradeItem = db.query(models.Grading) \
                .filter(models.Grading.submissionId == sub.id).first()
            if gradeItem:
                gradeList.append(gradeItem)
    if not returnList:
        return errorMsg
    if gradeList:
        returnList.append(gradeList)
    returnDict = {"SUBMISSION": subItem, "GRADES": gradeList}
    return returnDict

def get_attendance_for_student(db:Session,studentId:id):
    attenItem=db.query(models.Attendance).filter(models.Attendance.studentId==studentId).all()
    if not attenItem:
        return [errorMsg]
    return attenItem
"""Update Functions"""


def update_message_as_read(db: Session, messageId: int):
    model = db.query(models.Message).filter(models.Message.id == messageId).first();
    model.messageReadStatus = "READ"
    failed = False
    msg = successMsg
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = errorMsg
        return msg

    db.refresh(model)
    return msg


def update_grade(db: Session, gradeId: int, mark: str, remark: str):
    model = db.query(models.Grading).filter(models.Grading.id == gradeId).first()
    model.markGiven = mark
    model.feeback = remark
    model.gradedDate = datetime.utcnow()

    msg = successMsg
    failed = False;
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = errorMsg
        return msg

    db.refresh(model)
    return msg


def update_attendance(db: Session, attendance: schemas.Attendance):
    model = db.query(models.Grading).filter(models.Attendance.id == attendance.id)
    model.attendance = attendance.attendance
    model.date = attendance.date

    msg = successMsg
    failed = False;
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = errorMsg
        return msg

    db.refresh(model)
    return msg


"""Delete Submission"""


def delete_message(db: Session, messageId: int):
    model = db.query(models.Message).filter(models.Message.id == messageId).delete()

    failed = False
    msg = successMsg
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = errorMsg
        return msg
    return msg


"""Insert Functions"""


def add_attendance(db: Session, attendance: list):
    failed = False
    for attend in attendance:
        atten = schemas.AttendanceCreate(**attend)

        checkItem = db.query(models.Attendance).filter(
            (models.Attendance.studentId == atten.studentId) &
            (models.Attendance.date == atten.date) &
            (models.Attendance.classId == atten.classId)).first()
        if checkItem:
            print("HELLO")
            checkItem.attendance = atten.attendance
            try:
                db.commit()
            except Exception as e:
                print(e)
                db.rollback()
                db.flush()
                failed = True
            continue
        db_item = models.Attendance(**atten.dict())
        db.add(db_item)
        try:
            db.commit()
        except Exception as e:
            print(e)
            db.rollback()
            db.flush()
            failed = True

        db.refresh(db_item)
    if failed:
        return [errorMsg]
    return [successMsg]


def add_submission(db: Session, submission: schemas.SubmissionCreate):
    db_item = models.Submission(**submission.dict())
    db.add(db_item)

    failed = False
    msg = successMsg
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = errorMsg
        return msg

    db.refresh(db_item)
    return db_item


def add_grading(db: Session, grading: schemas.GradingCreate):
    db_item = models.Grading(**grading.dict())
    db.add(db_item)

    failed = False
    msg = successMsg
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = errorMsg
        return msg

    db.refresh(db_item)
    return successMsg


def add_message(db: Session, message: schemas.MessageCreate):
    db_message = models.Message(**message.dict())
    db.add(db_message)

    failed = False
    msg = successMsg
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = errorMsg
        return msg

    db.refresh(db_message)
    return msg


def create_user(db: Session, user: schemas.UserCreate):
    print("Create User")
    hashed_password = hashstr(user.password)
    db_user = models.User(email=user.email, category=user.category, password=hashed_password)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user


def create_user_detail(db: Session, userdetail: schemas.UserDetailCreate, userid: int):
    db_item = models.UserDetail(**userdetail.dict(), userId=userid)
    db.add(db_item)
    db.commit()
    db.refresh(db_item)
    return db_item


def create_category(db: Session, category: schemas.CategoryCreate, ):
    db_item = models.Category(**category.dict())
    db.add(db_item)
    db.commit()
    db.refresh(db_item)
    return db_item


def create_course(db: Session, course: schemas.Course):
    db_item = models.Course(**course.dict())
    db.add(db_item)
    db.commit()
    db.refresh(db_item)
    return db_item


def create_student(db: Session, student: schemas.Student):
    db_item = models.Student(**student.dict())

    db.add(db_item)
    db.commit()
    db.refresh(db_item)
    return db_item


def assign_course(db: Session, asscourse: schemas.AssignedClass):
    db_item = models.AssignedClass(**asscourse.dict())
    print(db_item)
    db.add(db_item)
    db.commit()
    db.refresh(db_item)
    return db_item


def create_teaching_class(db: Session, teachingclass: schemas.TeachingClass):
    db_item = models.TeachingClass(**teachingclass.dict())
    db.add(db_item)
    db.commit()
    db.refresh(db_item)
    return db_item


def add_assignment_for_course(db: Session, assignment: schemas.AssignedClass):
    db_item = models.Assignment(**assignment.dict())
    db.add(db_item)

    failed = False
    msg = successMsg
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = errorMsg
        return msg

    db.refresh(db_item)
    return msg


#############################
####Verification Functions###
#############################
def check_if_category_exists(db: Session, categoryNm: str):
    return db.query(models.Category).filter(models.Category.categoryName == categoryNm).first()
