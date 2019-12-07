package models;

import java.io.Serializable;

// 특정 개체와 학번을 같이 전달해야 할 경우에 사용
public final class Tuple<S extends Serializable, T extends Serializable> implements Serializable{
    public final S obj1;
    public final T obj2;

    public Tuple(S obj1, T obj2){
        this.obj1 = obj1;
        this.obj2 = obj2;
    }
}