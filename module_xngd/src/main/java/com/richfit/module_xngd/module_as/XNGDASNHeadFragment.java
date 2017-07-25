package com.richfit.module_xngd.module_as;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.widget.RichAutoEditText;
import com.richfit.data.constant.Global;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_as.imp.XNGDASNHeadPresenterImp;
import com.richfit.sdk_wzrk.base_asn_head.BaseASNHeadFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 库存类型和项目编号的逻辑是:
 * 如果库存类型选择项目物资，那么项目编号必输，在数据
 * 采集获取库存的时候，需要加入项目编号作为搜索维度。
 * 库存类型保存到specialInvFlag
 * Created by monday on 2017/6/23.
 */

public class XNGDASNHeadFragment extends BaseASNHeadFragment<XNGDASNHeadPresenterImp> {

    //库存类型
    Spinner spInvType;
    //项目编号
    RichAutoEditText etProjectNum;
    List<String> mAutoDatas;
    ArrayAdapter mAutoAdapter;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_asn_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new XNGDASNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initEvent() {
        //选择日期
        etTransferDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etTransferDate, Global.GLOBAL_DATE_PATTERN_TYPE1));

        //选择工厂，初始化项目编号
        RxAdapterView.itemSelections(spWork)
                .filter(position -> position.intValue() > 0)
                .subscribe(position -> mPresenter.getProjectNumList(mWorks.get(position).workCode,
                        getString(etProjectNum), 100, 0, mBizType));

        //点击自动提示控件，显示默认列表
        RxView.clicks(etProjectNum)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> mAutoDatas != null && mAutoDatas.size() > 0)
                .subscribe(a -> showAutoCompleteConfig(etProjectNum));

        //用户选择自动提示控件的某一条数据，隐藏输入法
        RxAutoCompleteTextView.itemClickEvents(etProjectNum)
                .subscribe(a -> hideKeyboard(etProjectNum));

        //修改自动提示控件，说明用户需要用关键字进行搜索，如果默认的列表中存在，那么不在向数据库进行查询
        RxTextView.textChanges(etProjectNum)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(str -> !TextUtils.isEmpty(str) && mAutoDatas != null && mAutoDatas.size() > 0
                        && !filterKeyWord(str))
                .subscribe(a -> mPresenter.getProjectNumList(mWorks.get(spWork.getSelectedItemPosition()).workCode,
                        getString(etProjectNum), 100, 0, mBizType));

        //删除缓存
        mPresenter.deleteCollectionData("", mBizType, Global.USER_ID, mCompanyCode);
    }

    @Override
    public void initView() {
        super.initView();
        llSupplier.setVisibility(View.GONE);
        spInvType = (Spinner) mView.findViewById(R.id.xngd_sp_inv_type);
        etProjectNum = (RichAutoEditText) mView.findViewById(R.id.xngd_et_project_num);
    }


    @Override
    public void initData() {
        super.initData();
        mAutoDatas = new ArrayList<>();
        //初始化库存类型，注意当用户选择项目物资的时候，项目编号必输
        ArrayList<String> items = new ArrayList<>();
        items.add("生产物资");
        items.add("项目物资");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, items);
        spInvType.setAdapter(adapter);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            //2017年07月25日将库存类型赋值给specialInvFlag。生产物资为N,项目物资为Q
            mRefData.specialInvFlag = spInvType.getSelectedItemPosition() == 0 ? "N" :
                    "Q";
            //注意使用编码作为项目编号的搜索条件
            mRefData.projectNum = getString(etProjectNum).split("_")[0];
        }
    }

    @Override
    public void showProjectNums(ArrayList<String> projectNums) {
        mAutoDatas.clear();
        mAutoDatas.addAll(projectNums);
        if (mAutoAdapter == null) {
            mAutoAdapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, mAutoDatas);
            etProjectNum.setAdapter(mAutoAdapter);
            setAutoCompleteConfig(etProjectNum);
        } else {
            mAutoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadProjectNumsFail(String message) {
        showMessage(message);
    }

    private boolean filterKeyWord(CharSequence keyWord) {
        Pattern pattern = Pattern.compile("^" + keyWord.toString().toUpperCase());
        for (String item : mAutoDatas) {
            Matcher matcher = pattern.matcher(item);
            while (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}
