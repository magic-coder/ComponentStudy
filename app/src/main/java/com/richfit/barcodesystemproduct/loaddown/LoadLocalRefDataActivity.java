package com.richfit.barcodesystemproduct.loaddown;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.richfit.barcodesystemproduct.BuildConfig;
import com.richfit.barcodesystemproduct.R;
import com.richfit.barcodesystemproduct.scanservice.BaseBarScannerActivity;
import com.richfit.common_lib.lib_mvp.BaseActivity;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.MenuNode;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 下载单据数据界面。系统给出业务类型和单据类型供用户选择，
 * 扫描或者手动输入单据号开始请求接口获取单据数据。
 * Created by monday on 2016/11/1.
 */

public class LoadLocalRefDataActivity extends BaseBarScannerActivity<LoadLocalRefDataPresenterImp>
        implements LoadLocalRefDataContract.View {

    @BindView(R.id.sp_ref_type)
    Spinner spRefType;
    @BindView(R.id.sp_biz_type)
    Spinner spBizType;
    @BindView(R.id.et_ref_num)
    RichEditText etRefNum;

    /*业务类型列表*/
    ArrayList<MenuNode> mBizTypes;
    /*所有明细行数据*/
    ArrayList<RefDetailEntity> mDatas;

    Map<Integer, Integer> mRefTypeMap;


    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length == 1) {
            etRefNum.setText(list[0]);
            getRefData(list[0]);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_load_local_data;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoadLocalRefDataPresenterImp(this);
    }


    @Override
    public void initVariables() {
        super.initVariables();
        mBizTypes = new ArrayList<>();
        mDatas = new ArrayList<>();
        mRefTypeMap = new HashMap<>();
    }


    @Override
    protected void initViews() {
        //设置toolBar
        setupToolBar(R.id.toolbar, R.id.toolbar_title, "单据数据下载");
    }

    /**
     * <!--（0：采购订单，1：验收清单，2：生产订单，3：到货验收单，4：交货单，5：出库单，6：提料单，-->
     * <!--7：领料申请单，8：批料单，9：SAP出入库单，10：预留单，11 调拨单,12.退库申请单,13.条码出入库单
     * 14.报废申请单,15.工单,16.入库通知单,17.出库通知单
     * ）-->
     *
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        //初始化单据类型和业务类型下拉列表
        List<String> refTypeList = getStringArray(R.array.ref_type_list);
        List<String> result = new ArrayList<>();
        if (Global.CQZT.equals(BuildConfig.APP_NAME)) {
         /*   采购订单    采购订单－采购入库101
            调拨入库    SAP出入库单－调拨入库物料凭证
            201出库     领料申请单－其他出库领料申请单
            311         领料申请单－311领料申请
            313         领料申请单－313领料申请*/
            //过滤掉不需要的类型
            result.add(refTypeList.get(0));
            result.add(refTypeList.get(7));
            result.add(refTypeList.get(9));

            mRefTypeMap.put(0,0);
            mRefTypeMap.put(1,7);
            mRefTypeMap.put(2,9);

        }

        //1.单据类型
        ArrayAdapter<String> refTypeAdapter = new ArrayAdapter<>(this, R.layout.item_simple_sp,
                result);
        spRefType.setAdapter(refTypeAdapter);
        //2.业务类型
        mPresenter.readMenuInfo(Global.LOGIN_ID, Global.OFFLINE_MODE);
    }


    @Override
    public void initEvent() {
        etRefNum.setOnRichEditTouchListener((view, refNum) -> {
            hideKeyboard(view);
            getRefData(refNum);
        });
    }

    /**
     * 请求单据数据入口
     *
     * @param refNum
     */
    private void getRefData(String refNum) {
        if (TextUtils.isEmpty(refNum)) {
            showMessage("请先输入单据号");
            return;
        }
        mPresenter.getReferenceInfo(refNum, String.valueOf(mRefTypeMap.get(spRefType.getSelectedItemPosition())),
                mBizTypes.get(spBizType.getSelectedItemPosition()).getBusinessType(),
                "", "", Global.USER_ID);
    }


    /**
     * 下载单据信息成功
     */
    @Override
    public void getReferenceInfoSuccess(ReferenceEntity data) {
        showMessage(data.recordNum + "下载成功!");
    }


    /**
     * 下载单据数据完成
     */
    @Override
    public void getReferenceInfoComplete() {

    }

    /**
     * 下载单据信息失败
     *
     * @param message
     */
    @Override
    public void getReferenceInfoFail(String message) {
        showMessage(message);
    }

    /**
     * 读取菜单成功
     *
     * @param list
     */
    @Override
    public void readMenuInfoSuccess(ArrayList<MenuNode> list) {
        mBizTypes.clear();
        mBizTypes.addAll(list);
        ArrayList<String> bizTypes = new ArrayList<>();
        for (MenuNode node : list) {
            bizTypes.add(node.getCaption());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_simple_sp, bizTypes);
        spBizType.setAdapter(adapter);
    }

    /**
     * 读取菜单失败
     *
     * @param message
     */
    @Override
    public void readMenuInfoFail(String message) {
        showMessage(message);
    }


    @Override
    public void retry(String action) {
        getRefData(CommonUtil.Obj2String(etRefNum.getText()));
        super.retry(action);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}