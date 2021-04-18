from sqlalchemy import Boolean, Column, ForeignKey, Integer, String
from sqlalchemy.orm import relationship

from .database import Base


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String(50), unique=True, index=True)
    password = Column(String(50))
    #sessionid = Column(String(20), default=True)

    #userdetail = relationship("UserDetail")


class UserDetail(Base):
    __tablename__ = "userdetail"

    id = Column(Integer, primary_key=True, index=True)
    firstname = Column(String(50), index=True)
    lastname = Column(String(50), index=True)
    phonenumber = Column(String(20), index=True)
    categoryid = Column(Integer, ForeignKey("category.id"),nullable=True)
    userid = Column(Integer, ForeignKey("users.id"),nullable=True)

    user = relationship("User")
    category = relationship("Category")


class Category(Base):
    __tablename__ = "category"

    id = Column(Integer, primary_key=True, index=True)
    categoryname = Column(String(50), index=True)
    categorydetail = Column(String(50), index=True)
