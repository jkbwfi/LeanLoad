package com.hzxm.easyloan.presenter;

/**
 * 作者：LMZ on 2017/1/19 0019 17:17
 */
public interface IEducation {
    void xuexinCode(int uid, String username, String password);

    void xuexinResult(int uid, String token, String username, String password);
}
