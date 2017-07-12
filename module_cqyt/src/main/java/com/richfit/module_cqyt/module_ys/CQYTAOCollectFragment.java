package com.richfit.module_cqyt.module_ys;


import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

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


    /**
     * 通过物料编码和批次匹配单据明细的行。这里我们返回的所有行的insLot集合
     *
     * @param materialNum
     * @param batchFlag
     * @return
     */
    @Override
    protected Flowable<ArrayList<String>> matchMaterialInfo(final String materialNum, final String batchFlag) {
        if (mRefData == null || mRefData.billDetailList == null ||
                mRefData.billDetailList.size() == 0 || TextUtils.isEmpty(materialNum)) {
            return Flowable.error(new Throwable("请先单据明细"));
        }
        ArrayList<String> insLosts = new ArrayList<>();
        List<RefDetailEntity> list = mRefData.billDetailList;
        for (RefDetailEntity entity : list) {
            if (entity.batchManagerStatus) {
                final String insLot = entity.insLot;
                //如果打开了批次，那么在看明细中是否有批次
                if (!TextUtils.isEmpty(entity.batchFlag) && !TextUtils.isEmpty(batchFlag)) {
                    if (materialNum.equalsIgnoreCase(entity.materialNum) &&
                            batchFlag.equalsIgnoreCase(entity.batchFlag) &&
                            !TextUtils.isEmpty(insLot))

                        insLosts.add(insLot);
                } else {
                    if (materialNum.equalsIgnoreCase(entity.materialNum) &&
                            !TextUtils.isEmpty(insLot))
                        insLosts.add(insLot);
                }
            } else {
                final String insLot = entity.insLot;
                //如果明细中没有打开了批次管理,那么只匹配物料编码
                if (materialNum.equalsIgnoreCase(entity.materialNum) && !TextUtils.isEmpty(insLot))
                    insLosts.add(insLot);
            }
        }
        if (insLosts.size() == 0) {
            return Flowable.error(new Throwable("未获取到匹配的物料"));
        }
        return Flowable.just(insLosts);
    }


    /**
     * 通过单据行的检验批得到该行在单据明细列表中的位置
     *
     * @param insLot:单据行的检验批
     * @return 返回该行号对应的行明细在明细列表的索引
     */
    @Override
    protected int getIndexByLineNum(String insLot) {
        int index = -1;
        if (TextUtils.isEmpty(insLot))
            return index;

        if (mRefData == null || mRefData.billDetailList == null
                || mRefData.billDetailList.size() == 0)
            return index;

        for (RefDetailEntity detailEntity : mRefData.billDetailList) {
            index++;
            if (insLot.equalsIgnoreCase(detailEntity.insLot))
                break;

        }
        return index;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));
    }

    @Override
    public void initData() {

    }

    /**
     * 检查数量是否合理。需要满足
     * 1. 退货交货数量 <= 不不合格量
     * 2. 退货交货数量 + 实收数量 = 检验批数量
     * 3. 实收数量 <= 合格数量
     *
     * @param quantity
     * @return
     */
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
        final float inSlotQuantityV = CommonUtil.convertToFloat(lineData.insLotQuantity, 0.0f);
        if (Float.compare(quantityV , inSlotQuantityV) != 0.0f) {
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
