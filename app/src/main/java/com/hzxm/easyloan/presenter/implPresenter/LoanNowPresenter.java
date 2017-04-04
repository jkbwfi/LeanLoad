package com.hzxm.easyloan.presenter.implPresenter;

import com.hzxm.easyloan.api.ApiManager;
import com.hzxm.easyloan.model.BaseModel;
import com.hzxm.easyloan.presenter.ILoanNow;
import com.hzxm.easyloan.presenter.implView.ILoanNowView;
import com.lmz.baselibrary.present.implPresenter.BasePresenterImpl;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 作者：LMZ on 2017/1/11 0011 14:20
 */
public class LoanNowPresenter extends BasePresenterImpl implements ILoanNow {
    private ILoanNowView mILoanNowView;

    public LoanNowPresenter(ILoanNowView mILoanNowView) {
        this.mILoanNowView = mILoanNowView;
    }

    /**
     * 借款
     *
     * @param uid
     * @param borrow_money
     * @param rate_money
     * @param checked_money
     * @param cash_money
     * @param return_type
     * @param normal_salary
     * @param normal_check
     * @param normal_crash
     * @param depart_salary
     * @param depart_check
     * @param depart_crash
     * @param depart_money
     * @param repay_number
     * @param borrow_time
     */
    @Override
    public void borrow_money(String uid, String borrow_money, String rate_money, String checked_money, String cash_money,
                             int return_type,
                             String normal_salary, String normal_check, String normal_crash,
                             String depart_salary, String depart_check, String depart_crash, String depart_money, String card_number,
                             String repay_number, String borrow_time) {
        Subscription s = ApiManager.getInstance().getLoanApiService().borrowMoney(System.currentTimeMillis() + "", "ee623eb637caceb9c13e4937a47874fa"
                , uid, borrow_money, rate_money, checked_money, cash_money, return_type,
                normal_salary, normal_check, normal_crash,
                depart_salary, depart_check, depart_crash, depart_money, card_number,
                repay_number, borrow_time)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mILoanNowView.showProgressDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mILoanNowView.showError(e.getMessage());
                        mILoanNowView.hiteProgressDialog();
                    }

                    @Override
                    public void onNext(BaseModel loanNowModel) {
                        mILoanNowView.hiteProgressDialog();
                        if (loanNowModel.getCode() == 0) {
                            mILoanNowView.loanSuccess();
                        } else {
                            mILoanNowView.showError(loanNowModel.getMsg());
                        }
                    }
                });
        addSubscription(s);
    }
}
