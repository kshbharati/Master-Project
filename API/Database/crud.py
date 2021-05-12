from sqlalchemy.orm import Session

from . import models, schemas
import hashlib


###Hash Function
def hashstr(text: str):
    return hashlib.md5(text.encode()).hexdigest()


def verify_user(db: Session, email: str, password: str):
    return db.query(models.User).filter(models.User.email == email & models.User.password)


"""Query Functions"""


def get_user(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()


def get_user_by_email(db: Session, email: str):
    query = db.query(models.User)
    return query.filter(models.User.email == email).first()


def get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.User).offset(skip).limit(limit).all()


def get_user_details(db: Session, userid: int):
    model=db.query(models.UserDetail).filter(models.UserDetail.userId == userid).first()

    return model;

def get_category(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Category).offset(skip).limit(limit).all()


def get_courselist(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Course).offset(skip).limit(limit).all()


def get_assigned_courses_for_student(db: Session, studentId: int, skip: int = 0, limit: int = 10):
    model=db.query(models.AssignedCourse).filter(models.AssignedCourse.student == studentId).all()
    #print(model)
    return model

"""Insert Functions"""


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


def assign_course(db: Session, asscourse: schemas.AssignedCourse):
    db_item = models.AssignedCourse(**asscourse.dict())
    print(db_item)
    db.add(db_item)
    db.commit()
    db.refresh(db_item)
    return db_item
