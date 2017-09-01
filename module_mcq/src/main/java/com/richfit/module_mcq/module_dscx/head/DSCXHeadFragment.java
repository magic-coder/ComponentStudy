package com.richfit.module_mcq.module_dscx.head;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.module_mcq.R;

/**
 * Created by monday on 2017/8/28.
 */

public class DSCXHeadFragment extends BaseHeadFragment<DSCXHeadPresenterImp> {

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
    protected void initVariable(@Nullable Bundle savedInstanceState) {

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

        if(TextUtils.isEmpty(getString(etCreateDate))) {
            showMessage("请先输入创建日期");
            return false;
        }
        return true;
    }


    @Override
    public void operationOnHeader(final String companyCode) {
        //todo 调用远程接口进行查询将数据保存到...
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        clearCommonUI(etCreateDate,etCreator);
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return true;
    }
}
