package com.richfit.module_qysh.module_pd;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.lib_adapter.WorkAdapter;
import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.module_qysh.R;
import com.richfit.module_qysh.module_pd.head.IQYSHCNHeadView;
import com.richfit.module_qysh.module_pd.head.QYSHCNHeadPresenterImp;
import com.richfit.sdk_wzpd.R2;
import com.richfit.sdk_wzpd.checkn.head.CNHeadFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.richfit.data.constant.Global.USER_ID;

/**
 * Created by monday on 2017/8/7.
 */

public class QYSHCNHeadFragment extends BaseHeadFragment<QYSHCNHeadPresenterImp>
        implements IQYSHCNHeadView {

    @BindView(R2.id.sp_work)
    Spinner spWork;
    @BindView(R2.id.sp_inv)
    Spinner spInv;
    @BindView(R2.id.sp_storage_num)
    Spinner spStorageNum;
    @BindView(R2.id.tv_checker)
    TextView tvChecker;
    @BindView(R2.id.et_check_date)
    RichEditText etTransferDate;
    @BindView(R2.id.cb_special_flag)
    CheckBox cbSpecialFlag;
    @BindView(R2.id.ll_location_type)
    LinearLayout llLocationType;
    @BindView(R2.id.sp_location_type)
    Spinner spLocationType;

    /*工厂列表*/
    List<WorkEntity> mWorkDatas;
    /*库存地点列表以及适配器*/
    InvAdapter mInvAdapter;
    List<InvEntity> mInvDatas;
    /*库存号列表*/
    List<String> mStorageNums;
    String mSpecialFlag;
    /*仓储类型*/
    List<SimpleEntity> mLocationTypes;

    @Override
    protected int getContentId() {
        return R.layout.qysh_fragment_cn_head;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new QYSHCNHeadPresenterImp(mActivity);
    }

    @Override
    public void initVariable(Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        mInvDatas = new ArrayList<>();
        mWorkDatas = new ArrayList<>();
        mStorageNums = new ArrayList<>();
    }

    @Override
    protected void initView() {
        if (isOpenLocationType) {
            llLocationType.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initEvent() {
        /*选择盘点日期*/
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));


        /*监听工厂，获取库存地点*/
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getInvsByWorkId(mWorkDatas.get(position).workId, 0));


        RxCompoundButton.checkedChanges(cbSpecialFlag)
                .subscribe(a -> mSpecialFlag = a.booleanValue() ? "Y" : "N");
    }

    @Override
    protected void initData() {
        etTransferDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        tvChecker.setText(Global.LOGIN_ID);
        mSpecialFlag = cbSpecialFlag.isChecked() ? "Y" : "N";
        //初始化工厂
        mPresenter.getWorks(0);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void showWorks(List<WorkEntity> works) {
        mWorkDatas.clear();
        mWorkDatas.addAll(works);
        WorkAdapter adapter = new WorkAdapter(mActivity, android.R.layout.simple_list_item_1, mWorkDatas);
        spWork.setAdapter(adapter);
    }

    @Override
    public void loadWorksFail(String message) {
        showMessage(message);
        SpinnerAdapter adapter = spWork.getAdapter();
        if (adapter != null && WorkAdapter.class.isInstance(adapter)) {
            mWorkDatas.clear();
            WorkAdapter workAdapter = (WorkAdapter) adapter;
            workAdapter.notifyDataSetChanged();
        }
    }

    //工厂初始化完毕
    @Override
    public void loadWorkSComplete() {
        //初始化仓库号
        mPresenter.getStorageNums(0);
    }

    @Override
    public void showInvs(List<InvEntity> invs) {
        mInvDatas.clear();
        mInvDatas.addAll(invs);
        if (mInvAdapter == null) {
            mInvAdapter = new InvAdapter(mActivity, android.R.layout.simple_list_item_1,
                    mInvDatas);
            spInv.setAdapter(mInvAdapter);
        } else {
            mInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInvsFail(String message) {
        showMessage(message);
        if (mInvAdapter != null) {
            mInvDatas.clear();
            mInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInvsComplete() {

    }

    @Override
    public void showStorageNums(List<String> storageNums) {
        mStorageNums.clear();
        mStorageNums.addAll(storageNums);
        ArrayAdapter<String> adapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1,
                mStorageNums);
        spStorageNum.setAdapter(adapter);
    }

    @Override
    public void loadStorageNumFail(String message) {
        showMessage(message);
        SpinnerAdapter adapter = spWork.getAdapter();
        if (adapter != null && ArrayAdapter.class.isInstance(adapter)) {
            mWorkDatas.clear();
            ArrayAdapter workAdapter = (ArrayAdapter) adapter;
            workAdapter.notifyDataSetChanged();
        }
    }

    //仓库号加载完毕
    @Override
    public void loadStorageNumComplete() {
        //加载仓储类型
        if (isOpenLocationType) {
            mPresenter.getDictionaryData("locationType");
        }
    }

    @Override
    public void deleteCacheSuccess() {
        showMessage("删除盘点历史成功");
        startCheck();
    }

    @Override
    public void deleteCacheFail(String message) {
        showMessage(message);
    }

    @Override
    public void bindCommonHeaderUI() {

    }

    /**
     * 用户初始化盘点之前进行必要的检查
     *
     * @return
     */
    @Override
    public boolean checkDataBeforeOperationOnHeader() {
        //如果仓位级选中，那么检查仓库号
        if (spStorageNum.getSelectedItemPosition() == 0) {
            showMessage("请选择仓库号");
            return false;
        }
        return true;
    }

    /**
     * 显示开始盘点和重新盘点两个菜单
     *
     * @param companyCode
     */
    @Override
    public void operationOnHeader(String companyCode) {

        View rootView = LayoutInflater.from(mActivity).inflate(com.richfit.sdk_wzpd.R.layout.dialog_bottom_menu, null);
        GridView menu = (GridView) rootView.findViewById(com.richfit.sdk_wzpd.R.id.gd_menus);
        BottomDialogMenuAdapter adapter = new BottomDialogMenuAdapter(mActivity, com.richfit.sdk_wzpd.R.layout.item_dialog_bottom_menu, provideDefaultBottomMenu());
        menu.setAdapter(adapter);

        final Dialog dialog = new Dialog(mActivity, com.richfit.sdk_wzpd.R.style.MaterialDialogSheet);
        dialog.setContentView(rootView);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        menu.setOnItemClickListener((adapterView, view, position, id) -> {
            switch (position) {
                case 0:
                    //1.开始盘点
                    startCheck();
                    break;
                case 1:
                    //2.重新盘点
                    restartCheck();
                    break;
            }
            dialog.dismiss();
        });

    }

    /**
     * 开始盘点
     */
    protected void startCheck() {
        //请求抬头信息
        mRefData = null;
        Map<String, Object>  extraMap = new HashMap<>();
        if (isOpenLocationType) {
            extraMap.put("locationType", mLocationTypes.get(spLocationType.getSelectedItemPosition()).code);
        }
        //库位级盘点
        if (mStorageNums == null || mStorageNums.size() <= 0) {
            showMessage("仓库号未初始化");
            return;
        }

        if (mWorkDatas == null || mWorkDatas.size() == 0) {
            showMessage("工厂未初始化");
            return;
        }
        if (mInvDatas == null || mInvDatas.size() == 0) {
            showMessage("库存地点未初始化");
        }
        String workId = mWorkDatas.get(spWork.getSelectedItemPosition()).workId;
        String workCode = mWorkDatas.get(spWork.getSelectedItemPosition()).workCode;
        String invId = mInvDatas.get(spInv.getSelectedItemPosition()).invId;
        String invCode = mInvDatas.get(spInv.getSelectedItemPosition()).invCode;
        extraMap.put("workCode",workCode);
        extraMap.put("invCode",invCode);
        //库位级盘点获取盘点信息
        mPresenter.getCheckInfo(Global.USER_ID, mBizType, "01",
                mSpecialFlag, mStorageNums.get(spStorageNum.getSelectedItemPosition()),
                workId, invId, getString(etTransferDate), extraMap);
    }

    /**
     * 重新开始盘点。先调用删除整单的接口删除历史的盘点数据，然后调用getCheckInfo获取新的盘点数据
     */
    private void restartCheck() {

        if (mRefData == null) {
            showMessage("请先点击开始盘点");
            return;
        }
        if (TextUtils.isEmpty(mRefData.checkId)) {
            showMessage("请先点击开始盘点");
            return;
        }

        //仓位级别
        mPresenter.deleteCheckData(mStorageNums.get(spStorageNum.getSelectedItemPosition()),
                "", "", mRefData.checkId, USER_ID, mBizType);
    }


    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        ArrayList<BottomMenuEntity> menus = new ArrayList<>();
        BottomMenuEntity menu = new BottomMenuEntity();
        menu.menuName = "开始盘点";
        menu.menuImageRes = com.richfit.sdk_wzpd.R.mipmap.icon_start_check;
        menus.add(menu);

        menu = new BottomMenuEntity();
        menu.menuName = "重新盘点";
        menu.menuImageRes = com.richfit.sdk_wzpd.R.mipmap.icon_restart_check;
        menus.add(menu);
        return menus;
    }

    @Override
    public void getCheckInfoSuccess(ReferenceEntity refData) {
        SPrefUtil.saveData(mBizType, "0");
        refData.bizType = mBizType;
        refData.refType = mRefType;
        mRefData = refData;
        showMessage("成功获取到盘点列表");
    }

    @Override
    public void getCheckInfoFail(String message) {
        mRefData = null;
        showMessage(message);
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        List<SimpleEntity> locationTypes = data.get("locationType");
        if (locationTypes != null) {
            if (mLocationTypes == null) {
                mLocationTypes = new ArrayList<>();
            }
            mLocationTypes.clear();
            mLocationTypes.addAll(locationTypes);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, com.richfit.sdk_wzpd.R.layout.item_simple_sp, mLocationTypes, false);
            spLocationType.setAdapter(adapter);
        }
    }

    @Override
    public void loadDictionaryDataFail(String message) {
        showMessage(message);
    }

    @Override
    public void _onPause() {
        super._onPause();
        //再次检查用户是否输入的额外字段而且必须输入的字段（情景是用户请求单据之前没有输入该字段，回来填上后，但是没有请求单据而是直接）
        //切换了页面
        if (mRefData != null) {
            mRefData.voucherDate = getString(etTransferDate);
            mRefData.storageNum = mStorageNums.get(spStorageNum.getSelectedItemPosition());
            int selectedWorkPosition = spWork.getSelectedItemPosition();
            int selectedInvPosition = spInv.getSelectedItemPosition();

            if (selectedWorkPosition > 0) {
                mRefData.workId = mWorkDatas.get(selectedWorkPosition).workId;
                mRefData.workCode = mWorkDatas.get(selectedWorkPosition).workCode;
            }

            if (selectedInvPosition > 0) {
                mRefData.invId = mInvDatas.get(selectedInvPosition).invId;
                mRefData.invCode = mInvDatas.get(selectedInvPosition).invCode;
            }
            mRefData.checkLevel = "01";
            mRefData.specialFlag = mSpecialFlag;
        }
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return true;
    }


    @Override
    public void retry(String action) {
        switch (action) {
            case Global.RETRY_LOAD_REFERENCE_ACTION:
                startCheck();
                break;
            case Global.RETRY_DELETE_TRANSFERED_CACHE_ACTION:
                restartCheck();
                break;
        }
        super.retry(action);
    }
}
