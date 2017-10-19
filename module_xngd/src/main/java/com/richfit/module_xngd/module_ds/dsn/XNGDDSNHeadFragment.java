package com.richfit.module_xngd.module_ds.dsn;


import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;
import com.richfit.sdk_wzck.base_dsn_head.imp.DSNHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 西南无参考入库抬头增加客户化字段
 * 移动类型，是否应急，项目移交物资
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNHeadFragment extends BaseDSNHeadFragment<DSNHeadPresenterImp> {

    //移动类型
    private Spinner spMoveType;
    //应急物资
    private CheckBox cbInvFlag;
    //项目移交物资
    private CheckBox cbInvType;
    //过账日期
    private TextView tvTransferDateName;
    //总账科目
    AutoCompleteTextView etGLAccount;
    //订单
    AutoCompleteTextView etOrderNum;


    private List<String> mMoveTypes;
    private List<String> mGLAccounts;
    private List<String> mOrderNums;


    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_dsn_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSNHeadPresenterImp(mActivity);
    }


    @Override
    protected void initView() {
        tvTransferDateName = (TextView) mView.findViewById(R.id.tv_transfer_date);
        spMoveType = (Spinner) mView.findViewById(R.id.xngd_sp_move_type);
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
        cbInvType = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_type);
        etGLAccount = (AutoCompleteTextView) mView.findViewById(R.id.xngd_et_gl_account);
        etOrderNum = mView.findViewById(R.id.xngd_et_io_account);
    }

    /**
     * 注册点击事件
     */
    @Override
    public void initEvent() {
        super.initEvent();
        //选择工厂，初始化成本中心和总账科目
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position ->
                    mPresenter.getAutoComList(mWorks.get(position).workCode,null,
                            getString(etAutoComp), 100, getOrgFlag(), mBizType, Global.COST_CENTER_DATA,
                            Global.GL_ACCOUNT_DATA,Global.INTERNAL_ORDER_DATA)
                );
        //点击自动提示控件，显示默认列表
        RxView.clicks(etGLAccount)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> mGLAccounts != null && mGLAccounts.size() > 0)
                .subscribe(a -> showAutoCompleteConfig(etGLAccount));

        //用户选择自动提示控件的某一条数据，隐藏输入法
        RxAutoCompleteTextView.itemClickEvents(etGLAccount)
                .subscribe(a -> hideKeyboard(etGLAccount));

        //修改自动提示控件，说明用户需要用关键字进行搜索，如果默认的列表中存在，那么不在向数据库进行查询
        RxTextView.textChanges(etGLAccount)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(str -> !TextUtils.isEmpty(str) && mGLAccounts != null &&
                        mGLAccounts.size() > 0 && !filterKeyWord(str,mGLAccounts) && spWork.getSelectedItemPosition() > 0)
                .subscribe(a ->  mPresenter.getAutoComList(mWorks.get(spWork.getSelectedItemPosition()).workCode,null,
                        getString(etGLAccount), 100, getOrgFlag(), mBizType, Global.GL_ACCOUNT_DATA));

        //------------------------------------------------------------------------------------------
        //订单
        //点击自动提示控件，显示默认列表
        RxView.clicks(etOrderNum)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> mOrderNums != null && mOrderNums.size() > 0)
                .subscribe(a -> showAutoCompleteConfig(etOrderNum));

        //用户选择自动提示控件的某一条数据，隐藏输入法
        RxAutoCompleteTextView.itemClickEvents(etOrderNum)
                .subscribe(a -> hideKeyboard(etOrderNum));

        //修改自动提示控件，说明用户需要用关键字进行搜索，如果默认的列表中存在，那么不在向数据库进行查询
        RxTextView.textChanges(etOrderNum)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(str -> !TextUtils.isEmpty(str) && mOrderNums != null &&
                        mOrderNums.size() > 0 && !filterKeyWord(str,mOrderNums) && spWork.getSelectedItemPosition() > 0)
                .subscribe(a ->  mPresenter.getAutoComList(mWorks.get(spWork.getSelectedItemPosition()).workCode,null,
                        getString(etOrderNum), 100, getOrgFlag(), mBizType, Global.INTERNAL_ORDER_DATA));
    }

    @Override
    public void initData() {
        super.initData();
        tvTransferDateName.setText("领料日期");
        if (mMoveTypes == null) {
            mMoveTypes = new ArrayList<>();
        }
        mMoveTypes.add("Z87");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, mMoveTypes);
        spMoveType.setAdapter(adapter);
    }


    @Override
    public void initDataLazily() {

    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            //保存抬头的移动类型，是否应急，项目移交物资，
            mRefData.invFlag = cbInvFlag.isChecked() ? "1" : "0";
            //项目移交物资
            mRefData.invType = cbInvType.isChecked() ? "0" : "1";
            //移动类型
            mRefData.moveType = mMoveTypes.get(spMoveType.getSelectedItemPosition());
            //总账科目
            mRefData.glAccount = getString(etGLAccount).split("_")[0];
            //订单
            mRefData.orderNum = getString(etOrderNum).split("_")[0];
            //成本中心
            String costCenter = getString(etAutoComp);
            if (!TextUtils.isEmpty(costCenter)) {
                mRefData.costCenter = costCenter.split("_")[0];
            }
        }
    }


    @Override
    public void showAutoCompleteList(Map<String,List<SimpleEntity>> map) {
        List<SimpleEntity> glAccounts = map.get(Global.GL_ACCOUNT_DATA);
        if(glAccounts == null || glAccounts.size() == 0) {
            //注意这里可能是父类的数据
            super.showAutoCompleteList(map);
            return;
        }
        List<String> glAccountList=  CommonUtil.toStringArray(glAccounts,true);
        if(mGLAccounts == null) {
            mGLAccounts = new ArrayList<>();
        }
        mGLAccounts.clear();
        mGLAccounts.addAll(glAccountList);
        ArrayAdapter<String>  glAdapter = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_dropdown_item_1line, mGLAccounts);
        etGLAccount.setAdapter(glAdapter);
        setAutoCompleteConfig(etGLAccount);


        //初始化订单编号
        List<SimpleEntity> orderNums = map.get(Global.INTERNAL_ORDER_DATA);
        if(orderNums == null || orderNums.size() == 0) {
            //注意这里可能是父类的数据
            super.showAutoCompleteList(map);
            return;
        }
        List<String> orderNumList=  CommonUtil.toStringArray(orderNums,false);
        if(mOrderNums == null) {
            mOrderNums = new ArrayList<>();
        }
        mOrderNums.clear();
        mOrderNums.addAll(orderNumList);
        ArrayAdapter<String>  orderNumAdapter = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_dropdown_item_1line, mOrderNums);
        etOrderNum.setAdapter(orderNumAdapter);
        setAutoCompleteConfig(etOrderNum);


        //注意回调父类方法初始化它的数据
        super.showAutoCompleteList(map);
    }


    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    protected String getAutoComDataType() {
        return Global.COST_CENTER_DATA;
    }

}
