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

import com.hzxm.easyloan.MyApplication;
import com.hzxm.easyloan.R;
import com.hzxm.easyloan.model.mine.AmountCreateModel;
import com.hzxm.easyloan.presenter.implPresenter.CompanyInfoPresenter;
import com.hzxm.easyloan.presenter.implView.ICompanyInfoView;
import com.hzxm.easyloan.utils.Constant;
import com.lmz.baselibrary.listener.IPermissionResultListener;
import com.lmz.baselibrary.ui.BaseActivity;
import com.lmz.baselibrary.util.AppActivityManager;
import com.lmz.baselibrary.util.LogUtil;
import com.lmz.baselibrary.util.SharedPreferencesUtil;
import com.lmz.baselibrary.util.glide.GliderHelper;
import com.lmz.baselibrary.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
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
 * 公司信息
 */
public class CompanyInfoActivity extends BaseActivity implements
        IPermissionResultListener, GalleryFinal.OnHanlderResultCallback, ICompanyInfoView {


    @BindView(R.id.tv_remind)
    TextView tvRemind;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.ll_top_remind)
    LinearLayout llTopRemind;
    @BindView(R.id.et_name)
    ClearEditText etName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.et_plane)
    ClearEditText etPlane;
    @BindView(R.id.ll_plane)
    LinearLayout llPlane;
    @BindView(R.id.et_work_position)
    ClearEditText etWorkPosition;
    @BindView(R.id.ll_work_position)
    LinearLayout llWorkPosition;
    @BindView(R.id.et_work_age)
    ClearEditText etWorkAge;
    @BindView(R.id.ll_work_age)
    LinearLayout llWorkAge;
    @BindView(R.id.et_entry_time)
    ClearEditText etEntryTime;
    @BindView(R.id.ll_entry_time)
    LinearLayout llEntryTime;
    @BindView(R.id.et_salary)
    ClearEditText etSalary;
    @BindView(R.id.ll_salary)
    LinearLayout llSalary;
    @BindView(R.id.iv_updata1)
    ImageView ivUpdata1;
    @BindView(R.id.iv_compilete1)
    ImageView ivCompilete1;
    @BindView(R.id.tv_compilete1)
    TextView tvCompilete1;
    @BindView(R.id.tv_uploading1)
    TextView tvUploading1;
    @BindView(R.id.pb_uploading1)
    ProgressBar pbUploading1;
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
    @BindView(R.id.iv_compilete3)
    ImageView ivCompilete3;
    @BindView(R.id.tv_compilete3)
    TextView tvCompilete3;
    @BindView(R.id.tv_uploading3)
    TextView tvUploading3;
    @BindView(R.id.pb_uploading3)
    ProgressBar pbUploading3;
    @BindView(R.id.iv_updata3)
    ImageView ivUpdata3;
    @BindView(R.id.rl_updata3)
    RelativeLayout rlUpdata3;
    @BindView(R.id.btn_verification)
    Button btnVerification;
    @BindView(R.id.activity_company_info)
    LinearLayout activityCompanyInfo;

    @BindView(R.id.tv_three)
    TextView mThreeMsg;

    private int type = 1;
    private int finalType = 1;

    private DocumentPopupWindow popupWindow;

    private CompanyInfoPresenter mCompanyInfoPresenter;
    private int uid;
    private String hashid;
    //三张图片地址
    private String path1 = "", path2 = "", path3 = "";

    //设置头部警告栏
    @BindString(R.string.top_remind)
    String mTopRemind;

    private AmountCreateModel.DataEntity.InfoEntity.CompanyEntity mInfoEntity;
    private List<AmountCreateModel.DataEntity.InfoEntity.CompanyEntity.CompanyImgEntity> mImages;

    @Override
    protected void initConvetView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_company_info);
    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        SetStatusBarColor(R.color.transparent_white);
        tvRemind.setText(mTopRemind);
    }

    @Override
    protected void initData() {
        try {
            mInfoEntity = getIntent().getExtras().getParcelable("info");
            mImages = getIntent().getExtras().getParcelableArrayList("img");
            etName.setText(mInfoEntity.getCompany_name());
            etName.setSelection(mInfoEntity.getCompany_name().length());
            etPlane.setText(mInfoEntity.getCompany_tel());
            etWorkPosition.setText(mInfoEntity.getCompany_work());
            etWorkAge.setText(mInfoEntity.getWork_year());
            etEntryTime.setText(mInfoEntity.getCompany_year());
            etSalary.setText(mInfoEntity.getWork_money());
            path1 = mImages.get(0).getImage();
            path2 = mImages.get(1).getImage();
            GliderHelper.loadImage(path1, ivUpdata1, null);
            GliderHelper.loadImage(path2, ivUpdata2, null);
            if (mImages.size() == 3) {
                path3 = mImages.get(2).getImage();
                GliderHelper.loadImage(path3, ivUpdata3, null);
                mThreeMsg.setText(mImages.get(2).getName());
            }
        } catch (NullPointerException e) {
            LogUtil.e(e);
        }
        uid = SharedPreferencesUtil.getInstance(MyApplication.getContext()).getInt(Constant.UID, -1);
        hashid = SharedPreferencesUtil.getInstance(MyApplication.getContext()).getString(Constant.HASH_ID, "");
        mCompanyInfoPresenter = new CompanyInfoPresenter(this);

    }

    /**
     * 关闭头部警告
     */
    @OnClick(R.id.iv_delete)
    public void closeTop() {
        llTopRemind.setVisibility(View.GONE);
    }


    /**
     * 上传图片
     */
    @OnClick({R.id.rl_updata1, R.id.rl_updata2, R.id.rl_updata3})
    public void upLoadPic1(RelativeLayout rl) {
        switch (rl.getId()) {
            case R.id.rl_updata1:
                type = 1;
                permissionRequest("是否允许凡易贷打开相机",
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        101, CompanyInfoActivity.this);
                break;
            case R.id.rl_updata2:
                type = 2;
                permissionRequest("是否允许凡易贷打开相机",
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        102, CompanyInfoActivity.this);
                break;
            case R.id.rl_updata3:
                type = 3;
                permissionRequest("是否允许凡易贷打开相机",
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        103, CompanyInfoActivity.this);
                break;
        }
    }

    /**
     * 获取权限
     */
    @Override
    public void onPermissionSuccess() {
        popupWindow = new DocumentPopupWindow(CompanyInfoActivity.this, rlUpdata1);

    }

    @Override
    public void onPermissionFaild() {

    }

    /**
     * 相机返回
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
        switch (type) {
            case 1:
                finalType = type;
                GliderHelper.loadImage(filePath, ivUpdata1, null);
                break;
            case 2:
                finalType = type;
                GliderHelper.loadImage(filePath, ivUpdata2, null);
                break;
            case 3:
                finalType = type;
                GliderHelper.loadImage(filePath, ivUpdata3, null);
                break;
        }
        File file = new File(filePath);
        /**
         * 压缩图片
         */
        Luban.get(CompanyInfoActivity.this)
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
                        //传finaltype  防止压缩时间长  而用户又点击了另一个按钮  type改变
                        mCompanyInfoPresenter.uploadFace(file, uid + "", hashid, finalType);
                    }
                });

    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
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
     * 上传图片成功 获得图片地址
     *
     * @param path
     */
    @Override
    public void uploadSuccess(String path, int mtype) {
        LogUtil.e("上传图片成功-->" + path);
        switch (mtype) {
            case 1:
                path1 = path;
                ivCompilete1.setVisibility(View.VISIBLE);
                tvCompilete1.setVisibility(View.VISIBLE);
                tvCompilete1.setText(getString(R.string.upload_complete));
                pbUploading1.setVisibility(View.GONE);
                tvUploading1.setVisibility(View.GONE);
                break;
            case 2:
                path2 = path;
                ivCompilete2.setVisibility(View.VISIBLE);
                tvCompilete2.setVisibility(View.VISIBLE);
                tvCompilete2.setText(getString(R.string.upload_complete));
                pbUploading2.setVisibility(View.GONE);
                tvUploading2.setVisibility(View.GONE);
                break;
            case 3:
                path3 = path;
                ivCompilete3.setVisibility(View.VISIBLE);
                tvCompilete3.setVisibility(View.VISIBLE);
                tvCompilete3.setText(getString(R.string.upload_complete));
                pbUploading3.setVisibility(View.GONE);
                tvUploading3.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void uploadPrograss(long hasWrittenLen, long totalLen, final int mtype) {
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
                switch (mtype) {
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
     * 点击完成  提交公司信息
     */
    @OnClick(R.id.btn_verification)
    public void submitInfo() {
        String companyName = etName.getText().toString().trim();
        if ("".equals(companyName)) {
            showToast("请输入公司名称", Toast.LENGTH_SHORT);
            return;
        }
        String planeTel = etPlane.getText().toString().trim();
        if ("".equals(planeTel)) {
            showToast("请输入公司座机", Toast.LENGTH_SHORT);
            return;
        }

        String mPositionInfo = etWorkPosition.getText().toString();
        if ("".equals(mPositionInfo)) {
            showToast("请输入职位信息", Toast.LENGTH_SHORT);
            return;
        }
        String mWorkAge = etWorkAge.getText().toString();
        if ("".equals(mWorkAge)) {
            showToast("请输入工作年限", Toast.LENGTH_SHORT);
            return;
        }
        String mWorkTime = etEntryTime.getText().toString();
        if ("".equals(mWorkTime)) {
            showToast("请输入入职时间", Toast.LENGTH_SHORT);
            return;
        }
        if ("".equals(path1)) {
            showToast("请上传工作证或入职协议照片", Toast.LENGTH_SHORT);
            return;
        }
        if ("".equals(path2)) {
            showToast("请上传公司照片", Toast.LENGTH_SHORT);
            return;
        }
        String mSalary = etSalary.getText().toString().trim();
        if ("".equals(mSalary)) {
            showToast("请输入薪资情况", Toast.LENGTH_SHORT);
            return;
        }
        List<JSONObject> mPics = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        try {
            jsonObject.put("name", "工作证/入职协议");
            jsonObject.put("image", path1);
            jsonObject2.put("name", "公司照片");
            jsonObject2.put("image", path2);
            jsonObject3.put("name", "更多照片");
            jsonObject3.put("image", path3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mPics.add(jsonObject);
        mPics.add(jsonObject2);
        if (!"".equals(path3)) {
            mPics.add(jsonObject3);
        }
        mCompanyInfoPresenter.companyInfo(uid + "", companyName, planeTel, mPics.toString(), mWorkTime, mWorkAge, mSalary, mPositionInfo);
    }

    /**
     * 提交公司信息成功
     */
    @Override
    public void showSuccess() {
        showToast("资料提交成功", Toast.LENGTH_SHORT);
        AppActivityManager.getAppManager().finishActivity(this);
    }


    @Override
    public void showProgressDialog() {
        createLoadingDialog();
    }

    @Override
    public void hiteProgressDialog() {
        cancleDialog();

    }

    /**
     * 上传图片失败
     *
     * @param msg
     * @param mtype
     */
    @Override
    public void showPicError(String msg, int mtype) {
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

    /**
     * 上传信息失败
     *
     * @param error
     */
    @Override
    public void showError(String error) {
        showToast(error, Toast.LENGTH_SHORT);
    }


    /**
     * 选择上传身份证
     */
    public class DocumentPopupWindow extends PopupWindow {
        public DocumentPopupWindow(Context mContext, View parent) {
            View view = View.inflate(CompanyInfoActivity.this, R.layout.pop_camera_select, null);
            view.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GalleryFinal.openGallerySingle(Constant.REQUEST_CODE_GALLERY, CompanyInfoActivity.this);
                    popupWindow.dismiss();
                }
            });
            view.findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GalleryFinal.openCamera(Constant.REQUEST_CODE_CAMERA, CompanyInfoActivity.this);
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
    protected void onDestroy() {
        mCompanyInfoPresenter.unsubcrible();
        super.onDestroy();
    }
}
