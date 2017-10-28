package com.richfit.sdk_wzck.base_dsn_head;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_adapter.WorkAdapter;
import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_wzck.R;
import com.richfit.sdk_wzck.R2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * 青海201,221(对应的bizType是26,27)无参考出库。201和221的
 * 区别在于移动类型不同，系统根据BizType自动生成供应商(26)或者
 * 项目编号(27)。
 * 2017年8月1日增加总账科目和订单两类基础数据
 * Created by monday on 2017/2/23.
 */

public abstract class BaseDSNHeadFragment<P extends IDSNHeadPresenter> extends BaseHeadFragment<P>
        implements IDSNHeadView {


    @BindView(R2.id.sp_work)
    protected Spinner spWork;
    @BindView(R2.id.ll_auto_comp)
    protected LinearLayout llAutoComp;
    @BindView(R2.id.et_auto_comp)
    protected AutoCompleteTextView etAutoComp;
    @BindView(R2.id.tv_auto_comp_name)
    protected TextView tvAutoCompName;
    @BindView(R2.id.et_transfer_date)
    protected   RichEditText etTransferDate;

    WorkAdapter mWorkAdapter;
    protected List<WorkEntity> mWorks;

    protected List<String> mAutoDatas;
    protected ArrayAdapter mAutoAdapter;


    @Override
    protected int getContentId() {
        return R.layout.wzck_fragment_base_dsn_header;
    }

    @Override
    public void initVariable(Bundle savedInstanceState) {
        mRefData = null;
        mWorks = new ArrayList<>();
    }

    /**
     * 注册点击事件
     */
    @Override
    public void initEvent() {
        //过账日期
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        //选择工厂，初始化供应商或者项目编号
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getAutoComList(mWorks.get(position).workCode,null,
                        getString(etAutoComp), 100, getOrgFlag(), mBizType,getAutoComDataType()));

        //点击自动提示控件，显示默认列表
        RxView.clicks(etAutoComp)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> mAutoDatas != null && mAutoDatas.size() > 0)
                .subscribe(a -> showAutoCompleteConfig(etAutoComp));

        //用户选择自动提示控件的某一条数据，隐藏输入法
        RxAutoCompleteTextView.itemClickEvents(etAutoComp)
                .subscribe(a -> hideKeyboard(etAutoComp));

        //修改自动提示控件，说明用户需要用关键字进行搜索，如果默认的列表中存在，那么不在向数据库进行查询
        RxTextView.textChanges(etAutoComp)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(str -> !TextUtils.isEmpty(str) && mAutoDatas != null &&
                        mAutoDatas.size() > 0 && !filterKeyWord(str,mAutoDatas) && spWork.getSelectedItemPosition() > 0)
                .subscribe(a -> mPresenter.getAutoComList(mWorks.get(spWork.getSelectedItemPosition()).workCode,null,
                        getString(etAutoComp), 100, getOrgFlag(), mBizType,getAutoComDataType()));

        //如果是离线直接获取缓存，不能让用户删除缓存
        if (mUploadMsgEntity != null && mPresenter != null && mPresenter.isLocal())
            return;

        //删除历史数据
        mPresenter.deleteCollectionData("", mBizType, Global.USER_ID, mCompanyCode);
    }


    @Override
    public void initData() {
        SPrefUtil.saveData(mBizType, "0");
        etTransferDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE1));
        //获取发出工厂列表
        mPresenter.getWorks(getOrgFlag());
    }

    @Override
    public void showWorks(List<WorkEntity> works) {
        mWorks.clear();
        mWorks.addAll(works);
        //绑定适配器
        if (mWorkAdapter == null) {
            mWorkAdapter = new WorkAdapter(mActivity, R.layout.item_simple_sp, mWorks);
            spWork.setAdapter(mWorkAdapter);
        } else {
            mWorkAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadWorksFail(String message) {
        showMessage(message);
    }

    @Override
    public void showAutoCompleteList(Map<String,List<SimpleEntity>> map) {
        List<SimpleEntity> simpleEntities = map.get(getAutoComDataType());
        if(simpleEntities == null || simpleEntities.size() == 0)
            return;
        if(mAutoDatas == null) {
            mAutoDatas = new ArrayList<>();
        }
        List<String> list = CommonUtil.toStringArray(simpleEntities,true);
        mAutoDatas.clear();
        mAutoDatas.addAll(list);
        if (mAutoAdapter == null) {
            mAutoAdapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, mAutoDatas);
            etAutoComp.setAdapter(mAutoAdapter);
            setAutoCompleteConfig(etAutoComp);
        } else {
            mAutoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadAutoCompleteFail(String message) {
        hideKeyboard(etAutoComp);
        showFailDialog(message);
    }

    @Override
    public void deleteCacheSuccess(String message) {
        showMessage(message);
    }

    @Override
    public void deleteCacheFail(String message) {
        showMessage(message);
    }

    /**
     * 如果用户输入的关键字在mLocationList存在，那么不在进行数据查询.
     *
     * @param keyWord
     * @return
     */
    protected boolean filterKeyWord(CharSequence keyWord,List<String> data) {
        Pattern pattern = Pattern.compile("^" + keyWord.toString().toUpperCase());
        for (String item : data) {
            Matcher matcher = pattern.matcher(item);
            while (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (spWork.getAdapter() != null && spWork.getSelectedItemPosition() > 0) {
            if (mRefData == null) {
                mRefData = new ReferenceEntity();
            }
            final int position = spWork.getSelectedItemPosition();
            mRefData.workId = mWorks.get(position).workId;
            mRefData.workCode = mWorks.get(position).workCode;
            mRefData.workName = mWorks.get(position).workName;
            mRefData.bizType = mBizType;
            mRefData.voucherDate = getString(etTransferDate);
        }
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {

    }

    /**
     * 返回组织机构flag，0表示ERP的组织机构；1表示二级单位的组织机构
     *
     * @return
     */
    protected abstract int getOrgFlag();

    /**
     * 返回autoCom控件需要加载的数据类型
     * @return
     */
    protected abstract String getAutoComDataType();
}
