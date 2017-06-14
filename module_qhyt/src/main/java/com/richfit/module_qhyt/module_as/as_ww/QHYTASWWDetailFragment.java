package com.richfit.module_qhyt.module_as.as_ww;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.util.List;

/**
 * 物资委外入库
 * Created by monday on 2017/2/20.
 */

public class QHYTASWWDetailFragment extends BaseASDetailFragment<ASDetailPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new ASDetailPresenterImp(mActivity);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(1).transToSapFlag = "05";
        if (mRefData == null) {
            return menus;
        }
        //如果全部是必检物资那么不需要上架
        return menus.subList(0, mRefData.qmFlag ? 1 : 2);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    /**
     * 第一步过账成功显示物料凭证
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mTransNum);
        if (mRefData.qmFlag) {
            setRefreshing(false, "过账成功");
            if (mAdapter != null) {
                mAdapter.removeAllVisibleNodes();
            }
            //注意这里必须清除单据数据
            mRefData = null;
            mTransNum = "";
            mTransId = "";
            SPrefUtil.saveData(mBizType + mRefType, "0");
            mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
        }
    }

    @Override
    protected String getSubFunName() {
        return "委外入库";
    }
}
