package com.taehun.SpringFrame.MethodInjection;

public interface Command {
    void setState(Object state);
    Object execute();
}