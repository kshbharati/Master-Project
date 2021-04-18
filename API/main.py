from typing import List
import sys

from fastapi import Depends, FastAPI, HTTPException
from sqlalchemy import Sequence
from sqlalchemy.orm import Session

from Database import crud, models, schemas
from Database.database import SessionLocal, engine

# sys.setrecursionlimit(10000)

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


@app.get("/categories/", response_model=List[schemas.Category])
def get_categories(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    categories = crud.get_category(db, skip=skip, limit=limit)
    return categories


@app.get("/users/", response_model=List[schemas.User])
def get_users(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    users = crud.get_users(db, skip=skip, limit=limit)
    return users


@app.get("/users/{user_id}", response_model=schemas.User)
def get_user(user_id: int, db: Session = Depends(get_db)):
    db_user = crud.get_user(db, user_id=user_id)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return db_user


@app.get("/user/userdetail/{userid}", response_model=schemas.UserDetail)
def get_userdetail(userid: int, skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    userdetail = crud.get_user_details(db, skip=skip, userid=userid)
    return userdetail


@app.post("/users/createuserdetail/{userid}", response_model=schemas.UserDetail)
def create_user_detail(userid: int, userdetail: schemas.UserDetailCreate, db: Session = Depends(get_db)):
    print(userid)
    return crud.create_user_detail(db=db, userdetail=userdetail, userid=userid, category_id=1)


@app.post("/category/create", response_model=schemas.Category)
def create_category(category: schemas.CategoryCreate, db: Session = Depends(get_db)):
    return crud.create_category(db=db, category=category)


@app.post("/user/create", response_model=schemas.User)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    # print(user.email,user.password)
    db_user = crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email Already Registered")
    return crud.create_user(db=db, user=user)


@app.get("/")
def read_root():
    return {"Basic API for Pre UNI"}
