package com.federation.masters.preuni.models;

public class GenericUser<T> {
    private T user;

    public T getUser() {
        return user;
    }

    public void setUser(T t) {
        this.user = t;
    }
}
