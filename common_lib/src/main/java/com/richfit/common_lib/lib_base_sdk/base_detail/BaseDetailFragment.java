package com.richfit.common_lib.lib_base_sdk.base_detail;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.richfit.common_lib.R;
import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_interface.IAdapterState;
import com.richfit.common_lib.lib_interface.IOnItemMove;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.lib_rv_animation.Animation.animators.FadeInDownAnimator;
import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.common_lib.widget.AutoSwipeRefreshLayout;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.TreeNode;

import java.util.HashMap;
import java.util.List;

/**
 * 所有明细界面公共基类。注意因为明细界面的控件已经固定，所以可以抽象出来。
 * 2017年7月20日进行修改，将数据上传，上架，下架等所有任务的消息(包括凭证和错误消息)
 * 统一处理，子类字需要关心该任务正确时的处理。
 * Created by monday on 2017/3/17.
 */

public abstract class BaseDetailFragment<P extends IBaseDetailPresenter, T extends TreeNode> extends
        BaseFragment<P> implements IBaseDetailView<T>, SwipeRefreshLayout.OnRefreshListener,
        IAdapterState, IOnItemMove<T> {

    /*明细界面的公共组件*/
    protected RecyclerView mRecyclerView;
    protected AutoSwipeRefreshLayout mSwipeRefreshLayout;
    protected HorizontalScrollView mHorizontalScroll;
    protected LinearLayout mExtraContainer;

    /*明细界面的公共适配器，给出父类，所以尽量将方法抽象在父类里面*/
    protected MultiItemTypeTreeAdapter<T> mAdapter;

    /*过账和数据上传的公共字段和信息*/
    /*第一步过账成功后返回的物料凭证以及第二步(上架，下架，转储)成功后返回的验收单号等需要展示的信息*/
    protected StringBuffer mShowMsg = new StringBuffer();
    /*缓存的抬头transId,用于上架处理*/
    protected String mTransId;
    /*底部提示菜单数据源*/
    protected List<BottomMenuEntity> mBottomMenus;
    /*数据上传01/05等业务，在开发后期需要增加的字段*/
    protected HashMap<String, Object> mExtraTansMap = new HashMap<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.base_detail_recycler_view);
        mSwipeRefreshLayout = (AutoSwipeRefreshLayout) mView.findViewById(R.id.base_detail_swipe_refresh_layout);
        mHorizontalScroll = (HorizontalScrollView) mView.findViewById(R.id.base_detail_horizontal_scroll);
        mExtraContainer = (LinearLayout) mView.findViewById(R.id.root_id);
        return mView;
    }

    /**
     * 初始化公共的组件，这里统一设置RecyclerView的基本配置；自动下拉刷新接口
     */
    @Override
    protected void initView() {
        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue_a700, R.color.red_a400,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager lm = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new FadeInDownAnimator());
    }

    /**
     * 这里将整个页面居中的目的是让用户看到下拉刷新动画，并且提示用户正在刷新明细数据。
     */
    @Override
    public void startAutoRefresh() {
        mSwipeRefreshLayout.postDelayed((() -> {
            mHorizontalScroll.scrollTo((int) (mExtraContainer.getWidth() / 2.0f - UiUtil.getScreenWidth(mActivity) / 2.0f), 0);
            mSwipeRefreshLayout.autoRefresh();
        }), 50);
    }

    /**
     * 响应自动下拉刷新动作，并且开始请求接口获取整单缓存
     */
    @Override
    public void onRefresh() {
        if (!checkTransStateBeforeRefresh()) {
            return;
        }
        //单据抬头id
        final String refCodeId = mRefData.refCodeId;
        //业务类型
        final String bizType = mRefData.bizType;
        //单据类型
        final String refType = mRefData.refType;
        //清除缓存id
        mTransId = "";
        //清除过账凭证
        mShowMsg.setLength(0);
        //开始获取整单缓存，注意由于在获取缓存之前进行了必要的检查，而这里不管是否有参考
        //都调用的是同一个接口，所以在具体的业务时，子类必须检验响应的参数。
        mPresenter.getTransferInfo(mRefData, refCodeId, bizType, refType,
                Global.USER_ID, mRefData.workId, mRefData.invId, mRefData.recWorkId, mRefData.recInvId);
    }

    /**
     * 保存明细节点的TransId
     */
    protected void saveTransId(List<RefDetailEntity> allNodes) {
        for (RefDetailEntity node : allNodes) {
            if (!TextUtils.isEmpty(node.transId)) {
                mTransId = node.transId;
                break;
            }
        }
    }

    /**
     * 关闭下拉刷新动画。提示是否获取单整单缓存的信息
     *
     * @param isSuccess
     * @param message
     */
    @Override
    public void setRefreshing(boolean isSuccess, String message) {
        //不论成功或者失败都应该关闭下拉加载动画
        showMessage(message);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 删除某条明细失败。
     *
     * @param message
     */
    @Override
    public void deleteNodeFail(String message) {
        showMessage("删除失败;" + message);
    }

    /**
     * 显示过账，数据上传等菜单对话框
     *
     * @param companyCode
     */
    @Override
    public void showOperationMenuOnDetail(final String companyCode) {
        if (mPresenter.isLocal()) {
            //如果是离线
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("温馨提示")
                    .setMessage("您是否要结束本次操作?")
                    .setPositiveButton("结束本次操作", (dialog, which) -> {
                        dialog.dismiss();
                        mPresenter.setTransFlag(mBizType, mTransId, "2");
                    }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
        } else {
            View rootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_bottom_menu, null);
            GridView menu = (GridView) rootView.findViewById(R.id.gd_menus);
            if (mBottomMenus == null)
                mBottomMenus = provideDefaultBottomMenu();
            BottomDialogMenuAdapter adapter = new BottomDialogMenuAdapter(mActivity, R.layout.item_dialog_bottom_menu, mBottomMenus);
            menu.setAdapter(adapter);

            final Dialog dialog = new Dialog(mActivity, R.style.MaterialDialogSheet);
            dialog.setContentView(rootView);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.show();

            menu.setOnItemClickListener((adapterView, view, position, id) -> {
                switch (position) {
                    case 0:
                        //1. 过账
                        submit2BarcodeSystem(mBottomMenus.get(position).transToSapFlag);
                        break;
                    case 1:
                        //2. 上架(下架)
                        submit2SAP(mBottomMenus.get(position).transToSapFlag);
                        break;
                    case 2:
                        //3. 转储
                        sapUpAndDownLocation(mBottomMenus.get(position).transToSapFlag);
                        break;
                }
                dialog.dismiss();
            });
        }
    }


    /**
     * 所有上传任务成功后记录物料凭证，以便显示。
     * 注意由于有可能即可能显示凭证信息还要显示
     * 错误信息，而且错误信息是一个列表，所以
     * 这里需要将凭证信息做成列表，以便统一显示。
     *
     * @param message
     */
    @Override
    public void saveMsgFowShow(String message) {
        adapterShowMsg(mShowMsg, message);
    }

    /**
     * 第一步失败，因为第一步可能涉及到图片，数据上传，01等步骤。
     *
     * @param message
     */
    @Override
    public void submitBarcodeSystemFail(String message) {
        //注意这里如果不涉及到01，那么错误消息不是列表，
        adapterShowMsg(mShowMsg, "错误消息列表:");
        adapterShowMsg(mShowMsg, message);
        showErrorDialog(mShowMsg);
        mShowMsg.setLength(0);
    }

    /**
     * 第二步上架失败显示失败信息
     *
     * @param messages
     */
    @Override
    public void submitSAPFail(String messages) {
        adapterShowMsg(mShowMsg, messages);
        showErrorDialog(messages);
        mShowMsg.setLength(0);
    }

    /**
     * 第三步转储失败显示失败信息
     *
     * @param messages
     */
    @Override
    public void upAndDownLocationFail(String messages) {
        adapterShowMsg(mShowMsg, messages);
        showErrorDialog(messages);
        mShowMsg.setLength(0);
    }

    @Override
    public void setTransFlagFail(String message) {
        showMessage(message);
    }

    @Override
    public void setTransFlagsComplete() {
        showMessage("结束本次操作成功!");
        startAutoRefresh();
    }

    /**
     * 重试赴过账和数据上传
     *
     * @param retryAction
     */
    @Override
    public void retry(String retryAction) {
        switch (retryAction) {
            case Global.RETRY_TRANSFER_DATA_ACTION:
                submit2BarcodeSystem(mBottomMenus.get(0).transToSapFlag);
                break;
            case Global.RETRY_UPLOAD_DATA_ACTION:
                submit2SAP(mBottomMenus.get(1).transToSapFlag);
                break;
            case Global.RETRY_SET_TRANS_FLAG_ACTION:
                mPresenter.setTransFlag(mBizType, mTransId, "2");
                break;
        }
        super.retry(retryAction);
    }

    /**
     * 当Fragment的视图销毁时接触明细适配器的接口,防止内存泄露
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.setAdapterStateListener(null);
            mAdapter.setOnItemClickListener(null);
            mAdapter.setOnItemEditAndDeleteListener(null);
        }
    }

    /**
     * 将消息组装，以便统一显示
     *
     * @param sb
     * @param message
     */
    protected void adapterShowMsg(StringBuffer sb, String message) {
        sb.append(message).append("\n").append("______");
    }

    @Override
    public void _onPause() {
        super._onPause();
    }

    /**
     * 子类根据自己的业务修改标准明细的抬头显示字段
     *
     * @param holder
     * @param viewType
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {

    }

    /**
     * 当明细界面需要至少两步来上传数据时，需要根据不同的业务需求返回是否已经过账。
     *
     * @return 返回false表示已经过账了，那么不在刷新明细页面
     */
    protected abstract boolean checkTransStateBeforeRefresh();

    /**
     * 第一步将数据上传到条码系统
     *
     * @param tranToSapFlag
     */
    protected abstract void submit2BarcodeSystem(String tranToSapFlag);

    /**
     * 第二步将数据上传到SAP系统
     *
     * @param tranToSapFlag
     */
    protected abstract void submit2SAP(String tranToSapFlag);

    /**
     * 第三步上架和下架
     *
     * @param transToSapFlag
     */
    protected abstract void sapUpAndDownLocation(String transToSapFlag);


}
