package com.pao.laboratory03.bonus.exception;

import com.pao.laboratory03.bonus.model.Status;

public class InvalidTransitionException extends RuntimeException {
    public InvalidTransitionException(Status fromStatus, Status toStatus) {
        super("Nu se poate trece din " + fromStatus + "în" + toStatus);
    }
}
