from sqlalchemy.orm import Session

from . import models, schemas
import hashlib


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
    list = []
    model = db.query(models.AssignedClass).filter(models.AssignedClass.classId == classId).all()
    for result in model:
        stu = db.query(models.Student).filter(models.Student.id == result.student).first()
        list.append(stu)
    return list


def get_assignment_for_course(db: Session, courseId: int):
    list = []
    model = db.query(models.Assignment).filter(models.Assignment.courseId == courseId).all()
    return model


def get_submission_for_assignment(db: Session, assignmentId: int):
    model = db.query(models.Submission).filter(models.Submission.assignmentId == assignmentId).all()
    return model


def get_grade_for_submission(db: Session, submissionId: int):
    model = db.query(models.Grading).filter(models.Grading.submissionId == submissionId).first()
    return model


"""Insert Functions"""


def add_submission(db: Session, submission: schemas.SubmissionCreate):
    db_item = models.Submission(**submission.dict())
    db.add(db_item)

    failed = False
    msg = {"Result": "Success"}
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = {"Result": "Failed"}
        return msg

    db.refresh(db_item)
    return msg

def add_grading(db: Session, grading: schemas.GradingCreate):
    db_item = models.Grading(**grading.dict())
    db.add(db_item)

    failed = False
    msg = {"Result": "Success"}
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = {"Result": "Failed"}
        return msg

    db.refresh(db_item)
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
    msg = {"Result": "Success"}
    try:
        db.commit()
    except Exception as e:
        print(e)
        db.rollback()
        db.flush()
        failed = True

    if failed:
        msg = {"Result": "Failed"}
        return msg

    db.refresh(db_item)
    return msg


#############################
####Verification Functions###
#############################
def check_if_category_exists(db: Session, categoryNm: str):
    return db.query(models.Category).filter(models.Category.categoryName == categoryNm).first()
