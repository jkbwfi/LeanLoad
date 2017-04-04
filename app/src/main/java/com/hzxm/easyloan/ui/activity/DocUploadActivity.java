package com.hzxm.easyloan.ui.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hzxm.easyloan.R;
import com.hzxm.easyloan.model.mine.BaseCertificationModel;
import com.hzxm.easyloan.presenter.implPresenter.DocUpLoadPresenter;
import com.hzxm.easyloan.presenter.implView.IDocUploadView;
import com.hzxm.easyloan.utils.Constant;
import com.lmz.baselibrary.listener.IPermissionResultListener;
import com.lmz.baselibrary.ui.BaseActivity;
import com.lmz.baselibrary.util.AppActivityManager;
import com.lmz.baselibrary.util.LogUtil;
import com.lmz.baselibrary.util.SharedPreferencesUtil;
import com.lmz.baselibrary.util.glide.GliderHelper;
import com.lmz.baselibrary.widget.TitleLayout;

import java.io.File;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 第一次证件上传界面
 */
public class DocUploadActivity extends BaseActivity implements
        IDocUploadView,
        IPermissionResultListener,
        GalleryFinal.OnHanlderResultCallback {

    //显示的要上传的图片1
    @BindView(R.id.iv_updata1)
    ImageView ivUpdata1;
    //上传完成图标
    @BindView(R.id.iv_compilete1)
    ImageView ivCompilete1;
    //上传完成文字
    @BindView(R.id.tv_compilete1)
    TextView tvCompilete1;
    //上传中文字
    @BindView(R.id.tv_uploading1)
    TextView tvUploading1;
    //上传中pb
    @BindView(R.id.pb_uploading1)
    ProgressBar pbUploading1;

    //点击上传第一个图片
    @BindView(R.id.rl_updata1)
    RelativeLayout rlUpdata1;

    @BindView(R.id.iv_updata2)
    ImageView ivUpdata2;
    @BindView(R.id.iv_compilete2)
    ImageView ivCompilete2;
    @BindView(R.id.tv_compilete2)
    TextView tvCompilete2;
    @BindView(R.id.tv_uploading2)
    TextView tvUploading2;
    @BindView(R.id.pb_uploading2)
    ProgressBar pbUploading2;
    @BindView(R.id.rl_updata2)
    RelativeLayout rlUpdata2;
    @BindView(R.id.iv_updata3)
    ImageView ivUpdata3;
    @BindView(R.id.iv_compilete3)
    ImageView ivCompilete3;
    @BindView(R.id.tv_compilete3)
    TextView tvCompilete3;
    @BindView(R.id.tv_uploading3)
    TextView tvUploading3;
    @BindView(R.id.pb_uploading3)
    ProgressBar pbUploading3;
    @BindView(R.id.rl_updata3)
    RelativeLayout rlUpdata3;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.activity_doc_upload)
    LinearLayout activityDocUpload;
    @BindView(R.id.rl_title_num)
    RelativeLayout mTitleNum;
    //完成
    @BindString(R.string.complete)
    String complete;
    //头部警告栏
    @BindView(R.id.tv_remind)
    TextView tvRemind;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.ll_top_remind)
    LinearLayout llTopRemind;

    @BindString(R.string.input_upload_card)
    String mTopReminds;
    @BindString(R.string.pass_upload)
    String mPassUpload;

    //标题栏
    @BindView(R.id.title)
    TitleLayout title;

    //选择上传图片的顺序 左面第一个为1
    private int type = 1;
    private int finalType = 1;

    //存储上传后图片的地址 正面照  反面照   手持照
    private String card_img_face = "", card_img_back = "", card_img_all = "";
    DocUpLoadPresenter mDocUpLoadPresenter;

    private int uid;
    private String hashid;
    private DocumentPopupWindow popupWindow;

    private int isFromBaseCertifiActivity;

    private boolean isPassUpLoad = false;

    private BaseCertificationModel.DataEntity.ZhenjianEntity mZhengjianEntity;

    @Override
    protected void initConvetView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_doc_upload);
    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        uid = SharedPreferencesUtil.getInstance(getApplicationContext()).getInt(Constant.UID, -1);
        hashid = SharedPreferencesUtil.getInstance(getApplicationContext()).getString(Constant.HASH_ID, "");
        try {
            isFromBaseCertifiActivity = getIntent().getExtras().getInt("isBaseCertifiActivity", -1);
            mZhengjianEntity = getIntent().getExtras().getParcelable("zhengjian");
            card_img_back = mZhengjianEntity.getCard_img_back();
            card_img_face = mZhengjianEntity.getCard_img_face();
            card_img_all = mZhengjianEntity.getCard_img_all();
            GliderHelper.loadImage(card_img_back, ivUpdata1, null);
            GliderHelper.loadImage(card_img_face, ivUpdata2, null);
            GliderHelper.loadImage(card_img_all, ivUpdata3, null);
            if (mZhengjianEntity.getStatus() == 1) {
                isPassUpLoad = true;
                btnNext.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            LogUtil.e(e.toString());
        }
        if (isFromBaseCertifiActivity == 1) {
            llTopRemind.setVisibility(View.VISIBLE);
            if (isPassUpLoad) {
                tvRemind.setText(mPassUpload);
            } else {
                tvRemind.setText(mTopReminds);
            }
            title.setWhiteStyle(this);
            SetStatusBarColor(R.color.transparent_white);
            mTitleNum.setVisibility(View.GONE);
            btnNext.setText(complete);
        }
    }

    @Override
    protected void initData() {
        mDocUpLoadPresenter = new DocUpLoadPresenter(this);

    }

    /**
     * 关闭头部警告栏
     */
    @OnClick(R.id.iv_delete)
    public void closeTop() {
        llTopRemind.setVisibility(View.GONE);
    }

    /**
     * 下一步
     */
    @OnClick(R.id.btn_next)
    public void nextBtn() {
        if ("".equals(card_img_back)) {
            showToast("请上传身份证反面照", Toast.LENGTH_SHORT);
            return;
        }
        if ("".equals(card_img_face)) {
            showToast("请上传身份证正面照", Toast.LENGTH_SHORT);
            return;
        }
        if ("".equals(card_img_all)) {
            showToast("请上传手持身份证照", Toast.LENGTH_SHORT);
            return;
        }
        mDocUpLoadPresenter.baseinfoImg(uid + "", card_img_face, card_img_back, card_img_all);
    }

    /**
     * 上传第一张图片
     */
    @OnClick({R.id.rl_updata1, R.id.rl_updata2, R.id.rl_updata3})
    public void upLoadPic1(RelativeLayout rl) {
        if (isPassUpLoad) {
            return;
        }
        switch (rl.getId()) {
            case R.id.rl_updata1:
                type = 1;
                permissionRequest("是否允许凡易贷打开相机",
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        101, DocUploadActivity.this);
                break;
            case R.id.rl_updata2:
                type = 2;
                permissionRequest("是否允许凡易贷打开相机",
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        102, DocUploadActivity.this);
                break;
            case R.id.rl_updata3:
                type = 3;
                permissionRequest("是否允许凡易贷打开相机",
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        103, DocUploadActivity.this);
                break;
        }
    }


    /**
     * 上传图片成功
     *
     * @param path 图片的地址
     */
    @Override
    public void uploadSuccess(String path, int mtype) {
        LogUtil.e("上传图片成功-->" + path);
        switch (mtype) {
            case 1:
                card_img_back = path;
                ivCompilete1.setVisibility(View.VISIBLE);
                tvCompilete1.setVisibility(View.VISIBLE);
                tvCompilete1.setText(getString(R.string.upload_complete));
                pbUploading1.setVisibility(View.GONE);
                tvUploading1.setVisibility(View.GONE);
                break;
            case 2:
                card_img_face = path;
                ivCompilete2.setVisibility(View.VISIBLE);
                tvCompilete2.setVisibility(View.VISIBLE);
                tvCompilete2.setText(getString(R.string.upload_complete));
                pbUploading2.setVisibility(View.GONE);
                tvUploading2.setVisibility(View.GONE);
                break;
            case 3:
                card_img_all = path;
                ivCompilete3.setVisibility(View.VISIBLE);
                tvCompilete3.setVisibility(View.VISIBLE);
                tvCompilete3.setText(getString(R.string.upload_complete));
                pbUploading3.setVisibility(View.GONE);
                tvUploading3.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 当用户点击再次上传时  将pv和tv状态还原
     */
    public void redictionStatus() {
        switch (finalType) {
            case 1:
                ivCompilete1.setImageResource(R.drawable.updata_complete);
                ivCompilete1.setVisibility(View.GONE);
                tvCompilete1.setVisibility(View.GONE);
                pbUploading1.setVisibility(View.VISIBLE);
                tvUploading1.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivCompilete2.setImageResource(R.drawable.updata_complete);
                ivCompilete2.setVisibility(View.GONE);
                tvCompilete2.setVisibility(View.GONE);
                pbUploading2.setVisibility(View.VISIBLE);
                tvUploading2.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivCompilete3.setImageResource(R.drawable.updata_complete);
                ivCompilete3.setVisibility(View.GONE);
                tvCompilete3.setVisibility(View.GONE);
                pbUploading3.setVisibility(View.VISIBLE);
                tvUploading3.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 上传图片时进度条
     *
     * @param hasWrittenLen
     * @param totalLen
     */
    @Override
    public void uploadPrograss(long hasWrittenLen, long totalLen, final int mytype) {
        LogUtil.e("hasWrittenLen-->" + hasWrittenLen);
        LogUtil.e("totalLen-->" + hasWrittenLen);
        final int prograss;
        final int totle;
        if (totalLen / 10000 > 100) {
            prograss = (int) (hasWrittenLen / 10000);
            totle = (int) (totalLen / 10000);
        } else if (totalLen / 1000 > 100) {
            prograss = (int) (hasWrittenLen / 1000);
            totle = (int) (totalLen / 1000);
        } else {
            prograss = (int) (hasWrittenLen / 100);
            totle = (int) (totalLen / 100);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (mytype) {
                    case 1:
                        pbUploading1.setMax(totle);
                        pbUploading1.setProgress(prograss);
                        break;
                    case 2:
                        pbUploading2.setMax(totle);
                        pbUploading2.setProgress(prograss);
                        break;
                    case 3:
                        pbUploading3.setMax(totle);
                        pbUploading3.setProgress(prograss);
                        break;
                }
            }
        });

    }

    /**
     * 身份证证件上传（只保存图片地址）成功
     */
    @Override
    public void showSuccess() {
        if (isFromBaseCertifiActivity == 1) {
            setResult(102);
            AppActivityManager.getAppManager().finishActivity(this);
        } else {
            startActivity(InfoCompleteActivity.class);
        }

    }


    /**
     * 权限回调成功
     */
    @Override
    public void onPermissionSuccess() {
        popupWindow = new DocumentPopupWindow(DocUploadActivity.this, rlUpdata1);
    }

    @Override
    public void onPermissionFaild() {

    }

    /**
     * 打开相册或者照相机成功
     *
     * @param reqeustCode
     * @param resultList
     */
    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        String filePath = resultList.get(0).getPhotoPath();
        if ("".equals(filePath)) {
            showToast("SD卡错误", Toast.LENGTH_SHORT);
            return;
        }
        finalType = type;
        switch (type) {
            case 1:
                GliderHelper.loadImage(filePath, ivUpdata1, null);
                break;
            case 2:
                GliderHelper.loadImage(filePath, ivUpdata2, null);
                break;
            case 3:
                GliderHelper.loadImage(filePath, ivUpdata3, null);
                break;
        }
        File file = new File(filePath);
        /**
         * 压缩图片
         */
        Luban.get(DocUploadActivity.this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.e("压缩图片错误--》" + throwable.toString());
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends File>>() {
                    @Override
                    public Observable<? extends File> call(Throwable throwable) {
                        return Observable.empty();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        redictionStatus();//更改状态
                        mDocUpLoadPresenter.uploadFace(file, uid + "", hashid, finalType);
                    }
                });
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        LogUtil.e("打开相册错误---》" + errorMsg);
    }


    /**
     * 选择上传身份证
     */
    public class DocumentPopupWindow extends PopupWindow {
        public DocumentPopupWindow(Context mContext, View parent) {
            View view = View.inflate(DocUploadActivity.this, R.layout.pop_camera_select, null);
            view.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GalleryFinal.openGallerySingle(Constant.REQUEST_CODE_GALLERY, DocUploadActivity.this);
                    popupWindow.dismiss();
                }
            });
            view.findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GalleryFinal.openCamera(Constant.REQUEST_CODE_CAMERA, DocUploadActivity.this);
                    popupWindow.dismiss();
                }
            });
            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in_a));
            setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setFocusable(true);
            showAsDropDown(view);
            setOutsideTouchable(true);
            setBackgroundDrawable(getResources().getDrawable(R.color.pop_bg));
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
        }

    }

    @Override
    public void showProgressDialog() {
        createLoadingDialog();
    }

    @Override
    public void hiteProgressDialog() {
        cancleDialog();
    }

    @Override
    public void showError(String error) {
        showToast(error, Toast.LENGTH_SHORT);

    }

    /**
     * 上传图片失败
     *
     * @param msg
     * @param mtype
     */
    @Override
    public void showPicError(String msg, int mtype) {
        LogUtil.e(msg);
        switch (mtype) {
            case 1:
                ivCompilete1.setImageResource(R.drawable.upload_faild);
                ivCompilete1.setVisibility(View.VISIBLE);
                tvCompilete1.setVisibility(View.VISIBLE);
                tvCompilete1.setText(getString(R.string.upload_faild));
                pbUploading1.setVisibility(View.GONE);
                tvUploading1.setVisibility(View.GONE);
                break;
            case 2:
                ivCompilete2.setImageResource(R.drawable.upload_faild);
                ivCompilete2.setVisibility(View.VISIBLE);
                tvCompilete2.setVisibility(View.VISIBLE);
                tvCompilete2.setText(getString(R.string.upload_faild));
                pbUploading2.setVisibility(View.GONE);
                tvUploading2.setVisibility(View.GONE);
                break;
            case 3:
                ivCompilete3.setImageResource(R.drawable.upload_faild);
                ivCompilete3.setVisibility(View.VISIBLE);
                tvCompilete3.setVisibility(View.VISIBLE);
                tvCompilete3.setText(getString(R.string.upload_faild));
                pbUploading3.setVisibility(View.GONE);
                tvUploading3.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mDocUpLoadPresenter.unsubcrible();
        super.onDestroy();
    }
}
