package com.richfit.module_mcq.module_dscx.head;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.richfit.common_lib.lib_adapter_rv.MultiItemTypeAdapter;
import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.lib_interface.IBarcodeSystemMain;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.module_mcq.R;
import com.richfit.module_mcq.adapter.ASCXRefsAdapter;
import com.richfit.module_mcq.adapter.DSCXRefsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/8/28.
 */

public class DSCXHeadFragment extends BaseHeadFragment<DSCXHeadPresenterImp>
        implements IDSCXHeadView, MultiItemTypeAdapter.OnItemClickListener {

    RichEditText etCreateDate;
    EditText etCreator;
    RecyclerView rvRefs;
    List<ReferenceEntity> mRefList;

    @Override
    protected int getContentId() {
        return R.layout.mcq_fragment_dscx_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSCXHeadPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        etCreateDate = mView.findViewById(R.id.et_create_date);
        etCreator = mView.findViewById(R.id.et_creator);
        rvRefs = mView.findViewById(R.id.base_detail_recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvRefs.setLayoutManager(lm);
    }

    @Override
    public void initEvent() {
        //监听点击事件
        etCreateDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etCreateDate, Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }


    @Override
    public boolean checkDataBeforeOperationOnHeader() {

        if (TextUtils.isEmpty(getString(etCreateDate))) {
            showMessage("请先输入创建日期");
            return false;
        }
        return true;
    }


    @Override
    public void operationOnHeader(final String companyCode) {
        //创建日期必输
        String createdBy = getString(etCreator);
        String creationDate = getString(etCreateDate);
        //清空上次已经选中的单据数据
        Map<String, Object> extraMap = new HashMap<>();
        //上架单据查询
        extraMap.put("refType", mRefType);
        mRefData = null;
        mPresenter.getArrivalInfo(createdBy, creationDate, extraMap);
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        clearCommonUI(etCreateDate, etCreator);
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return true;
    }

    //加载单据列表成功
    @Override
    public void loadRefListSuccess(List<ReferenceEntity> refs) {
        if (mRefList == null) {
            mRefList = new ArrayList<>();
        }
        mRefList.clear();
        mRefList.addAll(refs);
        DSCXRefsAdapter adapter = new DSCXRefsAdapter(mActivity, R.layout.mcq_item_dscx_refs, mRefList);
        rvRefs.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void loadRefListFail(String message) {
        showMessage(message);
    }

    /**
     * 明细点击
     *
     * @param view
     * @param holder
     * @param position
     */
    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        ReferenceEntity item = mRefList.get(position);
        if (mRefData == null) {
            //点击之后携带相关信息给出明细界面，然后明细界面根据相关信息获取该单据的明细进行展示
            mRefData = new ReferenceEntity();
        }
        //这里给出的是getReferenceInfo接口需要的参数，也就是上架模块需要的bizType和refType
        mRefData.bizType = "114";
        mRefData.refType = "17";
        mRefData.recordNum = item.recordNum;
        //跳转到明细界面
        if (mActivity instanceof IBarcodeSystemMain) {
            IBarcodeSystemMain activity = (IBarcodeSystemMain) mActivity;
            activity.showFragmentByPosition(BaseFragment.DETAIL_FRAGMENT_INDEX);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
