package edu.unh.cs.cs619_2015_project2.g9.util;

import java.io.Serializable;

public class ResultWrapper<T> implements Serializable {
    private T result;

    public ResultWrapper() {
    }

    public ResultWrapper(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
