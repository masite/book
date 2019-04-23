package com.hongguo.read.eventbus;

public class BackgroundLoginEvent {

    public LoginCode mLoginCode;

    public enum LoginCode{
        SUCCESS,
        EXIT,
        FAILURE
    }

    public BackgroundLoginEvent(LoginCode loginCode) {
        mLoginCode = loginCode;
    }
}
