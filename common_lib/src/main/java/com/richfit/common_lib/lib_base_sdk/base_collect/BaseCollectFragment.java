package com.richfit.common_lib.lib_base_sdk.base_collect;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * 2017年07年26日增加数据采集切面
 * Created by monday on 2017/7/26.
 */

public abstract class BaseCollectFragment<P extends IBaseCollectPresenter> extends BaseFragment<P>
        implements IBaseCollectView {

    private InputMethodManager imm;
    /*从条码中扫描带出来的单据行号，用于过滤重复的行号。该功能长庆使用了*/
    protected String mLineNumForFilter;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {

        //遍历该View树，去除所有的角点
        clearFocus(mView);
        if (imm == null) {
            imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        //关闭软键盘
        if (mView != null && isSoftShowing()) {
            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
        }
    }

    /**
     * 保存数据入库
     */
    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        ResultEntity result = provideResult();
        if (result == null) {
            showMessage("未获取到条件的数据");
            return;
        }
        mPresenter.uploadCollectionDataSingle(result);
    }

    /**
     * 返回数据
     *
     * @return
     */
    @Override
    public ResultEntity provideResult() {
        return null;
    }


    /**
     * 通过单据单位计算库存数量
     *
     * @param quantity
     * @param recordUnit
     * @param unitRate
     * @return
     */
    protected String calQuantityByUnitRate(String quantity, String recordUnit, float unitRate) {
        if (TextUtils.isEmpty(quantity)) {
            return "0";
        }
        float q = CommonUtil.convertToFloat(quantity, 0.0f);
        if (!TextUtils.isEmpty(recordUnit) && unitRate != 0) {
            q /= unitRate;
        }
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(q);
    }

    /**
     * 管理批次。注意这里通过是否打开了批次管理改变你enable属性，所以
     * 如果要强制不让用户输入必须在bindCommonCollectUI中强制设置；
     *
     * @param batchFlagView:接收批次信息输入的控件
     * @param batchManagerStatus:当前物料的批次管理状态
     */
    protected void manageBatchFlagStatus(TextView batchFlagView, boolean batchManagerStatus) {
        //如果该业务打开了批次管理，那么检查该物料是否打开了批次管理
        isOpenBatchManager = batchManagerStatus;
        Log.e("yff", "是否打开了批次管理 = " + isOpenBatchManager);
        batchFlagView.setEnabled(isOpenBatchManager);
        if (!isOpenBatchManager) {
            batchFlagView.setText("");
        }
    }


    /**
     * 通过物资条码信息和批次信息匹配
     * 这里的逻辑是：如果启用了批次管理那么明细里面可能有也可能没有批次，如果没有启用批次管理，那么
     * 明细一定没有批次
     *
     * @return:与该物料匹配的行号集合
     */
    protected Flowable<ArrayList<String>> matchMaterialInfo(final String materialNum, final String batchFlag) {
        if (mRefData == null || mRefData.billDetailList == null ||
                mRefData.billDetailList.size() == 0 || TextUtils.isEmpty(materialNum)) {
            return Flowable.error(new Throwable("请先获取单据信息"));
        }
        ArrayList<String> lineNums = new ArrayList<>();
        List<RefDetailEntity> list = mRefData.billDetailList;
        for (RefDetailEntity entity : list) {
            if (entity.batchManagerStatus) {
                final String lineNum = entity.lineNum;
                //如果打开了批次，那么在看明细中是否有批次
                if (!TextUtils.isEmpty(entity.batchFlag) && !TextUtils.isEmpty(batchFlag)) {
                    if (materialNum.equalsIgnoreCase(entity.materialNum) &&
                            batchFlag.equalsIgnoreCase(entity.batchFlag) &&
                            !TextUtils.isEmpty(lineNum))

                        lineNums.add(lineNum);
                } else {
                    if (materialNum.equalsIgnoreCase(entity.materialNum) &&
                            !TextUtils.isEmpty(lineNum))
                        lineNums.add(lineNum);
                }
            } else {
                final String lineNum = entity.lineNum;
                //如果明细中没有打开了批次管理,那么只匹配物料编码
                if (materialNum.equalsIgnoreCase(entity.materialNum) && !TextUtils.isEmpty(lineNum))
                    lineNums.add(entity.lineNum);

            }
        }
        if (lineNums.size() == 0) {
            return Flowable.error(new Throwable("所扫描物资在单据中不存在!"));
        }
        return Flowable.just(lineNums);
    }

    /**
     * 遍历View树，去除该树上面所有的焦点，以解决扫描的问题
     *
     * @param view
     */
    private void clearFocus(View view) {
        if (view == null)
            return;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                clearFocus(childAt);
            }
            return;
        }
        view.clearFocus();
//        view.setFocusable(false);
//        view.setFocusableInTouchMode(true);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return;
    }

    protected boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = mActivity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom - getSoftButtonsBarHeight() != 0;
    }

    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
}
