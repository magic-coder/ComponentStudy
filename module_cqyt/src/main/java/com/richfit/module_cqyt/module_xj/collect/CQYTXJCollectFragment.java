package com.richfit.module_cqyt.module_xj.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.lib_rv_animation.Animation.animators.FadeInDownAnimator;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.XJDetailAdapter;

import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTXJCollectFragment extends BaseFragment<CQYTXJCollectPresenterImp>
        implements CQYTXJCollectContract.View {

    EditText etInsLocation;
    RecyclerView rvLocations;
    XJDetailAdapter mAdapter;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length == 1) {
            final String location = list[0];
            etInsLocation.setText(location);
            saveCollectedData();
        }
    }


    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_xj_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CQYTXJCollectPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        etInsLocation = (EditText) mView.findViewById(R.id.cqyt_et_ins_location);
        rvLocations = (RecyclerView) mView.findViewById(R.id.base_detail_recycler_view);
        //设置布局管理器
        RecyclerView.LayoutManager lm = new GridLayoutManager(mActivity,3);
        rvLocations .setLayoutManager(lm);
        rvLocations.setItemAnimator(new FadeInDownAnimator());
    }

    @Override
    public void initEvent() {

    }


    @Override
    public void initData() {

    }


    @Override
    public void initDataLazily() {
        mPresenter.getTransferInfo(null, "", mBizType, "", Global.USER_ID,
                "", "", "", "");
    }


    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (TextUtils.isEmpty(getString(etInsLocation))) {
            showMessage("请先输入巡检货位");
            return false;
        }

        if (mRefData == null) {
            showMessage("请在抬头初始化本次巡检");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.voucherDate)) {
            showMessage("请先在抬头界面输入巡检日期");
            return false;
        }

        return true;
    }

    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("提示");
        builder.setMessage("您真的需要保存数据吗?点击确定将保存数据.");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            saveCollectedData();
        });
        builder.show();
    }

    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            ResultEntity result = new ResultEntity();
            result.businessType = mBizType;
            result.location = getString(etInsLocation);
            result.voucherDate = mRefData.voucherDate;
            result.userId = Global.USER_ID;
            result.inspectionType = mRefData.inspectionType;
            result.mapIns = new HashMap<>();
            if (0 == mRefData.inspectionType) {
                //班前
                result.mapIns.put("insDoor", mRefData.insDoor);
                result.mapIns.put("insMaterial", mRefData.insMaterial);
                result.mapIns.put("insEquipe", mRefData.insEquipe);
                result.mapIns.put("insLocation", mRefData.insLocation);
                result.mapIns.put("insSafe", mRefData.insSafe);
                result.mapIns.put("remark", mRefData.remark);
            }
            if (1 == mRefData.inspectionType) {
                //班后
                result.mapIns.put("insDocument", mRefData.insDocument);
                result.mapIns.put("insOffice", mRefData.insOffice);
                result.mapIns.put("insException", mRefData.insException);
                result.mapIns.put("insPower", mRefData.insPower);
                result.mapIns.put("insLock", mRefData.insLock);
                result.mapIns.put("remark", mRefData.remark);
            }
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }

    @Override
    public void saveCollectedDataSuccess() {
        showMessage("保存数据成功");
        clearAllUI();
        //调用缓存
        mPresenter.getTransferInfo(null, "", mBizType, "", Global.USER_ID,
                "", "", "", "");
    }

    @Override
    public void saveCollectedDataFail(String message) {
        showMessage("保存数据失败;" + message);
    }

    @Override
    public void showLocations(List<LocationInfoEntity> locations) {
        Log.d("yff", "location size = " + locations.size());
        if (mAdapter == null) {
            mAdapter = new XJDetailAdapter(mActivity, R.layout.cqyt_item_xj, locations);
            rvLocations.setAdapter(mAdapter);
        } else {
            mAdapter.addAll(locations);
        }
    }

    @Override
    public void getTransferInfoFail(String message) {
        showMessage(message);
        if (mAdapter != null) {
            mAdapter.removeAll();
        }
    }

    private void clearAllUI() {
        clearCommonUI(etInsLocation);
    }

    @Override
    public void _onPause() {
        super._onPause();
        clearCommonUI();
    }
}
