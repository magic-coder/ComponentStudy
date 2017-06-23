package com.richfit.module_xngd.module_ds.dsn;


import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_ds.dsn.imp.XNGDDSNHeadPresenterImp;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 西南无参考入库抬头增加客户化字段
 * 移动类型，是否应急，项目移交物资
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNHeadFragment extends BaseDSNHeadFragment<XNGDDSNHeadPresenterImp> {

    private Spinner spMoveType;
    private CheckBox cbInvFlag;
    private CheckBox cbProjectFlag;

    private List<String> mMoveTypes;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_dsn_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new XNGDDSNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        spMoveType = (Spinner) mView.findViewById(R.id.xngd_sp_move_type);
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
        cbProjectFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_project_flag);
    }

    @Override
    public void initData() {
        super.initData();
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
            mRefData.specialInvFlag = cbProjectFlag.isChecked() ? "Q" : "N";
            mRefData.moveType = mMoveTypes.get(spMoveType.getSelectedItemPosition());
        }
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
