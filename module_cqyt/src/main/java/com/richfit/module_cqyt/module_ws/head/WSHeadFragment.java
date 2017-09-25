package com.richfit.module_cqyt.module_ws.head;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.WorkAdapter;
import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.module_cqyt.R;

import java.util.List;

/**
 * 物资取样抬头界面
 * Created by monday on 2017/9/21.
 */

public class WSHeadFragment extends BaseHeadFragment<WSHeadPresenterImp>
        implements IWSHeadView {

    Spinner spWork;
    RichEditText etTransferDate;

    /*工厂列表*/
    List<WorkEntity>  mWorkDatas;
    /*库存地点列表以及适配器*/
    InvAdapter mInvAdapter;

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_ws_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new WSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mRefData = null;
    }

    @Override
    protected void initView() {
        spWork = mView.findViewById(R.id.sp_work);
        etTransferDate = mView.findViewById(R.id.et_transfer_date);
    }

    @Override
    public void initEvent() {
        //选择过账日期
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    public void initData() {
        etTransferDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));

    }

    @Override
    public void initDataLazily() {

    }

    /**
     * 显示工厂
     *
     * @param works
     */
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

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData == null) {
            mRefData = new ReferenceEntity();
        }
        if(mWorkDatas != null && mWorkDatas.size() > 0 && spWork.getSelectedItemPosition() > 0) {
            mRefData.workCode = mWorkDatas.get(spWork.getSelectedItemPosition()).workCode;
            mRefData.workId = mWorkDatas.get(spWork.getSelectedItemPosition()).workId;
        }
        mRefData.voucherDate = getString(etTransferDate);
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {

    }
}
