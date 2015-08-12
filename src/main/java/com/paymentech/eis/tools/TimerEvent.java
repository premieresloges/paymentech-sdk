package com.paymentech.eis.tools;

import java.util.*;

public class TimerEvent extends EventObject{
    int count = 0;

    public TimerEvent(Object obj){
        super(obj);
    }

    public int getCount(){
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }
}
