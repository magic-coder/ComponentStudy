package com.richfit.module_cqyt.module_xj.head;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadFragment;
import com.richfit.common_lib.utils.DateChooseHelper;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzys.camera.TakephotoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTXJHeadFragment extends BaseHeadFragment<CQYTXJHeadPresenterImp>
        implements CQYTXJHeadContract.View {

    RichEditText etXJDate;
    TextView tvXJPerson;
    RadioButton rbInsBefore;
    RadioButton rbInsAfter;
//    //巡检货位
//    RichEditText etInsLocation;
//    //巡检路径
//    TextView tvInsPath;
    //班前检查
    LinearLayout llXjInsBefore;
    Spinner spInsDoor;
    Spinner spInsMaterial;
    Spinner spInsEquipe;
    Spinner spInsLocation;
    Spinner spInsSafe;
    //班后检查
    LinearLayout llXjInsAfter;
    Spinner spInsDocument;
    Spinner spInsOffice;
    Spinner spInsException;
    Spinner spInsPower;
    Spinner spInsLock;

    EditText etRemark;

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_xj_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CQYTXJHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etXJDate = (RichEditText) mView.findViewById(R.id.cqyt_et_xj_date);
        tvXJPerson = (TextView) mView.findViewById(R.id.cqyt_tv_xj_person);
        rbInsBefore = (RadioButton) mView.findViewById(R.id.cqyt_rb_ins_before);
        rbInsAfter = (RadioButton) mView.findViewById(R.id.cqyt_rb_ins_after);
//        etInsLocation = (RichEditText) mView.findViewById(R.id.cqyt_et_ins_location);
//        tvInsPath = (TextView) mView.findViewById(R.id.cqyt_tv_ins_path);
        etRemark = (EditText) mView.findViewById(R.id.et_remark);

        //班前
        llXjInsBefore = (LinearLayout) mView.findViewById(R.id.cqyt_ll_xj_ins_before);
        spInsDoor = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_door);
        spInsMaterial = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_material);
        spInsEquipe = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_equipe);
        spInsLocation = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_location);
        spInsSafe = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_safe);

        //班后
        llXjInsAfter = (LinearLayout) mView.findViewById(R.id.cqyt_ll_xj_ins_after);
        spInsDocument = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_document);
        spInsOffice = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_office);
        spInsException = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_exception);
        spInsPower = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_power);
        spInsLock = (Spinner) mView.findViewById(R.id.cqyt_sp_ins_lock);
    }

    @Override
    public void initEvent() {
        //班前
        RxView.clicks(rbInsBefore)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(a -> {
                    resetUI();
                    //注意这里必须清空
                    mRefData = null;
                    rbInsBefore.setChecked(true);
                    llXjInsBefore.setVisibility(View.VISIBLE);
                });


        //班后
        RxView.clicks(rbInsAfter)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(a -> {
                    resetUI();
                    mRefData = null;
                    rbInsAfter.setChecked(true);
                    llXjInsAfter.setVisibility(View.VISIBLE);
                });
        etXJDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etXJDate, Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    public void initData() {
        etXJDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE3));
        tvXJPerson.setText(Global.LOGIN_ID);
        //班前
        //库区、办公室门窗有无启动(有无,有)
        initSp(getStringArray(R.array.cqyt_xj_list1), spInsDoor, 0);
        //物资有无异状(有无,无)
        initSp(getStringArray(R.array.cqyt_xj_list1), spInsMaterial, 1);
        //消防器材、苫垫、标牌是否完好(是否，是)
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsEquipe, 0);
        //货架、场地是否整洁(是否，是)
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsLocation, 0);
        //有无不安全因素(有无,无)
        initSp(getStringArray(R.array.cqyt_xj_list1), spInsSafe, 1);

        //班后
        //资料是否整理齐全(是否,是)
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsDocument, 0);
        //办公用品、工具是否归位(是否,是)
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsOffice, 0);
        //物资有无异动(有无，无)
        initSp(getStringArray(R.array.cqyt_xj_list1), spInsException, 1);
        //电源是否关闭(是否，是)
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsPower, 0);
        //库区、办公室门窗是否关严锁好(是否，是)
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsLock, 0);
    }

    private void initSp(List<String> dataSet, Spinner sp, int defaultSelectedPos) {
        if (dataSet == null || dataSet.size() == 0 || defaultSelectedPos < 0)
            return;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, dataSet);
        sp.setAdapter(adapter);
        sp.setSelection(defaultSelectedPos);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {

    }


    /**
     * 重置所有的Button背景
     */
    private void resetUI() {
        rbInsBefore.setChecked(false);
        rbInsAfter.setChecked(false);
        llXjInsBefore.setVisibility(View.INVISIBLE);
        llXjInsAfter.setVisibility(View.INVISIBLE);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData == null) {
            mRefData = new ReferenceEntity();
        }
        mRefData.inspectionType = rbInsBefore.isChecked() ? 0 : 1;
        mRefData.voucherDate = getString(etXJDate);
        mRefData.remark = getString(etRemark);
        if (rbInsBefore.isChecked()) {
            //班前
            mRefData.insDoor = spInsDoor.getSelectedItemPosition();
            mRefData.insMaterial = spInsMaterial.getSelectedItemPosition();
            mRefData.insEquipe = spInsEquipe.getSelectedItemPosition();
            mRefData.insLocation = spInsLocation.getSelectedItemPosition();
            mRefData.insSafe = spInsSafe.getSelectedItemPosition();
        }
        if (rbInsAfter.isChecked()) {
            mRefData.insDocument = spInsDocument.getSelectedItemPosition();
            mRefData.insOffice = spInsOffice.getSelectedItemPosition();
            mRefData.insException = spInsException.getSelectedItemPosition();
            mRefData.insPower = spInsPower.getSelectedItemPosition();
            mRefData.insLock = spInsLock.getSelectedItemPosition();
        }
    }

    /**
     * 显示开始盘点和重新盘点两个菜单
     *
     * @param companyCode
     */
    @Override
    public void operationOnHeader(String companyCode) {

        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_bottom_menu, null);
        GridView menu = (GridView) rootView.findViewById(R.id.gd_menus);
        final List<BottomMenuEntity> menus = provideDefaultBottomMenu();
        BottomDialogMenuAdapter adapter = new BottomDialogMenuAdapter(mActivity, R.layout.item_dialog_bottom_menu, menus);
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
                    //1.巡检拍照
                    takePhoto(menus.get(position).takePhotoType);
                    break;
                case 1:
                    //2.结束本次巡检,提交照片，修改缓存标识
                    closeXJ();
                    break;
            }
            dialog.dismiss();
        });
    }

    /**
     * 选件拍照
     */
    private void takePhoto(int takePhotoType) {
        _onPause();
        Intent intent = new Intent(mActivity, TakephotoActivity.class);
        Bundle bundle = new Bundle();

        //拍照类型
        bundle.putInt(Global.EXTRA_TAKE_PHOTO_TYPE, takePhotoType);
        //单据号
        bundle.putString(Global.EXTRA_REF_NUM_KEY, String.valueOf(mRefData.inspectionType));

        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, mBizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, mRefType);
        //行号
        bundle.putString(Global.EXTRA_REF_LINE_NUM_KEY, Global.USER_ID);
        //行id
        bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, Global.USER_ID);
        bundle.putBoolean(Global.EXTRA_IS_LOCAL_KEY, mPresenter.isLocal());
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    /**
     * 结束本次巡检
     */
    private void closeXJ() {
        _onPause();
        Map<String, Object> mapIns = new HashMap<>();
        if (0 == mRefData.inspectionType) {
            //班前
            mapIns.put("insDoor", mRefData.insDoor);
            mapIns.put("insMaterial", mRefData.insMaterial);
            mapIns.put("insEquipe", mRefData.insEquipe);
            mapIns.put("insLocation", mRefData.insLocation);
            mapIns.put("insSafe", mRefData.insSafe);
            mapIns.put("remark", mRefData.remark);
        }
        if (1 == mRefData.inspectionType) {
            //班后
            mapIns.put("insDocument", mRefData.insDocument);
            mapIns.put("insOffice", mRefData.insOffice);
            mapIns.put("insException", mRefData.insException);
            mapIns.put("insPower", mRefData.insPower);
            mapIns.put("insLock", mRefData.insLock);
            mapIns.put("remark", mRefData.remark);
        }
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("mapIns",mapIns);
        extraMap.put("inspectionType",mRefData.inspectionType);
        mPresenter.transferCollectionData(String.valueOf(mRefData.inspectionType), mRefData.refCodeId,
                mRefData.transId, mBizType, mRefType, mRefData.inspectionType, Global.USER_ID, false,
                mRefData.voucherDate, "", extraMap);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = new ArrayList<>();

        BottomMenuEntity menu = new BottomMenuEntity();
        menu.menuName = "巡检拍照";
        menu.takePhotoType = 4;
        menu.menuImageRes = R.mipmap.icon_take_photo4;
        menus.add(menu);

        menu = new BottomMenuEntity();
        menu.menuName = "结束本次巡检";
        menu.menuImageRes = R.mipmap.icon_complete;
        menus.add(menu);
        return menus;
    }


    @Override
    public boolean isNeedShowFloatingButton() {
        return true;
    }

    @Override
    public void closeComplete() {
        showMessage("本次巡检结束");
    }

    @Override
    public void closeFail(String message) {
        showMessage(message);
    }
}
