package com.richfit.module_cqyt.module_ms.ubsto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.TreeNode;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.CQYTUbstoDetailAdapter;
import com.richfit.module_cqyt.module_ms.ubsto.imp.CQYTUbstoDetailPresenterImp;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;

import java.util.ArrayList;
import java.util.List;

import static com.richfit.common_lib.utils.SPrefUtil.getData;

/**
 * ubsto数据上传分为3个状态，0表示初始化状态，1表示已经数据上传，2表示过账成功。
 * 2017年7月1日修改成一个保存按钮，过账和下架用户去sap做。
 * Created by monday on 2017/7/4.
 */

public class CQYTUbstoDetailFragment extends BaseDSDetailFragment<CQYTUbstoDetailPresenterImp> {


    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_ubsto_detail;
    }


    @Override
    public void initPresenter() {
        mPresenter = new CQYTUbstoDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    /**
     * 显示物资移库明细界面，注意这里显示的标准界面
     *
     * @param allNodes
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        saveTurnFlag(allNodes);
        if (mAdapter == null) {
            mAdapter = new CQYTUbstoDetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);

        }
    }

    @Override
    public void editNode(final RefDetailEntity node, int position) {
        mPresenter.editNode(null, null, mRefData, node, mCompanyCode, mBizType, mRefType,
                getSubFunName(), -1);
    }

    @Override
    public void deleteNode(final RefDetailEntity node, int position) {
        if (TextUtils.isEmpty(node.transLineId)) {
            showMessage("该行还未进行数据采集");
            return;
        }
        TreeNode parentNode = node.getParent();
        String lineDeleteFlag;
        if (parentNode == null) {
            lineDeleteFlag = "N";
        } else {
            lineDeleteFlag = parentNode.getChildren().size() > 1 ? "N" : "Y";
        }

        mPresenter.deleteNode(lineDeleteFlag, node.transId, node.transLineId,
                node.locationId, mRefData.refType, mRefData.bizType, position,
                mCompanyCode);
    }

    /**
     * 1.过账。因为要上传图片，给出单号
     */
    @Override
    protected void submit2BarcodeSystem(String transToSapFlag) {
        mExtraTansMap.clear();
        mExtraTansMap.put("refNum", mRefData.recordNum);
        if (isNeedTurn && !isTurnSuccess) {
            startTurnOwnSupplies("07");
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.put("shopCondition", mRefData.shopCondition);
        mPresenter.submitData2BarcodeSystem(mRefData.refCodeId, mTransId, mBizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
    }

    /**
     * 第一步过账成功显示物料凭证。注意这里需求改成只进行数据上传
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
    }


    /**
     * 2.调用05过账。注意成功后显示单号回调调用的第一步的回调
     *
     * @param transToSapFlag
     */
    @Override
    protected void submit2SAP(String transToSapFlag) {
        Log.d("yff", "submit2SAP : " + transToSapFlag);
        String state = (String) getData(mBizType + mRefType, "0");
        if (!"1".equals(state)) {
            showMessage("请先进行数据上传!");
            return;
        }
        mShowMsg.setLength(0);
        mPresenter.submitData2SAP(mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
    }

    /**
     * 第二步过账成功
     */
    @Override
    public void submitSAPSuccess() {
        showSuccessDialog(mShowMsg);
    }


    /**
     * 3. 06下架
     */
    @Override
    protected void sapUpAndDownLocation(String transToSapFlag) {
        Log.d("yff", "sapUpAndDownLocation : " + transToSapFlag);
        String state = (String) getData(mBizType + mRefType, "0");
        if (!"2".equals(state)) {
            showMessage("请先进行过账!");
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.clear();
        mExtraTansMap.put("shopCondition", mRefData.shopCondition);
        mPresenter.sapUpAndDownLocation(mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap, -1);
    }

    @Override
    public void upAndDownLocationSuccess() {
        setRefreshing(false, "下架成功");
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        //注意这里必须清除单据数据
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        //两步成功后将寄售转自有标识清空
        isNeedTurn = false;
        isTurnSuccess = false;
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }


    @Override
    protected boolean checkTransStateBeforeRefresh() {
        String transferFlag = (String) getData(mBizType + mRefType, "0");
        if ("1".equals(transferFlag)) {
            setRefreshing(false, "本次采集的数据已经上传,请先到数据明细界面进行过账等操作");
            return false;
        }
        return true;
    }

    @Override
    protected String getSubFunName() {
        return "物资出库";
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> defaultMenus = super.provideDefaultBottomMenu();
        //保存
        BottomMenuEntity menu = new BottomMenuEntity();
        menu.menuName = "保存";
        menu.menuImageRes = R.mipmap.icon_save_data;

        //过账
        defaultMenus.get(0).transToSapFlag = "06";

        //下架
        defaultMenus.get(2).transToSapFlag = "05";

        //保存菜单
        ArrayList menus = new ArrayList();
        menus.add(menu);
        menus.add(defaultMenus.get(0));
        menus.add(defaultMenus.get(2));
        return menus;
    }
}
