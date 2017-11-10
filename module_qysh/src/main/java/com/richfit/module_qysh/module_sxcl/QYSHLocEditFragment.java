package com.richfit.module_qysh.module_sxcl;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_sxcl.baseedit.LocQTEditFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/7/28.
 */

public class QYSHLocEditFragment extends LocQTEditFragment {

    @Override
    protected void initView() {
        super.initView();
        //隐藏批次
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if(batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
        //隐藏下架仓位
        View xLocation = mView.findViewById(R.id.ll_x_location);
        if(xLocation != null) {
            xLocation.setVisibility(View.GONE);
        }
        //隐藏库存数量
        View invQuantity = mView.findViewById(R.id.ll_inv_quantity);
        if(invQuantity != null) {
            invQuantity.setVisibility(View.GONE);
        }

        //修改上下架数量，和上下架累计数量
        TextView tvQuantityName = mView.findViewById(R.id.tv_quantity_name);
        if(tvQuantityName != null) {
            tvQuantityName.setText("上架数量");
        }

        TextView tvTotalQuantityName = mView.findViewById(R.id.tv_total_quantity_name);
        if(tvTotalQuantityName != null) {
            tvTotalQuantityName.setText("累计数量");
        }
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.invFlag = mRefData.invFlag;
        result.specialInvFlag = mRefData.specialInvFlag;
        result.projectNum = mRefData.projectNum;
        result.batchFlag = null;
        result.specialInvFlag = mRefData.specialInvFlag;
        return result;
    }
    /**
     * 更改获取库存维度
     * @return
     */
    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = TextUtils.isEmpty( mRefData.invType) ? "1" :  mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
