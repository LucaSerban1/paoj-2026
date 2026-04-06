package com.pao.laboratory07.exercise1;

import java.util.List;

public enum OrderState {
    PLACED,PROCESSED,SHIPPED,DELIVERED,CANCELED;
    private static final OrderState[] vals= values();
    public OrderState next(){
        return vals[(this.ordinal()+1)%vals.length];
    }
    public OrderState undo(List<OrderState> stateLog){
        stateLog.removeLast();
        return stateLog.getLast();
    }
    public OrderState cancel(){
        return CANCELED;
    }
}