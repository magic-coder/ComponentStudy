package com.richfit.module_xngd.module_ms.n311;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.widget.RichAutoEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzyk.base_msn_head.BaseMSNHeadFragment;
import com.richfit.sdk_wzyk.base_msn_head.imp.MSNHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Flowable;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGNMSNHeadFragment extends BaseMSNHeadFragment<MSNHeadPresenterImp> {

    //应急物资
    CheckBox cbInvFlag;
    //项目移交物资
    CheckBox cbInvType;
    //库存类型
    Spinner spSpecialInvType;
    //项目编号
    RichAutoEditText etProjectNum;
    List<String> mAutoDatas;
    ArrayAdapter mAutoAdapter;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_311n_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        //工厂内没有接收工厂，并且将发出工厂修改为工厂
        llRecWork.setVisibility(View.GONE);
        tvSendWorkName.setText("工厂");
        //应急物资
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
        //项目移交物资
        cbInvType = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_type);
        //库存类型
        spSpecialInvType = (Spinner) mView.findViewById(R.id.xngd_sp_special_inv_type);
        //项目编号
        etProjectNum = (RichAutoEditText) mView.findViewById(R.id.xngd_et_project_num);
    }

    @Override
    public void initEvent() {
        super.initEvent();

        //点击自动提示控件，显示默认列表
        RxView.clicks(etProjectNum)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> mAutoDatas != null && mAutoDatas.size() > 0)
                .subscribe(a -> showAutoCompleteConfig(etProjectNum));

        //用户选择自动提示控件的某一条数据，隐藏输入法
        RxAutoCompleteTextView.itemClickEvents(etProjectNum)
                .subscribe(a -> hideKeyboard(etProjectNum));

        //修改自动提示控件，说明用户需要用关键字进行搜索，如果默认的列表中存在，那么不在向数据库进行查询
        RxTextView.textChanges(etProjectNum)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(str -> !TextUtils.isEmpty(str) && mAutoDatas != null && mAutoDatas.size() > 0
                        && !filterKeyWord(str))
                .subscribe(a -> mPresenter.getProjectNumList(mSendWorks.get(spSendWork.getSelectedItemPosition()).workCode,
                        getString(etProjectNum), 100, 0, mBizType));
    }

    @Override
    public void initData() {
        super.initData();
        mAutoDatas = new ArrayList<>();
        //初始化库存类型，注意当用户选择项目物资的时候，项目编号必输
        ArrayList<String> items = new ArrayList<>();
        items.add("生产物资");
        items.add("项目物资");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, items);
        spSpecialInvType.setAdapter(adapter);
    }

    @Override
    public void initDataLazily() {

    }

    /**
     * 发出库位列表
     *
     * @param sendInvs
     */
    @Override
    public void showSendInvs(List<InvEntity> sendInvs) {
        mSendInvs.clear();
        mSendInvs.addAll(sendInvs);
        if (mSendInvAdapter == null) {
            mSendInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mSendInvs);
            spSendInv.setAdapter(mSendInvAdapter);
        } else {
            mSendInvAdapter.notifyDataSetChanged();
        }
        //同时初始化接收库位
        mRecInvs.clear();
        mRecInvs.addAll(sendInvs);
        if (mRecInvAdapter == null) {
            mRecInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mRecInvs);
            spRecInv.setAdapter(mRecInvAdapter);
        } else {
            mRecInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadSendInvsFail(String message) {
        showMessage(message);
    }

    @Override
    public void loadSendInvsComplete() {
        if (mUploadMsgEntity != null && !TextUtils.isEmpty(mUploadMsgEntity.invId)) {
            Flowable.just(mSendInvs)
                    .map(list -> {
                        int pos = -1;
                        for (InvEntity item : list) {
                            ++pos;
                            //注意请选择没有id
                            if (mUploadMsgEntity.invId.equalsIgnoreCase(item.invId))
                                return pos;
                        }
                        return pos;
                    })
                    .filter(pos -> pos.intValue() >= 0 && pos.intValue() < mSendInvs.size())
                    .compose(TransformerHelper.io2main())
                    .subscribe(pos -> spSendInv.setSelection(pos.intValue()), e -> Log.d("yff", e.getMessage()), () -> lockUIUnderEditState(spSendInv));
        }


        if (mUploadMsgEntity != null && !TextUtils.isEmpty(mUploadMsgEntity.recInvId)) {
            Flowable.just(mRecInvs)
                    .map(list -> {
                        int pos = -1;
                        for (InvEntity item : list) {
                            ++pos;
                            //注意请选择没有id
                            if (mUploadMsgEntity.recInvId.equalsIgnoreCase(item.invId))
                                return pos;
                        }
                        return pos;
                    })
                    .filter(pos -> pos.intValue() >= 0 && pos.intValue() < mRecInvs.size())
                    .compose(TransformerHelper.io2main())
                    .subscribe(pos -> spRecInv.setSelection(pos.intValue()), e -> Log.d("yff", e.getMessage()), () -> lockUIUnderEditState(spRecInv));
        }
        //加载项目编号
        mPresenter.getProjectNumList(mSendWorks.get(spSendWork.getSelectedItemPosition()).workCode,
                getString(etProjectNum), 100, 0, mBizType);
    }

    private boolean filterKeyWord(CharSequence keyWord) {
        Pattern pattern = Pattern.compile("^" + keyWord.toString().toUpperCase());
        for (String item : mAutoDatas) {
            Matcher matcher = pattern.matcher(item);
            while (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void _onPause() {
        super._onPause();
        //工厂内移库，默认接收工厂默认等于接收工厂
        if (mRefData != null) {
            mRefData.recWorkName = mRefData.workName;
            mRefData.recWorkCode = mRefData.workCode;
            mRefData.recWorkId = mRefData.workId;
            //应急物资
            mRefData.invFlag = cbInvFlag.isChecked() ? "1" : "0";
            //项目移交物资
            mRefData.invType = cbInvType.isChecked() ? "0" : "1";
            //库存类型
            mRefData.specialInvFlag = spSpecialInvType.getSelectedItemPosition() <= 0 ? "N" :
                    "Q";
            String projectNum = getString(etProjectNum);
            if (!TextUtils.isEmpty(projectNum)) {
                mRefData.projectNum = projectNum.split("_")[0];
            }
        }
    }

    @Override
    public void operationOnHeader(final String companyCode) {
        if (mLocalHeaderResult == null) {
            mLocalHeaderResult = new ResultEntity();
        }
        mLocalHeaderResult.voucherDate = getString(etTransferDate);
        mLocalHeaderResult.transId = mUploadMsgEntity.transId;
        mLocalHeaderResult.businessType = mBizType;
        super.operationOnHeader(companyCode);
    }


    @Override
    public void showRecInvs(List<InvEntity> recInvs) {

    }

    @Override
    public void loadRecInvsFail(String message) {
    }

    @Override
    public void loadRecInvsComplete() {

    }

    @Override
    public void showProjectNums(Map<String, List<SimpleEntity>> map) {
        List<SimpleEntity> simpleEntities = map.get(Global.PROJECT_NUM_DATA);
        if (simpleEntities == null || simpleEntities.size() == 0) {
            return;
        }
        List<String> projectNums = CommonUtil.toStringArray(simpleEntities,true);
        mAutoDatas.clear();
        mAutoDatas.addAll(projectNums);
        if (mAutoAdapter == null) {
            mAutoAdapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, mAutoDatas);
            etProjectNum.setAdapter(mAutoAdapter);
            setAutoCompleteConfig(etProjectNum);
        } else {
            mAutoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadProjectNumsFail(String message) {
        showMessage(message);
    }

    @Override
    protected String getMoveType() {
        return "5";
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {

    }
}