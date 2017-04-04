package com.hzxm.easyloan;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hzxm.easyloan.ui.fragment.ChamberlainFragment;
import com.hzxm.easyloan.ui.fragment.HomeFragment;
import com.hzxm.easyloan.ui.fragment.MineFragment;
import com.lmz.baselibrary.ui.BaseActivity;
import com.lmz.baselibrary.util.AppActivityManager;
import com.lmz.baselibrary.widget.TabStripView;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;

public class MainActivity extends BaseActivity implements TabStripView.OnTabSelectedListener {

    @BindView(R.id.main_container)
    FrameLayout mainContainer;
    @BindView(R.id.navigateTabBar)
    TabStripView navigateTabBar;

    @BindString(R.string.loan)
    String loan;
    @BindString(R.string.chamberlain)
    String chamberlain;
    @BindString(R.string.mine)
    String mine;
    @BindColor(R.color.tab_color)
    int tabColor;
    @BindColor(R.color.home_blue)
    int tabSelectColor;

    private long mExitTime = 0;

    @Override
    protected void initConvetView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        SetStatusBarColor(R.color.home_blue);
        navigateTabBar.setFrameLayoutId(R.id.main_container);
        //恢复选项状态

        navigateTabBar.setTabTextColor(tabColor);
        navigateTabBar.setSelectedTabTextColor(tabSelectColor);

        navigateTabBar.onRestoreInstanceState(savedInstanceState);
        navigateTabBar.setTabSelectListener(this);
        navigateTabBar.addTab(HomeFragment.class, new TabStripView.TabParam(R.drawable.home_loan, R.drawable.home_loan_focuse, loan));
        navigateTabBar.addTab(ChamberlainFragment.class, new TabStripView.TabParam(R.drawable.home_service, R.drawable.home_service_focuse, chamberlain));
        navigateTabBar.addTab(MineFragment.class, new TabStripView.TabParam(R.drawable.home_mine, R.drawable.home_mine_focuse, mine));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        navigateTabBar.onSaveInstanceState(outState);
    }

    @Override
    public void onTabSelected(TabStripView.ViewHolder holder) {
        switch (holder.tabIndex) {
            case 0:
                SetStatusBarColor(R.color.home_blue_title);
                break;
            case 1:
                SetStatusBarColor(R.color.home_blue);
                break;
            case 2:
                SetStatusBarColor(R.color.home_blue);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void exitApp() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            showToast("再按一次退出凡易贷", Toast.LENGTH_SHORT);
        } else {
            AppActivityManager.getAppManager().finishAllActivity();
        }
        mExitTime = System.currentTimeMillis();
    }
}
