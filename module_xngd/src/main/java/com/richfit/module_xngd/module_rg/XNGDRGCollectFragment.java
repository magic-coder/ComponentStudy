package com.richfit.module_xngd.module_rg;

import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增加退货原因
 * Created by monday on 2017/6/20.
 */

public class XNGDRGCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    private Spinner spMoveCause;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_rg_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        quantityName.setText("实退数量");
        actQuantityName.setText("应退数量");
        spMoveCause = mView.findViewById(R.id.xngd_sp_move_causes);
    }

    @Override
    public void initData() {
        //初始化退货原因
        List<String> moveCauses = getStringArray(R.array.xngd_move_causes);
        List<String> moveCauseCodes = getStringArray(R.array.xngd_move_cause_codes);
        List<SimpleEntity> items = new ArrayList<>();
        for (int i = 0; i < moveCauseCodes.size(); i++) {
            SimpleEntity item = new SimpleEntity();
            item.name = moveCauses.get(i);
            item.code = moveCauseCodes.get(i);
            items.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, items);
        spMoveCause.setAdapter(adapter);

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (spMoveCause.getSelectedItemPosition() <= 0) {
            showMessage("请先选择退货原因");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.moveCause = ((SimpleEntity) spMoveCause.getSelectedItem()).code;
        return result;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = "1";
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
    @Override
    public void _onPause() {
        if (spMoveCause.getAdapter() != null) {
            spMoveCause.setSelection(0);
        }
        super._onPause();
    }
}
