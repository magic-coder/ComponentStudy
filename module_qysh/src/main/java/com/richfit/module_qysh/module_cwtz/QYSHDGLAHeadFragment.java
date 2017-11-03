package com.richfit.module_qysh.module_cwtz;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_cwtz.head.LAHeadFragment;

/**
 * 代管料仓位调整
 * Created by monday on 2017/11/2.
 */

public class QYSHDGLAHeadFragment extends LAHeadFragment {

    RichEditText etTransferDate;

    @Override
    protected int getContentId() {
        return R.layout.qysh_fragment_la_head;
    }

    @Override
    protected void initView() {
        super.initView();
        etTransferDate = mView.findViewById(R.id.et_transfer_date);
    }

    @Override
    protected void initData() {
        super.initData();
        etTransferDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        //过账日期
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        RxAdapterView.itemSelections(spInv)
                .filter(position -> position.intValue() > 0)
                .filter(position -> "Y".equalsIgnoreCase(Global.WMFLAG))
                .subscribe(a -> {});
    }


    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData == null) {
            mRefData = new ReferenceEntity();
        }
        mRefData.voucherDate = getString(etTransferDate);
    }

    @Override
    public int getOrgFlag() {
        return 1;
    }
}
