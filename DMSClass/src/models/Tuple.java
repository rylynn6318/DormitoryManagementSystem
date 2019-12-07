package models;

import java.io.Serializable;

// string과 객체를 같이 보내야 하는 경우가 꽤 있어 아예 튜플로 전환
public final class Tuple<S extends Serializable, T extends Serializable> implements Serializable{
    public final S obj1;
    public final T obj2;

    public Tuple(S obj1, T obj2){
        this.obj1 = obj1;
        this.obj2 = obj2;
    }
}