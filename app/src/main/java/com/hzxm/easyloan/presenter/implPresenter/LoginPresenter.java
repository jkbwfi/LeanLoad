package com.hzxm.easyloan.presenter.implPresenter;

import com.hzxm.easyloan.api.ApiManager;
import com.hzxm.easyloan.model.LoginModel;
import com.hzxm.easyloan.presenter.ILogin;
import com.hzxm.easyloan.presenter.implView.ILoginView;
import com.lmz.baselibrary.present.implPresenter.BasePresenterImpl;
import com.lmz.baselibrary.util.LogUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 作者：LMZ on 2016/12/26 0026 14:29
 */
public class LoginPresenter extends BasePresenterImpl implements ILogin {
    private ILoginView mILoginView;

    public LoginPresenter(ILoginView mILoginView) {
        this.mILoginView = mILoginView;
    }

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public void login(String username, String password) {
        Subscription s = ApiManager.getInstance().getLoanApiService().ulogin(System.currentTimeMillis() + "",
                "3c437a1b469dc67c1e1a804b3a00270b", username, password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mILoginView.showProgressDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mILoginView.showError(e.getMessage());
                        mILoginView.hiteProgressDialog();
                    }

                    @Override
                    public void onNext(LoginModel loginModel) {
                        mILoginView.hiteProgressDialog();
                        int code = loginModel.getCode();
                        LogUtil.e(code);
                        if (code == 0) {
                            mILoginView.loginSuccess(loginModel);
                        } else {
                            mILoginView.showError(loginModel.getMsg());
                        }
                    }
                });
        addSubscription(s);
    }
}
