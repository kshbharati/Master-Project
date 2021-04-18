from typing import List, Optional
from pydantic import BaseModel


# Category Schema
class CategoryBase(BaseModel):
    categoryname: str
    categorydetail: Optional[str] = None


class CategoryCreate(CategoryBase):
    pass


class Category(CategoryBase):
    id: int

    class Config:
        orm_mode = True


# User Schema
class UserBase(BaseModel):
    email: str


class UserCreate(UserBase):
    password: str


class User(UserBase):
    id: int

    # sessionid: str

    class Config:
        orm_mode = True


# UserDetail Schema
class UserDetailBase(BaseModel):
    firstname: str
    lastname: str
    phonenumber: Optional[str] = None


class UserDetailCreate(UserDetailBase):
    pass


class UserDetail(UserDetailBase):
    id: int
    categoryid: int
    userid: int

    class Config:
        orm_mode = True
