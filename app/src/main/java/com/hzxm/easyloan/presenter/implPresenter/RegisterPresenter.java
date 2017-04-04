package com.hzxm.easyloan.presenter.implPresenter;

import com.hzxm.easyloan.api.ApiManager;
import com.hzxm.easyloan.model.BaseModel;
import com.hzxm.easyloan.model.RegisterModel;
import com.hzxm.easyloan.presenter.IgetVerification;
import com.hzxm.easyloan.presenter.implView.IRegisterView;
import com.lmz.baselibrary.present.implPresenter.BasePresenterImpl;
import com.lmz.baselibrary.util.LogUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 作者：LMZ on 2016/12/24 0024 14:43
 * 注册
 */
public class RegisterPresenter extends BasePresenterImpl implements IgetVerification {
    private IRegisterView mIVerificationView;


    public RegisterPresenter(IRegisterView mIVerificationView) {
        this.mIVerificationView = mIVerificationView;
    }

    @Override
    public void getVerification(String phone, String type) {
        Subscription s = ApiManager.getInstance().getLoanApiService()
                .getVerication(System.currentTimeMillis() + "", "c5ea4773f8c6122731ef99bcf1b960a8", phone, type)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIVerificationView.showProgressDialog();
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
                        mIVerificationView.hiteProgressDialog();
                        mIVerificationView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseModel baseModel) {
                        mIVerificationView.hiteProgressDialog();
                        LogUtil.d(baseModel.getCode() + "");
                        if (baseModel.getCode() == 0) {
                            mIVerificationView.showSuccess();
                        } else {
                            mIVerificationView.showError(baseModel.getMsg());
                        }
                    }
                });
        addSubscription(s);
    }

    /**
     * 注册
     *
     * @param phone
     * @param psw
     * @param repeatpwd
     * @param checkcode
     * @param invite_code
     */
    @Override
    public void register(String phone, String psw, String repeatpwd, String checkcode, String invite_code) {
        Subscription s = ApiManager.getInstance().getLoanApiService().register(System.currentTimeMillis() + "",
                "3ce6eb9c371cba63c759bad0e104f7f2", phone, psw, repeatpwd, checkcode, invite_code)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIVerificationView.showProgressDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegisterModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIVerificationView.hiteProgressDialog();
                        mIVerificationView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(RegisterModel registerModel) {
                        mIVerificationView.hiteProgressDialog();
                        if (registerModel.getCode() == 0) {
                            mIVerificationView.registerData(registerModel);
                        } else {
                            mIVerificationView.showError(registerModel.getMsg());
                        }
                    }
                });
        addSubscription(s);
    }
}
