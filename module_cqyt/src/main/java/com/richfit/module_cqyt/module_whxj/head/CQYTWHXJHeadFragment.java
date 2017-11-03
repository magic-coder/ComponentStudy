package com.richfit.module_cqyt.module_whxj.head;

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
import android.widget.Spinner;
import android.widget.TextView;

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

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTWHXJHeadFragment extends BaseHeadFragment<CQYTWHXJHeadPresenterImp>
        implements CQYTWHXJHeadContract.View {

    RichEditText etXJDate;
    TextView tvXJPerson;
    /*天气*/
    EditText etInsWeather;
    /*温度*/
    EditText etInsTemperature;
    /*湿度*/
    EditText etInsHumidity;
    /*库房门窗是否正常、完好*/
    Spinner spInsDoor;
    /*库区地面是否干净整洁*/
    Spinner spInsClean;
    /*帐、卡、物数量是否一致*/
    Spinner spInsQuantity;
    /*化学品物资码垛是否合理、牢固、苫垫是否严密*/
    Spinner spInsChemical;
    /*消防设施（器材）、防护用具是否有效、消防通道是否通畅*/
    Spinner spInsFire;
    /*库区温度与湿度是否符合库存维护批储存要求*/
    Spinner spInsTH;
    /*危化品包装是否有破损或者渗漏等*/
    Spinner spInsHazardous;
    /*易燃化学物资封口是否严密，有无挥发或者渗漏、变质、溶化或者变色等现象*/
    Spinner spInsFlammable;
    /*化学品是否超过有效期*/
    Spinner spInsEffective;


    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_whxj_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CQYTWHXJHeadPresenterImp(mActivity);
    }


    @Override
    protected void initView() {
        etXJDate = mView.findViewById(R.id.cqyt_et_xj_date);
        tvXJPerson = mView.findViewById(R.id.cqyt_tv_xj_person);
        etInsWeather = mView.findViewById(R.id.cqyt_et_ins_weather);
        etInsTemperature = mView.findViewById(R.id.cqyt_et_ins_temperature);
        etInsHumidity = mView.findViewById(R.id.cqyt_et_ins_humidity);
        spInsDoor = mView.findViewById(R.id.cqyt_sp_ins_door);
        spInsClean = mView.findViewById(R.id.cqyt_sp_ins_clean);
        spInsQuantity = mView.findViewById(R.id.cqyt_sp_ins_quantity);
        spInsChemical = mView.findViewById(R.id.cqyt_sp_ins_chemical);
        spInsFire = mView.findViewById(R.id.cqyt_sp_ins_fire);
        spInsTH = mView.findViewById(R.id.cqyt_sp_ins_th);
        spInsHazardous = mView.findViewById(R.id.cqyt_sp_ins_hazardous);
        spInsFlammable = mView.findViewById(R.id.cqyt_sp_ins_flammable);
        spInsEffective = mView.findViewById(R.id.cqyt_sp_ins_effective);
    }

    @Override
    public void initEvent() {
        etXJDate.setOnRichEditTouchListener((view, text) ->
                DateChooseHelper.chooseDateForEditText(mActivity, etXJDate, Global.GLOBAL_DATE_PATTERN_TYPE1));
    }

    @Override
    public void initData() {
        etXJDate.setText(CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE3));
        tvXJPerson.setText(Global.LOGIN_ID);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsDoor, 0);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsClean, 0);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsQuantity, 0);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsChemical, 0);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsFire, 0);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsTH, 0);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsHazardous, 0);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsFlammable, 0);
        initSp(getStringArray(R.array.cqyt_xj_list2), spInsEffective, 0);

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


    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData == null) {
            mRefData = new ReferenceEntity();
        }
        //危化品
        mRefData.inspectionType = 2;
        /*巡检日期*/
        mRefData.voucherDate = getString(etXJDate);

        mRefData.insWeather = getString(etInsWeather);
        mRefData.insTemperature = getString(etInsTemperature);
        mRefData.insHumidity = getString(etInsHumidity);

        mRefData.insDoor = spInsDoor.getSelectedItemPosition();
        mRefData.insClean = spInsClean.getSelectedItemPosition();
        mRefData.insQuantity = spInsQuantity.getSelectedItemPosition();
        mRefData.insChemical = spInsChemical.getSelectedItemPosition();
        mRefData.insFire = spInsFire.getSelectedItemPosition();
        mRefData.insTh = spInsTH.getSelectedItemPosition();
        mRefData.insHazardous = spInsHazardous.getSelectedItemPosition();
        mRefData.insFlammable = spInsFlammable.getSelectedItemPosition();
        mRefData.insEffective = spInsEffective.getSelectedItemPosition();
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
        mapIns.put("insDoor",mRefData.insDoor);
        mapIns.put("insClean",mRefData.insClean);
        mapIns.put("insQuantity",mRefData.insQuantity);
        mapIns.put("insChemical",mRefData.insChemical);
        mapIns.put("insFire",mRefData.insFire);
        mapIns.put("insTh",mRefData.insTh);
        mapIns.put("insHazardous",mRefData.insHazardous);
        mapIns.put("insFlammable",mRefData.insFlammable);
        mapIns.put("insEffective",mRefData.insEffective);
        mapIns.put("insWeather", mRefData.insWeather);
        mapIns.put("insTemperature", mRefData.insTemperature);
        mapIns.put("insHumidity", mRefData.insHumidity);
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("mapIns", mapIns);
        extraMap.put("inspectionType", mRefData.inspectionType);
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
