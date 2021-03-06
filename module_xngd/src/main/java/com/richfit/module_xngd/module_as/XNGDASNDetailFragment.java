package com.richfit.module_xngd.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.adapter.XNGDASNDetailAdapter;
import com.richfit.module_xngd.module_as.imp.XNGDASNDetailPresenterImp;
import com.richfit.sdk_wzrk.base_asn_detail.BaseASNDetailFragment;
import com.richfit.sdk_wzrk.base_asn_detail.imp.ASNDetailPresenterImp;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNDetailFragment extends BaseASNDetailFragment<XNGDASNDetailPresenterImp> {

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_asn_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new XNGDASNDetailPresenterImp(mActivity);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void showNodes(List<RefDetailEntity> nodes) {
        for (RefDetailEntity node : nodes) {
            if (!TextUtils.isEmpty(node.transId)) {
                mTransId = node.transId;
                break;
            }
        }
        if (mAdapter == null) {
            mAdapter = new XNGDASNDetailAdapter(mActivity, R.layout.xngd_item_asn_parent_item, nodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(nodes);
        }
    }

    /**
     * 第一步过账成功后直接跳转
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0, 1);
    }
}
