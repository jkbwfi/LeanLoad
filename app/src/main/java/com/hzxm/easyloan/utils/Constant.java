package com.hzxm.easyloan.utils;

import com.lmz.baselibrary.util.MD5Util;

/**
 * 作者：LMZ on 2016/12/20 0020 17:30
 */
public class Constant {
    public static final String WebSite = "http://139.196.214.241:8100/api/";
    public static final String KEY_CODE = "k+_b}yC2Hx~:uZ/O=a9g-0{6^B|LhfwFlG@I?1MY";
    public static final String EasyLoan = "EasyLoan";
    public static final String IS_LOGIN = "is_login";
    public static final String UID = "uid";
    public static final String HASH_ID = "hash_id";
    public static final String PHONE_NUM = "phone_num";

    public static final String USER_NAME = "user_name";
    public static final int REQUEST_CODE_CAMERA = 1000;
    public static final int REQUEST_CODE_GALLERY = 1001;
    /**
     * 参数名字的加密
     *
     * @param str
     */
    public static String addMd5(String... str) {
        if (null != str) {
            int length = str.length;
            if ("".equals(str)) {
                return MD5Util.md5("Time" + KEY_CODE);
            } else {
                StringBuilder mStr = new StringBuilder();
                mStr.append("Time");
                for (int i = 0; i < length; i++) {
                    mStr.append(str[i]);
                }
                mStr.append(KEY_CODE);
                return MD5Util.md5(mStr.toString());
            }
        }
        return null;
    }
}
