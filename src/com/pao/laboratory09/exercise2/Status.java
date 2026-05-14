package com.pao.laboratory09.exercise2;

enum Status {
    PENDING(0), PROCESSED(1), REJECTED(2);
    final int value;
    Status(int value) { this.value = value; }

    public static Status fromInt(int v) {
        for (Status s : values()) if (s.value == v) return s;
        return PENDING;
    }
}