package com.richfit.module_mcq.module_dscx.head;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.richfit.common_lib.lib_adapter_rv.MultiItemTypeAdapter;
import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.lib_interface.IBarcodeSystemMain;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.module_mcq.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/8/28.
 */

public class DSCXHeadFragment extends BaseHeadFragment<DSCXHeadPresenterImp>
        implements IDSCXHeadView {

    RichEditText etCreateDate;
    EditText etCreator;

    @Override
    protected int getContentId() {
        return R.layout.mcq_fragment_dscx_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSCXHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etCreateDate = mView.findViewById(R.id.et_create_date);
        etCreator = mView.findViewById(R.id.et_creator);
    }

    @Override
    public void initEvent() {
        //监听点击事件
        etCreateDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etCreateDate, Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }


    @Override
    public boolean checkDataBeforeOperationOnHeader() {

        if (TextUtils.isEmpty(getString(etCreateDate))) {
            showMessage("请先输入创建日期");
            return false;
        }
        return true;
    }


    @Override
    public void operationOnHeader(final String companyCode) {

    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        clearCommonUI(etCreateDate, etCreator);
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData == null) {
            mRefData = new ReferenceEntity();
        }
        //创建日期必输
        String createdBy = getString(etCreator);
        String creationDate = getString(etCreateDate);
        mRefData.createdBy = createdBy;
        mRefData.creationDate = creationDate;
        mRefData.bizType = mBizType;
        mRefData.refType = mRefType;
    }

}
