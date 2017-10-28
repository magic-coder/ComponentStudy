package com.richfit.module_cqyt.module_whxj.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.richfit.common_lib.lib_adapter_rv.MultiItemTypeAdapter;
import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectFragment;
import com.richfit.common_lib.lib_rv_animation.Animation.animators.FadeInDownAnimator;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.XJDetailAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTWHXJCollectFragment extends BaseCollectFragment<CQYTWHXJCollectPresenterImp>
        implements CQYWHTXJCollectContract.View, MultiItemTypeAdapter.OnItemClickListener {

    EditText etInsLocation;
    RecyclerView rvLocations;
    XJDetailAdapter mAdapter;
    List<LocationInfoEntity> mDatas;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length <= 2) {
            final String location = list[0];
            etInsLocation.setText("");
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
        mPresenter = new CQYTWHXJCollectPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        etInsLocation = (EditText) mView.findViewById(R.id.cqyt_et_ins_location);
        rvLocations = (RecyclerView) mView.findViewById(R.id.base_detail_recycler_view);
        //设置布局管理器
        RecyclerView.LayoutManager lm = new GridLayoutManager(mActivity, 2);
        rvLocations.setLayoutManager(lm);
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
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("inspectionType",mRefData.inspectionType);
        mPresenter.getTransferInfo(null, "", mBizType, "", Global.USER_ID,
                "", "", "", "",extraMap);
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
    public ResultEntity provideResult() {
        ResultEntity result = new ResultEntity();
        result.businessType = mBizType;
        result.location = getString(etInsLocation);
        result.voucherDate = mRefData.voucherDate;
        result.userId = Global.USER_ID;
        result.inspectionType = mRefData.inspectionType;

        result.mapIns = new HashMap<>();
        result.mapIns.put("insDoor", mRefData.insDoor);
        result.mapIns.put("insClean", mRefData.insClean);
        result.mapIns.put("insQuantity", mRefData.insQuantity);
        result.mapIns.put("insChemical", mRefData.insChemical);
        result.mapIns.put("insFire", mRefData.insFire);
        result.mapIns.put("insTh", mRefData.insTh);
        result.mapIns.put("insHazardous", mRefData.insHazardous);
        result.mapIns.put("insFlammable", mRefData.insFlammable);
        result.mapIns.put("insEffective", mRefData.insEffective);
        result.mapIns.put("insWeather", mRefData.insWeather);
        result.mapIns.put("insTemperature", mRefData.insTemperature);
        result. mapIns.put("insHumidity", mRefData.insHumidity);
        return result;
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        showMessage(message);
        clearAllUI();
        //调用缓存
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("inspectionType",mRefData.inspectionType);
        mPresenter.getTransferInfo(null, "", mBizType, "", Global.USER_ID,
                "", "", "", "",extraMap);
    }

    @Override
    public void saveCollectedDataFail(String message) {
        showMessage("保存数据失败;" + message);
    }

    @Override
    public void showLocations(List<LocationInfoEntity> locations) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(locations);
        if (mAdapter == null) {
            mAdapter = new XJDetailAdapter(mActivity, R.layout.cqyt_item_xj, locations);
            mAdapter.setOnItemClickListener(this);
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

    @Override
    public void deleteNodeSuccess(int position) {
        showMessage("删除成功");
        //获取最新缓存，刷新界面
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("inspectionType",mRefData.inspectionType);
        mPresenter.getTransferInfo(null, "", mBizType, "", Global.USER_ID,
                "", "", "", "",extraMap);
    }

    @Override
    public void deleteNodeFail(String message) {
        showMessage(message);
    }

    private void clearAllUI() {
        clearCommonUI(etInsLocation);
    }

    @Override
    public void _onPause() {
        super._onPause();
        clearCommonUI();
    }

    /**
     * 点击提示删除
     *
     * @param view
     * @param holder
     * @param position
     */
    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("警告");
        builder.setIcon(com.richfit.common_lib.R.mipmap.icon_warning);
        builder.setMessage("您真的要删除该条数据?点击确定删除.");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            final LocationInfoEntity node = mDatas.get(position);
            mPresenter.deleteNode("N", node.transId, node.transLineId, node.location, mRefType,
                    mBizType, position, Global.COMPANY_ID);

        });
        builder.show();
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
