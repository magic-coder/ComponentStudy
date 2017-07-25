package com.richfit.module_cqyt.module_ys;


import android.view.View;
import android.widget.TextView;

import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

/**
 * 注意该业务比较特别，每一个单据的所有的行明细都是由一行生成拆分出来的，这就意味着
 * 单据中所有的行的refLineId和lineNum都一致，所以不能将它作为唯一的表示来标识该行。
 * 这里采用的方式使用每一行明细的insLot字段来标识每一行的明细。
 * Created by monday on 2017/3/1.
 */

public class CQYTAOCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        TextView tvRefLineNumName = (TextView) mView.findViewById(R.id.tv_ref_line_num_name);
        tvRefLineNumName.setText("检验批");
        llInsLostQuantity.setVisibility(View.VISIBLE);
        tvActQuantityName.setText("允许过账数量");
        tvQuantityName.setText("过账数量");
    }


    @Override
    public void initDataLazily() {
    }


    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));
    }

    @Override
    public void initData() {

    }


    @Override
    protected boolean refreshQuantity(final String quantity) {

        //1.实收数量必须大于0
        final float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入实收数量不合理");
            return false;
        }

        //2.实收数量必须小于合格数量(应收数量)
        final float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        if (Float.compare(quantityV, actQuantityV) > 0.0f) {
            showMessage("实收数量不能大于应收数量");
            return false;
        }

        // 3. 实收数量 <= 检验批数量
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        final float orderQuantityV = CommonUtil.convertToFloat(lineData.orderQuantity, 0.0f);
        if (Float.compare(quantityV , orderQuantityV) > 0.0f) {
            showMessage("实收数量不能大于检验批数量");
            if (!cbSingle.isChecked())
                etQuantity.setText("");
            return false;
        }
        return true;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
