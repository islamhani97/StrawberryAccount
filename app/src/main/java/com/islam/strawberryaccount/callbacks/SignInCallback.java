package com.islam.strawberryaccount.callbacks;


import com.islam.strawberryaccount.pojo.User;

public interface SignInCallback {

    public void verifyPhoneNumber(String phoneNumber);

    public void onCodeSubmit(String code);

    public void submitUserInfo(User user);

}
