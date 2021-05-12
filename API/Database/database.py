from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, session

SQLALCHEMY_DATABASE_URL = "mysql+pymysql://preuni:preuni1234@localhost/preuni?charset=utf8mb4"
# SQLALCHEMY_DATABASE_URL = "postgresql://user:password@postgresserver/db"

engine = create_engine(
    SQLALCHEMY_DATABASE_URL
    #, connect_args={"check_same_thread": False
)


SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

print("Creatiing Base")
Base = declarative_base()