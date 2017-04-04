package com.hzxm.easyloan.presenter.implPresenter;

import android.content.Context;

import com.hzxm.easyloan.adapter.mine.SocialCityAdapter;
import com.hzxm.easyloan.api.ApiManager;
import com.hzxm.easyloan.model.mine.SocialCityModel;
import com.hzxm.easyloan.presenter.ISocialCity;
import com.hzxm.easyloan.presenter.implView.ISocialCityView;
import com.lmz.baselibrary.present.implPresenter.BasePresenterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 作者：LMZ on 2017/1/23 0023 14:34
 */
public class SocialCityPresenter extends BasePresenterImpl implements ISocialCity {
    private ISocialCityView mISocialCityView;
    private SocialCityAdapter mSocialCityAdapter;


    private List<SocialCityModel.DataEntityX.DataEntity> dataEntities = new ArrayList<>();

    public SocialCityPresenter(Context context, ISocialCityView mISocialCityView) {
        this.mISocialCityView = mISocialCityView;
        mSocialCityAdapter = new SocialCityAdapter(context, dataEntities);
    }

    @Override

    public void socalArea(int uid) {
        Subscription s = ApiManager.getInstance().getLoanApiService().socialArea(System.currentTimeMillis() + "", "4b50512c9c732419a0d992ab9cd202bc", uid + "")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mISocialCityView.showProgressDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SocialCityModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mISocialCityView.hiteProgressDialog();
                        mISocialCityView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(SocialCityModel socialCityModel) {
                        mISocialCityView.hiteProgressDialog();
                        if (socialCityModel.getCode() == 0) {
                            dataEntities = socialCityModel.getData().getData();
                            if (dataEntities.size() == 0) {
                                mISocialCityView.emptyView();
                            }
                            List<SocialCityModel.DataEntityX.DataEntity> newDatas = new ArrayList<>();
                            for (int i = 0; i < dataEntities.size(); i++) {
                                newDatas.add(new SocialCityModel.DataEntityX.DataEntity(dataEntities.get(i).getAreaName(), dataEntities.get(i).getAreaCode()));
                            }
                            Collections.sort(newDatas);
                            mSocialCityAdapter.notifyData(newDatas);
                            mSocialCityAdapter.setNewData(newDatas);
                            mISocialCityView.success();
                        } else {
                            mISocialCityView.showError(socialCityModel.getMsg());
                        }
                    }
                });
        addSubscription(s);
    }

    @Override
    public SocialCityAdapter getAdapter() {
        return mSocialCityAdapter;
    }
}
