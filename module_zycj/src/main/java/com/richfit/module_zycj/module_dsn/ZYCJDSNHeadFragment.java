package com.richfit.module_zycj.module_dsn;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_zycj.R;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;
import com.richfit.sdk_wzck.base_dsn_head.imp.DSNHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2018/1/10.
 */

public class ZYCJDSNHeadFragment extends BaseDSNHeadFragment<DSNHeadPresenterImp> {

    AutoCompleteTextView etProjectNum;
    EditText etSerialNum;
    Spinner spMoveType;
    Spinner spMoveCause;

    @Override
    public int getContentId() {
        return R.layout.zycj_fragment_dsn_head;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DSNHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etProjectNum = mView.findViewById(R.id.zycj_et_project_num);
        etSerialNum = mView.findViewById(R.id.zycj_et_serial_num);
        spMoveType = mView.findViewById(R.id.sp_move_type);
        spMoveCause = mView.findViewById(R.id.sp_move_cause);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void initData() {
        super.initData();
        //加载移动类型和移动原因字典数据
        mPresenter.getDictionaryData("moveCause", "moveType");
    }


    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        List<SimpleEntity> moveCauses = data.get("moveCause");
        List<SimpleEntity> moveTypes = data.get("moveType");
        initSpAdapter(moveCauses, spMoveCause);
        initSpAdapter(moveTypes, spMoveType);
    }


    private void initSpAdapter(List<SimpleEntity> datas, Spinner spinner) {
        if (datas == null || datas.size() == 0)
            return;
        List<String> list = CommonUtil.toStringArray(datas, true);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, list);
        spinner.setAdapter(adapter);
    }


    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    protected String getAutoComDataType() {
        //加载成本中心
        return Global.COST_CENTER_DATA;
    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            if(spMoveCause.getAdapter() != null) {
                String item = (String) spMoveCause.getSelectedItem();
                mRefData.moveCause = item.split("0")[0];
            }

            if(spMoveType.getAdapter() != null) {
                String item = (String) spMoveType.getSelectedItem();
                mRefData.moveType = item.split("0")[0];
            }

            //项目编号
            mRefData.projectNum = getString(etProjectNum);
            //序列号
            mRefData.serialNum = getString(etSerialNum);
        }
    }
}
