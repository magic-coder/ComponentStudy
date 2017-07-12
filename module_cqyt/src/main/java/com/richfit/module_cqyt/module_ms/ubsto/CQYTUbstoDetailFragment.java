package com.richfit.module_cqyt.module_ms.ubsto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
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
 * ubsto数据上传分为3个状态，0表示初始化状态，1表示已经数据上传，2表示过账成功
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
        String state = (String) getData(mBizType + mRefType, "0");
        if ("2".equals(state)) {
            showMessage("已经过账,不允许修改");
            return;
        }
        mPresenter.editNode(null, null, mRefData, node, mCompanyCode, mBizType, mRefType,
                getSubFunName(), -1);
    }

    @Override
    public void deleteNode(final RefDetailEntity node, int position) {
        String state = (String) getData(mBizType + mRefType, "0");
        if ("2".equals(state)) {
            showMessage("已经过账,不允许删除");
            return;
        }
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


    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        //保存
        BottomMenuEntity menu = new BottomMenuEntity();
        menu.menuName = "保存";
        menu.menuImageRes = R.mipmap.icon_save_data;
        //过账
        tmp.get(0).transToSapFlag = "06";
        //上架
        tmp.get(2).transToSapFlag = "05";
        ArrayList menus = new ArrayList();
        menus.add(menu);
        menus.add(tmp.get(0));
        menus.add(tmp.get(2));
        return menus;
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
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("本次采集已经数据删除已经成功,请进行其他的操作!");
            return;
        }
        mTransNum = "";
        mPresenter.submitData2BarcodeSystem(mRefData.refCodeId, mTransId, mBizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
    }

    /**
     * 第一步过账失败显示错误信息。这里第一步进行的是图片上传和数据上传
     *
     * @param message
     */
    @Override
    public void submitBarcodeSystemFail(String message) {
        mTransNum = "";
        showErrorDialog(TextUtils.isEmpty(message) ? "保存数据失败" : message);
    }

    /**
     * 2.调用06过账。注意成功后显示单号回调调用的第一步的回调
     *
     * @param transToSapFlag
     */
    protected void submit2SAP(String transToSapFlag) {
        String state = (String) getData(mBizType + mRefType, "0");
        if (!"1".equals(state)) {
            showMessage("请先进行数据上传!");
            return;
        }
        mTransNum = "";
        mPresenter.submitData2SAP(mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
    }

    /**
     * 第二步过账成功
     */
    @Override
    public void submitSAPSuccess() {
        showSuccessDialog(mTransNum);
    }

    /**
     * 第二步过账失败
     *
     * @param messages
     */
    @Override
    public void submitSAPFail(String[] messages) {
        mTransNum = "";
        showErrorDialog(messages);
    }

    /**
     * 3. 05下架
     */
    protected void sapUpAndDownLocation(String transToSapFlag) {
        String state = (String) getData(mBizType + mRefType, "0");
        if (!"2".equals(state)) {
            showMessage("请先进行过账!");
            return;
        }
        mInspectionNum = "";
        mPresenter.sapUpAndDownLocation(mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, null, -1);
    }

    @Override
    public void upAndDownLocationSuccess() {
        setRefreshing(false, "下架成功");
        showSuccessDialog(mInspectionNum);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        //注意这里必须清除单据数据
        mRefData = null;
        mTransNum = "";
        mTransId = "";
        //两步成功后将寄售转自有标识清空
        isNeedTurn = false;
        isTurnSuccess = false;
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    @Override
    public void upAndDownLocationFail(String[] messages) {
        mInspectionNum = "";
        showErrorDialog(messages);
    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        String transferFlag = (String) getData(mBizType + mRefType, "0");
        if("2".equals(transferFlag)) {
            setRefreshing(false, "本次采集的数据已经过账，不允许修改!");
            return false;
        }
        return true;
    }

    @Override
    protected String getSubFunName() {
        return "UBSTO-转储发出";
    }
}
