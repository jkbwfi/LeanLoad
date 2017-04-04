package com.hzxm.easyloan.presenter.implView;

import com.lmz.baselibrary.present.implView.IBasePresenter;

/**
 * 作者：LMZ on 2016/12/26 0026 16:40
 */
public interface IDocUploadView extends IBasePresenter {
    void uploadSuccess(String path, int type);

    void uploadPrograss(long hasWrittenLen, long totalLen, int type);

    void showSuccess();

    void showPicError(String msg, int mtype);
}
