package com.hzxm.easyloan.presenter;

/**
 * 作者：LMZ on 2017/1/3 0003 13:52
 */
public interface IAddBankCard {
    void getIdCard(String uid);

    void sendcode(String mobile, String checktype);

    void addBankCard(String uid, String reallyname, String id_card, String bank_number, String bank_tel, String check_code);

}
