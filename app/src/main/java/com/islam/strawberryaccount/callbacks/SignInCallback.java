package com.islam.strawberryaccount.callbacks;


import com.islam.strawberryaccount.pojo.User;

public interface SignInCallback {

    void verifyPhoneNumber(String phoneNumber);

    void onCodeSubmit(String code);

    void submitUserInfo(User user);

}
