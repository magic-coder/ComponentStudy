package com.richfit.module_xngd.module_cwtz;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.widget.RichAutoEditText;
import com.richfit.module_xngd.R;
import com.richfit.sdk_cwtz.head.LAHeadFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by monday on 2017/7/19.
 */

public class XNGDLAHeadFragment extends LAHeadFragment {

    //应急物资
    private CheckBox cbInvFlag;
    //项目移交物资
    private CheckBox cbInvType;
    //库存类型
    private Spinner spSpecialInvType;
    //项目编号
    private RichAutoEditText etProjectNum;

    List<String> mAutoDatas;
    ArrayAdapter mAutoAdapter;

    @Override
    protected int getContentId() {
        return R.layout.xngd_fragment_la_head;
    }

    @Override
    public void initEvent() {
        super.initEvent();
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
                .filter(str -> !TextUtils.isEmpty(str) && mAutoDatas != null && mAutoDatas.size() > 0 && !filterKeyWord(str))
                .subscribe(a -> mPresenter.getProjectNumList(mWorks.get(spWork.getSelectedItemPosition()).workCode,
                        getString(etProjectNum), 100, 0, mBizType));
    }

    @Override
    public void initView() {
        super.initView();
        //应急物资
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
        //项目移交物资
        cbInvType = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_type);
        //库存类型
        spSpecialInvType = (Spinner) mView.findViewById(R.id.xngd_sp_inv_type);
        //项目编号
        etProjectNum = (RichAutoEditText) mView.findViewById(R.id.xngd_et_project_num);
    }

    @Override
    public void initData() {
        super.initData();
        mAutoDatas = new ArrayList<>();
        //初始化库存类型，注意当用户选择项目物资的时候，项目文本必输
        ArrayList<String> items = new ArrayList<>();
        items.add("生产物资");
        items.add("项目物资");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, items);
        spSpecialInvType.setAdapter(adapter);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            //应急物资
            mRefData.invFlag = cbInvFlag.isChecked() ? "1" : "0";
            //项目移交物资
            mRefData.invType = cbInvType.isChecked() ? "0" : "1";
            //库存类型
            mRefData.specialInvFlag = spSpecialInvType.getSelectedItemPosition() == 0 ? "N" :
                    "Q";
            //项目编号
            String projectNum = getString(etProjectNum);
            if(!TextUtils.isEmpty(projectNum)) {
                mRefData.projectNum = projectNum.split("_")[0];
            }
        }
    }

    /**
     * 库存地点加载完毕
     */
    @Override
    public void loadInvsComplete() {
        //不能够选择完工厂后直接记载库存地点和项目编号，因为并发范围数据出现异常
        mPresenter.getProjectNumList(mWorks.get(spWork.getSelectedItemPosition()).workCode,
                getString(etProjectNum), 100, 0, mBizType);
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
