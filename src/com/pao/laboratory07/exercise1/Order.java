package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private OrderState state;
    private List<OrderState> stateLog;
    public Order(OrderState initialState) {
        this.state = initialState;
        stateLog=new ArrayList<>();
        stateLog.add(state);
    }
    public void nextState(){
        if(state.ordinal()>2){
            throw new OrderIsAlreadyFinalException();
        }else{
            state=state.next();
            stateLog.add(state);
            System.out.println("Order state updated to: "+state.name());
        }
    }
    public void cancel(){
        if(state.ordinal()>2){
            throw new CannotCancelFinalOrderException();
        }else{
            state=state.cancel();
            stateLog.add(state);
            System.out.println("Order has been canceled.");

        }
    }
    public void undoState(){
        if(state==OrderState.PLACED){
            throw new CannotRevertInitialOrderStateException();
        }
        if(stateLog.size()==1){
            System.out.println(" Nu există stare anterioară pentru undo.");
        }else{
            state=state.undo(stateLog);
            System.out.println("Order state reverted to: "+state.name());
        }
    }
}