package com.hongguo.common.eventbus;

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
