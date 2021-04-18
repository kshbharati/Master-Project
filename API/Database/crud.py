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


def get_user_details(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.UserDetail).offset(skip).limit(limit).all()


def get_category(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Category).offset(skip).limit(limit).all()


"""Insert Functions"""


def create_user(db: Session, user: schemas.UserCreate):
    print("Create User")
    fake_hashed_password = hashstr(user.password)
    db_user = models.User(email=user.email, password=fake_hashed_password)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user


def create_user_detail(db: Session, userdetail: schemas.UserDetailCreate, userid: int, category_id: int):
    db_item = models.UserDetail(**userdetail.dict(), userid=userid, categoryid=category_id)
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
