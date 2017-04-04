package com.hzxm.easyloan.presenter.implPresenter;

import com.hzxm.easyloan.api.ApiManager;
import com.hzxm.easyloan.model.BaseModel;
import com.hzxm.easyloan.model.mine.EducationModel;
import com.hzxm.easyloan.presenter.IEducation;
import com.hzxm.easyloan.presenter.implView.IEducationView;
import com.lmz.baselibrary.present.implPresenter.BasePresenterImpl;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 作者：LMZ on 2017/1/19 0019 17:20
 */
public class EducationPresenter extends BasePresenterImpl implements IEducation {
    private IEducationView mIEducationView;

    public EducationPresenter(IEducationView mIEducationView) {
        this.mIEducationView = mIEducationView;
    }

    /**
     * 获取学信网信息
     *
     * @param uid
     * @param username
     * @param password
     */
    @Override
    public void xuexinCode(int uid, String username, String password) {
        Subscription s = ApiManager.getInstance().getLoanApiService().education(System.currentTimeMillis() + "", "75b7058a174344381b5b4d80657f4861",
                uid + "", username, password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIEducationView.showProgressDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EducationModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIEducationView.hiteProgressDialog();
                        mIEducationView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(EducationModel educationModel) {
                        mIEducationView.hiteProgressDialog();
                        if (educationModel.getCode() == 0) {
                            mIEducationView.statusSuccess(educationModel.getData().getData());
                        } else if (educationModel.getData().getState().equals("9")) {
                            mIEducationView.statusFaild(educationModel.getMsg());
                        } else {
                            mIEducationView.showError(educationModel.getMsg());
                        }
                    }
                });
        addSubscription(s);
    }

//    /**
//     * 获取学信网状态
//     *
//     * @param uid
//     * @param token
//     */
//    @Override
//    public void xuexinStatus(int uid, String token) {
//        Subscription s = ApiManager.getInstance().getLoanApiService().educationStatus(System.currentTimeMillis() + "", "fa949ed81c69a45e2d3705feae8428ae",
//                uid + "", token)
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        mIEducationView.showProgressDialog();
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<EducationModel>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mIEducationView.hiteProgressDialog();
//                        mIEducationView.statusFaild(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(EducationModel educationModel) {
//                        mIEducationView.hiteProgressDialog();
//                        if (educationModel.getCode() == 0) {
//                            mIEducationView.statusSuccess(educationModel.getData());
//                        } else {
//                            mIEducationView.statusFaild(educationModel.getMsg());
//                        }
//                    }
//                });
//        addSubscription(s);
//    }

    @Override
    public void xuexinResult(int uid, String token, String username, String password) {
        Subscription s = ApiManager.getInstance().getLoanApiService().educationResult(System.currentTimeMillis() + "", "d07a4cd4c49d45e1487fe817de8f4fa3",
                uid + "", token, username, password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIEducationView.showProgressDialog();
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
                        mIEducationView.hiteProgressDialog();
                        mIEducationView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseModel baseModel) {
                        mIEducationView.hiteProgressDialog();
                        if (baseModel.getCode() == 0) {
                            mIEducationView.resultSuccess();
                        } else {
                            mIEducationView.showError(baseModel.getMsg());
                        }
                    }
                });
        addSubscription(s);
    }
}
