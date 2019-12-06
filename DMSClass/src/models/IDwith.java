package models;

import java.io.Serializable;

// 특정 개체와 학번을 같이 전달해야 할 경우에 사용
public final class IDwith<T extends Serializable> implements Serializable{
    public final String id;
    public final T obj;

    public IDwith(String id, T obj){
        this.id = id;
        this.obj = obj;
    }
}