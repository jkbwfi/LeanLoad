package com.hzxm.easyloan.presenter.implView;

import com.hzxm.easyloan.adapter.chamberlain.CommonProblemAdapte;
import com.lmz.baselibrary.present.implView.IBasePresenter;

/**
 * 作者：LMZ on 2017/1/6 0006 16:22
 */
public interface IChamberlainView extends IBasePresenter {
    void getAdapter(CommonProblemAdapte commonProblemAdapte);

    void getMsg(String msg);
}
